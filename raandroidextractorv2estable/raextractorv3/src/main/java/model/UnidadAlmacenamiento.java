package main.java.model;

import java.io.File;
import java.text.DecimalFormat;

import org.apache.commons.io.FileUtils;

/**
 * Esta clase, contiene toda la información asociada al lugar de unidad de almacenamiento elegida,
 * en donde se guardará todos los datos extraídos de dispositivo Android detectado. Como así también 
 * reporte y/o archivos de hash asociados a extracción de datos de dispositivo Android detectado.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class UnidadAlmacenamiento 
{
	private File directorioDestino;
	private String rutaDestino;
	private long tamanioLibre;
	private boolean esDirectorio;
	private boolean esMayor;
	
	/**
     * Este constructor es el encargado de inicializar los diferentes atributos y objetos para la clase unidad de almacenamiento.
     * rutaDestino Variable de tipo String, que almacena directorio seleccionado.
     * tamanioLibre Variable de tipo long, que almacena tamaño libre de directorio seleccionado.
     */
	public UnidadAlmacenamiento()
	{
		rutaDestino = "";
		tamanioLibre = 0;	
		esDirectorio = false;
		esMayor = false;
	}
	
	/**
     * Este método es el encargado de guardar ruta destino final, en la cual se alojaran datos extraídos 
     * de dispositivo Android seleccionado.
     * Además se registra tamaño libre de directorio seleccionado.
     * @param stage Este atributo es de tipo Stage se utiliza para mostrar ventana selector de directorio
     * @return retorna true si se seleccionó algún directorio, false en caso contrario
     */
	public boolean guardarRutaDestino(String rutaDestino)
	{
		esDirectorio = false;
		directorioDestino = new File(rutaDestino);
		
		if(directorioDestino.exists() && directorioDestino.isDirectory())
        {
			this.rutaDestino = rutaDestino;
			tamanioLibre = directorioDestino.getFreeSpace();
			esDirectorio = true;
        }
        else
        {
        	this.rutaDestino = "";
        	tamanioLibre = 0;
        	esDirectorio = false;
        }
		
		return esDirectorio;
	}
	
	/**
     * Este método verifica tamaño libre en directorio seleccionado.
     * @param tamanioOrigen Este atributo es de tipo long, contiene tamaño de datos que se extraerá de dispositivo Android detectado.
     * @return retorna true, si tamaño libre de directorio seleccionado es mayor a cantidad de datos a 
     * extraer de dispositivo Android detectado, false en caso contrario
     */
	public boolean chequearTamanioLibre(long tamanioOrigen)
	{
		esMayor = false;
		
		if(tamanioLibre > tamanioOrigen)
		{
			esMayor = true;
		}
		else
		{
			esMayor = false;
		}
		
		return esMayor;
	}
	
	/**
     * Este método devuelve ruta destino seleccionada por usuario.
     * @return retorna variable String llamada rutaDestino con ruta destino.
     */
	public String getRutaDestino()
	{
		return rutaDestino;
	}
	
	/**
     * Este método devuelve tamaño libre de directorio seleccionado por usuario.
     * @return retorna variable long llamada tamanioLibre con cantidad de bytes disponibles en ese directorio.
     */
	public long getTamanioLibre()
	{
		tamanioLibre = FileUtils.sizeOfDirectory(directorioDestino);
		return tamanioLibre;
	}
	
	/**
     * Este método devuelve tamaño faltante en directorio seleccionado por usuario, es decir cuando tamaño de extracción de
     * dispositivo Android detectado es más grande a tamaño disponible en directorio seleccionado.
     * @param tamanioAlmacenamientoDispositivo  esta variable es de tipo long contiene tamaño total de datos 
     * que se quiere extraer de dispositivo Android detectado.
     * @return retorna variable de tipo String llamada espacioFaltante que contiene cantidad de bytes 
     * faltantes en unidad de almacenamiento.
     */
	public String getTamanioFaltante(long tamanioAlmacenamientoDispositivo)
	{
		String espacioFaltante = byteCountToDisplaySize(tamanioAlmacenamientoDispositivo-getTamanioLibre());
		return espacioFaltante;
	}
	
	/**
     * Este método retorna String con cantidad de datos expresadas en una unidad de almacenamiento.  
     * @param size esta variable es de tipo long, contiene cantidad de datos expresados en bytes.
     * @return retorna cantidad de datos expresados en b, Kb, Mb, Gb O Tb, según corresponda.
     */
	public String byteCountToDisplaySize(long size)
	{
		String readableSize;
		if(size==0)
		{
			readableSize = "0 B";
		}
		else
		{
			String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		    int unitIndex = (int) (Math.log10(size)/3);
		    double unitValue = 1 << (unitIndex * 10);
		 
		    readableSize = new DecimalFormat("#,##0.##").format(size/unitValue) + " " + units[unitIndex];
		}
	    
	    return readableSize;
	}
	
	/**
	 * Este método, crea direcotiro con formato especial dentro de directorio destino de unidad de almacenamiento.
	 * @param formatoRuta Es variable String que contiene formato de extracción de datos 
     * para ficheros y directorios que se creen en ruta destino de unidad de almacenamiento.
	 */
	public void crearDirectorioRutaDestino(String formatoRuta)
	{
		rutaDestino = rutaDestino + "/" + formatoRuta;
		directorioDestino = new File(rutaDestino);
		directorioDestino.mkdir();
	}
}