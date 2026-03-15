import numpy as np
import matplotlib.pyplot as plt

def generate_eca(rule_number, steps):
    """Gera a evolução de um Autômato Celular Elementar."""
    cells = steps# * 2 + 1
    state = np.zeros((steps, cells), dtype=int)
    state[0, (cells // 2)+1] = 1  # Semente inicial no centro
    
    # Converte o número da regra para binário de 8 bits
    rule_bin = np.array([int(x) for x in np.binary_repr(rule_number, width=8)][::-1])
    
    for t in range(steps - 1):
        # Vizinhos: Esquerda, Centro, Direita
        left = np.roll(state[t], 1)
        center = state[t]
        right = np.roll(state[t], -1)
        
        # Calcula o índice da regra (4*L + 2*C + 1*D)
        idx = 4 * left + 2 * center + 1 * right
        state[t+1] = rule_bin[idx]
        
    return state

def box_count(matrix, box_size):
    print(box_size)
    """Divide a matriz em blocos de box_size x box_size e conta blocos ocupados."""
    S = matrix.shape[0]
    n_boxes = S // box_size
    # Redimensiona a matriz para criar uma visão de blocos
    reshaped = matrix[:n_boxes*box_size, :n_boxes*box_size].reshape(
        n_boxes, box_size, n_boxes, box_size
    )
    # Um bloco está ocupado se houver pelo menos um '1' dentro dele
    
    count = np.sum(np.any(reshaped, axis=(1, 3)))
    return count

# --- Parâmetros ---
STEPS = 512# Deve ser potência de 2 para o box-counting ser exato
rule_90_matrix = generate_eca(90, STEPS)

# --- Cálculo da Dimensão Fractal ---
# Usamos tamanhos de caixa que são potências de 2: 1, 2, 4, 8...
scales = [2**i for i in range(0, int(np.log2(STEPS)) - 2)]
counts = [box_count(rule_90_matrix, s) for s in scales]

# Transformação para log-log
print(1.0 / np.array(scales))
print(np.array(counts))
x = np.log(1.0 / np.array(scales))
y = np.log(np.array(counts))


# Regressão Linear (A inclinação 'slope' é a Dimensão Fractal D)
coeffs = np.polyfit(x, y, 1)
fractal_dim = coeffs[0]
print(coeffs)
# --- Visualização ---
fig, ax = plt.subplots(1, 2, figsize=(14, 6))

# Plot do Autômato
ax[0].imshow(rule_90_matrix, cmap='binary', interpolation='nearest')
ax[0].set_title(f"Regra 90 - Evolução Espaço-Tempo\n({STEPS} passos)")
ax[0].axis('off')

# Plot do Box-counting
ax[1].plot(x, y, 'o-', label=f'D estimada: {fractal_dim:.4f}')
ax[1].plot(x, coeffs[0]*x + coeffs[1], '--', label='Ajuste Linear')
ax[1].set_xlabel("log(1/ε)")
ax[1].set_ylabel("log(N)")
ax[1].set_title("Cálculo da Dimensão Fractal (Box-counting)")
ax[1].legend()
ax[1].grid(True)

plt.tight_layout()
plt.show()

print(f"A dimensão fractal calculada para a Regra 90 é: {fractal_dim:.4f}")
print(f"O valor teórico (log 3 / log 2) é: {np.log(3)/np.log(2):.4f}")