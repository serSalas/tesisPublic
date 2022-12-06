package main.java.controller;

import java.io.IOException;

import com.jfoenix.controls.JFXProgressBar;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import main.java.Main;

/**
 * Esta clase, es un controlador vinculado a las vistas: 
 * VISTA_EMERGENTE_MODO_BOOTLOADER, VISTA_EMERGENTE_FLASHEAR_RECOVERY, VISTA_EMERGENTE_MODO_RECOVERY,
 * VISTA_EMERGENTE_GENERAR_HASH, VISTA_EMERGENTE_MODO_DEPURACION, VISTA_EMERGENTE_EXPLOTAR_VULNERABILIDAD,
 * VISTA_EMERGENTE_MODO_MTP, VISTA_EMERGENTE_MODO_DOWNLOAD, VISTA_EMERGENTE_FINALIZACION_EXITOSA,
 * VISTA_EMERGENTE_FINALIZACION_FALLIDA, VISTA_EMERGENTE_CALCULO_TAMANIO_ALMACENAMIENTO y VISTA_EXTRACCION_PROGRESO.
 * Además, hereda de la clase Controlador.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 * v2.0.0
 * @author Mantay Rodrigo Ariel
 * @author Salas Canalicchio Sergio Enrique
 *
 */
public class ControladorExtraccionProgreso extends Controlador
{  	
	@FXML
    private AnchorPane panelExtraccionProgreso;
	
	@FXML
    private Text porcentaje;

    @FXML
    private JFXProgressBar barraProgreso;
    
