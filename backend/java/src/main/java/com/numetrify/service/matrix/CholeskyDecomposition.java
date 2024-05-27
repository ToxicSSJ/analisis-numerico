package com.numetrify.service.matrix;

public class CholeskyDecomposition {

    private int n;
    private double[][] L;

    public CholeskyDecomposition(double[][] A) {
        if (A.length != A[0].length) {
            throw new IllegalArgumentException("Matrix A must be square");
        }
        n = A.length;
        L = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                double sum = 0.0;
                for (int k = 0; k < j; k++) {
                    sum += L[i][k] * L[j][k];
                }
                if (i == j) {
                    if (A[i][i] - sum <= 0) {
                        throw new RuntimeException("Matrix is not positive definite");
                    }
                    L[i][j] = Math.sqrt(A[i][i] - sum);
                } else {
                    L[i][j] = (A[i][j] - sum) / L[j][j];
                }
            }
        }
    }

    public double[] solve(double[] b) {
        if (b.length != n) {
            throw new IllegalArgumentException("Vector b length must be equal to the number of rows in matrix A");
        }
        double[] y = new double[n];
        double[] x = new double[n];

        // Solve L*y = b
        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            for (int k = 0; k < i; k++) {
                sum += L[i][k] * y[k];
            }
            y[i] = (b[i] - sum) / L[i][i];
        }

        // Solve L^T*x = y
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int k = i + 1; k < n; k++) {
                sum += L[k][i] * x[k];
            }
            x[i] = (y[i] - sum) / L[i][i];
        }

        return x;
    }

    public double[][] getL() {
        return L;
    }

    public double[][] getU() {
        double[][] U = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i > j) {
                    U[i][j] = 0.0;
                } else if (i == j) {
                    U[i][j] = L[i][j];
                } else {
                    U[i][j] = L[j][i];
                }
            }
        }
        return U;
    }
}