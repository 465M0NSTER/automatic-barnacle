import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class ShortUrl {

    public LocalDateTime expiresAt;
    public int currentClicks;
    public boolean isActive;
    public String status;
    private final String originalURL;
    private final String shortUrl;
    private final String uuidUsers;
    private final int clickLimit;

    public ShortUrl(String shortUrl, String originalURL, String userUUID, int clickLimit) {
        this.shortUrl = shortUrl;
        this.originalURL = originalURL;
        this.uuidUsers = userUUID;
        this.expiresAt = LocalDateTime.now().plusHours(Server.DEFAULT_HOURS_VALID);
        this.clickLimit = clickLimit;
        this.currentClicks = 0;
        this.isActive = true;
        this.status = "Активна";
    }

    public String setOriginalURL() {
        String value = originalURL;
        return value;
    }

    public String setShortUrl() {
        String value = shortUrl;
        return value;
    }

    public String setuuidUsers() {
        String value = uuidUsers;
        return value;
    }

    public String setExpiresAt() {
        LocalDateTime value = expiresAt;
        DateTimeFormatter sdf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String formattedDate = sdf.format(value);
        return formattedDate;
    }

    public String setClickLimit() {
        int value = clickLimit;
        return String.valueOf(value);
    }

    public String setCurrentClicks() {
        int value = clickLimit - currentClicks;
        return String.valueOf(value);
    }

    public String setIsActive() {
        boolean value = isActive;
        return String.valueOf(value);
    }

}
