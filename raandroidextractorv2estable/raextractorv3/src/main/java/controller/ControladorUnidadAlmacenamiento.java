package main.java.controller;

import java.io.File;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import main.java.Main;

/**
 * Esta clase, es un controlador vinculado a las vistas: 
 * VISTA_UNIDAD_ALMACENAMIENTO y VISTA_EMERGENTE_ADVERTENCIA_UNIDAD_ALMACENAMIENTO.
 * Además, hereda de la clase Controlador.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 * v2.0.0
 * @author Mantay Rodrigo Ariel
 * @author Salas Canalicchio Sergio Enrique
 *
 */
public class ControladorUnidadAlmacenamiento extends Controlador
{
	@FXML
    private AnchorPane panelUnidadAlmacenamiento;
	
	@FXML
    private GridPane panelBuscarRutaDestino;
	
	@FXML
    private GridPane panelTitulo;
	
	@FXML
	private GridPane panelAyuda;
	
	@FXML
    private JFXButton botonSiguiente;
	
	@FXML
    private JFXButton botonRutaDestino;
	
	@FXML
    private Text NombreRutaDestino;
	
	@FXML
    private JFXCheckBox hashMd5;

    @FXML
    private JFXCheckBox hashSha256;
    
    private DirectoryChooser seleccionadorDirectorio;
    
    private File directorioSeleccionado;
    
    /**
     * Este método, sirve para inicializar los nodos de la vista vinculada
     * al controlador ControladorUnidadAlmacenamiento. Cada vez que se crea una instancia de
     * ControladorUnidadAlmacenamiento se llama tanto a su constructor como así también al método initialize().
     * Cuando las variables panelBuscarRutaDestino, NombreRutaDestino y botonSiguiente
     * sean distintas de nulo se encuentra en la vista VISTA_UNIDAD_ALMACENAMIENTO
     * e inicia en vacio el nombre de la ruta destino y deshabilita el
     * botón botonSiguiente.
     * Cuando las variables panelBuscarRutaDestino, NombreRutaDestino y botonSiguiente
     * sean nulas se encuentra en la vista
     * VISTA_EMERGENTE_ADVERTENCIA_UNIDAD_ALMACENAMIENTO y setea el título y
     * el mensaje de la ventana emergente.
     */
    public void initialize()
    {
    	if(panelBuscarRutaDestino!=null && NombreRutaDestino!=null && botonSiguiente!=null)
    	{
    		getFadeIn().setNode(panelBuscarRutaDestino);
    		getFadeIn().setSpeed(0.5);
    		getFadeIn().play();
    		
    		NombreRutaDestino.setText("");
    		botonSiguiente.setVisible(false);
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
     * Por lo tanto, su función es consultar el modelo para saber si se seleccionó
     * ruta destino.
     * Dependiendo del estado del modelo, puede cambiar a ventana VISTA_METODO_EXTRACCION
     * o lanzar la ventana emergente VISTA_EMERGENTE_ADVERTENCIA_UNIDAD_ALMACENAMIENTO
     * @param event Se produjo una click del mouse en el botonSiguiente.
     * @throws IOException
     */
    @FXML
    public void clickSiguiente(MouseEvent event) throws IOException 
    {
    	getModelo().solicitarGeneracionHash(hashMd5.isSelected(), hashSha256.isSelected());
    	
    	switch(getModelo().getEstadoModelo())
    	{
    		case 7:
    		{
    			getMain().cambiarVentana(Main.VISTA_METODO_EXTRACCION);
    			break;
    		}
    		case 8:
    		{
    			panelUnidadAlmacenamiento.setEffect(getEfectoGaussianBlur());
        		getMain().lanzarVentanaEmergente(Main.VISTA_EMERGENTE_ADVERTENCIA_UNIDAD_ALMACENAMIENTO);
    			break;
    		}
    	}
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa el botón botonAyuda de la vista
     * VISTA_UNIDAD_ALMACENAMIENTO.
     * Por lo tanto, su función es mostrar información acerca de la vista VISTA_UNIDAD_ALMACENAMIENTO. 
     * @param event Se produjo una click del mouse en el botonAyuda.
     */
    @FXML
    public void clickIrAyuda(MouseEvent event) 
    {
    	if(panelAyuda.isVisible())
    	{
    		panelAyuda.setVisible(false);
    		panelTitulo.setEffect(null);
    		panelBuscarRutaDestino.setEffect(null);
    		botonSiguiente.setDisable(false);
    		botonRutaDestino.setDisable(false);
    		hashMd5.setDisable(false);
    		hashSha256.setDisable(false);
    	}
    	else
    	{
    		panelAyuda.setVisible(true);
    		panelTitulo.setEffect(getEfectoGaussianBlur());
    		panelBuscarRutaDestino.setEffect(getEfectoGaussianBlur());
    		botonSiguiente.setDisable(true);
    		botonRutaDestino.setDisable(true);
    		hashMd5.setDisable(true);
    		hashSha256.setDisable(true);
    	}
    }
    
    /**
     * Este método, es llamado cada vez que se pulsa en el botón botonBuscarRutaDestino.
     * Por lo tanto, su función es consultar el modelo para saber la ruta destino de la
     * unidad de almacenamiento y mostrarla en la ventana principal habilitando el botón
     * botonSiguiente.
     * Además, cuando se muestra la ruta destino se aplica la animación Pulse al nodo
     * NombreRutaDestino.
     * @param event Se produjo una click del mouse en el botonSiguiente.
     * @throws IOException
     */
    @FXML
    public void clickBuscarRutaDestino(MouseEvent event) throws IOException 
    {
    	seleccionadorDirectorio = new DirectoryChooser();
		seleccionadorDirectorio.setTitle("Seleccionar lugar de unidad de almacenamiento");
		directorioSeleccionado = seleccionadorDirectorio.showDialog(getMain().getMainStage());
		String rutaDestino = "";
		
		if(directorioSeleccionado!=null)
		{
			rutaDestino = directorioSeleccionado.getAbsolutePath();
		}
		else
		{
			rutaDestino = "";
		}
    	
		getModelo().seleccionarUbicacionUnidadAlmacenamiento(rutaDestino);
		
    	NombreRutaDestino.setText(getModelo().consultarRutaDestino().replace("/", " > "));
    	getFadeIn().setNode(NombreRutaDestino);
		getFadeIn().setSpeed(0.2);
		getFadeIn().play();
    	
    	botonSiguiente.setVisible(true);
    	getPulse().setNode(botonSiguiente);
    	getPulse().setSpeed(2.0);
    	getPulse().setCycleCount(5);
    	getPulse().play();
    }

    /**
     * Este método, es llamado cada vez que se pulsa el botón botonAceptar de la vista
     * VISTA_EMERGENTE_ADVERTENCIA_UNIDAD_ALMACENAMIENTO.
     * Por lo tanto, su función es cerrar la ventana emergente
     * VISTA_EMERGENTE_ADVERTENCIA_UNIDAD_ALMACENAMIENTO y volver a la vista VISTA_UNIDAD_ALMACENAMIENTO.
     * @param event Se produjo una click del mouse en el botonAceptar.
     * @throws IOException
     */
    @FXML
    public void clickAceptar(MouseEvent event) throws IOException 
    {
    	getMain().getDialogStage().close();
    	getMain().cambiarVentana(Main.VISTA_UNIDAD_ALMACENAMIENTO);
    }
}
