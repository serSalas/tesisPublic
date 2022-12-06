package main.java.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import main.java.Main;
import main.java.model.Propiedades;

/**
 * Esta clase, es un controlador vinculado a tres vistas:
 * VISTA_PRINCIPAL, VISTA_EMERGENTE_DETECCION_USB_INCORRECTA
 * y VISTA_EMERGENTE_VERSION_SOFTWARE.
 * Además, hereda de la clase Controlador.  
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 * v2.0.0
 * @author Mantay Rodrigo Ariel
 * @author Salas Canalicchio Sergio Enrique
 *
 */
public class ControladorRespuesta extends Controlador
{
	@FXML
    private AnchorPane panelPrincipal;
	
    @FXML
    private JFXButton botonHome;
    
    @FXML
    private GridPane panelMenuDesplegable;

    @FXML
    private GridPane panelTitulo;
    
    @FXML
    private GridPane panelImages;
    
    @FXML
    private GridPane panelAyuda;
    
    @FXML
    private Text secuencia;
    
    /**
     * Este método, sirve para inicializar los nodos de la vista vinculada
     * al controlador ControladorPrincipal. Cada vez que se crea una instancia de
     * ControladorPrincipal se llama tanto a su constructor como así también al método initialize().
     * Cuando las variables panelImages, botonHome y listadoDispositivos sean distintas de nulo se efectúa la animación
     * Pulse en el botón botonHome y la animación FadeIn en el panel panelImages
     * de la vista VISTA_PRINCIPAL. Para el caso de listadoDispositivos se consulta al modelo para mostrar en dicho componente
     * el listado de dispositivos Android soportados.
     * Y cuando las variables panelImages, botonHome y listadoDispositivos sean nulas no realiza ninguna acción y en este caso
     * se encuentra en la vista VISTA_EMERGENTE_DETECCION_USB_INCORRECTA o VISTA_EMERGENTE_VERSION_SOFTWARE.
     * @throws IOException 	
     */
    public void initialize() throws IOException
    {
    	if(panelImages!=null && botonHome!=null && secuencia!=null)
    	{
        	getFadeIn().setNode(panelImages);
    		getFadeIn().setSpeed(0.5);
    		getFadeIn().play();
        	
    		getPulse().setNode(botonHome);
        	getPulse().setDelay(Duration.millis(2000));
        	getPulse().setSpeed(2.0);
        	getPulse().setCycleCount(5);
        	getPulse().play();
        	
        	BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/output/data.txt"));
			String line = reader.readLine();
			secuencia.setText(line);
	    	Process p = Runtime.getRuntime().exec("rm src/main/resources/output/data.txt");
	    	Process p2 = Runtime.getRuntime().exec("rmdir src/main/resources/output");

    	}
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonMenuDesplegable.
     * Por lo tanto, su función es hacer visible o invisible el menuDesplegable
     * (panelMenuDesplegable) de la vista VISTA_PRINCIPAL. Para hacer posible lo descripto anteriormente
     * se llama a la funcion menuDesplegable().
     * @param event Se produjo una click del mouse en el botonMenuDesplegable.
     */
    @FXML
    public void clickMenuDesplegable(MouseEvent event) 
    {
    	
    	menuDesplegable();
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonHome.
     * Por lo tanto, su función es consultar al modelo y dependiendo del estado del modelo,
     * cambiar a la vista VISTA_TIPO_DETECCION o aplicar efecto a la ventana principal
     * y lanzar ventana emergente VISTA_EMERGENTE_DETECCION_USB_INCORRECTA.
     * Este método lo comparten ambos botones botonHome.
     * @param event Se produjo una click del mouse en el botonHome.
     * @throws IOException
     */
    @FXML
    public void goToHome(MouseEvent event) throws IOException 
    {
		getMain().cambiarVentana(Main.VISTA_PRINCIPAL);
    	
    }    
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonVersionSoftware.
     * Por lo tanto, su función es aplicar el efecto EfectoGaussianBlur al panelPrincipal de la ventana principal.
     * Y por último, lanza la ventana emergente VISTA_EMERGENTE_VERSION_SOFTWARE.
     * @param event Se produjo una click del mouse en el botonVersionSoftware.
     * @throws IOException
     */
    @FXML
    public void clickVersionSoftware(MouseEvent event) throws IOException 
    {
    	panelPrincipal.setEffect(getEfectoGaussianBlur());
    	getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_SW_VERSION);
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonCerrarVentanaPrincipal.
     * Por lo tanto, su función es cerrar la ventana principal.
     * @param event Se produjo una click del mouse en el botonCerrarVentanaPrincipal.
     */
    @FXML
    public void clickCerrarVentanaPrincipal(MouseEvent event) 
    {
    	getMain().getMainStage().close();
    }

    /**
     * Este método, es llamado cada vez que se pulsa el botón botonAceptar de la vista
     * VISTA_EMERGENTE_VERSION_SOFTWARE o VISTA_EMERGENTE_DETECCION_USB_INCORRECTA.
     * Por lo tanto, su función es cerrar la ventana emergente ya sea
     * VISTA_EMERGENTE_VERSION_SOFTWARE o VISTA_EMERGENTE_DETECCION_USB_INCORRECTA.
     * Ambas vistas comparten el mismo método.
     * @param event Se produjo una click del mouse en el botonAceptar.
     * @throws IOException
     */
    @FXML
    public void clickAceptar(MouseEvent event) throws IOException 
    {
    	getMain().getDialogStage().close();
    	getMain().cambiarVentana(Main.VISTA_DESCUBIERTA);
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonAyuda de la vista
     * VISTA_PRINCIPAL.
     * Por lo tanto, su función es mostrar información acerca de la vista VISTA_PRINCIPAL. 
     * @param event Se produjo una click del mouse en el botonAyuda.
     */
    @FXML
    public void clickIrAyuda(MouseEvent event) 
    {   
    	if(panelAyuda.isVisible() && !panelMenuDesplegable.isVisible())
    	{
    		panelAyuda.setVisible(false);
    		panelTitulo.setEffect(null);
    		panelImages.setEffect(null);
    		secuencia.setDisable(false);
    	}
    	else if(panelAyuda.isVisible() && panelMenuDesplegable.isVisible())
    	{
    		panelAyuda.setVisible(false);
    	}
    	else
    	{
    		panelAyuda.setVisible(true);
    		panelTitulo.setEffect(getEfectoGaussianBlur());
    		panelImages.setEffect(getEfectoGaussianBlur());
    		secuencia.setDisable(true);
    	}
    }
       
    /**
     * Este método, es llamado desde el método clickMenuDesplegable(MouseEvent event).
     * Por lo tanto, su función es que cuando el panelMenuDesplegable es visible quita cualquier
     * efecto agregado a los paneles: panelMenuDesplegable, panelTitulo y panelImages.
     * Ademas, habilita el botón botonHome y coloca al panelMenuDesplegable
     * en falso para que sea invisible.
     * Cuando el panelMenuDesplegable es invisible agrega efecto a los paneles:
     * panelMenuDesplegable, panelTitulo y panelImages.
     * Además, deshabilita el boton botonHome y coloca al panelMenuDesplegable
     * en verdadero para que sea visible.
     */
    public void menuDesplegable()
    {
    	if(panelMenuDesplegable.isVisible() && !panelAyuda.isVisible())
    	{
    		panelMenuDesplegable.setVisible(false);
    		panelTitulo.setEffect(null);
    		panelImages.setEffect(null);
    		secuencia.setDisable(false);
    	}
    	else if(panelMenuDesplegable.isVisible() && panelAyuda.isVisible())
    	{
    		panelMenuDesplegable.setVisible(false);
    	}
    	else
    	{
    		panelMenuDesplegable.setVisible(true);
    		panelTitulo.setEffect(getEfectoGaussianBlur());
    		panelImages.setEffect(getEfectoGaussianBlur());
    		secuencia.setDisable(true);
    	}
    }
}