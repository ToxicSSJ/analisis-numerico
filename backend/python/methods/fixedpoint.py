import sympy as sp

def fixed_point(function_expr, g_function_expr, initial_guess, error_type, tolerance_value, max_iterations):
    x = sp.symbols('x')
    function = sp.sympify(function_expr)
    g_function = sp.sympify(g_function_expr)
    
    tolerance = 0.5 * 10 ** -tolerance_value
    
    xvalues = []
    functionvalues = []
    errors = []
    iterations = []

    current_x = initial_guess
    current_function_value = function.evalf(subs={x: current_x})
    iteration_count = 0
    error = 100.0  # Initial error set to 100%
    xvalues.append(current_x)
    functionvalues.append(current_function_value)
    errors.append(error)
    iterations.append(iteration_count)

    while error >= tolerance and current_function_value != 0 and iteration_count < max_iterations:
        iteration_count += 1
        current_x = g_function.evalf(subs={x: current_x})
        current_function_value = function.evalf(subs={x: current_x})
        xvalues.append(current_x)
        functionvalues.append(current_function_value)
        iterations.append(iteration_count)

        error = abs(xvalues[iteration_count] - xvalues[iteration_count - 1]) if error_type == 1 else abs((xvalues[iteration_count] - xvalues[iteration_count - 1]) / xvalues[iteration_count])
        errors.append(error)

    message = f"{current_x} is a root of f(x)" if current_function_value == 0 else f"The approximate solution is: {current_x}, with a tolerance = {tolerance}" if errors[-1] < tolerance else f"Failed in {max_iterations} iterations"

    return {
        "message": message,
        "xvalues": xvalues,
        "functionValues": functionvalues,
        "errors": errors,
        "iterations": iterations
    }