package su.eternum.eternalbans;

import org.apache.commons.io.IOUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EternalBansAPI {

    private static final JSONParser PARSER = new JSONParser();

    private static long lastCheck = 0;
    private static boolean available = false;

    public static boolean isAvailable() {
        if (System.currentTimeMillis() - lastCheck >= 60000L)
            return available;

        try {
            JSONObject json = new JSONObject();
            json.put("server-id", getServerID());
            byte[] bytes = json.toJSONString().getBytes(StandardCharsets.UTF_8);

            HttpsURLConnection cn = (HttpsURLConnection) new URL("https://api.eternum.su/status").openConnection();
            cn.setConnectTimeout(3000);
            cn.setReadTimeout(3000);
            cn.setDoOutput(true);
            cn.setFixedLengthStreamingMode(bytes.length);
            cn.connect();

            try (OutputStream os = cn.getOutputStream()) {
                os.write(bytes);
                os.flush();
            }

            if (cn.getResponseCode() == 200) {
                try (InputStream is = cn.getInputStream()) {
                    if (IOUtils.toString(is, StandardCharsets.UTF_8).equals("OK")) {
                        lastCheck = System.currentTimeMillis();
                        return available = true;
                    }
                }
            }

            lastCheck = System.currentTimeMillis();
            return available = false;
        } catch (Exception ex) {
            lastCheck = System.currentTimeMillis();
            return available = false;
        }
    }

    public static String getServerID() {
        return null; // TODO
    }

    public static Set<BanEntry> getEntries(Player player) {
        return getEntries(player.getUniqueId());
    }

    public static Set<BanEntry> getEntries(OfflinePlayer player) {
        return getEntries(player.getUniqueId());
    }

    public static Set<BanEntry> getEntries(UUID playerUUID) {
        try {
            JSONObject json = new JSONObject();
            json.put("server-id", getServerID());
            json.put("uuid", playerUUID.toString());
            byte[] bytes = json.toJSONString().getBytes(StandardCharsets.UTF_8);

            HttpsURLConnection cn = (HttpsURLConnection) new URL("https://api.eternum.su/getEntries").openConnection();
            cn.setConnectTimeout(3000);
            cn.setReadTimeout(3000);
            cn.setDoOutput(true);
            cn.setFixedLengthStreamingMode(bytes.length);
            cn.connect();

            try (OutputStream os = cn.getOutputStream()) {
                os.write(bytes);
                os.flush();
            }

            if (cn.getResponseCode() == 200) {
                try (InputStream is = cn.getInputStream()) {
                    JSONArray array = (JSONArray) PARSER.parse(IOUtils.toString(is, StandardCharsets.UTF_8));

                    return ((Stream<Object>) array.stream()).map(object -> {
                        JSONObject obj = (JSONObject) object;
                        String username = obj.get("username").toString();
                        UUID uuid = UUID.fromString(obj.get("uuid").toString());
                        String reason = obj.get("reason") == null ? null : obj.get("reason").toString();
                        Date timestamp = new Date(Long.parseLong(obj.get("timestamp").toString()));
                        Date expires = obj.get("expires") == null ? null : new Date(Long.parseLong(obj.get("expires").toString()));
                        String serverName = obj.get("serverName").toString();
                        return new BanEntry(username, uuid, reason, timestamp, expires, serverName);
                    }).collect(Collectors.toSet());
                }
            } else
                throw new RuntimeException(new IOException("Не удалось получить список банов. Ответ API: "
                        + cn.getResponseCode() + " " + cn.getResponseMessage() + "."));
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
