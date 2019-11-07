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

        parent1.addConnectionGene(parent1.getNodeGenes().get(1).getId(), parent1.getNodeGenes().get(4).getId(), 1, true);
        parent1.addConnectionGene(parent1.getNodeGenes().get(2).getId(), parent1.getNodeGenes().get(4).getId(), 2, false);
        parent1.addConnectionGene(parent1.getNodeGenes().get(3).getId(), parent1.getNodeGenes().get(4).getId(), 3, true);
        parent1.addConnectionGene(parent1.getNodeGenes().get(2).getId(), parent1.getNodeGenes().get(5).getId(), 4, true);
        parent1.addConnectionGene(parent1.getNodeGenes().get(5).getId(), parent1.getNodeGenes().get(4).getId(), 5, true);
        parent1.addConnectionGene(parent1.getNodeGenes().get(1).getId(), parent1.getNodeGenes().get(5).getId(), 8, true);


        Genome parent2 = new Genome();

        for(int i = 1; i < 4; i++){
            parent2.addNodeGene(TYPE.INPUT);
        }

        parent2.addNodeGene(TYPE.OUTPUT);

        parent2.addNodeGene();
        parent2.addNodeGene();

        parent2.addConnectionGene(parent2.getNodeGenes().get(1).getId(), parent2.getNodeGenes().get(4).getId(), 1, true);

        parent2.addConnectionGene(parent2.getNodeGenes().get(2).getId(), parent2.getNodeGenes().get(4).getId(), 2, false);

        parent2.addConnectionGene(parent2.getNodeGenes().get(3).getId(), parent2.getNodeGenes().get(4).getId(), 3, true);
        parent2.addConnectionGene(parent2.getNodeGenes().get(2).getId(), parent2.getNodeGenes().get(5).getId(), 4, true);

        parent2.addConnectionGene(parent2.getNodeGenes().get(5).getId(), parent2.getNodeGenes().get(4).getId(), 5, false);

        parent2.addConnectionGene(parent2.getNodeGenes().get(5).getId(), parent2.getNodeGenes().get(6).getId(), 6, true);
        parent2.addConnectionGene(parent2.getNodeGenes().get(6).getId(), parent2.getNodeGenes().get(4).getId(), 7, true);
        parent2.addConnectionGene(parent2.getNodeGenes().get(3).getId(), parent2.getNodeGenes().get(5).getId(), 9, true);
        parent2.addConnectionGene(parent2.getNodeGenes().get(1).getId(), parent2.getNodeGenes().get(6).getId(), 10, true);


        GenomePrinter.printGenome(parent1, "./src/com/test/crossover/parent1.png");
        GenomePrinter.printGenome(parent2, "./src/com/test/crossover/parent2.png");

        Genome child = Genome.Crossover(parent1, parent2, new Random());

        GenomePrinter.printGenome(child, "./src/com/test/crossover/child.png");
    }
}