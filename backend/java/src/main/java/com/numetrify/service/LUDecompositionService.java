package com.numetrify.service;

import com.numetrify.dto.LUDecompositionResponse;
import org.springframework.stereotype.Service;

/**
 * Service class to perform LU Decomposition on a matrix.
 */
@Service
public class LUDecompositionService {

    /**
     * Performs LU Decomposition on the given matrix.
     *
     * @param A the matrix to decompose
     * @return LUDecompositionResponse containing the L and U matrices and the solution vector
     * @throws IllegalArgumentException if the matrix does not correspond to a square system of equations
     * @throws ArithmeticException if division by zero occurs during the decomposition
     *
     * Example usage:
     * <pre>
     * {@code
     * double[][] A = {
     *     {2, -1, -2, -3},
     *     {-4, 6, 3, 9},
     *     {-4, -2, 8, -4}
     * };
     * LUDecompositionResponse response = luDecompositionService.luDecomposition(A);
     * double[][] L = response.getL();
     * double[][] U = response.getU();
     * double[] solution = response.getSolution();
     * }
     * </pre>
     */
    public LUDecompositionResponse luDecomposition(double[][] A) {

        int n = A.length;
        int m = A[0].length;

        if (m != n + 1) {
            throw new IllegalArgumentException("Error: The matrix does not correspond to a square system of equations.");
        }

        double[][] L = new double[n][n];
        double[][] U = new double[n][n];

        // Initialize L matrix with 1s on the diagonal
        for (int i = 0; i < n; i++) {
            L[i][i] = 1.0;
        }

        // Perform LU decomposition
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                double sum1 = 0.0;
                for (int k = 0; k < i; k++) {
                    sum1 += L[i][k] * U[k][j];
                }
                U[i][j] = A[i][j] - sum1;
            }

            for (int j = i + 1; j < n; j++) {
                if (U[i][i] == 0.0) {
                    throw new ArithmeticException("Error: Division by zero.");
                }
                double sum2 = 0.0;
                for (int k = 0; k < i; k++) {
                    sum2 += L[j][k] * U[k][i];
                }
                L[j][i] = (A[j][i] - sum2) / U[i][i];
            }
        }

        // Assume the vector b is in the last column of A
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = A[i][n]; // Here, the index is n because b is the last column
        }

        // Solve Ly = b using forward substitution
        double[] y = forwardSubstitution(L, b);

        // Solve Ux = y using backward substitution
        double[] x = backwardSubstitution(U, y);

        // Create the response
        LUDecompositionResponse response = new LUDecompositionResponse();
        response.setL(L);
        response.setU(U);
        response.setSolution(x);

        return response;
    }

    /**
     * Performs forward substitution to solve Ly = b.
     *
     * @param L the lower triangular matrix
     * @param b the vector b
     * @return the solution vector y
     *
     * Example usage:
     * <pre>
     * {@code
     * double[][] L = {
     *     {1, 0, 0},
     *     {0.5, 1, 0},
     *     {0.5, 0.25, 1}
     * };
     * double[] b = {2, 1, 3};
     * double[] y = forwardSubstitution(L, b);
     * }
     * </pre>
     */
    private double[] forwardSubstitution(double[][] L, double[] b) {
        int n = L.length;
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            for (int j = 0; j < i; j++) {
                sum += L[i][j] * y[j];
            }
            y[i] = (b[i] - sum) / L[i][i];
        }
        return y;
    }

    /**
     * Performs backward substitution to solve Ux = y.
     *
     * @param U the upper triangular matrix
     * @param y the vector y
     * @return the solution vector x
     *
     * Example usage:
     * <pre>
     * {@code
     * double[][] U = {
     *     {2, -1, -2},
     *     {0, 2.5, 1},
     *     {0, 0, 4}
     * };
     * double[] y = {2, 1, 3};
     * double[] x = backwardSubstitution(U, y);
     * }
     * </pre>
     */
    private double[] backwardSubstitution(double[][] U, double[] y) {
        int n = U.length;
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += U[i][j] * x[j];
            }
            x[i] = (y[i] - sum) / U[i][i];
        }
        return x;
    }
}