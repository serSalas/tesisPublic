package main.java.controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
public class ControladorDeteccionManual extends Controlador
{	
	@FXML
    private AnchorPane panelDeteccionManual;
	
	@FXML
    private GridPane panelTitulo;
	
	@FXML
    private GridPane panelListadoDispositivos;
	
	@FXML
	private GridPane panelAyuda;

	@FXML
    private JFXComboBox<String> listaLg;
	
	@FXML
    private JFXComboBox<String> listaSony;
	
	@FXML
    private JFXComboBox<String> listaSamsung;
	
	@FXML
    private JFXComboBox<String> listaMotorola;
	
	@FXML
    private JFXButton botonSiguiente;
    
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
		if(panelListadoDispositivos!=null && botonSiguiente!=null)
		{
			getFadeIn().setNode(panelListadoDispositivos);
    		getFadeIn().setSpeed(0.5);
    		getFadeIn().play();
    		
    		getPulse().setNode(botonSiguiente);
        	getPulse().setSpeed(2.0);
        	getPulse().setCycleCount(5);
        	getPulse().play();
    		
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
    	getMain().cambiarVentana(Main.VISTA_TIPO_DETECCION);    
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonSiguiente.
     * Por lo tanto, su función es consultar el modelo para saber que dispositivo
     * se seleccionó en uno de los comboBox.
     * Dependiendo del estado del modelo, puede cambiar a ventana VISTA_UNIDAD_ALMACENAMIENTO
     * o lanzar la ventana emergente VISTA_EMERGENTE_SELECCION_DISPOSITIVO_INCORRECTA.
     * @param event Se produjo una click del mouse en el botonSiguiente.
     * @throws IOException
     */
    @FXML
    public void clickSiguiente(MouseEvent event) throws IOException 
    {
    	if(listaLg.getValue()!=null)
    	{
    		getModelo().solicitarDeteccionManualDispositivo(listaLg.getValue());
    	}
    	else if(listaSamsung.getValue()!=null)
    	{
    		getModelo().solicitarDeteccionManualDispositivo(listaSamsung.getValue());
    	}
    	else if(listaMotorola.getValue()!=null)
    	{
    		getModelo().solicitarDeteccionManualDispositivo(listaMotorola.getValue());
    	}
    	else if(listaSony.getValue()!=null)
    	{
    		getModelo().solicitarDeteccionManualDispositivo(listaSony.getValue());
    	}
    	else
    	{
    		getModelo().solicitarDeteccionManualDispositivo("NA, NA, NA, NA");
    	}
    	
    	switch(getModelo().getEstadoModelo())
    	{
    		case 5:
    		{
    			getMain().cambiarVentana(Main.VISTA_ELECCION);
    			break;
    		}
    		case 6:
    		{
    			panelDeteccionManual.setEffect(getEfectoGaussianBlur());
        		getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_SELECCION_DISPOSITIVO_INCORRECTA);
    			break;
    		}
    	}
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el comboBoxListaLg.
     * Su función es deseleccionar los comboBoxListaSamsung y comboBoxListaMotorola,
     * para evitar que haya más de dispositivo seleccionado.
     * @param event Se produjo una click del mouse en el comboBoxListaLg.
     */
    @FXML
    public void clickListaLg(MouseEvent event) 
    {
    	listaSamsung.setValue(null);
    	listaMotorola.setValue(null);
    	listaSony.setValue(null);
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el comboBoxListaSamsung.
     * Su función es deseleccionar los comboListaBoxLg y comboBoxListaMotorola,
     * para evitar que haya más de dispositivo seleccionado.
     * @param event Se produjo una click del mouse en el comboBoxListaMotorola.
     */
    @FXML
    public void clickListaSamsung(MouseEvent event) 
    {
    	listaLg.setValue(null);
    	listaMotorola.setValue(null);
    	listaSony.setValue(null);
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el comboBoxListaMotorola.
     * Su función es deseleccionar los comboBoxListaLg y comboBoxListaSamsung,
     * para evitar que haya más de dispositivo seleccionado.
     * @param event Se produjo una click del mouse en el comboBoxListMotorola.
     */
    @FXML
    public void clickListaMotorola(MouseEvent event) 
    {
    	listaLg.setValue(null);
    	listaSamsung.setValue(null);
    	listaSony.setValue(null);
    }
    
    
    public void clickListaSony(MouseEvent event) 
    {
    	listaLg.setValue(null);
    	listaSamsung.setValue(null);
    	listaMotorola.setValue(null);
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
    	getMain().cambiarVentana(Main.VISTA_DETECCION_MANUAL);
    	
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonAyuda de la vista
     * VISTA_DETECCION_MANUAL.
     * Por lo tanto, su función es mostrar información acerca de la vista VISTA_DETECCION_MANUAL. 
     * @param event Se produjo una click del mouse en el botonAyuda.
     */
    @FXML
    public void clickIrAyuda(MouseEvent event) 
    {
    	if(panelAyuda.isVisible())
    	{
    		panelAyuda.setVisible(false);
    		panelTitulo.setEffect(null);
    		panelListadoDispositivos.setEffect(null);
    		botonSiguiente.setDisable(false);
    		listaSamsung.setDisable(false);
    		listaLg.setDisable(false);
    		listaMotorola.setDisable(false);
    		listaSony.setDisable(false);
    	}
    	else
    	{
    		panelAyuda.setVisible(true);
    		panelTitulo.setEffect(getEfectoGaussianBlur());
    		panelListadoDispositivos.setEffect(getEfectoGaussianBlur());
    		botonSiguiente.setDisable(true);
    		listaSamsung.setDisable(true);
    		listaLg.setDisable(true);
    		listaMotorola.setDisable(true);
    		listaSony.setDisable(true);
    	}
    }
    
    /**
     * Este método, es llamado por el método initialize().
     * Su función es mostrar en cada comboBox la lista de dispositivos soportados
     * consultando al modelo.
     */
    public void actualizarListados()
    {
    	listaLg.getItems().clear();
    	listaSamsung.getItems().clear();
    	listaMotorola.getItems().clear();
    	listaSony.getItems().clear();
    	for(int i=0; i < getModelo().consultarListaDispositivos().size(); i++)
    	{
    		switch(getModelo().consultarListaDispositivos().get(i).getFabricante())
        	{
        		case "LGE":
        		{
        			listaLg.getItems().add(getModelo().consultarListaDispositivos().get(i).getFabricante() 
        					+ ", " + getModelo().consultarListaDispositivos().get(i).getModelo()
        					+ ", " + getModelo().consultarListaDispositivos().get(i).getVersionSo()
        					+ ", " + getModelo().consultarListaDispositivos().get(i).getNumeroCompilacion());
        			break;
        		}
        		case "samsung":
        		{
        			listaSamsung.getItems().add(getModelo().consultarListaDispositivos().get(i).getFabricante() 
        					+ ", " + getModelo().consultarListaDispositivos().get(i).getModelo()
        					+ ", " + getModelo().consultarListaDispositivos().get(i).getVersionSo()
        					+ ", " + getModelo().consultarListaDispositivos().get(i).getNumeroCompilacion());
        			break;
        		}
        		case "motorola":
        		{
        			listaMotorola.getItems().add(getModelo().consultarListaDispositivos().get(i).getFabricante() 
        					+ ", " + getModelo().consultarListaDispositivos().get(i).getModelo()
        					+ ", " + getModelo().consultarListaDispositivos().get(i).getVersionSo()
        					+ ", " + getModelo().consultarListaDispositivos().get(i).getNumeroCompilacion());
        			break;
        		}
        		case "Sony":
        		{
        			listaSony.getItems().add(getModelo().consultarListaDispositivos().get(i).getFabricante() 
        					+ ", " + getModelo().consultarListaDispositivos().get(i).getModelo()
        					+ ", " + getModelo().consultarListaDispositivos().get(i).getVersionSo()
        					+ ", " + getModelo().consultarListaDispositivos().get(i).getNumeroCompilacion());
        			break;
        		}
        	}
    	}
    }
}