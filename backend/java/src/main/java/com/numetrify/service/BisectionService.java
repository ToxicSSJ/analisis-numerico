package com.numetrify.service;

import org.mariuszgromada.math.mxparser.Function;
import org.springframework.stereotype.Service;
import com.numetrify.dto.BisectionResponse;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
    public BisectionResponse bisection(String functionExpression, double lowerBound, double upperBound, int errorType, double toleranceValue, int maxIterations) {
        Function function = new Function("f(x) = " + functionExpression);

        double functionAtLowerBound = function.calculate(lowerBound);
        double functionAtUpperBound = function.calculate(upperBound);
        BigDecimal tolerance = BigDecimal.valueOf(0.5).divide(BigDecimal.TEN.pow((int) toleranceValue), 20, RoundingMode.HALF_UP);

        if (functionAtLowerBound == 0) {
            return new BisectionResponse(lowerBound + " is a root of f(x)", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        } else if (functionAtUpperBound == 0) {
            return new BisectionResponse(upperBound + " is a root of f(x)", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        } else if (functionAtLowerBound * functionAtUpperBound > 0) {
            return new BisectionResponse("The interval is inadequate", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        List<BigDecimal> xValues = new ArrayList<>();
        List<Double> functionValues = new ArrayList<>();
        List<BigDecimal> errors = new ArrayList<>();
        List<Integer> iterations = new ArrayList<>();

        int iterationCount = 0;
        BigDecimal lower = BigDecimal.valueOf(lowerBound);
        BigDecimal upper = BigDecimal.valueOf(upperBound);
        BigDecimal midPoint = lower.add(upper).divide(BigDecimal.valueOf(2), 20, RoundingMode.HALF_UP);
        double functionAtMidPoint = function.calculate(midPoint.doubleValue());
        xValues.add(midPoint);
        functionValues.add(functionAtMidPoint);
        errors.add(BigDecimal.valueOf(100.0)); // Initial error set to 100%
        iterations.add(iterationCount);

        BigDecimal previousMidPoint = midPoint;

        while (iterationCount < maxIterations) {
            iterationCount++;

            double tempX = function.calculate(lower.doubleValue()) * functionAtMidPoint;

            if (tempX < 0 || tempX == 0) {
                upper = midPoint;
            } else {
                lower = midPoint;
            }

            midPoint = lower.add(upper).divide(BigDecimal.valueOf(2), 20, RoundingMode.HALF_UP);
            functionAtMidPoint = function.calculate(midPoint.doubleValue());

            BigDecimal error = errorType == 1
                    ? midPoint.subtract(previousMidPoint).abs()
                    : midPoint.subtract(previousMidPoint).abs().divide(midPoint.abs(), 20, RoundingMode.HALF_UP);
            xValues.add(midPoint);
            functionValues.add(functionAtMidPoint);
            errors.add(error);
            iterations.add(iterationCount);

            if (error.compareTo(tolerance) < 0) {
                break;
            }

            previousMidPoint = midPoint;
        }

        String message = functionAtMidPoint == 0 ? midPoint + " is a root of f(x)"
                : errors.get(iterationCount).compareTo(tolerance) < 0 ? "The approximate solution is: " + midPoint + ", with a tolerance = " + tolerance
                : "Failed in " + maxIterations + " iterations";

        return new BisectionResponse(message, xValues, functionValues, errors, iterations);
    }
}