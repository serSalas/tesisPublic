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
 * VISTA_METODO_EXTRACCION y VISTA_EMERGENTE_SELECCION_METODO_EXTRACCION_INCORRECTA.
 * Además, hereda de la clase Controlador.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 * v2.0.0
 * @author Mantay Rodrigo Ariel
 * @author Salas Canalicchio Sergio Enrique
 *
 */
public class ControladorMetodoExtraccion extends Controlador
{   
	@FXML
	private AnchorPane panelMetodoExtraccion;
	
	@FXML
    private GridPane panelListadoMetodosExtraccion;
	
	@FXML
    private GridPane panelTitulo;
	
	@FXML
    private GridPane panelAyuda;
	
	@FXML
	private Text NombreDispositivo;
	
	@FXML
    private JFXComboBox<String> listaMetodosFisicos;

    @FXML
    private JFXComboBox<String> listaMetodosLogicos;
    
    @FXML
    private JFXButton botonSiguiente;
    
    /**
     * Este método, sirve para inicializar los nodos de la vista vinculada
     * al controlador ControladorMetodoExtraccion. Cada vez que se crea una instancia de
     * ControladorMetodoExtraccion se llama tanto a su constructor como así también al método initialize().
     * Cuando las variables panelListadoMetodosExtraccion, botonSiguiente y NombreDispositivo
     * sean distintas de nulo se encuentra en la vista VISTA_METODO_EXTRACCION
     * y aplica a los nodos la animación FadeIn. Además, consulta al modelo
     * el listado de métodos de extracción soportados por el dispositivo Android
     * para visualizarlos en los ComboBox.
     * Cuando las variables panelListadoMetodosExtraccion, botonSiguiente y NombreDispositivo
     * sean nulas se encuentra en la vista
     * VISTA_EMERGENTE_SELECCION_METODO_EXTRACCION_INCORRECTA y no realiza ninguna acción.
     */
	public void initialize()
    {
		if(panelListadoMetodosExtraccion!=null && botonSiguiente!=null && NombreDispositivo!=null)
		{
			getFadeIn().setNode(panelListadoMetodosExtraccion);
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
     * Por lo tanto, su función es cambiar a la vista VISTA_UNIDAD_ALMACENAMIENTO.
     * @param event Se produjo una click del mouse en el botonIrAtras.
     * @throws IOException
     */
	@FXML
    public void clickIrAtras(MouseEvent event) throws IOException 
    {
    	getMain().cambiarVentana(Main.VISTA_UNIDAD_ALMACENAMIENTO);
    }
    
	/**
     * Este método, es llamado cada vez que se pulsa en el botón botonSiguiente.
     * Por lo tanto, su función es consultar el modelo para saber que metodo de extraccion
     * se seleccionó en uno de los comboBox.
     * Dependiendo del estado del modelo, puede cambiar a ventana VISTA_EXTRACCION_PROGRESO
     * o lanzar la ventana emrgente VISTA_EMERGENTE_SELECCION_METODO_EXTRACCION_INCORRECTA
     * @param event Se produjo una click del mouse en el botonSiguiente.
     * @throws IOException
     */
    @FXML
    public void clickSiguiente(MouseEvent event) throws IOException 
    {
    	if(listaMetodosFisicos.getValue()!=null)
    	{
    		getModelo().seleccionarMetodoExtraccionDatos(listaMetodosFisicos.getValue());
    		getMain().cambiarVentana(Main.VISTA_EXTRACCION_PROGRESO);
    	}
    	else if(listaMetodosLogicos.getValue()!=null)
    	{
    		getModelo().seleccionarMetodoExtraccionDatos(listaMetodosLogicos.getValue());
    		getMain().cambiarVentana(Main.VISTA_EXTRACCION_PROGRESO);
    	}
    	else
    	{
    		panelMetodoExtraccion.setEffect(getEfectoGaussianBlur());
    		getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_SELECCION_METODO_EXTRACCION_INCORRECTA);
    	}
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el comboBoxListaMetodosFisicos.
     * Su función es deseleccionar el comboBoxListaMetodosLogicos,
     * para evitar que haya más de un método de extracción seleccionado.
     * @param event Se produjo una click del mouse en el comboBoxListaMetodosFisicos.
     */
    @FXML
    public void clickListaMetodosFisicos(MouseEvent event) 
    {
    	listaMetodosLogicos.setValue(null);
    }

    /**
     * Este método, es llamado cada vez que se pulsa en el comboBoxListaMetodosLogicos.
     * Su función es deseleccionar el comboBoxListaMetodosFisicos,
     * para evitar que haya más de un método de extracción seleccionado.
     * @param event Se produjo una click del mouse en el comboBoxListaMetodosLogicos.
     */
    @FXML
    public void clickListaMetodosLogicos(MouseEvent event) 
    {
    	listaMetodosFisicos.setValue(null);
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonAceptar de la vista
     * VISTA_EMERGENTE_SELECCION_METODO_EXTRACCION_INCORRECTA
     * Por lo tanto, su función es cerrar la ventana emergente
     * VISTA_EMERGENTE_SELECCION_METODO_EXTRACCION_INCORRECTA y volver a la vista VISTA_METODO_EXTRACCION.
     * @param event Se produjo una click del mouse en el botonAceptar.
     * @throws IOException
     */
    @FXML
    public void clickAceptar(MouseEvent event) throws IOException 
    {
    	getMain().getDialogStage().close();
    	getMain().cambiarVentana(Main.VISTA_METODO_EXTRACCION);
    	
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonAyuda de la vista
     * VISTA_METODO_EXTRACCION.
     * Por lo tanto, su función es mostrar información acerca de la vista VISTA_METODO_EXTRACCION. 
     * @param event Se produjo una click del mouse en el botonAyuda.
     */
    @FXML
    public void clickIrAyuda(MouseEvent event) 
    {
    	
    	if(panelAyuda.isVisible())
    	{
    		panelAyuda.setVisible(false);
    		panelTitulo.setEffect(null);
    		panelListadoMetodosExtraccion.setEffect(null);
    		botonSiguiente.setDisable(false);
    		listaMetodosFisicos.setDisable(false);
    		listaMetodosLogicos.setDisable(false);
    	}
    	else
    	{
    		panelAyuda.setVisible(true);
    		panelTitulo.setEffect(getEfectoGaussianBlur());
    		panelListadoMetodosExtraccion.setEffect(getEfectoGaussianBlur());
    		botonSiguiente.setDisable(true);
    		listaMetodosFisicos.setDisable(true);
    		listaMetodosLogicos.setDisable(true);
    	}

    }
    
    /**
     * Este método, es llamado por el método initialize().
     * Su función es mostrar en cada comboBox la lista de metodos de extraccion soportados
     * por el dispositivo Android, consultando al modelo.
     */
    public void actualizarListados()
    {
    	listaMetodosFisicos.getItems().clear();
    	listaMetodosLogicos.getItems().clear();
    	if(getModelo().consultarDispositivoDetectado().getMetodoDirtyCow())
    	{
    		listaMetodosFisicos.getItems().add("Metodo Dirty Cow");
    	}
    	if(getModelo().consultarDispositivoDetectado().getMetodoRecovery())
    	{
    		listaMetodosFisicos.getItems().add("Metodo Recovery");
    	}
    	if(getModelo().consultarDispositivoDetectado().getMetodoLgLaf())
    	{
    		listaMetodosFisicos.getItems().add("Metodo Lg laf");
    	}
    	if(getModelo().consultarDispositivoDetectado().getMetodoMtp())
    	{
    		listaMetodosLogicos.getItems().add("Metodo Mtp");
    	}
    	if(getModelo().consultarDispositivoDetectado().getMetodoAdb())
    	{
    		listaMetodosLogicos.getItems().add("Metodo Adb");
    	}
    	if(getModelo().consultarDispositivoDetectado().getMetodoDirtyCowModificado())
    	{
    		listaMetodosFisicos.getItems().add("Metodo Dirty Cow Modificado");
    	}
    }
}
