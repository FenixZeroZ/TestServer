import javafx.beans.binding.When;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class TestServer extends JFrame {

    private static JScrollPane jScrollPane;
    private static JEditorPane serverLog;
    private static JEditorPane serverTextFrame;
    private static JTextField serverWrite;


    private static BufferedReader is = null;
    private static PrintWriter os = null;

    private static String logServer = "";
    private static String textServer = "";
    private static String text="";

    private static ServerSocket server = null;
    private static Socket client = null;

    private void serverStart() {

        serverLog = new JEditorPane();
        serverTextFrame = new JEditorPane();
        serverTextFrame.setEditable(false);
        serverWrite = new JTextField();
        JLabel labelServerLog = new JLabel("Сервер логи");
        JLabel labelServerTextFrame = new JLabel("Окно общения");
        JLabel labelServerWrite = new JLabel("Окно ввода текста");
        jScrollPane = new JScrollPane(serverTextFrame);

        this.setTitle("Сервер ver.1");
        this.setSize(500, 500);
        this.setLocation(500, 400);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(3, 2));
        this.add(serverLog);
        this.add(labelServerLog);

        this.add(jScrollPane);
        jScrollPane.getVerticalScrollBar().setValue(100);
        this.add(labelServerTextFrame);
        this.add(serverWrite);
        this.add(labelServerWrite);
        this.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        TestServer testServer = new TestServer();
        testServer.serverStart();

        try {
            server = new ServerSocket(7777);

            logServer += "Сервер запущен на порту 7777";
            serverLog.setText(logServer);
        } catch (IOException e) {
            e.printStackTrace();
            logServer += "\nОшибка открытия порта 7777";
            serverLog.setText(logServer);
        }

        try {
            client = server.accept();
            logServer += ("\nПодключился клиент");
            serverLog.setText(logServer);

        } catch (IOException e) {
            e.printStackTrace();
            logServer += ("\nОшибка подключения клиента");
            serverLog.setText(logServer);
        }

        try {
            is = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {

            logServer += ("\nОшибка чтения сообщения от клиента");
            serverLog.setText(logServer);
        }

        try {
            os = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            logServer += ("\nОшибка записи сообщения к клиенту");
            serverLog.setText(logServer);
            e.printStackTrace();
        }

        serverWrite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text = serverWrite.getText();
                textServer += ("\n" + "Сервер:" + text);
                serverWrite.setText("");
                serverTextFrame.setText(textServer);
                os.println(text);

            }
        });
        try {
            while (!text.equals("exit")) {

                text = is.readLine();
                textServer += ("\n" + "Client: " + text);
                serverTextFrame.setText(textServer);
            }

            server.close();
            client.close();
            is.close();
            os.close();
        } catch (Exception e) {
            String s = e.toString();
            logServer += ("\n" + s);
            serverLog.setText(logServer);
        }

    }

}
