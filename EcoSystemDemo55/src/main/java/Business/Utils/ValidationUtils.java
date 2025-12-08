package Business.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Utility class for form validation and password hashing
 */
public class ValidationUtils {

    // Email validation regex
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Name validation regex (alphabetic characters and spaces only)
    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[A-Za-z\\s]{2,50}$");

    // Phone validation regex (US format)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\(?([0-9]{3})\\)?[-.\\s]?([0-9]{3})[-.\\s]?([0-9]{4})$");

    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates name (alphabetic characters and spaces, 2-50 chars)
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    /**
     * Validates age (must be between min and max)
     */
    public static boolean isValidAge(int age, int min, int max) {
        return age >= min && age <= max;
    }

    /**
     * Validates age string and parses it
     */
    public static boolean isValidAge(String ageStr, int min, int max) {
        try {
            int age = Integer.parseInt(ageStr.trim());
            return isValidAge(age, min, max);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates phone number
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validates budget/price (must be positive)
     */
    public static boolean isValidBudget(double budget) {
        return budget > 0;
    }

    /**
     * Validates budget string and parses it
     */
    public static boolean isValidBudget(String budgetStr) {
        try {
            double budget = Double.parseDouble(budgetStr.trim());
            return isValidBudget(budget);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validates password strength
     * Requirements: min 8 chars, at least 1 uppercase, 1 lowercase, 1 digit
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))
                hasUpper = true;
            if (Character.isLowerCase(c))
                hasLower = true;
            if (Character.isDigit(c))
                hasDigit = true;
        }

        return hasUpper && hasLower && hasDigit;
    }

    /**
     * Returns password strength validation message
     */
    public static String getPasswordRequirements() {
        return "Password must be at least 8 characters long and contain:\n" +
                "- At least one uppercase letter\n" +
                "- At least one lowercase letter\n" +
                "- At least one digit";
    }

    /**
     * Hashes a password using SHA-256
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Validates that a string is not null or empty
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Validates string length
     */
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (str == null)
            return false;
        int length = str.trim().length();
        return length >= minLength && length <= maxLength;
    }
}
