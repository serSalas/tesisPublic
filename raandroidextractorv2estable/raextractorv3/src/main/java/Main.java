package main.java;

import java.io.IOException;

import com.jfoenix.controls.JFXDecorator;

import animatefx.animation.FadeInUp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.controller.Controlador;
import main.java.model.Modelo;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Esta clase, da inicio a la aplicación.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class Main extends Application 
{
    private Stage mainStage;
    private AnchorPane root;
    private AnchorPane panelNuevo;
    private Controlador controlador;
    private Modelo modelo;
    private Stage dialogStage;

    public static final String VISTA_PRINCIPAL = "/main/resources/fxml/vistaPrincipal.fxml";
    public static final String VISTA_DESCUBIERTA = "/main/resources/fxml/vistaDescubierta.fxml";
    public static final String VISTA_EMERGENTE_SW_VERSION = "/main/resources/fxml/vistaEmergenteVersionSoftwareEnDescubierto.fxml";
    public static final String VISTA_TIPO_DETECCION = "/main/resources/fxml/vistaTipoDeteccion.fxml";
    public static final String VISTA_METODO_DESBLOQUEO = "/main/resources/fxml/vistaMetodoDesbloqueo.fxml";
    public static final String VISTA_ELECCION = "/main/resources/fxml/vistaEleccion.fxml";
    public static final String VISTA_DETECCION_MANUAL = "/main/resources/fxml/vistaDeteccionManual.fxml";
    public static final String VISTA_DETECCION_AUTOMATICA = "/main/resources/fxml/vistaDeteccionAutomatica.fxml";
    public static final String VISTA_METODO_EXTRACCION = "/main/resources/fxml/vistaMetodoExtraccion.fxml";
    public static final String VISTA_UNIDAD_ALMACENAMIENTO = "/main/resources/fxml/vistaUnidadAlmacenamiento.fxml";
    public static final String VISTA_EXTRACCION_PROGRESO = "/main/resources/fxml/vistaExtraccionProgreso.fxml";
    public static final String VISTA_DESBLOQUEO_PROGRESO = "/main/resources/fxml/vistaDesbloqueoProgreso.fxml";
    public static final String VISTA_EMERGENTE_VERSION_SOFTWARE = "/main/resources/fxml/vistaEmergenteVersionSoftware.fxml";
    public static final String VISTA_EMERGENTE_DETECCION_ADB_CORRECTA = "/main/resources/fxml/vistaEmergenteDeteccionAdbCorrecta.fxml";
    public static final String VISTA_EMERGENTE_DETECCION_ADB_INCORRECTA = "/main/resources/fxml/vistaEmergenteDeteccionAdbIncorrecta.fxml";
    public static final String VISTA_EMERGENTE_DETECCION_USB_INCORRECTA = "/main/resources/fxml/vistaEmergenteDeteccionUsbIncorrecta.fxml";
    public static final String VISTA_EMERGENTE_ADVERTENCIA_UNIDAD_ALMACENAMIENTO = "/main/resources/fxml/vistaEmergenteAdvertenciaUnidadAlmacenamiento.fxml";
    public static final String VISTA_EMERGENTE_SELECCION_DISPOSITIVO_INCORRECTA = "/main/resources/fxml/vistaEmergenteSeleccionDisposotivoIncorrecta.fxml";
    public static final String VISTA_EMERGENTE_SELECCION_METODO_EXTRACCION_INCORRECTA = "/main/resources/fxml/vistaEmergenteSeleccionMetodoExtraccionIncorrecta.fxml";
    public static final String VISTA_EMERGENTE_SELECCION_METODO_DESBLOQUEO_INCORRECTO = "/main/resources/fxml/vistaEmergenteSeleccionMetodoDesbloqueoIncorrecto.fxml";
    public static final String VISTA_EMERGENTE_MODO_BOOTLOADER = "/main/resources/fxml/vistaEmergenteModoBootloader.fxml";
    public static final String VISTA_EMERGENTE_DES_MODO_BOOTLOADER = "/main/resources/fxml/vistaEmergenteDesModoBootloader.fxml";
    public static final String VISTA_EMERGENTE_FLASHEAR_RECOVERY = "/main/resources/fxml/vistaEmergenteFlashearRecovery.fxml";
    public static final String VISTA_EMERGENTE_DES_FLASHEAR_RECOVERY = "/main/resources/fxml/vistaEmergenteDesFlashearRecovery.fxml";
    public static final String VISTA_EMERGENTE_MODO_RECOVERY = "/main/resources/fxml/vistaEmergenteModoRecovery.fxml";
    public static final String VISTA_EMERGENTE_DES_MODO_RECOVERY = "/main/resources/fxml/vistaEmergenteDesModoRecovery.fxml";
    public static final String VISTA_EMERGENTE_GENERAR_HASH = "/main/resources/fxml/vistaEmergenteGenerarHash.fxml";
    public static final String VISTA_EMERGENTE_MODO_DEPURACION = "/main/resources/fxml/vistaEmergenteModoDepuracion.fxml";
    public static final String VISTA_EMERGENTE_EXPLOTAR_VULNERABILIDAD = "/main/resources/fxml/vistaEmergenteExplotarVulnerabilidad.fxml";
    public static final String VISTA_EMERGENTE_MODO_MTP = "/main/resources/fxml/vistaEmergenteModoMtp.fxml";
    public static final String VISTA_EMERGENTE_MODO_DOWNLOAD = "/main/resources/fxml/vistaEmergenteModoDownload.fxml";
    public static final String VISTA_EMERGENTE_GEN_PUERTOS_AT = "/main/resources/fxml/vistaEmergenteGenPuertosAT.fxml";
    public static final String VISTA_EMERGENTE_CHANGE_PUERTOS_AT = "/main/resources/fxml/vistaEmergenteCambiaPuertosAT.fxml";
    public static final String VISTA_EMERGENTE_HABILITA_CONSOLA_AT = "/main/resources/fxml/vistaEmergenteHabilitaConsolaAT.fxml";
    public static final String VISTA_EMERGENTE_INGRESA_COMANDO_AT = "/main/resources/fxml/vistaEmergenteIngresaComandoAT.fxml";
    public static final String VISTA_EMERGENTE_FINALIZACION_EXITOSA = "/main/resources/fxml/vistaEmergenteFinalizacionExitosa.fxml";
    public static final String VISTA_EMERGENTE_DES_FINALIZACION_EXITOSA = "/main/resources/fxml/vistaEmergenteDesFinalizacionExitosa.fxml";
    public static final String VISTA_EMERGENTE_DES_DISCOVER_FINALIZACION_EXITOSA = "/main/resources/fxml/vistaEmergenteDesDiscoverFinalizacionExitosa.fxml";
    public static final String VISTA_EMERGENTE_FINALIZACION_FALLIDA = "/main/resources/fxml/vistaEmergenteFinalizacionFallida.fxml";
    public static final String VISTA_EMERGENTE_DES_FINALIZACION_FALLIDA = "/main/resources/fxml/vistaEmergenteDesFinalizacionFallida.fxml";
    public static final String VISTA_EMERGENTE_CALCULO_TAMANIO_ALMACENAMIENTO = "/main/resources/fxml/vistaEmergenteCalcularTamanioAlmacenamientoDispositivo.fxml";
    public static final String ESTILO_1 = "/main/resources/styles/estilo1.css";
    public static final String ESTILO_2 = "/main/resources/styles/estilo2.css";
    public static final String ICONO = "/main/resources/images/icono.png";
    
    
    /**
     * Este método, inicia la primera vista VISTA_PRINCIPAL creando el Stage, Scene y los Nodos.
     * Además, se aplican estilos css como ESTILO_1 y ESTILO_2.
     * Estos estilos se aplican en la escena.
     * Cada vista tiene asociado un controlador y un modelo único.
     * Este método es llamado por el método launch().
     * @param mainStage Es el Stage principal provisto por el framework JavaFX.
     */
    @Override
    public void start(Stage mainStage) throws IOException 
    {      
        this.mainStage = mainStage;
        modelo = new Modelo();
        controlador = new Controlador();
        controlador.setMain(this);
        controlador.setModelo(modelo);
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Main.VISTA_PRINCIPAL));
        root = (AnchorPane)loader.load();
        
        controlador = loader.getController();
        
    	JFXDecorator decorator = new JFXDecorator(mainStage, root);
        decorator.setCustomMaximize(true);
        
        Scene mainScene = new Scene(decorator);
        String uri_1 = getClass().getResource(Main.ESTILO_1).toExternalForm();
        String uri_2 = getClass().getResource(Main.ESTILO_2).toExternalForm();
        mainStage.getIcons().add(new Image(getClass().getResource(Main.ICONO).toExternalForm()));
        mainScene.getStylesheets().add(uri_1);
        mainScene.getStylesheets().add(uri_2);
        mainStage.setScene(mainScene);
        mainStage.setTitle("RA Android Extractor");
        mainStage.show();
        root.requestFocus(); //Selecciona el panel para que respnda las cambinaciones de teclas
    }
    
    /**
     * Este método, permite cambiar de AnchorPane. Dando como resultado un cambio de vista.
     * Recibe un parámetro String que contiene la ruta donde se encuentra
     * el archivo .fxml que es la vista.
     * @param archivoFxml Es una variable String que indica la ruta en donde se encuentra el archivo .fxml.
     * @throws IOException
     */
    public void cambiarVentana(String archivoFxml) throws IOException
    {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFxml));
        panelNuevo = (AnchorPane)loader.load();
        
        controlador = loader.getController();
        
        AnchorPane.setTopAnchor(panelNuevo, 0.0);
        AnchorPane.setRightAnchor(panelNuevo, 0.0);
        AnchorPane.setLeftAnchor(panelNuevo, 0.0);
        AnchorPane.setBottomAnchor(panelNuevo, 0.0);
        root.getChildren().setAll(panelNuevo);
        root.setEffect(null); //saca cualquier efecto agregado
        root.requestFocus(); //Selecciona el panel para que respnda las cambinaciones de teclas
    }
    
    /**
     * Este método, permite lanzar una ventana secundaria o también llamada ventana emergente.
     * Recibe un parámetro String que contiene la ruta donde se encuentra
     * el archivo .fxml que es la vista.
     * @param archivoFxml Es una variable String que indica la ruta en donde se encuentra el archivo .fxml.
     * @throws IOException
     */
    public void lanzarVentanaEmergente(String archivoFxml) throws IOException
    {   
    	dialogStage = new Stage();
    	dialogStage.initModality(Modality.APPLICATION_MODAL);
    	dialogStage.initStyle(StageStyle.UNDECORATED);
    	dialogStage.initOwner(mainStage);
    	AnchorPane panelEmergente = new AnchorPane(); 
    	FXMLLoader loader = new FXMLLoader(getClass().getResource(archivoFxml));
    	panelEmergente = (AnchorPane)loader.load();
    	
    	controlador = loader.getController();    	
    	
    	//new BounceIn(panelEmergente).play();
    	FadeInUp fadeInUp = new FadeInUp();
    	fadeInUp.setNode(panelEmergente);
    	fadeInUp.setSpeed(0.3);
    	fadeInUp.play();
    	
        Scene dialogScene = new Scene(panelEmergente, Color.valueOf("#1E3D7E"));
        String uri = getClass().getResource(Main.ESTILO_2).toExternalForm();
        dialogScene.getStylesheets().add(uri);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }
    
    /**
     * Esta función, retorna el Stage de la ventana principal.
     * Los controladores de las diferentes
     * vistas utilizan dicho método cuando necesitan cerrar una
     * ventana principal.
     * @return Retorna la instancia Stage mainStage.
     */
    public Stage getMainStage()
    {
    	return mainStage;
    }
    
    /**
     * Esta función, retorna el Stage de la ventana emergente.
     * Los controladores de las diferentes
     * vista utilizan dicho método cuando necesitan cerrar una
     * ventana emergente.
     * @return Retorna la instancia Stage dialogStage.
     */
    public Stage getDialogStage()
    {
    	return dialogStage;
    }
    
    /**
     * Esta función, da inicio a la aplicación.
     * Llama al método estático launch().
     * @param args Es un arreglo con los parámetros se reciben por consola.
     */
    public static void main(String[] args) 
    {
    	/*
         * Este método, llama internamente al método start().
         * Es estático debido a que solo se puede llamar una única vez.
         * Este método normalmente se llama desde el método main()
         * como en este caso.
         */
        launch(args);
    }
}