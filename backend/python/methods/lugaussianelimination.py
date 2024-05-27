import numpy as np

def lu_gaussian_elimination(A, b):
    n = len(A)
    L = np.zeros((n, n))
    U = np.zeros((n, n))

    # Initialize L with 1s in the diagonal
    np.fill_diagonal(L, 1)

    for i in range(n):
        for j in range(i, n):
            sum1 = sum(L[i][k] * U[k][j] for k in range(i))
            U[i][j] = A[i][j] - sum1

        for j in range(i + 1, n):
            if U[i][i] == 0:
                return {
                    "message": "Error: Division by zero.",
                    "solution": None,
                    "L": None,
                    "U": None
                }
            sum2 = sum(L[j][k] * U[k][i] for k in range(i))
            L[j][i] = (A[j][i] - sum2) / U[i][i]

    y = forward_substitution(L, b)
    x = backward_substitution(U, y)

    return {
        "message": "Success",
        "solution": x.tolist(),
        "l": L.tolist(),
        "u": U.tolist()
    }

def forward_substitution(L, b):
    n = len(L)
    y = np.zeros(n)
    for i in range(n):
        sum_val = sum(L[i][j] * y[j] for j in range(i))
        y[i] = (b[i] - sum_val) / L[i][i]
    return y

def backward_substitution(U, y):
    n = len(U)
    x = np.zeros(n)
    for i in range(n - 1, -1, -1):
        sum_val = sum(U[i][j] * x[j] for j in range(i + 1, n))
        x[i] = (y[i] - sum_val) / U[i][i]
    return x