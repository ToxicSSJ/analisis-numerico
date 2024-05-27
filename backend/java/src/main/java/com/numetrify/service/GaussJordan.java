package com.numetrify.service;

import com.numetrify.dto.LUGaussianEliminationResponse;

public class GaussJordan {

    public static LUGaussianEliminationResponse luDecomposition(double[][] a, double[] b) {
        int n = a.length;
        double[][] L = new double[n][n];
        double[][] U = new double[n][n];

        // Initialize L with 1s in the diagonal
        for (int i = 0; i < n; i++) {
            L[i][i] = 1.0;
        }

        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                double sum1 = 0;
                for (int k = 0; k < i; k++) {
                    sum1 += L[i][k] * U[k][j];
                }
                U[i][j] = a[i][j] - sum1;
            }

            for (int j = i + 1; j < n; j++) {
                if (U[i][i] == 0) {
                    return new LUGaussianEliminationResponse("Error: División por cero.", null, null, null);
                }
                double sum2 = 0;
                for (int k = 0; k < i; k++) {
                    sum2 += L[j][k] * U[k][i];
                }
                L[j][i] = (a[j][i] - sum2) / U[i][i];
            }
        }

        double[] y = forwardSubstitution(L, b);
        double[] x = backwardSubstitution(U, y);

        return new LUGaussianEliminationResponse("Success", x, L, U);
    }

    private static double[] forwardSubstitution(double[][] L, double[] b) {
        int n = L.length;
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++) {
                sum += L[i][j] * y[j];
            }
            y[i] = (b[i] - sum) / L[i][i];
        }
        return y;
    }

    private static double[] backwardSubstitution(double[][] U, double[] y) {
        int n = U.length;
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += U[i][j] * x[j];
            }
            x[i] = (y[i] - sum) / U[i][i];
        }
        return x;
    }

    public static void main(String[] args) {
        // Ejemplo de uso
        double[][] A = {
                {3, -5, 20},
                {-30, 5, 2},
                {1, -5, 6}
        };
        double[] B = {40, 15, 6};

        LUGaussianEliminationResponse response = luDecomposition(A, B);

        if (response.getMessage().equals("Success")) {
            System.out.println("Matriz L:");
            for (double[] row : response.getL()) {
                for (double value : row) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
            System.out.println("Matriz U:");
            for (double[] row : response.getU()) {
                for (double value : row) {
                    System.out.print(value + " ");
                }
                System.out.println();
            }
            System.out.println("Solución del sistema:");
            for (double value : response.getSolution()) {
                System.out.print(value + " ");
            }
        } else {
            System.out.println(response.getMessage());
        }
    }
}