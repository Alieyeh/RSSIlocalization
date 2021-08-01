package com.example.nazanin.finalproject.optimization.genetic_algorithm;



import com.mapbox.geojson.Point;

import java.util.*;

/**
 * Created by nazanin on 4/30/2020.
 */
public class Chromosome {
    private ArrayList<Double> genes;
    private boolean isFitnessChanged = true;
    private double fitness = -1;
    private double xRange,yRange;
    Map<Point,Double> anchors;


    public Chromosome(Map<Point,Double> anchors,double xRange, double yRange) {
        genes = new ArrayList<>();
        this.anchors = anchors;
        this.xRange = xRange;
        this.yRange = yRange;
    }

    // ch {P0,beta,x,y}
    public Chromosome createInitialPop(){
        double P0 = RandomHelper.getValue(-30,-120);
        double beta = RandomHelper.getValue(1.6,6.0);
        double x = RandomHelper.getValue(xRange-1,xRange+1);
        double y = RandomHelper.getValue(yRange-1,yRange+1);
        genes.add(P0);genes.add(beta);genes.add(x);genes.add(y);
        return this;
    }


    public ArrayList<Double> getGenes() {
        isFitnessChanged = true;
        return genes;
    }

    public double getFitness() {
        if (isFitnessChanged == true){
            fitness=calculateFitness();
            isFitnessChanged=false;
        }
        return fitness;
    }
    private double calculateFitness(){
        double exp = 0;
        for (Map.Entry<Point, Double> entry : anchors.entrySet()) {
            double xi = entry.getKey().latitude();
            double yi = entry.getKey().longitude();
                    // (pi       -   p0       +  10*beta*log10(radical(xi-xt)2+(yi-yt)2)2
            exp += Math.pow(entry.getValue()-(genes.get(0)+(10*genes.get(1)*Math.log10(
                        Math.sqrt(Math.pow(xi-genes.get(2),2)+Math.pow(yi-genes.get(3),2))))),2);

        }
        return exp;
    }



}
