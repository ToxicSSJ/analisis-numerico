package com.numetrify.service;

import com.numetrify.dto.SimpleGaussianEliminationResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

/**
 * Service class to perform Gaussian Elimination on a matrix.
 */
@Service
public class SimpleGaussianEliminationService {

    @SneakyThrows
    public SimpleGaussianEliminationResponse simpleGaussianElimination(double[][] matrixA, double[] vectorB) {
        int n = matrixA.length;
        double[][] M = new double[n][n + 1];

        // Crear la matriz aumentada
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrixA[i], 0, M[i], 0, n);
            M[i][n] = vectorB[i];
        }

        // Eliminación Gaussiana
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (M[j][i] != 0) {
                    double factor = M[j][i] / M[i][i];
                    for (int k = i; k <= n; k++) {
                        M[j][k] -= factor * M[i][k];
                    }
                }
            }
        }

        // Sustitución regresiva
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += M[i][j] * x[j];
            }
            x[i] = (M[i][n] - sum) / M[i][i];
        }

        return new SimpleGaussianEliminationResponse(x);
    }
}