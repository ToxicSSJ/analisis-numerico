package com.numetrify.service;

import com.numetrify.dto.CholeskyResponse;

public class CholeskyTest {

    /*public static void main(String[] args) {
        double[][] A = {
                {4, 12, -16},
                {12, 37, -43},
                {-16, -43, 98}
        };
        double[] b = {1, 2, 3};

        double[][] A = {
                {4, 3, -2, 7},
                {3, 12, 8, -3},
                {2, 3, -9, 3},
                {1, -2, -5, 6}
        };
        double[] b = {20, 18, 31, 12};

        try {
            CholeskyResponse response = cholesky(A, b);
            double[] solution = response.getSolution();
            double[][] L = response.getL();
            double[][] U = response.getU();

            System.out.println("Solution:");
            for (double v : solution) {
                System.out.println(v);
            }

            System.out.println("\nL matrix:");
            for (double[] row : L) {
                for (double v : row) {
                    System.out.print(v + "\t");
                }
                System.out.println();
            }

            System.out.println("\nU matrix:");
            for (double[] row : U) {
                for (double v : row) {
                    System.out.print(v + "\t");
                }
                System.out.println();
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }*/

    /*public static CholeskyResponse cholesky(double[][] A, double[] b) {
        int n = A.length;
        double[][] L = new double[n][n];
        double[][] U = new double[n][n];

        // Initialize L and U matrices
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                L[i][j] = 0.0;
                U[i][j] = 0.0;
            }
        }

        // Perform Cholesky decomposition
        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            for (int k = 0; k < i; k++) {
                sum += L[i][k] * L[i][k];
            }
            double value = A[i][i] - sum;
            if (value < 0) {
                throw new IllegalArgumentException("Matrix is not positive definite");
            }
            L[i][i] = Math.sqrt(value);
            U[i][i] = L[i][i];

            for (int j = i + 1; j < n; j++) {
                sum = 0.0;
                for (int k = 0; k < i; k++) {
                    sum += L[j][k] * L[i][k];
                }
                L[j][i] = (A[j][i] - sum) / L[i][i];
                U[i][j] = L[j][i];
            }
        }

        // Forward substitution to solve Lz = b
        double[] z = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            for (int j = 0; j < i; j++) {
                sum += L[i][j] * z[j];
            }
            z[i] = (b[i] - sum) / L[i][i];
        }

        // Backward substitution to solve Ux = z
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += U[i][j] * x[j];
            }
            x[i] = (z[i] - sum) / U[i][i];
        }

        return new CholeskyResponse(x, L, U);
    }*/
}