package com.example.nazanin.finalproject.optimization.genetic_algorithm;

import java.util.Random;

/**
 * Created by nazanin on 9/11/2020.
 */
public class RandomHelper {
    public static int getValue(int min,int max){
        Random r = new Random();
        int randomValue = min + r.nextInt()%(max - min);
        return randomValue;
    }

    public static Double getValue(Double min,Double max){
        Random r = new Random();
        double randomValue = min + (max - min) * r.nextDouble();
        return randomValue;
    }
}
