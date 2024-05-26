package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IncrementalSearchResponse {
    private double a;
    private double b;
    private int iterations;
    private List<Double> xValues;
    private List<Double> functionValues;
}