from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt

from methods.bisection import bisection
from methods.falserule import false_rule
from methods.fixedpoint import fixed_point
from methods.falserule import false_rule
from methods.incrementalsearch import incremental_search
from methods.multipleroots import multiple_roots
from methods.newtonraphson import newton_raphson
from methods.secant import secant

from methods.cholesky import cholesky
from methods.crout import crout
from methods.doolittle import doolittle
from methods.simplegaussianelimination import simple_gaussian_elimination
from methods.pivotgaussianelimination import pivot_gaussian_elimination
from methods.lugaussianelimination import lu_gaussian_elimination
from methods.jacobi import jacobi
from methods.gaussseidel import gauss_seidel

import numpy as np
import re

def convert_expression(expression):
    expression = re.sub(r'(\d)([a-zA-Z])', r'\1*\2', expression)
    expression = re.sub(r'\^', r'**', expression)
    return expression

def parse_matrix(matrix_str):
    return np.array([[float(num) for num in row.split()] for row in matrix_str.split(';')])

def parse_vector(vector_str):
    return np.array([float(num) for num in vector_str.split()])

@csrf_exempt
def bisection_method(request):
    if request.method == 'POST':
        function = convert_expression(request.GET.get('function'))
        lower_bound = float(request.GET.get('a'))
        upper_bound = float(request.GET.get('b'))
        error_type = int(request.GET.get('errorType'))
        tolerance_value = float(request.GET.get('toleranceValue'))
        max_iterations = int(request.GET.get('maxIterations'))
        
        result = bisection(function, lower_bound, upper_bound, error_type, tolerance_value, max_iterations)
        
        result['xvals'] = [float(x) for x in result['xvals']]
        result['fvals'] = [float(f) for f in result['fvals']]
        result['errors'] = [float(e) for e in result['errors']]
        
        return JsonResponse(result, safe=False)

