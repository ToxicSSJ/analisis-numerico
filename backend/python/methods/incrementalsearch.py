import sympy as sp

def incremental_search(function_expr, x0, h, max_iterations, error_type, tolerance_value):
    x = sp.symbols('x')
    function = sp.sympify(function_expr)
    
    x_previous = x0
    f_previous = function.evalf(subs={x: x_previous})
    x_current = x_previous + h
    f_current = function.evalf(subs={x: x_current})
    iterations = 0
    tolerance = 0.5 * 10 ** -tolerance_value

    xvalues = [x_previous]
    functionvalues = [f_previous]
    errors = [100.0]  # Initial error set to 100%

    for iterations in range(1, max_iterations + 1):
        if f_previous * f_current < 0 or errors[iterations - 1] < tolerance:
            break
        x_previous = x_current
        f_previous = f_current
        x_current = x_previous + h
        f_current = function.evalf(subs={x: x_current})
        xvalues.append(x_previous)
        functionvalues.append(f_previous)

        error = abs(x_current - x_previous) if error_type == 1 else abs((x_current - x_previous) / x_current)
        errors.append(error)
    
    return {
        "a": x_previous,
        "b": x_current,
        "iterations": iterations,
        "xvalues": xvalues,
        "functionValues": functionvalues,
        "errors": errors
    }