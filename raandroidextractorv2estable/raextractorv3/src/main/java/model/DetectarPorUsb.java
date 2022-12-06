package main.java.model;

import java.util.List;

import org.usb4java.Context;
import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

/**
 * Esta clase, es una estrategia de detectar dispositivo.
 * En este caso se efectua detección de dispositivo por USB.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class DetectarPorUsb extends ReconocedorDispositivo
{
	public DetectarPorUsb(List<Dispositivo> listaDispositivos)
	{
		super(listaDispositivos);
	}
	
	/**
     * Esta función, retorna true si se detectó dispositivo Android
     * en la lista de dispositivos. O retorna false si no hubo coincidencia.
     * Comparando únicamente el idVendor del dispositivo Android con
     * IdFabricante de cada dispositivo de la lista.
     * @return esDetectado Variable boolean que devuelve verdadero en caso que hubo
     * coincidencia entre alguno de los dispositivos de la lista dispositivos con dispositivo Android conectado (idVendor)
     * o falso si no hubo coincidencia.
     */
	@Override
	public boolean detectarDispositivo() 
	{	
		esDetectado = false;
		
		// Create the libusb context
        Context context = new Context();
 
        // Initialize the libusb context
        int result = LibUsb.init(context);
        if (result < 0)
        {
            throw new LibUsbException("Unable to initialize libusb", result);
        }
 
        // Read the USB device list
        DeviceList list = new DeviceList();
        result = LibUsb.getDeviceList(context, list);
        if (result < 0)
        {
            throw new LibUsbException("Unable to get device list", result);
        }
 
        try
        {
            // Iterate over all devices and list them
            for (Device device: list)
            {
                DeviceDescriptor descriptor = new DeviceDescriptor();
                //String idV = String.valueOf(descriptor.idVendor());
                result = LibUsb.getDeviceDescriptor(device, descriptor);
                if (result < 0)
                {
                    throw new LibUsbException("Unable to read device descriptor", result);
                }
                if(!esDetectado)
                {
                	for(int i=0; i < listaDispositivos.size(); i++)
            		{
            			if(listaDispositivos.get(i).getIdFabricante().equals(String.valueOf(descriptor.idVendor())))
            			{
            				esDetectado = true;
            				break;
            			}
            			else
            			{
            				esDetectado = false;
            			}
            		}
                }
                else
                {
                	break;
                }
            }
        }
        finally
        {
            // Ensure the allocated device list is freed
            LibUsb.freeDeviceList(list, true);
        }
 
        // Deinitialize the libusb context
        LibUsb.exit(context);
        
		return esDetectado;
	}
}
