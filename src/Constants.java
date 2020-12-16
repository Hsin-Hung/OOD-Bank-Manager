import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Formatter;

/**
 * Constants.java - class that keeps track of all the hard coded constants needed for bank.
 */
public class Constants {
    public static final BigDecimal savingsInterestPercentage = new BigDecimal("0.05");
    public static final BigDecimal loanInterestPercentage = new BigDecimal("0.10");
    public static final BigDecimal openAccountFee = new BigDecimal("5");
    public static final BigDecimal closeAccountFee = new BigDecimal("5");
    public static final BigDecimal withdrawFee = new BigDecimal("5");
    public static final BigDecimal vipThreshold = new BigDecimal("5000");

    public static final boolean DEV_MODE = false;
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final CurrencyFormatter CURRENCY_FORMAT = new CurrencyFormatter();

    public static final BigDecimal minimalSecurityAmount = new BigDecimal(2500);
    public static final double minimalStartingSecurityAmount = 1000;

    public static final BigDecimal minAccountOpeningBalance = new BigDecimal("20");
    public static final BigDecimal checkingFee = new BigDecimal("5");

    static class CurrencyFormatter {
        String format(double number) {
            StringBuilder sb = new StringBuilder();
            Formatter formatter = new Formatter(sb);
            formatter.format("%(,.2f", number);
            return sb.toString();
        }
    }
}