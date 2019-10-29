package com.test.print;

import java.util.Random;

import com.neat.Genome;
import com.neat.TYPE;

/**
 *
 * @author Petar
 */
public class GenomePrintTest {

    Random random = new Random();
   
    public static void main(String[] args) {
        System.out.println("Starting printer");
        Genome genome = new Genome(3, 1);         

        for(int i = 1; i < 4; i++){
                genome.addNodeGene(TYPE.INPUT);
        }


        genome.addNodeGene(TYPE.OUTPUT);

        genome.addNodeGene();

        genome.addConnectionGene(genome.getNodeGenes().get(1), genome.getNodeGenes().get(4));
        genome.addConnectionGene(genome.getNodeGenes().get(2), genome.getNodeGenes().get(4));
        genome.addConnectionGene(genome.getNodeGenes().get(3), genome.getNodeGenes().get(4));
        genome.addConnectionGene(genome.getNodeGenes().get(2), genome.getNodeGenes().get(5));
        genome.addConnectionGene(genome.getNodeGenes().get(5), genome.getNodeGenes().get(4));
        genome.addConnectionGene(genome.getNodeGenes().get(1), genome.getNodeGenes().get(5));

        GenomePrinter.printGenome(genome, "./before.png");
        
        System.out.println(genome.isFullyConnected());

        genome.addConnectionMutation();

        System.out.println(genome.isFullyConnected());

        genome.addConnectionMutation();

        GenomePrinter.printGenome(genome, "./after.png");
    }
    
}