import sys
import numpy as np
import matplotlib.pyplot as plt
  
def plot(num):
    file_name = "Times" + num + ".txt"
    print(file_name)
    (ls_size, ls_time) = data_from_file(file_name)
    x = np.array(ls_size)
    y = np.array(ls_time)
    plt.plot(x, y, '.', label = "Algo " + num)
    
def data_from_file(file_name):
    f = open(file_name)
    ls_size = list(map(int, f.readline().split()))
    ls_time = list(map(int, f.readline().split()))
    return (ls_size, ls_time)
    
if __name__ == "__main__":
    print(sys.argv[1])
    plot(sys.argv[1])
    plot(sys.argv[2])
    plt.xlabel('Taille des listes')
    plt.ylabel('Temps')
    plt.legend()
    plt.show()




