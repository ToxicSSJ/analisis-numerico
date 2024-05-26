package com.numetrify.service;

import com.numetrify.dto.MultipleRootsResponse;
import com.numetrify.util.MathUtils;
import lombok.SneakyThrows;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class to perform the Multiple Roots method for root finding.
 */
@Service
public class MultipleRootsService {

    /**
     * Performs the Multiple Roots method to find a root of the given function.
     *
     * @param functionExpression the expression of the function
     * @param initialGuess the initial guess for the root
     * @param errorType the type of error to use (1 for absolute error, 2 for relative error)
     * @param precisionType the type of precision to use (1 for significant figures, 2 for decimal places)
     * @param toleranceValue the tolerance value for the stopping criterion
     * @param maxIterations the maximum number of iterations
     * @return MultipleRootsResponse containing the result of the Multiple Roots method
     *
     * Example usage:
     * <pre>
     * {@code
     * String functionExpression = "x^3 - x - 2";
     * double initialGuess = 1.0;
     * int errorType = 1;
     * int precisionType = 1;
     * double toleranceValue = 0.01;
     * int maxIterations = 100;
     * MultipleRootsResponse response = multipleRootsService.multipleRoots(functionExpression, initialGuess, errorType, precisionType, toleranceValue, maxIterations);
     * String message = response.getMessage();
     * List<Double> xValues = response.getXValues();
     * List<Double> functionValues = response.getFunctionValues();
     * List<Double> firstDerivatives = response.getFirstDerivatives();
     * List<Double> secondDerivatives = response.getSecondDerivatives();
     * List<Double> errors = response.getErrors();
     * List<Integer> iterations = response.getIterations();
     * }
     * </pre>
     */
    @SneakyThrows
    public MultipleRootsResponse multipleRoots(String functionExpression, double initialGuess, int errorType, int precisionType, double toleranceValue, int maxIterations) {
        // Create the function and its derivatives using mXparser
        Argument x = new Argument("x = " + initialGuess);
        Expression function = new Expression(functionExpression, x);
        Expression firstDerivative = new Expression("der(" + functionExpression + ", x)", x);
        Expression secondDerivative = new Expression("der(der(" + functionExpression + ", x), x)", x);

        // Calculate tolerance based on the type of error
        double tolerance = MathUtils.getTolerance(toleranceValue, errorType);

        // Initialize lists to store the values of x, f(x), f'(x), f''(x), errors, and iterations
        List<Double> xValues = new ArrayList<>();
        List<Double> functionValues = new ArrayList<>();
        List<Double> firstDerivatives = new ArrayList<>();
        List<Double> secondDerivatives = new ArrayList<>();
        List<Double> errors = new ArrayList<>();
        List<Integer> iterations = new ArrayList<>();

        // Initial values
        double currentX = initialGuess;
        double currentValue = function.calculate();
        double currentFirstDerivative = firstDerivative.calculate();
        double currentSecondDerivative = secondDerivative.calculate();
        int iterationCount = 0;
        double error = 100.0; // Initial error set to 100%
        xValues.add(currentX);
        functionValues.add(currentValue);
        firstDerivatives.add(currentFirstDerivative);
        secondDerivatives.add(currentSecondDerivative);
        errors.add(error);
        iterations.add(iterationCount);

        // Check if the initial guess is valid
        if (Double.isNaN(currentValue) || Double.isNaN(currentFirstDerivative) || Double.isNaN(currentSecondDerivative) ||
                Double.isInfinite(currentFirstDerivative) || Double.isInfinite(currentSecondDerivative)) {
            String message = "The function or its derivatives are not defined at x = " + initialGuess + ". The method fails.";
            return new MultipleRootsResponse(message, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        // Perform the multiple roots method
        while (iterationCount < maxIterations) {
            double denominator = (currentFirstDerivative * currentFirstDerivative - currentValue * currentSecondDerivative);
            if (denominator == 0) {
                String message = "The method fails. The denominator is zero.";
                return new MultipleRootsResponse(message, xValues, functionValues, firstDerivatives, secondDerivatives, errors, iterations);
            }

            iterationCount++;
            currentX = currentX - (currentValue * currentFirstDerivative) / denominator;
            x.setArgumentValue(currentX);
            currentValue = function.calculate();
            currentFirstDerivative = firstDerivative.calculate();
            currentSecondDerivative = secondDerivative.calculate();

            // Check if the current value is valid
            if (Double.isNaN(currentValue) || Double.isNaN(currentFirstDerivative) || Double.isNaN(currentSecondDerivative) ||
                    Double.isInfinite(currentFirstDerivative) || Double.isInfinite(currentSecondDerivative)) {
                String message = "The function or its derivatives are not defined at x = " + currentX + ". The method fails.";
                return new MultipleRootsResponse(message, xValues, functionValues, firstDerivatives, secondDerivatives, errors, iterations);
            }

            xValues.add(currentX);
            functionValues.add(currentValue);
            firstDerivatives.add(currentFirstDerivative);
            secondDerivatives.add(currentSecondDerivative);
            iterations.add(iterationCount);

            // Calculate the error based on the error type
            error = errorType == 1 ? Math.abs(xValues.get(iterationCount) - xValues.get(iterationCount - 1))
                    : Math.abs((xValues.get(iterationCount) - xValues.get(iterationCount - 1)) / xValues.get(iterationCount));
            errors.add(error);

            // Break the loop if the root is found or tolerance is met
            if (currentValue == 0 || error < tolerance) {
                break;
            }
        }

        // Ensure a final iteration if the error tolerance was not met exactly
        if (error >= tolerance && iterationCount < maxIterations) {
            iterationCount++;
            currentX = currentX - (currentValue * currentFirstDerivative) / (currentFirstDerivative * currentFirstDerivative - currentValue * currentSecondDerivative);
            x.setArgumentValue(currentX);
            currentValue = function.calculate();
            currentFirstDerivative = firstDerivative.calculate();
            currentSecondDerivative = secondDerivative.calculate();

            xValues.add(currentX);
            functionValues.add(currentValue);
            firstDerivatives.add(currentFirstDerivative);
            secondDerivatives.add(currentSecondDerivative);
            iterations.add(iterationCount);

            // Calculate the error based on the error type
            error = errorType == 1 ? Math.abs(xValues.get(iterationCount) - xValues.get(iterationCount - 1))
                    : Math.abs((xValues.get(iterationCount) - xValues.get(iterationCount - 1)) / xValues.get(iterationCount));
            errors.add(error);
        }

        // Apply precision formatting
        List<Double> formattedXValues = new ArrayList<>();
        for (double xValue : xValues) {
            formattedXValues.add(precisionType == 1 ? roundSignificantFigures(xValue, (int) toleranceValue) : roundDecimalPlaces(xValue, (int) toleranceValue));
        }

        // Determine the result message
        String message = currentValue == 0 ? currentX + " is a root of f(x)"
                : errors.get(iterationCount) < tolerance ? "The approximate solution is: " + currentX + ", with a tolerance = " + tolerance
                : "Failed in " + maxIterations + " iterations";
        return new MultipleRootsResponse(message, formattedXValues, functionValues, firstDerivatives, secondDerivatives, errors, iterations);
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