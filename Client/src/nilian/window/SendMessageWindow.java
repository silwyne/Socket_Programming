package nilian.window;

import nilian.client.Client;

import javax.swing.*;

public class SendMessageWindow {


    public static void show() {


        //Create the JFrame for username input
        JFrame sending = new JFrame("sending");
        sending.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a JPanel to hold components
        JPanel panel = new JPanel();

        // Create a JTextField for username input
        JTextField textField = new JTextField(20);
        // Set the last username as the initial text value
        textField.setText("write something!");
        panel.add(textField);

        // Create a button to retrieve the username
        JButton button = new JButton("send");
        button.addActionListener(e -> {

            String message = textField.getText();
            Client.sendMessage(message) ;
            sending.dispose();// Close the username input window
            // Enable the "Run" button in the initial window
        });
        panel.add(button);
        // Disable the "Run" button in the initial window
        // Add the panel to the usernameFrame
        sending.setLocationRelativeTo(null);
        sending.getContentPane().add(panel);
        sending.pack();
        sending.setVisible(true);
    }
}
