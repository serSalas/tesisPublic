package main.java.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;

/**
 * Esta clase, es una estrategia de metodo de extracción.
 * En este caso se efectua extracción de datos por Recovery.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class ExtraerPorRecovery extends ExtractorDatos 
{
	private static int estadoExtraccionRecovery;
	private IDevice disp;
	private String pathOrigen;
	private String pathDestino;
	
	public ExtraerPorRecovery(Dispositivo dispositivo, UnidadAlmacenamiento unidadAlmacenamiento, boolean isFlagHashMd5, boolean isFlagHashSha256) 
	{
		super(dispositivo, unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
		estadoExtraccionRecovery = 0;
		pathOrigen = "";
		pathDestino = "";
	}

	/**
     * Este método, realiza extracción de datos de dispositivo Android conectado a lugar de unidad de almacenamiento,
     * utilizando recovery personalizado.
     * El método cuenta con 10 estados.
     * Estado 0:
     * Chequea que dispositivo Android detectado este conectado a PC y en modo bootloader, mediante herramienta Heimdall.
     * Estado 1:
     * Chequea que en unidad de almacenamiento seleccionada se encuentre con suficiente memoria.
     * Es decir, debe poseer mayor cantidad de memoria libre que lo que ocupa partición o almacenamiento
     * de dispositivo Android detectado. En caso que no posea suficiente memoria unidad de almacenamiento
     * se notifica con mensaje de error.
     * Estado 2:
     * Se flashea dispositivo Android detectado, mediante herramienta Heimdall, recovery personalizado asociado a
     * dicho dispositivo y luego se reinicia dispositivo.
     * Estado 3:
     * Chequea que dispositivo Android detectado este conectado a PC, en modo recovery y rooteado.
     * Estado 4:
     * Se extrae partición /data de dispositivo Android detectado mediante protocolo ADB. Registrando fecha de inicio,
     * fecha de finalización y tiempo de duración de extracción realizada.
     * Estado 5:
     * Chequea que dispositivo Android detectado este conectado a PC y en modo bootloader, mediante herramienta Heimdall.
     * Estado 6:
     * Se flashea nuevamente dispositivo Android detectado, mediante herramienta Heimdall, recovery de fabrica asociado a
     * dicho dispositivo y luego se reinicia dispositivo.
     * Estado 7:
     * Se realiza el o los hash a partir de extracción de datos efectuada. Dando como resultado uno o dos archivos .md5 y/o .sha256.
     * Estado 8:
     * Se realiza reporte de extracción de datos.
     * Estado -1:
     * Cuando ocurre una excepción o espacio de almacenamiento es insuficiente en unidad de almacenamiento.
     * Además, se registra el error de excepción en variable mensajeError.
     */
	@Override
	public void extraerDatos() 
	{
		try
		{
			switch(estadoExtraccionRecovery)
			{
				case 0:
				{
					//SE ENCUENTRA EN MODO BOOTLOADER
					Process p = Runtime.getRuntime().exec("heimdall detect");
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String s;
					s = stdInput.readLine();
					if(s!=null)
					{
						estadoExtraccionRecovery = 1;
						System.out.println(s);
					}
					else
					{
						estadoExtraccionRecovery = 0;
					}
					break;
				}
				case 1:
				{
					if(unidadAlmacenamiento.chequearTamanioLibre(dispositivo.getTamanioParticion()))
					{
						estadoExtraccionRecovery = 2;
					}
					else
					{
						mensajeError = "La unidad de almacenamiento seleccionada no dispone de suficiente espacio de memoria. "
								+ "Se necesitan mas de " 
								+ unidadAlmacenamiento.getTamanioFaltante(dispositivo.getTamanioParticion()) 
								+ " en la unidad de almacenamiento para guardar la extraccion de datos";
						estadoExtraccionRecovery = -1;
					}
					break;
				}
				case 2:
				{
					if(dispositivo.getNumeroCompilacion()=="MMB29T.G532MUMU1ASH2") 
					{
						//FLASHEAR RECOVERY PERSONALIZADO
						Process p = Runtime.getRuntime().exec("heimdall flash --recovery " + dispositivo.getRutaRecoveryPersonalizado());
						BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
						StringBuffer stringBuffer = new StringBuffer(); 
						String s;
						s = stdInput.readLine();
					
						while(s!=null)
						{
							stringBuffer.append(s);
							stringBuffer.append("\n");
							s = stdInput.readLine();
							
							estadoExtraccionRecovery = 3;
						}
						System.out.println(stringBuffer.toString());
						break;	
					}
					else
					{
					//FLASHEAR RECOVERY PERSONALIZADO
					Process p = Runtime.getRuntime().exec("heimdall flash --RECOVERY " + dispositivo.getRutaRecoveryPersonalizado());
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					StringBuffer stringBuffer = new StringBuffer(); 
					String s;
					s = stdInput.readLine();
					
					while(s!=null)
					{
						stringBuffer.append(s);
						stringBuffer.append("\n");
						s = stdInput.readLine();
						
						estadoExtraccionRecovery = 3;
					}
					System.out.println(stringBuffer.toString());
					break;
					}
				}
				case 3:
				{
					//SE ENCUENTRA EN MODO RECOVERY?
					AndroidDebugBridge adb;
					AndroidDebugBridge.init(false);
					adb = AndroidDebugBridge.createBridge();
					
					Thread.sleep(1000);
					
					if (!adb.isConnected()) 
			        {
			            System.out.println("No se pudo conectar al servidor ADB");
			        }
					else
					{
						if(adb.getDevices().length > 0)
						{
							for(IDevice device : adb.getDevices()) 
							{	
								if(dispositivo.getFabricante().equals(Propiedades.getPropiedades("ro.product.manufacturer")) 
								&& dispositivo.getModelo().equals(Propiedades.getPropiedades("ro.product.model"))
								&& dispositivo.getVersionSo().equals(Propiedades.getPropiedades("ro.build.version.release"))
								&& dispositivo.getNumeroCompilacion().equals(Propiedades.getPropiedades("ro.build.display.id")))
								{
									//if(device.isRoot())
									//{
										disp = device;
										estadoExtraccionRecovery = 4;

									//}
									break;
								}
							}
						}
					}
					
					AndroidDebugBridge.disconnectBridge();
					AndroidDebugBridge.terminate();
					break;
				}
				case 4:
				{
					formatoRuta("Extraccion_Fisica");
					unidadAlmacenamiento.crearDirectorioRutaDestino(getFormatoRuta());
					
					pathOrigen = dispositivo.getRutaParticion();
					pathDestino = unidadAlmacenamiento.getRutaDestino() + "/" + getFormatoRuta() + ".img"; 
					
					setFechaInicioExtraccion();
					disp.pullFile(pathOrigen, pathDestino);
					setFechaFinalExtraccion();
					setDuracionExtraccion();
					
					estadoExtraccionRecovery = 5;
					break;
				}
				case 5:
				{
					//SE ENCUENTRA EN MODO BOOTLOADER
					Process p = Runtime.getRuntime().exec("heimdall detect");
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String s;
					s = stdInput.readLine();
					if(s!=null)
					{
						estadoExtraccionRecovery = 6;
						System.out.println(s);
					}
					break;
				}
				case 6:
				{
					if(dispositivo.getNumeroCompilacion()=="MMB29T.G532MUMU1ASH2") 
					{
						//FLASHEAR RECOVERY PERSONALIZADO
						Process p = Runtime.getRuntime().exec("heimdall flash --recovery " + dispositivo.getRutaRecoveryFabrica());
						BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
						StringBuffer stringBuffer = new StringBuffer(); 
						String s;
						s = stdInput.readLine();
					
						while(s!=null)
						{
							stringBuffer.append(s);
							stringBuffer.append("\n");
							s = stdInput.readLine();
							
							estadoExtraccionRecovery = 7;
						}
						System.out.println(stringBuffer.toString());
						System.out.println("Se realizo extraccion de datos utilizando metodo Recovery.");
						break;	
					}
					else
					{					
					//FLASHEAR RECOVERY FABRICA
					Process p = Runtime.getRuntime().exec("heimdall flash --RECOVERY " + dispositivo.getRutaRecoveryFabrica());
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					StringBuffer stringBuffer = new StringBuffer(); 
					String s;
					s = stdInput.readLine();
					
					while(s!=null)
					{
						stringBuffer.append(s);
						stringBuffer.append("\n");
						s = stdInput.readLine();
						
						estadoExtraccionRecovery = 7;
					}
					System.out.println(stringBuffer.toString());
					System.out.println("Se realizo extraccion de datos utilizando metodo Recovery.");
					break;
					}
				}
				case 7:
				{
					hash = new Hash(unidadAlmacenamiento, isFlagHashMd5,isFlagHashSha256);
					if(hash.generarHash(getFormatoRuta()))
					{
						estadoExtraccionRecovery = 8;
					}
					else
					{
						mensajeError = "No se pudo generar código/s hash.";
						estadoExtraccionRecovery = -1;
					}
					break;
				}
				case 8:
				{
					nombreMetodoExtraccion = "Metodo Recovery";
					reporte = new Reporte(dispositivo, unidadAlmacenamiento, hash, this);
					reporte = new Reporte(dispositivo, unidadAlmacenamiento, hash, this);
					if(reporte.generarReporte())
					{
						estadoExtraccionRecovery = 9;
					}
					else
					{
						mensajeError = "No se pudo generar reporte.";
						estadoExtraccionRecovery = -1;
					}
					break;
				}
			}
		}
		catch(IOException | InterruptedException | TimeoutException | AdbCommandRejectedException | /*ShellCommandUnresponsiveException |*/SyncException e)
		{
			mensajeError = e.getMessage();
			estadoExtraccionRecovery = -1;
		} 
	}
	
	/**
     * Esta función, retorna porcentaje de extracción de datos física.
     * Necesita de instancia dispositivo Android detectado para conocer tamaño en bytes
     * de partición. Y teniendo tamaño en bytes parcial de fichero destino
     * se calcula porcentaje. Tamaño Parcial/Tamaño Total x 100.
     * @return porcentajeExtraccionDatos Variable long que contiene porcentaje de extracción
     * de datos.
     */
	@Override
	public long getPorcentajeExtraccionDatos() 
	{
		long porcentajeExtraccionDatos = 0;
		long tamanioOrigen = dispositivo.getTamanioParticion();
		long tamanioDestino = unidadAlmacenamiento.getTamanioLibre();
		
		if(tamanioOrigen == 0)
		{
			porcentajeExtraccionDatos = 100;
		}
		else
		{
			porcentajeExtraccionDatos = (tamanioDestino*100)/tamanioOrigen;
		}
		
		return porcentajeExtraccionDatos;
	}
	
	/**
     * Esta función, retorna tamaño parcial de extracción de datos física convertido en formato
     * legible para humano.
     * Necesita de instancia dispositivo Android detectado para conocer tamaño en bytes
     * de partición. Y teniendo tamaño en bytes parcial de ruta destino de unidad de almacenamiento
     * se concatena ambos tamaños quedando de la siguiente forma. Tamaño Parcial de Tamaño Total.
     * Por ejemplo: 200,15 MB de 5,19 GB.
     * @return tamanioLegibleParcial Variable String que contiene tamaño parcial de extracción de datos
     * convertido en formato legible para humano.
     */
	@Override
	public String getTamanioParcialExtraccionDatos() 
	{
		//String tamanioLegibleOrigen = FileUtils.byteCountToDisplaySize(dispositivo.getTamanioParticion()).toString();
		//String tamanioLegibleDestino = FileUtils.byteCountToDisplaySize(this.getTamanioFicheroDestino()).toString();
		String tamanioLegibleOrigen = byteCountToDisplaySize(dispositivo.getTamanioParticion()).toString();
		String tamanioLegibleDestino = byteCountToDisplaySize(unidadAlmacenamiento.getTamanioLibre()).toString();
		String tamanioLegibleParcial = tamanioLegibleDestino + " de " + tamanioLegibleOrigen;
				
		return tamanioLegibleParcial;
	}
	
	/**
     * Esta función, retorna estado de extracción de datos.
     * Que permite ser consultada ya sea por modelo o por controladores.
     * @return estadoExtraccionRecovery Variable entera.
     */
	public int getEstadoExtraccionDatos()
	{
		return estadoExtraccionRecovery;
	}
	
}

