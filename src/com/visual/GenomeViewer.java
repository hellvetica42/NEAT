package com.visual;

import com.neat.*;
import com.test.print.GenomePrinter;
import javax.swing.*;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

class Window extends JFrame {
    Window() {
        setLayout(new FlowLayout());
        getContentPane().setBackground(Color.BLACK);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 0));

        JButton nodeBtn = new JButton("Mutate Node");
        buttonPanel.add(nodeBtn);

        JButton connBtn = new JButton("Mutate Connection");
        buttonPanel.add(connBtn);

        JButton resetBtn = new JButton("Reset");
        buttonPanel.add(resetBtn);

        buttonPanel.setSize(1024, 128);

        Genome g = new Genome(3, 4);
        JLabel imageLabel = new JLabel(new ImageIcon(GenomePrinter.getImage(g)));

        nodeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                g.addNodeMutation();
                imageLabel.setIcon(new ImageIcon(GenomePrinter.getImage(g)));
            }
        });

        connBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                g.addConnectionMutation();
                imageLabel.setIcon(new ImageIcon(GenomePrinter.getImage(g)));
            }
        });

        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                imageLabel.setIcon(new ImageIcon(GenomePrinter.getImage(g)));
            }
        });
        add(imageLabel);
        add(buttonPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1024, 512 + 128);

        setVisible(true);
    }
}

public class GenomeViewer{

    public static void main(String[] args){

        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                new Window();
            }
        });
    }

}