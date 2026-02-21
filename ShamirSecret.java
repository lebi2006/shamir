import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;
import com.google.gson.*;

class Fraction {

    BigInteger num;
    BigInteger den;

    Fraction(BigInteger n, BigInteger d) {
        BigInteger gcd = n.gcd(d);
        num = n.divide(gcd);
        den = d.divide(gcd);

        if (den.compareTo(BigInteger.ZERO) < 0) {
            num = num.negate();
            den = den.negate();
        }
    }

    Fraction add(Fraction other) {

        BigInteger n =
        num.multiply(other.den)
        .add(other.num.multiply(den));

        BigInteger d =
        den.multiply(other.den);

        return new Fraction(n,d);
    }

    Fraction multiply(Fraction other) {

        return new Fraction(
        num.multiply(other.num),
        den.multiply(other.den)
        );
    }
}

public class ShamirSecret {

    static BigInteger solve(String file) throws Exception {

        Gson gson = new Gson();

        JsonObject obj =
        gson.fromJson(
        new FileReader(file),
        JsonObject.class
        );

        int k =
        obj.getAsJsonObject("keys")
        .get("k").getAsInt();

        List<BigInteger> x = new ArrayList<>();
        List<BigInteger> y = new ArrayList<>();

        for(String key : obj.keySet()) {

            if(key.equals("keys")) continue;

            JsonObject share =
            obj.getAsJsonObject(key);

            int base =
            Integer.parseInt(
            share.get("base").getAsString());

            BigInteger value =
            new BigInteger(
            share.get("value").getAsString(),
            base);

            x.add(new BigInteger(key));
            y.add(value);
        }

        Fraction secret =
        new Fraction(BigInteger.ZERO,BigInteger.ONE);

        for(int i=0;i<k;i++) {

            Fraction term =
            new Fraction(y.get(i),BigInteger.ONE);

            for(int j=0;j<k;j++) {

                if(i==j) continue;

                Fraction frac =
                new Fraction(
                x.get(j).negate(),
                x.get(i).subtract(x.get(j))
                );

                term =
                term.multiply(frac);
            }

            secret =
            secret.add(term);
        }

        return secret.num;
    }

    public static void main(String[] args) throws Exception {

        System.out.println(
        "Secret 1 = " + solve("input1.json"));

        System.out.println(
        "Secret 2 = " + solve("input2.json"));
    }
}
