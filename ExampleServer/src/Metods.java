import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Metods {

    public static String createShortUrl(String userUUID, String originalUrl, int clickLimit) { // Создает короткую ссылку
        String id = generateShortId();
        String createUrl = Server.BASE_SHORT_URL.concat(id);
        ShortUrl shortUrl = new ShortUrl(createUrl, originalUrl, userUUID, clickLimit);
        Server.urlDatebase.put(id, shortUrl);
        ArrayList list = Server.userUrl.get(userUUID);
        list.add(createUrl);
        return createUrl;
    }

    public static String deleteShortUrl(String userUUID, String createUrl) {// Удаляет короткую ссылку
        if (!validUrl(createUrl)) return "Ссылка введена неправильно";
        String id = createUrl.substring(Server.BASE_SHORT_URL.length());
        if (Server.urlDatebase.get(id).setuuidUsers() != userUUID) {
            return "У вас нет прав к этой ссылки";
        }
        ArrayList list = Server.userUrl.get(userUUID);
        list.remove(createUrl);
        Server.urlDatebase.remove(id);
        return "Ссылка успешно удалена";
    }

    public static String infoAllLinkUser(String userUUID) { //  Метод показывает все действующие ссылки пользователя
        List<String> list = Server.userUrl.get(userUUID);
        StringBuilder result = new StringBuilder();
        if (list.size() == 0) {
            result.append("У вас нету ссылок");
        }else {
            for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i)).append("\n");
            }
        }
        return result.toString();
    }

    public static String infoOfLink(String createUrl) { // Показывавет информацию о данной ссылки
        if (!validUrl(createUrl)) return "Ссылка введена неправильно";
        StringBuilder result = new StringBuilder();
        String id = createUrl.substring(Server.BASE_SHORT_URL.length());
        if (!Server.urlDatebase.containsKey(id)) return result.append("Такой ссылки не существует").toString();
        ShortUrl shortUrl = Server.urlDatebase.get(id);
        result.append("Оригинальная ссылка: ").append(shortUrl.setOriginalURL() + "\n");
        result.append("Коротка ссылка: ").append(shortUrl.setShortUrl() + "\n");
        result.append("Максимальное кол-во переходов: ").append(shortUrl.setClickLimit() + "\n");
        result.append("Оставшееся кол-во переходов: ").append(shortUrl.setCurrentClicks() + "\n");
        result.append("Время блокирования: ").append(shortUrl.setExpiresAt() + "\n");
        result.append("Статус: ").append(shortUrl.status);
        return result.toString();
    }

    public static String openShortUrl(String createUrl) { // Производит открытике короткой ссылки
        if (!validUrl(createUrl)) return "Ссылка введена неправильно";
        String id = createUrl.substring(Server.BASE_SHORT_URL.length());
        ShortUrl shortUrl = Server.urlDatebase.get(id);
        StringBuilder result = new StringBuilder();
        if (shortUrl.isActive == false) {
            result.append(shortUrl.status);
        } else {
            shortUrl.currentClicks++;
            String line = shortUrl.setOriginalURL();
            result.append(line);
            if (shortUrl.currentClicks == Integer.parseInt(shortUrl.setClickLimit())) {
                Server.urlDatebase.get(id).isActive = false;
                Server.urlDatebase.get(id).status = "Лимит переходов исчерпа";
                result.append(Server.urlDatebase.get(id).status);
            }
        }
        return result.toString();
    }

    public static String generateShortId() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder shortId = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            shortId.append(chars.charAt(random.nextInt(chars.length())));
        }
        return new String(shortId);
    }

    public static String createUUID(String userUUID) { // Создает новый UUID для пользователя или вернет который есть в системе
        if (Server.userUrl.containsKey(userUUID)) {
            return userUUID;
        }
        userUUID = UUID.randomUUID().toString();
        ArrayList<String> str = new ArrayList<>();
        Server.userUrl.put(userUUID, str);
        return userUUID;
    }


    public static void checkTimeLive() {
        for (String item : Server.urlDatebase.keySet()) {
            LocalDateTime time_url = Server.urlDatebase.get(item).expiresAt;
            LocalDateTime timeNow = LocalDateTime.now();
            if (timeNow.isAfter(time_url.plusHours(12))) {
                String uuid = Server.urlDatebase.get(item).setuuidUsers();
                String url = Server.urlDatebase.get(item).setShortUrl();
                deleteShortUrl(uuid, url);
            } else if (timeNow.isAfter(time_url)) {
                Server.urlDatebase.get(item).isActive = false;
                Server.urlDatebase.get(item).status = "Время жизни ссылки истекло";
            }
        }
    }

    public static boolean validUrl(String createUrl) {
        String id = createUrl.substring(Server.BASE_SHORT_URL.length());
        if (!Server.urlDatebase.containsKey(id))
            return false;
        return true;
    }

    public static String parseMes(String line, String userUUID) { // Обработка пресланных команд клиентом
        StringBuilder result = new StringBuilder();
        String[] words = line.split("#");
        String com = words[0].trim().toLowerCase();
        if (com.contains("/create")) {
            int clicklimit = 0;
            if(words.length <= 2) clicklimit = Server.DEFAULT_CLICK_LIMIT;
            else clicklimit = Integer.parseInt(words[2]);
            try {
                if(words.length <= 2) clicklimit = Server.DEFAULT_CLICK_LIMIT;
                else clicklimit = Integer.parseInt(words[2]);
            } catch (NumberFormatException e) {
                clicklimit = Server.DEFAULT_CLICK_LIMIT;
            }
            result.append(createShortUrl(userUUID, words[1].trim(), clicklimit));
        } else if (com.contains("/delete")) {
            result.append(deleteShortUrl(userUUID, words[1].trim()));
        } else if (com.contains("/info")) {
            if(words.length >= 2) result.append(infoOfLink(words[1].trim()));
            else result.append("Неизвестная команда");
        } else if (com.contains("/mylinks")) {
            result.append(infoAllLinkUser(userUUID));
        } else if (com.contains("/open")) {
            result.append(openShortUrl(words[1]));
        } else if (com.contains("/exit")) {
            result.append("Соединение разоравано");
        }else result.append("Неизвестная команда");
        result.append("\n***");
        return result.toString();
        }


}
