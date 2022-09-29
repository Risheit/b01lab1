import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Polynomial {
    public double[] coefficients;
    public int[] exponents;

    // Lab Functions

    public Polynomial() {
        coefficients = null;
        exponents = null;
    }

    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = coefficients == null ? null : coefficients.clone();
        this.exponents = exponents == null ? null : exponents.clone();

        for (var c : this.coefficients) {
            if (c != 0)
                return;
        }
        
        this.coefficients = null;
        this.exponents = null;
    }
    
    public Polynomial(File polyFile) throws FileNotFoundException {
        Pattern noCoefficient = Pattern.compile("^x.*", Pattern.MULTILINE);
        Pattern noExponent = Pattern.compile(".*x$", Pattern.MULTILINE);

        var polyString = makeValidPolyString(readFile(polyFile));
        var polynomial = polyString.split("\\+");
        
        ArrayList<Double> coefficients = new ArrayList<>();
        ArrayList<Integer> exponents = new ArrayList<>();
        for (var term : polynomial) {
            if (!term.contains("x")) { // Case N
                coefficients.add(Double.parseDouble(term));
                exponents.add(0);
            } else if (term == "x") { // Case x
                coefficients.add(1.0);
                exponents.add(1);
            } else if (term.matches(noExponent.toString())) { // Case Nx
                String[] splitTerm = term.split("x");
                coefficients.add(Double.parseDouble(splitTerm[0]));
                exponents.add(1);
            } else if (term.matches(noCoefficient.toString())) { // Case xN
                String[] splitTerm = term.split("x");
                coefficients.add(1.0);
                exponents.add(Integer.parseInt(splitTerm[1]));
            } else { // Case NxN
                String[] splitTerm = term.split("x");
                coefficients.add(Double.parseDouble(splitTerm[0]));
                exponents.add(Integer.parseInt(splitTerm[1]));
            }
        }

        this.coefficients = toDoubleArray(coefficients);
        this.exponents = toIntArray(exponents);
        
        for (var c : this.coefficients) {
            if (c != 0)
                return;
        }
        
        this.coefficients = null;
        this.exponents = null;
    }

    public Polynomial add(Polynomial other) {
        var uniqueExponents = toIntArray(this.getUniqueExponents(other));

        if (uniqueExponents.length == 0)
            return new Polynomial();

        double[] coefficientSum = new double[uniqueExponents.length];
        for (int i = 0; i < coefficientSum.length; i++) {
            coefficientSum[i] = this.getCorrespondingCoefficient(uniqueExponents[i])
                    + other.getCorrespondingCoefficient(uniqueExponents[i]);
        }

        return new Polynomial(coefficientSum, uniqueExponents);
    }

    public double evaluate(double x) {
        double sum = 0.0;
        for (int i = 0; i < numTerms(); i++) {
            sum += coefficients[i] * Math.pow(x, exponents[i]);
        }

        return sum;
    }

    public Polynomial multiply(Polynomial other) {
        double[] newCoefficients = new double[this.numTerms() * other.numTerms()];
        int[] newExponents = new int[this.numTerms() * other.numTerms()];

        if (newExponents.length == 0)
            return new Polynomial();

        int k = 0;
        for (int i = 0; i < this.numTerms(); i++) {
            for (int j = 0; j < other.numTerms(); j++) {
                newCoefficients[k] = this.coefficients[i] * other.coefficients[j];
                newExponents[k] = this.exponents[i] + other.exponents[j];
                k++;
            }
        }
        
        return shrinkPolynomial(new Polynomial(newCoefficients, newExponents));
    }

    public boolean hasRoot(double x) {
        return (evaluate(x) == 0);
    }

    public void saveToFile(String filePath) throws IOException {
        FileWriter polyFile = new FileWriter(filePath);
        polyFile.append(this.toString());
        polyFile.close();
    }

    // Helper functions 

    public String toString() {
        StringBuilder polyString = new StringBuilder();

        if (coefficients == null || exponents == null) {
            return "0";
        }

        for (int i = 0; i < numTerms(); i++) {
            if (coefficients[i] != 1) 
                polyString.append(coefficients[i]);
                
            if (exponents[i] != 0) {
                polyString.append("x");
                if (exponents[i] != 1)
                    polyString.append(exponents[i]);
            }

            if (i + 1 != numTerms() && coefficients[i + 1] >= 0)
                polyString.append("+");
        }

        return polyString.toString();
    }

    public int numTerms() {
        return exponents == null ? 0 : exponents.length;
    }

    public double getCorrespondingCoefficient(int exponent) {
        if (this.exponents == null)
            return 0.0;

        for (int i = 0; i < numTerms(); i++) {
            if (this.exponents[i] == exponent)
                return coefficients[i];
        }

        return 0.0;
    }

    private ArrayList<Integer> getUniqueExponents(Polynomial other) {
        ArrayList<Integer> uniqueExponents = new ArrayList<>();

        for (int exp : this.exponents == null ? new int[]{} : this.exponents) {
            uniqueExponents.add(exp);
        }
        for (int exp : other.exponents == null ? new int[]{} : other.exponents) {
            if (!uniqueExponents.contains(exp))
                uniqueExponents.add(exp);
        }

        uniqueExponents.sort(null);
        return uniqueExponents;
    }

    private static int[] toIntArray(ArrayList<Integer> arrayList) {
        int[] converted = new int[arrayList.size()];

        for (int i = 0; i < converted.length; i++) {
            converted[i] = arrayList.get(i).intValue();
        }

        return converted;
    }

    private static double[] toDoubleArray(ArrayList<Double> arrayList) {
        double[] converted = new double[arrayList.size()];

        for (int i = 0; i < converted.length; i++) {
            converted[i] = arrayList.get(i).doubleValue();
        }

        return converted;
    }

    private static Polynomial shrinkPolynomial(Polynomial polynomial) {
        if (polynomial.numTerms() == 0)
            return polynomial;

        ArrayList<Integer> uniqueExponents = new ArrayList<>();
        ArrayList<Double> uniqueCoefficients = new ArrayList<>();

        for (int i = 0; i < polynomial.numTerms(); i++) {
            if (uniqueExponents.contains(polynomial.exponents[i])) {
                int index = uniqueExponents.indexOf(polynomial.exponents[i]);
                uniqueCoefficients.set(index, uniqueCoefficients.get(index) + polynomial.coefficients[i]);
            } else {
                uniqueExponents.add(polynomial.exponents[i]);
                uniqueCoefficients.add(polynomial.coefficients[i]);
            }
        }

        return new Polynomial(toDoubleArray(uniqueCoefficients), toIntArray(uniqueExponents));
    }

    private static String readFile(File polyFile) throws FileNotFoundException {
        Scanner reader = new Scanner(polyFile);
        String polyString = reader.nextLine();
        reader.close();
        
        return polyString;
    }

    private static String makeValidPolyString(String polyString) {
        StringBuilder valid = new StringBuilder(polyString);

        boolean fullyChecked = false;
        while (!fullyChecked) {
            for (int i = 0; i < valid.length(); i++) {
                if (valid.charAt(i) == '-' && valid.charAt(i - 1) != '+') {
                    valid.insert(i, '+');
                    fullyChecked = false;
                    break;
                }

                fullyChecked = true;
            }
        }

        return valid.toString();
    }
}