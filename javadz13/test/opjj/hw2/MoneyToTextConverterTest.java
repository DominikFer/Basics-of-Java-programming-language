package opjj.hw2;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

public class MoneyToTextConverterTest {

    private MoneyToTextConverter c = new MoneyToTextConverter();

    @Test(expected = IllegalArgumentException.class)
    public void convertShouldThrowAnExceptionOnNullArgument() {
        c.convert(null);
    }

    @Test
    public void textualRepresentationOfAPositiveSum() {
        assertConversion(0,  "nula kuna");
        assertConversion(1,  "jedna kuna");
        assertConversion(2,  "dvije kune");
        assertConversion(3,  "tri kune");
        assertConversion(4,  "četiri kune");
        assertConversion(5,  "pet kuna");
        assertConversion(6,  "šest kuna");
        assertConversion(7,  "sedam kuna");
        assertConversion(8,  "osam kuna");
        assertConversion(9,  "devet kuna");
        assertConversion(10, "deset kuna");
        assertConversion(11, "jedanaest kuna");
        assertConversion(12, "dvanaest kuna");
        assertConversion(13, "trinaest kuna");
        assertConversion(14, "četrnaest kuna");
        assertConversion(15, "petnaest kuna");
        assertConversion(16, "šesnaest kuna");
        assertConversion(17, "sedamnaest kuna");
        assertConversion(18, "osamnaest kuna");
        assertConversion(19, "devetnaest kuna");
        assertConversion(20, "dvadeset kuna");
        assertConversion(21, "dvadeset jedna kuna");
        assertConversion(22, "dvadeset dvije kune");
        assertConversion(29, "dvadeset devet kuna");
        assertConversion(30, "trideset kuna");
        assertConversion(40, "četrdeset kuna");
        assertConversion(50, "pedeset kuna");
        assertConversion(60, "šezdeset kuna");
        assertConversion(70, "sedamdeset kuna");
        assertConversion(80, "osamdeset kuna");
        assertConversion(90, "devedeset kuna");
        assertConversion(95, "devedeset pet kuna");
        assertConversion(100,    "jedna stotina kuna");
        assertConversion(111,    "jedna stotina jedanaest kuna");
        assertConversion(157,    "jedna stotina pedeset sedam kuna");
        assertConversion(200,    "dvije stotine kuna");
        assertConversion(300,    "tri stotine kuna");
        assertConversion(567,    "pet stotina šezdeset sedam kuna");
        assertConversion(729,    "sedam stotina dvadeset devet kuna");
        assertConversion(1000,   "jedna tisuća kuna");
        assertConversion(1024,   "jedna tisuća dvadeset četiri kune");
        assertConversion(3000,   "tri tisuće kuna");
        assertConversion(5000,   "pet tisuća kuna");
        assertConversion(14000,  "četrnaest tisuća kuna");
        assertConversion(50001,  "pedeset tisuća jedna kuna");
        assertConversion(148345, "jedna stotina četrdeset osam tisuća tri stotina četrdeset pet kuna");
        assertConversion(190853, "jedna stotina devedeset tisuća osam stotina pedeset tri kune");
        assertConversion(234692, "dvije stotine trideset četiri tisuće šest stotina devedeset dvije kune");
        assertConversion(571460, "pet stotina sedamdeset jedna tisuća četiri stotina šezdeset kuna");
        assertConversion(991992999.03, "devet stotina devedeset jedan milijun devet stotina devedeset dvije tisuće devet stotina devedeset devet kuna i tri lipe");
    }

    @Test
    public void textualRepresentationOfANegativeSum() {
        assertConversion(7,    "sedam kuna");
        assertConversion(-15,  "petnaest kuna");
        assertConversion(-239, "dvije stotina trideset devet kuna");
    }

    private void assertConversion(double n, String text) {
        assertEquals(text, c.convert(new BigDecimal(n)));
    }

}
