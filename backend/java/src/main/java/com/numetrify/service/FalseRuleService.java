package com.numetrify.service;

import com.numetrify.dto.FalseRuleResponse;
import lombok.SneakyThrows;
import org.mariuszgromada.math.mxparser.Function;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class to perform the False Rule method for root finding.
 */
@Service
public class FalseRuleService {

    /**
     * Performs the False Rule method to find a root of the given function.
     *
     * @param functionExpression the expression of the function
     * @param lowerBound the lower bound of the interval
     * @param upperBound the upper bound of the interval
     * @param errorType the type of error to use (1 for absolute error, 2 for relative error)
     * @param toleranceValue the tolerance value for the stopping criterion
     * @param maxIterations the maximum number of iterations
     * @return FalseRuleResponse containing the result of the False Position method
     * @throws IllegalArgumentException if the interval is inadequate
     *
     * Example usage:
     * <pre>
     * {@code
     * String functionExpression = "x^3 - x - 2";
     * double lowerBound = 1.0;
     * double upperBound = 2.0;
     * int errorType = 1;
     * double toleranceValue = 0.01;
     * int maxIterations = 100;
     * FalsePositionResponse response = falseRuleService.falsePosition(functionExpression, lowerBound, upperBound, precisionType, errorType, toleranceValue, maxIterations);
     * List<Double> xValues = response.getXValues();
     * List<Double> functionValues = response.getFunctionValues();
     * List<Double> errors = response.getErrors();
     * List<Integer> iterations = response.getIterations();
     * }
     * </pre>
     */
    @SneakyThrows
    public FalseRuleResponse falseRule(String functionExpression, double lowerBound, double upperBound, int errorType, double toleranceValue, int maxIterations) {
        // Create the function using the provided expression
        Function function = new Function("f(x) = " + functionExpression);

        // Calculate function values at the bounds
        double functionAtLowerBound = function.calculate(lowerBound);
        double functionAtUpperBound = function.calculate(upperBound);
        double tolerance = 0.5 * Math.pow(10, -toleranceValue);

        // Check if the bounds are roots of the function
        if (functionAtLowerBound == 0) {
            return new FalseRuleResponse(lowerBound + " is a root of f(x)", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        } else if (functionAtUpperBound == 0) {
            return new FalseRuleResponse(upperBound + " is a root of f(x)", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        } else if (functionAtLowerBound * functionAtUpperBound > 0) {
            return new FalseRuleResponse("The interval is inadequate", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        // Initialize lists to store the values of x, f(x), errors, and iterations
        List<Double> xValues = new ArrayList<>();
        List<Double> functionValues = new ArrayList<>();
        List<Double> errors = new ArrayList<>();
        List<Integer> iterations = new ArrayList<>();

        // Perform the false position method
        double rootApproximation = lowerBound - (functionAtLowerBound * (upperBound - lowerBound)) / (functionAtUpperBound - functionAtLowerBound);
        double functionAtRootApproximation = function.calculate(rootApproximation);
        xValues.add(rootApproximation);
        functionValues.add(functionAtRootApproximation);
        errors.add(100.0); // Initial error set to 100%
        iterations.add(0);

        int iterationCount = 0;
        while (errors.get(iterationCount) >= tolerance && functionAtRootApproximation != 0 && iterationCount < maxIterations) {
            // Update the bounds based on the sign of the function at the rootApproximation
            if (functionAtLowerBound * functionAtRootApproximation < 0) {
                upperBound = rootApproximation;
                functionAtUpperBound = function.calculate(upperBound);
            } else {
                lowerBound = rootApproximation;
                functionAtLowerBound = function.calculate(lowerBound);
            }

            // Update the root approximation and function value at the root approximation
            iterationCount++;
            rootApproximation = lowerBound - (functionAtLowerBound * (upperBound - lowerBound)) / (functionAtUpperBound - functionAtLowerBound);
            functionAtRootApproximation = function.calculate(rootApproximation);
            xValues.add(rootApproximation);
            functionValues.add(functionAtRootApproximation);
            iterations.add(iterationCount);

            // Calculate the error based on the error type
            double error = errorType == 1 ? Math.abs(xValues.get(iterationCount) - xValues.get(iterationCount - 1))
                    : Math.abs((xValues.get(iterationCount) - xValues.get(iterationCount - 1)) / xValues.get(iterationCount));
            errors.add(error);
        }

        // Determine the result message
        String message = functionAtRootApproximation == 0 ? rootApproximation + " is a root of f(x)"
                : errors.get(iterationCount) < tolerance ? "The approximate solution is: " + rootApproximation + ", with a tolerance = " + tolerance
                : "Failed in " + maxIterations + " iterations";

        return new FalseRuleResponse(message, xValues, functionValues, errors, iterations);
    }
}