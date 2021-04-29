import java.util.Random;

public class Main {
    public static void main(String[] args) {
        //Set these to whatever you want
        boolean calculate = false;
        boolean displayAll = false;
        int range = 10000000;
        int iterations = 500;
        double rangeMin = 0.01;
        double rangeMax = 1;

        double[] as = new double[range];
        double[] bs = new double[range];

        //Generate random axes values
        Random random = new Random();
        for(int i = 0; i < range; i++) {
            as[i] = Math.abs(rangeMin + (rangeMax - rangeMin) * random.nextDouble());
            bs[i] = Math.abs(rangeMin + (rangeMax - rangeMin) * random.nextDouble());
        }

        //Calculate exact perimeters
        double[] exacts = calculateRange(new Ellipse.Exact(iterations), as, bs, true);
        System.out.println("PARKER\t\t\t\t" + leastSquaresError(new Ellipse.Parker(), exacts,as,bs));
        System.out.println("e/2   \t\t\t\t" + leastSquaresError(new Ellipse.ExponentConstant(Math.E / 2), exacts, as, bs));
        if(displayAll) {
            System.out.println("2*dottie  \t\t\t" + leastSquaresError(new Ellipse.ExponentConstant(1.4781702664303213), exacts, as, bs));
            System.out.println("phi   \t\t\t\t" + leastSquaresError(new Ellipse.ExponentConstant((1 + Math.sqrt(5)) / 2), exacts, as, bs));
            System.out.println("1.5   \t\t\t\t" + leastSquaresError(new Ellipse.ExponentConstant(1.5), exacts, as, bs));
            System.out.println("pi/2  \t\t\t\t" + leastSquaresError(new Ellipse.ExponentConstant(Math.PI / 2), exacts, as, bs));
            System.out.println("root2 \t]t]t\t" + leastSquaresError(new Ellipse.ExponentConstant(Math.sqrt(2)), exacts, as, bs));
            System.out.println("root3 \t\t\t\t" + leastSquaresError(new Ellipse.ExponentConstant(Math.sqrt(3)), exacts, as, bs));
        }
        System.out.println("Constant\t\t\t" + leastSquaresError(new Ellipse.ExponentConstant(1.4665039242799713), exacts, as, bs));

        if(calculate)
            approximateValue(exacts, as, bs);
    }

    //Very crudely check for optimal value
    static void approximateValue(double[] exacts, double[] as, double[] bs){
        double base = 0, error = 0, delta = 1;
        for(int j = 0; j < 64; j++, delta /= 10) {
            double[] errors = new double[19];
            double[] constants = new double[19];
            for (int i = -9; i <= 9; i++) {
                constants[i+9] = base + delta * i;
                errors[i+9] = Math.abs(leastSquaresError(new Ellipse.ExponentConstant(constants[i+9]), exacts, as, bs));
            }
            int minIndex = 0;
            for(int i = 1; i < errors.length; i++) {
                if(Double.isNaN(errors[minIndex]) || errors[i] < errors[minIndex])
                    minIndex = i;
            }
            base = constants[minIndex];
            error = errors[minIndex];
        }
        System.out.println("CONSTANT : " + base);
        System.out.println("ERROR : " + error);
    }

    //Interface to treat perimeter functions as lambdas
    static abstract class Ellipse {
        double calculate(double a, double b) {
            return b > a ? _calculate(b , a) : _calculate(a , b);
        }
        protected abstract double _calculate(double a, double b);

        //Summation implementation
        static class Exact extends Ellipse {
            int iterations;
            public Exact(int iterations) {
                this.iterations = iterations;
            }
            @Override protected double _calculate(double a, double b) {
                double e2 = (a * a - b * b) / (a * a),
                        exact = 1,
                        coefficient = 1;
                for (int i = 1, j = 0; i <= 2 * iterations; i += 2, j++) {
                    double f = i / (i + 1D);
                    coefficient *= f * f * e2;
                    exact -= coefficient / i;
                }
                return 2 * a * Math.PI * exact;
            }
        }

        //Matt Parker Implementation
        static class Parker extends Ellipse {
            @Override protected double _calculate(double a, double b) {
                return Math.PI * (6 * a / 5 + 3 * b / 4);
            }
        }
        //Based on Three Feet of Air Implementation
        static class ExponentConstant extends Ellipse {
            double constant;
            public ExponentConstant(double constant) {
                this.constant = constant;
            }
            @Override protected double _calculate(double a, double b) {
                return a * ((2 * Math.PI - 4)*Math.pow(b / a,constant) + 4);
            }
        }
    }

    //Calculates perimeter array for axes values
    static double[] calculateRange(Ellipse ellipse, double[] as, double[] bs, boolean print) {
        double[] results = new double[as.length];
        for(int i = 0; i < as.length; i++) {
            if(print)
                System.out.println(i);
            results[i] = ellipse.calculate(as[i], bs[i]);
        }
        return results;
    }

    //Calculates least squares error for lambda, axes values, and expected perimeters
    public static double leastSquaresError(Ellipse ellipse, double[] expected, double[] as, double[] bs) {
        double[] actual = calculateRange(ellipse, as, bs,false);
        double error = 0;
        for(int i = 0; i < actual.length; i++) {
            double cur = expected[i] - actual[i];
            error += cur * cur;
        }
        return error;
    }


}