@csrf_exempt
def false_rule_method(request):
    if request.method == 'POST':
        function = convert_expression(request.GET.get('function'))
        lower_bound = request.GET.get('lowerBound')
        upper_bound = request.GET.get('upperBound')
        error_type = request.GET.get('errorType')
        tolerance_value = request.GET.get('toleranceValue')
        max_iterations = request.GET.get('maxIterations')

        print(function, lower_bound, upper_bound, error_type, tolerance_value, max_iterations)

        if not all([function, lower_bound, upper_bound, error_type, tolerance_value, max_iterations]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        try:
            lower_bound = float(lower_bound)
            upper_bound = float(upper_bound)
            error_type = int(error_type)
            tolerance_value = float(tolerance_value)
            max_iterations = int(max_iterations)
        except ValueError as e:
            return JsonResponse({"error": f"Invalid parameter type: {str(e)}"}, status=400)

        try:
            result = false_rule(function, lower_bound, upper_bound, error_type, tolerance_value, max_iterations)

            result['xvalues'] = [float(x) for x in result.get('xvalues', [])]
            result['functionValues'] = [float(f) for f in result.get('functionValues', [])]  # Cambio aquí
            result['errors'] = [float(e) for e in result.get('errors', [])]
        except Exception as e:
            return JsonResponse({"error": f"Error in false_rule method: {str(e)}"}, status=500)

        return JsonResponse(result, safe=False)
    return JsonResponse({"error": "Invalid HTTP method"}, status=405)

@csrf_exempt
def fixed_point_method(request):
    if request.method == 'POST':
        function = convert_expression(request.GET.get('function'))
        g_function = request.GET.get('gFunction')
        initial_guess = request.GET.get('initialGuess')
        error_type = request.GET.get('errorType')
        tolerance_value = request.GET.get('toleranceValue')
        max_iterations = request.GET.get('maxIterations')

        if not all([function, g_function, initial_guess, error_type, tolerance_value, max_iterations]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        initial_guess = float(initial_guess)
        error_type = int(error_type)
        tolerance_value = float(tolerance_value)
        max_iterations = int(max_iterations)
        
        result = fixed_point(function, g_function, initial_guess, error_type, tolerance_value, max_iterations)
        
        result['xvalues'] = [float(x) for x in result['xvalues']]
        result['functionValues'] = [float(f) for f in result['functionValues']]
        result['errors'] = [float(e) for e in result['errors']]
        
        return JsonResponse(result, safe=False)
    
@csrf_exempt
def incremental_search_method(request):
    if request.method == 'POST':
        function = convert_expression(request.GET.get('function'))
        x0 = request.GET.get('x0')
        h = request.GET.get('h')
        max_iterations = request.GET.get('maxIterations')
        error_type = request.GET.get('errorType')
        tolerance_value = request.GET.get('toleranceValue')

        if not all([function, x0, h, max_iterations, error_type, tolerance_value]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        x0 = float(x0)
        h = float(h)
        max_iterations = int(max_iterations)
        error_type = int(error_type)
        tolerance_value = float(tolerance_value)
        
        result = incremental_search(function, x0, h, max_iterations, error_type, tolerance_value)
        
        result['xvalues'] = [float(x) for x in result['xvalues']]
        result['functionValues'] = [float(f) for f in result['functionValues']]
        result['errors'] = [float(e) for e in result['errors']]
        
        return JsonResponse(result, safe=False)

@csrf_exempt
def multiple_roots_method(request):
    if request.method == 'POST':
        function = convert_expression(request.GET.get('function'))
        initial_guess = request.GET.get('initialGuess')
        error_type = request.GET.get('errorType')
        tolerance_value = request.GET.get('toleranceValue')
        max_iterations = request.GET.get('maxIterations')

        if not all([function, initial_guess, error_type, tolerance_value, max_iterations]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        initial_guess = float(initial_guess)
        error_type = int(error_type)
        tolerance_value = float(tolerance_value)
        max_iterations = int(max_iterations)
        
        result = multiple_roots(function, initial_guess, error_type, tolerance_value, max_iterations)
        
        result['xvalues'] = [float(x) for x in result['xvalues']]
        result['functionValues'] = [float(f) for f in result['functionValues']]
        result['firstDerivatives'] = [float(d) for d in result['firstDerivatives']]
        result['secondDerivatives'] = [float(d) for d in result['secondDerivatives']]
        result['errors'] = [float(e) for e in result['errors']]
        
        return JsonResponse(result, safe=False)
    
@csrf_exempt
def newton_raphson_method(request):
    if request.method == 'POST':
        function = convert_expression(request.GET.get('function'))
        initial_guess = request.GET.get('initialGuess')
        error_type = request.GET.get('errorType')
        tolerance_value = request.GET.get('toleranceValue')
        max_iterations = request.GET.get('maxIterations')

        if not all([function, initial_guess, error_type, tolerance_value, max_iterations]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        initial_guess = float(initial_guess)
        error_type = int(error_type)
        tolerance_value = float(tolerance_value)
        max_iterations = int(max_iterations)
        
        result = newton_raphson(function, initial_guess, error_type, tolerance_value, max_iterations)
        
        result['xvalues'] = [float(x) for x in result['xvalues']]
        result['functionValues'] = [float(f) for f in result['functionValues']]
        result['derivatives'] = [float(d) for d in result['derivatives']]
        result['errors'] = [float(e) for e in result['errors']]
        
        return JsonResponse(result, safe=False)
    
@csrf_exempt
def secant_method(request):
    if request.method == 'POST':
        function = convert_expression(request.GET.get('function'))
        initial_guess1 = request.GET.get('initialGuess1')
        initial_guess2 = request.GET.get('initialGuess2')
        error_type = request.GET.get('errorType')
        tolerance_value = request.GET.get('toleranceValue')
        max_iterations = request.GET.get('maxIterations')

        if not all([function, initial_guess1, initial_guess2, error_type, tolerance_value, max_iterations]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        initial_guess1 = float(initial_guess1)
        initial_guess2 = float(initial_guess2)
        error_type = int(error_type)
        tolerance_value = float(tolerance_value)
        max_iterations = int(max_iterations)
        
        result = secant(function, initial_guess1, initial_guess2, error_type, tolerance_value, max_iterations)
        
        result['xvalues'] = [float(x) for x in result['xvalues']]
        result['functionValues'] = [float(f) for f in result['functionValues']]
        result['errors'] = [float(e) for e in result['errors']]
        
        return JsonResponse(result, safe=False)
    
@csrf_exempt
def cholesky_method(request):
    if request.method == 'POST':
        matrix = request.GET.get('matrix')
        vector = request.GET.get('vector')

        if not all([matrix, vector]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        A = parse_matrix(matrix)
        b = parse_vector(vector)
        
        result = cholesky(A, b)
        
        return JsonResponse(result, safe=False)
    
@csrf_exempt
def crout_method(request):
    if request.method == 'POST':
        matrix_data = request.GET.get('matrixData')
        vector_data = request.GET.get('vectorData')

        if not all([matrix_data, vector_data]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        A = parse_matrix(matrix_data)
        B = parse_vector(vector_data)
        
        result = crout(A, B)
        
        return JsonResponse(result, safe=False)

@csrf_exempt
def doolittle_method(request):
    if request.method == 'POST':
        matrix_data = request.GET.get('matrixData')
        b_data = request.GET.get('bData')

        if not all([matrix_data, b_data]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        A = parse_matrix(matrix_data)
        B = parse_vector(b_data)
        
        result = doolittle(A, B)
        
        return JsonResponse(result, safe=False)
    
@csrf_exempt
def simple_gaussian_elimination_method(request):
    if request.method == 'POST':
        matrix_a = request.GET.get('matrixA')
        vector_b = request.GET.get('vectorB')

        if not all([matrix_a, vector_b]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        A = parse_matrix(matrix_a)
        B = parse_vector(vector_b)
        
        result = simple_gaussian_elimination(A, B)
        
        return JsonResponse(result, safe=False)
    
@csrf_exempt
def pivot_gaussian_elimination_method(request):
    if request.method == 'POST':
        matrix_a = request.GET.get('matrixA')
        vector_b = request.GET.get('vectorB')

        if not all([matrix_a, vector_b]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        A = parse_matrix(matrix_a)
        B = parse_vector(vector_b)
        
        result = pivot_gaussian_elimination(A, B)
        
        return JsonResponse(result, safe=False)
    

@csrf_exempt
def lu_gaussian_elimination_method(request):
    if request.method == 'POST':
        matrix_a = request.GET.get('matrixA')
        vector_b = request.GET.get('vectorB')

        if not all([matrix_a, vector_b]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        A = parse_matrix(matrix_a)
        B = parse_vector(vector_b)
        
        result = lu_gaussian_elimination(A, B)
        
        return JsonResponse(result, safe=False)
    
@csrf_exempt
def jacobi_method(request):
    if request.method == 'POST':
        size = int(request.GET.get('size'))
        matrix = request.GET.get('matrix')
        b = request.GET.get('b')
        x0 = request.GET.get('x0')
        error_type = int(request.GET.get('errorType'))
        tolerance_value = float(request.GET.get('toleranceValue'))
        max_iterations = int(request.GET.get('maxIterations'))

        if not all([size, matrix, b, x0, error_type, tolerance_value, max_iterations]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        A = parse_matrix(matrix)
        B = parse_vector(b)
        
        result = jacobi(size, A, B, x0, error_type, tolerance_value, max_iterations)
        
        return JsonResponse(result, safe=False)

@csrf_exempt
def gauss_seidel_method(request):
    if request.method == 'POST':
        size = int(request.GET.get('size'))
        matrix = request.GET.get('matrixData')
        b = request.GET.get('b')
        x0 = request.GET.get('x0')
        error_type = int(request.GET.get('errorType'))
        tolerance_value = float(request.GET.get('toleranceValue'))
        max_iterations = int(request.GET.get('maxIterations'))

        if not all([size, matrix, b, x0, error_type, tolerance_value, max_iterations]):
            return JsonResponse({"error": "Missing parameter"}, status=400)

        A = parse_matrix(matrix)
        B = parse_vector(b)
        
        result = gauss_seidel(size, A, B, x0, error_type, tolerance_value, max_iterations)
        
        return JsonResponse(result, safe=False)