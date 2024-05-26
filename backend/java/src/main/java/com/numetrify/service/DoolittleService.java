package com.numetrify.service;

import com.numetrify.dto.DoolittleResponse;
import org.springframework.stereotype.Service;

/**
 * Service class to perform Doolittle's method for LU decomposition on a matrix.
 */
@Service
public class DoolittleService {

    /**
     * Performs Doolittle's method for LU decomposition on the given matrix A and solves the system of equations Ax = B.
     *
     * @param A the matrix to decompose
     * @param B the vector B in the system of equations Ax = B
     * @return DoolittleResponse containing the solution vector x, and the L and U matrices
     *
     * Example usage:
     * <pre>
     * {@code
     * double[][] A = {
     *     {2, -1, -2},
     *     {-4, 6, 3},
     *     {-4, -2, 8}
     * };
     * double[] B = {1, 2, 3};
     * DoolittleResponse response = doolittleService.doolittle(A, B);
     * double[] x = response.getSolution();
     * double[][] L = response.getL();
     * double[][] U = response.getU();
     * }
     * </pre>
     */
    public DoolittleResponse doolittle(double[][] A, double[] B) {
        int n = A.length;

        double[][] L = new double[n][n];
        double[][] U = new double[n][n];

        // Initialize L to the identity matrix
        for (int i = 0; i < n; i++) {
            L[i][i] = 1.0;
        }

        // Doolittle's algorithm for LU decomposition
        for (int i = 0; i < n; i++) {
            // Upper Triangular
            for (int k = i; k < n; k++) {
                double sum = 0.0;
                for (int j = 0; j < i; j++) {
                    sum += (L[i][j] * U[j][k]);
                }
                U[i][k] = A[i][k] - sum;
            }

            // Lower Triangular
            for (int k = i + 1; k < n; k++) {
                double sum = 0.0;
                for (int j = 0; j < i; j++) {
                    sum += (L[k][j] * U[j][i]);
                }
                L[k][i] = (A[k][i] - sum) / U[i][i];
            }
        }

        // Solve Lz = B using forward substitution
        double[] z = new double[n];
        for (int i = 0; i < n; i++) {
            z[i] = B[i];
            for (int j = 0; j < i; j++) {
                z[i] -= L[i][j] * z[j];
            }
        }

        // Solve Ux = z using backward substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = z[i];
            for (int j = i + 1; j < n; j++) {
                x[i] -= U[i][j] * x[j];
            }
            x[i] /= U[i][i];
        }

        return new DoolittleResponse(x, L, U);
    }
}