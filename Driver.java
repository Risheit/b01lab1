import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Driver {
    public static void main(String[] args) {
        Polynomial pNull = new Polynomial();
        System.out.println(pNull.evaluate(3));
        
        double[] cNull = { 0.0, 0.0, 0.0 };
        int[] eNull = { 0, 1, 2 };
        Polynomial pNull2 = new Polynomial(cNull, eNull);
        Polynomial addNull = pNull.add(pNull2);
        System.out.println("addNull(1.3) = " + addNull.evaluate(1.3));
        Polynomial multiplyNull = pNull.multiply(pNull2);
        System.out.println("multiplyNull(2.3) = " + multiplyNull.evaluate(2.3));
        if (pNull.hasRoot(1))
            System.out.println("1 is a root of pNull");
        else
            System.out.println("1 is not a root of pNull");
        
        double[] c1 = { 6, 5 };
        int[] e1 = {0, 3};
        Polynomial p1 = new Polynomial(c1, e1);
        double[] c2 = {-2, -9 };
        int[] e2 = { 1, 4 };
        Polynomial p2 = new Polynomial(c2, e2);
        Polynomial s = p1.add(p2);
        System.out.println("s(0.1) = " + s.evaluate(0.1));
        Polynomial s2 = p1.multiply(p2);
        System.out.println("s2(0.1) = " + s2.evaluate(0.1));
        if (s.hasRoot(1))
            System.out.println("1 is a root of s");
        else
            System.out.println("1 is not a root of s");

        Polynomial addNullToReg = pNull.add(p1);
        System.out.println("addNullToReg(1) = " + addNullToReg.evaluate(1));
        Polynomial multiplyNullToReg = pNull.multiply(p1);
        System.out.println("multiplyNullToReg(1) = " + multiplyNullToReg.evaluate(1));

        double[] c3 = { 10, 2, 3, 9 };
        int[] e3 = { 0, 2, 3, 5 };
        Polynomial p3 = new Polynomial(c3, e3);
        Polynomial s3 = p1.multiply(p3);
        System.out.println("s3(0.1) = " + s3.evaluate(0.1));

        try {
            pNull.saveToFile("pNull.txt");
        } catch (IOException e) {
            System.err.println(e);
        }
        try {
            s.saveToFile("poly.txt");        
        } catch (IOException e) {
            System.err.println(e);
        }

        File polyFile = new File("poly.txt");
        try {
            Polynomial pFile = new Polynomial(polyFile);
            System.out.println("pFile(0.1) = " + pFile.evaluate(0.1));
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }
}