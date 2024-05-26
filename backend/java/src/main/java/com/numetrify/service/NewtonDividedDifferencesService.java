package com.numetrify.service;

import com.numetrify.dto.NewtonDividedDifferencesResponse;
import org.springframework.stereotype.Service;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.analysis.interpolation.DividedDifferenceInterpolator;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

/**
 * Service class to perform Newton's Divided Differences interpolation.
 */
@Service
public class NewtonDividedDifferencesService {

    /**
     * Performs Newton's Divided Differences interpolation on the given points.
     *
     * @param xValues the x values of the points
     * @param yValues the y values of the points
     * @return NewtonDividedDifferencesResponse containing the resulting polynomial
     *
     * Example usage:
     * <pre>
     * {@code
     * double[] xValues = {1, 2, 3};
     * double[] yValues = {1, 4, 9};
     * NewtonDividedDifferencesResponse response = newtonDividedDifferencesService.newtonDividedDifferences(xValues, yValues);
     * String message = response.getMessage();
     * double[] coefficients = response.getCoefficients();
     * String polynomialString = response.getPolynomialString();
     * }
     * </pre>
     */
    public NewtonDividedDifferencesResponse newtonDividedDifferences(double[] xValues, double[] yValues) {
        if (xValues.length != yValues.length) {
            return new NewtonDividedDifferencesResponse("X and Y must have the same number of elements", null, null);
        }

        if (hasDuplicates(xValues)) {
            return new NewtonDividedDifferencesResponse("X values must not repeat", null, null);
        }

        try {
            DividedDifferenceInterpolator interpolator = new DividedDifferenceInterpolator();
            PolynomialFunction polynomial = new PolynomialFunction(interpolator.interpolate(xValues, yValues).getCoefficients());

            String polynomialString = polynomialToString(polynomial);

            return new NewtonDividedDifferencesResponse("The polynomial that interpolates the given points is: " + polynomialString, polynomial.getCoefficients(), polynomialString);
        } catch (MathIllegalArgumentException e) {
            return new NewtonDividedDifferencesResponse("Interpolation failed: " + e.getMessage(), null, null);
        }
    }

    /**
     * Checks if the given array has duplicate values.
     *
     * @param array the array to check
     * @return true if there are duplicate values, false otherwise
     */
    private boolean hasDuplicates(double[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] == array[i + 1]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts a polynomial function to its string representation.
     *
     * @param polynomial the polynomial function
     * @return the string representation of the polynomial
     */
    private String polynomialToString(PolynomialFunction polynomial) {
        double[] coefficients = polynomial.getCoefficients();
        StringBuilder sb = new StringBuilder();
        int n = coefficients.length;
        for (int i = n - 1; i >= 0; i--) {
            double coef = coefficients[i];
            if (coef != 0) {
                if (i < n - 1 && coef > 0) {
                    sb.append(" + ");
                } else if (coef < 0) {
                    sb.append(" - ");
                }
                sb.append(Math.abs(coef));
                if (i > 0) {
                    sb.append(" x");
                    if (i > 1) {
                        sb.append("^").append(i);
                    }
                }
            }
        }
        return sb.toString();
    }
}