package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CholeskyResponse {
    private final double[] solution;
    private final double[][] L;
    private final double[][] U;
}