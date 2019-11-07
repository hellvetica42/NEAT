package com.test.species;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

import com.neat.Genome;
import com.neat.Species;

public class SpeciesTest{

    static Random random;
    public static void main(String[] args) {
        System.out.println("Species Test Running...");
        random = new Random();

        Species species = new Species(random);

        for(int i = 0; i < 20; i++){
            Genome g = new Genome();
            g.setFitness(random.nextDouble() * 100);
            species.addGenome(g);
        }

        System.out.println("Unadjusted unsorted fitness:");
        for(Genome g : species.getGenomes()){
            System.out.printf("%.3f ", g.getFitness());
        }
        System.out.println("//////");

        System.out.println("Adjusted unsorted fitness:");
        for(Genome g : species.getGenomes()){
            System.out.printf("%.3f ", g.getFitness()/species.getSize());
        }
        System.out.println("//////");

        Collections.sort(species.getGenomes());

        System.out.println("Adjusted sorted fitness:");
        for(Genome g : species.getGenomes()){
            System.out.printf("%.3f ", g.getFitness()/species.getSize());
        }
        System.out.println("//////");

        species.killWorstGenomes(0.5); 

        System.out.println("Killed bottom half of species:");
        for(Genome g : species.getGenomes()){
            System.out.printf("%.3f ", g.getFitness()/species.getSize());
        }
        System.out.println("//////");
    }
}