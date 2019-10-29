package com.test.crossover;

import com.test.print.GenomePrinter;
import java.util.Random;
import com.neat.*;


public class CrossoverTest{
    
    Random random = new Random();

    public static void main(String[] args) {
       
        Genome parent1 = new Genome();

        for(int i = 1; i < 4; i++){
            parent1.addNodeGene(TYPE.INPUT);
        }

        parent1.addNodeGene(TYPE.OUTPUT);

        parent1.addNodeGene();

        parent1.addConnectionGene(parent1.getNodeGenes().get(1), parent1.getNodeGenes().get(4), 1, true);
        parent1.addConnectionGene(parent1.getNodeGenes().get(2), parent1.getNodeGenes().get(4), 2, false);
        parent1.addConnectionGene(parent1.getNodeGenes().get(3), parent1.getNodeGenes().get(4), 3, true);
        parent1.addConnectionGene(parent1.getNodeGenes().get(2), parent1.getNodeGenes().get(5), 4, true);
        parent1.addConnectionGene(parent1.getNodeGenes().get(5), parent1.getNodeGenes().get(4), 5, true);
        parent1.addConnectionGene(parent1.getNodeGenes().get(1), parent1.getNodeGenes().get(5), 8, true);


        Genome parent2 = new Genome();

        for(int i = 1; i < 4; i++){
            parent2.addNodeGene(TYPE.INPUT);
        }

        parent2.addNodeGene(TYPE.OUTPUT);

        parent2.addNodeGene();
        parent2.addNodeGene();

        parent2.addConnectionGene(parent2.getNodeGenes().get(1), parent2.getNodeGenes().get(4), 1, true);

        parent2.addConnectionGene(parent2.getNodeGenes().get(2), parent2.getNodeGenes().get(4), 2, false);

        parent2.addConnectionGene(parent2.getNodeGenes().get(3), parent2.getNodeGenes().get(4), 3, true);
        parent2.addConnectionGene(parent2.getNodeGenes().get(2), parent2.getNodeGenes().get(5), 4, true);

        parent2.addConnectionGene(parent2.getNodeGenes().get(5), parent2.getNodeGenes().get(4), 5, false);

        parent2.addConnectionGene(parent2.getNodeGenes().get(5), parent2.getNodeGenes().get(6), 6, true);
        parent2.addConnectionGene(parent2.getNodeGenes().get(6), parent2.getNodeGenes().get(4), 7, true);
        parent2.addConnectionGene(parent2.getNodeGenes().get(3), parent2.getNodeGenes().get(5), 9, true);
        parent2.addConnectionGene(parent2.getNodeGenes().get(1), parent2.getNodeGenes().get(6), 10, true);


        GenomePrinter.printGenome(parent1, "./src/com/test/crossover/parent1.png");
        GenomePrinter.printGenome(parent2, "./src/com/test/crossover/parent2.png");

        Genome child = Genome.Crossover(parent1, parent2, new Random());

        GenomePrinter.printGenome(child, "./src/com/test/crossover/child.png");
    }
}