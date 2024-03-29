package com.neat;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

public class Population{
    Random random;

    ArrayList<Genome> population = new ArrayList<>();
    ArrayList<Species> species = new ArrayList<>();
    Map<Genome, Species> speciesMap = new HashMap<>();

    public Population(Genome sample, int size, Random random){
        this.random = random;
        for(int i = 0; i < size; i++){
            population.add(sample.copy());
        }
    }

    Population(Random random){
        this.random = random;
    }


    double getAdjustedFitness(Genome g){
        return g.getFitness() / speciesMap.get(g).getSize();
    }

    ArrayList<Species> getSpecies(){
        return species;
    }

    public int getSize(){
        return population.size();
    }

    Species createNewSpecies(){
        Species s = new Species(random);
        species.add(s);
        return s;
    }

    void killSpecies(Species s){
        if(species.contains(s)){
            species.remove(s);
        }
    }

    void killWorstGenomes(double treshold){
        for(Species s : species){
            s.killWorstGenomes(treshold);
        }
    }

    Species getRandomSpecies(){
        return species.get(random.nextInt(species.size()));
    }
    
    void speciate(){
        species.clear();
        speciesMap.clear();
       for(Genome g : population){
            boolean foundSpecies = false;
            for(Species s : species){
                if(Evaluator.belongsInSpecies(g, s)){
                    foundSpecies = true;
                    s.addGenome(g);
                    speciesMap.put(g, s);
                    break;
                }
            }
            
            if(!foundSpecies){
                Species s = createNewSpecies();
                s.addGenome(g);
                speciesMap.put(g, s);
            }
       }
    }

    public ArrayList<Genome> getGenomes(){
        return population;
    }
}