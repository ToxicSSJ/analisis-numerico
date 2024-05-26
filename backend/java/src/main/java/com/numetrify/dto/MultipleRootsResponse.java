package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MultipleRootsResponse {
    private String message;
    private List<Double> xValues;
    private List<Double> functionValues;
    private List<Double> firstDerivatives;
    private List<Double> secondDerivatives;
    private List<Double> errors;
    private List<Integer> iterations;
}