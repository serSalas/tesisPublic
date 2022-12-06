package main.java.controller;

import animatefx.animation.FadeIn;
import animatefx.animation.Pulse;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Lighting;
import main.java.Main;
import main.java.model.Modelo;

/**
 * Esta clase, es padre de los controladores:
 * ControladorDeteccionAutomatica, ControladorDeteccionManual, ControladorPrincipal,
 * ControladorTipoDeteccion, ControladorMetodoExtraccion, ControladorUnidadAlmacenamiento
 * y ControladorExtraccionProgreso.  
 * v1.0.0
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 * @author Sosa Ludueña Diego
 * v2.0.0
 * @author Mantay Rodrigo Ariel
 * @author Salas Canalicchio Sergio Enrique
 */
public class Controlador
{
	/*
     * Esta instancia, es necesaria que sea estática
     * para que todos los controladores puedan consultar dicha instancia.
     * Los controladores hacen referencia a esta instancia main.
     */
	private static Main main; 
	
	/*
     * Esta instancia, es necesaria que sea estática
     * para que todos los controladores puedan consultar dicha instancia.
     * Los controladores hacen referencia a esta instancia modelo.
     */
	private static Modelo modelo;
	
	private Lighting efectoLighting;
	
	private GaussianBlur efectoGaussianBlur;
	
	private Pulse pulse;
	
	private FadeIn fadeIn;
	
	/**
	 * 
	 */
	public Controlador()
	{
		efectoLighting = new Lighting();
    	efectoGaussianBlur = new GaussianBlur();
    	efectoLighting.setDiffuseConstant(0.8);
    	efectoGaussianBlur.setRadius(50);
    	efectoGaussianBlur.setInput(efectoLighting);
    	
    	pulse = new Pulse();
    	fadeIn = new FadeIn();
	}
	
	/**
	 * Este método, se utiliza para que todos los controladores 
	 * trabajen con la misma instancia main
	 * @param main
	 */
	public void setMain(Main main) 
    {
        Controlador.main = main;
    }
    
	/**
	 * Este método, se utiliza para que todos los controladores
	 * trabajen con la misma instancia modelo
	 * @param modelo
	 */
    public void setModelo(Modelo modelo)
    {
    	Controlador.modelo = modelo;
    }
    
    /**
     * Esta función, retorna la instancia main
     * para que pueda ser consultada por los controladores.
     * @return Main
     */
    public Main getMain()
    {
    	return main;
    }
    
    /**
     * Esta función, retorna la instancia modelo,
     * para que pueda ser consultada por los controladores.
     * @return Modelo
     */
    public Modelo getModelo()
    {
    	return modelo;
    }
    
    /**
     * Esta función, retorna el efecto efectoGaussianBlur.
     * Que permite oscurecer y desenfocar la ventana principal.
     * Es consultada por los controladores cuando se lanza una ventana emergente
     * o cuando se despliega el menu desplegable.
     * @return GaussianBlur 
     */
    public GaussianBlur getEfectoGaussianBlur()
    {
    	return efectoGaussianBlur;
    }
    
    /**
     * Esta función, retorna la animación pulse.
     * Que permite darle animación pulse a los nodos de la ventana principal.
     * @return Pulse
     */
    public Pulse getPulse()
    {
    	return pulse;
    }
    
    /**
     * Esta función, retorna la animación fadeIn.
     * Que permite darle animación fadeIn a los nodos de la ventana principal.
     * @return FadeIn
     */
    public FadeIn getFadeIn()
    {
    	return fadeIn;
    }
}