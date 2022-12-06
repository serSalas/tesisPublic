package main.java.model;
/**
 * Esta clase, contiene métodos de desbloqueo de pantalla
 * como método Recovery Pass, Modem AT, entre otros.
 * v2.0.0
 * @author Mantay Rodrigo Ariel
 * @author Salas Canalicchio Sergio Enrique
 *
 */
public abstract class DesbloquearPantalla 
{
	protected Dispositivo dispositivo;
	protected String nombreMetodoDesbloqueo;
	protected String mensajeError;
	
	/**
	 * Este constructor, recibe un dispositivo.
	 * Su función es inicializar el atributo dispositivo y evitar que el mismo sea nulo.
	 * @param dispositivo Instancia de dispositivo Android detectado.
	 */
	public DesbloquearPantalla(Dispositivo dispositivo)
	{
		this.dispositivo=dispositivo;
		nombreMetodoDesbloqueo="";
	}
	
	/**
	 * Este método, se encarga de llevar acabo la técnica forense de desbloqueo de pantalla,
	 * dependiendo de la técnica que haya seleccionado usuario. 
	 *
	 */
	public abstract void desbloqueoPantalla();
	
	/**
     * Esta función, retorna estado de desbloqueo de pantalla.
     * Permite ser consultada por modelo o por controladores.
     * @return estadoDesbloqueo Variable entera.
     */
	public abstract int getEstadoDesbloqueo();
	
	/**
     * Esta función, retorna nombre de método de desbloqueo de pantalla elegido por el usuario.
     * @return nombreMetodoDesbloqueo Variable String : contiene nombre de método 
     * de desbloqueo de panatalla elegido.
     */
	public String getNombreMetodoDesbloqueo()
	{
		return nombreMetodoDesbloqueo;
	}
	
	/**
     * Esta función, retorna error de excepción lanzada
     * @return mensajeError Variable String que contiene error de excepción lanzada.
	 *
     */
	public String getMensajeError()
	{
		return mensajeError;
	}	
}