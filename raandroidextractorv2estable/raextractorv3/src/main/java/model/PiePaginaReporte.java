package main.java.model;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Esta clase, contiene los métodos necesarios
 * para  creación de pie de página de documento en reporte final
 * generado junto a extracción de datos de dispositivo Android detectado,
 * estableciendo formato de documento utilizando tipografía personalizada.
 * @author Sosa Ludueña Diego
 * @author Choquevilca Gustavo
 * @author Montiel Emiliano
 *
 */
public class PiePaginaReporte extends PdfPageEventHelper 
{
    private PdfPTable footer;
	private BaseFont baseFont = BaseFont.createFont("src/main/resources/fonts/Montserrat-Regular.ttf", "UTF-8",BaseFont.EMBEDDED);
    private Font footerE = new Font(baseFont, 12,Font.NORMAL,BaseColor.BLACK);
	private int contadorPagina = 1;
	
	/**
     * En este método se instancia pie de página de documento.
     * @param footer Instanciado de PdfTable
     * @throws DocumentException Señala que se ha producido error de tipo DocumentException.
     * @throws IOException Señala que se ha producido excepción de E/S de algún tipo.
     */
    public PiePaginaReporte(PdfPTable footer) throws DocumentException, IOException 
    {
        this.footer = footer;
    }
    
    /**
     * En este método se define a finPagina y se aplica cuando se termina una página, 
     * justo antes de inscribirse en documento pdf dentro de reporte.
     * Se puede agregar encabezado y pie de página a cada página dentro de documento PDF.
     */
    public void finPagina(PdfWriter writer, Document document) 
    {

        footer.writeSelectedRows(0, -1, 36, 64, writer.getDirectContent());
        String numeroPagina = Integer.toString(contadorPagina);
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(numeroPagina,footerE), 550,23,0);
		contadorPagina++;        
    }
}