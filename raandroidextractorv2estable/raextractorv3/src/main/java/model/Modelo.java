package main.java.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Esta clase, contiene la lógica del modelo.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class Modelo 
{
	private int estadoModelo;
	private ReconocedorDispositivo reconocedorDispositivo;
	private UnidadAlmacenamiento unidadAlmacenamiento;
	private ExtractorDatos extractorDatos;
	private static List<Dispositivo> listaDispositivos;
	private DesbloquearPantalla DesblqPantalla;
	private boolean isFlagHashMd5;
	private boolean isFlagHashSha256;
	
	/**
     * Este constructor inicializa atributos para evitar que sean nulos.
     * El listaDispositivos se crea en constructor para que sea la única
     * instancia consultada. Debido a que sólo contiene el listado de dispositivo y es estático
     * no varía su información a lo largo de la ejecución.
     */
	public Modelo()
	{
		listaDispositivos = new ArrayList<Dispositivo>();
		listaDispositivos.add(new Dispositivo("samsung", "GT-I8550L", "4.1.2", "JZO54K.I8550LUBUANF1"));	// 1- Galaxy win
		listaDispositivos.add(new Dispositivo("samsung", "GT-I8260L", "4.1.2", "JZO54K.I8260LUBAMG3"));		// 2- Galaxy core
		listaDispositivos.add(new Dispositivo("samsung", "SM-G355M",  "4.4.2", "KOT49H.G355MUBS0AQA7"));	// 3- Galaxy core2
		listaDispositivos.add(new Dispositivo("samsung", "SM-G130M",  "4.4.2", "KOT49H.G130MUBS0AQA1"));	// 4- Galaxy young2
		listaDispositivos.add(new Dispositivo("samsung", "SM-J106M",  "6.0.1", "MMB29Q.J106MUBS0ARE3"));	// 5- Galaxy j1miniprime
		listaDispositivos.add(new Dispositivo("samsung", "SM-G532M",  "6.0.1", "MMB29T.G532MUMU1ASH2"));	// 6- Galaxy j2prime
		listaDispositivos.add(new Dispositivo("samsung", "SM-J320M",  "5.1.1", "LMY47V.J320MUBU0ARA2"));	// 7- Galaxy j3
		listaDispositivos.add(new Dispositivo("samsung", "SM-G570M",  "7.0",   "NRD90M.G570MUBU2BRA3"));	// 8- Galaxy j5prime
		listaDispositivos.add(new Dispositivo("samsung", "SM-G610M",  "8.1.0", "M1AJQ.G610MUBU6CSJ1"));		// 9- Galaxy j7prime
		listaDispositivos.add(new Dispositivo("samsung", "SM-J700M",  "5.1.1", "LMY48B.J700MUBU1AOK1"));	//10- Galaxy j7
		listaDispositivos.add(new Dispositivo("samsung", "SM-J710MN", "8.1.0", "M1AJQ.J710MNUBS4CSF1"));	//11- Galaxy j7 2016
		listaDispositivos.add(new Dispositivo("samsung", "SM-G3812B", "4.2.2", "JDQ39.G3812BVJUANC2"));		//12- Galaxy s3slim
		
//--------------------------------------------------------------------------------------------------------------------------------------------------		
		listaDispositivos.add(new Dispositivo("LGE", "LG-D625", "4.4.2", "KOT49I"));						// 1- G2 mini
		listaDispositivos.add(new Dispositivo("LGE", "Nexus 4", "4.2.2", "JDQ39"));							// 2- Nexus 4
		listaDispositivos.add(new Dispositivo("LGE", "LG-P712", "4.1.2", "JZO54K"));						// 3- Optimus L7
		listaDispositivos.add(new Dispositivo("LGE", "LG-H340AR", "5.0.1", "LRX21Y"));						// 4- Leon
		listaDispositivos.add(new Dispositivo("LGE", "LG-H440AR", "5.0.1", "LRX21Y"));						// 5- Spirit
		listaDispositivos.add(new Dispositivo("LGE", "LG-K120", "5.1.1", "LMY47V"));						// 6- K4
		listaDispositivos.add(new Dispositivo("LGE", "LG-M250", "7.0", "NRD90U"));							// 7- K10
		listaDispositivos.add(new Dispositivo("LGE", "LG-K220", "6.0.1", "MXB48T"));						// 8- X power
		listaDispositivos.add(new Dispositivo("LGE", "LG-H221AR", "4.4.2", "KOT49I"));						// 9- KITE
//--------------------------------------------------------------------------------------------------------------------------------------------------		
		listaDispositivos.add(new Dispositivo("motorola", "XT890", "4.1.2", "9.8.2I-50_SML-23"));			//1- Razr I
		listaDispositivos.add(new Dispositivo("motorola", "XT915", "4.1.2", "2_32A_2017"));					//2- Razr D1
		listaDispositivos.add(new Dispositivo("motorola", "XT1032", "5.1",  "LPBS23.13-56-2"));				//3- G
		listaDispositivos.add(new Dispositivo("motorola", "XT1021", "4.4.4",  "KXC21.5-40"));				//4- X

//--------------------------------------------------------------------------------------------------------------------------------------------------		
		listaDispositivos.add(new Dispositivo("Sony", "C2104", "4.2.2", "15.3.A.1.17"));					//1- Xperia L
		listaDispositivos.add(new Dispositivo("Sony", "D2303", "5.1.1", "18.6.A.0.182"));					//2- Xperia M2
		estadoModelo = 0;
	}
	
	/**
     * Este método, se encarga consultar si se detectó fabricante de dispositivo Android conectado,
     * siempre y cuando el dispositivo Android se encuentre conectado a PC
     * y sea compatible con lista de dispositivos compatibles por aplicación.
     */
	public void solicitarDeteccionFabricanteDispositivo()
	{
		reconocedorDispositivo = new DetectarPorUsb(listaDispositivos);
		if(reconocedorDispositivo.detectarDispositivo())
		{
			estadoModelo = 1;
		}
		else
		{
			estadoModelo = 2;
		}
	}
	
	/**
     * Este método, consulta si se detectó de forma automática, dispositivo Android.
     * Siempre y cuando dispositivo Android se encuentre conectado a PC y con modo de
     * depuración habilitado.
     * En caso que se detecte dispositivo Android conectado, estado de modelo será 3.
     * En caso que no se detecte dispositivo Android conectado, pero que este conectado a PC o 
     * no esté conectado a PC, estado de modelo será 4.
	 * @throws IOException 
     */
	public void solicitarDeteccionAutomaticaDispositivo() throws IOException
	{
		Propiedades.getPropiedades("ro.product.manufacturer");
		reconocedorDispositivo = new DetectarPorAdb(listaDispositivos);
		if(reconocedorDispositivo.detectarDispositivo())
		{
			estadoModelo = 3;
		}
		else
		{
			estadoModelo = 4;
		}
	}
	
	/**
     * Este método, consulta si se detectó de forma manual, dispositivo Android.
     * Usuario se encargará de seleccionar dispositivo adecuado en alguno de los tres ComboBox de
     * vista VISTA_DETECCION_MANUAL. Cada ComboBox tendrá cargado listado de cada marca.
     * Se tiene lista de samsung, de Lg y de motorola y usuario se encarga de seleccionar que dispositivo seleccionar.
     * En caso que haya coincidencia entre dispositivo seleccionado por usuario y algún dispositivo de lista,
     * estado de modelo será 6.
     * En caso que no haya coincidencia, estado de modelo será 7.
     * @param propDispositivo Variable String que contiene dispositivo seleccionado de cierto ComboBox
     * cuyo formato es: marca, modelo, versión de sistema operativo, numero de compilacion.
     * Por ejemplo: samsung, GT-I8550L, 4.1.2, JZO54K.I8550LUBUANF1.
     */
	public void solicitarDeteccionManualDispositivo(String propDispositivo)
	{
		reconocedorDispositivo = new DetectarManual(listaDispositivos, propDispositivo);
		if(reconocedorDispositivo.detectarDispositivo())
		{
			estadoModelo = 5;
		}
		else
		{
			estadoModelo = 6;
		}
	}
	
	/**
     * Este método, consulta lugar de almacenamiento. Es decir, usuario es el encargado de indicar un directorio
     * de lugar de unidad de almacenamiento, que puede ser un pendrive, tarjeta de memoria, disco rígido, etc.
     * Si se selecciona directorio de unidad de almacenamiento, estado de modelo será 8.
     * Si no se selecciona directorio de unidad de almacenamiento, estado de modelo será 9.
     * @param stage Instancia que viene de la ventana principal.
     */
	public void seleccionarUbicacionUnidadAlmacenamiento(String rutaDestino)
	{
		unidadAlmacenamiento = new UnidadAlmacenamiento();
		if(unidadAlmacenamiento.guardarRutaDestino(rutaDestino))
        {
			estadoModelo = 7;
        }
        else
        {
        	estadoModelo = 8;
        }
	}
	
	/**
     * Este método, setea flags de hash MD5 y SHA-256 para saber
     * si se efectuarán y guardarán el/los cálculos hash o no.
     * @param isFlagHashMd5 Variable booleana que contiene true si usuario quiere realizar hash MD5 o false caso contrario.
     * @param isFlagHashSha256 Variable booleana que contiene true si usuario quiere realizar hash SHA-256 o caso contrario.
     */
	public void solicitarGeneracionHash(boolean isFlagHashMd5, boolean isFlagHashSha256)
	{
		this.isFlagHashMd5 = isFlagHashMd5;
		this.isFlagHashSha256 = isFlagHashSha256;
	}
	
	/**
     * Este método, cambia de estado modelo dependiendo de método de extracción de datos
     * seleccionado por usuario en vista VISTA_METODO_EXTRACCION.
     * Si método de extracción de datos seleccionado por usuario es Metodo Dirty Cow, estado de modelo será 11.
     * Si método de extracción de datos seleccionado por usuario es Metodo Recovery, estado de modelo será 12.
     * Si método de extracción de datos seleccionado por usuario es Metodo Lg laf, estado de modelo será 13.
     * Si método de extracción de datos seleccionado por usuario es Metodo Mtp, estado de modelo será 14.
     * Si método de extracción de datos seleccionado por usuario es Metodo Adb, estado de modelo será 15.
     * @param MetodoExtraccion Variable String que contiene nombre de método de extracción de datos
     * a realizar. Es una variable que se setea dependiendo de método de extraccion de datos
     * que elija usuario en los dos listados de vista VISTA_METODO_EXTRACCION.
     */
	public void seleccionarMetodoExtraccionDatos(String MetodoExtraccion)
	{
		switch(MetodoExtraccion)
		{
			case "Metodo Dirty Cow":
			{
				extractorDatos = new ExtraerPorDirtyCow(reconocedorDispositivo.getDispositivoDetectado(), unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
				estadoModelo = 9;
				break;
			}
			case "Metodo Recovery":
			{
				extractorDatos = new ExtraerPorRecovery(reconocedorDispositivo.getDispositivoDetectado(), unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
				estadoModelo = 10;
				break;
			}
			case "Metodo Lg laf":
			{
				extractorDatos = new ExtraerPorLgLaf(reconocedorDispositivo.getDispositivoDetectado(), unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
				estadoModelo = 11;
				break;
			}
			case "Metodo Mtp":
			{
				extractorDatos = new ExtraerPorMtp(reconocedorDispositivo.getDispositivoDetectado(), unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
				estadoModelo = 12;
				break;
			}
			case "Metodo Adb":
			{
				extractorDatos = new ExtraerPorAdb(reconocedorDispositivo.getDispositivoDetectado(), unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
				estadoModelo = 13;
				break;
			}
			case "Metodo Dirty Cow Modificado":
			{
				extractorDatos = new ExtraerPorDirtyCowModificado(reconocedorDispositivo.getDispositivoDetectado(), unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
				estadoModelo = 18;
				break;
			}
		}
	}
	public void seleccionarMetodoDesbloqueo(String MetodoDesbloqueo)
	{
		switch(MetodoDesbloqueo)
		{
			case "Metodo UnlockScreen Samsung":
			{
				DesblqPantalla = new DesbloquearPorUnlockScreen(reconocedorDispositivo.getDispositivoDetectado());
				estadoModelo = 14;
				break;
			}
			case "Metodo Delete Pass":
			{
				DesblqPantalla = new DesbloquearPorDeletePass(reconocedorDispositivo.getDispositivoDetectado());
				
				estadoModelo = 15;
				break;
			}
			case "Metodo Discover Pattern":
			{
				DesblqPantalla = new DesbloquearPorDiscoverPattern(reconocedorDispositivo.getDispositivoDetectado());
				estadoModelo = 16;
				break;
			}
			case "Metodo AT":
			{
				DesblqPantalla = new DesbloquearPorAT(reconocedorDispositivo.getDispositivoDetectado());
				//extractorDatos = new ExtraerPorMtp(reconocedorDispositivo.getDispositivoDetectado(), unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
				estadoModelo = 17;
				break;
			}
		}
	}
	/**
     * Esta función, sirve para consultar método de extracción de datos que usuario eligió.
     */
	public void consultarMetodoExtraccionDatos() 
	{
		extractorDatos.extraerDatos();
	}
	
	public void consultarMetodoDesbloqueo() {
		DesblqPantalla.desbloqueoPantalla();
	}
	/**
     * Esta función, retorna estado de modelo.
     * Normalmente el estado de modelo es consultado por controladores.
     * @return estadoModelo Variable entera.
     */
	public int getEstadoModelo()
	{
		return estadoModelo;
	}
	
	/**
     * Esta función, retorna lista de dispositivos soportados.
     * @return listaDispositivo Variable List<Dispositivo>
     * que contiene todos los dispositivos Android soportados.
     */
	public List<Dispositivo> consultarListaDispositivos()
	{
		return listaDispositivos;
	}
	
	/**
	 * Esta función, retorna consulta que dispositivo Android fue detectado.
	 * @return retorna dispositivo Android detectado.
	 */
	public Dispositivo consultarDispositivoDetectado()
	{
		return reconocedorDispositivo.getDispositivoDetectado();
	}
	
	/**
     * Esta función, retorna ruta destino de unidad de almacenamiento seleccionada por usuario.
     * Que normalmente es consultada por ControladorUnidadAlmacenamiento.
     * @return Retorna ruta destino de unidad de almacenamiento seleccionada.
     */
	public String consultarRutaDestino()
	{
		return unidadAlmacenamiento.getRutaDestino();
	}
	
	/**
     * Esta función, retorna estado de extracción de datos.
     * @return Variable entera.
     */
	public int getEstadoExtraccionDatos()
	{
		return extractorDatos.getEstadoExtraccionDatos();
	}
	
	public int getEstadoDesbloqueo()
	{
		return DesblqPantalla.getEstadoDesbloqueo();
	}
	/**
     * Esta función, retorna porcentaje de extracción de datos.
     * @return Variable long que contiene porcentaje de extracción
     * de datos.
     */
	public long consultarPorcentajeExtraccionDatos()
	{	
		return extractorDatos.getPorcentajeExtraccionDatos();
	}
	
	/**
     * Esta función, retorna tamaño parcial de extracción de datos convertido en formato
     * legible para humano.
     * @return Variable String que contiene tamaño parcial de extracción de datos
     * convertido en formato legible para humano.
     */
	public String consultarTamanioParcialExtraccionDatos()
	{
		return extractorDatos.getTamanioParcialExtraccionDatos();
	}
	
	/**
     * Esta función, retorna mensaje de error de excepción lanzada o de espacio de memoria faltante en
     * unidad de almacenamiento.
     * @return Variable String que contiene error de excepción lanzada
     * o de espacio de memoria faltante en unidad de almacenamiento.
     */
	public String consultarMensajeError()
	{
		return extractorDatos.getMensajeError();
	}
	
	/**
     * Esta función, retorna mensaje de detección de dispositivo Android detectado o no.
     * @return Variable String que contiene mensaje de detección de dispositivo Android.
     */
	public String consultarMensajeDeteccion()
	{
		return reconocedorDispositivo.getMensajeDeteccion();
	}
	
	/**
	 * Esta función, retorna lista de dispositivos compatibles por aplicación en formato de lista.
	 * @return Variable String que muestra en formato de lista, lista de dispositivos compatibles por aplicación.
	 */
	public String consultarMostrarListaDispositivos()
	{
		String mostrarListaDispositivos;
		mostrarListaDispositivos = "";
		for(int i=0; i < listaDispositivos.size(); i++)
		{
			mostrarListaDispositivos = mostrarListaDispositivos + "- " + listaDispositivos.get(i).getFabricante() 
			+ ", " + listaDispositivos.get(i).getModelo()
			+ ", " + listaDispositivos.get(i).getVersionSo()
			+ ", " + listaDispositivos.get(i).getNumeroCompilacion() + "\n";
		}
		
		return mostrarListaDispositivos;
	}
	public String consultarDesMensajeError()
	{
		return DesblqPantalla.getMensajeError();
	}
}
