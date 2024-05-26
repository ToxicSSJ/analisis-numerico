package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VandermondeResponse {
    private String message;
    private double[] coefficients;
    private String polynomial;
}