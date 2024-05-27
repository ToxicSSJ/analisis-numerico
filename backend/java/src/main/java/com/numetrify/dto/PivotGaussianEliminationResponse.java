package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class PivotGaussianEliminationResponse {
    private final String message;
    private final double[] solution;
}