/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.screenshottopdf;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import javax.swing.filechooser.FileSystemView;
/**
 *
 * @author frida
 */
public class ScreenshotToPdf {
    
    public static void main(String[] args) {
        int response = JOptionPane.showConfirmDialog(null, "Vuoi fare uno screenshot?", "Screenshot", JOptionPane.OK_CANCEL_OPTION);

        if (response == JOptionPane.OK_OPTION) {
            try {
                // Prende il percorso della cartella in cui salvare il file PDF
                JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnValue = fileChooser.showSaveDialog(null);
                
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    // Cattura lo screenshot
                    Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                    BufferedImage screenCapture = new Robot().createScreenCapture(screenRect);

                    // Percorso e nome del file PDF
                    File selectedDirectory = fileChooser.getSelectedFile();
                    File pdfFile = new File(selectedDirectory, "screenshot.pdf");

                    // Creazione del PDF
                    PDDocument document = new PDDocument();
                    PDPage page = new PDPage();
                    document.addPage(page);

                    // Conversione dell'immagine per il PDF
                    File tempImageFile = new File(selectedDirectory, "temp_screenshot.png");
                    ImageIO.write(screenCapture, "png", tempImageFile);
                    PDImageXObject pdfImage = PDImageXObject.createFromFile(tempImageFile.getAbsolutePath(), document);
                    
                    // Aggiunta dell'immagine al PDF
                    PDPageContentStream contentStream = new PDPageContentStream(document, page);
                    contentStream.drawImage(pdfImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
                    contentStream.close();

                    // Salvataggio del PDF
                    document.save(pdfFile);
                    document.close();
                    tempImageFile.delete();  // Rimuove il file temporaneo

                    JOptionPane.showMessageDialog(null, "Screenshot salvato come PDF in: " + pdfFile.getAbsolutePath());
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Si è verificato un errore durante il salvataggio dello screenshot.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
