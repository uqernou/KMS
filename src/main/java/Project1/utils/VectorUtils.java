package Project1.utils;


import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * Klasa Utilsowa do operacji matematycznych
 */
@UtilityClass
public class VectorUtils {

    /**
     * Metoda dodająca dwa wektory do siebie
     * @param vec0 - Wektor 1
     * @param vec1 - Wektor 2
     * @return Wynik dodawania dwóch wektorów
     */
    public double[] addVectors(double[] vec0, double[] vec1){
        return new double[]{
                vec0[0] + vec1[0],
                vec0[1] + vec1[1],
                vec0[2] + vec1[2]
        };
    }

    /**
     * Metoda dodająca trzy wektory do siebie
     * @param vec0 - Wektor 1
     * @param vec1 - Wektor 2
     * @param vec2 - Wektor 3
     * @return Wynik dodawania trzech wektorów
     */
    public double[] addThreeVectors(double[] vec0, double[] vec1, double[] vec2){
        return new double[]{
                vec0[0] + vec1[0] + vec2[0],
                vec0[1] + vec1[1] + vec2[1],
                vec0[2] + vec1[2] + vec2[2]
        };
    }

    /**
     * Metoda odejmująca dwa wektory do siebie
     * @param vec0 - Wektor 1
     * @param vec1 - Wektor 2
     * @return Wynik odejmowania dwóch wektorów
     */
    public double[] subtractVectors(double[] vec0, double[] vec1){
        return new double[]{
                vec0[0] - vec1[0],
                vec0[1] - vec1[1],
                vec0[2] - vec1[2]
        };
    }

    /**
     * Metoda zwracająca długość wektora
     * @param vec0 - Wektor 1
     * @return Długość wektora
     */
    public double lenghtVector(double[] vec0){
        return Math.sqrt(
                Math.pow(vec0[0], 2) + Math.pow(vec0[1], 2) + Math.pow(vec0[2], 2)
        );
    }

    /**
     * Metoda przemnażająca wektor przez wartość stałą
     * @param constance - wartość stała
     * @param vec0      - Wektor 1
     * @return Wynik przemnożenia wektora przez stałą
     */
    public double[] multipleByConst(double constance, double[] vec0){
        return new double[]{
                vec0[0] * constance,
                vec0[1] * constance,
                vec0[2] * constance
        };
    }

    /**
     * Metoda losująca wektor składający się z +/- 1
     * @return Wektor
     */
    public double[] randomSignUnitVector(){
        double plus = 1.0;
        double minus = -1.0;
        return new double[] {
                Math.random() > 0.5 ? plus : minus,
                Math.random() > 0.5 ? plus : minus,
                Math.random() > 0.5 ? plus : minus
        };
    }

    /**
     * Metoda zwracająca losową wartość energii kinetycznej
     * @param k   - Stała Boltzmana
     * @param T_0 - Temperatura początkowa układu
     * @return Losowa wartość energii kinetycznej
     */
    public double getRandomKineticEnergy(double k, double T_0){
        return - (1.0 / 2.0) * k * T_0 * Math.log(Math.random());
    }

    /**
     * Metoda obliczająca średnią oraz odchylenie standarowe na podstawie danej listy
     * @param list - Uniwersalna lista danych
     * @return Tablica [0] - wartość średnia, [1] - odchylenie standardowe
     */
    public static double[] averageAndStd(List<Double> list){
        final double srednia =  list.stream().mapToDouble(e -> e).sum() / (double) list.size();
        final double variance = Math.sqrt(1.0 / (double) list.size()
                * list.stream().mapToDouble(e -> Math.pow(e - srednia, 2)).sum());

        return new double[] {srednia, variance};
    }
}
