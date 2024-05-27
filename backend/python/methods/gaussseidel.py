import numpy as np

def gauss_seidel(size, A, b, x0_data, error_type, tolerance_value, max_iterations):
    x0 = np.array([float(num) for num in x0_data.split()])
    
    if len(x0) != size:
        return {
            "message": "Vector size does not match the given matrix size.",
            "xvalues": [],
            "errors": []
        }
    
    if error_type == 1:
        tolerance = 0.5 * 10 ** -tolerance_value
    elif error_type == 2:
        tolerance = 5 * 10 ** -tolerance_value

    norm_type = np.inf  # Cambia esto si necesitas otro tipo de norma

    D = np.diag(np.diag(A))
    L = -np.tril(A, -1)
    U = -np.triu(A, 1)

    if np.linalg.det(D - L) == 0:
        return {
            "message": "Matrix (D - L) is singular, the method fails.",
            "xvalues": [],
            "errors": []
        }

    DL_inv = np.linalg.inv(D - L)
    T = DL_inv @ U
    C = DL_inv @ b

    x_values = []
    errors = []
    x_values.append(x0.tolist())
    error = tolerance + 1
    iterations = 0

    while error > tolerance and iterations < max_iterations:
        x1 = T @ x0 + C
        error = np.linalg.norm(x1 - x0, norm_type)
        if error_type == 2:
            error /= np.linalg.norm(x1, norm_type)
        errors.append(error)
        x_values.append(x1.tolist())
        x0 = x1
        iterations += 1

    spectral_radius = max(abs(np.linalg.eigvals(T)))

    if error < tolerance:
        message = f"The approximate solution is: {x0}, with a tolerance = {tolerance}"
        if spectral_radius < 1:
            message += f" This solution is unique because the spectral radius of T is {spectral_radius} and is less than 1."
    else:
        message = f"Failed in {max_iterations} iterations."
        if spectral_radius >= 1:
            message += f" It is possible that the method failed because the spectral radius of T is {spectral_radius} and is greater than or equal to 1."
        else:
            row_sums = np.sum(np.abs(A), axis=1)
            diag = np.abs(np.diag(A))
            dominant = np.all(diag > row_sums - diag)
            if not dominant:
                message += " It is possible that the method failed because A is not diagonally dominant."

    return {
        "message": message,
        "xvalues": x_values,
        "errors": errors
    }