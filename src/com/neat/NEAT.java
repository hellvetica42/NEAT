package com.neat;

import java.util.Random;

/**
 *
 * @author Petar
 */
public class NEAT {

    public static final double PROBABILITY_MUTATE_CONNECTION = 0.01;
    public static final double PROBABILITY_MUTATE_NODE = 0.1;
    public static final double PROBABILITY_MUTATE_WEIGHT_SHIFT = 0.02; 
    public static final double PROBABILITY_MUTATE_WEIGHT_RANDOM = 0.02;
    public static final double PROBABILITY_MUTATE_TOGGLE_CONNECTION = 0;

    public static final double WEIGHT_RANDOM_STRENGTH = 0.3;
    public static final double WEIGHT_SHIFT_STRENGTH = 1;

    Random random = new Random();
   
    public static void main(String[] args) {
        System.out.println("NEAT running");
    }
    
}
