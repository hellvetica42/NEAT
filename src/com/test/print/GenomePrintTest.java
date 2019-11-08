package com.test.print;

import java.util.Random;

import com.neat.Genome;

/**
 *
 * @author Petar
 */
public class GenomePrintTest {

    Random random = new Random();
   
    public static void main(String[] args) {
        System.out.println("Starting printer");
        Genome genome = new Genome(3, 1);         


        GenomePrinter.printGenome(genome, "./src/com/test/print/before.png");
        
        for(int i = 0; i < 30; i++){
            if(new Random().nextBoolean()){
                genome.addConnectionMutation();
            }
            else{
                genome.addNodeMutation();
            }
        }

        GenomePrinter.printGenome(genome, "./src/com/test/print/after.png");
    }
    
}