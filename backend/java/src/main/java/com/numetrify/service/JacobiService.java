package com.numetrify.service;

import com.numetrify.dto.JacobiResponse;
import com.numetrify.util.MathUtils;
import lombok.SneakyThrows;
import org.apache.commons.math3.linear.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service class to perform the Jacobi iterative method for solving systems of linear equations.
 */
@Service
public class JacobiService {

    /**
     * Performs the Jacobi iterative method to solve the system of linear equations Ax = b.
     *
     * @param size the size of the matrix and vectors
     * @param matrixData the string representation of the matrix A, with rows separated by semicolons (;) and elements within rows separated by spaces
     * @param bData the string representation of the vector b, with elements separated by spaces
     * @param x0Data the string representation of the initial guess vector x0, with elements separated by spaces
     * @param errorType the type of error to use (1 for absolute error, 2 for relative error)
     * @param toleranceValue the tolerance value for the stopping criterion
     * @param maxIterations the maximum number of iterations
     * @param normType the type of norm to use ("inf" for infinity norm, otherwise the integer value of the norm)
     * @return JacobiResponse containing the result of the Jacobi method
     *
     * Example usage:
     * <pre>
     * {@code
     * int size = 3;
     * String matrixData = "4 1 2; 1 5 1; 2 1 3";
     * String bData = "4 7 3";
     * String x0Data = "0 0 0";
     * int errorType = 1;
     * double toleranceValue = 0.01;
     * int maxIterations = 100;
     * String normType = "inf";
     * JacobiResponse response = jacobiService.jacobi(size, matrixData, bData, x0Data, errorType, toleranceValue, maxIterations, normType);
     * String message = response.getMessage();
     * List<double[]> xn = response.getXn();
     * List<Double> errors = response.getErrors();
     * }
     * </pre>
     */
    @SneakyThrows
    public JacobiResponse jacobi(int size, String matrixData, String bData, String x0Data, int errorType, double toleranceValue, int maxIterations, String normType) {
        // Parse matrix A
        double[][] A = new double[size][size];
        String[] rows = matrixData.split(";");
        for (int i = 0; i < size; i++) {
            String[] rowData = rows[i].trim().split("\\s+");
            for (int j = 0; j < size; j++) {
                A[i][j] = Double.parseDouble(rowData[j]);
            }
        }

        // Parse vector b and initial guess x0
        double[] b = parseVector(bData, size);
        double[] x0 = parseVector(x0Data, size);

        // Determine tolerance
        double tolerance = MathUtils.getTolerance(toleranceValue, errorType);

        // Determine norm
        int norm = normType.equalsIgnoreCase("inf") ? Integer.MAX_VALUE : Integer.parseInt(normType);

        // Initialize matrices and vectors
        RealMatrix matrixA = MatrixUtils.createRealMatrix(A);
        RealVector vectorB = MatrixUtils.createRealVector(b);
        RealVector vectorX0 = MatrixUtils.createRealVector(x0);

        // Initialize D, L, U matrices
        RealMatrix D = MatrixUtils.createRealDiagonalMatrix(matrixA.getColumn(0));
        for (int i = 1; i < size; i++) {
            D.setEntry(i, i, matrixA.getEntry(i, i));
        }
        RealMatrix L = MatrixUtils.createRealMatrix(size, size);
        RealMatrix U = MatrixUtils.createRealMatrix(size, size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i > j) L.setEntry(i, j, -matrixA.getEntry(i, j));
                if (i < j) U.setEntry(i, j, -matrixA.getEntry(i, j));
            }
        }

        // Check if matrix D is singular
        if (new LUDecomposition(D).getDeterminant() == 0) {
            return new JacobiResponse("Matrix D is singular, the method fails.", new ArrayList<>(), new ArrayList<>());
        }

        // Calculate T and C
        RealMatrix D_inv = new LUDecomposition(D).getSolver().getInverse();
        RealMatrix T = D_inv.multiply(L.add(U));
        RealVector C = D_inv.operate(vectorB);

        // Perform Jacobi iteration
        List<double[]> xn = new ArrayList<>();
        List<Double> errors = new ArrayList<>();
        RealVector x1;
        int iterations = 0;
        double error = tolerance + 1;
        xn.add(vectorX0.toArray());
        while (error > tolerance && iterations < maxIterations) {
            x1 = T.operate(vectorX0).add(C);
            error = calculateError(x1, vectorX0, norm);
            if (errorType == 2) {
                error /= x1.getNorm();
            }
            errors.add(error);
            xn.add(x1.toArray());
            vectorX0 = x1;
            iterations++;
        }

        // Calculate spectral radius
        double spectralRadius = calculateSpectralRadius(T);

        // Check for convergence
        String message;
        if (error < tolerance) {
            message = "The approximate solution is: " + Arrays.toString(vectorX0.toArray()) + ", with a tolerance = " + tolerance;
            if (spectralRadius < 1) {
                message += " This solution is unique because the spectral radius of T is " + spectralRadius + " and is less than 1.";
            }
        } else {
            message = "Failed in " + maxIterations + " iterations.";
            if (spectralRadius >= 1) {
                message += " It is possible that the method failed because the spectral radius of T is " + spectralRadius + " and is greater than or equal to 1.";
            }
        }
        return new JacobiResponse(message, xn, errors);
    }

    /**
     * Parses a string representation of a vector.
     *
     * @param data the string representation of the vector, with elements separated by spaces
     * @param size the expected size of the vector
     * @return the parsed vector as a double array
     * @throws IllegalArgumentException if the size of the parsed vector does not match the expected size
     */
    private double[] parseVector(String data, int size) {
        String[] elements = data.split("\\s+");
        if (elements.length != size) {
            throw new IllegalArgumentException("Vector size does not match the given matrix size.");
        }
        double[] vector = new double[size];
        for (int i = 0; i < size; i++) {
            vector[i] = Double.parseDouble(elements[i]);
        }
        return vector;
    }

    /**
     * Calculates the error between two vectors.
     *
     * @param x1 the first vector
     * @param x0 the second vector
     * @param norm the type of norm to use for the error calculation
     * @return the calculated error
     */
    private double calculateError(RealVector x1, RealVector x0, int norm) {
        return x1.subtract(x0).getNorm();
    }

    /**
     * Calculates the spectral radius of the matrix T.
     *
     * @param T the matrix T
     * @return the spectral radius of T
     */
    private double calculateSpectralRadius(RealMatrix T) {
        EigenDecomposition eigenDecomposition = new EigenDecomposition(T);
        double[] realParts = eigenDecomposition.getRealEigenvalues();
        double maxEigenvalue = 0;
        for (double eigenvalue : realParts) {
            maxEigenvalue = Math.max(maxEigenvalue, Math.abs(eigenvalue));
        }
        return maxEigenvalue;
    }
}