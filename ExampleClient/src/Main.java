import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {
    public static void main(String[] agrs) throws IOException, URISyntaxException, InterruptedException {

        Socket socket = new Socket("localhost", 8080);
        System.out.println("Поделючелся к серверу!");

        String client = ""; // Пишет клиент
        String server = ""; // Пишет сервер

        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        Interface Ui = new Interface(reader, writer);
        Ui.checkUUID();
        while (!client.contains("/exit")) {
            Scanner sc = new Scanner(System.in);
            System.out.print("/");
            client = "/" + sc.nextLine();
            client += Ui.print(client.trim());
        }
        socket.close();
    }
}
