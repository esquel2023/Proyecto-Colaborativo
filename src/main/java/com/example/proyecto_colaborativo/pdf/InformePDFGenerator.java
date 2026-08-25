package com.example.proyecto_colaborativo.pdf;

import com.example.proyecto_colaborativo.Clases.Producto; // Asegurate de que tu clase Producto esté bien importada
import com.example.proyecto_colaborativo.Clases.claseFactura;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;

public class InformePDFGenerator {

    // Se pasa el objeto Document para poder escribir las líneas dentro del flujo del PDF
    private static void agregarEncabezado(Document document) throws Exception {

        // ===== TÍTULO ARRIBA =====
        Font titulo = new Font(Font.TIMES_ROMAN, 12, Font.BOLD);
        Paragraph tituloP = new Paragraph("FACTURA", titulo);
        tituloP.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloP);

        // ===== TABLA LOGO + TEXTO =====
        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1.5f, 6});

        // ===== LOGO =====
        Image logo = Image.getInstance(InformePDFGenerator.class.getResource("/ImagenesBilletes/billete20000.jpg"));
        logo.scaleToFit(70, 70);
        logo.setAlignment(Image.ALIGN_CENTER);

        PdfPCell celdaLogo = new PdfPCell(logo);
        celdaLogo.setBorder(Rectangle.NO_BORDER);
        celdaLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tabla.addCell(celdaLogo);

        // ===== TEXTO EMISOR =====
        Font normal = new Font(Font.TIMES_ROMAN, 10);
        Font negrita = new Font(Font.TIMES_ROMAN, 10, Font.BOLD);

        Paragraph texto = new Paragraph();
        texto.add(new Chunk("87787/7", negrita));
        texto.add(new Chunk(" – Especialista en Gastroenterología.\n", normal));
        texto.add(new Chunk("Miembro titular de la Soc. Argentina de Gastroenterología (SAGE).\n", normal));
        texto.add(new Chunk("Miembro titular de la Asociación Argentina de Endoscopistas Digestivos (AAED).\n\n", normal));
        texto.add(new Chunk("M.P. 1624 – M.N. 80.094", normal));

        texto.setAlignment(Element.ALIGN_LEFT);

        PdfPCell celdaTexto = new PdfPCell(texto);
        celdaTexto.setBorder(Rectangle.NO_BORDER);
        celdaTexto.setVerticalAlignment(Element.ALIGN_MIDDLE);
        tabla.addCell(celdaTexto);

        document.add(tabla);

        // ===== LÍNEA SEPARADORA =====
        LineSeparator linea = new LineSeparator();
        linea.setOffset(-5);
        document.add(new Chunk(linea));
    }

    public static File generarFactura(claseFactura factura, String carpetaDestino) throws Exception {

        File carpeta = new File(carpetaDestino);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        String archivo = "Factura_" + factura.getNumeroDeFactura().replace("/", "-") + ".pdf";
        File pdf = new File(carpeta, archivo);

        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(document, new FileOutputStream(pdf));

        document.open();

        // Renderizar el membrete médico superior
        agregarEncabezado(document);
        document.add(Chunk.NEWLINE);

        // ===== TÍTULO DEL COMPROBANTE COMERCIAL =====
        Font tituloFacturaFont = new Font(Font.HELVETICA, 14, Font.BOLD);
        Paragraph tituloFactura = new Paragraph("FACTURA DE VENTA", tituloFacturaFont);
        tituloFactura.setAlignment(Element.ALIGN_CENTER);
        document.add(tituloFactura);
        document.add(Chunk.NEWLINE);

        // ===== DATOS DEL COMPROBANTE Y DEL CLIENTE =====
        Font fuenteDetalle = new Font(Font.HELVETICA, 10, Font.NORMAL);
        PdfPTable tablaDatos = new PdfPTable(2);
        tablaDatos.setWidthPercentage(100);
        tablaDatos.setWidths(new float[]{1f, 1f});

        tablaDatos.addCell(celdaConBorde("Cliente: " + factura.getCliente(), fuenteDetalle));
        tablaDatos.addCell(celdaConBorde("Factura N°: " + factura.getNumeroDeFactura(), fuenteDetalle));
        tablaDatos.addCell(celdaConBorde("DNI / CUIT: " + factura.getDNI(), fuenteDetalle));
        tablaDatos.addCell(celdaConBorde("Fecha Emisión: " + factura.getFechaEmison(), fuenteDetalle));
        tablaDatos.addCell(celdaConBorde("Condición IVA: Consumidor Final", fuenteDetalle));
        tablaDatos.addCell(celdaConBorde("Método de Pago: " + factura.getMetodoDePago(), fuenteDetalle));

        document.add(tablaDatos);
        document.add(Chunk.NEWLINE);

        // ===== DETALLE GRÁFICO DE LOS PRODUCTOS VENDIDOS =====
        Font cabeceraFont = new Font(Font.HELVETICA, 10, Font.BOLD);
        PdfPTable tablaProductos = new PdfPTable(4);
        tablaProductos.setWidthPercentage(100);
        tablaProductos.setWidths(new float[]{5, 1.5f, 1.5f, 2});

        tablaProductos.addCell(celdaCabecera("Descripción del Artículo", cabeceraFont));
        tablaProductos.addCell(celdaCabecera("Cant.", cabeceraFont));
        tablaProductos.addCell(celdaCabecera("P. Unitario", cabeceraFont));
        tablaProductos.addCell(celdaCabecera("Subtotal", cabeceraFont));

        // 🔥 MODIFICACIÓN CRÍTICA: Bucle para iterar y pintar todos los productos dinámicamente
        if (factura.getListaProductos() != null) {
            for (Producto prod : factura.getListaProductos()) {
                // Celda Descripción
                PdfPCell cNombre = new PdfPCell(new Phrase(prod.getNombre(), fuenteDetalle));
                cNombre.setPadding(6);
                tablaProductos.addCell(cNombre);

                // Celda Cantidad
                PdfPCell cCant = new PdfPCell(new Phrase(String.valueOf(prod.getCantidad()), fuenteDetalle));
                cCant.setHorizontalAlignment(Element.ALIGN_CENTER);
                cCant.setPadding(6);
                tablaProductos.addCell(cCant);

                // Celda Precio Unitario
                PdfPCell cPrecio = new PdfPCell(new Phrase(String.format("$%.2f", prod.getPrecio()), fuenteDetalle));
                cPrecio.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cPrecio.setPadding(6);
                tablaProductos.addCell(cPrecio);

                // Cálculo y Celda Subtotal por ítem
                double subtotalItem = prod.getPrecio() * prod.getCantidad();
                PdfPCell cSub = new PdfPCell(new Phrase(String.format("$%.2f", subtotalItem), fuenteDetalle));
                cSub.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cSub.setPadding(6);
                tablaProductos.addCell(cSub);
            }
        }

        document.add(tablaProductos);

        // ===== SECCIÓN DE TOTAL GENERAL =====
        Font totalFont = new Font(Font.HELVETICA, 11, Font.BOLD);
        PdfPTable tablaTotal = new PdfPTable(2);
        tablaTotal.setWidthPercentage(100);
        tablaTotal.setWidths(new float[]{7.5f, 2.5f});

        PdfPCell celdaVacia = new PdfPCell(new Phrase(""));
        celdaVacia.setBorder(Rectangle.NO_BORDER);
        tablaTotal.addCell(celdaVacia);

        // 🔥 MODIFICACIÓN CRÍTICA: Vinculado a la variable de precio total general acumulado de Lombok
        PdfPCell celdaTotal = new PdfPCell(new Phrase(String.format("TOTAL: $%.2f", factura.getPrecioFinal()), totalFont));
        celdaTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
        celdaTotal.setPadding(8);
        tablaTotal.addCell(celdaTotal);

        document.add(Chunk.NEWLINE);
        document.add(tablaTotal);

        document.close();
        return pdf;
    }

    private static PdfPCell celdaConBorde(String texto, Font fuente) {
        PdfPCell c = new PdfPCell(new Phrase(texto, fuente));
        c.setBorder(Rectangle.BOX);
        c.setPadding(6);
        return c;
    }

    private static PdfPCell celdaCabecera(String texto, Font fuente) {
        PdfPCell c = new PdfPCell(new Phrase(texto, fuente));
        c.setPadding(6);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setBackgroundColor(com.lowagie.text.html.Markup.decodeColor("#F5F5F5"));
        return c;
    }
}
