package main.java.model;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

/**
 * Esta clase, contiene métodos de extracción de datos
 * como método Dirty COW, método Recovery, método LG LAF,
 * método MTP y método ADB.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public abstract class ExtractorDatos 
{
	protected Dispositivo dispositivo;
	protected UnidadAlmacenamiento unidadAlmacenamiento;
	protected Hash hash;
	protected Reporte reporte;
	
	protected String nombreMetodoExtraccion;
	protected String mensajeError;
	private String formatoRuta;
	private String fechaInicioExtraccion;
	private String fechaFinalExtraccion;
	private String duracionExtraccion;
	private long horaI;
	private long horaF;
	
	protected boolean isFlagHashMd5;
	protected boolean isFlagHashSha256;
	
	/**
	 * Este constructor, recibe un dispositivo, una unidad de almacenamiento y dos variables boolenas para saber si efectuar calculo hash.
	 * Su función es inicializar atributos y evitar que sean nulas.
	 * @param dispositivo Instancia de dispositivo Android detectado.
	 * @param unidadAlmacenamiento Instancia  de unidad de almacenamiento seleccionada.
	 * @param isFlagHashMd5 Variable boolean, que contiene true si usuario quiere realizar hash MD5 o false caso contrario.
	 * @param isFlagHashSha256 Variable boolean, que contiene true si usuario quiere realizar hash SHA 256 o caso contrario.
	 */
	public ExtractorDatos(Dispositivo dispositivo, UnidadAlmacenamiento unidadAlmacenamiento, boolean isFlagHashMd5, boolean isFlagHashSha256)
	{
		this.dispositivo = dispositivo;
		this.unidadAlmacenamiento = unidadAlmacenamiento;
		this.isFlagHashMd5 = isFlagHashMd5;
		this.isFlagHashSha256 = isFlagHashSha256;
		nombreMetodoExtraccion = "";
		mensajeError = "";
		formatoRuta = "";
		fechaInicioExtraccion = "";
		fechaFinalExtraccion = "";
		duracionExtraccion = "";
		horaI = 0;
		horaF = 0;
	}
	
	/**
	 * Este método, se encarga de llevar acabo tecnica de extraccion de datos forenses,
	 * dependiendo de la técnica que haya seleccionado ususario.  
	 */
	public abstract void extraerDatos();
	
	/**
     * Esta función, retorna porcentaje de la extracción de datos.
     * @return Variable long que contiene porcentaje de extracción
     * de datos.
     */
	public abstract long getPorcentajeExtraccionDatos();
	
	/**
     * Esta función, retorna tamaño parcial de extracción de datos convertido en formato
     * legible para humano.
     * @return Variable String que contiene tamaño parcial de extracción de datos
     * convertido en formato legible para humano.
     */
	public abstract String getTamanioParcialExtraccionDatos();
	
	/**
     * Esta función, retorna estado de extracción de datos.
     * Que permite ser consultada ya sea por modelo o por controladores.
     * @return estadoExtraccionDatos Variable entera.
     */
	public abstract int getEstadoExtraccionDatos();
	
	/**
     * Este método, genera formato especial, ya sea para directorio o para fichero.
     * El formato que genera es para fichero como para directorios.
     * Para ficheros como para directorio, path sería por ejemplo:
     * Extraccion_Logica_samsung_GT-I8550L_28-07-2019_16-50-34
     */
	public void formatoRuta(String tipoExtraccion)
	{
		Calendar fechaHora = Calendar.getInstance();
		int dia = fechaHora.get(5);
		int mes = fechaHora.get(2) + 1;
		int anio = fechaHora.get(1);
		int segundo = fechaHora.get(13);
		int minuto = fechaHora.get(12);
		int hora = fechaHora.get(11);
		
		formatoRuta = tipoExtraccion + "_" 
				  + dispositivo.getFabricante() + "_" + dispositivo.getModelo() + "_" + String.format("%02d", dia) + "-" 
			      + String.format("%02d", mes) + "-" + String.format("%04d", anio) + "_" + String.format("%02d", hora) + "-"
			      + String.format("%02d", minuto) + "-" + String.format("%02d", segundo);
	}	
	
	/**
     * Este método, registra fecha de inicio de extracción de datos.
     * Se la utiliza por reporte.
     * Su formato es dd/MM/yyyy.
     * La variable que contiene fecha de inicio es fechaInicioExtraccion,
     * que es una variable String.
     */
	public void setFechaInicioExtraccion() 
	{	
		Calendar calInicio = Calendar.getInstance();
		calInicio.setTime(new Date());
		Date date = calInicio.getTime();
		
		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		fechaInicioExtraccion = hourdateFormat.format(date);
        horaI = Instant.now().getEpochSecond();
	}

	/**
     * Este método, registra fecha de finalización de extracción de datos.
     * Se la utiliza por reporte.
     * Su formato es dd/MM/yyyy.
     * La variable que contiene fecha de finalización es fechaFinalExtraccion,
     * que es una variable String.
     */
	public void setFechaFinalExtraccion() 
	{
		Calendar calFinal = Calendar.getInstance();
		calFinal.setTime(new Date());
		Date date = calFinal.getTime();

		DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		fechaFinalExtraccion = hourdateFormat.format(date);
        horaF = Instant.now().getEpochSecond();
	}
	
	/**
     * Este método, registra duración de la extracción de datos.
     * Se la utiliza por reporte.
     * Su formato es HH:mm:ss.
     * La variable que contiene duración de extracción es duracionExtraccion,
     * que es una variable String.
     */
	public void setDuracionExtraccion()
	{
		long seconds = horaF - horaI;
        long hor,min,seg;

        hor = seconds/3600;
        min = (seconds-(3600*hor))/60;
        seg = seconds-((hor*3600)+(min*60));
        
		Calendar calDuracion = Calendar.getInstance();
		calDuracion.set(Calendar.HOUR, (int) hor);
		calDuracion.set(Calendar.MINUTE, (int) min);
		calDuracion.set(Calendar.SECOND, (int) seg);
		calDuracion.set(Calendar.AM_PM,Calendar.AM);
		
		//Desplegamos la fecha
		Date date = calDuracion.getTime();
		date = calDuracion.getTime();
		DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
		duracionExtraccion = hourFormat.format(date);
	}
	
	/**
     * Esta función, retorna tamaño legible por humano.
     * Recibe como parámetro variable long expresada en bytes
     * para convertirla en String legible por humano.  
     * Por ejemplo: size = 5573804032L readableSize = 5,19 GB.
     * @param size Variable long que contiene tamaño en bytes.
     * @return readableSize Variable String que contiene tamaño legible por humano.
     */
	public String byteCountToDisplaySize(long size)
	{
		String readableSize;
		if(size==0)
		{
			readableSize = "0 B";
		}
		else
		{
			String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
		    int unitIndex = (int) (Math.log10(size)/3);
		    double unitValue = 1 << (unitIndex * 10);
		 
		    readableSize = new DecimalFormat("#,##0.##").format(size/unitValue) + " " + units[unitIndex];
		}
	    
	    return readableSize;
	}
	
	/**
     * Esta función, retorna error de excepción lanzada o de espacio de memoria faltante en
     * unidad de almacenamiento.
     * @return mensajeError Variable String que contiene error de excepción lanzada
     * o de espacio de memoria faltante en unidad de almacenamiento.
     */
	public String getMensajeError()
	{
		return mensajeError;
	}
	
	/**
     * Esta función, retorna nombre de método de extracción de datos que eligió usuario.
     * @return nombreMetodoExtraccion Variable String que contiene nombre de método 
     * de extracción de datos que eligió usuario.
     */
	public String getNombreMetodoExtraccion()
	{
		return nombreMetodoExtraccion;
	}
	
	/**
     * Esta función, retorna fecha de inicio de extracción de datos en formato dd/MM/yyyy.
     * @return fechaInicioExtraccion Variable String que contiene fecha de inicio 
     * de extracción de datos. 
     */
	public String getFechaInicioExtraccion()
	{
		return fechaInicioExtraccion;
	}
	
	/**
     * Esta función, retorna fecha de finalización de extracción de datos en formato dd/MM/yyyy.
     * @return fechaFinalExtraccion Variable String que contiene fecha de finalización 
     * de extracción de datos. 
     */
	public String getFechaFinalExtraccion()
	{
		return fechaFinalExtraccion;
	}
	
	/**
     * Esta función, retorna duración de extracción de datos en formato HH:mm:ss.
     * @return fechaFinalExtraccion Variable String que contiene duración 
     * de extracción de datos.
     */
	public String getDuracionExtraccion()
	{
		return duracionExtraccion;
	}
	
	/**
     * Esta función, retorna formato de extracción de datos para ficheros y directorios que se creen
     * en ruta destino de unidad de almacenamiento. 
     * El formato es Nombre-de-Extraccion_Marca-de-dispositivo_Modelo-de-dispositivo_dia-mes-año_hr-min-seg.
     * @return formatoRuta Variable String que contiene formato de extracción de datos 
     * para ficheros y directorios que se creen en ruta destino de unidad de almacenamiento. 
     */
	public String getFormatoRuta()
	{
		return formatoRuta;
	}
}
