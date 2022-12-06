
package main.java.controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import main.java.Main;

/**
 * Esta clase, es un controlador vinculado a la vista:
 * VISTA_METODO_DETECCION.
 * Además, hereda de la clase Controlador.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 * v2.0.0
 * @author Mantay Rodrigo Ariel
 * @author Salas Canalicchio Sergio Enrique
 *
 */
public class ControladorEleccion extends Controlador 
{   
	@FXML
    private GridPane panelEleccion;
	
	@FXML
    private GridPane panelTitulo;
	
	@FXML
    private GridPane panelAyuda;
	
	@FXML
    private JFXButton botonExtraer;

    @FXML
    private JFXButton botonDesbloqueo;
    
	/**
     * Este método, sirve para inicializar los nodos de la vista vinculada
     * al controlador ControladorMetodoDeteccion. Cada vez que se crea una instancia de
     * ControladorMetodoDeteccion se llama tanto a su constructor como asi tambien al método initialize().
     * En este método, se efectua la animación FadeIn en el panel panelMetodoDeteccion
     * de la vista VISTA_METODO_DETECCION.
     */
    @FXML
	public void initialize()
    {
		getFadeIn().setNode(panelEleccion);
		getFadeIn().setSpeed(0.5);
		getFadeIn().play();
    }
	
	/**
     * Este método, es llamado cada vez que se pulsa en el botón botonIrAtras.
     * Por lo tanto, su función es cambiar a la vista VISTA_PRINCIPAL.
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
     * VISTA_TIPO_DETECCION.
     * Por lo tanto, su función es mostrar información acerca de la vista VISTA_TIPO_DETECCION. 
     * @param event Se produjo una click del mouse en el botonAyuda.
     */
	@FXML
    public void clickIrAyuda(MouseEvent event) 
	{
		if(panelAyuda.isVisible())
    	{
    		panelAyuda.setVisible(false);
    		panelTitulo.setEffect(null);
    		panelEleccion.setEffect(null);
    		botonExtraer.setDisable(false);
    		botonDesbloqueo.setDisable(false);
    	}
    	else
    	{
    		panelAyuda.setVisible(true);
    		panelTitulo.setEffect(getEfectoGaussianBlur());
    		panelEleccion.setEffect(getEfectoGaussianBlur());
    		botonExtraer.setDisable(true);
    		botonDesbloqueo.setDisable(true);
    	}
    }
    
	/**
     * Este método, es llamado cada vez que se pulsa en el botón botonDeteccionAutomatica.
     * Por lo tanto, su función es cambiar a la vista VISTA_DETECCION_AUTOMATICA.
     * @param event Se produjo una click del mouse en el botonDeteccionAutomatica.
     * @throws IOException
     */
    @FXML
    public void clickExtraer(MouseEvent event) throws IOException 
    {
    	getMain().cambiarVentana(Main.VISTA_UNIDAD_ALMACENAMIENTO);
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonDeteccionManual.
     * Por lo tanto, su función es cambiar a la vista VISTA_DETECCION_MANUAL.
     * @param event Se produjo una click del mouse en el botonDeteccionManual.
     * @throws IOException
     */
    @FXML
    public void clickDesbloquear(MouseEvent event) throws IOException 
    {
    	getMain().cambiarVentana(Main.VISTA_METODO_DESBLOQUEO);
    	
    }
}