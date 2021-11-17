package Project1;

import Project1.utils.FileUtils;
import Project1.utils.VectorUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Argon {

    private double n, m, e, R, f, L, a, T_0, tau, S_o, S_d, S_out, S_xyz;
    private double N;

    private final double k_B = 8.31e-3;   // [J/K]
    private double[] b_0 = new double[3], b_1 = new double[3], b_2 = new double[3];
    private List<Atom> atoms = new ArrayList<>();
    private List<Double> V = new ArrayList<>();
    private List<Double> P = new ArrayList<>();
    private List<Double> T = new ArrayList<>();
    private List<Double> t = new ArrayList<>();
    private List<Double> H = new ArrayList<>();
    private String pathToXYZ = "C:"+ File.separator +"Users"+ File.separator +"uqern"+ File.separator +"Desktop" + File.separator + "KMS" + File.separator + "Dane" + File.separator + "data_";
    private String pathToCharacteristic = "C:"+ File.separator +"Users"+ File.separator +"uqern"+ File.separator +"Desktop" + File.separator + "KMS" + File.separator + "Dane" + File.separator + "characteristic_";

    public static void main(String[] args){

        long startTime = System.currentTimeMillis();

        Argon argon = new Argon();
        argon.initParams("parametry.txt");

        argon.symulacja();

        System.out.println("Execution time in milliseconds: " + (System.currentTimeMillis() - startTime));

    }

    private void symulacja(){
        int counter = 0;
        for (int i = 1; i < this.S_o + this.S_d; i++) {
            nextPedPolozenie(i);
            t.add((double) i * this.tau);
            T.add(T_i(i));
            if (i % this.S_xyz == 0) {
                counter++;
                FileUtils.saveXYZ(pathToXYZ, atoms, i, counter);
                FileUtils.saveCharacteristics(pathToCharacteristic, T, V, P, t, counter);
            }
//            if (i >= this.S_o)
//                T.add(T_i(i));
        }
        double t_sum = 1.0 / (double) T.size() * T.stream().mapToDouble(e -> e).sum();
        double v_sum = 1.0 / (double) V.size() * V.stream().mapToDouble(e -> e).sum();
        double p_sum = 1.0 / (double) (P.size() - this.S_o) * P_sr();
        double h_sum = 1.0 / (double) (H.size() - this.S_o) * H_sr();
        System.out.println("T_sr: " + t_sum + "\nV_sr: "+ v_sum + "\nP_sr: "+ p_sum + "\nH_sr: " + h_sum);

    }

    private void initParams(String fileName){
        double[] parameters = FileUtils.loadParams(fileName);

        n = parameters[0];
        m = parameters[1];
        e = parameters[2];
        R = parameters[3];
        f = parameters[4];
        L = parameters[5];
        a = parameters[6];
        T_0 = parameters[7];
        tau = parameters[8];
        S_o = parameters[9];
        S_d = parameters[10];
        S_out = parameters[11];
        S_xyz = parameters[12];

        N = Math.pow(n, 3);

        ustawWektoryB();
        inicjalizujAtomy();
        obliczPotecjalAndSily(0);
    }

    private void ustawWektoryB(){
        b_0[0] = a;
        b_0[1] = 0;
        b_0[2] = 0;

        b_1[0] = a * 0.5;
        b_1[1] = a * Math.sqrt(3) / 2.0;
        b_1[2] = 0;

        b_2[0] = a * 0.5;
        b_2[1] = a * Math.sqrt(3) / 6.0;
        b_2[2] = a * Math.sqrt(2.0/3.0);
    }

    private void inicjalizujAtomy(){
        for(int i = 0 ; i < Math.pow(n, 3); i++) {
            Atom atom = new Atom();
            atom.setId((long) i);
            atoms.add(atom);
        }

        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                for(int k = 0; k < n; k++){
                    int i_x = (int) ( i + j * n + k * n * n);

                    double[] r = VectorUtils.addThreeVectors(
                            VectorUtils.multipleByConst((i - (n - 1.0)/2.0), b_0),
                            VectorUtils.multipleByConst((j - (n - 1.0)/2.0), b_1),
                            VectorUtils.multipleByConst((k - (n - 1.0)/2.0), b_2)
                    );

                    double[] kierunekPedu = VectorUtils.randomSignUnitVector();
                    double[] p = {
                            kierunekPedu[0] * Math.sqrt(2.0 * m * FileUtils.getRandomKineticEnergy(this.k_B, this.T_0)),
                            kierunekPedu[1] * Math.sqrt(2.0 * m * FileUtils.getRandomKineticEnergy(this.k_B, this.T_0)),
                            kierunekPedu[2] * Math.sqrt(2.0 * m * FileUtils.getRandomKineticEnergy(this.k_B, this.T_0))
                    };
                    atoms.get(i_x).getR().add(r);
                    atoms.get(i_x).getP().add(p);
                }
    }

    private void obliczPotecjalAndSily(int step){
        double V = 0.0;
        double P = 0.0;
        initForce();
        for(int i = 0; i < this.atoms.size(); i++){

            for(int j = i + 1; j < this.atoms.size(); j++){

//                V += V_ij(this.atoms.get(i), this.atoms.get(j), step);
//
//                double[] Fp = Fp_i(this.atoms.get(i), this.atoms.get(j), step);
//
//                this.atoms.get(i).getF().get(step)[0] += Fp[0];
//                this.atoms.get(i).getF().get(step)[1] += Fp[1];
//                this.atoms.get(i).getF().get(step)[2] += Fp[2];
//
//                this.atoms.get(j).getF().get(step)[0] -= Fp[0];
//                this.atoms.get(j).getF().get(step)[1] -= Fp[1];
//                this.atoms.get(j).getF().get(step)[2] -= Fp[2];
            }

            V += Vs_ri(this.atoms.get(i), step);

            double[] Fs = Fs_i(this.atoms.get(i), step);
            this.atoms.get(i).getF().get(step)[0] += Fs[0];
            this.atoms.get(i).getF().get(step)[1] += Fs[1];
            this.atoms.get(i).getF().get(step)[2] += Fs[2];
            P += VectorUtils.lenghtVector(Fs);
        }
        P = 1.0/(4.0 * Math.PI * Math.pow(this.L, 2)) * P;
        this.P.add(P);
        this.V.add(V);
    }

    private double R_ij(double[] v1, double[] v2){
        return VectorUtils.lenghtVector(VectorUtils.subtractVectors(v1, v2));
    }

    private double V_ij(Atom a1, Atom a2, int step){
        return this.e * (
                Math.pow(this.R / R_ij(a1.getR().get(step), a2.getR().get(step)), 12) -
                        2.0 * Math.pow(this.R / R_ij(a1.getR().get(step), a2.getR().get(step)), 6)
        );
    }

    private double Vs_ri(Atom a, int step){
        return VectorUtils.lenghtVector(a.getR().get(step)) < this.L ?
                0.00 :
                (1.00 / 2.00) * this.f * Math.pow((VectorUtils.lenghtVector(a.getR().get(step)) - this.L), 2);
    }

    private double[] Fp_i(Atom a1, Atom a2, int step){
        double r_ij_lenght = VectorUtils.lenghtVector(
                VectorUtils.subtractVectors(
                        a1.getR().get(step), a2.getR().get(step)));
        double constant = 12.0 * this.e * (
                Math.pow(this.R / r_ij_lenght, 12) - Math.pow(this.R / r_ij_lenght, 6)) /
                Math.pow(r_ij_lenght, 2);

        return VectorUtils.multipleByConst(constant,
                VectorUtils.subtractVectors(a1.getR().get(step), a2.getR().get(step))
        );
    }

    private double[] Fs_i(Atom a, int step){
        double r_lenght = VectorUtils.lenghtVector(a.getR().get(step));
        double constant = r_lenght < this.L ?
                0.00 : this.f * (this.L - r_lenght) / r_lenght;

        return VectorUtils.multipleByConst(constant, a.getR().get(step));
    }

    private void nextPedPolozenie(int step){
        this.atoms.forEach(a -> {
            a.setP_tmp(new double[3]);
            double[] pi_tmp = VectorUtils.addVectors(
                    a.getP().get(step - 1),
                    VectorUtils.multipleByConst( this.tau / 2.0, a.getF().get(step - 1))
            );
            a.setP_tmp(pi_tmp);
            double[] ri_next = VectorUtils.addVectors(
                    a.getR().get(step - 1),
                    VectorUtils.multipleByConst( this.tau / this.m, pi_tmp)
            );

            a.getR().add(ri_next);
        });
        obliczPotecjalAndSily(step);

        this.atoms.forEach(a -> {
            double[] pi_next = VectorUtils.addVectors(
                    a.getP_tmp(),
                    VectorUtils.multipleByConst(this.tau / 2.0, a.getF().get(step))
            );
            a.getP().add(pi_next);
        });
//        System.out.println((H_i(step)+"").replace(".", ","));
//        System.out.println("T(" + step + ") = " + T_i(step));
//        System.out.println((this.tau*step + " " + H_i(step)).replace(".", ","));
        this.H.add(H_i(step));
    }

    private double T_i(int step){
        return 2.0 / (3.0 * this.N * this.k_B) * this.atoms.stream().mapToDouble(a -> Math.pow(VectorUtils.lenghtVector(a.getP().get(step)), 2) / (2.0 * this.m)).sum();
    }

    private double H_i(int step){
        return (this.atoms.stream().mapToDouble(a -> Math.pow(VectorUtils.lenghtVector(a.getP().get(step)), 2) / (2.0 * this.m)).sum() + this.V.get(step));
    }

    private double P_sr(){
        return (this.P.stream()
                .filter(e -> P.indexOf(e) > S_o)
                .mapToDouble(ped -> ped).sum());
    }

    private double H_sr(){
        return (this.H.stream()
                .filter(e -> H.indexOf(e) > S_o)
                .mapToDouble(h -> h).sum());
    }

    private void initForce(){
        this.atoms.forEach(e -> e.getF().add(new double[] {0.0, 0.0, 0.0}));
    }

    public static double[] averageAndStd(List<Double> list){
        final double srednia =  list.stream().mapToDouble(e -> e).sum() / (double) list.size();
        final double variance = Math.sqrt(1.0 / (double) list.size()
                * list.stream().mapToDouble(e -> Math.pow(e - srednia, 2)).sum());

        return new double[] {srednia, variance};
    }

}
