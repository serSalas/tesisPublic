package main.java.model;

import static com.android.ddmlib.FileListingService.TYPE_DIRECTORY;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.DdmPreferences;
import com.android.ddmlib.FileListingService;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.SyncService;
import com.android.ddmlib.TimeoutException;
import com.android.ddmlib.FileListingService.FileEntry;
import com.android.ddmlib.SyncService.ISyncProgressMonitor;

/**
 * Esta clase, es una estrategia de metodo de extracción.
 * En este caso se efectua extracción de datos por ADB.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class ExtraerPorAdb extends ExtractorDatos 
{
	private static int estadoExtraccionAdb;
	private IDevice disp;
	
	private long tamañoDirectorioExtraccionLogicaAdb;
	private long totalTamañoDirectorio;
	private FileEntry internalDir0;
	private FileEntry internalDir1;
	
	public ExtraerPorAdb(Dispositivo dispositivo, UnidadAlmacenamiento unidadAlmacenamiento, boolean isFlagHashMd5, boolean isFlagHashSha256) 
	{
		super(dispositivo, unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
		estadoExtraccionAdb = 0;
		tamañoDirectorioExtraccionLogicaAdb = 0;
		totalTamañoDirectorio = 0;
	}
	
	/**
     * Este método, realiza extracción de datos utilizando protocolo ADB.
     * El método cuenta con 7 estados.
     * Estado 0:
     * Chequea que dispositivo Android detectado este conectado a PC y en modo depuración.
     * Estado 1:
     * Calcula tamaño de almacenamiento de dispositivo Android detectado.
     * Estado 2:
     * Chequea que en unidad de almacenamiento seleccionada se encuentre con suficiente memoria.
     * Es decir, debe poseer mayor cantidad de memoria libre que lo que ocupa partición o almacenamiento
     * de dispositivo Android detectado. En caso que no posea suficiente memoria unidad de almacenamiento
     * se notifica con mensaje de error.
     * Estado 3:
     * Se copia todos archivos visibles de dispositivo Android detectado mediante protocolo ADB. Registrando fecha de inicio,
     * fecha de finalización y tiempo de duración de extracción realizada.
     * Estado 4:
     * Se realiza el o los hash a partir de extracción de datos efectuada. Dando como resultado uno o dos archivos .md5 y/o .sha256.
     * Estado 5:
     * Se realiza reporte de extracción de datos.
     * Estado -1:
     * Cuando ocurre excepción o espacio de almacenamiento es insuficiente en unidad de almacenamiento.
     * Además, se registra el error de excepción en variable mensajeError.
     */
	
	@Override
	public void extraerDatos() 
	{
		try
		{
			switch(estadoExtraccionAdb)
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
								if(dispositivo.getFabricante().equals(device.getProperty("ro.product.manufacturer")) 
								&& dispositivo.getModelo().equals(device.getProperty("ro.product.model"))
								&& dispositivo.getVersionSo().equals(device.getProperty("ro.build.version.release"))
								&& dispositivo.getNumeroCompilacion().equals(device.getProperty("ro.build.display.id")))
								{
									if(device.getState().toString().equals("ONLINE"))
									{
										disp = device;
										estadoExtraccionAdb = 1;
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
					internalDir0 = getDirectoryOnInternalStorage(dispositivo.getRutaSdcard0());
					internalDir1 = getDirectoryOnInternalStorage(dispositivo.getRutaSdcard1());
					
					tamañoDirectorioExtraccionLogicaAdb = tamañoDirectorio(internalDir0) + tamañoDirectorio(internalDir1);
					
					dispositivo.setTamanioLogico(tamañoDirectorioExtraccionLogicaAdb);
					totalTamañoDirectorio = 0;
					estadoExtraccionAdb = 2;
					break;
				}
				case 2:
				{
					if(unidadAlmacenamiento.chequearTamanioLibre(dispositivo.getTamanioLogico()))
					{
						formatoRuta("Extraccion_Logica");
						unidadAlmacenamiento.crearDirectorioRutaDestino(getFormatoRuta());
						
						estadoExtraccionAdb = 3;
					}
					else
					{
						mensajeError = "La unidad de almacenamiento seleccionada no dispone de suficiente espacio de memoria. "
								+ "Se necesitan mas de " 
								+ unidadAlmacenamiento.getTamanioFaltante(dispositivo.getTamanioLogico()) 
								+ " en la unidad de almacenamiento para guardar la extraccion de datos";
						estadoExtraccionAdb = -1;
						tamañoDirectorioExtraccionLogicaAdb = 0;
					}
					break;
				}
				case 3:
				{
					setFechaInicioExtraccion();
		        	ISyncProgressMonitor monitor = SyncService.getNullProgressMonitor();
		            SyncService sync = disp.getSyncService();
					DdmPreferences.setTimeOut(24000);
					sync.pull(new FileEntry[]{internalDir0},unidadAlmacenamiento.getRutaDestino(), monitor);
					sync.pull(new FileEntry[]{internalDir1}, unidadAlmacenamiento.getRutaDestino(), monitor);
		            setFechaFinalExtraccion();
					setDuracionExtraccion();
					estadoExtraccionAdb = 4;
					System.out.println("Se realizo extraccion de datos utilizando metodo ADB.");
					break;
				}
				case 4:
				{
					hash = new Hash(unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
					if(hash.generarHash(getFormatoRuta()))
					{
						estadoExtraccionAdb = 5;
					}
					else
					{
						mensajeError = "No se pudo generar código/s hash.";
						estadoExtraccionAdb = -1;
					}
					break;
				}
				case 5:
				{
					nombreMetodoExtraccion = "Metodo Adb";
					reporte = new Reporte(dispositivo, unidadAlmacenamiento, hash, this);
					if(reporte.generarReporte())
					{
						estadoExtraccionAdb = 6;
					}
					else
					{
						mensajeError = "No se pudo generar reporte.";
						estadoExtraccionAdb = -1;
					}
					tamañoDirectorioExtraccionLogicaAdb = 0;
					break;
				}
			}
		}
		catch(SyncException | InterruptedException | IOException | TimeoutException | AdbCommandRejectedException e)
		{
			mensajeError = e.getMessage();
			estadoExtraccionAdb = -1;
			tamañoDirectorioExtraccionLogicaAdb = 0;
		}
	}
	
	/**
	 * Esta funcion, retorna tamaño de almacenamiento total calculado mediante protocolo ADB.
	 * @param internalDir
	 * @param dispositivo Instancia de dispositivo Android detectado
	 * @return totalTamañoDirectorio Variable long que contiene tamaño de almacenamiento total 
	 * de dispositivo Android detectado calculado mediante protocolo ADB.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private long tamañoDirectorio(FileEntry internalDir) throws IOException, InterruptedException 
	{
		Process p = Runtime.getRuntime().exec("adb devices"); //Para iniciar el servidor ADB
		p.isAlive();
		AndroidDebugBridge.init(false);
		AndroidDebugBridge adb = AndroidDebugBridge.createBridge();
		
		Thread.sleep(1500);
		
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
						DdmPreferences.setTimeOut(24000);
			        	FileListingService fls = device.getFileListingService();
			        	totalTamañoDirectorio = getTotalRemoteFileSize(new FileEntry[]{internalDir}, fls);
					}
				}
			}
		}
		
		AndroidDebugBridge.disconnectBridge();
		AndroidDebugBridge.terminate();		
		return totalTamañoDirectorio;
	}
	
	/**
	 * 
	 * @param entries
	 * @param fls
	 * @return
	 */
	private long getTotalRemoteFileSize(FileEntry[] entries, FileListingService fls) 
  	{
		long count = 0;
	    for (FileEntry e : entries) 
	    {
	        long type = e.getType();
	        if (type == FileListingService.TYPE_DIRECTORY) 
	        {
	            // get the children
	            FileEntry[] children = fls.getChildren(e, false, null);
	            count += getTotalRemoteFileSize(children, fls);// + 1;
	        }
	        else if (type == FileListingService.TYPE_FILE) 
	        {
	            count += e.getSizeValue();
	        }
	    }

	    return count;
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	private FileEntry obtainDirectoryFileEntry(String path) 
	{
	    try 
	    {
	      FileEntry lastEntry = null;
	      Constructor<FileEntry> c =
	          FileEntry.class.getDeclaredConstructor(FileEntry.class, String.class, int.class,
	              boolean.class);
	      c.setAccessible(true);
	      for (String part : path.split("/")) 
	      {
	        lastEntry = c.newInstance(lastEntry, part, TYPE_DIRECTORY, lastEntry == null);
	      }
	      return lastEntry;
	    } catch (NoSuchMethodException ignored) {
	    } catch (InvocationTargetException ignored) {
	    } catch (InstantiationException ignored) {
	    } catch (IllegalAccessException ignored) {
	    }
	    return null;
	  }
	
	/**
	 * 
	 * @param dir
	 * @return
	 */
	private FileEntry getDirectoryOnInternalStorage(final String dir) 
	{
		return obtainDirectoryFileEntry(dir);
	}

	/**
     * Esta función, retorna porcentaje de extracción de datos lógica.
     * Necesita de instancia dispositivo Android detectado para conocer tamaño en bytes
     * de espacio de almacenamiento. Y teniendo tamaño en bytes parcial de directorio destino
     * se calcula porcentaje. Tamaño Parcial/Tamaño Total x 100.
     * @return porcentajeExtraccionDatos Variable long que contiene porcentaje de extracción
     * de datos.
     */
	@Override
	public long getPorcentajeExtraccionDatos() 
	{
		long porcentajeExtraccionDatos = 0;
		long tamanioOrigen = dispositivo.getTamanioLogico();
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
     * Esta función, retorna tamaño parcial de extracción de datos lógica convertido en formato
     * legible para humano.
     * Necesita de instancia dispositivo Android detectado para conocer tamaño en bytes
     * de espacio de almacenamiento. Y teniendo tamaño en bytes parcial de ruta destino de unidad de almacenamiento
     * se concatena ambos tamaños quedando de la siguiente forma. Tamaño Parcial de Tamaño Total.
     * Por ejemplo: 200,15 MB de 5,19 GB.
     * @return tamanioLegibleParcial Variable String que contiene tamaño parcial de extracción de datos
     * convertido en formato legible para humano.
     */
	@Override
	public String getTamanioParcialExtraccionDatos() 
	{
		String tamanioLegibleOrigen = byteCountToDisplaySize(dispositivo.getTamanioLogico()).toString();		
		String tamanioLegibleDestino = byteCountToDisplaySize(unidadAlmacenamiento.getTamanioLibre()).toString();
		String tamanioLegibleParcial = tamanioLegibleDestino + " de " + tamanioLegibleOrigen;
		
		return tamanioLegibleParcial;
	}
	
	/**
     * Esta función, retorna estado de extracción de datos.
     * Que permite ser consultada ya sea por modelo o por controladores.
     * @return estadoExtraccionAdb Variable entera.
     */
	public int getEstadoExtraccionDatos()
	{
		return estadoExtraccionAdb;
	}
}
