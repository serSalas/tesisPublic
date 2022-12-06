package main.java.controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.java.Main;

/**
 * Esta clase, es un controlador vinculado a las vistas: 
 * VISTA_DETECCION_AUTOMATICA, VISTA_EMERGENTE_DETECCION_ADB_CORRECTA 
 * y VISTA_EMERGENTE_DETECCION_ADB_INCORRECTA.
 * Además, hereda de la clase Controlador.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 * v2.0.0
 * @author Mantay Rodrigo Ariel
 * @author Salas Canalicchio Sergio Enrique
 *
 */
public class ControladorDeteccionAutomatica extends Controlador
{
	@FXML
    private AnchorPane panelDeteccionAutomatica;
	
	@FXML
    private GridPane panelTitulo;
	
	@FXML
	private GridPane panelImages;
	
	@FXML
    private GridPane panelAyuda;
	
	@FXML
    private Text mensajeDeteccionAutomatica;
	
	@FXML
    private JFXButton botonSiguiente;
	
	private static String mensaje;
    
	/**
     * Este método, sirve para inicializar los nodos de la vista vinculada
     * al controlador ControladorDeteccionAutomatica. Cada vez que se crea una instancia de
     * ControladorDeteccionAutomatica se llama tanto a su constructor como así también al método initialize().
     * Cuando la variable mensajeDeteccionAutomatica sea nula se encuentra en la vista VISTA_DETECCION_AUTOMATICA
     * y no realiza ninguna inicialización de sus nodos.
     * Cuando las variables panelImages y botonSiguiente sean nulas se encuentra en la vista
     * VISTA_EMERGENTE_DETECCION_ADB_CORRECTA o VISTA_EMERGENTE_DETECCION_ADB_INCORRECTA.
     * Además, en este estado se seteará un mensaje en el nodo mensajeDeteccionAutomatica
     * dependiendo del estado del modelo.
     */
    public void initialize()
    {
    	if(mensajeDeteccionAutomatica!=null)
    	{
    		mensajeDeteccionAutomatica.setText(mensaje);
    	}
    	if(panelImages!=null && botonSiguiente!=null)
    	{
    		getFadeIn().setNode(panelImages);
    		getFadeIn().setSpeed(0.5);
    		getFadeIn().play();
    		
    		getPulse().setNode(botonSiguiente);
        	getPulse().setSpeed(2.0);
        	getPulse().setCycleCount(5);
        	getPulse().play();
    	}
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonIrAtras.
     * Por lo tanto, su función es cambiar a la vista VISTA_TIPO_DETECCION.
     * @param event Se produjo una click del mouse en el botonIrAtras.
     * @throws IOException
     */
    @FXML
    public void clickIrAtras(MouseEvent event) throws IOException 
    {
    	getMain().cambiarVentana(Main.VISTA_TIPO_DETECCION);
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonAyuda de la vista
     * VISTA_DETECCION_AUTOMATICA.
     * Por lo tanto, su función es mostrar información acerca de la vista VISTA_DETECCION_AUTOMATICA. 
     * @param event Se produjo una click del mouse en el botonAyuda.
     */
    @FXML
    public void clickIrAyuda(MouseEvent event) 
    {
    	if(panelAyuda.isVisible())
    	{
    		panelAyuda.setVisible(false);
    		panelTitulo.setEffect(null);
    		panelImages.setEffect(null);
    		botonSiguiente.setDisable(false);
    	}
    	else
    	{
    		panelAyuda.setVisible(true);
    		panelTitulo.setEffect(getEfectoGaussianBlur());
    		panelImages.setEffect(getEfectoGaussianBlur());
    		botonSiguiente.setDisable(true);
    	}
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonSiguiente.
     * Por lo tanto, su función es consultar el modelo para saber si detectó dispositivo
     * Android conectado por Adb.
     * Dependiendo del estado del modelo lanzará cierta ventana emergente.
     * La ventana emergente puede ser:
     * VISTA_EMERGENTE_DETECCION_ADB_CORRECTA
     * o VISTA_EMERGENTE_DETECCION_ADB_INCORRECTA.
     * Además, se aplica a la ventana VISTA_DETECCION_AUTOMATICA el efecto GaussianBlur.
     * @param event Se produjo una click del mouse en el botonSiguiente.
     * @throws IOException
     * @throws InterruptedException
     */
    @FXML
    public void clickSiguiente(MouseEvent event) throws IOException, InterruptedException 
    {
    	panelDeteccionAutomatica.setEffect(getEfectoGaussianBlur());
		
    	getModelo().solicitarDeteccionAutomaticaDispositivo();
    	mostrarDetalles();
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonAceptar de la vista
     * VISTA_EMERGENTE_DETECCION_ADB_CORRECTA.
     * Por lo tanto, su función es cerrar la ventana emergente
     * VISTA_EMERGENTE_DETECCION_ADB_CORRECTA y cambiar a la vista VISTA_UNIDAD_ALMACENAMIENTO.
     * @param event Se produjo una click del mouse en el botonAceptar.
     * @throws IOException
     */
    @FXML
    public void clickAceptar_1(MouseEvent event) throws IOException 
    {   
    	getMain().getDialogStage().close();
    	getMain().cambiarVentana(Main.VISTA_ELECCION);
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonAceptar de la vista
     * VISTA_EMERGENTE_DETECCION_ADB_INCORRECTA.
     * Por lo tanto, su función es cerrar la ventana emergente
     * VISTA_EMERGENTE_DETECCION_ADB_INCORRECTA y volver a la vista VISTA_TIPO_DETECCION.
     * @param event Se produjo una click del mouse en el botonAceptar.
     * @throws IOException
     */
    @FXML
    public void clickAceptar_2(MouseEvent event) throws IOException 
    {
    	getMain().getDialogStage().close();
    	getMain().cambiarVentana(Main.VISTA_TIPO_DETECCION);
    }
    
    /**
     * Este método, es llamado por el metodo clickSiguiente().
     * Su función es, dependiendo del estado del modelo setear un mensaje específico
     * para que de esta manera el nodo mensajeDeteccionAutomatica muestre dicho mensaje.
     * Además, lanzará una ventana emergente específica de dicho mensaje.
     * @throws IOException
     */
    public void mostrarDetalles() throws IOException
    {
    	switch(getModelo().getEstadoModelo())
    	{
    		case 3:
    		{
    			mensaje = getModelo().consultarMensajeDeteccion();
    			getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DETECCION_ADB_CORRECTA);
    			break;
    		}
    		case 4:
    		{
    			mensaje = getModelo().consultarMensajeDeteccion();
    			getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_DETECCION_ADB_INCORRECTA);
    			break;
    		}
    	}    	
    }
}