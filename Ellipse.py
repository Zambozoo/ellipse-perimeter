import math
from scipy.optimize import *
import numpy as np
import matplotlib.pyplot as plt

# TODO: PAIR MAJOR AND MINOR AXES IN A BETTER MANNER

# SET TO DESIRED VALUES
#   _max = MAX VALUE FOR MAJOR AXIS
#   num = NUMBER OF MAJOR AXIS TO CALCULATE PERIMETER
#   iterations = DEPTH OF EXACT PERIMETER CALCULATION
#   show_plot = DISPLAYS 3D PLOT, TAKES TIME
_max = 100_000
num = 10_000_000
iterations = 10_000
show_plot = False

if __name__ == '__main__':
    guess_count = 0  # PRINTS TO SHOW PROGRESS

    def guess_perimeter(data, E):
        global guess_count
        print("GuessCount:", guess_count := guess_count + 1)
        a, b = data
        return a * ((2 * math.pi - 4) * (b / a) ** E + 4)

    def true_perimeter(data):
        a, b = data
        e2 = 1 - (b / a)**2
        exact, coefficient = 1, 1
        coefficient = 1
        for j in range(1, 2 * iterations, 2):
            print("TrueCount:", (j - 1) // 2)  # PRINTS TO SHOW PROGRESS
            coefficient *= (j / (j + 1))**2 * e2
            exact -= coefficient / j
        return 2 * a * math.pi * exact

    # CALCULATED AXIS LIMITS
    _min = 1
    mid = (_max + _min) / 2

    # AXIS VALUES
    xs = np.linspace(_max, mid + 1, num)
    ys = np.linspace(_min, mid - 1, num)

    # CURVE FIT
    parameters, covariance = curve_fit(guess_perimeter, [xs, ys], true_perimeter([xs, ys]))

    print("E =", parameters[0], "error = ", np.sqrt(covariance[0, 0]))

    if show_plot:
        fig = plt.figure()
        ax = plt.axes(projection='3d')
        Xs, Ys = np.meshgrid(xs, ys)
        Zs = guess_perimeter(np.array([Xs, Ys]), *parameters)
        print(parameters)
        ax.contour3D(Xs, Ys, Zs, 50, cmap='binary')
        plt.show()
