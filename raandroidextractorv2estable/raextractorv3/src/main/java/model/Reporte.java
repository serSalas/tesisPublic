package main.java.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Esta clase, contiene los métodos necesarios
 * para  la creación de reporte forense de extracción de datos de dispositivo Android detectado.
 * Reporte tendrá extensión PDF.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class Reporte 
{
	private String rutaReporte;
	private String rutaAlmacenamientoInterno;
	private Dispositivo dispositivo;	
	private UnidadAlmacenamiento unidadAlmacenamiento;
	private Hash hash;
	private ExtractorDatos extractorDatos;
	
	private BaseFont baseFont;
	private Font regularHead;
	private Font regularReport;
	private Font regularName;
	private Font regularAddress;
	private Font regularSub;
	private Font regularTitulo;
	private Font footerN;
	private Font footerE;
	private PdfPCell celdaTitulo;
	private PdfPCell celdaInformacion;
	
	private boolean isGeneratedReporte;
	
	/**
     * Este constructor ReporteExtraccionDatos, es el que genera el documento pdf del tipo  extracción de datos seleccionado utilizando los datos del modelo de dispositivo.
     * configurandolo con distintas fuentes de tipografía  para cambiar la apariencia del PDF.
     * @param dispositivo Instancia de dispositivo Android detectado.
     * @param unidadAlmacenamiento Instancia  de unidad de almacenamiento seleccionada por usuario.
     * @param hash Instancia que hace referencia a generacion de hash efectuada.
     * @param extractorDatos Instancia que hace referencia a extracción de datos efectuada.
     * @throws DocumentException Señala que se ha producido error de tipo DocumentException.
     * @throws IOException Señala que se ha producido excepción de E/S de algún tipo.
     */
	public Reporte(Dispositivo dispositivo, UnidadAlmacenamiento unidadAlmacenamiento, Hash hash, ExtractorDatos extractorDatos)
	{
		this.dispositivo = dispositivo;
		this.unidadAlmacenamiento = unidadAlmacenamiento;
		this.hash = hash;
		this.extractorDatos = extractorDatos;
	    
	    this.rutaReporte = unidadAlmacenamiento.getRutaDestino() + "/" + extractorDatos.getFormatoRuta() + "_Reporte.pdf";
	    
	    switch(extractorDatos.getNombreMetodoExtraccion())
	    {
	    	case "Metodo Mtp":
	    	{
	    		this.rutaAlmacenamientoInterno = dispositivo.getRutaMtp();
	    		break;
	    	}
	    	case "Metodo Adb":
	    	{
	    		this.rutaAlmacenamientoInterno = dispositivo.getRutaSdcard0() + " " + dispositivo.getRutaSdcard1();
	    		break;
	    	}
	    }
	    
	    try 
	    {
			this.baseFont = BaseFont.createFont("src/main/resources/fonts/Montserrat-Regular.ttf", "UTF-8", BaseFont.EMBEDDED);
		} 
	    catch (DocumentException | IOException e) 
	    {
			e.printStackTrace();
		}
	    this.regularHead = new Font(baseFont, 15,Font.BOLD,BaseColor.WHITE);
	    this.regularReport = new Font(baseFont, 30,Font.BOLD,BaseColor.BLACK);
	    this.regularName = new Font(baseFont, 24,Font.BOLD,BaseColor.BLACK);
	    this.regularAddress = new Font(baseFont, 12,Font.BOLD,BaseColor.BLACK);
	    this.regularSub = new Font(baseFont, 12);
	    this.regularTitulo = new Font(baseFont, 12,Font.BOLD,BaseColor.BLACK);
	    this.footerN = new Font(baseFont, 15,Font.BOLD,BaseColor.BLACK);
	    this.footerE = new Font(baseFont, 12,Font.NORMAL,BaseColor.BLACK);
	    
	    isGeneratedReporte = false;
	}
	
	/**
     * En este método, se utiliza y definie creación de tabla de documento con PdfPTable para organizar contenidos de documento generado.
     * Además se setearán títulos correspondiente al pie de página e información vinculada a dispositivo Android detectado
     * y a extracción de datos efectuada. Y también otros parámetros
     * como: tamaño de papel tipo A4 que es tamaño más común utilizado para este tipo de documentación.
     * También se le da formato  personalizado a tablas para cambiar apariencia de PDF como ser:
     * Bordes, cantidad de celdas, relleno de color de tabla, tipo de hash entre otros parámetros.
     * @throws DocumentException Señala que se ha producido error de tipo DocumentException.
     * @throws IOException Señala que se ha producido excepción de E/S de algún tipo.
     */
	public boolean generarReporte() 
	{
		isGeneratedReporte = false;
		Document documento = new Document(PageSize.A4, 36, 36, 36, 72);
		documento.addCreationDate();

		try 
		{
			// Location to save
			PdfWriter writer = PdfWriter.getInstance(documento, new FileOutputStream(rutaReporte));
			PdfPTable tablaPiePagina = new PdfPTable(2);
			tablaPiePagina.setTotalWidth(523);
			int contadorPagina = 1;
			PdfPCell tituloReporte = new PdfPCell(new Phrase("RA Android extractor",footerN));
			PdfPCell subtituloReporte = new PdfPCell(new Phrase("Forensia Movil",footerE));
			PdfPCell espacioVacio = new PdfPCell(new Phrase(""));
			PdfPCell contadorPag = new PdfPCell(new Phrase(Integer.toString(contadorPagina),footerE));


			tituloReporte.setBorder(Rectangle.NO_BORDER);
			espacioVacio.setBorder(Rectangle.NO_BORDER);
			subtituloReporte.setBorder(Rectangle.NO_BORDER);
			contadorPag.setBorder(Rectangle.NO_BORDER);
			contadorPag.setHorizontalAlignment(Element.ALIGN_RIGHT);
			
			PdfPCell preBorderBlue = new PdfPCell(new Phrase(""));
			preBorderBlue.setMinimumHeight(5f);
			preBorderBlue.setUseVariableBorders(true);
			preBorderBlue.setBorder(Rectangle.TOP);
			preBorderBlue.setBorderColorTop(new BaseColor(38,82,171));
			preBorderBlue.setBorderWidthTop(3);
			tablaPiePagina.addCell(preBorderBlue);
			tablaPiePagina.addCell(preBorderBlue);
			tablaPiePagina.addCell(tituloReporte);
			tablaPiePagina.addCell(espacioVacio);
			tablaPiePagina.addCell(subtituloReporte);
			tablaPiePagina.addCell(espacioVacio);


			PiePaginaReporte event = new PiePaginaReporte(tablaPiePagina);
			writer.setPageEvent(event);
			documento.open();
			documento.addTitle("Reporte forensia de dispositivos móviles");
			
			if(extractorDatos.getNombreMetodoExtraccion().equals("Metodo Dirty Cow") || 
			   extractorDatos.getNombreMetodoExtraccion().equals("Metodo Recovery") || 
			   extractorDatos.getNombreMetodoExtraccion().equals("Metodo Lg Laf")||
			   extractorDatos.getNombreMetodoExtraccion().equals("Metodo Dirty Cow Modificado")
			   )
			{
				documento.addSubject("Reporte de extracción de datos física");
			}
			else
			{
				documento.addSubject("Reporte de extracción de datos lógica");
			}
			
			documento.addKeywords("Android, forensia en dispositivos móviles, extracción de datos");
			documento.addAuthor("RA Android extractor");
			documento.addCreator("RA Android extractor");
			
			PdfPCell preTituloReporte = new PdfPCell(new Phrase("REPORTE",regularReport));


			preTituloReporte.setHorizontalAlignment(Element.ALIGN_RIGHT);
			preTituloReporte.setVerticalAlignment(Element.ALIGN_BOTTOM);
			preTituloReporte.setBorder(Rectangle.NO_BORDER);
			PdfPTable tablaCabeceraReporte = new PdfPTable(2);

			tablaCabeceraReporte.setWidths(new float[] {1,3});

			Image image = Image.getInstance("src/main/resources/images/logo.png");
			image.scaleToFit(30,30);
			PdfPCell logoReporte = new PdfPCell(image, true);
			logoReporte.setBorder(Rectangle.NO_BORDER);

			tablaCabeceraReporte.addCell(logoReporte);
			tablaCabeceraReporte.addCell(preTituloReporte);

			documento.add(tablaCabeceraReporte);

			PdfPTable piePagina = new PdfPTable(2);
			piePagina.setSpacingBefore(40);

			Date c = Calendar.getInstance().getTime();
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String formatoFechaReporte = df.format(c);


			PdfPCell nombreMetodo = new PdfPCell(new Phrase("Adquisicion de datos en dispositivo movil",regularName));
			PdfPCell fechaReporte = new PdfPCell(new Phrase("FECHA: "+formatoFechaReporte,regularAddress));

			fechaReporte.setVerticalAlignment(Element.ALIGN_BOTTOM);
			fechaReporte.setHorizontalAlignment(Element.ALIGN_RIGHT);
			nombreMetodo.setBorder(Rectangle.NO_BORDER);
			fechaReporte.setBorder(Rectangle.NO_BORDER);

			piePagina.addCell(nombreMetodo);

			piePagina.addCell(fechaReporte);
			documento.add(piePagina);

			PdfPTable tabla1 = new PdfPTable(2);
			tabla1.setSpacingBefore(20);
			tabla1.setWidths(new float[] {1,1});
			tabla1.setHeaderRows(1);
			tabla1.setSplitRows(false);
			tabla1.setComplete(false);
			
			///////////////////////////////////////////////////
			PdfPCell preBorderGray = new PdfPCell(new Phrase(""));
			preBorderGray.setPaddingTop(10);
			preBorderGray.setMinimumHeight(20f);
			preBorderGray.setUseVariableBorders(true);
			preBorderGray.setBorder(Rectangle.BOTTOM);
			preBorderGray.setBorderColorBottom(BaseColor.GRAY);
			preBorderGray.setBorderWidthBottom(3);
			preBorderGray.setColspan(5);
			////////////////////////////////////////////////////

			PdfPCell celdaInfoDispositivo = new PdfPCell(new Phrase("INFORMACION DEL DISPOSITIVO",regularHead));

			celdaInfoDispositivo.setPaddingLeft(15);
			celdaInfoDispositivo.setPaddingTop(10);
			celdaInfoDispositivo.setPaddingBottom(14);
			celdaInfoDispositivo.setVerticalAlignment(Element.ALIGN_CENTER);
			celdaInfoDispositivo.setHorizontalAlignment(Element.ALIGN_CENTER);

			celdaInfoDispositivo.setBackgroundColor(new BaseColor(38,82,171));
			celdaInfoDispositivo.setBorder(Rectangle.NO_BORDER);
			celdaInfoDispositivo.setColspan(2);
			tabla1.addCell(celdaInfoDispositivo);
			tabla1.addCell(celdaTitulo("MODELO", regularTitulo, 0));
			tabla1.addCell(celdaInformacion(dispositivo.getModelo(), regularSub, 0));
			tabla1.addCell(celdaTitulo("FABRICANTE", regularTitulo, 1));
			tabla1.addCell(celdaInformacion(dispositivo.getFabricante(), regularSub, 1));
			tabla1.addCell(celdaTitulo("VERSION DEL SO", regularTitulo, 2));
			tabla1.addCell(celdaInformacion(dispositivo.getVersionSo(), regularSub, 2));
			tabla1.addCell(celdaTitulo("NUMERO DE COMPILACION", regularTitulo, 3));
			tabla1.addCell(celdaInformacion(dispositivo.getNumeroCompilacion(), regularSub, 3));
			
			tabla1.addCell(preBorderGray);
			tabla1.setComplete(true);
			documento.add(tabla1);
			
			PdfPTable tabla2 = new PdfPTable(2);
			tabla2.setSpacingBefore(20);
			tabla2.setWidths(new float[] {1,1});
			tabla2.setHeaderRows(1);
			tabla2.setSplitRows(false);
			tabla2.setComplete(false);
			
			PdfPCell celdaInfoDispositivo2 = new PdfPCell(new Phrase("INFORMACION DE EXTRACCION DE DATOS",regularHead));

			celdaInfoDispositivo2.setPaddingLeft(15);
			celdaInfoDispositivo2.setPaddingTop(10);
			celdaInfoDispositivo2.setPaddingBottom(14);
			celdaInfoDispositivo2.setVerticalAlignment(Element.ALIGN_CENTER);
			celdaInfoDispositivo2.setHorizontalAlignment(Element.ALIGN_CENTER);

			celdaInfoDispositivo2.setBackgroundColor(new BaseColor(38,82,171));
			celdaInfoDispositivo2.setBorder(Rectangle.NO_BORDER);
			celdaInfoDispositivo2.setColspan(2);
			tabla2.addCell(celdaInfoDispositivo2);

			tabla2.addCell(celdaTitulo("METODO DE EXTRACCION", regularTitulo, 0));
			tabla2.addCell(celdaInformacion(extractorDatos.getNombreMetodoExtraccion(), regularSub, 0));
			tabla2.addCell(celdaTitulo("UBICACION DE EXTRACCION", regularTitulo, 1));
			tabla2.addCell(celdaInformacion(unidadAlmacenamiento.getRutaDestino(), regularSub, 1));
			
			if(extractorDatos.getNombreMetodoExtraccion().equals("Metodo Dirty Cow") || 
			   extractorDatos.getNombreMetodoExtraccion().equals("Metodo Recovery") || 
			   extractorDatos.getNombreMetodoExtraccion().equals("Metodo Lg Laf") ||
			   extractorDatos.getNombreMetodoExtraccion().equals("Metodo Dirty Cow Modificado")
			   )
			{
				tabla2.addCell(celdaTitulo("UBICACION DE PARTICION", regularTitulo, 2));
				tabla2.addCell(celdaInformacion(dispositivo.getRutaParticion(), regularSub, 2));
				tabla2.addCell(celdaTitulo("PESO DE EXTRACCION Y PARTICION", regularTitulo, 3));
				tabla2.addCell(celdaInformacion(extractorDatos.byteCountToDisplaySize(dispositivo.getTamanioParticion()), regularSub, 3));
				//tabla2.addCell(celdaInformacion(Long.toString(dispositivo.getTamanioParticion())+" bytes", regularSub, 3));
			}
			else
			{
				tabla2.addCell(celdaTitulo("UBICACION DE ALMACENAMIENTO INTERNO", regularTitulo, 2));
				tabla2.addCell(celdaInformacion(rutaAlmacenamientoInterno, regularSub, 2));
				tabla2.addCell(celdaTitulo("PESO DE EXTRACCION Y ALMACENAMIENTO INTERNO", regularTitulo, 3));
				tabla2.addCell(celdaInformacion(extractorDatos.byteCountToDisplaySize(dispositivo.getTamanioLogico()), regularSub, 3));
			}
			
			tabla2.addCell(celdaTitulo("INICIO DE EXTRACCION", regularTitulo, 4));
			tabla2.addCell(celdaInformacion(extractorDatos.getFechaInicioExtraccion(), regularSub, 4));
			tabla2.addCell(celdaTitulo("FINALIZACION DE EXTRACCION", regularTitulo, 5));
			tabla2.addCell(celdaInformacion(extractorDatos.getFechaFinalExtraccion(), regularSub, 5));
			tabla2.addCell(celdaTitulo("DURACION DE EXTRACCION", regularTitulo, 6));
			tabla2.addCell(celdaInformacion(extractorDatos.getDuracionExtraccion(), regularSub, 6));
			tabla2.addCell(celdaTitulo("ALGORITMO HASH", regularTitulo, 7));
			
			if(hash.isFlagHashMd5() && hash.isFlagHashSha256())
			{
				tabla2.addCell(celdaInformacion("MD5 y SHA-256", regularSub, 7));
			}
			else if(hash.isFlagHashMd5())
			{
				tabla2.addCell(celdaInformacion("MD5", regularSub, 7));
			}
			else if(hash.isFlagHashSha256())
			{
				tabla2.addCell(celdaInformacion("SHA-256", regularSub, 7));
			}
			else
			{
				tabla2.addCell(celdaInformacion("N/A", regularSub, 7));
			}
			
			tabla2.addCell(preBorderGray);
			tabla2.setComplete(true);
			documento.add(tabla2);
		
			if(hash.isFlagHashMd5() || hash.isFlagHashSha256())
			{
				PdfPTable tabla3 = new PdfPTable(2);
				tabla3.setSpacingBefore(20);
				tabla3.setWidths(new float[] {1,1});
				tabla3.setHeaderRows(1);
				tabla3.setSplitRows(false);
				tabla3.setComplete(false);
				
				PdfPCell celdaInfoDispositivo3 = new PdfPCell(new Phrase("HASH DE EXTRACCION DE DATOS",regularHead));
			
				celdaInfoDispositivo3.setPaddingLeft(15);
				celdaInfoDispositivo3.setPaddingTop(10);
				celdaInfoDispositivo3.setPaddingBottom(14);
				celdaInfoDispositivo3.setVerticalAlignment(Element.ALIGN_CENTER);
				celdaInfoDispositivo3.setHorizontalAlignment(Element.ALIGN_CENTER);
			
				celdaInfoDispositivo3.setBackgroundColor(new BaseColor(38,82,171));
				celdaInfoDispositivo3.setBorder(Rectangle.NO_BORDER);
				celdaInfoDispositivo3.setColspan(3);
				tabla3.addCell(celdaInfoDispositivo3);
				
				if(hash.isFlagHashMd5() && hash.isFlagHashSha256())
				{
					tabla3.addCell(celdaTitulo("MD5", regularTitulo, 0));
					tabla3.addCell(celdaInformacion(hash.getRutaHashMd5(), regularSub, 0));
					tabla3.addCell(celdaTitulo("SHA-256", regularTitulo, 1));
					tabla3.addCell(celdaInformacion(hash.getRutaHashSha256(), regularSub, 1));
				}
				else if(hash.isFlagHashMd5())
				{
					tabla3.addCell(celdaTitulo("MD5", regularTitulo, 0));
					tabla3.addCell(celdaInformacion(hash.getRutaHashMd5(), regularSub, 0));				
				}
				else if(hash.isFlagHashSha256())
				{
					tabla3.addCell(celdaTitulo("SHA-256", regularTitulo, 0));
					tabla3.addCell(celdaInformacion(hash.getRutaHashSha256(), regularSub, 0));				
				}
			
				tabla3.addCell(preBorderGray);
				tabla3.setComplete(true);
				documento.add(tabla3);
			}
			documento.close();
	       
			System.out.println("Se genero reporte.");
			isGeneratedReporte = true;
		} 
		catch (DocumentException | IOException e) 
		{
			isGeneratedReporte = false;
		}
		
		return isGeneratedReporte;
	}
	
	/**
     * En este método, se utiliza para  mejorar legibilidad de tablas grandes coloreando sus filas alternas.
     * Las tablas tendrán un fondo blanco claro para filas pares y grises para impares.
     * @param contenidoNo Variable de tipo String que contiene datos de su correspondiente a su fila.
     * @param regularTitulo Instancia de tipografía utilizada en documento.
     * @param aw Variable de tipo entero, el cual se utilizará para obtener valor par e impar.
     * @return Retorna celda oscura o blanca dependiendo si es par o impar.
     */
	private PdfPCell celdaTitulo(String contenidoNo, Font regularTitulo , int aw)
	{
		this.celdaTitulo = new PdfPCell(new Phrase(contenidoNo,regularTitulo));
		this.celdaTitulo.setPaddingLeft(15);
		this.celdaTitulo.setPaddingBottom(8);
		this.celdaTitulo.setPaddingTop(5);
		this.celdaTitulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
		this.celdaTitulo.setBorder(Rectangle.NO_BORDER);

		if (aw%2==0)
		{
			this.celdaTitulo.setBackgroundColor(BaseColor.WHITE);
		}
		else 
		{
			this.celdaTitulo.setBackgroundColor(BaseColor.LIGHT_GRAY);
		}
		return this.celdaTitulo;
	}
	
	/**
     * En este método, se utiliza para  mejorar legibilidad de tablas coloreando sus filas alternas.
     * Las tablas tendrán un fondo blanco claro para filas pares y grises para impares.
     * @param contenidoName Variable de tipo String que contiene datos de su correspondiente a su fila.
     * @param regularSub Instancia de tipografía utilizada en el documento.
     * @param aw Variable de tipo entero, el cual se utilizará para obtener valor par e impar.
     * @return Retorna celda oscura o blanca dependiendo si es par o impar.
     */
	private PdfPCell celdaInformacion(String contenidoName, Font regularSub , int aw)
	{
		this.celdaInformacion = new PdfPCell(new Phrase(contenidoName,regularSub));
		this.celdaInformacion.setPaddingBottom(8);
		this.celdaInformacion.setPaddingTop(5);
		this.celdaInformacion.setVerticalAlignment(Element.ALIGN_MIDDLE);
		this.celdaInformacion.setBorder(Rectangle.NO_BORDER);

		if (aw%2==0)
		{
			this.celdaInformacion.setBackgroundColor(BaseColor.WHITE);
		}
		else 
		{
			this.celdaInformacion.setBackgroundColor(BaseColor.LIGHT_GRAY);
		}
		return this.celdaInformacion;
	}
}
