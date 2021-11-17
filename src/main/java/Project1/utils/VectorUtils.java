package Project1.utils;


import lombok.experimental.UtilityClass;

@UtilityClass
public class VectorUtils {

    public double[] addVectors(double[] vec0, double[] vec1){
        return new double[]{
                vec0[0] + vec1[0],
                vec0[1] + vec1[1],
                vec0[2] + vec1[2]
        };
    }

    public double[] addThreeVectors(double[] vec0, double[] vec1, double[] vec2){
        return new double[]{
                vec0[0] + vec1[0] + vec2[0],
                vec0[1] + vec1[1] + vec2[1],
                vec0[2] + vec1[2] + vec2[2]
        };
    }

    public double[] subtractVectors(double[] vec0, double[] vec1){
        return new double[]{
                vec0[0] - vec1[0],
                vec0[1] - vec1[1],
                vec0[2] - vec1[2]
        };
    }

    public double lenghtVector(double[] vec0){
        return Math.sqrt(
                Math.pow(vec0[0], 2) + Math.pow(vec0[1], 2) + Math.pow(vec0[2], 2)
        );
    }

    public double[] multipleByConst(double constance, double[] vec0){
        return new double[]{
                vec0[0] * constance,
                vec0[1] * constance,
                vec0[2] * constance
        };
    }

    public double[] randomSignUnitVector(){
        double plus = 1.0;
        double minus = -1.0;
        return new double[] {
                Math.random() > 0.5 ? plus : minus,
                Math.random() > 0.5 ? plus : minus,
                Math.random() > 0.5 ? plus : minus
        };
    }
}
