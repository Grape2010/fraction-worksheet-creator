package com.elementaryengineers.fwc.custom;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * This class allows the easy creation of a JButton
 * that uses an image as a button, rather than
 * the default Java style button with text.
 **/
public class ImageButton extends JButton {

    public ImageButton(String filename, int width, int height) {
        this.setText(null);

        // Get icon of image pointed to by filename

        try {
            URL imgURL = ImageButton.class.getClassLoader().getResource("images/" + filename);
            BufferedImage imgBuff = ImageIO.read(imgURL);

            if (imgURL == null)
                System.out.println("Could not load " + filename + ".");
            else
                this.setIcon(new ImageIcon(imgBuff.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        this.setFocusPainted(false);
        this.setRolloverEnabled(false);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLUE.darker(), Color.BLACK),
                BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)));
    }
}