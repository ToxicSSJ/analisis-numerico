package com.numetrify.util;

/**
 * Utility class for mathematical operations.
 */
public class MathUtils {

    /**
     * Calculates the tolerance value based on the error type and tolerance value.
     *
     * @param toleranceValue the value of tolerance
     * @param errorType the type of error (1 for absolute error, 2 for relative error)
     * @return the calculated tolerance
     *
     * Example usage:
     * <pre>
     * {@code
     * double toleranceValue = 0.01;
     * int errorType = 1;
     * double tolerance = MathUtils.getTolerance(toleranceValue, errorType);
     * }
     * </pre>
     */
    public static double getTolerance(double toleranceValue, int errorType) {
        return errorType == 1 ? 0.5 * Math.pow(10, -toleranceValue) : 5 * Math.pow(10, -toleranceValue);
    }

}