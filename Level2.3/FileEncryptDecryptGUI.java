import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FileEncryptDecryptGUI extends JFrame {
    private JButton selectFileButton;
    private JTextField filePathField;
    private JRadioButton encryptRadio;
    private JRadioButton decryptRadio;
    private JButton runButton;
    private JTextArea statusArea;

    private File selectedFile;
    private final int SHIFT = 3; // Caesar Cipher shift

    public FileEncryptDecryptGUI() {
        setTitle("File Encryption / Decryption");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for file selection
        JPanel filePanel = new JPanel(new FlowLayout());
        selectFileButton = new JButton("Select File");
        filePathField = new JTextField(25);
        filePathField.setEditable(false);
        filePanel.add(selectFileButton);
        filePanel.add(filePathField);

        // Middle panel for options
        JPanel optionsPanel = new JPanel(new FlowLayout());
        encryptRadio = new JRadioButton("Encrypt", true);
        decryptRadio = new JRadioButton("Decrypt");
        ButtonGroup group = new ButtonGroup();
        group.add(encryptRadio);
        group.add(decryptRadio);
        optionsPanel.add(encryptRadio);
        optionsPanel.add(decryptRadio);

        // Run button
        runButton = new JButton("Run");
        optionsPanel.add(runButton);

        // Status area
        statusArea = new JTextArea(5, 40);
        statusArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statusArea);

        add(filePanel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Button actions
        selectFileButton.addActionListener(e -> selectFile());
        runButton.addActionListener(e -> processFile());
    }

    private void selectFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
            statusArea.setText("Selected file: " + selectedFile.getName());
        }
    }

    private void processFile() {
        if (selectedFile == null) {
            statusArea.setText("Please select a file first.");
            return;
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            statusArea.setText("Error reading file: " + e.getMessage());
            return;
        }

        String resultText;
        if (encryptRadio.isSelected()) {
            resultText = encrypt(content.toString());
        } else {
            resultText = decrypt(content.toString());
        }

        String outputPath = generateOutputFileName(selectedFile.getPath(), encryptRadio.isSelected());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(resultText);
            statusArea.setText("Operation completed.\nOutput saved to:\n" + outputPath);
        } catch (IOException e) {
            statusArea.setText("Error writing output file: " + e.getMessage());
        }
    }

    private String encrypt(String text) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                result.append((char) ((ch - base + SHIFT) % 26 + base));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    private String decrypt(String text) {
        StringBuilder result = new StringBuilder();
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                result.append((char) ((ch - base - SHIFT + 26) % 26 + base));
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    private String generateOutputFileName(String originalPath, boolean isEncrypt) {
        File file = new File(originalPath);
        String name = file.getName();
        String parent = file.getParent() + File.separator;
        String suffix = isEncrypt ? "_encrypted.txt" : "_decrypted.txt";
        if (name.endsWith(".txt")) {
            name = name.substring(0, name.length() - 4);
        }
        return parent + name + suffix;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FileEncryptDecryptGUI().setVisible(true);
        });
    }
}
