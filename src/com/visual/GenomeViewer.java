package com.visual;

import com.neat.*;
import com.test.print.GenomePrinter;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

class Window extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    ArrayList<JTextField> inputFields = new ArrayList<>();
    ArrayList<JTextField> outputFields = new ArrayList<>();
    Genome g;
    Calculator c;
    Window() {
        setName("GenomeViewer");
        setLayout(new FlowLayout());
        getContentPane().setBackground(Color.BLACK);

        JPanel toolPanel = new JPanel();
        toolPanel.setLayout(new FlowLayout());

        JToolBar buttonPanel = new JToolBar();
        buttonPanel.setLayout(new GridLayout(1, 0));

        JButton nodeBtn = new JButton("Mutate Node");
        buttonPanel.add(nodeBtn);

        JButton connBtn = new JButton("Mutate Connection");
        buttonPanel.add(connBtn);

        JButton calcBtn = new JButton("Calculate");
        buttonPanel.add(calcBtn);

        buttonPanel.setSize(1024, 128);

        toolPanel.add(buttonPanel);

        g = getGenome();
        c = new Calculator(g);

        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridLayout(0,2));


        for(int i = 0; i < Math.max(g.inputCount, g.outputCount); i++){

            if(g.inputCount > inputFields.size()){
                inputFields.add(new JTextField("Input"));
                dataPanel.add(inputFields.get(i));
            }
            else{
                dataPanel.add(new JLabel());
            }

            if(g.outputCount > outputFields.size()){
                outputFields.add(new JTextField("Output"));
                dataPanel.add(outputFields.get(i));
            }
            else{
                dataPanel.add(new JLabel());
            }
        }

        JScrollPane scrollPane = new JScrollPane(dataPanel);
        scrollPane.setPreferredSize(new Dimension(200, 70));

        toolPanel.add(scrollPane);

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

        calcBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Double[] input = inputFields.stream().map(i -> Double.parseDouble(i.getText()))
                                                     .collect(Collectors.toList())
                                                     .toArray(new Double[inputFields.size()]);
                double[] out = c.calculate(input);

                for(int i = 0; i < out.length; i++){
                    outputFields.get(i).setText(String.format("%.4f",out[i]));
                }
            }
        });
        add(imageLabel);
        add(toolPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1024, 512 + 128);

        setVisible(true);
    }

    Genome getGenome(){
        return new Genome(2, 5);
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