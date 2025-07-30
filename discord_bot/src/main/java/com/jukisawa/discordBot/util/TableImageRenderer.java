package com.jukisawa.discordBot.util;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class TableImageRenderer {

    public static BufferedImage createTableImage(List<String> headers, List<List<String>> rows) {
        int rowHeight = 32;
        int headerHeight = 40;
        int fontSize = 16;
        int columns = headers.size();

        // Dummy image/graphics for measuring text width
        BufferedImage dummyImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dummyImg.createGraphics();
        g.setFont(new Font("Arial", Font.BOLD, fontSize));
        java.awt.FontMetrics fmHeader = g.getFontMetrics();
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        java.awt.FontMetrics fmCell = g.getFontMetrics();

        int[] colWidths = new int[columns];
        // Initial width: header width
        for (int i = 0; i < columns; i++) {
            colWidths[i] = fmHeader.stringWidth(headers.get(i)) + 20;
        }
        // Check cell content width
        for (int row = 0; row < rows.size(); row++) {
            List<String> rowData = rows.get(row);
            for (int col = 0; col < columns; col++) {
                String text = rowData.get(col);
                int w = fmCell.stringWidth(text) + 20;
                if (w > colWidths[col])
                    colWidths[col] = w;
            }
        }
        g.dispose();

        int width = 0;
        for (int w : colWidths)
            width += w;
        int height = headerHeight + rows.size() * rowHeight + 20;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        // Hintergrund
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, width, height);

        // Tabellenlinien
        g2.setColor(Color.LIGHT_GRAY);
        int x = 0;
        for (int i = 0; i <= columns; i++) {
            g2.drawLine(x, 0, x, height);
            if (i < columns)
                x += colWidths[i];
        }
        for (int i = 0; i <= rows.size() + 1; i++) {
            int y = headerHeight + i * rowHeight;
            g2.drawLine(0, y, width, y);
        }

        // Schrift
        g2.setFont(new Font("Arial", Font.BOLD, fontSize));
        g2.setColor(Color.BLACK);

        // Header
        x = 0;
        for (int i = 0; i < columns; i++) {
            String text = headers.get(i);
            drawCenteredString(g2, text, x, 0, colWidths[i], headerHeight);
            x += colWidths[i];
        }

        // Zeilen
        g2.setFont(new Font("Arial", Font.PLAIN, fontSize));
        for (int row = 0; row < rows.size(); row++) {
            List<String> rowData = rows.get(row);
            x = 0;
            for (int col = 0; col < columns; col++) {
                String text = rowData.get(col);
                drawCenteredString(g2, text, x, headerHeight + row * rowHeight, colWidths[col], rowHeight);
                x += colWidths[col];
            }
        }

        g2.dispose();
        return image;
    }

    private static void drawCenteredString(Graphics2D g, String text, int x, int y, int w, int h) {
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getAscent();
        int tx = x + (w - textWidth) / 2;
        int ty = y + (h + textHeight) / 2 - 4;
        g.drawString(text, tx, ty);
    }
}
