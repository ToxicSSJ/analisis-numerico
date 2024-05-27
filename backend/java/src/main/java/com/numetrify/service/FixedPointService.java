package com.numetrify.service;

import com.numetrify.dto.FixedPointResponse;
import lombok.SneakyThrows;
import org.mariuszgromada.math.mxparser.Function;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class to perform the Fixed Point method for root finding.
 */
@Service
public class FixedPointService {

    /**
     * Performs the Fixed Point method to find a root of the given function.
     *
     * @param functionExpression the expression of the function
     * @param gFunctionExpression the expression of the g function used for iteration
     * @param initialGuess the initial guess for the root
     * @param errorType the type of error to use (1 for absolute error, 2 for relative error)
     * @param toleranceValue the tolerance value for the stopping criterion
     * @param maxIterations the maximum number of iterations
     * @return FixedPointResponse containing the result of the Fixed Point method
     *
     * Example usage:
     * <pre>
     * {@code
     * String functionExpression = "x^3 - x - 2";
     * String gFunctionExpression = "sqrt(x + 2)";
     * double initialGuess = 1.0;
     * int errorType = 1;
     * double toleranceValue = 0.01;
     * int maxIterations = 100;
     * FixedPointResponse response = fixedPointService.fixedPoint(functionExpression, gFunctionExpression, initialGuess, precisionType, errorType, toleranceValue, maxIterations);
     * List<Double> xValues = response.getXValues();
     * List<Double> functionValues = response.getFunctionValues();
     * List<Double> errors = response.getErrors();
     * List<Integer> iterations = response.getIterations();
     * }
     * </pre>
     */
    @SneakyThrows
    public FixedPointResponse fixedPoint(String functionExpression, String gFunctionExpression, double initialGuess, int errorType, double toleranceValue, int maxIterations) {
        // Create the function and gFunction using the provided expressions
        Function function = new Function("f(x) = " + functionExpression);
        Function gFunction = new Function("g(x) = " + gFunctionExpression);

        // Calculate tolerance based on the type of error
        double tolerance = 0.5 * Math.pow(10, -toleranceValue);

        // Initialize lists to store the values of x, f(x), errors, and iterations
        List<Double> xValues = new ArrayList<>();
        List<Double> functionValues = new ArrayList<>();
        List<Double> errors = new ArrayList<>();
        List<Integer> iterations = new ArrayList<>();

        // Initial values
        double currentX = initialGuess;
        double currentFunctionValue = function.calculate(currentX);
        int iterationCount = 0;
        double error = 100.0; // Initial error set to 100%
        xValues.add(currentX);
        functionValues.add(currentFunctionValue);
        errors.add(error);
        iterations.add(iterationCount);

        // Perform the fixed point method
        while (error >= tolerance && currentFunctionValue != 0 && iterationCount < maxIterations) {
            iterationCount++;
            currentX = gFunction.calculate(currentX);
            currentFunctionValue = function.calculate(currentX);
            xValues.add(currentX);
            functionValues.add(currentFunctionValue);
            iterations.add(iterationCount);

            // Calculate the error based on the error type
            error = errorType == 1 ? Math.abs(xValues.get(iterationCount) - xValues.get(iterationCount - 1))
                    : Math.abs((xValues.get(iterationCount) - xValues.get(iterationCount - 1)) / xValues.get(iterationCount));
            errors.add(error);
        }

        // Determine the result message
        String message = currentFunctionValue == 0 ? currentX + " is a root of f(x)"
                : errors.get(iterationCount) < tolerance ? "The approximate solution is: " + currentX + ", with a tolerance = " + tolerance
                : "Failed in " + maxIterations + " iterations";

        return new FixedPointResponse(message, xValues, functionValues, errors, iterations);
    }
}