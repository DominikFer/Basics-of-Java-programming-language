package opjj.hw2;

import java.math.BigDecimal;

public final class MoneyToTextConverter {

    private static final String[] FROM_0_TO_20 = { "nula", "jedna", "dvije",
            "tri", "četiri", "pet", "šest", "sedam", "osam", "devet", "deset",
            "jedanaest", "dvanaest", "trinaest", "četrnaest", "petnaest",
            "šesnaest", "sedamnaest", "osamnaest", "devetnaest" };
    
    private static final String[] FROM_0_TO_20_MILLIONS = { "nula", "jedan", "dva",
        "tri", "četiri", "pet", "šest", "sedam", "osam", "devet", "deset",
        "jedanaest", "dvanaest", "trinaest", "četrnaest", "petnaest",
        "šesnaest", "sedamnaest", "osamnaest", "devetnaest" };
    
    private static final String[] TENTHS = { "dvadeset", "trideset",
            "četrdeset", "pedeset", "šezdeset", "sedamdeset", "osamdeset",
            "devedeset" };

    public String convert(BigDecimal money) {
        if (money == null) {
            throw new IllegalArgumentException("No conversion for null");
        }

        int baseMoney = money.abs().intValue();
        String result = fromInteger(baseMoney, false);
        
        String resultArray[] = result.split(" ");
        String last = resultArray[resultArray.length-1];
        if(last.equals("dvije") || last.equals("tri") || last.equals("četiri"))
        	return result + " kune";
        
        return result + " kuna";
    }

    private String fromInteger(int n, boolean suffixE) {
        if (n < 20) {
        	return FROM_0_TO_20[n];
        } else if (n < 100) {
            int div = n / 10;
            int mod = n % 10;
            int arrayStartsAt20Offset = 2;
            return TENTHS[div - arrayStartsAt20Offset] + continuationFor(mod);
        } else if (n < 1000) {
            return scale(n, 100, suffixE ? "stotine" : "stotina");
        } else if (n < 1000000) {
            return scale(n, 1000, suffixE ? "tisuće" : "tisuća");
        } else {
            return scale(n, 1000000, "milijuna");
        }
    }

    private String continuationFor(int n) {
        return n == 0 ? "" : " " + fromInteger(n, false);
    }
    
    private String scale(int n, int s, String text) {
        int div = n / s;
        int mod = n % s;
        
        return fromInteger(div, false) + " " + text + continuationFor(mod);
    }

}
