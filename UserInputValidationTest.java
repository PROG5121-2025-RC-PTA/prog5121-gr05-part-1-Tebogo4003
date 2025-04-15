import java.util.Scanner;

public class UserInputValidation {
    private static String registeredUsername;
    private static String registeredPassword;
    private static String registeredFirstName;

    public static boolean CheckUserName(String username) {
        // Ensure the username contains an underscore and is no longer than five characters
        if (username.contains("_") && username.length() <= 5) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Check if the phone number is a South African cell phone number starting with +27
        return phoneNumber.matches("^\\+27\\d{9}$"); // Should start with '+27' and have 11 digits total
    }

    public static boolean isValidPassword(String password) {
        // Check for password length, capital letter, number, and special character
        return password.length() >= 8 
               && password.matches(".[A-Z].") // At least one uppercase letter
               && password.matches(".\\d.")   // At least one number
               && password.matches(".[^a-zA-Z0-9]."); // At least one special character
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Registration process
        System.out.print("Please Enter your first name: ");
        registeredFirstName = scanner.nextLine();
        
        System.out.print("Please Enter your username (must contain an underscore and be no longer than five characters): ");
        String username = scanner.nextLine();

        if (CheckUserName(username)) {
            System.out.print("Please Enter your password: ");
            String password = scanner.nextLine();

            if (isValidPassword(password)) {
                System.out.print("Please Enter your South African cell phone number (must start with '+27' and be 11 digits long): ");
                String phoneNumber = scanner.nextLine();

                if (isValidPhoneNumber(phoneNumber)) {
                    registeredUsername = username; // Store the registered username
                    registeredPassword = password; // Store the registered password
                    System.out.println("Successfully registered! You can now log in.");
                } else {
                    System.out.println("Invalid phone number. Please ensure it is an 11-digit South African cell phone number starting with '+27'.");
                    scanner.close();
                    return;
                }
            } else {
                System.out.println("Password is not correctly formatted. Please ensure that it contains at least eight characters, one capital letter, one number, and one special character.");
                scanner.close();
                return;
            }
        } else {
            System.out.println("Username is not correctly formatted. It must contain an underscore and be no longer than five characters.");
            scanner.close();
            return;
        }

        // Login process
        System.out.print("Please log in with your credentials.\nEnter your username: ");
        String loginUsername = scanner.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = scanner.nextLine();

        if (loginUsername.equals(registeredUsername) && loginPassword.equals(registeredPassword)) {
            System.out.println("Welcome " + registeredFirstName + "!");
        } else {
            System.out.println("Username or password incorrect. Please try again.");
        }

        scanner.close();
    }
}