package com.numetrify.service;

import com.numetrify.dto.SORResponse;
import org.apache.commons.math3.linear.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class to perform the Successive Over-Relaxation (SOR) method for solving systems of linear equations.
 */
@Service
public class SORService {

    /**
     * Solves the system of linear equations Ax = b using the SOR method.
     *
     * @param size the size of the matrix and vectors
     * @param matrixData the string representation of the matrix A, with rows separated by semicolons (;) and elements within rows separated by spaces
     * @param bData the string representation of the vector b, with elements separated by spaces
     * @param x0Data the string representation of the initial guess vector x0, with elements separated by spaces
     * @param w the relaxation factor
     * @param errorType the type of error to use (1 for absolute error, 2 for relative error)
     * @param toleranceValue the tolerance value for the stopping criterion
     * @param maxIterations the maximum number of iterations
     * @param normType the type of norm to use (1 for L1 norm, 2 for L2 norm, Integer.MAX_VALUE for infinity norm)
     * @return SORResponse containing the result of the SOR method
     *
     * Example usage:
     * <pre>
     * {@code
     * int size = 3;
     * String matrixData = "4 1 2; 1 5 1; 2 1 3";
     * String bData = "4 7 3";
     * String x0Data = "0 0 0";
     * double w = 1.25;
     * int errorType = 1;
     * double toleranceValue = 0.01;
     * int maxIterations = 100;
     * int normType = 2;
     * SORResponse response = sorService.solveSOR(size, matrixData, bData, x0Data, w, errorType, toleranceValue, maxIterations, normType);
     * String message = response.getMessage();
     * List<double[]> xValues = response.getXValues();
     * List<Double> errors = response.getErrors();
     * List<Integer> iterations = response.getIterations();
     * }
     * </pre>
     */
    public SORResponse solveSOR(int size, String matrixData, String bData, String x0Data, double w, int errorType, double toleranceValue, int maxIterations, int normType) {
        // Convert input data to matrix and vectors
        RealMatrix matrixA = MatrixUtils.createRealMatrix(parseMatrix(matrixData, size));
        RealVector bVector = MatrixUtils.createRealVector(parseVector(bData, size));
        RealVector xVector = MatrixUtils.createRealVector(parseVector(x0Data, size));

        // Define tolerance based on error type
        double tolerance = (errorType == 1) ? 0.5 * Math.pow(10, -toleranceValue) : 5 * Math.pow(10, -toleranceValue);

        // Decompose matrix into D, L, U
        RealMatrix D = MatrixUtils.createRealDiagonalMatrix(matrixA.getColumn(0));
        RealMatrix L = MatrixUtils.createRealMatrix(size, size);
        RealMatrix U = MatrixUtils.createRealMatrix(size, size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i > j) {
                    L.setEntry(i, j, -matrixA.getEntry(i, j));
                } else if (i < j) {
                    U.setEntry(i, j, -matrixA.getEntry(i, j));
                } else {
                    D.setEntry(i, i, matrixA.getEntry(i, i));
                }
            }
        }

        // Check if D - wL is singular
        RealMatrix D_minus_wL = D.subtract(L.scalarMultiply(w));
        LUDecomposition lud = new LUDecomposition(D_minus_wL);
        if (!lud.getSolver().isNonSingular()) {
            String message = "The matrix (D - wL) is singular. The method fails.";
            return new SORResponse(message, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        RealMatrix invD_minus_wL = lud.getSolver().getInverse();

        RealMatrix T = invD_minus_wL.multiply(D.scalarMultiply(1 - w).add(U.scalarMultiply(w)));
        RealVector C = invD_minus_wL.operate(bVector).mapMultiply(w);

        // Iteration variables
        List<double[]> xValues = new ArrayList<>();
        List<Double> errors = new ArrayList<>();
        List<Integer> iterations = new ArrayList<>();

        xValues.add(xVector.toArray());
        double error = tolerance + 1;
        errors.add(error);
        iterations.add(0);

        int iterationCount = 0;
        while (error > tolerance && iterationCount < maxIterations) {
            RealVector x1 = T.operate(xVector).add(C);
            error = calculateNorm(x1.subtract(xVector), normType);
            if (errorType == 2) {
                error /= calculateNorm(x1, normType);
            }

            xVector = x1;
            iterationCount++;
            xValues.add(xVector.toArray());
            errors.add(error);
            iterations.add(iterationCount);
        }

        // Determine result message
        String message = (error <= tolerance) ?
                "The approximate solution is: " + xVector.toString() + ", with a tolerance = " + tolerance :
                "Failed in " + maxIterations + " iterations";

        return new SORResponse(message, xValues, errors, iterations);
    }

    /**
     * Calculates the norm of a vector based on the specified norm type.
     *
     * @param vector the vector to calculate the norm of
     * @param normType the type of norm (1 for L1 norm, 2 for L2 norm, Integer.MAX_VALUE for infinity norm)
     * @return the calculated norm
     */
    private double calculateNorm(RealVector vector, int normType) {
        switch (normType) {
            case 1:
                return vector.getL1Norm();
            case 2:
                return vector.getNorm();
            case Integer.MAX_VALUE:
                return vector.getLInfNorm();
            default:
                throw new IllegalArgumentException("Unsupported norm type: " + normType);
        }
    }

    /**
     * Parses a string representation of a matrix.
     *
     * @param matrixData the string representation of the matrix, with rows separated by semicolons (;) and elements within rows separated by spaces
     * @param size the size of the matrix
     * @return the parsed matrix as a 2D double array
     */
    private double[][] parseMatrix(String matrixData, int size) {
        String[] rows = matrixData.split(";");
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            String[] values = rows[i].trim().split("\\s+");
            for (int j = 0; j < size; j++) {
                matrix[i][j] = Double.parseDouble(values[j]);
            }
        }
        return matrix;
    }

    /**
     * Parses a string representation of a vector.
     *
     * @param vectorData the string representation of the vector, with elements separated by spaces
     * @param size the size of the vector
     * @return the parsed vector as a double array
     */
    private double[] parseVector(String vectorData, int size) {
        String[] values = vectorData.split("\\s+");
        double[] vector = new double[size];
        for (int i = 0; i < size; i++) {
            vector[i] = Double.parseDouble(values[i]);
        }
        return vector;
    }
}