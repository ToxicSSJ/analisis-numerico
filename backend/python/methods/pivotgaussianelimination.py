import numpy as np

def pivot_gaussian_elimination(A, b):
    n = len(A)
    augmented_matrix = np.hstack((A, b.reshape(-1, 1)))

    for i in range(n):
        max_row = i
        for k in range(i + 1, n):
            if abs(augmented_matrix[k, i]) > abs(augmented_matrix[max_row, i]):
                max_row = k

        augmented_matrix[[i, max_row]] = augmented_matrix[[max_row, i]]

        if augmented_matrix[i, i] == 0:
            return {
                "message": "No unique solution exists",
                "solution": None
            }

        pivot = augmented_matrix[i, i]
        augmented_matrix[i] = augmented_matrix[i] / pivot

        for j in range(n):
            if j != i:
                factor = augmented_matrix[j, i]
                augmented_matrix[j] = augmented_matrix[j] - factor * augmented_matrix[i]

    solution = augmented_matrix[:, -1]

    return {
        "message": "Success",
        "solution": solution.tolist()
    }