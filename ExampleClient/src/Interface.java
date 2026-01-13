import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

class Interface {
    private static final int DEFAULT_CLICK_LIMIT = 10;
    private static final String BASE_SHORT_URL = "clck.ru";
    BufferedReader reader;
    PrintWriter writer;
    private String originalURL;

    public Interface(BufferedReader reader, PrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static String dispatchMes(BufferedReader reader, PrintWriter writer, String line) throws IOException {
        StringBuilder result = new StringBuilder();
        writer.println(line);
        String str = "";
        while ((str = reader.readLine()) != null) {
            if (str.equals("***")) break;
            result.append(str).append("\n");
        }
        return result.toString();
    }

    private static String comHelp() {
        String result = "create - Создать короткую ссылку.\n" +
                "delete - Удалить короткую ссылку.\n" +
                "info - Получить информацию о ссылки.\n" +
                "mylinks - Просмотреть мои короткие ссылки.\n" +
                "open - Открыть короткую ссылку.\n" +
                "exit - Выйти из сервиса.\n" +
                "help - Просмотреть вкоманды.";
        return result;
    }

    private static String comOpen() {
        StringBuilder result = new StringBuilder();
        Scanner sc = new Scanner(System.in);
        result.append("/open").append("#");
        System.out.print("Введите ссылку:");
        String terminal = sc.nextLine();
        if (!terminal.contains(BASE_SHORT_URL)) {
            result.setLength(0);
            result.append("Неверная ссылка");
        } else {
            result.append(terminal);
        }
        System.out.println("Ответ сервера: ");
        return result.toString();
    }

    private static String comCreate() {
        StringBuilder result = new StringBuilder();
        Scanner sc = new Scanner(System.in);
        result.append("/create").append("#");
        System.out.print("Введите ссылку: ");
        String terminal = sc.nextLine();
        if ((!terminal.contains("http://") && (!terminal.contains("https://")))) {
            return "Неверная ссылка";
        }
        result.append(terminal).append("#");
        System.out.print("Введите кол-во использований: ");
        terminal = sc.nextLine();
        try {
            Integer.parseInt(terminal);
        } catch (NumberFormatException e) {
            terminal = String.valueOf(DEFAULT_CLICK_LIMIT);
        }
        result.append(terminal.trim());
        System.out.println("Ответ сервера: ");
        return result.toString();
    }

    private static String comDelete() {
        StringBuilder result = new StringBuilder();
        Scanner sc = new Scanner(System.in);
        result.append("/delete").append("#");
        System.out.print("Введите короткую ссылку: ");
        String terminal = sc.nextLine();
        if (!terminal.contains(BASE_SHORT_URL)) {
            return "Невернj указана ссылка";
        } else {
            result.append(terminal);
        }
        System.out.println("Ответ сервера: ");
        return result.toString();
    }

    private static String comInfo() {
        StringBuilder result = new StringBuilder();
        Scanner sc = new Scanner(System.in);
        result.append("/info").append("#");
        System.out.print("Введите короткую ссылку: ");
        String terminal = sc.nextLine();
        if (!terminal.contains(BASE_SHORT_URL)) {
            result.setLength(0);
            result.append("Неверная ссылка");
        } else {
            result.append(terminal);
        }
        System.out.println("Ответ сервера: ");
        return result.toString();
    }

    private static String comMylinks() {
        String result = "/mylinks" + "#";
        System.out.println("Ответ сервера: ");
        return result;
    }

    private static String comExit() {
        String result = "/exit" + "#";
        System.out.println("Ответ сервера: ");
        return result;
    }

    private static String comLog() {
        String result = "/log" + "#";
        System.out.println("Ответ сервера: ");
        return result;
    }

    public String print(String line) throws IOException, URISyntaxException {
        String client = "";
        String server = "";
        switch (line) {
            case "/help":
                client = comHelp();
                System.out.println(client);
                break;
            case "/create":
                client = comCreate();
                server = dispatchMes(reader, writer, client);
                System.out.println(server);
                break;
            case "/delete":
                client = comDelete();
                server = dispatchMes(reader, writer, client);
                System.out.println(server);
                break;
            case "/info":
                client = comInfo();
                server = dispatchMes(reader, writer, client);
                System.out.println(server);
                break;
            case "/mylinks":
                client = comMylinks();
                server = dispatchMes(reader, writer, client);
                System.out.println(server);
                break;
            case "/open":
                client = comOpen();
                server = dispatchMes(reader, writer, client);
                System.out.println(server);
                Desktop.getDesktop().browse(new URI(server.trim()));
                break;
            case "/exit":
                client = comExit();
                server = dispatchMes(reader, writer, client);
                System.out.println(server);
                break;
            default:
                client = "Неверная команда";
                break;
        }
        return client;
    }

    public void checkUUID() throws IOException {
        File file = new File("C:/Users/bdani/Desktop/UUID.txt/");
        try {
            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Файл создан");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader readFile = new BufferedReader(new FileReader(file));
        String userUUID = "";
        String serverUUID = "";
        StringBuilder result = new StringBuilder();
        while ((userUUID = readFile.readLine()) != null) {
            result.append(userUUID);
        }
        readFile.close();
        userUUID = result.toString();
        serverUUID = dispatchMes(reader, writer, userUUID);
        if (!serverUUID.equals(userUUID)) {
            BufferedWriter writeFile = new BufferedWriter(new FileWriter(file));
            writeFile.write(serverUUID);
            writeFile.close();
        }
        System.out.print("Ваш UUID:" + serverUUID);
    }


}
