import numpy as np

def simple_gaussian_elimination(A, b):
    n = len(A)
    M = np.zeros((n, n + 1))
    
    for i in range(n):
        M[i, :-1] = A[i]
        M[i, -1] = b[i]

    for i in range(n - 1):
        for j in range(i + 1, n):
            if M[j, i] != 0:
                factor = M[j, i] / M[i, i]
                M[j, i:] -= factor * M[i, i:]

    x = np.zeros(n)
    for i in range(n - 1, -1, -1):
        x[i] = (M[i, -1] - np.dot(M[i, i + 1:n], x[i + 1:n])) / M[i, i]

    return {
        "solution": x.tolist()
    }