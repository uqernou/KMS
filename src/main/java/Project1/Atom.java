package Project1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa reprezentująca atom Argonu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atom {

    private Long id;
    private List<double[]> r = new ArrayList<>();
    private List<double[]> p = new ArrayList<>();
    private double[] V = new double[3];
    private List<double[]> F = new ArrayList<>();
    private double[] p_tmp = new double[3];
}
