import java.io.*;
import java.util.*;
import java.util.Base64;

public class PasswordManager {
    private static final String FILE_NAME = "passwords.txt";
    private static final String MASTER_PASSWORD = "admin123"; // Change this to your master password

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Master password check
        System.out.print("Enter master password: ");
        String inputPassword = sc.nextLine();
        if (!inputPassword.equals(MASTER_PASSWORD)) {
            System.out.println("Incorrect master password. Exiting...");
            return;
        }

        // Menu loop
        while (true) {
            System.out.println("\n1. Add Password");
            System.out.println("2. View Passwords");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addPassword(sc);
                    break;
                case 2:
                    viewPasswords();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    private static void addPassword(Scanner sc) {
        try (FileWriter fw = new FileWriter(FILE_NAME, true)) {
            System.out.print("Enter website: ");
            String website = sc.nextLine();
            System.out.print("Enter username: ");
            String username = sc.nextLine();
            System.out.print("Enter password: ");
            String password = sc.nextLine();

            // Encrypt password using Base64 encoding
            String encrypted = Base64.getEncoder().encodeToString(password.getBytes());

            fw.write(website + "," + username + "," + encrypted + "\n");
            System.out.println("Password saved successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private static void viewPasswords() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            System.out.println("\nSaved Passwords:");
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String website = parts[0];
                    String username = parts[1];
                    String encrypted = parts[2];
                    String decrypted;
                    try {
                        // Attempt Base64 decoding; if it fails, treat as plain text
                        decrypted = new String(Base64.getDecoder().decode(encrypted));
                    } catch (IllegalArgumentException ex) {
                        decrypted = encrypted; // fallback for non-Base64 entries
                    }
                    System.out.println("Website: " + website + ", Username: " + username + ", Password: " + decrypted);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No passwords saved yet.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
