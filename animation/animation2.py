import matplotlib.pyplot as plt
import matplotlib.animation as animation
from importlib import reload
import os.path

import numpy as np

nn = ["1", "4", "9"]
oo = ["0", "1", "2"]
kk = ["2", "4", "10"]

for n in range(2, 3):
    for o in range(0, 3):
        for k in range(0, 3):
            reload(plt)
            # %matplotlib notebook
            x, rho, time, norma, polozenie_sr, energia = [], [], [], [], [], []
            x2, rho2, time2, norma2, polozenie_sr2, energia2 = [], [], [], [], [], []

            # subplot(nrows, ncols, index, **kwargs)
            fig = plt.figure()
            figsize = plt.figaspect(2.)

            ax1 = fig.add_subplot(322)

            wykres1 = ax1.plot(list(map(float, time)), list(map(float, norma)), c='green', label='Norma')
            ax1.legend(loc="center right")
            ax1.set_ylabel('N')

            ax3 = fig.add_subplot(324, sharex=ax1)

            wykres2 = ax3.plot(list(map(float, time)), list(map(float, polozenie_sr)), c='yellow',
                               label='Średnie położenie')
            ax3.legend(loc="center right")
            ax3.set_ylabel('x')

            ax4 = fig.add_subplot(326, sharex=ax1)

            wykres3 = ax4.plot(list(map(float, time)), list(map(float, energia)), c='orange', label='Energia')
            ax4.legend(loc="center right")
            ax4.set_xlabel('Time')
            ax4.set_ylabel('E')

            ax2 = fig.add_subplot(1, 2, 1)

            wykres5 = ax2.plot(list(map(float, x)), list(map(float, rho)), c='black',
                               label='Gęstość prawdopodobieństwa')
            ax2.set_ylabel(r'$\rho$')
            plt.xlabel('$x_k$')
            plt.title("Symulacja dla n = " + nn[n])

            if os.path.exists(r"C:\Users\uqern\Desktop\KMS_symulacje\n_" + nn[n] + "_o_" + oo[o] + "_k_" + kk[
                k] + "\data_200000.txt"):
                with open(r"C:\Users\uqern\Desktop\KMS_symulacje\n_" + nn[n] + "_o_" + oo[o] + "_k_" + kk[
                    k] + "\data_200000.txt") as fobj:
                    for line in fobj:
                        row = line.split()
                        time2.append(row[0])
                        norma2.append(row[1])
                        polozenie_sr2.append(row[2])
                        energia2.append(row[3])


            def update(i):
                i = i * 500
                print(i)
                ax2.clear()
                x.clear()
                rho.clear()
                time = [time2[a] for a in range(i + 10000)]
                norma = [norma2[a] for a in range(i + 10000)]
                polozenie_sr = [polozenie_sr2[a] for a in range(i + 10000)]
                energia = [energia2[a] for a in range(i + 10000)]
                if os.path.exists(r"C:\Users\uqern\Desktop\KMS_symulacje\n_" + nn[n] + "_o_" + oo[o] + "_k_" + kk[
                    k] + "\characteristic_" + str(i) + ".txt") and i % 10 == 0:
                    with open(r"C:\Users\uqern\Desktop\KMS_symulacje\n_" + nn[n] + "_o_" + oo[o] + "_k_" + kk[
                        k] + "\characteristic_" + str(i) + ".txt") as fobj:
                        for line in fobj:
                            row = line.split()
                            x.append(row[0])
                            rho.append(row[1])

                wykres5 = ax2.plot(list(map(float, x)), list(map(float, rho)), c='black',
                                   label='Gęstość prawdopodobieństwa')
                wykres1 = ax1.plot(list(map(float, time)), list(map(float, norma)), c='green', label='Norma')
                wykres2 = ax3.plot(list(map(float, time)), list(map(float, polozenie_sr)), c='yellow',
                                   label='Średnie położenie')
                wykres3 = ax4.plot(list(map(float, time)), list(map(float, energia)), c='orange', label='Energia')


            figure = plt.gcf()
            figure.set_size_inches(16, 9)

            ani = animation.FuncAnimation(fig, update, repeat=True, frames=200, interval=20, blit=False)
            f = r"C:\Users\uqern\Desktop\KMS_symulacje\n_" + nn[n] + "_o_" + oo[o] + "_k_" + kk[k] + r"\animation_1.gif"
            writergif = animation.PillowWriter(fps=30)
            ani.save(f, writer=writergif)
