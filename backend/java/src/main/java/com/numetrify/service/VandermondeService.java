package com.numetrify.service;

import com.numetrify.dto.VandermondeResponse;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Service class to solve Vandermonde matrix problems and find the polynomial that interpolates given points.
 */
@Service
public class VandermondeService {

    /**
     * Solves the Vandermonde matrix problem to find the polynomial that interpolates the given points.
     *
     * @param x the x values of the points
     * @param y the y values of the points
     * @return VandermondeResponse containing the resulting polynomial and its coefficients
     *
     * Example usage:
     * <pre>
     * {@code
     * double[] xValues = {1, 2, 3};
     * double[] yValues = {2, 3, 5};
     * VandermondeResponse response = vandermondeService.solveVandermonde(xValues, yValues);
     * String message = response.getMessage();
     * double[] coefficients = response.getCoefficients();
     * String polynomial = response.getPolynomial();
     * }
     * </pre>
     */
    public VandermondeResponse solveVandermonde(double[] x, double[] y) {
        if (x.length != y.length) {
            return new VandermondeResponse("The X and Y values must have the same number of elements.", null, null);
        }

        if (Arrays.stream(x).distinct().count() != x.length) {
            return new VandermondeResponse("The X values must not repeat to be a valid function.", null, null);
        }

        int n = x.length;
        double[][] vandermondeMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                vandermondeMatrix[i][j] = Math.pow(x[i], n - j - 1);
            }
        }

        RealMatrix matrixA = MatrixUtils.createRealMatrix(vandermondeMatrix);
        RealVector vectorB = MatrixUtils.createRealVector(y);

        if (new LUDecomposition(matrixA).getDeterminant() == 0) {
            return new VandermondeResponse("The method does not work because matrix A is singular.", null, null);
        }

        DecompositionSolver solver = new LUDecomposition(matrixA).getSolver();
        RealVector solution = solver.solve(vectorB);

        double[] coefficients = solution.toArray();
        String polynomial = formatPolynomial(coefficients);

        return new VandermondeResponse("The polynomial that interpolates the given points is: " + polynomial, coefficients, polynomial);
    }

    /**
     * Formats the polynomial coefficients into a readable string representation.
     *
     * @param coefficients the polynomial coefficients
     * @return the string representation of the polynomial
     */
    private String formatPolynomial(double[] coefficients) {
        StringBuilder sb = new StringBuilder();
        int degree = coefficients.length - 1;
        for (int i = 0; i < coefficients.length; i++) {
            if (i != 0 && coefficients[i] >= 0) {
                sb.append(" + ");
            }
            sb.append(String.format("%.3f", coefficients[i]));
            if (degree - i > 0) {
                sb.append(" x^").append(degree - i);
            }
        }
        return sb.toString();
    }
}