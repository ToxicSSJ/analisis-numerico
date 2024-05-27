package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class BisectionResponse {
    private String message;
    private List<BigDecimal> xVals;
    private List<Double> fVals;
    private List<BigDecimal> errors;
    private List<Integer> iterations;
}
