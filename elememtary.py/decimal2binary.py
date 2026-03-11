import sys
def int_para_binario_8bits(n) -> str:
    # Garante que o número esteja no intervalo de 0-255 para 8 bits
    if not 0 <= n <= 255:
        return "Valor fora do intervalo de 8 bits"
    
    return f"{n:08b}"

def getValues(value) -> [int, int]:
    a = 0
    b = 0
    
    for s in value:
        if s == '1':
            a = a + 1
        elif s == '0':
            b = b + 1
        else:
            sys.exit(1)
    return a, b

if __name__ == "__main__":
    print("Convertendo: ")
    print(int_para_binario_8bits(90))
    print(int_para_binario_8bits(182))
    a, b = getValues(int_para_binario_8bits(90))
    print(a)
    print(b)
    a, b = getValues(int_para_binario_8bits(182))
    print(a)
    print(b)