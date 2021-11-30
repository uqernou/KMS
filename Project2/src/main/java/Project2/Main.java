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
    List<Double> x_k = new ArrayList<>();
    List<Double> time = new ArrayList<>();
    List<Number>[] Psi = new ArrayList[455000];
    List<Number>[] H = new ArrayList[455000];
    List<Double>[] rho = new ArrayList[455000];
    List<Double>[] params = new ArrayList[455000];

    private String pathToRho = "C:"+ File.separator +"Users"+ File.separator +"uqern"+ File.separator +"Desktop" + File.separator + "KMS" + File.separator + "Dane2" + File.separator + "characteristic_";
    private String pathToData = "C:"+ File.separator +"Users"+ File.separator +"uqern"+ File.separator +"Desktop" + File.separator + "KMS" + File.separator + "Dane2" + File.separator + "data_";


    public static void main(String[] args) {

        long startTime = System.currentTimeMillis();

        Main main = new Main();
        main.symulacja();

        System.out.println("Execution time in milliseconds: " + (System.currentTimeMillis() - startTime));

    }

    private void initParams() {
        double[] parameters = FileUtils.loadParams(SIZE);
        N = parameters[0];
        k = parameters[1];
//        o = parameters[2];
        o = 8.0 * PI / 2.0 ;
        n = parameters[3];
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

    private void symulacja() {
        initParams();
        initX_k();
        initPsiAndH((int) n);
        calcRho(0);
        calcParams(0);
        time.add(0.0);
        for (int i = 1; i < 6000; i++) {
            time.add(i * dTau);
            initPsiInTime(i);
            obliczPsi(i);
            calcRho(i);
            calcParams(i);
        }
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

    private void calcParams(int step){
        params[step] = new ArrayList<>();
        double N = dX * Psi[step].stream().mapToDouble(i ->
                (Math.pow(i.getComplexPart(), 2) + Math.pow(i.getRealPart(), 2))
        ).sum();
        List<Double> list = new ArrayList<>();
        Psi[step].forEach(i -> {
            list.add(Math.pow(i.getComplexPart(), 2) + Math.pow(i.getRealPart(), 2));
        });
        double x = 0;
        for(int i = 0; i < x_k.size(); i++){
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
        FileUtils.saveCharacteristic(pathToData, time, params, step);
    }

    private void calcRho(int step){
        rho[step] = new ArrayList<>();
        Psi[step].forEach(i -> {
            rho[step].add(Math.pow(i.getComplexPart(), 2) + Math.pow(i.getRealPart(), 2));
        });
        FileUtils.saveRho(pathToRho, x_k, rho, step);
    }
}

