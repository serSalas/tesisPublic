package main.java.model;

import java.util.List;

/**
 * Esta clase, se encarga de reconocer dispositivo Android conectado a ordenador.
 * Para reconocer dispositivo Android conectado a ordenador, utiliza un listado de caracteristicas de dispositivos
 * compatibles y compara cada uno con caracteristicas de dispositivo Android concetado a ordenador.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public abstract class ReconocedorDispositivo
{
	protected List<Dispositivo> listaDispositivos;
	protected static boolean esDetectado;
	protected String mensajeDeteccion;
	protected Dispositivo dispositivo;
	
	/**
     * Este constructor, inicializa atributos de clase ReconocedorDispositivo. En donde,
     * se hace referencia al listado de dispositivos ya creado por la clase modelo. 
     * También inicializa el mensajeDeteccion y la variable booleana esDetectado.
     */
	public ReconocedorDispositivo(List<Dispositivo> listaDispositivos)
	{
		this.listaDispositivos = listaDispositivos;
		mensajeDeteccion = "";
		esDetectado = false;
		dispositivo = new Dispositivo("", "", "", "");
	}
	
	/**
	 * Utiliza diferentes estrategias de detección de dispositivo.
	 * @return retorna true si se detectó dispositivo conectado a ordenador o false en caso contrario.
	 */
	public abstract boolean detectarDispositivo();
	
	/**
     * Este método, coloca atributo seleccionado de cada instancia Dispositivo
     * en false. Esto quiere decir que deselecciona todos los dispositivos de lista
     * dispositivos.
     */
	public void deseleccionarDispositivos()
	{
		for(int i=0; i < listaDispositivos.size(); i++)
		{
			listaDispositivos.get(i).setSeleccionar(false);
		}
	}
	
	/**
     * Esta función, retorna dispositivo detectado por alguna de las estrategias de deteccion de dispositivo.
     * @return Instancia Dispositivo que fue detectada por alguna de las estrategias de deteccion de dispositivo.
     */
	public Dispositivo getDispositivoDetectado()
	{
		for(int i=0; i < listaDispositivos.size(); i++)
		{
			if(listaDispositivos.get(i).getSeleccionado())
			{
				dispositivo = listaDispositivos.get(i);
				break;
			}
		}
		
		return dispositivo;
	}
	
	/**
	 * Esta función, retorna mensaje de detección de alguna de las estrategias de deteccion de dispositivo.
	 * @return retorna mensaje de detección de alguna de las estrategias de detección de dispositivo.
	 */
	public String getMensajeDeteccion()
	{
		return mensajeDeteccion;
	}
}
