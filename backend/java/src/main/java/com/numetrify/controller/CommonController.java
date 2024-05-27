package com.numetrify.controller;

import com.numetrify.dto.*;
import com.numetrify.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class CommonController {

    @Autowired
    private BisectionService bisectionService;

    @Autowired
    private FalseRuleService falseRuleService;

    @Autowired
    private FixedPointService fixedPointService;

    @Autowired
    private NewtonRaphsonService newtonRaphsonService;

    @Autowired
    private MultipleRootsService multipleRootsService;

    @Autowired
    private SecantService secantService;

    @Autowired
    private JacobiService jacobiService;

    @Autowired
    private GaussSeidelService gaussSeidelService;

    @Autowired
    private IncrementalSearchService incrementalSearchService;

    @Autowired
    private SimpleGaussianEliminationService simpleGaussianEliminationService;

    @Autowired
    private PivotGaussianEliminationService pivotGaussianEliminationService;

    @Autowired
    private CroutService croutService;

    @Autowired
    private CholeskyService choleskyService;

    @Autowired
    private DoolittleService doolittleService;

    @Autowired
    private LUGaussianEliminationService luGaussianEliminationService;

    @Operation(summary = "Performs the bisection method", description = "Calculates the root of a function using the bisection method.")
    @PostMapping("/bisection")
    public BisectionResponse bisection(
            @RequestParam String function,
            @RequestParam double a,
            @RequestParam double b,
            @RequestParam int errorType,
            @RequestParam double toleranceValue,
            @RequestParam int maxIterations) {
        return bisectionService.bisection(function, a, b, errorType, toleranceValue, maxIterations);
    }

    @Operation(summary = "Performs the false rule method", description = "Calculates the root of a function using the false rule method.")
    @PostMapping("/false-rule")
    public FalseRuleResponse falseRule(
            @RequestParam String function,
            @RequestParam double lowerBound,
            @RequestParam double upperBound,
            @RequestParam int errorType,
            @RequestParam double toleranceValue,
            @RequestParam int maxIterations) {
        return falseRuleService.falseRule(function, lowerBound, upperBound, errorType, toleranceValue, maxIterations);
    }

    @Operation(summary = "Performs the fixed point method", description = "Calculates the root of a function using the fixed point method.")
    @PostMapping("/fixed-point")
    public FixedPointResponse fixedPoint(
            @RequestParam String function,
            @RequestParam String gFunction,
            @RequestParam double initialGuess,
            @RequestParam int errorType,
            @RequestParam double toleranceValue,
            @RequestParam int maxIterations) {
        return fixedPointService.fixedPoint(function, gFunction, initialGuess, errorType, toleranceValue, maxIterations);
    }

    @Operation(summary = "Performs incremental search", description = "Searches for roots of a function using the incremental search method.")
    @PostMapping("/incremental-search")
    public IncrementalSearchResponse incrementalSearch(
            @RequestParam String function,
            @RequestParam double x0,
            @RequestParam double h,
            @RequestParam int maxIterations,
            @RequestParam int errorType,
            @RequestParam double toleranceValue) {
        return incrementalSearchService.incrementalSearch(function, x0, h, maxIterations, errorType, toleranceValue);
    }

    @Operation(summary = "Performs the multiple roots method", description = "Calculates the root of a function using the multiple roots method.")
    @PostMapping("/multiple-roots")
    public MultipleRootsResponse multipleRoots(
            @RequestParam String function,
            @RequestParam double initialGuess,
            @RequestParam int errorType,
            @RequestParam double toleranceValue,
            @RequestParam int maxIterations) {
        return multipleRootsService.multipleRoots(function, initialGuess, errorType, toleranceValue, maxIterations);
    }

    @Operation(summary = "Performs the Newton-Raphson method", description = "Calculates the root of a function using the Newton-Raphson method.")
    @PostMapping("/newton-raphson")
    public NewtonRaphsonResponse newtonRaphson(
            @RequestParam String function,
            @RequestParam double initialGuess,
            @RequestParam int errorType,
            @RequestParam double toleranceValue,
            @RequestParam int maxIterations) {
        return newtonRaphsonService.newtonRaphson(function, initialGuess, errorType, toleranceValue, maxIterations);
    }

    @Operation(summary = "Performs the secant method", description = "Calculates the root of a function using the secant method.")
    @PostMapping("/secant")
    public SecantResponse secant(
            @RequestParam String function,
            @RequestParam double initialGuess1,
            @RequestParam double initialGuess2,
            @RequestParam int errorType,
            @RequestParam double toleranceValue,
            @RequestParam int maxIterations) {
        return secantService.secant(function, initialGuess1, initialGuess2, errorType, toleranceValue, maxIterations);
    }

    @Operation(summary = "Solves a system of linear equations using the Jacobi method", description = "Solves a system of linear equations using the Jacobi iterative method.")
    @PostMapping("/jacobi")
    public JacobiResponse jacobi(
            @RequestParam int size,
            @RequestParam String matrix,
            @RequestParam String b,
            @RequestParam String x0,
            @RequestParam int errorType,
            @RequestParam double toleranceValue,
            @RequestParam int maxIterations,
            @RequestParam String norm) {
        return jacobiService.jacobi(size, matrix, b, x0, errorType, toleranceValue, maxIterations, norm);
    }

    @Operation(summary = "Solves a system of linear equations using the Gauss-Seidel method", description = "Solves a system of linear equations using the Gauss-Seidel iterative method.")
    @PostMapping("/gauss-seidel")
    public GaussSeidelResponse gaussSeidel(
            @RequestParam int size,
            @RequestParam String matrixData,
            @RequestParam String b,
            @RequestParam String x0,
            @RequestParam int errorType,
            @RequestParam double toleranceValue,
            @RequestParam int maxIterations,
            @RequestParam String norm) {
        return gaussSeidelService.gaussSeidel(size, matrixData, b, x0, errorType, toleranceValue, maxIterations, norm);
    }

    @Operation(summary = "Solves a system using Simple Gaussian Elimination", description = "Solves a system of linear equations using the Simple Gaussian Elimination method.")
    @PostMapping("/simple-gaussian-elimination")
    public SimpleGaussianEliminationResponse simpleGaussianElimination(
            @RequestParam String matrixA,
            @RequestParam String vectorB) {
        return simpleGaussianEliminationService.simpleGaussianElimination(parseMatrix(matrixA), parseVector(vectorB));
    }

    @Operation(summary = "Solves a system using Pivot Gaussian Elimination", description = "Solves a system of linear equations using the Pivot Gaussian Elimination method.")
    @PostMapping("/pivot-gaussian-elimination")
    public PivotGaussianEliminationResponse pivotGaussianElimination(
            @RequestParam String matrixA,
            @RequestParam String vectorB) {
        return pivotGaussianEliminationService.pivotGaussianElimination(parseMatrix(matrixA), parseVector(vectorB));
    }

    @Operation(summary = "Performs LU Gaussian Elimination", description = "Solves a system of linear equations using the LU Gaussian Elimination method.")
    @PostMapping("/lu-gaussian-elimination")
    public LUGaussianEliminationResponse luDecomposition(
            @RequestParam String matrixA,
            @RequestParam String vectorB) {
        double[][] A = parseMatrix(matrixA);
        double[] b = parseVector(vectorB);
        return luGaussianEliminationService.luGaussianElimination(A, b);
    }

    @Operation(summary = "Solves a system using the Crout method", description = "Solves a system of linear equations using the Crout decomposition method.")
    @PostMapping("/crout")
    public CroutResponse crout(
            @RequestParam String matrixData,
            @RequestParam String vectorData) {
        return croutService.crout(parseMatrix(matrixData), parseVector(vectorData));
    }

    @Operation(summary = "Solves a system using the Cholesky method", description = "Solves a system of linear equations using the Cholesky decomposition method.")
    @PostMapping("/cholesky")
    public CholeskyResponse cholesky(
            @RequestParam String matrix,
            @RequestParam String vector) {
        double[][] A = parseMatrix(matrix);
        double[] B = parseVector(vector);
        return choleskyService.cholesky(A, B);
    }

    @Operation(summary = "Solves a system using the Doolittle method", description = "Solves a system of linear equations using the Doolittle decomposition method.")
    @PostMapping("/doolittle")
    public DoolittleResponse doolittle(
            @RequestParam String matrixData,
            @RequestParam String bData) {
        double[][] matrix = parseMatrix(matrixData);
        double[] b = parseVector(bData);
        return doolittleService.doolittle(matrix, b);
    }

    private double[][] parseMatrix(String matrixText) {
        String[] rows = matrixText.split(";");
        int n = rows.length;
        String[] firstRowElements = rows[0].trim().split(" ");
        int m = firstRowElements.length;
        double[][] matrix = new double[n][m];

        for (int i = 0; i < n; i++) {
            String[] elements = rows[i].trim().split(" ");
            if (elements.length != m) {
                throw new IllegalArgumentException("Error: all rows must have the same number of columns.");
            }
            for (int j = 0; j < m; j++) {
                matrix[i][j] = Double.parseDouble(elements[j]);
            }
        }
        return matrix;
    }

    private double[] parseVector(String vectorText) {
        String[] elements = vectorText.trim().split(" ");
        int n = elements.length;
        double[] vector = new double[n];
        for (int i = 0; i < n; i++) {
            vector[i] = Double.parseDouble(elements[i]);
        }
        return vector;
    }

}