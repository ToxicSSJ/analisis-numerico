package com.numetrify.service;

import com.numetrify.dto.GaussSeidelResponse;
import com.numetrify.util.MathUtils;
import lombok.SneakyThrows;
import org.apache.commons.math3.linear.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GaussSeidelService {

    @SneakyThrows
    public GaussSeidelResponse gaussSeidel(int size, String matrixData, String bData, String x0Data, int errorType, double toleranceValue, int maxIterations) {
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

        // Check if matrix D-L is singular
        if (new LUDecomposition(D.subtract(L)).getDeterminant() == 0) {
            return new GaussSeidelResponse("The matrix (D - L) is not invertible. The method fails.", new ArrayList<>(), new ArrayList<>());
        }

        // Calculate T and C
        RealMatrix DL_inv = new LUDecomposition(D.subtract(L)).getSolver().getInverse();
        RealMatrix T = DL_inv.multiply(U);
        RealVector C = DL_inv.operate(vectorB);

        // Perform Gauss-Seidel iteration
        List<double[]> xValues = new ArrayList<>();
        List<Double> errors = new ArrayList<>();
        RealVector x1;
        int iterations = 0;
        double error = tolerance + 1;
        xValues.add(vectorX0.toArray());
        errors.add(error);
        while (error > tolerance && iterations < maxIterations) {
            x1 = T.operate(vectorX0).add(C);
            error = calculateError(x1, vectorX0);
            if (errorType == 2) {
                error /= x1.getNorm();
            }
            errors.add(error);
            xValues.add(x1.toArray());
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
        return new GaussSeidelResponse(message, xValues, errors);
    }

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

    private double calculateError(RealVector x1, RealVector x0) {
        return x1.subtract(x0).getNorm();
    }

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