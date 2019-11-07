package com.test.population;

import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.test.print.GenomePrinter;
import com.neat.*;
import java.util.ArrayList;

public class PopulationPrinter{

    static final int imgSize = 2048;
    static final int segmentSize = 256;
    static final int perRow = imgSize/segmentSize;

    static void printGenomes(ArrayList<Genome> genomes){
        BufferedImage image = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);

        int xcount = 0;
        int ycount = 0;
        for(Genome g : genomes){
            BufferedImage before = GenomePrinter.getImage(g);
            BufferedImage after = new BufferedImage(before.getWidth(), before.getHeight(), BufferedImage.TYPE_INT_ARGB);
            AffineTransform at = new AffineTransform();
            double scalingFactor = (double)segmentSize/(double)before.getWidth();
            at.scale(scalingFactor, scalingFactor);
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);

            after = scaleOp.filter(before, after);

            image.getGraphics().drawImage(after, (xcount%perRow)*segmentSize, ycount*segmentSize, null);
            
            xcount++;
            ycount = xcount/perRow;
        }

        try {
            ImageIO.write(image, "PNG", new File("./src/com/test/population/population.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}