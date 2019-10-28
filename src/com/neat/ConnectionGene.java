package com.neat;

import java.util.Random;

/**
 *
 * @author Petar
 */
public class ConnectionGene {
    NodeGene from, to;
    
    double weight;
    boolean enable = true;
    final int innovation;
    Random random;
    
    public ConnectionGene(NodeGene from, NodeGene to, int innovation, double weight, Random random) {
        this.from = from;
        this.to = to;
        this.innovation = innovation;
        this.weight = weight;
        this.random = random;
        from.connectOutput(this);
        to.connectInput(this);
    }
    public ConnectionGene(NodeGene from, NodeGene to, int innovation, Random random) {
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
        return new ConnectionGene(from, to, innovation, random);
    }
    
   public NodeGene getFromNode(){
       return from;
   } 

   public NodeGene getToNode(){
       return to;
   }

   public double getWeight(){
       return weight;
   }
    
}
