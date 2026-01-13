import java.io.*;
import java.net.Socket;
import java.io.IOException;

class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    public void run(){
        try(PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))){

            String uuid = checkUUID(reader, writer);
            System.out.println(uuid);

            String client = ""; // Пишет клиент
            String server = ""; // Пишет сервер

            while(true){
                client = read(reader);
                System.out.println(client);
                server = Metods.parseMes(client, uuid);
                System.out.println(server);
                write(writer, server);
                if(client.contains("/exit")) break;
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            }catch (IOException ex){
                ex.printStackTrace();
            }

        }
    }
    public static String read(BufferedReader reader) throws IOException{
        StringBuilder result = new StringBuilder();
        String line;
        line = reader.readLine();
        result.append(line);
        return result.toString();
    }
    public static String write(PrintWriter writer, String line) throws IOException{
        writer.println(line);
        return line;
    }
    public static String checkUUID(BufferedReader reader, PrintWriter writer) throws IOException{
        StringBuilder str = new StringBuilder();
        String line = read(reader);
        String result = Metods.createUUID(line);
        str.append(result).append("\n***");
        write(writer, str.toString());
        return result;
    }

}
