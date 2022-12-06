package main.java.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import javax.swing.JTextField;

import com.jfoenix.controls.JFXProgressBar;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.Main;
import main.java.controller.Controlador;
import main.java.model.DesbloquearPorDiscoverPattern;

/**
 * Esta clase, es un controlador vinculado a las vistas: 
 * VISTA_EMERGENTE_MODO_BOOTLOADER, VISTA_EMERGENTE_FLASHEAR_RECOVERY, VISTA_EMERGENTE_MODO_RECOVERY,
 * VISTA_EMERGENTE_GENERAR_HASH, VISTA_EMERGENTE_MODO_DEPURACION, VISTA_EMERGENTE_EXPLOTAR_VULNERABILIDAD,
 * VISTA_EMERGENTE_MODO_MTP, VISTA_EMERGENTE_MODO_DOWNLOAD, VISTA_EMERGENTE_FINALIZACION_EXITOSA,
 * VISTA_EMERGENTE_FINALIZACION_FALLIDA, VISTA_EMERGENTE_CALCULO_TAMANIO_ALMACENAMIENTO y VISTA_EXTRACCION_PROGRESO.
 * Además, hereda de la clase Controlador.
 * v1.0.0
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 * @author Sosa Ludueña Diego
 * v2.0.0
 * @author Mantay Rodrigo Ariel
 * @author Salas Canalicchio Sergio Enrique
 *
 */
public class ControladorDesbloqueoProgreso extends Controlador
{  	
	@FXML
    private AnchorPane panelMetodoDesbloqueo;
    
    @FXML
    private Text mensajeError;
    

    /*
     * la variable hilo se la utiliza mayormente para realizar tareas en el modelo
     * y evitar que se tilde la interfaz de usuario.
     * Se utiliza hilo "H1" para realizar la extracción de datos del dispositivo
     * a la unidad de almacenamiento.
     * Y se utiliza hilo "H2" para consultar porcentaje y tamaño parcial de descarga
     * en el modelo y visualizarlo en la interfaz de usuario.  
     */
    private Thread hilo;
    
    /*
     * la variable estadoThread se lanza únicamente cuando se inicia la ventana
     * VISTA_EXTRACCION_PROGRESO para independizar la lógica del modelo con la
     * lógica de la interfaz de usuario. Es decir, se lanza este hilo para que
     * no se tilde la interfaz de usuario. Este hilo realiza tarea en modelo
     * y el hilo principal se encarga de la interfaz de usuario.
     */
    private static boolean estadoThread;
    
    /*
     * la variable cerrojo es un recurso compartido de los hilos
     * y sirve para proveer exclusión mutua.
     * El primer hilo que adquiere el recurso será el único que podrá estar
     * en la sección crítica. Los demás hilos que quieran ingresar, tendrán
     * que esperar hasta que libere el primer hilo que ingresó.
     */
    private static Object cerrojo = new Object();
    
