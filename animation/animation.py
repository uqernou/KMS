import matplotlib.pyplot as plt
import matplotlib.animation as animation
from importlib import reload
import os.path

import numpy as np

reload(plt)
# %matplotlib notebook
time, potential, pressure, temperature, = [], [], [], []
# subplot(nrows, ncols, index, **kwargs)
fig = plt.figure()
figsize = plt.figaspect(2.)

ax1 = fig.add_subplot(322)

wykres1 = ax1.plot(list(map(float, time)), list(map(float, temperature)), c='green', label='Temperature T')
ax1.legend(loc="center right")
ax1.set_ylabel('Temperature [K]')
plt.xlim(0, 4.2)


ax3 = fig.add_subplot(324, sharex=ax1)

wykres2 = ax3.plot(list(map(float, time)), list(map(float, pressure)), c='yellow', label='Pressure p')
ax3.legend(loc="center right")
ax3.set_ylabel('Pressure [Pa]')

ax4 = fig.add_subplot(326, sharex=ax1)

wykres3 = ax4.plot(list(map(float, time)), list(map(float, potential)), c='orange', label='Potential V')
ax4.legend(loc="center right")
ax4.set_xlabel('Time [ps]')
ax4.set_ylabel('Potential [J]')

ax2 = fig.add_subplot(1, 2, 1, projection='3d')
ax2.set_zlim(-2.3, 2.3)
plt.xlim(-2.3, 2.3)
plt.ylim(-2.3, 2.3)
x_data, y_data, z_data = [], [], []


def drawSphere(xCenter, yCenter, zCenter, r):
    u, v = np.mgrid[0:2 * np.pi:20j, 0:np.pi:10j]
    x = np.cos(u) * np.sin(v)
    y = np.sin(u) * np.sin(v)
    z = np.cos(v)

    x = r * x + xCenter
    y = r * y + yCenter
    z = r * z + zCenter
    return x, y, z


def update(i):
    print(i)
    time.clear()
    pressure.clear()
    potential.clear()
    temperature.clear()
    x_data.clear()
    y_data.clear()
    z_data.clear()
    if os.path.exists(r"C:\Users\uqern\Desktop\KMS\Dane\data_" + str(i) + ".txt"):
        with open(r"C:\Users\uqern\Desktop\KMS\Dane\data_" + str(i) + ".txt") as fobj:
            for line in fobj:
                row = line.split()
                x_data.append(row[0])
                y_data.append(row[1])
                z_data.append(row[2])
        graph._offsets3d = np.array(
            (list(map(float, x_data)), list(map(float, y_data)), list(map(float, z_data)))
        )
    if os.path.exists(r"C:\Users\uqern\Desktop\KMS\Dane\characteristic_" + str(i) + ".txt"):
        with open(r"C:\Users\uqern\Desktop\KMS\Dane\characteristic_" + str(i) + ".txt") as fobj:
            for line in fobj:
                row = line.split()
                time.append(row[0])
                temperature.append(row[1])
                pressure.append(row[2])
                potential.append(row[3])
    wykres1 = ax1.plot(list(map(float, time)), list(map(float, temperature)), c='green', label='Temperature T')
    wykres2 = ax3.plot(list(map(float, time)), list(map(float, pressure)), c='yellow', label='Pressure p')
    wykres3 = ax4.plot(list(map(float, time)), list(map(float, potential)), c='orange', label='Potential V')
    if i == 0:
        fig.suptitle('Time: ' + str(0) + ' ps\n' +
                     'Temperature' + str(0) + ' K\n' +
                     'Potential' + str(0) + ' J\n' +
                     'Pressure' + str(0) + ' Pa\n', fontsize=16, x=0.1)
    else:
        fig.suptitle('Time: ' + str("{:.3f}".format(float(time[len(time) - 1]))) + ' ps\n' +
                     'Temperature: ' + str("{:.3f}".format(float(temperature[len(temperature) - 1]))) + ' K\n' +
                     'Potential: ' + str("{:.3f}".format(float(potential[len(potential) - 1]))) + ' J\n' +
                     'Pressure: ' + str("{:.3f}".format(float(pressure[len(pressure) - 1]))) + ' Pa\n',
                     fontsize=16,
                     x=0.1)
    return graph,


figure = plt.gcf()
graph = ax2.scatter(list(map(float, x_data)), list(map(float, y_data)), list(map(float, z_data)), c="blue", s=100)
(xs, ys, zs) = drawSphere(0, 0, 0, 2.3)
ax2.plot_wireframe(xs, ys, zs, color='0.8', linewidth=0.5)
ax2.set_xlabel("x [nm]")
ax2.set_ylabel("y [nm]")
ax2.set_zlabel("z [nm]")
ax2.legend([graph], ['Argon atoms'], numpoints=1)
figure.set_size_inches(16, 9)

ani = animation.FuncAnimation(fig, update, repeat=True, frames=409, interval=50, blit=False)
f = r"C:\Users\uqern\Desktop\KMS\Dane\animation_2000k.gif"
writergif = animation.PillowWriter(fps=30)
ani.save(f, writer=writergif)
#
# plt.show()
