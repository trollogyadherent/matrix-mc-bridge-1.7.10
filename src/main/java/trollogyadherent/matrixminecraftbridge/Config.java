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

        sConfigFile = MatrixMinecraftBridge.CONFIGFILELOCATION;
        try {
            File configFile = new File(sConfigFile);
            if (!configFile.exists()) {
                System.out.println("Configfile not exists. Creating default-config");
                if (configFile.createNewFile()) {
                    FileHelper.writeFile(sConfigFile, "{\n" +
                            "  \"host\": \"http://matrix.org\",\n" +
                            "  \"username\": \"grepbot\",\n" +
                            "  \"password\": \"wordpass\",\n" +
                            "  \"usertoken\": \"user70ken\"\n" +
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
                    if (!config.has("host") || !config.has("username") || !config.has("password") || !config.has("usertoken")) {
                        throw new JSONException("missing paramter!");
                    }
                    String host = config.getString("host");
                    String username = config.getString("username");
                    String password = config.getString("password");
                    String usertoken = config.getString("usertoken");
                    configData = new ConfigData(host, username, password, usertoken);
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
