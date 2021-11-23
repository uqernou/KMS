package main.java.Project2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Glowna klasa do generowania symulacji
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Main {

    public static void main(String[] args){

        long startTime = System.currentTimeMillis();

        Main argon = new Main();

        System.out.println("Execution time in milliseconds: " + (System.currentTimeMillis() - startTime));

    }
}
