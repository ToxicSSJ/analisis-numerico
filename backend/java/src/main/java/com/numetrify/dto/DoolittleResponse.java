package com.numetrify.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoolittleResponse {
    private double[] solution;
    private double[][] L;
    private double[][] U;
}