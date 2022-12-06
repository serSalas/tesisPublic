package main.java.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;

/**
 *  
 * v2.0.0
 * @author Mantay Rodrigo Ariel
 * @author Salas Canalicchio Sergio Enrique
 *
 */
public class DesbloquearPorUnlockScreen extends DesbloquearPantalla
{
	private static int estadoDesbloquearPantalla;
	private IDevice disp;


	public DesbloquearPorUnlockScreen(Dispositivo dispositivo) {
		super(dispositivo);
		estadoDesbloquearPantalla=0;
	}

	@Override
	public void desbloqueoPantalla() 
	{
		try
		{
			switch(estadoDesbloquearPantalla)
			{
				case 0:
				{
					/* Detecta si el dispositivo conectado a Puerto USB está en modo Bootloader */
					Process p = Runtime.getRuntime().exec("heimdall detect");
					BufferedReader stdInput = new BufferedReader (new InputStreamReader (p.getInputStream()));
					String s;
					s = stdInput.readLine();
					if(s!= null)
					{
						estadoDesbloquearPantalla=1;
						System.out.println(s);
					}
					else
					{
						estadoDesbloquearPantalla=0;
						
					}
					break;
				}
				case 1:
				{
					/*FLASHEAR RECOVERY PERSONALIZADO*/
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
						
						estadoDesbloquearPantalla = 2;
					}
					System.out.println(stringBuffer.toString());
					break;
				}
				case 2:
				{
					/* 
					 * SE ENCUENTRA EN MODO RECOVERY y REALIZA PROCEDIMIENTOS 
					 * SOBRE DISPOSITIVO PARA PODER EVITAR LA INSTANCIA DE BLOQUEO DE PANTALLA
					 *
					 */

					AndroidDebugBridge adb;
					AndroidDebugBridge.init(false);
					adb = AndroidDebugBridge.createBridge();
					Thread.sleep(1000);
					if(!adb.isConnected())
					{
						System.out.println("No se pudo conectar al servidor ADB");
					}
					
					else
					{
						if(adb.getDevices().length>0)
						{	
							for(IDevice device : adb.getDevices())
							{
							
								if(dispositivo.getFabricante().equals(Propiedades.getPropiedades("ro.product.manufacturer"))
								&& dispositivo.getModelo().equals(Propiedades.getPropiedades("ro.product.model"))
								&& dispositivo.getVersionSo().equals(Propiedades.getPropiedades("ro.build.version.release"))
								&& dispositivo.getNumeroCompilacion().equals(Propiedades.getPropiedades("ro.build.display.id")))
								{
									disp = device;
									estadoDesbloquearPantalla=3;
									Thread.sleep(5000);
									Process p1 = Runtime.getRuntime().exec("adb shell rm /data/system/gesture.key");
									Thread.sleep(1000);
									Process p2 = Runtime.getRuntime().exec("adb reboot download");
									break;
								}
							}
						}
					}
				AndroidDebugBridge.disconnectBridge();
				AndroidDebugBridge.terminate();
				break;
			}

			case 3:
			{
				/* Detecta si el dispositivo conectado a Puerto USB está en modo Bootloader */
				
				Process p = Runtime.getRuntime().exec("heimdall detect");
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String s;
				s = stdInput.readLine();
				if(s!=null)
				{
					estadoDesbloquearPantalla = 4;
					System.out.println(s);
				}
				break;
				
			}
			case 4:
			{
				/* FLASHEAR RECOVERY FABRICA */
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
					/* Cambia estado a finalizado con éxito */
					estadoDesbloquearPantalla = 5;
				}
				System.out.println(stringBuffer.toString());
				System.out.println("Se realizo el desbloqueo de pantalla.");
				break;
			}
			
		}
	}
		catch(IOException | InterruptedException e)
		{
			/* Cambia estado a finalizado con falla */
			estadoDesbloquearPantalla = -1;
		}
	}

	@Override
	public int getEstadoDesbloqueo() {
		// TODO Auto-generated method stub
		return estadoDesbloquearPantalla;
	}
	

}
