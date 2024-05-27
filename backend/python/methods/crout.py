import numpy as np

def crout(A, B):
    n = A.shape[0]

    L = np.zeros((n, n))
    U = np.eye(n)

    for j in range(n):
        for i in range(j, n):
            L[i, j] = A[i, j] - sum(L[i, k] * U[k, j] for k in range(j))
        for i in range(j + 1, n):
            U[j, i] = (A[j, i] - sum(L[j, k] * U[k, i] for k in range(j))) / L[j, j]

    z = np.zeros(n)
    for i in range(n):
        z[i] = B[i] - sum(L[i, j] * z[j] for j in range(i))
        z[i] /= L[i, i]

    x = np.zeros(n)
    for i in range(n - 1, -1, -1):
        x[i] = z[i] - sum(U[i, j] * x[j] for j in range(i + 1, n))

    return {
        "solution": x.tolist(),
        "l": L.tolist(),
        "u": U.tolist()
    }