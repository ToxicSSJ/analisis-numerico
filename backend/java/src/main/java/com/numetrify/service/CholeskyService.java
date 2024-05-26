package com.numetrify.service;

import com.numetrify.dto.CholeskyResponse;
import org.springframework.stereotype.Service;

/**
 * Service class to perform Cholesky Decomposition on a matrix.
 */
@Service
public class CholeskyService {

    /**
     * Performs Cholesky Decomposition on the given matrix A and solves the system of equations Ax = b.
     *
     * @param A the matrix to decompose
     * @param b the vector b in the system of equations Ax = b
     * @return CholeskyResponse containing the solution vector x, and the L and U matrices
     *
     * Example usage:
     * <pre>
     * {@code
     * double[][] A = {
     *     {4, 12, -16},
     *     {12, 37, -43},
     *     {-16, -43, 98}
     * };
     * double[] b = {1, 2, 3};
     * CholeskyResponse response = choleskyService.cholesky(A, b);
     * double[] x = response.getSolution();
     * double[][] L = response.getL();
     * double[][] U = response.getU();
     * }
     * </pre>
     */
    public CholeskyResponse cholesky(double[][] A, double[] b) {
        int n = A.length;
        double[][] L = new double[n][n];
        double[][] U = new double[n][n];

        // Initialize L and U matrices
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    U[i][i] = 1.0;
                } else {
                    U[i][j] = 0.0;
                    L[i][j] = 0.0;
                }
            }
        }

        // Perform Cholesky decomposition
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                double sum = 0.0;
                for (int k = 0; k < j; k++) {
                    sum += L[i][k] * L[j][k];
                }
                if (i == j) {
                    L[i][j] = Math.sqrt(A[i][i] - sum);
                } else {
                    L[i][j] = (A[i][j] - sum) / L[j][j];
                }
            }
        }

        // Transpose L to get U
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                U[j][i] = L[i][j];
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
    }
}
