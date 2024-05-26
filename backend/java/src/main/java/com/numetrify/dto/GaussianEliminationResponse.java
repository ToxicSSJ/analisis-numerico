package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GaussianEliminationResponse {
    private final double[] solution;
}