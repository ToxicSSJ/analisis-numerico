package com.numetrify.service;

import com.numetrify.dto.PivotGaussianEliminationResponse;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

/**
 * Service class to perform Pivot Gaussian Elimination on a matrix.
 */
@Service
public class PivotGaussianEliminationService {

    @SneakyThrows
    public PivotGaussianEliminationResponse pivotGaussianElimination(double[][] matrixA, double[] vectorB) {
        int n = matrixA.length;

        double[][] augmentedMatrix = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmentedMatrix[i][j] = matrixA[i][j];
            }
            augmentedMatrix[i][n] = vectorB[i];
        }

        for (int i = 0; i < n; i++) {
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(augmentedMatrix[k][i]) > Math.abs(augmentedMatrix[maxRow][i])) {
                    maxRow = k;
                }
            }

            double[] temp = augmentedMatrix[i];
            augmentedMatrix[i] = augmentedMatrix[maxRow];
            augmentedMatrix[maxRow] = temp;

            if (augmentedMatrix[i][i] == 0) {
                return new PivotGaussianEliminationResponse("No unique solution exists", null);
            }

            double pivot = augmentedMatrix[i][i];
            for (int j = 0; j <= n; j++) {
                augmentedMatrix[i][j] /= pivot;
            }

            for (int j = 0; j < n; j++) {
                if (j != i) {
                    double factor = augmentedMatrix[j][i];
                    for (int k = 0; k <= n; k++) {
                        augmentedMatrix[j][k] -= factor * augmentedMatrix[i][k];
                    }
                }
            }
        }

        double[] solution = new double[n];
        for (int i = 0; i < n; i++) {
            solution[i] = augmentedMatrix[i][n];
        }

        return new PivotGaussianEliminationResponse("Success", solution);
    }
}