import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PasswordStrengthChecker extends JFrame {

    private JPasswordField passwordField;
    private JButton checkButton;
    private JLabel resultLabel;
    private JLabel enteredPasswordLabel;

    public PasswordStrengthChecker() {
        setTitle("Password Strength Checker");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.add(new JLabel("Enter Password:"));
        passwordField = new JPasswordField(20);
        inputPanel.add(passwordField);
        add(inputPanel, BorderLayout.NORTH);

        // Check Button
        checkButton = new JButton("Check Strength");
        add(checkButton, BorderLayout.CENTER);

        // Result Panel
        JPanel resultPanel = new JPanel(new GridLayout(2, 1));
        enteredPasswordLabel = new JLabel("Entered Password: ");
        enteredPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        enteredPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        resultLabel = new JLabel(" ");
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));

        resultPanel.add(enteredPasswordLabel);
        resultPanel.add(resultLabel);
        add(resultPanel, BorderLayout.SOUTH);

        // Button Action
        checkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                String strength = checkPasswordStrength(password);
                enteredPasswordLabel.setText("Entered Password: " + password);
                resultLabel.setText(strength);
            }
        });

        setVisible(true);
    }

    private String checkPasswordStrength(String password) {
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        String specialChars = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~";

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isDigit(ch)) hasDigit = true;
            else if (specialChars.contains(String.valueOf(ch))) hasSpecial = true;
        }

        int score = 0;
        if (password.length() >= 8) score++;
        if (hasUpper) score++;
        if (hasLower) score++;
        if (hasDigit) score++;
        if (hasSpecial) score++;

        if (score <= 2) return "Weak Password";
        else if (score == 3 || score == 4) return "Moderate Password";
        else return "Strong Password ";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PasswordStrengthChecker());
    }
}
