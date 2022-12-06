package main.java.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

/**
 * Esta clase, genera hash md5 y/o sha256 de extracción de datos realizada
 * a dispositivo Android detectado.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class Hash 
{
	private String rutaHashMd5;
	private String rutaHashSha256;
	private ArrayList<String> codigoHashMd5;
	private ArrayList<String> codigoHashSha256;
	private ArrayList<String> listaRutaFicherosDestino;
	private boolean isFlagHashMd5;
	private boolean isFlagHashSha256;
	private boolean isGeneratedHash;
	private UnidadAlmacenamiento unidadAlmacenamiento;
	
	/**
     * Este constructor, realiza tratamiento de directorio en vez de fichero.
     * Es decir, recibe ruta de directorio destino que contiene un fichero o conjunto de ficheros y debido a que puede ser que contenga
     * mas de un fichero, directorio destino, se debe recorrer directorio para ir generando hash md5 y/o sha256
     * a cada uno de los ficheros.
     * Por lo tanto, su función es inicializar atributos y evitar que sean nulas.
     * @param unidadAlmacenamiento Instancia  de unidad de almacenamiento seleccionada.
     * @param isFlagHashMd5 Variable boolean, que contiene true si usuario quiere realizar hash MD5 o false caso contrario.
     * @param isFlagHashSha256 Variable boolean, que contiene true si usuario quiere realizar hash SHA-256 o caso contrario.
     */
	public Hash(UnidadAlmacenamiento unidadAlmacenamiento, boolean isFlagHashMd5, boolean isFlagHashSha256)
	{
		this.unidadAlmacenamiento = unidadAlmacenamiento;
		this.isFlagHashMd5 = isFlagHashMd5;
		this.isFlagHashSha256 = isFlagHashSha256;
		rutaHashMd5 = "";
		rutaHashSha256 = "";
		codigoHashMd5 = new ArrayList<String>();
		codigoHashSha256 = new ArrayList<String>();
		listaRutaFicherosDestino = new ArrayList<String>();
		File directorioDestino = new File(unidadAlmacenamiento.getRutaDestino());
		listarFicheros(directorioDestino);
		isGeneratedHash = false;
	}
	
	/**
	 * Esta método, se encarga de delegar si efectuar cálculo MD5 y/o SHA-256. 
	 * Enviando a cada funcion de cálculo de hash, formato de fichero destino.
	 * @param rutaFicheroDestino Variable String que contiene formato de fichero destino.
	 */
	public boolean generarHash(String rutaFicheroDestino)
	{
		
		if(isFlagHashMd5 || isFlagHashSha256)
		{
			if(isFlagHashMd5) //generar hash Md5
			{
				generarHashMd5(rutaFicheroDestino);
			}
			if(isFlagHashSha256) //generar hash Sha-256
			{
				generarHashSha256(rutaFicheroDestino);
			}
			System.out.println("Se genero codigo/s hash.");
		}
		else
		{
			isGeneratedHash = true;
		}
		
		return isGeneratedHash;
	}
	
	/**
     * Este método, genera fichero .md5 que contiene hash MD5 asociado a uno o a cada fichero
     * del conjunto de ficheros.
     * Recibe formato de extración de datos para capturar formato de nombre de fichero. De esta manera,
     * se coloca mismo nombre que directorio generado en unidad de almacenamietno pero con extensión .md5.
     * @param formatoRuta Variable String que contiene formato de extracción de datos 
     * para ficheros y directorios que se creen en ruta destino de unidad de almacenamiento.  
     */
	private void generarHashMd5(String formatoRuta)
	{	
		isGeneratedHash = false;
		rutaHashMd5 = unidadAlmacenamiento.getRutaDestino() + "/" + formatoRuta + "_Hash_MD5.md5";
        File ficheroHashMd5 = new File(rutaHashMd5);
        
		try 
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroHashMd5));
			for(int i=0; i<listaRutaFicherosDestino.size(); i++)
	    	{
	    		File ficheroDestino = new File(listaRutaFicherosDestino.get(i));
	    		
	    		HashCode hashCodeMd5 = Files.hash(ficheroDestino, Hashing.md5());
	    		codigoHashMd5.add(hashCodeMd5.toString());
	    		
	            bw.write(codigoHashMd5.get(i) + " [" + ficheroDestino.getAbsolutePath() + "]\n\n");
	    	}
	    	bw.close();
	    	isGeneratedHash = true;
		} 
		catch (IOException e) 
		{
			isGeneratedHash = false;
		}
	}
	
	/**
     * Este método, genera fichero .sha256 que contiene hash SHA-256 asociado a uno o a cada fichero
     * del conjunto de ficheros.
     * Recibe formato de extración de datos para capturar formato de nombre de fichero. De esta manera,
     * se coloca mismo nombre que directorio generado en unidad de almacenamietno pero con extensión .sha256.
     * @param formatoRuta Variable String que contiene formato de extracción de datos 
     * para ficheros y directorios que se creen en ruta destino de unidad de almacenamiento.  
     */
	private void generarHashSha256(String formatoRuta)
	{		
		isGeneratedHash = false;
		rutaHashSha256 = unidadAlmacenamiento.getRutaDestino() + "/" + formatoRuta + "_Hash_SHA-256.sha256";
        File ficheroHashSha256 = new File(rutaHashSha256);
        
		try 
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(ficheroHashSha256));
			for(int i=0; i<listaRutaFicherosDestino.size(); i++)
	    	{
	    		File ficheroDestino = new File(listaRutaFicherosDestino.get(i));
	    		
	    		HashCode hashCodeSha256 = Files.hash(ficheroDestino, Hashing.sha256());
	    		codigoHashSha256.add(hashCodeSha256.toString());
	    		
	            bw.write(codigoHashSha256.get(i) + " [" + ficheroDestino.getAbsolutePath() + "]\n\n");
	    	}
	    	bw.close();
	    	isGeneratedHash = true;
		} 
		catch (IOException e) 
		{
			isGeneratedHash = false;
		}
	}
	
	/**
     * Este método, almacena en una lista todos las rutas de ficheros de un directorio.
     * Recibe al directorio dir para ir consultando cada fichero que tenga internamente el directorio
     * y registrarlo en la lista listaRutaFicherosDestino.
     * @param dir Variable String que contiene ruta de un directorio.
     * Normalmente, el directorio se almacena extracción realizada por algún método lógico.
     */
	private void listarFicheros(File dir) 
	{
        File listFile[] = dir.listFiles();
        if (listFile != null) 
        {
            for (int i = 0; i < listFile.length; i++) 
            {
                if (listFile[i].isDirectory()) 
                {
                	listarFicheros(listFile[i]);
                } 
                else 
                {
                	listaRutaFicherosDestino.add(listFile[i].getAbsolutePath());
                }
            }
        }
    }
	
	/**
     * Esta función, retorna ruta del fichero hash MD5 generada por función
     * generarHashMd5Fichero o generarHashMd5Ficheros.
     * @return rutaHashMd5 Variable String que contiene ruta de fichero creado
     * por función generarHashMd5Fichero o generarHashMd5Ficheros.
     */
	public String getRutaHashMd5()
	{
		return rutaHashMd5;
	}
	
	/**
     * Esta función, retorna ruta de fichero hash SHA 256 generada por función
     * generarHashSha256Fichero o generarHashSha256Ficheros.
     * @return rutaHashSha256 Variable String que contiene ruta de fichero creado
     * por funcion generarHashSha256Fichero o generarHashSha256Ficheros.
     */
	public String getRutaHashSha256()
	{
		return rutaHashSha256;
	}
	
	/**
	 * Esta función, retorna variable booleana para indicar si se solicitó realizar hash MD5 o no.
	 * @return isFlagHashMd5 Variable boolean, que contiene true si usuario quiere realizar hash MD5 o caso contrario.
	 */
	public boolean isFlagHashMd5()
	{
		return isFlagHashMd5;
	}
	
	/**
	 * Esta función, retorna variable booleana para indicar si se solicitó realizar un hash SHA 256 o no.
	 * @return isFlagHashSha256 Variable boolean, que contiene true si usuario quiere realizar hash SHA 256 o caso contrario.
	 */
	public boolean isFlagHashSha256()
	{
		return isFlagHashSha256;
	}
}
