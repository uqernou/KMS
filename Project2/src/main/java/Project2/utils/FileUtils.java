package main.java.Project2.utils;

import lombok.experimental.UtilityClass;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;

/**
 * Klasa Utilsowa do operacji na plikach
 */
@UtilityClass
public class FileUtils {

    private static Scanner scanner;
    private static BufferedWriter writer;
    private static final String path = "C:\\Users\\uqern\\IdeaProjects\\KMS\\Project2\\parametry2.txt";

    /**
     * Metoda pobierajaca parametry z pliku wejsciowego
     *
     * @param nrOfParams - Liczba parametrow do wczytania
     * @return Tablica parametrow podczytana z pliku
     */
    public double[] loadParams(int nrOfParams) {
        double[] params = new double[nrOfParams];
        File file = new File(path);
        try {
            scanner = new Scanner(file);
            int i = 0;
            while (scanner.hasNextLine()) {
                String dane = scanner.nextLine();
                params[i] = Double.parseDouble(dane.substring(0, dane.indexOf("#") - 1));
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return params;
    }

    public void saveRho(String path, List<Double> x_k, List<Double>[] rho, int counter) {
        try {
            writer = Files.newBufferedWriter(Paths.get(path + counter + ".txt"), StandardOpenOption.CREATE);
            for (int i = 0; i < rho[counter].size(); i++) {
                try {
                    writer.write(x_k.get(i) + " " + rho[counter].get(i));
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCharacteristic(String path, List<Double> t, List<Double>[] param, int counter) {
        try {
            writer = Files.newBufferedWriter(Paths.get(path + counter + ".txt"), StandardOpenOption.CREATE);
            for (int i = 0; i < counter; i++) {
                try {
                    writer.write(t.get(i) + " " + param[i].get(0) + " " + param[i].get(1) + " " + param[i].get(2));
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
