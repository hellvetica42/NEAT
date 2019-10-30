package com.neat;

import java.util.ArrayList;

public class Species{

    final double fitnessTreshold = 0.5;

    ArrayList<Genome> genomes = new ArrayList<>();

    public double getAdjustedFitness(Genome genome){
        int count = 0; //represents number of genomes in the same species as argument

        for(Genome g : genomes){
            if(g != genome){
               count += getCompatibilityDistance(genome, g) > fitnessTreshold ? 0 : 1;
            }
        }

        return genome.getFitness() / count;
    }

    public static double getCompatibilityDistance(Genome genome1, Genome genome2){
        double dis = 0;

        final double c1 = 1, c2 = 1, c3 = 1;

        int inno1 = genome1.getYougestConnectionGene();
        int inno2 = genome2.getYougestConnectionGene();

        int excessGenes = Math.abs(inno1 - inno2);

        int maxInnovation = Math.max(inno1, inno2);

        int matchingGenes = 0;

        double weightDif = 0;

        for(int i = 1; i <= maxInnovation; i++){
            if(genome1.getConnectionGenes().keySet().contains(i) &&
               genome2.getConnectionGenes().keySet().contains(i)){
                matchingGenes++;

                weightDif += Math.abs(genome1.getConnectionGenes().get(i).getWeight() -
                                      genome2.getConnectionGenes().get(i).getWeight());
            }
        }

        double avgWeightDif = weightDif/matchingGenes;

        int disjointGenes = maxInnovation - matchingGenes - excessGenes;

        dis = c1*excessGenes + c2*disjointGenes + c3*avgWeightDif;

        return dis;
    }
}