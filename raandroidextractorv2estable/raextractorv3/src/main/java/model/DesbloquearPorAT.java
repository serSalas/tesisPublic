package main.java.model;
/**
 * Esta clase, contiene 
 * v2.0.0
 * @author Mantay Rodrigo Ariel
 * @author Salas Canalicchio Sergio Enrique
 *
 */
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;

public class DesbloquearPorAT extends DesbloquearPantalla
{
	private static int estadoDesbloquearPantalla;
	private IDevice disp;


	public DesbloquearPorAT(Dispositivo dispositivo) {
		super(dispositivo);
		estadoDesbloquearPantalla=0;
	}

	@Override
	public void desbloqueoPantalla() 
	{
		try
		{
			StringBuffer stringBuffer = new StringBuffer();
			switch(estadoDesbloquearPantalla)
			{
		
				/* Genera el archivo necesario para cambiar entre puertos de USB */
				case 0:
				{
					String command;
					
					command="sh src/main/resources/devices/AT/usbswitch/datos.sh";
					
					estadoDesbloquearPantalla = 1;
					Runtime runtime=Runtime.getRuntime();
					Process p = runtime.exec(command);
					Thread.sleep(15000);
					
					/* Get OuputStream */
		            PrintWriter writer = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(p.getOutputStream())), true);
		            /* Read the output of command prompt */
		            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		            String line = reader.readLine();
		            /* Read upto end of execution */
		            while (line != null) {
		                /* Pass the value to command prompt/user input */
		                System.out.println(line);
		                line = reader.readLine();
		            }
		            /* The stream obtains data from the standard output stream of the process represented by this Process object. */
		            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
		            /* The stream obtains data from the error output stream of the process represented by this Process object. */
		            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
		            
		            String Input;
		            while ((Input = stdInput.readLine()) != null) {
		                System.out.println(Input);
		            }            
		            
		            String Error;
		            while ((Error = stdError.readLine()) != null) {
		                System.out.println(Error);
		            }		            	        
		            estadoDesbloquearPantalla = 1;
						break;
				}
				
				case 1:
				{

					/* Se habilita la opci�n del celular para entrar en la consola AT  */					
									
					String command;
					command="python src/main/resources/devices/AT/interact/atinteract.py";
					Runtime runtime=Runtime.getRuntime();
					Process p = runtime.exec(command);				
		
					estadoDesbloquearPantalla = 2;
					break;
				}
			

				case 2:
				{
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
