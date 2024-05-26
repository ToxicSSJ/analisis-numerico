package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SecantResponse {
    private String message;
    private List<Double> xValues;
    private List<Double> functionValues;
    private List<Double> errors;
    private List<Integer> iterations;
}