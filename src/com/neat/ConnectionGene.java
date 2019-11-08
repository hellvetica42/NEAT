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

    boolean activated = false;
    double activation = 0;
    
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
        this(from, to, innovation, random.nextDouble()*2 - 1, random);
    }
    
    void setEnable(boolean e){
        this.enable = e;
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

   public double getActivation(){
        if(this.activated)
            return this.activation;
        else
            return 0;
   }

   public boolean isActivated(){
       return activated;
   }

   public void activate(double value){
        this.activation = value*weight;
        this.activated = true;
   }

   public void reset(){
       this.activated = false;
       this.activation = 0;
   }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    
}
