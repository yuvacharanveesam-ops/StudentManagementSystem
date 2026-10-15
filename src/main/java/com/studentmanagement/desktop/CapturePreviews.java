package com.studentmanagement.desktop;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class CapturePreviews {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        File outDir = new File("docs/screenshots");
        outDir.mkdirs();

        // 1. Capture LoginFrame
        LoginFrame login = new LoginFrame();
        login.pack();
        login.setSize(450, 380);
        login.doLayout();
        captureComponent(login.getContentPane(), new File(outDir, "login_screen.png"));
        login.dispose();

        // 2. Capture DashboardFrame
        DashboardFrame dashboard = new DashboardFrame("admin");
        dashboard.pack();
        dashboard.setSize(1050, 660);
        dashboard.doLayout();
        captureComponent(dashboard.getContentPane(), new File(outDir, "dashboard_screen.png"));

        // 3. Capture StudentFormDialog
        StudentFormDialog form = new StudentFormDialog(dashboard, null);
        form.pack();
        form.setSize(480, 420);
        form.doLayout();
        captureComponent(form.getContentPane(), new File(outDir, "form_screen.png"));
        form.dispose();

        dashboard.dispose();

        System.out.println("All screenshots captured successfully!");
    }

    private static void captureComponent(Component comp, File file) throws Exception {
        BufferedImage img = new BufferedImage(Math.max(1, comp.getWidth()), Math.max(1, comp.getHeight()), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        comp.paint(g2d);
        g2d.dispose();
        ImageIO.write(img, "png", file);
    }
}