    @FXML
    private Text cantidadBytes;
    
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
     */
    public void initialize()
    {
    	if(!estadoThread)
    	{
    		estadoThread = true;
    		hilo = new Thread(task);
    		hilo.setName("H1");
			hilo.setDaemon(true);
			hilo.start();
    	}
    	if(getModelo().getEstadoExtraccionDatos() != 0)
    	{
    		if(barraProgreso!=null && porcentaje!=null && cantidadBytes!=null)
        	{
        		barraProgreso.setProgress(0);
    	    	porcentaje.setText("0%");
    	    	cantidadBytes.setText("");
    	    	
    	    	barraProgreso.progressProperty().unbind();
    	    	barraProgreso.progressProperty().bind(task.progressProperty());
    	    	hilo = new Thread(task);
    	    	hilo.setName("H2");
    	    	hilo.setDaemon(true);
    	    	hilo.start();
        	}
    	}
    	if(mensajeError!=null)
    	{
    		mensajeError.setText(getModelo().consultarMensajeError());
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
    	getModelo().consultarMetodoExtraccionDatos();
    	if(getModelo().getEstadoExtraccionDatos() == 1 || getModelo().getEstadoExtraccionDatos() == 6)
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
    	getModelo().consultarMetodoExtraccionDatos();
    	if(getModelo().getEstadoExtraccionDatos() == 4)
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
     * si se encuentra el dispositivo en modo depuración.
     * @param event Se produjo una click del mouse en el botonAceptar.
     */
    @FXML
    public void clickChequearModoDepuracion(MouseEvent event) 
    {
    	getModelo().consultarMetodoExtraccionDatos();
    	if(getModelo().getEstadoExtraccionDatos() == 1)
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
    	getModelo().consultarMetodoExtraccionDatos();
    	if(getModelo().getEstadoExtraccionDatos() == 1)
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
    	getModelo().consultarMetodoExtraccionDatos();
    	if(getModelo().getEstadoExtraccionDatos() == 1)
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
		getMain().cambiarVentana(Main.VISTA_METODO_EXTRACCION);
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
    
    /**
     * Este método, es llamado por hilo "H1" para realizar tarea de extracción de datos consultado
     * modelo. y también es llamado por hilo "H2" para realizar tarea de mostrar en la interfaz de usuario
     * porcentaje, barra  de progreso y tamaño parcial de extracción de datos consultando también el
     * modelo.
     */
    public Task<Object> task = new Task<Object>() 
    {
    	@Override
    	protected Object call() throws InterruptedException  
    	{
    		if(hilo.getName().equals("H1"))
    		{
    			controladorExtraccionDatos();
    		}
    		else if(hilo.getName().equals("H2"))
    		{
    			long porc = 0;
				String infMem = "";
    	    	
				while(porc < 100 && getModelo().getEstadoExtraccionDatos() > 0) //Hasta que no llegue al 100% continuara avanzando la barra de progreso y el porcentaje
				{
					Thread.sleep(1000);
					porc = getModelo().consultarPorcentajeExtraccionDatos();
					infMem = getModelo().consultarTamanioParcialExtraccionDatos();
					
					updateProgress(porc, 100);
                    porcentaje.setText(String.valueOf(porc)+"%");
                    cantidadBytes.setText(infMem);
				}
				
    			Thread.sleep(1000);
				controladorExtraccionDatos();
    		}
    		return true;
    	}
    };
    
    /**
     * Este método, provee exclusión mutua ya que es accedido por un único hilo a la vez
     * ya que se colocó la sentencia synchronized y un recurso compartido por los hilos.
     * Cada hilo ingresa al método para consultar al modelo.
     */
    public void controladorExtraccionDatos()
    {
    	synchronized(cerrojo)
    	{
    		switch(getModelo().getEstadoModelo())
    		{
    			case 9:
    			{
    				controladorMetodoExtraccionDirtyCow();
    				break;
    			}
    			case 10:
    			{
    				controladorMetodoExtraccionRecovery();
    				break;
    			}
    			case 11:
    			{
    				controladorMetodoExtraccionLglaf();
    				break;
    			}
    			case 12:
    			{
    				controladorMetodoExtraccionMtp();
    				break;
    			}
    			case 13:
    			{
    				controladorMetodoExtraccionAdb();
    				break;
    			}
    			case 18:
    			{
    				controladorMetodoExtraccionDirtyCowModificado();
    				break;
    			}
    		}
    	}
    }  
    
    /**
     * Este método, maneja la lógica de lanzamiento de ventanas emergentes y principales dependiendo de
     * la lógica del metodo de extraccion de datos dirty cow.
     * Caso ideal:
     * 1 VISTA_EMERGENTE_MODO_DEPURACION
     * 2 VISTA_EMERGENTE_EXPLOTAR_VULNERABILIDAD
     * 3 VISTA_EXTRACCION_PROGRESO
     * 4 VISTA_EMERGENTE_GENERAR_HASH
     * 5 VISTA_EMERGENTE_FINALIZACION_EXITOSA
     * Si se produce un error o excepción en alguno de los estados se lanza ventana
     * VISTA_EMERGENTE_FINALIZACION_FALLIDA
     */
    public void controladorMetodoExtraccionDirtyCow()
    {
    	if(getModelo().getEstadoExtraccionDatos() == 0)
		{
    		Platform.runLater(() ->
			{
				panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_MODO_DEPURACION);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
    	else if(getModelo().getEstadoExtraccionDatos() == 1)
		{
			getModelo().consultarMetodoExtraccionDatos();
		}
		if(getModelo().getEstadoExtraccionDatos() == 2)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_EXPLOTAR_VULNERABILIDAD);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoExtraccionDatos();
		}
		if(getModelo().getEstadoExtraccionDatos() == 3)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().cambiarVentana(Main.VISTA_EXTRACCION_PROGRESO);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
    		getModelo().consultarMetodoExtraccionDatos();
    		return;
		}
		else if(getModelo().getEstadoExtraccionDatos() == 4)
		{
			Platform.runLater(() ->
			{
				panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_GENERAR_HASH);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoExtraccionDatos();
		}
		if(getModelo().getEstadoExtraccionDatos() == 5)
		{
			getModelo().consultarMetodoExtraccionDatos();
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FINALIZACION_EXITOSA);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		if(getModelo().getEstadoExtraccionDatos() == -1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				if(panelExtraccionProgreso!=null)
				{
					panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				}
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FINALIZACION_FALLIDA);
					
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
     * la lógica del metodo de extraccion de datos Recovery.
     * Caso ideal:
     * 1 VISTA_EMERGENTE_MODO_BOOTLOADER
     * 2 VISTA_EMERGENTE_FLASHEAR_RECOVERY
     * 3 VISTA_EMERGENTE_MODO_RECOVERY
     * 4 VISTA_EXTRACCION_PROGRESO
     * 5 VISTA_EMERGENTE_MODO_BOOTLOADER
     * 6 VISTA_EMERGENTE_FLASHEAR_RECOVERY
     * 7 VISTA_EMERGENTE_GENERAR_HASH
     * 8 VISTA_EMERGENTE_FINALIZACION_EXITOSA
     * Si se produce un error o excepción en alguno de los estados se lanza ventana
     * VISTA_EMERGENTE_FINALIZACION_FALLIDA
     */
    public void controladorMetodoExtraccionRecovery()
    {
    	if(getModelo().getEstadoExtraccionDatos() == 0)
		{
			Platform.runLater(() ->
			{
				panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_MODO_BOOTLOADER);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
    	else if(getModelo().getEstadoExtraccionDatos() == 1)
    	{
    		getModelo().consultarMetodoExtraccionDatos();
    	}
		if(getModelo().getEstadoExtraccionDatos() == 2)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FLASHEAR_RECOVERY);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoExtraccionDatos();
		}
		if(getModelo().getEstadoExtraccionDatos() == 3)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_MODO_RECOVERY);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		else if(getModelo().getEstadoExtraccionDatos() == 4)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().cambiarVentana(Main.VISTA_EXTRACCION_PROGRESO);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
    		getModelo().consultarMetodoExtraccionDatos();
		}
		else if(getModelo().getEstadoExtraccionDatos() == 5)
		{
			Platform.runLater(() ->
			{
				panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_MODO_BOOTLOADER);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
		else if(getModelo().getEstadoExtraccionDatos() == 6)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FLASHEAR_RECOVERY);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoExtraccionDatos();
		}
		if(getModelo().getEstadoExtraccionDatos() == 7)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_GENERAR_HASH);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoExtraccionDatos();
		}
		if(getModelo().getEstadoExtraccionDatos() == 8)
		{
			getModelo().consultarMetodoExtraccionDatos();
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FINALIZACION_EXITOSA);
					
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		if(getModelo().getEstadoExtraccionDatos() == -1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				if(panelExtraccionProgreso!=null)
				{
					panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				}
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FINALIZACION_FALLIDA);
					
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
     * la logica del metodo de extraccion de datos Lg laf.
     * Caso ideal:
     * 1 VISTA_EMERGENTE_MODO_DOWNLOAD
     * 2 VISTA_EXTRACCION_PROGRESO
     * 3 VISTA_EMERGENTE_GENERAR_HASH
     * 4 VISTA_EMERGENTE_FINALIZACION_EXITOSA
     * Si se produce un error o excepción en alguno de los estados se lanza ventana
     * VISTA_EMERGENTE_FINALIZACION_FALLIDA
     */
    public void controladorMetodoExtraccionLglaf() 
    {
    	if(getModelo().getEstadoExtraccionDatos() == 0)
		{
    		Platform.runLater(() ->
			{
				panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_MODO_DOWNLOAD);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
    	else if(getModelo().getEstadoExtraccionDatos() == 1)
    	{
    		getModelo().consultarMetodoExtraccionDatos();
    	}
    	if(getModelo().getEstadoExtraccionDatos() == 2)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().cambiarVentana(Main.VISTA_EXTRACCION_PROGRESO);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
    		getModelo().consultarMetodoExtraccionDatos();
    		return;
		}
    	if(getModelo().getEstadoExtraccionDatos() == 3)
    	{
    		getModelo().consultarMetodoExtraccionDatos();
    	}
    	if(getModelo().getEstadoExtraccionDatos() == 4)
    	{
    		Platform.runLater(() ->
			{
				panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_GENERAR_HASH);
					
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoExtraccionDatos();
    	}
    	if(getModelo().getEstadoExtraccionDatos() == 5)
		{
			getModelo().consultarMetodoExtraccionDatos();
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FINALIZACION_EXITOSA);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
    	if(getModelo().getEstadoExtraccionDatos() == -1)
		{
    		Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				if(panelExtraccionProgreso!=null)
				{
					panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				}
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FINALIZACION_FALLIDA);
					
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
     * la lógica del método de extracción de datos MTP.
     * Caso ideal:
     * 1 VISTA_EMERGENTE_MODO_MTP
     * 2 VISTA_EMERGENTE_CALCULO_TAMANIO_ALMACENAMIENTO
     * 3 VISTA_EXTRACCION_PROGRESO
     * 4 VISTA_EMERGENTE_GENERAR_HASH
     * 5 VISTA_EMERGENTE_FINALIZACION_EXITOSA
     * Si se produce un error o excepción en alguno de los estados se lanza ventana
     * VISTA_EMERGENTE_FINALIZACION_FALLIDA
     */
    public void controladorMetodoExtraccionMtp()
    {
    	if(getModelo().getEstadoExtraccionDatos() == 0)
		{
    		Platform.runLater(() ->
			{
				panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_MODO_MTP);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
    	else if(getModelo().getEstadoExtraccionDatos() == 1)
    	{
    		Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_CALCULO_TAMANIO_ALMACENAMIENTO);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoExtraccionDatos();
    	}
    	if(getModelo().getEstadoExtraccionDatos() == 2)
		{
    		getModelo().consultarMetodoExtraccionDatos();
		}
    	if(getModelo().getEstadoExtraccionDatos() == 3)
    	{
    		Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().cambiarVentana(Main.VISTA_EXTRACCION_PROGRESO);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
    		getModelo().consultarMetodoExtraccionDatos();
    		return;
    	}
    	else if(getModelo().getEstadoExtraccionDatos() == 4)
		{
			Platform.runLater(() ->
			{
				panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_GENERAR_HASH);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoExtraccionDatos();
		}
		if(getModelo().getEstadoExtraccionDatos() == 5)
		{
			getModelo().consultarMetodoExtraccionDatos();
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FINALIZACION_EXITOSA);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		if(getModelo().getEstadoExtraccionDatos() == -1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				if(panelExtraccionProgreso!=null)
				{
					panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				}
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FINALIZACION_FALLIDA);
					
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
     * la logica del metodo de extraccion de datos ADB.
     * Caso ideal:
     * 1 VISTA_EMERGENTE_MODO_DEPURACION
     * 2 VISTA_EMERGENTE_CALCULO_TAMANIO_ALMACENAMIENTO
     * 3 VISTA_EXTRACCION_PROGRESO
     * 4 VISTA_EMERGENTE_GENERAR_HASH
     * 5 VISTA_EMERGENTE_FINALIZACION_EXITOSA
     * Si se produce un error o excepción en alguno de los estados se lanza ventana
     * VISTA_EMERGENTE_FINALIZACION_FALLIDA
     */
    public void controladorMetodoExtraccionAdb() 
    {
    	if(getModelo().getEstadoExtraccionDatos() == 0)
		{
    		Platform.runLater(() ->
			{
				panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_MODO_DEPURACION);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
    	else if(getModelo().getEstadoExtraccionDatos() == 1)
    	{
    		Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_CALCULO_TAMANIO_ALMACENAMIENTO);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoExtraccionDatos();
    	}
    	if(getModelo().getEstadoExtraccionDatos() == 2)
		{
    		getModelo().consultarMetodoExtraccionDatos();
		}
    	if(getModelo().getEstadoExtraccionDatos() == 3)
    	{
    		Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().cambiarVentana(Main.VISTA_EXTRACCION_PROGRESO);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
    		getModelo().consultarMetodoExtraccionDatos();
    		return;
    	}
    	else if(getModelo().getEstadoExtraccionDatos() == 4)
		{
			Platform.runLater(() ->
			{
				panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_GENERAR_HASH);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoExtraccionDatos();
		}
		if(getModelo().getEstadoExtraccionDatos() == 5)
		{
			getModelo().consultarMetodoExtraccionDatos();
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FINALIZACION_EXITOSA);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		if(getModelo().getEstadoExtraccionDatos() == -1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				if(panelExtraccionProgreso!=null)
				{
					panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				}
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FINALIZACION_FALLIDA);
					
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
     * la lógica del metodo de extraccion de datos dirty cow Modificado.
     * Caso ideal:
     * 1 VISTA_EMERGENTE_MODO_DEPURACION
     * 2 VISTA_EMERGENTE_EXPLOTAR_VULNERABILIDAD
     * 3 VISTA_EXTRACCION_PROGRESO
     * 4 VISTA_EMERGENTE_GENERAR_HASH
     * 5 VISTA_EMERGENTE_FINALIZACION_EXITOSA
     * Si se produce un error o excepción en alguno de los estados se lanza ventana
     * VISTA_EMERGENTE_FINALIZACION_FALLIDA
     */
    public void controladorMetodoExtraccionDirtyCowModificado()
    {
    	if(getModelo().getEstadoExtraccionDatos() == 0)
		{
    		Platform.runLater(() ->
			{
				panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_MODO_DEPURACION);
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
			);
		}
    	else if(getModelo().getEstadoExtraccionDatos() == 1)
		{
			getModelo().consultarMetodoExtraccionDatos();
		}
		if(getModelo().getEstadoExtraccionDatos() == 2)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_EXPLOTAR_VULNERABILIDAD);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoExtraccionDatos();
		}
		if(getModelo().getEstadoExtraccionDatos() == 3)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().cambiarVentana(Main.VISTA_EXTRACCION_PROGRESO);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
    		getModelo().consultarMetodoExtraccionDatos();
    		return;
		}
		else if(getModelo().getEstadoExtraccionDatos() == 4)
		{
			Platform.runLater(() ->
			{
				panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_GENERAR_HASH);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
			getModelo().consultarMetodoExtraccionDatos();
		}
		if(getModelo().getEstadoExtraccionDatos() == 5)
		{
			getModelo().consultarMetodoExtraccionDatos();
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FINALIZACION_EXITOSA);
					
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
    		);
		}
		if(getModelo().getEstadoExtraccionDatos() == -1)
		{
			Platform.runLater(() ->
			{
				getMain().getDialogStage().close();
				if(panelExtraccionProgreso!=null)
				{
					panelExtraccionProgreso.setEffect(getEfectoGaussianBlur());
				}
				try 
				{
					getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_FINALIZACION_FALLIDA);
					
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
