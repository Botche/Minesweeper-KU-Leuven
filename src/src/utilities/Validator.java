package utilities;

public final class Validator {

    /**
     * This method check is given number is positive.
     * IMPORTANT: In method zero is considerate as positive number.
     */
    public static boolean IsPositive(int number) {
        boolean isPositive = true;

        if (number > 0) {
            isPositive = false;
        }

        return isPositive;
    }
}
