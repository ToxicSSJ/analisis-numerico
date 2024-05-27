import numpy as np

def doolittle(A, B):
    n = A.shape[0]

    L = np.eye(n)
    U = np.zeros((n, n))

    for i in range(n):
        for k in range(i, n):
            sum_val = sum(L[i][j] * U[j][k] for j in range(i))
            U[i][k] = A[i][k] - sum_val

        for k in range(i + 1, n):
            sum_val = sum(L[k][j] * U[j][i] for j in range(i))
            L[k][i] = (A[k][i] - sum_val) / U[i][i]

    z = np.zeros(n)
    for i in range(n):
        z[i] = B[i] - sum(L[i][j] * z[j] for j in range(i))

    x = np.zeros(n)
    for i in range(n - 1, -1, -1):
        x[i] = z[i] - sum(U[i][j] * x[j] for j in range(i + 1, n))
        x[i] /= U[i][i]

    return {
        "solution": x.tolist(),
        "l": L.tolist(),
        "u": U.tolist()
    }