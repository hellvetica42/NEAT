package com.neat;

import java.util.ArrayList;
import java.util.Random;

public class Species{

    ArrayList<Genome> genomes = new ArrayList<>();
    Genome mascot;

    double speciesFitness = 0;
    Random random;

    Species(Random random){
       this.random = random;
    }
    
    void updateFitness(){
        speciesFitness = 0;
        for(Genome g : genomes){
           speciesFitness += g.getFitness();
        }
        speciesFitness /= genomes.size();
    }

    double getFitness(){
        return getFitness(false);
    }

    double getFitness(boolean update){
        if(update)
            updateFitness();

        return speciesFitness;
    }

    int getSize(){
        return genomes.size();
    }

    ArrayList<Genome> getGenomes(){
        return genomes;
    }

    void addGenome(Genome g){
        genomes.add(g);
    }

    Genome getMascot(){
        if(mascot == null){
            pickMascot();
        }
        return mascot;
    }

    void pickMascot(){
       mascot = genomes.get(random.nextInt(genomes.size()));
    }
}