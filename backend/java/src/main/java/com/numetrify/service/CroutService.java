package com.numetrify.service;

import com.numetrify.dto.CroutResponse;
import org.springframework.stereotype.Service;

/**
 * Service class to perform Crout's method for LU decomposition on a matrix.
 */
@Service
public class CroutService {

    /**
     * Performs Crout's method for LU decomposition on the given matrix A and solves the system of equations Ax = B.
     *
     * @param A the matrix to decompose
     * @param B the vector B in the system of equations Ax = B
     * @return CroutResponse containing the solution vector x, and the L and U matrices
     *
     * Example usage:
     * <pre>
     * {@code
     * double[][] A = {
     *     {4, 12, -16},
     *     {12, 37, -43},
     *     {-16, -43, 98}
     * };
     * double[] B = {1, 2, 3};
     * CroutResponse response = croutService.crout(A, B);
     * double[] x = response.getSolution();
     * double[][] L = response.getL();
     * double[][] U = response.getU();
     * }
     * </pre>
     */
    public CroutResponse crout(double[][] A, double[] B) {
        int n = A.length;

        double[][] L = new double[n][n];
        double[][] U = new double[n][n];

        // Initialize U to the identity matrix
        for (int i = 0; i < n; i++) {
            U[i][i] = 1.0;
        }

        // Crout's algorithm for LU decomposition
        for (int j = 0; j < n; j++) {
            for (int i = j; i < n; i++) {
                L[i][j] = A[i][j];
                for (int k = 0; k < j; k++) {
                    L[i][j] -= L[i][k] * U[k][j];
                }
            }
            for (int i = j + 1; i < n; i++) {
                U[j][i] = A[j][i];
                for (int k = 0; k < j; k++) {
                    U[j][i] -= L[j][k] * U[k][i];
                }
                U[j][i] /= L[j][j];
            }
        }

        // Solve Lz = B using forward substitution
        double[] z = new double[n];
        for (int i = 0; i < n; i++) {
            z[i] = B[i];
            for (int j = 0; j < i; j++) {
                z[i] -= L[i][j] * z[j];
            }
            z[i] /= L[i][i];
        }

        // Solve Ux = z using backward substitution
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            x[i] = z[i];
            for (int j = i + 1; j < n; j++) {
                x[i] -= U[i][j] * x[j];
            }
        }

        return new CroutResponse(x, L, U);
    }
}