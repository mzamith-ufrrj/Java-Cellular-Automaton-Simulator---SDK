import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import LinearSegmentedColormap
import math
import sys
import random

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
                    #breakpoint()
                    x_l = matrix[t-1][x + 1]
                elif BoundaryCondition == 'constante':
                    x_l = BoundaryValue
                elif  BoundaryCondition == 'periodic':
                    x_l = matrix[t-1][len(X) - 1]
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

def entropy(matrix, T, X, lambda_p):
    K = -1
    timeStep = []
    timeEntroy = []
    aveEntropy = 0
    #accEntropy = 0
    for t in range(1, T):
        p1 = 0
        p0 = 0
        for x in range(0, X):
            if matrix[t][x] == 1:
                p1 = p1 + 1
            else:
                p0 = p0 + 1
        
        timeStep.append(t+1)

        if p0 > 0:    
            a = (p0/X) * math.log2(1/(p0/X)) 
        else:
            a = 0
        
        if p1 > 0:
            b = (p1/X) * math.log2(1/(p1/X)) 
        else:
            b = 0

        e = -K * (a + b)
        aveEntropy = aveEntropy + e
        timeEntroy.append(e)

    aveEntropy = aveEntropy / T
    min_e = min(timeEntroy)
    max_e = max(timeEntroy)

    #print('Entropy: ', accEntropy, ' -- ', (accEntropy / T))
    fig = plt.figure()
    ax = plt.axes()
    ax.plot(timeStep, timeEntroy, color='b')
    plt.axhline(y=aveEntropy, color='r', linestyle='-')
    plt.axhline(y=min_e, color='r', linestyle='-')
    plt.axhline(y=max_e, color='r', linestyle='-')
    txt = r'$\lambda$ = {} / $\mu$ = {:.3f} / min = {:.3f} / max = {:.3f}'.format(lambda_p, aveEntropy, min_e, max_e)
    plt.title(txt)
    plt.xlabel('time step')
    plt.ylabel('H')
    plt.grid()
    plt.show()

def lambda_parameter_1D(rule):

    r = int(rule)
    n = int(0)
    p = int(1)
    q = int(0)
    while p > 0:
        p = int(r / 2)
        q = int(r % 2)
        
        n = n + (1 * q)
        r = p    
    q = r % 2
    n = n + (1 * q)
    n = n + (1 * r)
    KN = 8.0
    l = (KN - float(8-n)) / KN
    
    return l
    #txt = 'Bits on = {}\nLambda = {}\n'.format(n, l)
    #print(txt)
    
if __name__ == '__main__':
    print('Wolfram CA rules')
    #Input parameters
    IsRand = False
    random.seed(42)
    Probp = 0.50
    T = 32
    X = 32
    #T = 768
    #X = 1024
    BoundaryValue = 0
    BoundaryCondition = 'periodic' #constante #periodic

    #initial condition:
    matrix =  np.zeros((T, X), dtype=np.int32) #np.matrix((self.m_Height, self.m_Width), dtype=np.int8)
    #
    if IsRand:
        for i in range(0, X):
            if random.random() < Probp:
                matrix[0][i] = 1
    else:
        matrix[0][int(X/2)] = 1
    '''
    matrix[0][0] = 1
    matrix[0][1] = 1
    matrix[0][2] = 1
    matrix[0][3] = 0
    matrix[0][4] = 0
    matrix[0][5] = 1
    matrix[0][6] = 1
    matrix[0][7] = 0
    matrix[0][8] = 1
    matrix[0][9] = 0
    '''

    
    applyRule(matrix, T, X, BoundaryValue, BoundaryCondition, rule90)
    #entropy(matrix, T, X, lambda_parameter_1D(110))
    #print(lambda_parameter_1D(0))

    #Show CA - 
    plt.figure(figsize=(18,12))
    my_colors = ['white', 'blue'] # colors for 0, 1, 2 and 3
    cmap = LinearSegmentedColormap.from_list('', my_colors, len(my_colors))
    plt.imshow(matrix, cmap=cmap, vmin=0, vmax=len(my_colors) - 1, alpha=0.4)
    #plt.matshow(matrix, cmap=cmap);    
    
    #plt.colorbar()
    #plt.savefig("rule90.png", format="png", dpi=300)
    plt.show()
