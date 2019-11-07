package com.neat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Species{

    ArrayList<Genome> genomes = new ArrayList<>();
    Genome mascot;

    double speciesFitness = 0;
    Random random;
 
    public Species(Random random){
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

    public int getSize(){
        return genomes.size();
    }

    public ArrayList<Genome> getGenomes(){
        return genomes;
    }

    public void addGenome(Genome g){
        genomes.add(g);
    }

    public void killWorstGenomes(double percentage){
        int genomesToKill = (int)Math.floor((double)genomes.size()*percentage);
        Collections.sort(genomes);
        
        for(int i = 0; i < genomesToKill; i++){
            genomes.remove(0);
        }

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

    Genome getRandomGenome(){
        return genomes.get(random.nextInt(genomes.size()));
    }
}