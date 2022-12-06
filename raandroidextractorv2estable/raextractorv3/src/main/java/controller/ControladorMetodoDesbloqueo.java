package main.java.controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import main.java.Main;

/**
 * Esta clase, es un controlador vinculado a las vistas:
 * VISTA_DETECCION_MANUAL y VISTA_EMERGENTE_SELECCION_DISPOSITIVO_INCORRECTA.
 * Además, hereda de la clase Controlador.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 * v2.0.0
 * @author Mantay Rodrigo Ariel
 * @author Salas Canalicchio Sergio Enrique
 *
 */
public class ControladorMetodoDesbloqueo extends Controlador
{	
	@FXML
    private AnchorPane panelMetodoDesbloqueo;
	
	@FXML
    private GridPane panelTitulo;

	@FXML
    private JFXComboBox<String> listaMetodos;
	
	@FXML
    private JFXButton botonSiguiente;

	@FXML
	private Text NombreDispositivo;
	
	/**
     * Este método, sirve para inicializar los nodos de la vista vinculada
     * al controlador ControladorDeteccionManual. Cada vez que se crea una instancia de
     * ControladorDeteccionManual se llama tanto a su constructor como así también al método initialize().
     * Cuando las variables panelListadoDispositivos y botonSiguiente sean distintas de nulo
     * se encuentra en la vista VISTA_DETECCION_MANUAL y aplica a los nodos la animación FadeIn. 
     * Además, consulta al modelo el listado de dispositivos soportados para visualizarlos en los ComboBox.
     * Cuando las variables panelListadoDispositivos y botonSiguiente sean nulas se encuentra en la vista
     * VISTA_EMERGENTE_SELECCION_DISPOSITIVO_INCORRECTA y no realiza ninguna acción.
     */
	public void initialize()
    {
		if(panelMetodoDesbloqueo!=null && botonSiguiente!=null && NombreDispositivo!=null)
		{
			getFadeIn().setNode(panelMetodoDesbloqueo);
    		getFadeIn().setSpeed(0.5);
    		getFadeIn().play();
    		
    		getPulse().setNode(botonSiguiente);
        	getPulse().setSpeed(2.0);
        	getPulse().setCycleCount(5);
        	getPulse().play();
        	
        	NombreDispositivo.setText(
					  " > " + getModelo().consultarDispositivoDetectado().getFabricante()
					+ " > " + getModelo().consultarDispositivoDetectado().getModelo()
					+ " > " + getModelo().consultarDispositivoDetectado().getVersionSo());
			getFadeIn().setNode(NombreDispositivo);
			getFadeIn().setSpeed(0.2);
			getFadeIn().play();
    		
			actualizarListados();
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
    	getMain().cambiarVentana(Main.VISTA_ELECCION);    
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonSiguiente.
     * Por lo tanto, su función es consultar el modelo para saber que dispositivo
     * se seleccionó en uno de los comboBox.
     * Dependiendo del estado del modelo, puede cambiar a ventana VISTA_UNIDAD_ALMACENAMIENTO
     * o lanzar la ventana emergente VISTA_EMERGENTE_SELECCION_DISPOSITIVO_INCORRECTA.
     * @param event Se produjo una click del mouse en el botonSiguiente.
     * @throws IOException
     *
     **/
    @FXML
    public void clickSiguiente(MouseEvent event) throws IOException 
    {
    	
    	if(listaMetodos.getValue()!=null)
    	{
    		getModelo().seleccionarMetodoDesbloqueo(listaMetodos.getValue());
    		getMain().cambiarVentana(Main.VISTA_DESBLOQUEO_PROGRESO );
    	}
    	
    	else
    	{
    		panelMetodoDesbloqueo.setEffect(getEfectoGaussianBlur());
    		getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_SELECCION_METODO_DESBLOQUEO_INCORRECTO);
    	}
 
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el comboBoxListaLg.
     * Su función es deseleccionar los comboBoxListaSamsung y comboBoxListaMotorola,
     * para evitar que haya más de dispositivo seleccionado.
     * @param event Se produjo una click del mouse en el comboBoxListaLg.
     */
    @FXML
    public void clickListaMetodos(MouseEvent event) 
    {
    
    }
    
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonAceptar de la vista
     * VISTA_EMERGENTE_SELECCION_DISPOSITIVO_INCORRECTA
     * Por lo tanto, su función es cerrar la ventana emergente
     * VISTA_EMERGENTE_SELECCION_DISPOSITIVO_INCORRECTA y volver a la vista VISTA_DETECCION_MANUAL.
     * @param event Se produjo una click del mouse en el botonAceptar.
     * @throws IOException
     */
    @FXML
    public void clickAceptar(MouseEvent event) throws IOException 
    {
    	getMain().getDialogStage().close();
    	getMain().cambiarVentana(Main.VISTA_METODO_DESBLOQUEO);    	
    }
    
    
    /**
     * Este método, es llamado por el método initialize().
     * Su función es mostrar en cada comboBox la lista de dispositivos soportados
     * consultando al modelo.
     */
    public void actualizarListados()
    {
    	listaMetodos.getItems().clear();
    	if(getModelo().consultarDispositivoDetectado().getMetodoUnlockScreenSamsung())
    	{
    		listaMetodos.getItems().add("Metodo UnlockScreen Samsung");
    	}
    	if(getModelo().consultarDispositivoDetectado().getmetodoDeletePass() )
    	{
    		listaMetodos.getItems().add("Metodo Delete Pass");
    	}
    	if(getModelo().consultarDispositivoDetectado().getmetodoDiscoverPattern())
    	{
    		listaMetodos.getItems().add("Metodo Discover Pattern");
    	}
    	if(getModelo().consultarDispositivoDetectado().getmetodoAT())
    	{
    		listaMetodos.getItems().add("Metodo AT");
    	}
        
    }
}