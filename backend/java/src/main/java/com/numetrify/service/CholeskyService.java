package com.numetrify.service;

import com.numetrify.dto.CholeskyResponse;
import com.numetrify.service.matrix.CholeskyDecomposition;
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
        try {
            CholeskyDecomposition cholesky = new CholeskyDecomposition(A);
            double[] solution = cholesky.solve(b);
            double[][] L = cholesky.getL();
            double[][] U = cholesky.getU();
            return new CholeskyResponse("Success", solution, L, U);
        } catch (Exception e) {
            return new CholeskyResponse("Error: " + e.getMessage(), null, null, null);
        }
    }

}
