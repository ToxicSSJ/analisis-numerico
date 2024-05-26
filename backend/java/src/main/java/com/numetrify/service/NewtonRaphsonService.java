package com.numetrify.service;

import com.numetrify.dto.NewtonRaphsonResponse;
import lombok.SneakyThrows;
import org.mariuszgromada.math.mxparser.Argument;
import org.mariuszgromada.math.mxparser.Function;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class to perform the Newton-Raphson method for root finding.
 */
@Service
public class NewtonRaphsonService {

    /**
     * Performs the Newton-Raphson method to find a root of the given function.
     *
     * @param functionExpression the expression of the function
     * @param initialGuess the initial guess for the root
     * @param errorType the type of error to use (1 for absolute error, 2 for relative error)
     * @param precisionType the type of precision to use (1 for significant figures, 2 for decimal places)
     * @param toleranceValue the tolerance value for the stopping criterion
     * @param maxIterations the maximum number of iterations
     * @return NewtonRaphsonResponse containing the result of the Newton-Raphson method
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
     * NewtonRaphsonResponse response = newtonRaphsonService.newtonRaphson(functionExpression, initialGuess, errorType, precisionType, toleranceValue, maxIterations);
     * String message = response.getMessage();
     * List<Double> xValues = response.getXValues();
     * List<Double> functionValues = response.getFunctionValues();
     * List<Double> derivatives = response.getDerivatives();
     * List<Double> errors = response.getErrors();
     * List<Integer> iterations = response.getIterations();
     * }
     * </pre>
     */
    @SneakyThrows
    public NewtonRaphsonResponse newtonRaphson(String functionExpression, double initialGuess, int errorType, int precisionType, double toleranceValue, int maxIterations) {
        // Define the function
        Function function = new Function("f(x) = " + functionExpression);
        // Define the argument for x
        Argument x = new Argument("x = " + initialGuess);

        // Ensure the function is valid
        if (!function.checkSyntax()) {
            String message = "Invalid function syntax.";
            return new NewtonRaphsonResponse(message, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        // Calculate tolerance based on the type of error
        double tolerance = errorType == 1 ? 0.5 * Math.pow(10, -toleranceValue) : 5 * Math.pow(10, -toleranceValue);

        // Initialize lists to store the values of x, f(x), f'(x), errors, and iterations
        List<Double> xValues = new ArrayList<>();
        List<Double> functionValues = new ArrayList<>();
        List<Double> derivatives = new ArrayList<>();
        List<Double> errors = new ArrayList<>();
        List<Integer> iterations = new ArrayList<>();

        // Initial values
        double currentX = initialGuess;
        double currentFunctionValue = function.calculate(currentX);
        double currentDerivativeValue = calculateDerivative(function, currentX);
        int iterationCount = 0;
        double error = 100.0; // Initial error set to 100%
        xValues.add(currentX);
        functionValues.add(currentFunctionValue);
        derivatives.add(currentDerivativeValue);
        errors.add(error);
        iterations.add(iterationCount);

        // Check if the initial guess is valid
        if (Double.isNaN(currentFunctionValue) || Double.isNaN(currentDerivativeValue) || Double.isInfinite(currentDerivativeValue)) {
            String message = "The function is not defined or differentiable at x = " + initialGuess + ". The method fails.";
            return new NewtonRaphsonResponse(message, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        // Perform the Newton-Raphson method
        while (error >= tolerance && currentFunctionValue != 0 && currentDerivativeValue != 0 && iterationCount < maxIterations) {
            iterationCount++;
            currentX = currentX - (currentFunctionValue / currentDerivativeValue);
            x.setArgumentValue(currentX);
            currentFunctionValue = function.calculate(currentX);
            currentDerivativeValue = calculateDerivative(function, currentX);

            // Check if the current value is valid
            if (Double.isNaN(currentFunctionValue) || Double.isNaN(currentDerivativeValue) || Double.isInfinite(currentDerivativeValue)) {
                String message = "The function is not defined or differentiable at x = " + currentX + ". The method fails.";
                return new NewtonRaphsonResponse(message, xValues, functionValues, derivatives, errors, iterations);
            }

            xValues.add(currentX);
            functionValues.add(currentFunctionValue);
            derivatives.add(currentDerivativeValue);
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
        String message = currentFunctionValue == 0 ? currentX + " is a root of f(x)"
                : errors.get(iterationCount) < tolerance ? "The approximate solution is: " + currentX + ", with a tolerance = " + tolerance
                : "Failed in " + maxIterations + " iterations";
        return new NewtonRaphsonResponse(message, formattedXValues, functionValues, derivatives, errors, iterations);
    }

    /**
     * Calculates the numerical derivative of the function at a given point.
     *
     * @param function the function to differentiate
     * @param x the point at which to calculate the derivative
     * @return the numerical derivative value
     */
    private double calculateDerivative(Function function, double x) {
        double h = 1e-7; // A small step size
        double f_x_h = function.calculate(x + h);
        double f_x = function.calculate(x);
        return (f_x_h - f_x) / h;
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