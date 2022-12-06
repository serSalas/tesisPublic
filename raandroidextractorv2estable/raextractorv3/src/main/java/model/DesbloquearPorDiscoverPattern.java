package main.java.model;

/**
* v2.0.0
* @author Mantay Rodrigo Ariel
* @author Salas Canalicchio Sergio Enrique
*
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.ProcessBuilder.Redirect;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;

public class DesbloquearPorDiscoverPattern extends DesbloquearPantalla
{
	private static int estadoDesbloquearPantalla;
	private IDevice disp;
	private static String line;

	public DesbloquearPorDiscoverPattern(Dispositivo dispositivo) {
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
					/* Flashear RECOVERY PERSONALIZADO */
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
					/* Se encuentra EN MODO RECOVERY */
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
										
									/* Arrastrar gesture.key de dispositivo Android a Unidad de almacenamiento */
									Thread.sleep(5000);
									Process p1 = Runtime.getRuntime().exec("adb pull /data/system/gesture.key src/main/resources/output/gesture.key");
									Thread.sleep(5000);
									/* Desencriptar gesture.key */
									ProcessBuilder builder = new ProcessBuilder("python","src/main/resources/devices/gesture/gesturecrack.py","-f","src/main/resources/output/gesture.key");
									Process p = builder.start();
									int exitCode = p.waitFor();
									if(exitCode != 0)
									{
										System.out.println("SE PRODUJO UN ERROR: "+  exitCode);
									}
									else if (exitCode == 0)
									{
										System.out.println("SIN ERRORES: "+ exitCode);
									}
								Process p2 = Runtime.getRuntime().exec("adb reboot download");
								estadoDesbloquearPantalla=3;
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
				/* Se encuentra en MODO BOOTLOADER */
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
				/* Flashear RECOVERY DE FABRICA */
				Process p = Runtime.getRuntime().exec("heimdall flash --RECOVERY " + dispositivo.getRutaRecoveryFabrica());
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
				StringBuffer stringBuffer = new StringBuffer(); 
				String s;
				s = stdInput.readLine();
				Process p2 = Runtime.getRuntime().exec("rm src/main/resources/output/gesture.key");
				while(s!=null)
				{
					stringBuffer.append(s);
					stringBuffer.append("\n");
					s = stdInput.readLine();
					
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
			estadoDesbloquearPantalla = -1;
		}
	}

	@Override
	public int getEstadoDesbloqueo() {
		// TODO Auto-generated method stub
		return estadoDesbloquearPantalla;
	}
}
