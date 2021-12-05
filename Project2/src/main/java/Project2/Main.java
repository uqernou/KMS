package main.java.Project2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.java.Project2.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Glowna klasa do generowania symulacji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Main {

    private final int SIZE = 4;
    private static final double PI = Math.PI;
    private double N, k, o, dX, tau, n;
    private double dTau = 0.0001;
    private static final int time_step = 210000;
    List<Double> x_k = new ArrayList<>();
    List<Double> time = new ArrayList<>();
    List<Number>[] Psi = new ArrayList[time_step];
    List<Number>[] H = new ArrayList[time_step];
    List<Double>[] rho = new ArrayList[time_step];
    List<Double>[] params = new ArrayList[time_step];
    double omega[] = {3.0 * Math.pow(PI, 2) / 2.0, 5.0 * Math.pow(PI, 2) / 2.0, 8.0 * Math.pow(PI, 2) / 2.0};
    int kappa[] = {2, 4, 10};
    int number[] = {1, 4, 9};

    private String pathToRho = "C:" + File.separator + "Users" + File.separator + "uqern" + File.separator + "Desktop" + File.separator + "KMS_symulacje" + File.separator;
    private String pathToData = "C:" + File.separator + "Users" + File.separator + "uqern" + File.separator + "Desktop" + File.separator + "KMS_symulacje" + File.separator;


    public static void main(String[] args) {

        long startTime2 = System.currentTimeMillis();

        Main main = new Main();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    long startTime = System.currentTimeMillis();
                    main.symulacja(i, j, k);
                    System.out.println("n_" + main.number[i] + "_o_" + j + "_k_" + main.kappa[k]);
                    System.out.println("Execution time in milliseconds: " + (System.currentTimeMillis() - startTime));
                }
            }
        }

        System.out.println("Execution FULL time in milliseconds: " + (System.currentTimeMillis() - startTime2));

    }

    private void initParams(int nn, int oo, int kk) {
        double[] parameters = FileUtils.loadParams(SIZE);
        N = parameters[0];
        n = this.number[nn];
        o = this.omega[oo];
        k = this.kappa[kk];
        dX = 1.0 / N;
    }

    private void initX_k() {
        for (int i = 0; i < N + 1; i++)
            x_k.add(i * dX);
    }

    private void initPsiAndH(int n) {
        initPsiInTime(0);
        for (int i = 0; i < N + 1; i++) {
            Psi[0].add(new Number(Math.sqrt(2) * Math.sin(n * PI * x_k.get(i)), 0));
        }

        for (int i = 0; i < N + 1; i++) {
            if (i == 0 || i == N)
                H[0].add(new Number(0.0, 0.0));
            else
                H[0].add(new Number(H(TypeE.REAL, i, 0, Psi[0]), H(TypeE.COMPLEX, i, 0, Psi[0])));
        }
    }

    private double H(TypeE numberType, int i, int t, List<Number> psi) {
        switch (numberType) {
            case REAL:
                return -1.0 / 2.0 * (psi.get(i + 1).getRealPart() + psi.get(i - 1).getRealPart() - 2 * psi.get(i).getRealPart()) / Math.pow(dX, 2)
                        + k * (x_k.get(i) - 1.0 / 2.0) * psi.get(i).getRealPart() * Math.sin(o * t * dTau);
            case COMPLEX:
                return -1.0 / 2.0 * (psi.get(i + 1).getComplexPart() + psi.get(i - 1).getComplexPart() - 2 * psi.get(i).getComplexPart()) / Math.pow(dX, 2)
                        + k * (x_k.get(i) - 1.0 / 2.0) * psi.get(i).getComplexPart() * Math.sin(o * t * dTau);
            default:
                return 0.0;
        }
    }

    private void symulacja(int nn, int oo, int kk) {
        initParams(nn, oo, kk);
        initX_k();
        generateFolderName(nn, oo, kk);
        initPsiAndH((int) n);
        calcRho(0);
        calcParams(0);
        time.add(0.0);
        for (int i = 1; i < time_step; i++) {
            time.add(i * dTau);
            initPsiInTime(i);
            obliczPsi(i);
            calcRho(i);
            calcParams(i);
        }
        resetData();
    }

    private void obliczPsi(int step) {
        List<Number> psi_half = Psi[step - 1];
        List<Number> psi_full = Psi[step - 1];
        List<Number> H_plus_half = H[step - 1];
        List<Number> H_plus_full = H[step - 1];

        // Obliczanie (32) cz. 1    // Psi_R(t+dt/2)
        for (int i = 0; i < psi_half.size(); i++) {
            psi_half.get(i).setRealPart(psi_half.get(i).getRealPart() +
                    H_plus_half.get(i).getComplexPart() * dTau / 2.0);
        }
        // Obliczanie H real dla t + dt/2
        for (int i = 1; i < H_plus_half.size() - 1; i++) {
            H_plus_half.get(i).setRealPart(H(TypeE.REAL, i, step, psi_half));
        }

        // Obliczanie (33) cz. 1    // Psi_I(t+dt)
        for (int i = 0; i < psi_full.size(); i++) {
            psi_full.get(i).setComplexPart(psi_full.get(i).getComplexPart() -
                    H_plus_half.get(i).getRealPart() * dTau);
        }
        // Obliczanie H complex dla t + dt
        for (int i = 1; i < H_plus_full.size() - 1; i++) {
            H_plus_full.get(i).setComplexPart(H(TypeE.COMPLEX, i, step, psi_full));
        }

        // Obliczanie (34) cz. 1   // Psi_R(t+dt)
        for (int i = 0; i < psi_full.size(); i++) {
            psi_full.get(i).setRealPart(psi_half.get(i).getRealPart() +
                    H_plus_full.get(i).getComplexPart() * dTau / 2.0);
        }
        // Obliczanie H real dla T + dt
        for (int i = 1; i < H_plus_full.size() - 1; i++) {
            H_plus_full.get(i).setRealPart(H(TypeE.REAL, i, step, psi_full));
        }

        // Dodanie nowej list H(t+dt) oraz Psi(t+dt)
        Psi[step] = psi_full;
        H[step] = H_plus_full;
    }

    private void initPsiInTime(int step) {
        Psi[step] = new ArrayList<>();
        H[step] = new ArrayList<>();
    }

    private void calcParams(int step) {
        params[step] = new ArrayList<>();
        double N = dX * Psi[step].stream().mapToDouble(i ->
                (Math.pow(i.getComplexPart(), 2) + Math.pow(i.getRealPart(), 2))
        ).sum();
        List<Double> list = new ArrayList<>();
        Psi[step].forEach(i -> {
            list.add(Math.pow(i.getComplexPart(), 2) + Math.pow(i.getRealPart(), 2));
        });
        double x = 0;
        for (int i = 0; i < x_k.size(); i++) {
            x += x_k.get(i) * list.get(i);
        }
        x = dX * x;
        double E = dX * Psi[step].stream().mapToDouble(i -> (
                (i.getComplexPart() * H[step].get(Psi[step].indexOf(i)).getComplexPart())
                        + (i.getRealPart() * H[step].get(Psi[step].indexOf(i)).getRealPart())
        )).sum();
        params[step].add(N);
        params[step].add(x);
        params[step].add(E);
        if (step % 200000 == 0 && step != 0)
            FileUtils.saveCharacteristic(pathToData, time, params, step);
    }

    private void calcRho(int step) {
        rho[step] = new ArrayList<>();
        Psi[step].forEach(i -> {
            rho[step].add(Math.pow(i.getComplexPart(), 2) + Math.pow(i.getRealPart(), 2));
        });
        if (step % 10 == 0)
            FileUtils.saveRho(pathToRho, x_k, rho, step);
    }

    private void generateFolderName(int nn, int oo, int kk) {
        this.pathToData = "C:" + File.separator + "Users" + File.separator + "uqern" + File.separator + "Desktop" + File.separator + "KMS_symulacje" + File.separator +
                "n_" + this.number[nn] + "_o_" + oo + "_k_" + this.kappa[kk] + File.separator + "data_";
        this.pathToRho = "C:" + File.separator + "Users" + File.separator + "uqern" + File.separator + "Desktop" + File.separator + "KMS_symulacje" + File.separator +
                "n_" + this.number[nn] + "_o_" + oo + "_k_" + this.kappa[kk] + File.separator + "characteristic_";
    }

    private void resetData() {
        x_k = new ArrayList<>();
        time = new ArrayList<>();
        Psi = new ArrayList[time_step];
        H = new ArrayList[time_step];
        rho = new ArrayList[time_step];
        params = new ArrayList[time_step];
    }
}

