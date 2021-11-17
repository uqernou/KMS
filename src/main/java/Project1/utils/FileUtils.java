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

@UtilityClass
public class FileUtils {

    private static Scanner scanner;
    private static int SIZE = 13;
    private static BufferedWriter writer;

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

    public double getRandomKineticEnergy(double k, double T_0){
        return - (1.0 / 2.0) * k * T_0 * Math.log(Math.random());
    }

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
