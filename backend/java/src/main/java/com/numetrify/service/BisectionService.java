package com.numetrify.service;

import org.mariuszgromada.math.mxparser.Function;
import org.springframework.stereotype.Service;
import com.numetrify.dto.BisectionResponse;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class to perform the Bisection method for root finding.
 */
@Service
public class BisectionService {

    /**
     * Performs the Bisection method to find a root of the given function.
     *
     * @param functionExpression the expression of the function
     * @param lowerBound the lower bound of the interval
     * @param upperBound the upper bound of the interval
     * @param errorType the type of error to use (1 for absolute error, 2 for relative error)
     * @param toleranceValue the tolerance value for the stopping criterion
     * @param maxIterations the maximum number of iterations
     * @return BisectionResponse containing the result of the Bisection method
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
     * BisectionResponse response = bisectionService.bisection(functionExpression, lowerBound, upperBound, errorType, toleranceValue, maxIterations);
     * List<Double> xValues = response.getXValues();
     * List<Double> functionValues = response.getFunctionValues();
     * List<Double> errors = response.getErrors();
     * List<Integer> iterations = response.getIterations();
     * }
     * </pre>
     */
    @SneakyThrows
    public BisectionResponse bisection(String functionExpression, double lowerBound, double upperBound, int precisionType, int errorType, double toleranceValue, int maxIterations) {
        // Create the function using the provided expression
        Function function = new Function("f(x) = " + functionExpression);

        // Calculate function values at the bounds
        double functionAtLowerBound = function.calculate(lowerBound);
        double functionAtUpperBound = function.calculate(upperBound);
        double tolerance = precisionType == 1 ? 0.5 * Math.pow(10, -toleranceValue) : 5 * Math.pow(10, -toleranceValue);

        // Check if the bounds are roots of the function
        if (functionAtLowerBound == 0) {
            return new BisectionResponse(lowerBound + " is a root of f(x)", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        } else if (functionAtUpperBound == 0) {
            return new BisectionResponse(upperBound + " is a root of f(x)", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        } else if (functionAtLowerBound * functionAtUpperBound > 0) {
            return new BisectionResponse("The interval is inadequate", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        // Initialize lists to store the values of x, f(x), errors, and iterations
        List<Double> xValues = new ArrayList<>();
        List<Double> functionValues = new ArrayList<>();
        List<Double> errors = new ArrayList<>();
        List<Integer> iterations = new ArrayList<>();

        // Perform the bisection method
        double midPoint = (lowerBound + upperBound) / 2;
        double functionAtMidPoint = function.calculate(midPoint);
        xValues.add(midPoint);
        functionValues.add(functionAtMidPoint);
        errors.add(100.0); // Initial error set to 100%
        iterations.add(0);

        int iterationCount = 0;
        while (errors.get(iterationCount) >= tolerance && functionAtMidPoint != 0 && iterationCount < maxIterations) {
            // Update the bounds based on the sign of the function at the midPoint
            if (functionAtLowerBound * functionAtMidPoint < 0) {
                upperBound = midPoint;
                functionAtUpperBound = function.calculate(upperBound);
            } else {
                lowerBound = midPoint;
                functionAtLowerBound = function.calculate(lowerBound);
            }

            // Update the midpoint and function value at the midpoint
            iterationCount++;
            midPoint = (lowerBound + upperBound) / 2;
            functionAtMidPoint = function.calculate(midPoint);
            xValues.add(midPoint);
            functionValues.add(functionAtMidPoint);
            iterations.add(iterationCount);

            // Calculate the error based on the error type
            double error = errorType == 1 ? Math.abs(xValues.get(iterationCount) - xValues.get(iterationCount - 1))
                    : Math.abs((xValues.get(iterationCount) - xValues.get(iterationCount - 1)) / xValues.get(iterationCount));
            errors.add(error);
        }

        // Determine the result message
        String message = functionAtMidPoint == 0 ? midPoint + " is a root of f(x)"
                : errors.get(iterationCount) < tolerance ? "The approximate solution is: " + midPoint + ", with a tolerance = " + tolerance
                : "Failed in " + maxIterations + " iterations";

        // Apply precision formatting
        List<Double> formattedXValues = new ArrayList<>();
        for (double x : xValues) {
            formattedXValues.add(precisionType == 1 ? roundSignificantFigures(x, (int) toleranceValue) : roundDecimalPlaces(x, (int) toleranceValue));
        }

        return new BisectionResponse(message, formattedXValues, functionValues, errors, iterations);
    }

    private double roundSignificantFigures(double value, int significantFigures) {
        if (value == 0) {
            return 0;
        }

        final double d = Math.ceil(Math.log10(value < 0 ? -value : value));
        final int power = significantFigures - (int) d;

        final double magnitude = Math.pow(10, power);
        final long shifted = Math.round(value * magnitude);
        return shifted / magnitude;
    }

    private double roundDecimalPlaces(double value, int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.round(value * scale) / scale;
    }
}