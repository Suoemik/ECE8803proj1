package com.example.suoemi.ece8803proj;

import com.mathworks.engine.MatlabEngine;

/**
 * Created by Suoemi on 3/31/2017.
 */

public class javaFevalFunc {
    public static void main(String[] args) throws Exception {
        MatlabEngine eng = MatlabEngine.startMatlab();
        int[] x = {0, 0};
        int[] A = {1, 1};
        Object[] results = eng.feval("fmincon", "@(x)10*(x(1) + x(2))", x, A, 5);
        Integer G = (Integer)results[0];
        eng.close();
    }
}
