package com.numetrify.service;

import com.numetrify.dto.IncrementalSearchResponse;
import lombok.SneakyThrows;
import org.mariuszgromada.math.mxparser.Function;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class to perform Incremental Search for root finding.
 */
@Service
public class IncrementalSearchService {

    /**
     * Performs Incremental Search to find intervals where the function changes sign, indicating the presence of a root.
     *
     * @param functionExpression the expression of the function
     * @param x0 the initial value of x
     * @param h the increment step
     * @param Nmax the maximum number of iterations
     * @param errorType the type of error to use (1 for absolute error, 2 for relative error)
     * @param toleranceValue the tolerance value for the stopping criterion
     * @return IncrementalSearchResponse containing the result of the Incremental Search method
     *
     * Example usage:
     * <pre>
     * {@code
     * String functionExpression = "x^3 - x - 2";
     * double x0 = 1.0;
     * double h = 0.1;
     * int Nmax = 100;
     * int errorType = 1;
     * double toleranceValue = 0.01;
     * IncrementalSearchResponse response = incrementalSearchService.incrementalSearch(functionExpression, x0, h, Nmax, precisionType, errorType, toleranceValue);
     * double xPrevious = response.getXPrevious();
     * double xCurrent = response.getXCurrent();
     * int iterations = response.getIterations();
     * List<Double> xValues = response.getXValues();
     * List<Double> functionValues = response.getFunctionValues();
     * List<Double> errors = response.getErrors();
     * }
     * </pre>
     */
    @SneakyThrows
    public IncrementalSearchResponse incrementalSearch(String functionExpression, double x0, double h, int Nmax, int errorType, double toleranceValue) {
        // Create the function using the provided expression
        Function function = new Function("f(x) = " + functionExpression);

        // Initialize
        double xPrevious = x0;
        double fPrevious = function.calculate(xPrevious);
        double xCurrent = xPrevious + h;
        double fCurrent = function.calculate(xCurrent);
        int iterations = 0;
        double tolerance = 0.5 * Math.pow(10, -toleranceValue);

        // Lists to store the values
        List<Double> xValues = new ArrayList<>();
        List<Double> functionValues = new ArrayList<>();
        List<Double> errors = new ArrayList<>();
        xValues.add(xPrevious);
        functionValues.add(fPrevious);
        errors.add(100.0); // Initial error set to 100%

        // Loop
        for (iterations = 1; iterations <= Nmax; iterations++) {
            if (fPrevious * fCurrent < 0 || errors.get(iterations - 1) < tolerance) {
                break;
            }
            xPrevious = xCurrent;
            fPrevious = fCurrent;
            xCurrent = xPrevious + h;
            fCurrent = function.calculate(xCurrent);
            xValues.add(xPrevious);
            functionValues.add(fPrevious);

            // Calculate the error based on the error type
            double error = errorType == 1 ? Math.abs(xCurrent - xPrevious)
                    : Math.abs((xCurrent - xPrevious) / xCurrent);
            errors.add(error);
        }

        // Result
        return new IncrementalSearchResponse(xPrevious, xCurrent, iterations, xValues, functionValues, errors);
    }
}