    /**
     * Este método, sirve para inicializar los nodos de la vista vinculada
     * al controlador ControladorExtraccionProgreso. Cada vez que se crea una instancia de
     * ControladorExtraccionProgreso se llama tanto a su constructor como así también al método initialize().
     * Cuando la variable estadoThread es falsa se encuentra en la VISTA_EXTRACCION_PROGRESO
     * y ademas lanza un hilo llamado "H1" para realizar la tarea de la extracción de datos (logica del modelo).
     * De esta manera se dependiza el hilo principal de consulta el modelo para centrarse en la interfaz de usuario.
     * Cuando las variables barraProgreso, porcentaje y botonSiguiente
     * sean distintas de nulo se encuentra en la vista VISTA_EXTRACCION_PROGRESO
     * y si el estado del metodo de extraccion de datos es distinto de 0 inicializa cada variable.
     * Estas variables son útiles para mostrar el procentaje, la barra de progreso y el tamaño de extraccion parcial
     * en la interfaz de usuario. Además, se lanza un hilo llamado "H2" para que realice esta tarea de mostrar por
     * la interfaz de usuario.
     * Cuando las variable mensajeError sea distinta de nulo se encuentra en la
     * sean nulas se encuentra en la VISTA_EMERGENTE_FINALIZACION_FALLIDA.
     * Esta variable se utiliza para mostrar en dicha ventana emergente un mensaje de error
     * que dependerá de la Excepción que se origine en el proceso de extracción de datos.
     * @throws IOException 
     */
    public void initialize() throws IOException
    {
    
    	Process p = Runtime.getRuntime().exec("mkdir src/main/resources/output");

    	if(!estadoThread)
    	{
    		estadoThread = true;
    		hilo = new Thread(task);
    		hilo.setName("H1");
			hilo.setDaemon(true);
			hilo.start();
    	}
    	if(mensajeError!=null)
    	{
    		mensajeError.setText(getModelo().consultarDesMensajeError());
    	}
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonAceptar.
     * Por lo tanto, su función es lanzar hilo llamado "H1" para que consulte al modelo
     * si se encuentra el dispositivo en modo bootloader.
     * @param event Se produjo una click del mouse en el botonAceptar.
     */
    @FXML
    public void clickChequearModoBootloader(MouseEvent event)  
    {
    	    getModelo().consultarMetodoDesbloqueo();
    		hilo = new Thread(task);
    		hilo.setName("H1");
    		hilo.setDaemon(true);
    		hilo.start();
    
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonAceptar.
     * Por lo tanto, su función es lanzar hilo llamado "H1" para que consulte al modelo
     * si se encuentra el dispositivo en modo bootloader.
     * @param event Se produjo una click del mouse en el botonAceptar.
     */
    @FXML
    public void clickConfirmarGenPuertos(MouseEvent event)  
    {
    	getModelo().consultarMetodoDesbloqueo();
    		hilo = new Thread(task);
    		hilo.setName("H1");
    		hilo.setDaemon(true);
    		hilo.start();
    }
    
    public void clickcambiaAT(MouseEvent event)  
    {
    	getModelo().consultarMetodoDesbloqueo();
    		hilo = new Thread(task);
    		hilo.setName("H1");
    		hilo.setDaemon(true);
    		hilo.start();
    }
    
    public void clickHabilitaAT(MouseEvent event)  
    {
    	getModelo().consultarMetodoDesbloqueo();
    		hilo = new Thread(task);
    		hilo.setName("H1");
    		hilo.setDaemon(true);
    		hilo.start();
    }
        
    public void clickDesbloquearAT(MouseEvent event)  
    {
    	getModelo().consultarMetodoDesbloqueo();
    	if(getModelo().getEstadoDesbloqueo() == 4)
    	{
    		hilo = new Thread(task);
    		hilo.setName("H1");
    		hilo.setDaemon(true);
    		hilo.start();
    	}
    }

    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonAceptar.
     * Por lo tanto, su función es lanzar hilo llamado "H1" para que consulte al modelo
     * si se encuentra el dispositivo en modo recovery.
     * @param event Se produjo una click del mouse en el botonAceptar.
     */
    @FXML
    public void clickChequearModoRecovery(MouseEvent event)  
    {
    	getModelo().consultarMetodoDesbloqueo();
    		hilo = new Thread(task);
    		hilo.setName("H1");
    		hilo.setDaemon(true);
    		hilo.start();
    	
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonAceptar.
     * Por lo tanto, su función es lanzar hilo llamado "H1" para que consulte al modelo
     * si se encuentra el dispositivo en modo depuración.
     * @param event Se produjo una click del mouse en el botonAceptar.
     */
    @FXML
    public void clickChequearModoDepuracion(MouseEvent event) 
    {
    	getModelo().consultarMetodoDesbloqueo();
    	if(getModelo().getEstadoDesbloqueo() == 1)
    	{
    		hilo = new Thread(task);
    		hilo.setName("H1");
    		hilo.setDaemon(true);
    		hilo.start();
    	}
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonAceptar.
     * Por lo tanto, su función es lanzar hilo llamado "H1" para que consulte al modelo
     * si se encuentra el dispositivo en modo MTP.
     * @param event Se produjo una click del mouse en el botonAceptar.
     */
    @FXML
    public void clickChequearModoMtp(MouseEvent event) 
    {
    	getModelo().consultarMetodoDesbloqueo();
    	if(getModelo().getEstadoDesbloqueo() == 1)
    	{
    		hilo = new Thread(task);
    		hilo.setName("H1");
    		hilo.setDaemon(true);
    		hilo.start();
    	}
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonAceptar.
     * Por lo tanto, su función es lanzar hilo llamado "H1" para que consulte al modelo
     * si se encuentra el dispositivo en modo download.
     * @param event Se produjo una click del mouse en el botonAceptar.
     */
    @FXML
    public void clickChequearModoDownload(MouseEvent event) 
    {
    	getModelo().consultarMetodoDesbloqueo();
    	if(getModelo().getEstadoDesbloqueo() == 1)
    	{
    		hilo = new Thread(task);
    		hilo.setName("H1");
    		hilo.setDaemon(true);
    		hilo.start();
    	}
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonSalir de la vista
     * VISTA_EMERGENTE_MODO_BOOTLOADER, VISTA_EMERGENTE_MODO_RECOVERY, VISTA_EMERGENTE_MODO_DEPURACION, 
     * VISTA_EMERGENTE_MODO_MTP o VISTA_EMERGENTE_MODO_DOWNLOAD.
     * Por lo tanto, su función es cerrar la ventana emergente ya sea
     * VISTA_EMERGENTE_MODO_BOOTLOADER, VISTA_EMERGENTE_MODO_RECOVERY, VISTA_EMERGENTE_MODO_DEPURACION, 
     * VISTA_EMERGENTE_MODO_MTP o VISTA_EMERGENTE_MODO_DOWNLOAD y cambia a la vista
     * VISTA_METODO_EXTRACCION.
     * Estas vistas comparten el mismo método.
     * @param event Se produjo una click del mouse en el botonSalir.
     * @throws IOException
     */
    @FXML
    public void clickSalir(MouseEvent event) throws IOException 
    {
    	estadoThread = false;
		getMain().getDialogStage().close();
		getMain().cambiarVentana(Main.VISTA_METODO_DESBLOQUEO);
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonAceptar de la vista
     * VISTA_EMERGENTE_FINALIZACION_EXITOSA.
     * Por lo tanto, su función es cerrar la ventana emergente
     * VISTA_EMERGENTE_FINALIZACION_EXITOSA y cambia a la vista
     * VISTA_PRINCIPAL.
     * @param event Se produjo una click del mouse en el botonSalir.
     */
    @FXML
    public void clickAceptar_1(MouseEvent event) 
    {
    	Platform.runLater(() ->
		{
			estadoThread = false;
			getMain().getDialogStage().close();
			try 
			{
				getMain().cambiarVentana(Main.VISTA_PRINCIPAL);
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		);
    }
    
    @FXML
    public void clickAceptar_2(MouseEvent event) 
    {
    	Platform.runLater(() ->
		{
			estadoThread = false;
			getMain().getDialogStage().close();
			try 
			{
				getMain().cambiarVentana(Main.VISTA_DESCUBIERTA);
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		);
    }
    /**
     * Este método, es llamado por hilo "H1" para realizar tarea de extracción de datos consultado
     * modelo. y también es llamado por hilo "H2" para realizar tarea de mostrar en la interfaz de usuario
     * porcentaje, barra  de progreso y tamaño parcial de extracción de datos consultando también el
     * modelo.
     */
    public Task<Object> task = new Task<Object>() 
    {
    	@Override
    	protected Object call() throws InterruptedException, IOException  
    	{
    		if(hilo.getName().equals("H1"))
    		{
    			controladorDesbloqueo();
    		}
    		
    		return true;
    	}
    };
    
    /**
     * Este método, provee exclusión mutua ya que es accedido por un único hilo a la vez
     * ya que se colocó la sentencia synchronized y un recurso compartido por los hilos.
     * Cada hilo ingresa al método para consultar al modelo.
     * @throws IOException 
     */
    public void controladorDesbloqueo() throws IOException
    {
    	synchronized(cerrojo)
    	{
    		switch(getModelo().getEstadoModelo())
    		{
    			case 14:
    			{
    				controladorMetodoDesbloqueoRecovery();
    				break;
    			}
    			case 15:
    			{
    				controladorMetodoDeletePass();
    				break;
    			}
    			case 16:
    			{
    				controladorMetodoDiscoverPattern();
    				break;
    			}
    			case 17:
    			{
    				controladorMetodoAT();
    				break;
    			}
    		}
    	}
    }  
    
    
    /**
     * Este método, maneja la lógica de lanzamiento de ventanas emergentes y principales dependiendo de
     * la lógica del método de bypass del bloqueo de pantalla "UnlockScreen Samsung".
     * Caso ideal:
     * 1 VISTA_EMERGENTE_DES_MODO_BOOTLOADER
     * 2 VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY
     * 3 VISTA_EMERGENTE_DES_MODO_RECOVERY
     * 4 VISTA_EMERGENTE_DES_MODO_BOOTLOADER
     * 5 VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY
     * 6 VISTA_EMERGENTE_DES_FINALIZACION_EXITOSA
     * Si se produce un error o excepción en alguno de los estados se lanza ventana
     * VISTA_EMERGENTE_DES_FINALIZACION_FALLIDA
     */
    public void controladorMetodoDesbloqueoRecovery()
    {
    	if(getModelo().getEstadoDesbloqueo() == 0)
		{
			Platform.runLater(() ->
			{
				panelMetodoDesbloqueo.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_MODO_BOOTLOADER);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
		if(getModelo().getEstadoDesbloqueo() == 1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoDesbloqueo();
		}
		if(getModelo().getEstadoDesbloqueo() == 2)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_MODO_RECOVERY);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		else if(getModelo().getEstadoDesbloqueo() == 3)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();		
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_MODO_BOOTLOADER);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
		else if(getModelo().getEstadoDesbloqueo() == 4)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoDesbloqueo();
		}
		
		if(getModelo().getEstadoDesbloqueo() == 5)
		{
			getModelo().consultarMetodoDesbloqueo();
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FINALIZACION_EXITOSA);
					
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		if(getModelo().getEstadoDesbloqueo() == -1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				if(panelMetodoDesbloqueo!=null)
				{
					panelMetodoDesbloqueo.setEffect(getEfectoGaussianBlur());
				}
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FINALIZACION_FALLIDA);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
    }
    /**
     * Este método, maneja la lógica de lanzamiento de ventanas emergentes y principales dependiendo de
     * la lógica del método de bypass del bloqueo de pantalla "Delete Pass".
     * Caso ideal:
     * 1 VISTA_EMERGENTE_DES_MODO_BOOTLOADER
     * 2 VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY
     * 3 VISTA_EMERGENTE_DES_MODO_RECOVERY
     * 4 VISTA_EMERGENTE_DES_MODO_BOOTLOADER
     * 5 VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY
     * 6 VISTA_EMERGENTE_DES_FINALIZACION_EXITOSA
     * Si se produce un error o excepción en alguno de los estados se lanza ventana
     * VISTA_EMERGENTE_FINALIZACION_FALLIDA
     */
    public void controladorMetodoDeletePass()
    {
    	if(getModelo().getEstadoDesbloqueo() == 0)
		{
			Platform.runLater(() ->
			{
				panelMetodoDesbloqueo.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_MODO_BOOTLOADER);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
		if(getModelo().getEstadoDesbloqueo() == 1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoDesbloqueo();
		}
		if(getModelo().getEstadoDesbloqueo() == 2)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_MODO_RECOVERY);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		else if(getModelo().getEstadoDesbloqueo() == 3)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				//panelMetodoDesbloqueo.setEffect(getEfectoGaussianBlur());		
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_MODO_BOOTLOADER);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
		else if(getModelo().getEstadoDesbloqueo() == 4)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoDesbloqueo();
		}
		
		if(getModelo().getEstadoDesbloqueo() == 5)
		{
			getModelo().consultarMetodoDesbloqueo();
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FINALIZACION_EXITOSA);
					
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		if(getModelo().getEstadoDesbloqueo() == -1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				if(panelMetodoDesbloqueo!=null)
				{
					panelMetodoDesbloqueo.setEffect(getEfectoGaussianBlur());
				}
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FINALIZACION_FALLIDA);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
    }
  
    /**
     * Este método, maneja la lógica de lanzamiento de ventanas emergentes y principales dependiendo de
     * la lógica del método de bypass del bloqueo de pantalla "Discover Pattern".
     * Caso ideal:
     * 1 VISTA_EMERGENTE_DES_MODO_BOOTLOADER
     * 2 VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY
     * 3 VISTA_EMERGENTE_DES_MODO_RECOVERY
     * 5 VISTA_EMERGENTE_DES_MODO_BOOTLOADER
     * 6 VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY
     * 7 VISTA_EMERGENTE_DES_FINALIZACION_EXITOSA
     * Si se produce un error o excepción en alguno de los estados se lanza ventana
     * VISTA_EMERGENTE_DES_FINALIZACION_FALLIDA
     * @throws IOException 
     */
    public void controladorMetodoDiscoverPattern() throws IOException
    {
    	if(getModelo().getEstadoDesbloqueo() == 0)
		{
			Platform.runLater(() ->
			{
				panelMetodoDesbloqueo.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_MODO_BOOTLOADER);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
		if(getModelo().getEstadoDesbloqueo() == 1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoDesbloqueo();
		}
		if(getModelo().getEstadoDesbloqueo() == 2)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_MODO_RECOVERY);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		else if(getModelo().getEstadoDesbloqueo() == 3)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();		
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_MODO_BOOTLOADER);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
		else if(getModelo().getEstadoDesbloqueo() == 4)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoDesbloqueo();
		}
		
		if(getModelo().getEstadoDesbloqueo() == 5)
		{
			getModelo().consultarMetodoDesbloqueo();			
									
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_DISCOVER_FINALIZACION_EXITOSA);
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		if(getModelo().getEstadoDesbloqueo() == -1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				if(panelMetodoDesbloqueo!=null)
				{
					panelMetodoDesbloqueo.setEffect(getEfectoGaussianBlur());
				}
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FINALIZACION_FALLIDA);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
    }
    /**
     * Este método, maneja la lógica de lanzamiento de ventanas emergentes y principales dependiendo de
     * la lógica del método de bypass del bloqueo de pantalla "AT".
     * Caso ideal:
     * 1 VISTA_EMERGENTE_DES_MODO_BOOTLOADER
     * 2 VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY
     * 3 VISTA_EMERGENTE_DES_MODO_RECOVERY
     * 5 VISTA_EMERGENTE_DES_MODO_BOOTLOADER
     * 6 VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY
     * 7 VISTA_EMERGENTE_DES_FINALIZACION_EXITOSA
     * Si se produce un error o excepción en alguno de los estados se lanza ventana
     * VISTA_EMERGENTE_FINALIZACION_FALLIDA
     */
    public void controladorMetodoAT()
    {
    	int flag = 1;
    	if(getModelo().getEstadoDesbloqueo() == 0 && flag==1)
		{
    		flag=0;
			Platform.runLater(() ->
			{
				panelMetodoDesbloqueo.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_GEN_PUERTOS_AT);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
    	if(getModelo().getEstadoDesbloqueo() == 1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_CHANGE_PUERTOS_AT);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
    	if(getModelo().getEstadoDesbloqueo() == 2)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FINALIZACION_EXITOSA);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		else if(getModelo().getEstadoDesbloqueo() == -1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				if(panelMetodoDesbloqueo!=null)
				{
					panelMetodoDesbloqueo.setEffect(getEfectoGaussianBlur());
				}
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DES_FINALIZACION_FALLIDA);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
    }
}
