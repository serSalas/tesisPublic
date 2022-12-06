package main.java.model;

import java.util.List;

/**
 * Esta clase, es una estrategia de detectar dispositivo.
 * En este caso se efectua detección de dispositivo manualmente.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class DetectarManual extends ReconocedorDispositivo
{
	String propDispositivo;
	
	public DetectarManual(List<Dispositivo> listaDispositivos, String propDispositivo)
	{
		super(listaDispositivos);
		this.propDispositivo = propDispositivo;
	}
	
	/**
     * Esta función, retorna true si se encontro dispositivo Android
     * en lista de dispositivos. O retorna false si no hubo coincidencia.
     * @param propDispositivo Variable String que contiene dispositivo elegido
     * por usuario manualmente.
     * Comparando marca, modelo, versión del sistema operativo y número de compilacion
     * entre dispositivo Android y cada dispositivo de la lista.
     * @return esDetectado Variable boolean que devuelve verdadero en caso que hubo
     * coincidencia entre alguno de los dispositivos de la lista dispositivo con dispositivo Android conectado (propDispositivo)
     * elegido por el usuario o falso si no hubo coincidencia.
     */
	@Override
	public boolean detectarDispositivo() 
	{
		deseleccionarDispositivos();
		esDetectado = false;
		
		String[] separarPropDispositivo = propDispositivo.split(", ");
		
		for(int i=0; i < listaDispositivos.size(); i++)
		{
			if(listaDispositivos.get(i).getFabricante().equals(separarPropDispositivo[0]) 
			&& listaDispositivos.get(i).getModelo().equals(separarPropDispositivo[1])
			&& listaDispositivos.get(i).getVersionSo().equals(separarPropDispositivo[2])
			&& listaDispositivos.get(i).getNumeroCompilacion().equals(separarPropDispositivo[3]))
			{
				listaDispositivos.get(i).setSeleccionar(true);
				esDetectado = true;
				break;
			}
			else
			{
				esDetectado = false;
			}
		}
		System.out.println(esDetectado);
		return esDetectado;
	}
}
