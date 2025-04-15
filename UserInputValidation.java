import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class User {
    String username;
    String passwordHash;
    String firstName;
    String phoneNumber;

    public User(String username, String passwordHash, String firstName, String phoneNumber) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
    }
}

class UserService {
    private User registeredUser;

    public String registerUser(Scanner scanner) {
        System.out.print("Please Enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Please Enter your username (must contain an underscore and be no more than five characters): ");
        String username = scanner.nextLine();

        if (!checkUserName(username)) {
            return "Username is not correctly formatted. Please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        System.out.print("Please Enter your password: ");
        String password = scanner.nextLine();

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        System.out.print("Please Enter your South African cell phone number (must start with '+27' and be 13 characters long): ");
        String phoneNumber = scanner.nextLine();

        if (!checkCellPhoneNumber(phoneNumber)) {
            return "Invalid phone number. It must start with '+27' and be exactly 13 characters long.";
        }

        // Hash the password for security
        String passwordHash = hashPassword(password);
         registeredUser = new User(username, passwordHash, firstName, phoneNumber);
        return "Username successfully captured. Password successfully captured. You can now log in.";
    }

    public User getRegisteredUser() {
        return registeredUser;
    }

    private boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    private boolean checkCellPhoneNumber(String phoneNumber) {
        return phoneNumber.startsWith("+27") && phoneNumber.length() == 13;
    }

    private boolean checkPasswordComplexity(String password) {
        return password.length() >= 8 
               && password.matches(".[A-Z].") // At least one uppercase letter
               && password.matches(".\\d.")   // At least one number
               && password.matches(".[^a-zA-Z0-9]."); // At least one special character
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}

class Login {
    private UserService userService;

    public Login(UserService userService) {
        this.userService = userService;
    }

    public void authenticate(Scanner scanner) {
        int attempts = 3; // Allow up to 3 login attempts
        while (attempts > 0) {
            System.out.print("Please log in with your credentials.\nEnter your username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter your password: ");
            String loginPassword = scanner.nextLine();

            String loginMessage = returnLoginStatus(loginUsername, loginPassword);
            System.out.println(loginMessage);

            if (loginMessage.startsWith("Welcome")) {
                break; // Exit the loop on successful login
            } else {
                attempts--;
                if (attempts > 0) {
                    System.out.println("You have " + attempts + " attempt(s) left.");
                } else {
                    System.out.println("No attempts left. Exiting login.");
                }
            }
        }
    }

    private String returnLoginStatus(String username, String password) {
        User registeredUser = userService.getRegisteredUser();
        if (registeredUser != null && username.equals(registeredUser.username) 
                && registeredUser.passwordHash.equals(userService.hashPassword(password))) {
            return "Welcome " + registeredUser.firstName + ", it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}

public class UserInputValidation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();

        // Registration process
        String registrationMessage = userService.registerUser(scanner);
        System.out.println(registrationMessage);
        
        // Check if registration was successful before proceeding to login
        if (registrationMessage.contains("successfully captured")) {
            Login login = new Login(userService);
            // Call the login method
            login.authenticate(scanner);
        }

        scanner.close();
    }
}
