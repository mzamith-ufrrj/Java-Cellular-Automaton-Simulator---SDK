import numpy as np
import zlib
import matplotlib.pyplot as plt

def generate_eca(rule_number, steps):
    """Gera a evolução de um ECA e retorna a matriz."""
    cells = steps * 2 + 1
    state = np.zeros((steps, cells), dtype=np.uint8)
    state[0, cells // 2] = 1
    
    rule_bin = np.array([int(x) for x in np.binary_repr(rule_number, width=8)][::-1])
    
    for t in range(steps - 1):
        left = np.roll(state[t], 1)
        center = state[t]
        right = np.roll(state[t], -1)
        idx = 4 * left + 2 * center + 1 * right
        state[t+1] = rule_bin[idx]
    return state

def estimate_kolmogorov(matrix):
    """
    Estima a complexidade de Kolmogorov via compressão zlib.
    Retorna a razão de compressão (bytes comprimidos / bytes originais).
    """
    # Converte a matriz em uma string de bits (0s e 1s)
    # flattened = matrix.flatten().astype(str) # Lento para matrizes grandes
    # Mais eficiente: transformar em bytes brutos
    data_bytes = np.packbits(matrix).tobytes()
    
    original_size = len(data_bytes)
    compressed_data = zlib.compress(data_bytes, level=9)
    compressed_size = len(compressed_data)
    
    # Razão de compressão: quanto menor, mais 'simples' (menos complexo) é o objeto
    kolmogorov_ratio = compressed_size / original_size
    return kolmogorov_ratio, compressed_size, original_size

# --- Experimento Comparativo ---
STEPS = 500
rules_to_test = {
    "Regra 0 (Trivial)": 0,
    "Regra 90 (Sierpinski)": 90,
    "Regra 30 (Caótica)": 30
}

print(f"{'Regra':<25} | {'Razão K (estimada)':<20} | {'Tamanho Comprimido'}")
print("-" * 65)

results = {}
for name, num in rules_to_test.items():
    matrix = generate_eca(num, STEPS)
    ratio, comp_size, orig_size = estimate_kolmogorov(matrix)
    results[name] = ratio
    print(f"{name:<25} | {ratio:<20.4f} | {comp_size} bytes")

# --- Visualização ---
plt.bar(results.keys(), results.values(), color=['green', 'blue', 'red'])
plt.ylabel('Estimativa de Kolmogorov (Razão de Compressão)')
plt.title('Complexidade de Kolmogorov Estimada: Estrutura vs Caos')
plt.show()