package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LUDecompositionResponse {
    private double[] solution;
    private double[][] L;
    private double[][] U;
}