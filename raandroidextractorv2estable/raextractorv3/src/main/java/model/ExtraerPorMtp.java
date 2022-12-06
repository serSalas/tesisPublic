package main.java.model;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

/**
 * Esta clase, es una estrategia de metodo de extracción.
 * En este caso se efectua extracción de datos por MTP.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class ExtraerPorMtp extends ExtractorDatos 
{
	private static int estadoExtraccionMtp;
	private String pathOrigen;
	private String pathDestino;
	
	public ExtraerPorMtp(Dispositivo dispositivo, UnidadAlmacenamiento unidadAlmacenamiento, boolean isFlagHashMd5, boolean isFlagHashSha256) 
	{
		super(dispositivo, unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
		estadoExtraccionMtp = 0;
		pathOrigen = "";
		pathDestino = "";
	}
	
	/**
     * Este método, realiza extracción de datos, utilizando ruta run/user/1000/gvfs/ para realizar copia
     * donde se aloja carpeta multimedia de dispositivo Android detectado.
     * El método cuenta con 7 estados.
     * Estado 0:
     * Chequea que dispositivo Android detectado este conectado a PC y en modo MTP.
     * Estado 1:
     * Calcula tamaño de almacenamiento de dispositivo Android detectado.
     * Estado 2:
     * Chequea que en unidad de almacenamiento seleccionada se encuentre con suficiente memoria.
     * Es decir, debe poseer mayor cantidad de memoria libre que lo que ocupa particion o almacenamiento
     * de dispositivo Android detectado. En caso que no posea suficiente memoria unidad de almacenamiento
     * se notifica con mensaje de error.
     * Estado 3:
     * Se copia directorio MTP de dispositivo Android detectado mediante manejo de ficheros. Registrando fecha de inicio,
     * fecha de finalización y tiempo de duracion de extracción realizada.
     * Estado 4:
     * Se realiza el o los hash a partir de extracción de datos efectuada. Dando como resultado uno o dos archivos .md5 y/o .sha256.
     * Estado 5:
     * Se realiza reporte de extracción de datos.
     * Estado -1:
     * Cuando ocurre una excepción o espacio de almacenamiento es insuficiente en unidad de almacenamiento.
     * Además, se registra error de excepción en variable mensajeError.
     */
	@Override
	public void extraerDatos() 
	{
		try
		{
			switch(estadoExtraccionMtp)
			{
				case 0:
				{
					Context context = new Context();
			        int result = LibUsb.init(context);
			        
			        if (result < 0)
			        {
			            throw new LibUsbException("Unable to initialize libusb", result);
			        }
			 
			        DeviceList list = new DeviceList();
			        result = LibUsb.getDeviceList(context, list);
			        
			        if (result < 0)
			        {
			            throw new LibUsbException("Unable to get device list", result);
			        }
			 
			        try
			        {
			            for (Device device: list)
			            {
			            	int address = LibUsb.getDeviceAddress(device);
			                int busNumber = LibUsb.getBusNumber(device);
			                DeviceDescriptor descriptor = new DeviceDescriptor();
			                result = LibUsb.getDeviceDescriptor(device, descriptor);
			                if (result < 0)
			                {
			                    throw new LibUsbException("Unable to read device descriptor", result);
			                }
			                if(dispositivo.getIdFabricante().equals(String.valueOf(descriptor.idVendor())))
			                {
			                	dispositivo.setRutaMtp(String.format("%03d", busNumber), String.format("%03d", address));
			                	break;
			                }
			            }
			        }
			        finally
			        {
			            LibUsb.freeDeviceList(list, true);
			        }
			 
			        LibUsb.exit(context);
			        
					pathOrigen = dispositivo.getRutaMtp();
					File directorioOrigenMtp = new File(pathOrigen);

					if(directorioOrigenMtp.exists())
					{
						estadoExtraccionMtp = 1;
					}
					break;
				}
				case 1:
				{
					File directorioOrigenMtp = new File(pathOrigen);
					dispositivo.setTamanioLogico(FileUtils.sizeOfDirectory(directorioOrigenMtp));
					estadoExtraccionMtp = 2;
					break;
				}
				case 2:
				{
					if(unidadAlmacenamiento.chequearTamanioLibre(dispositivo.getTamanioLogico()))
					{
						formatoRuta("Extraccion_Logica");
						unidadAlmacenamiento.crearDirectorioRutaDestino(getFormatoRuta());
						
						estadoExtraccionMtp = 3;
					}
					else
					{
						mensajeError = "La unidad de almacenamiento seleccionada no dispone de suficiente espacio de memoria. "
								+ "Se necesitan mas de " 
								+ unidadAlmacenamiento.getTamanioFaltante(dispositivo.getTamanioLogico()) 
								+ " en la unidad de almacenamiento para guardar la extraccion de datos";
						estadoExtraccionMtp = -1;
					}
					break;
				}
				case 3:
				{
					pathOrigen = dispositivo.getRutaMtp();
					pathDestino = unidadAlmacenamiento.getRutaDestino();
					File directorioOrigen = new File(pathOrigen);
			        File directorioDestino = new File(pathDestino);
			        setFechaInicioExtraccion();
			        FileUtils.copyDirectory(directorioOrigen, directorioDestino);
			        setFechaFinalExtraccion();
			        setDuracionExtraccion();
			        estadoExtraccionMtp = 4;
			        System.out.println("Se realizo extraccion de datos utilizando metodo MTP.");
			        break;
				}
				case 4:
				{
					hash = new Hash(unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
					if(hash.generarHash(getFormatoRuta()))
					{
						estadoExtraccionMtp = 5;
					}
					else
					{
						mensajeError = "No se pudo generar código/s hash.";
						estadoExtraccionMtp = -1;
					}
			        break;
				}
				case 5:
				{
					nombreMetodoExtraccion = "Metodo Mtp";
					reporte = new Reporte(dispositivo, unidadAlmacenamiento, hash, this);
					if(reporte.generarReporte())
					{
						estadoExtraccionMtp = 6;
					}
					else
					{
						mensajeError = "No se pudo generar reporte.";
						estadoExtraccionMtp = -1;
					}
					break;
				}
			}
		}
		catch (IOException e)
		{
			mensajeError = e.getMessage();
			estadoExtraccionMtp = -1;
		}
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
     * @return estadoExtraccionMtp Variable entera.
     */
	public int getEstadoExtraccionDatos()
	{
		return estadoExtraccionMtp;
	}
}
