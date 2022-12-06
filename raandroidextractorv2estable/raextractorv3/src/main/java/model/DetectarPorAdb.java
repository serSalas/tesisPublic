package main.java.model;

import java.io.IOException;
import java.util.List;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

/**
 * Esta clase, es una estrategia de detectar dispositivo.
 * En este caso se efectua detección de dispositivo por ADB.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class DetectarPorAdb extends ReconocedorDispositivo
{
	private int cantidadDispositivosAdb;
	
	public DetectarPorAdb(List<Dispositivo> listaDispositivos)
	{
		super(listaDispositivos);
		cantidadDispositivosAdb = 0;
	}
	
	/**
     * Esta función, retorna true si se encontro dispositivo Android
     * en lista de dispositivos. O retorna false si no hubo coincidencia.
     * Comparando marca, modelo, versión del sistema operativo y número de compilacion
     * entre dispositivo Android y cada dispositivo de la lista.
     * @return esDetectado Variable boolean que devuelve verdadero en caso que hubo
     * coincidencia entre alguno de los dispositivos de la lista dispositivo con dispositivo Android conectado (device)
     * o falso si no hubo coincidencia.
	 * @throws InterruptedException 
	 * @throws IOException 
     */
	@Override
	public boolean detectarDispositivo() 
	{
		try
		{
			deseleccionarDispositivos();
			esDetectado = false;
			
			Process p;
			p = Runtime.getRuntime().exec("adb devices");
			//Para iniciar el servidor ADB
			p.isAlive();
			AndroidDebugBridge adb;
			AndroidDebugBridge.init(false);
			adb = AndroidDebugBridge.createBridge();
			
			Thread.sleep(1000);
			
			if (!adb.isConnected()) 
	        {
	            mensajeDeteccion = "No se pudo conectar al servidor ADB";
	            esDetectado = false;
	        }
			else
			{
				cantidadDispositivosAdb = adb.getDevices().length;						
				if(cantidadDispositivosAdb > 0)
				{
					for(IDevice device : adb.getDevices()) 
					{
						for(int i=0; i < listaDispositivos.size(); i++)
						{
							if(listaDispositivos.get(i).getFabricante().equals(device.getProperty("ro.product.manufacturer")) 
							&& listaDispositivos.get(i).getModelo().equals(device.getProperty("ro.product.model"))
							&& listaDispositivos.get(i).getVersionSo().equals(device.getProperty("ro.build.version.release"))
							&& listaDispositivos.get(i).getNumeroCompilacion().equals(device.getProperty("ro.build.display.id")))
							{
								listaDispositivos.get(i).setSeleccionar(true);
								esDetectado = true;
								
								mensajeDeteccion = "Dispositivos conectados por ADB: " + String.valueOf(cantidadDispositivosAdb)
				        		+ "\nMarca: " + getDispositivoDetectado().getFabricante()
				        		+ "\nModelo: " + getDispositivoDetectado().getModelo()
				        		+ "\nVersión de sistema operativo: " + getDispositivoDetectado().getVersionSo()
				        		+ "\nNúmero de compilación: " + getDispositivoDetectado().getNumeroCompilacion();
								
								break;
							}
							else
							{
								mensajeDeteccion = "Dispositivos conectados por ADB: " + String.valueOf(cantidadDispositivosAdb)
				        		+ "\nDispositivo no encontrado";
								
								esDetectado = false;
							}
						}
					}
				}
				else
				{
					mensajeDeteccion = "Dispositivos conectados por ADB: " + String.valueOf(cantidadDispositivosAdb)
	        		+ "\nSe recomienda:" + "\n- Habilitar modo depuración en dispositivo Android o" 
	        		+ "\n- Buscar manualmente el dispositivo Android";
					
					esDetectado = false;
				}
			}
			
			AndroidDebugBridge.disconnectBridge();
			AndroidDebugBridge.terminate();
		}
		catch(IOException | InterruptedException e)
		{
			e.printStackTrace();
		}
		
		return esDetectado;
	}
}
