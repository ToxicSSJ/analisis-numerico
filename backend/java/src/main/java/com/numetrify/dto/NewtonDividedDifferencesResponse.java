package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewtonDividedDifferencesResponse {
    private String message;
    private double[] coefficients;
    private String polynomial;
}