import sympy as sp
import math

def secant(function_expr, initial_guess1, initial_guess2, error_type, tolerance_value, max_iterations):
    x = sp.symbols('x')
    function = sp.sympify(function_expr)
    
    tolerance = 0.5 * 10 ** -tolerance_value
    
    xvalues = []
    functionvalues = []
    errors = []
    iterations = []

    x0 = initial_guess1
    x1 = initial_guess2
    f0 = function.evalf(subs={x: x0})
    f1 = function.evalf(subs={x: x1})
    iteration_count = 0
    error = 100.0  # Initial error set to 100%
    xvalues.append(x0)
    xvalues.append(x1)
    functionvalues.append(f0)
    functionvalues.append(f1)
    errors.append(error)
    errors.append(abs(x1 - x0))
    iterations.append(0)
    iterations.append(1)

    if math.isnan(f0) or math.isnan(f1) or math.isinf(f0) or math.isinf(f1):
        message = f"The function is not defined at x0 = {initial_guess1} or x1 = {initial_guess2}. The method fails."
        return {
            "message": message,
            "xvalues": [],
            "functionValues": [],
            "errors": [],
            "iterations": []
        }

    while iteration_count < max_iterations and errors[-1] >= tolerance and f1 != 0 and f1 - f0 != 0:
        iteration_count += 1
        x2 = x1 - (f1 * (x1 - x0)) / (f1 - f0)
        x0 = x1
        f0 = f1
        x1 = x2
        f1 = function.evalf(subs={x: x1})

        if math.isnan(f1) or math.isinf(f1):
            message = f"The function is not defined at x = {x1}. The method fails."
            return {
                "message": message,
                "xvalues": xvalues,
                "functionValues": functionvalues,
                "errors": errors,
                "iterations": iterations
            }

        xvalues.append(x1)
        functionvalues.append(f1)
        iterations.append(iteration_count)

        error = abs(x1 - x0) if error_type == 1 else abs((x1 - x0) / x1)
        errors.append(error)

    message = f"{x1} is a root of f(x)" if f1 == 0 else f"The approximate solution is: {x1}, with a tolerance = {tolerance}" if errors[-1] < tolerance else f"Failed in {max_iterations} iterations"
    return {
        "message": message,
        "xvalues": xvalues,
        "functionValues": functionvalues,
        "errors": errors,
        "iterations": iterations
    }