public class Polynomial {
    public double[] coefficients;

    public Polynomial() {
        coefficients = new double[] { 0.0 };
    }

    public Polynomial(double[] initArray) {
        coefficients = initArray.clone();
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public int numTerms() {
        return coefficients.length;
    }

    public Polynomial add(Polynomial other) {
        int loopLength = Math.min(this.numTerms(), other.numTerms());
        double[] polySum = new double[Math.max(this.numTerms(), other.numTerms())];

        for (int i = 0; i < loopLength; i++) {
            polySum[i] = this.coefficients[i] + other.coefficients[i];
        }

        Polynomial biggest = this.numTerms() > other.numTerms() ? this : other;
        for (int i = loopLength; i < biggest.numTerms(); i++) {
            polySum[i] = biggest.coefficients[i];
        }

        return new Polynomial(polySum);
    }

    public double evaluate(double x) {
        double sum = 0.0;
        for (int i = 0; i < numTerms(); i++) {
            sum += coefficients[i] * Math.pow(x, i);
        }

        return sum;
    }

    public boolean hasRoot(double x) {
        return (evaluate(x) == 0);
    }
}