import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.zip.DataFormatException;

class ShortUrl {

      private String originalURL;
      private String shortUrl;
      private String uuidUsers;
      public LocalDateTime expiresAt;
      private int clickLimit;
      public int currentClicks;
      public boolean isActive;
      public String status;

      public ShortUrl(String shortUrl, String originalURL, String userUUID, int clickLimit){
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
