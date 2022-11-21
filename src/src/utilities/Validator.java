package utilities;

public final class Validator {

    /**
     * This method check is given number is positive.
     * IMPORTANT: In method zero is considerate as positive number.
     */
    public static boolean isPositive(int number) {
        boolean isPositive = number >= 0;

        return isPositive;
    }

    public static boolean isGreaterThan(int number, int comparableNumber) {
        boolean isGreaterThan = number > comparableNumber;

        return isGreaterThan;
    }
}
