package com.example.nazanin.finalproject.optimization.genetic_algorithm;

import android.os.Build;


import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nazanin on 4/30/2020.
 */
public class Population {
    private ArrayList<Chromosome> chromosomes;

    public Population(int size,Map<Point,Double> anchors, double xRange,double yRange) {
        chromosomes = new ArrayList<Chromosome>(size);
        for (int i=0;i<size;i++){
            chromosomes.add(new Chromosome(anchors,xRange,yRange).createInitialPop());
        }
    }

    public ArrayList<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public List<Double> getFittest(){
        Chromosome fittest = chromosomes.get(0);
        return fittest.getGenes();
    }

    public Population sortByFitness(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            chromosomes.sort((chromosome1, chromosome2) ->{
                int returnValue = 0;
                if (chromosome1.getFitness()< chromosome2.getFitness()) returnValue = -1;
                else if (chromosome1.getFitness() > chromosome2.getFitness()) returnValue = 1;
                return returnValue;

            });
        }
        return this;
    }
}
