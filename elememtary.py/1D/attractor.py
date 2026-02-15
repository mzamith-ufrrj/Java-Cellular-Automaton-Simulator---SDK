import matplotlib.pyplot as plt

def dfunc(x, a):
    return abs((1 + a) - (2 * a * x))


def func(x, a):
    return ((1 + a) * x) - (a * (x*x))

if __name__ == '__main__':
    print('Hello')
    x = 0.35
    a = 20
    X = []
    X1 = []
    I = []
    for i in range(0, 1000):
        X.append(x)
        I.append(i+1)
        X1.append(dfunc(x, a))
        #txt = '{} {}'.format(i+1, x)
        #print(txt)
        x = func(x, a)

    plt.scatter(I, X)
    plt.show()
    plt.scatter(I, X1)
    plt.show()