package Project1.utils;

import Project1.Atom;
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
    private static int SIZE = 13;
    private static BufferedWriter writer;

    /**
     * Metoda pobierająca parametry z pliku wejściowego
     * @param fileName - Nazwa pliku umieszczonego w programie
     * @return Tablica parametrów podczytana z pliku
     */
    public double[] loadParams(String fileName){
        double[] params = new double[SIZE];
        File file = new File("src" + File.separator + fileName);
        try {
            scanner = new Scanner(file);
            int i = 0;
            while(scanner.hasNextLine()) {
                String dane = scanner.nextLine();
                params[i] = Double.parseDouble(dane.substring(0, dane.indexOf("#") - 1));
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return params;
    }


    /**
     * Metoda zapisująca położenie XYZ wszystkich atomów do danego kroku czasowego
     * @param path    - Ścieżka zapisu pliku
     * @param atoms   - Lista atomów
     * @param step    - Krok czasowy symulacji
     * @param counter - Licznik
     */
    public void saveXYZ(String path, List<Atom> atoms, int step, int counter){
        try {
            writer = Files.newBufferedWriter(Paths.get(path + counter + ".txt"), StandardOpenOption.CREATE);
            atoms.forEach(atom -> {
                try {
                    writer.write(atom.getR().get(step)[0] + " " + atom.getR().get(step)[1] + " " + atom.getR().get(step)[2]);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Metoda zapisująca charaketrystyki do pliku
     * @param path    - Ścieżka zapisu pliku
     * @param T       - Lista temperatur
     * @param V       - Lista potencjałów
     * @param p       - Lista pędów
     * @param t       - Lista czasów tau
     * @param counter - Licznik
     */
    public void saveCharacteristics(String path, List<Double> T, List<Double> V, List<Double> p, List<Double> t, int counter){
        try {
            writer = Files.newBufferedWriter(Paths.get(path + counter + ".txt"), StandardOpenOption.CREATE);
            for(int i = 0; i < t.size(); i++) {
                try {
                    writer.write(t.get(i) + " " + T.get(i) + " " + p.get(i) + " " + V.get(i));
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            writer.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
