import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import LinearSegmentedColormap
import math
import sys
import random
import statistics
def applyRule(matrix, T, X, BoundaryValue, BoundaryCondition, rule):
    for t in range(1, T):
        for x in range(0, X):
            x_l = -1
            x_r = -1
            x_c = matrix[t-1][x]

            if x > 0:
                x_l = matrix[t-1][x - 1]
            else:
                if BoundaryCondition == 'reflexive':
                    x_l = matrix[t-1][x + 1]
                elif BoundaryCondition == 'constante':
                    x_l = BoundaryValuebo
                elif  BoundaryCondition == 'periodic':
                    x_l = matrix[t-1][X - 1]
                else:
                    print('Error: BoundaryCondition unknown in x == 0')
                    sys.exit(-1)

            if x < X - 1:
                x_r = matrix[t-1][x + 1]
            else:
                if BoundaryCondition == 'reflexive':
                    x_r = matrix[t-1][x - 1]
                elif BoundaryCondition == 'constante':
                    x_r = BoundaryValue
                elif  BoundaryCondition == 'periodic':
                    x_r = matrix[t-1][0]
                else:
                    print('Error: BoundaryCondition unknown in x == X')
                    sys.exit(-1)

            if x_l == -1 or x_c == -1 or x_r == -1:
                    txt = 'Error: Values are not defined  ({}, {}) --> ({}, {}, {}) '.format(t, x, x_l, x_c, x_r)
                    print(txt)
                    sys.exit(-1)

            matrix[t][x] =  rule(x_l, x_c, x_r)
            
def rule0(x_l, x_c, x_r):
    return 0

def rule5(x_l, x_c, x_r):
    return (not x_l) and (not x_r)

def rule22(x_l, x_c, x_r):
    a = x_l and (not x_c) and (not x_r)
    b = (not x_l) and x_c and (not x_r)
    return a or b

def rule90(x_l, x_c, x_r):
    return (x_l and (not x_r)) or  ((not x_l) and x_r)

def rule110(x_l, x_c, x_r):
    a = (not x_l) and x_c
    b =  x_c and (not x_r) 
    c = (not x_c) and x_r
    return a or b or c

def rule150(x_l, x_c, x_r):
    a = x_l and (not x_c)  and (not x_r)
    b = (not x_l) and x_c and (not x_r)
    c = (not x_l) and (not x_c) and x_r
    d = x_l and x_c and  x_r
    return a or b or c or d

def rule122(x_l, x_c, x_r):
    a = x_l and (not x_c) 
    b = x_l and (not x_r) 
    c = (not x_l) and x_r
    return a or b or c

def rule255(x_l, x_c, x_r):
    return 1

def entropy(matrix, entropies, TS, T, X):
    K = 1.0

    for t in range(0, T):
        count = 0.0
        for x in range(0, X):
            cell = matrix[t][x]
            if cell == 1:
                count = count + 1.0
        count = count / X;
        h = K * count * math.log2(1/count)
        if t > X:
            entropies.append(h)
            TS.append(t)


def getEntropy(IsRand, Probp, T, X, BoundaryValue, BoundaryCondition, rule):

    # initial condition:
    matrix = np.zeros((T, X), dtype=np.int32)  # np.matrix((self.m_Height, self.m_Width), dtype=np.int8)
    entropies = []
    TS = []
    #
    if IsRand:
        for i in range(0, X):
            if random.random() < Probp:
                matrix[0][i] = 1
    else:

        matrix[0][int(X/2)] = 1

    applyRule(matrix, T, X, BoundaryValue, BoundaryCondition, rule)
    entropy(matrix, entropies, TS, T, X)
    return entropies, TS

if __name__ == '__main__':
    print('Wolfram CA rules')
    # Input parameters
    IsRand = False

    Probp = 0.1
    T = 1000
    X = 50

    # T = 768
    # X = 1024
    BoundaryValue = 0
    BoundaryCondition = 'periodic'  # constante #periodic

    random.seed(42)
    rule = rule90
    r90E, r90TS = getEntropy(IsRand, Probp, T, X, BoundaryValue, BoundaryCondition, rule)

    random.seed(42)
    rule = rule150
    r150E, r150TS = getEntropy(IsRand, Probp, T, X, BoundaryValue, BoundaryCondition, rule)

    random.seed(42)
    rule = rule110
    r110E, r110TS = getEntropy(IsRand, Probp, T, X, BoundaryValue, BoundaryCondition, rule)

    random.seed(42)
    rule = rule22
    r22E, r22TS = getEntropy(IsRand, Probp, T, X, BoundaryValue, BoundaryCondition, rule)

    random.seed(42)
    rule = rule5
    r5E, r5TS = getEntropy(IsRand, Probp, T, X, BoundaryValue, BoundaryCondition, rule)

    print('Rule  90 Mean:', statistics.mean(r90E),  "\tStdev: ", statistics.stdev(r90E))
    print('Rule 150 Mean:', statistics.mean(r150E), "\tStdev: ", statistics.stdev(r150E))
    print('Rule 110 Mean:', statistics.mean(r110E), "\tStdev: ", statistics.stdev(r110E))
    print('Rule  22 Mean:', statistics.mean(r22E),  "\tStdev: ", statistics.stdev(r22E))
    print('Rule   5 Mean:', statistics.mean(r5E),   "\tStdev: ", statistics.stdev(r5E))

'''
    plt.figure(figsize=(9, 6))
    plt.plot(r90TS, r90E, label='Rule 90')
    plt.plot(r150TS, r150E, label='Rule 150')
    plt.plot(r110TS, r110E, label='Rule 110')
    plt.plot(r22TS, r22E, label='Rule 22')
    plt.plot(r5TS, r5E, label='Rule 5')

    plt.legend()
    plt.show()
'''
    #print_2_file("rule90_fix_middle.csv", entropies)

    #my_colors = ['white', 'red'] # colors for 0, 1, 2 and 3
    #cmap = LinearSegmentedColormap.from_list('', my_colors, len(my_colors))


    #plt.imshow(matrix, cmap=cmap, vmin=0, vmax=len(my_colors) - 1, alpha=0.4)
    #plt.matshow(matrix, cmap=cmap);
    
    #plt.colorbar()
    #plt.savefig("rule90.png", format="png", dpi=300)

