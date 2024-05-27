import numpy as np

class CholeskyDecomposition:
    def __init__(self, A):
        self.n = A.shape[0]
        self.L = np.zeros_like(A)

        for i in range(self.n):
            for j in range(i + 1):
                sum_val = np.sum(self.L[i, :j] * self.L[j, :j])
                if i == j:
                    if A[i, i] - sum_val <= 0:
                        raise ValueError("Matrix is not positive definite")
                    self.L[i, j] = np.sqrt(A[i, i] - sum_val)
                else:
                    self.L[i, j] = (A[i, j] - sum_val) / self.L[j, j]

    def solve(self, b):
        y = np.zeros_like(b)
        x = np.zeros_like(b)

        for i in range(self.n):
            y[i] = (b[i] - np.dot(self.L[i, :i], y[:i])) / self.L[i, i]

        for i in range(self.n - 1, -1, -1):
            x[i] = (y[i] - np.dot(self.L[i + 1:, i], x[i + 1:])) / self.L[i, i]

        return x

    def get_L(self):
        return self.L

    def get_U(self):
        return self.L.T

def cholesky(A, b):
    try:
        cholesky = CholeskyDecomposition(A)
        solution = cholesky.solve(b)
        L = cholesky.get_L()
        U = cholesky.get_U()
        return {
            "message": "Success",
            "solution": solution.tolist(),
            "l": L.tolist(),
            "u": U.tolist()
        }
    except Exception as e:
        return {
            "message": f"Error: {e}",
            "solution": None,
            "l": None,
            "u": None
        }