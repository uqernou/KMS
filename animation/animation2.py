import matplotlib.pyplot as plt
import matplotlib.animation as animation
from importlib import reload
import os.path

import numpy as np

reload(plt)
# %matplotlib notebook
x, rho, time, norma, polozenie_sr, energia = [], [], [], [], [], []
# subplot(nrows, ncols, index, **kwargs)
fig = plt.figure()
figsize = plt.figaspect(2.)

ax1 = fig.add_subplot(322)

wykres1 = ax1.plot(list(map(float, time)), list(map(float, norma)), c='green', label='Norma')
ax1.legend(loc="center right")
ax1.set_ylabel('N')

ax3 = fig.add_subplot(324, sharex=ax1)

wykres2 = ax3.plot(list(map(float, time)), list(map(float, polozenie_sr)), c='yellow', label='Średnie położenie')
ax3.legend(loc="center right")
ax3.set_ylabel('x')

ax4 = fig.add_subplot(326, sharex=ax1)

wykres3 = ax4.plot(list(map(float, time)), list(map(float, energia)), c='orange', label='Energia')
ax4.legend(loc="center right")
ax4.set_xlabel('Time')
ax4.set_ylabel('E')

ax2 = fig.add_subplot(1, 2, 1)

wykres5 = ax2.plot(list(map(float, x)), list(map(float, rho)), c='black', label='Gęstość prawdopodobieństwa')
ax2.set_ylabel(r'$\rho$')
plt.xlabel('$x_k$')

def update(i):
    print(i)
    x.clear()
    rho.clear()
    time.clear()
    norma.clear()
    polozenie_sr.clear()
    energia.clear()
    if os.path.exists(r"C:\Users\uqern\Desktop\KMS\Dane2\characteristic_" + str(i) + ".txt") and i % 4 == 0:
        with open(r"C:\Users\uqern\Desktop\KMS\Dane2\characteristic_" + str(i) + ".txt") as fobj:
            for line in fobj:
                row = line.split()
                x.append(row[0])
                rho.append(row[1])
    if os.path.exists(r"C:\Users\uqern\Desktop\KMS\Dane2\data_" + str(i) + ".txt")  and i % 4 == 0:
        with open(r"C:\Users\uqern\Desktop\KMS\Dane2\data_" + str(i) + ".txt") as fobj:
            for line in fobj:
                row = line.split()
                time.append(row[0])
                norma.append(row[1])
                polozenie_sr.append(row[2])
                energia.append(row[3])

    wykres5 = ax2.plot(list(map(float, x)), list(map(float, rho)), c='black', label='Gęstość prawdopodobieństwa')
    wykres1 = ax1.plot(list(map(float, time)), list(map(float, norma)), c='green', label='Norma')
    wykres2 = ax3.plot(list(map(float, time)), list(map(float, polozenie_sr)), c='yellow', label='Średnie położenie')
    wykres3 = ax4.plot(list(map(float, time)), list(map(float, energia)), c='orange', label='Energia')



figure = plt.gcf()
figure.set_size_inches(16, 9)

ani = animation.FuncAnimation(fig, update, repeat=True, frames=4000, interval=20, blit=False)
f = r"C:\Users\uqern\Desktop\KMS\Dane2\animation_1.gif"
writergif = animation.PillowWriter(fps=30)
ani.save(f, writer=writergif)

plt.show()