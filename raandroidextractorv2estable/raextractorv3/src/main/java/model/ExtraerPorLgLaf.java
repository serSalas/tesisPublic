package main.java.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Esta clase, es una estrategia de metodo de extracción.
 * En este caso se efectua extracción de datos por LG LAF.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class ExtraerPorLgLaf extends ExtractorDatos 
{
	private static int estadoExtraccionLgLaf;
	private String pathDestino;

	public ExtraerPorLgLaf(Dispositivo dispositivo, UnidadAlmacenamiento unidadAlmacenamiento, boolean isFlagHashMd5, boolean isFlagHashSha256) 
	{
		super(dispositivo, unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
		estadoExtraccionLgLaf = 0;
		pathDestino = "";
	}
	
	/**
     * Este método, realiza extracción de datos utilizando protocolo Lg laf.
     * El método cuenta con 7 estados.
     * Estado 0:
     * Chequea que dispositivo Android detectado este conectado a PC y en modo download, mediante protocolo Lg laf.
     * Estado 1:
     * Chequea que en unidad de almacenamiento seleccionada se encuentre con suficiente memoria.
     * Es decir, debe poseer mayor cantidad de memoria libre que lo que ocupa partición o almacenamiento
     * de dispositivo Android detectado. En caso que no posea suficiente memoria unidad de almacenamiento
     * se notifica con mensaje de error.
     * Estado 2:
     * Se extrae partición general /mmcblk0 de dispositivo Android detectado mediante protocolo LG LAF. Registrando fecha de inicio,
     * fecha de finalización y tiempo de duración de extracción realizada.
     * Estado 3:
     * Se apaga dispositivo Android detectado mediante protocolo LG LAF.
     * Estado 4:
     * Se realiza el o los hash a partir de extracción de datos efectuada. Dando como resultado uno o dos archivos .md5 y/o .sha256.
     * Estado 5:
     * Se realiza reporte de extracción de datos.
     * Estado -1:
     * Cuando ocurre excepción o espacio de almacenamiento es insuficiente en unidad de almacenamiento.
     * Además, se registra error de excepción en variable mensajeError.
     */
	@Override
	public void extraerDatos() 
	{
		try
		{
			switch(estadoExtraccionLgLaf)
			{
				case 0:
				{
					//compruebo conexion y verifico que el dispositivo esta en modo download
					ProcessBuilder builder = new ProcessBuilder("python3",dispositivo.getRutaLglaf(),"--rawshell","--debug","-c ls");
					Process p = builder.start();
					int exitCode = p.waitFor();
			        if(exitCode != 0)
			        {
			        	System.out.println("SE PRODUJO UN ERROR: "+  exitCode);
			        }
			        else if (exitCode == 0)
			        {
			        	System.out.println("SIN ERRORES: "+ exitCode);
			        	estadoExtraccionLgLaf = 1;
			        }
			        break;
				}
				case 1:
				{
					if(unidadAlmacenamiento.chequearTamanioLibre(dispositivo.getTamanioParticion()))
					{
						estadoExtraccionLgLaf = 2;
					}
					else
					{
						mensajeError = "La unidad de almacenamiento seleccionada no dispone de suficiente espacio de memoria. "
								+ "Se necesitan mas de " 
								+ unidadAlmacenamiento.getTamanioFaltante(dispositivo.getTamanioParticion()) 
								+ " en la unidad de almacenamiento para guardar la extraccion de datos";
						estadoExtraccionLgLaf = -1;
					}
					break;
				}
				case 2:
				{
					formatoRuta("Extraccion_Fisica");
					unidadAlmacenamiento.crearDirectorioRutaDestino(getFormatoRuta());

					pathDestino = unidadAlmacenamiento.getRutaDestino() + "/" + getFormatoRuta() + ".img"; 	
					//extrae todas las particiones
					ProcessBuilder builder = new ProcessBuilder("python3",dispositivo.getRutaLglafExtractPartitions(),"-d",unidadAlmacenamiento.getRutaDestino(),"--max-size", dispositivo.getCantidadBloques(),"-n",pathDestino);
					setFechaInicioExtraccion();
					Process p = builder.start(); // may throw IOException
					BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			        String line;
			        while ((line = reader.readLine()) != null) 
			        {
			            System.out.println(line);
			        }
			        int exitCode = p.waitFor();
			        if(exitCode != 0)
			        {
			        	System.out.println("SE PRODUJO UN ERROR: "+  exitCode);
			        	mensajeError = "Device not found";
			        	estadoExtraccionLgLaf = -1;
			        }
			        else if (exitCode == 0)
			        {
			        	estadoExtraccionLgLaf = 3;
			        	System.out.println("SIN ERRORES: "+ exitCode);
						setFechaFinalExtraccion();
						setDuracionExtraccion();
			        }
			        break;
				}
				case 3:
				{
					File ficheroTemporal_1 = File.createTempFile("ApagarLglaf", ".txt");
			        
					FileWriter flwriter_1 = new FileWriter(ficheroTemporal_1);
					BufferedWriter bfwriter_1 = new BufferedWriter(flwriter_1);
					bfwriter_1.write("!CTRL POFF\n");
					bfwriter_1.close();
					flwriter_1.close();
					
					ProcessBuilder builder_1 = new ProcessBuilder("python3",dispositivo.getRutaLglaf());
					builder_1.redirectInput(ficheroTemporal_1);
					Process p_1 = builder_1.start();
			        int exitCode_1 = p_1.waitFor();
			        if(exitCode_1 != 0)
			        {
			        	System.out.println("SE PRODUJO UN ERROR: "+  exitCode_1);
			        	mensajeError = "Device not found";
			        	estadoExtraccionLgLaf = -1;
			        }
			        else if (exitCode_1 == 0)
			        {
			        	System.out.println("SIN ERROR: "+ exitCode_1);
			        	System.out.println("Se realizo extraccion de datos utilizando metodo LG LAF.");
			        	estadoExtraccionLgLaf = 4;
			        }
					ficheroTemporal_1.deleteOnExit();
					break;
				}
				case 4:
				{
					hash = new Hash(unidadAlmacenamiento, isFlagHashMd5, isFlagHashSha256);
					if(hash.generarHash(getFormatoRuta()))
					{
						estadoExtraccionLgLaf = 5;
					}
					else
					{
						mensajeError = "No se pudo generar código/s hash.";
						estadoExtraccionLgLaf = -1;
					}
					break;
				}
				case 5:
				{
					nombreMetodoExtraccion = "Metodo Lg Laf";
					reporte = new Reporte(dispositivo, unidadAlmacenamiento, hash, this);
					if(reporte.generarReporte())
					{
						estadoExtraccionLgLaf = 6;
					}
					else
					{
						mensajeError = "No se pudo generar reporte.";
						estadoExtraccionLgLaf = -1;
					}
					break;
				}
			}			
		}
		catch(IOException | InterruptedException e)
		{
			mensajeError = e.getMessage();
			estadoExtraccionLgLaf = -1;
		}
	}
	
	/**
     * Esta función, retorna porcentaje de extracción de datos física.
     * Necesita de instancia dispositivo Android detectado para conocer tamaño en bytes
     * de la partición. Y teniendo tamaño en bytes parcial de fichero destino
     * se calcula porcentaje. Tamaño Parcial/Tamaño Total x 100.
     * @return porcentajeExtraccionDatos Variable long que contiene porcentaje de extracción
     * de datos.
     */
	@Override
	public long getPorcentajeExtraccionDatos() 
	{
		long porcentajeExtraccionDatos = 0;
		long tamanioOrigen = dispositivo.getTamanioParticion();
		long tamanioDestino = unidadAlmacenamiento.getTamanioLibre();
		
		if(tamanioOrigen == 0)
		{
			porcentajeExtraccionDatos = 100;
		}
		else
		{
			porcentajeExtraccionDatos = (tamanioDestino*100)/tamanioOrigen;
		}
		
		return porcentajeExtraccionDatos;
	}
	
	/**
     * Esta función, retorna tamaño parcial de extracción de datos física convertido en formato
     * legible para humano.
     * Necesita de instancia dispositivo Android detectado para conocer tamaño en bytes
     * de partición. Y teniendo tamaño en bytes parcial de ruta destino de unidad de almacenamiento
     * se concatena ambos tamaños quedando de la siguiente forma. Tamaño Parcial de Tamaño Total.
     * Por ejemplo: 200,15 MB de 5,19 GB.
     * @return tamanioLegibleParcial Variable String que contiene tamaño parcial de extracción de datos
     * convertido en formato legible para humano.
     */
	@Override
	public String getTamanioParcialExtraccionDatos() 
	{
		//String tamanioLegibleOrigen = FileUtils.byteCountToDisplaySize(dispositivo.getTamanioParticion()).toString();
		//String tamanioLegibleDestino = FileUtils.byteCountToDisplaySize(this.getTamanioFicheroDestino()).toString();
		String tamanioLegibleOrigen = byteCountToDisplaySize(dispositivo.getTamanioParticion()).toString();
		String tamanioLegibleDestino = byteCountToDisplaySize(unidadAlmacenamiento.getTamanioLibre()).toString();
		String tamanioLegibleParcial = tamanioLegibleDestino + " de " + tamanioLegibleOrigen;
				
		return tamanioLegibleParcial;
	}
	
	/**
     * Esta función, retorna estado de extracción de datos.
     * Que permite ser consultada ya sea por modelo o por controladores.
     * @return estadoExtraccionLgLaf Variable entera.
     */
	public int getEstadoExtraccionDatos()
	{
		return estadoExtraccionLgLaf;
	}
}
