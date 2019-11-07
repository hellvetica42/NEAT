package com.neat;

import java.util.Random;

/**
 *
 * @author Petar
 */
public class ConnectionGene {
    int from, to;
    
    double weight;
    boolean enable = true;
    final int innovation;
    Random random;
    
    public ConnectionGene(int from, int to, int innovation, double weight, boolean enable, Random random) {
        this(from, to, innovation, weight, random);
        this.enable = enable;
    }

    public ConnectionGene(int from, int to, int innovation, double weight, Random random) {
        this.from = from;
        this.to = to;
        this.innovation = innovation;
        this.weight = weight;
        this.random = random;
    }
    public ConnectionGene(int from, int to, int innovation, Random random) {
        this(from, to, innovation, random.nextDouble(), random);
    }
    
    void disable(){
        enable = false;
    }
    
    void enable(){
        enable = true;
    }

    public boolean isEnabled(){
        return enable;
    }

    public ConnectionGene copy() {
        return new ConnectionGene(from, to, innovation, weight, enable, random);
    }
    
   public int getFromNodeId(){
       return from;
   } 

   public int getToNodeId(){
       return to;
   }

   public double getWeight(){
       return weight;
   }
    
}
