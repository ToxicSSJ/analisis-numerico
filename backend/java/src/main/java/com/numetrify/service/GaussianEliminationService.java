package com.numetrify.service;

import com.numetrify.dto.GaussianEliminationResponse;
import org.springframework.stereotype.Service;

/**
 * Service class to perform Gaussian Elimination on a matrix.
 */
@Service
public class GaussianEliminationService {

    /**
     * Performs Gaussian Elimination on the given matrix A and vector B to solve the system of equations Ax = B.
     *
     * @param matrixA the matrix A in the system of equations
     * @param vectorB the vector B in the system of equations
     * @return GaussianEliminationResponse containing the solution vector x
     *
     * Example usage:
     * <pre>
     * {@code
     * double[][] matrixA = {
     *     {2, -1, -2},
     *     {-4, 6, 3},
     *     {-4, -2, 8}
     * };
     * double[] vectorB = {1, 2, 3};
     * GaussianEliminationResponse response = gaussianEliminationService.gaussianElimination(matrixA, vectorB);
     * double[] solution = response.getSolution();
     * }
     * </pre>
     */
    public GaussianEliminationResponse gaussianElimination(double[][] matrixA, double[] vectorB) {
        int n = matrixA.length;

        // Initialize augmented matrix
        double[][] augmentedMatrix = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmentedMatrix[i][j] = matrixA[i][j];
            }
            augmentedMatrix[i][n] = vectorB[i];
        }

        // Perform Gaussian elimination with partial pivoting
        for (int i = 0; i < n - 1; i++) {
            // Pivoting
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(augmentedMatrix[k][i]) > Math.abs(augmentedMatrix[maxRow][i])) {
                    maxRow = k;
                }
            }

            if (maxRow != i) {
                double[] temp = augmentedMatrix[i];
                augmentedMatrix[i] = augmentedMatrix[maxRow];
                augmentedMatrix[maxRow] = temp;
            }

            // Elimination
            for (int j = i + 1; j < n; j++) {
                if (augmentedMatrix[j][i] != 0) {
                    double factor = augmentedMatrix[j][i] / augmentedMatrix[i][i];
                    for (int k = i; k < n + 1; k++) {
                        augmentedMatrix[j][k] -= factor * augmentedMatrix[i][k];
                    }
                }
            }
        }

        // Back substitution
        double[] solution = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            solution[i] = augmentedMatrix[i][n] / augmentedMatrix[i][i];
            for (int j = i - 1; j >= 0; j--) {
                augmentedMatrix[j][n] -= augmentedMatrix[j][i] * solution[i];
            }
        }

        return new GaussianEliminationResponse(solution);
    }
}