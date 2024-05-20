package com.numetrify.service;

import org.mariuszgromada.math.mxparser.Function;
import org.springframework.stereotype.Service;
import com.numetrify.dto.BisectionResponse;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

@Service
public class BisectionService {

    @SneakyThrows
    public BisectionResponse bisection(String functionExpression, double lowerBound, double upperBound, int errorType, double toleranceValue, int maxIterations) {
        // Create the function using the provided expression
        Function function = new Function("f(x) = " + functionExpression);

        // Calculate function values at the bounds
        double functionAtLowerBound = function.calculate(lowerBound);
        double functionAtUpperBound = function.calculate(upperBound);
        double tolerance = errorType == 1 ? 0.5 * Math.pow(10, -toleranceValue) : 5 * Math.pow(10, -toleranceValue);

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
        return new BisectionResponse(message, xValues, functionValues, errors, iterations);
    }
}