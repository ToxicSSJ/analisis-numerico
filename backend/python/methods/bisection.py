import sympy as sp

def bisection(function_expr, lower_bound, upper_bound, error_type, tolerance_value, max_iterations):
    x = sp.symbols('x')
    function = sp.sympify(function_expr)
    
    f_lower = function.evalf(subs={x: lower_bound})
    f_upper = function.evalf(subs={x: upper_bound})
    tolerance = 0.5 * 10 ** -tolerance_value
    
    if f_lower == 0:
        return {"message": f"{lower_bound} is a root of f(x)", "xvals": [], "fvals": [], "errors": [], "iterations": []}
    elif f_upper == 0:
        return {"message": f"{upper_bound} is a root of f(x)", "xvals": [], "fvals": [], "errors": [], "iterations": []}
    elif f_lower * f_upper > 0:
        return {"message": "The interval is inadequate", "xvals": [], "fvals": [], "errors": [], "iterations": []}
    
    x_values = []
    f_values = []
    errors = []
    iterations = []

    iteration_count = 0
    lower = lower_bound
    upper = upper_bound
    mid_point = (lower + upper) / 2.0
    f_mid = function.evalf(subs={x: mid_point})
    x_values.append(mid_point)
    f_values.append(f_mid)
    errors.append(100.0)  # Initial error set to 100%
    iterations.append(iteration_count)
    
    previous_mid_point = mid_point

    while iteration_count < max_iterations:
        iteration_count += 1

        tempX = function.evalf(subs={x: lower}) * f_mid
        
        if tempX < 0 or tempX == 0:
            upper = mid_point
        else:
            lower = mid_point

        mid_point = (lower + upper) / 2.0
        f_mid = function.evalf(subs={x: mid_point})
        
        error = abs(mid_point - previous_mid_point) if error_type == 1 else abs(mid_point - previous_mid_point) / abs(mid_point)
        x_values.append(mid_point)
        f_values.append(f_mid)
        errors.append(error)
        iterations.append(iteration_count)

        if error < tolerance:
            break
        
        previous_mid_point = mid_point
    
    message = f"{mid_point} is a root of f(x)" if f_mid == 0 else f"The approximate solution is: {mid_point}, with a tolerance = {tolerance}" if errors[-1] < tolerance else f"Failed in {max_iterations} iterations"

    return {
        "message": message,
        "xvals": x_values,
        "fvals": f_values,
        "errors": errors,
        "iterations": iterations
    }