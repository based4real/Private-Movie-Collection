package pmc.gui.utils;


import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.function.Consumer;

public class FileManagementService {
    private static long lastReportedProgress = 0;

    public static void copyFileToDir(File sourceFile, String destDir) throws IOException {
        File destFile = new File(destDir, sourceFile.getName());

        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdir();

        Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void copyFileToDir(File sourceFile, String destDir, Consumer<Double> onProgressUpdate) throws IOException {
        lastReportedProgress = 0;
        File destFile = new File(destDir, sourceFile.getName());

        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdir();

        long totalSize = sourceFile.length();
        long totalCopied = 0;

        try (InputStream in = new FileInputStream(sourceFile);
             OutputStream out = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
                totalCopied += bytesRead;
                double progress = (double) totalCopied / totalSize;

                if (shouldUpdateProgress(totalCopied, totalSize)) { // throttle progress opdatering
                    onProgressUpdate.accept(progress);
                }
            }
        }
    }

    private static boolean shouldUpdateProgress(long totalCopied, long totalSize) {
        long threshold = totalSize / 100; // opdater for hver ændring på 1%
        long currentProgress = (totalCopied / threshold) * threshold;

        if (currentProgress > lastReportedProgress) {
            lastReportedProgress = currentProgress;
            return true;
        }
        return false;
    }

    public static void deleteFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists() && !file.delete()) {
            throw new IOException("Kunne ikke slette filen: " + filePath);
        }
    }

    public static void downloadImageToDir(String imageUrl, String destDir, String imageName, int width, int height) throws IOException {
        try (InputStream imageStream = URI.create(imageUrl).toURL().openStream()) {
            BufferedImage originalImage = ImageIO.read(imageStream);
            BufferedImage resizedImage = resizeImage(originalImage, width, height);

            File dir = new File(destDir);
            if (!dir.exists()) dir.mkdirs();

            File outputFile = new File(dir, imageName);
            String fileExtension = getFileExtension(imageUrl);
            ImageIO.write(resizedImage, fileExtension, outputFile);
        }
    }

    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();

        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();

        return resizedImage;
    }

    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "jpg"; // standard extension hvis vi ikke kan finde en
    }
}