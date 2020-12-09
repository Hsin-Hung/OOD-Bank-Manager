import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class Constants {
    public static final BigDecimal savingsInterestPercentage = new BigDecimal("0.05");
    public static final BigDecimal loanInterestPercentage = new BigDecimal("0.10");
    public static final BigDecimal openAccountFee = new BigDecimal("5");
    public static final BigDecimal closeAccountFee = new BigDecimal("5");
    public static final BigDecimal withdrawFee = new BigDecimal("5");
    public static final BigDecimal vipThreshold = new BigDecimal("5000");

    public static final boolean DEV_MODE = false;
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final BigDecimal minAccountOpeningBalance = new BigDecimal("20");
}