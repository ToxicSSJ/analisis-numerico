import sympy as sp
import math

def calculate_derivative(function, value):
    x = sp.symbols('x')
    derivative = sp.diff(function, x)
    return derivative.evalf(subs={x: value})

def newton_raphson(function_expr, initial_guess, error_type, tolerance_value, max_iterations):
    x = sp.symbols('x')
    function = sp.sympify(function_expr)
    
    tolerance = 0.5 * 10 ** -tolerance_value
    
    xvalues = []
    functionvalues = []
    derivatives = []
    errors = []
    iterations = []

    current_x = initial_guess
    current_function_value = function.evalf(subs={x: current_x})
    current_derivative_value = calculate_derivative(function, current_x)
    iteration_count = 0
    error = 100.0  # Initial error set to 100%
    xvalues.append(current_x)
    functionvalues.append(current_function_value)
    derivatives.append(current_derivative_value)
    errors.append(error)
    iterations.append(iteration_count)

    if math.isnan(current_function_value) or math.isnan(current_derivative_value) or math.isinf(current_derivative_value):
        message = f"The function is not defined or differentiable at x = {initial_guess}. The method fails."
        return {
            "message": message,
            "xvalues": [],
            "functionValues": [],
            "derivatives": [],
            "errors": [],
            "iterations": []
        }

    while error >= tolerance and current_function_value != 0 and current_derivative_value != 0 and iteration_count < max_iterations:
        iteration_count += 1
        current_x = current_x - (current_function_value / current_derivative_value)
        current_function_value = function.evalf(subs={x: current_x})
        current_derivative_value = calculate_derivative(function, current_x)

        if math.isnan(current_function_value) or math.isnan(current_derivative_value) or math.isinf(current_derivative_value):
            message = f"The function is not defined or differentiable at x = {current_x}. The method fails."
            return {
                "message": message,
                "xvalues": xvalues,
                "functionValues": functionvalues,
                "derivatives": derivatives,
                "errors": errors,
                "iterations": iterations
            }

        xvalues.append(current_x)
        functionvalues.append(current_function_value)
        derivatives.append(current_derivative_value)
        iterations.append(iteration_count)

        error = abs(xvalues[iteration_count] - xvalues[iteration_count - 1]) if error_type == 1 else abs((xvalues[iteration_count] - xvalues[iteration_count - 1]) / xvalues[iteration_count])
        errors.append(error)

    message = f"{current_x} is a root of f(x)" if current_function_value == 0 else f"The approximate solution is: {current_x}, with a tolerance = {tolerance}" if errors[-1] < tolerance else f"Failed in {max_iterations} iterations"
    return {
        "message": message,
        "xvalues": xvalues,
        "functionValues": functionvalues,
        "derivatives": derivatives,
        "errors": errors,
        "iterations": iterations
    }