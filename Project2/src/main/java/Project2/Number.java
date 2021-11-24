package main.java.Project2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Klasa reprezentujaca liczbe
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Number {
    private double realPart;
    private double complexPart;
}
