package com.test.population;

import java.util.Random;
import com.neat.*;

public class PopulationTest{
    static Random random;
    public static void main(String[] args) {
        System.out.println("Population Test running ....");

        random = new Random();

        Genome sample = new Genome(3, 4);
        Population population = new Population(sample, 64, random);
        
        PopulationPrinter.printGenomes(population.getGenomes());


    }

}