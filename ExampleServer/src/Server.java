import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Server {

    public static final HashMap<String, ShortUrl> urlDatebase = new HashMap<>();
    public static final HashMap<String, ArrayList> userUrl = new HashMap<>();
    public static final String BASE_SHORT_URL = "clck.ru";
    public static final int DEFAULT_CLICK_LIMIT = 10;
    public static final int DEFAULT_HOURS_VALID = 24;

    public static void main(String[] agrs) throws IOException {
        ServerSocket server = new ServerSocket(8080);
        System.out.println("Сервер запущен!");

        ScheduledExecutorService schelduler = Executors.newSingleThreadScheduledExecutor();
        schelduler.scheduleAtFixedRate(Metods::checkTimeLive, 0, 30, TimeUnit.MINUTES);

        while (true) {
            Socket clientSoket = server.accept();
            System.out.println("Подключился клиент с IP: " + clientSoket.getInetAddress());
            new Thread(new ClientHandler(clientSoket)).start();
        }
    }
}
