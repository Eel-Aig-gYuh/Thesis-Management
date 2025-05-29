/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.utils;

import com.ghee.services.impl.ScoreServiceImpl;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author giahu
 */
public class ReportUtils {

    private static final Logger logger = Logger.getLogger(ScoreServiceImpl.class.getName());
    
    public static void compileLatexToPDF(String latexContent, String outputPath) throws IOException, InterruptedException {
        // Write LaTeX content to a temporary .tex file
        File tempFile = new File("temp.tex");
        try (FileWriter writer = new FileWriter(tempFile, StandardCharsets.UTF_8)) {
            writer.write(latexContent);
        }

        // Compile using xelatex
        ProcessBuilder pb = new ProcessBuilder("xelatex", "-output-directory", outputPath, tempFile.getAbsolutePath());
        pb.redirectErrorStream(true);
        Process process = pb.start();
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            logger.log(Level.SEVERE, "LaTeX compilation failed with exit code: {0}", exitCode);
            throw new RuntimeException("Failed to compile LaTeX to PDF");
        }

        // Clean up temporary .tex file
        tempFile.delete();
    }
}
