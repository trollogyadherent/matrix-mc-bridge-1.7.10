package trollogyadherent.matrixminecraftbridge;

import de.jojii.matrixclientserver.File.FileHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Config {
    private String sConfigFile;
    private ConfigData configData;
    public static Config instance;

    public Config() {
        System.setProperty("file.encoding", "UTF-8");
        java.lang.reflect.Field charset = null;
        try {
            charset = java.nio.charset.Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (Exception e) {
            System.out.println("Something went wrong with charset accessibility");
        }
        sConfigFile = MatrixMinecraftBridge.CONFIGFILELOCATION;
        try {
            File configFile = new File(sConfigFile);
            if (!configFile.exists()) {
                System.out.println("Configfile not exists. Creating default-config");
                if (configFile.createNewFile()) {
                    FileHelper.writeFile(sConfigFile, "{\n" +
                            "  \"host\": \"http://home.server\",\n" +
                            "  \"username\": \"@matrix-bot:home.server\",\n" +
                            "  \"password\": \"ilikepie\",\n" +
                            "  \"usertoken\": \"user70ken\",\n" +
                            "  \"roomid\": \"!xxxxxx\",\n" +
                            "  \"startupMessage\": \"❇ Server has started\",\n" +
                            "  \"stopMessage\": \"\uD83D\uDED1 Server has stopped\",\n" +
                            "  \"joinMessage\": \"✅ %player% has joined the server\",\n" +
                            "  \"leaveMessage\": \"❌ %player% has left the server\",\n" +
                            "  \"deathMessage\": \"\uD83D\uDC80 %player% just died due to %reason%\",\n" +
                            "  \"achievementMessage\": \"\uD83C\uDF86 %player% just gained the achievement %achievement%\\n%description%\"\n" +
                            "}\n");
                    System.out.println("Default-config created!\r\n" + "You can pass username and password OR usertoken\r\nAdjust the config and restart the bot\r\nPassword will be cleaned and replaced with usertoken");
                    //System.exit(0);
                } else {
                    System.out.println("Couldn't create default-config! Exiting");
                    //System.exit(1);
                }
            } else {
                String configContent = FileHelper.readFile(sConfigFile);
                try {
                    JSONObject config = new JSONObject(configContent);
                    if (!config.has("host") || !config.has("username") || !config.has("password") || !config.has("usertoken") || !config.has("roomid")) {
                        throw new JSONException("missing paramter!");
                    }
                    String host = config.getString("host");
                    String username = config.getString("username");
                    String password = config.getString("password");
                    String usertoken = config.getString("usertoken");
                    String roomId = config.getString("roomid");
                    String startupMessage = config.getString("startupMessage");
                    String stopMessage = config.getString("stopMessage");
                    String joinMessage = config.getString("joinMessage");
                    String leaveMessage = config.getString("leaveMessage");
                    String deathMessage = config.getString("deathMessage");
                    String achievementMessage = config.getString("achievementMessage");
                    configData = new ConfigData(host, username, password, usertoken, roomId, startupMessage, stopMessage, joinMessage, leaveMessage, deathMessage, achievementMessage);
                } catch (JSONException e) {
                    System.out.println("Configfile in from format! Watch for correct json-content!\r\nYou can delete the config and restart the bot to create a blank config");
                    //System.exit(1);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    public ConfigData getConfigData() {
        return configData;
    }
}
