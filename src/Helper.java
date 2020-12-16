import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Helper class that holds some static helper functions for the whole project to use
 */
public class Helper {

    /**
     * Function to get last item in a list.
     *
     * @param list
     * @param <T>
     * @return
     */

    public static <T> T getLastItem(List<T> list) {
        return list.get(list.size() - 1);
    }

    /**
     * Function if bigdecimal has more than 2dp.
     */
    public static boolean isCorrectMoney(BigDecimal bigDecimal) {
        String string = bigDecimal.stripTrailingZeros().toPlainString();
        int index = string.indexOf(".");
        if (index <= 0) {
            return true;
        } else if ((string.length() - index - 1) > 2) {
            System.out.println("Len = " + (string.length() - index - 1));
            return false;
        } else {
            return true;
        }
    }

    /**
     * Function to check if spinner has valid money amount
     *
     * @param spinner
     * @return BigDecimal value of spinner
     */
    public static BigDecimal checkSpinnnerMoneyValue(JSpinner spinner) {
        String amount = spinner.getValue().toString();
        boolean isCorrectAmount = Helper.isCorrectMoney(new BigDecimal(amount));

        if (isCorrectAmount) {
            BigDecimal dec = new BigDecimal(amount);
            if (dec.compareTo(new BigDecimal(0)) < 0) {
                return null;
            } else {
                return dec;
            }
        } else {
            return null;
        }
    }

    /**
     * Force a spinner to only accept valid inputs
     * @param spinner The spinner to set the property on
     */
    public static void disableSpinnerInvalids(JSpinner spinner) {
        JFormattedTextField txt = (((JSpinner.DefaultEditor) spinner.getEditor()).getTextField());
        NumberFormatter formatter = (NumberFormatter) txt.getFormatter();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        formatter.setFormat(decimalFormat);
        formatter.setAllowsInvalid(false);
    }

    /**
     * Force a spinner to only accept valid inputs integer
     * @param spinner The spinner to set the property on
     */
    public static void disableSpinnerInvalidsInteger(JSpinner spinner) {
        JFormattedTextField txt = (((JSpinner.DefaultEditor) spinner.getEditor()).getTextField());
        NumberFormatter formatter = (NumberFormatter) txt.getFormatter();
        formatter.setAllowsInvalid(false);
    }
}
