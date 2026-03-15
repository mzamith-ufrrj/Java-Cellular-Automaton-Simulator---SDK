import pandas as pd
import matplotlib.pyplot as plt
if __name__=="__main__":
    print("Exibindo a densidade - *** Incluiu o t=0 ****")
    df_p = pd.read_csv("R110-central-point.csv", comment='#', delimiter=';')
    df_r = pd.read_csv("R110-random-025.csv", comment='#', delimiter=';')
    
    plt.figure(figsize=(10, 6))
    plt.plot(df_p['timestep'], df_p['entropy'], 'o-', label='Rule 90 - Point', color='blue')
    plt.plot(df_r['timestep'], df_r['entropy'], 's-', label='Rule 90 - random', color='red')
    
    plt.xlabel('Timesteps')
    plt.ylabel('Entropy')
    plt.grid(True, linestyle='--', alpha=0.7)
    plt.legend()
    plt.show()