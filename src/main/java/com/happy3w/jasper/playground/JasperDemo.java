package com.happy3w.jasper.playground;

import com.lowagie.text.pdf.PdfWriter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.base.JRBasePrintLine;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.base.JRBasePrintText;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JasperDemo {
    public static void main(String[] args) throws JRException {
        InputStream inputStream = JasperDemo.class.getResourceAsStream("/reports/invoiceReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList<>());
        Map parameters = new HashMap();
        parameters.put("headup", "company");

        JasperPrint jasperPrint = getJasperPrint();
        WaterMarkRenderer.addMark(jasperPrint, "G18560", -55, 200);

        try (FileOutputStream outputStream = new FileOutputStream(new File("abc.pdf"))) {
            final JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));

            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            configuration.setPermissions(PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING);
            exporter.setConfiguration(configuration);

            exporter.exportReport();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static JasperPrint getJasperPrint() throws JRException {
        //JasperPrint
        JasperPrint jasperPrint = new JasperPrint();
        jasperPrint.setName("NoReport");
        jasperPrint.setPageWidth(595);
        jasperPrint.setPageHeight(842);

        //Fonts
        JRDesignStyle normalStyle = new JRDesignStyle();
        normalStyle.setName("Sans_Normal");
        normalStyle.setDefault(true);
        normalStyle.setFontName("DejaVu Sans");
        normalStyle.setFontSize(8);
        normalStyle.setPdfFontName("Helvetica");
        normalStyle.setPdfEncoding("Cp1252");
        normalStyle.setPdfEmbedded(false);
        jasperPrint.addStyle(normalStyle);

        JRDesignStyle boldStyle = new JRDesignStyle();
        boldStyle.setName("Sans_Bold");
        boldStyle.setFontName("DejaVu Sans");
        boldStyle.setFontSize(8);
        boldStyle.setBold(true);
        boldStyle.setPdfFontName("Helvetica-Bold");
        boldStyle.setPdfEncoding("Cp1252");
        boldStyle.setPdfEmbedded(false);
        jasperPrint.addStyle(boldStyle);

        JRDesignStyle italicStyle = new JRDesignStyle();
        italicStyle.setName("Sans_Italic");
        italicStyle.setFontName("DejaVu Sans");
        italicStyle.setFontSize(8);
        italicStyle.setItalic(true);
        italicStyle.setPdfFontName("Helvetica-Oblique");
        italicStyle.setPdfEncoding("Cp1252");
        italicStyle.setPdfEmbedded(false);
        jasperPrint.addStyle(italicStyle);

        JRPrintPage page = newJrPrintPage(jasperPrint, normalStyle, boldStyle, italicStyle);
        jasperPrint.addPage(page);
        JRPrintPage page2 = newJrPrintPage(jasperPrint, normalStyle, boldStyle, italicStyle);
        jasperPrint.addPage(page2);

        return jasperPrint;
    }

    private static JRPrintPage newJrPrintPage(JasperPrint jasperPrint, JRDesignStyle normalStyle, JRDesignStyle boldStyle, JRDesignStyle italicStyle) throws JRException {
        JRPrintPage page = new JRBasePrintPage();

        JRPrintLine line = new JRBasePrintLine(jasperPrint.getDefaultStyleProvider());
        line.setX(40);
        line.setY(50);
        line.setWidth(515);
        line.setHeight(0);
        page.addElement(line);

//        JRPrintImage image = new JRBasePrintImage(jasperPrint.getDefaultStyleProvider());
//        image.setX(0);
//        image.setY(0);
//        image.setWidth(165);
//        image.setHeight(400);
//        image.setScaleImage(ScaleImageEnum.CLIP);
////        image.setRenderer(new WaterMarkRenderer2());
//        image.setRenderer(
//                JRImageRenderer.getInstance(
//                        JRLoader.loadBytesFromResource("jasperreports.png")
//                )
//        );
//        page.addElement(image);

        JRPrintText text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
        text.setX(210);
        text.setY(55);
        text.setWidth(345);
        text.setHeight(30);
        text.setTextHeight(text.getHeight());
        text.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        text.setLineSpacingFactor(1.3133681f);
        text.setLeadingOffset(-4.955078f);
        text.setStyle(boldStyle);
        text.setFontSize(18);
        text.setText("JasperReports Project Description");
        page.addElement(text);

        text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
        text.setX(210);
        text.setY(85);
        text.setWidth(325);
        text.setHeight(15);
        text.setTextHeight(text.getHeight());
        text.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        text.setLineSpacingFactor(1.329241f);
        text.setLeadingOffset(-4.076172f);
        text.setStyle(italicStyle);
        text.setFontSize(12);
        text.setText((new SimpleDateFormat("EEE, MMM d, yyyy")).format(new Date()));
        page.addElement(text);

        text = new JRBasePrintText(jasperPrint.getDefaultStyleProvider());
        text.setX(40);
        text.setY(150);
        text.setWidth(515);
        text.setHeight(200);
        text.setTextHeight(text.getHeight());
        text.setHorizontalAlignment(HorizontalAlignEnum.JUSTIFIED);
        text.setLineSpacingFactor(1.329241f);
        text.setLeadingOffset(-4.076172f);
        text.setStyle(normalStyle);
        text.setFontSize(14);
        text.setText(
                "JasperReports is a powerful report-generating tool that has the ability to deliver rich " +
                        "content onto the screen, to the printer or into PDF, HTML, XLS, CSV or XML files.\n\n" +
                        "It is entirely written in Java and can be used in a variety of Java enabled applications, " +
                        "including J2EE or Web applications, to generate dynamic content.\n\n" +
                        "Its main purpose is to help creating page oriented, ready to print documents in a simple and flexible manner."
        );
        page.addElement(text);
        return page;
    }
}
