package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BisectionResponse {

    private String message;
    private List<Double> xVals;
    private List<Double> fVals;
    private List<Double> errors;
    private List<Integer> iterations;

}
