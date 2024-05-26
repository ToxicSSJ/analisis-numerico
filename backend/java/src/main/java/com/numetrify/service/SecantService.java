package com.numetrify.service;

import com.numetrify.dto.SecantResponse;
import com.numetrify.util.MathUtils;
import lombok.SneakyThrows;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Expression;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class to perform the Secant method for root finding.
 */
@Service
public class SecantService {

    /**
     * Performs the Secant method to find a root of the given function.
     *
     * @param functionExpression the expression of the function
     * @param initialGuess1 the first initial guess for the root
     * @param initialGuess2 the second initial guess for the root
     * @param errorType the type of error to use (1 for absolute error, 2 for relative error)
     * @param precisionType the type of precision to use (1 for significant figures, 2 for decimal places)
     * @param toleranceValue the tolerance value for the stopping criterion
     * @param maxIterations the maximum number of iterations
     * @return SecantResponse containing the result of the Secant method
     *
     * Example usage:
     * <pre>
     * {@code
     * String functionExpression = "x^3 - x - 2";
     * double initialGuess1 = 1.0;
     * double initialGuess2 = 2.0;
     * int errorType = 1;
     * int precisionType = 1;
     * double toleranceValue = 0.01;
     * int maxIterations = 100;
     * SecantResponse response = secantService.secant(functionExpression, initialGuess1, initialGuess2, errorType, precisionType, toleranceValue, maxIterations);
     * String message = response.getMessage();
     * List<Double> xValues = response.getXValues();
     * List<Double> functionValues = response.getFunctionValues();
     * List<Double> errors = response.getErrors();
     * List<Integer> iterations = response.getIterations();
     * }
     * </pre>
     */
    @SneakyThrows
    public SecantResponse secant(String functionExpression, double initialGuess1, double initialGuess2, int errorType, int precisionType, double toleranceValue, int maxIterations) {
        // Create the function using mXparser
        Argument x = new Argument("x");
        Expression function = new Expression(functionExpression, x);

        // Calculate tolerance based on the type of error
        double tolerance = MathUtils.getTolerance(toleranceValue, errorType);

        // Initialize lists to store the values of x, f(x), errors, and iterations
        List<Double> xValues = new ArrayList<>();
        List<Double> functionValues = new ArrayList<>();
        List<Double> errors = new ArrayList<>();
        List<Integer> iterations = new ArrayList<>();

        // Initial values
        double x0 = initialGuess1;
        double x1 = initialGuess2;
        x.setArgumentValue(x0);
        double f0 = function.calculate();
        x.setArgumentValue(x1);
        double f1 = function.calculate();
        int iterationCount = 0;
        double error = 100.0; // Initial error set to 100%
        xValues.add(x0);
        xValues.add(x1);
        functionValues.add(f0);
        functionValues.add(f1);
        errors.add(error);
        errors.add(Math.abs(x1 - x0));
        iterations.add(0);
        iterations.add(1);

        // Check if the initial guesses are valid
        if (Double.isNaN(f0) || Double.isNaN(f1) || Double.isInfinite(f0) || Double.isInfinite(f1)) {
            String message = "The function is not defined at x0 = " + initialGuess1 + " or x1 = " + initialGuess2 + ". The method fails.";
            return new SecantResponse(message, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        // Perform the secant method
        while (iterationCount < maxIterations && errors.get(iterationCount + 1) >= tolerance && f1 != 0 && f1 - f0 != 0) {
            iterationCount++;
            double x2 = x1 - (f1 * (x1 - x0)) / (f1 - f0);
            x0 = x1;
            f0 = f1;
            x1 = x2;
            x.setArgumentValue(x1);
            f1 = function.calculate();

            // Check if the current value is valid
            if (Double.isNaN(f1) || Double.isInfinite(f1)) {
                String message = "The function is not defined at x = " + x1 + ". The method fails.";
                return new SecantResponse(message, xValues, functionValues, errors, iterations);
            }

            xValues.add(x1);
            functionValues.add(f1);
            iterations.add(iterationCount);

            // Calculate the error based on the error type
            error = errorType == 1 ? Math.abs(x1 - x0)
                    : Math.abs((x1 - x0) / x1);
            errors.add(error);
        }

        // Apply precision formatting
        List<Double> formattedXValues = new ArrayList<>();
        for (double xValue : xValues) {
            formattedXValues.add(precisionType == 1 ? roundSignificantFigures(xValue, (int) toleranceValue) : roundDecimalPlaces(xValue, (int) toleranceValue));
        }

        // Determine the result message
        String message = f1 == 0 ? x1 + " is a root of f(x)"
                : errors.get(iterationCount + 1) < tolerance ? "The approximate solution is: " + x1 + ", with a tolerance = " + tolerance
                : "Failed in " + maxIterations + " iterations";
        return new SecantResponse(message, formattedXValues, functionValues, errors, iterations);
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