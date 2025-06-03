//package com.sure.utilities;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.text.PDFTextStripper;
//
//import java.io.File;
//import java.io.IOException;
//
//public class PdfReader {
//    private static final Logger logger = LogManager.getLogger(PdfReader.class);
//
//    private PdfReader() {
//    }
//
//    public static String getPdfData(String fileName, int startPage, int endPage) {
//        String filePath = System.getProperty("user.dir") + File.separator + "Files" + File.separator + "Download" + File.separator + fileName;
//        File file = new File(filePath);
//        String data = null;
//
//        try (PDDocument document = PDDocument.class.) {
//            PDFTextStripper pdfTextStripper = new PDFTextStripper();
//            pdfTextStripper.setStartPage(startPage);
//            pdfTextStripper.setEndPage(endPage);
//            data = pdfTextStripper.getText(document);
//        } catch (IOException e) {
//            logger.error("Failed to load or parse the PDF document: {}", fileName, e);
//        }
//        return data;
//    }
//
//    public static boolean verifyPDFContent(String fileName, String textInPDF, int startPage, int endPage) {
//        String pdfData = getPdfData(fileName, startPage, endPage);
//        if (pdfData == null) {
//            logger.warn("No data extracted from PDF: {}", fileName);
//            return false;
//        }
//        return pdfData.contains(textInPDF);
//    }
//}
