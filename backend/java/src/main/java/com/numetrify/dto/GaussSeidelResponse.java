package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GaussSeidelResponse {
    private String message;
    private List<double[]> xValues;
    private List<Double> errors;
}