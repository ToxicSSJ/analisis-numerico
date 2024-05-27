import sympy as sp
import math

def multiple_roots(function_expr, initial_guess, error_type, tolerance_value, max_iterations):
    x = sp.symbols('x')
    function = sp.sympify(function_expr)
    first_derivative = sp.diff(function, x)
    second_derivative = sp.diff(first_derivative, x)
    
    tolerance = 0.5 * 10 ** -tolerance_value
    
    xvalues = []
    functionvalues = []
    firstderivatives = []
    secondderivatives = []
    errors = []
    iterations = []

    current_x = initial_guess
    current_value = function.evalf(subs={x: current_x})
    current_first_derivative = first_derivative.evalf(subs={x: current_x})
    current_second_derivative = second_derivative.evalf(subs={x: current_x})
    iteration_count = 0
    error = 100.0  # Initial error set to 100%
    xvalues.append(current_x)
    functionvalues.append(current_value)
    firstderivatives.append(current_first_derivative)
    secondderivatives.append(current_second_derivative)
    errors.append(error)
    iterations.append(iteration_count)

    if math.isnan(current_value) or math.isnan(current_first_derivative) or math.isnan(current_second_derivative) or \
       math.isinf(current_first_derivative) or math.isinf(current_second_derivative):
        message = f"The function or its derivatives are not defined at x = {initial_guess}. The method fails."
        return {
            "message": message,
            "xvalues": [],
            "functionValues": [],
            "firstDerivatives": [],
            "secondDerivatives": [],
            "errors": [],
            "iterations": []
        }

    while iteration_count < max_iterations:
        denominator = current_first_derivative**2 - current_value * current_second_derivative
        if denominator == 0:
            message = "The method fails. The denominator is zero."
            return {
                "message": message,
                "xvalues": xvalues,
                "functionValues": functionvalues,
                "firstDerivatives": firstderivatives,
                "secondDerivatives": secondderivatives,
                "errors": errors,
                "iterations": iterations
            }

        iteration_count += 1
        current_x = current_x - (current_value * current_first_derivative) / denominator
        current_value = function.evalf(subs={x: current_x})
        current_first_derivative = first_derivative.evalf(subs={x: current_x})
        current_second_derivative = second_derivative.evalf(subs={x: current_x})

        if math.isnan(current_value) or math.isnan(current_first_derivative) or math.isnan(current_second_derivative) or \
           math.isinf(current_first_derivative) or math.isinf(current_second_derivative):
            message = f"The function or its derivatives are not defined at x = {current_x}. The method fails."
            return {
                "message": message,
                "xvalues": xvalues,
                "functionValues": functionvalues,
                "firstDerivatives": firstderivatives,
                "secondDerivatives": secondderivatives,
                "errors": errors,
                "iterations": iterations
            }

        xvalues.append(current_x)
        functionvalues.append(current_value)
        firstderivatives.append(current_first_derivative)
        secondderivatives.append(current_second_derivative)
        iterations.append(iteration_count)

        error = abs(xvalues[iteration_count] - xvalues[iteration_count - 1]) if error_type == 1 else abs((xvalues[iteration_count] - xvalues[iteration_count - 1]) / xvalues[iteration_count])
        errors.append(error)

        if current_value == 0 or error < tolerance:
            break
    
    message = f"{current_x} is a root of f(x)" if current_value == 0 else f"The approximate solution is: {current_x}, with a tolerance = {tolerance}" if errors[-1] < tolerance else f"Failed in {max_iterations} iterations"
    return {
        "message": message,
        "xvalues": xvalues,
        "functionValues": functionvalues,
        "firstDerivatives": firstderivatives,
        "secondDerivatives": secondderivatives,
        "errors": errors,
        "iterations": iterations
    }