package trollogyadherent.matrixminecraftbridge;

import de.jojii.matrixclientserver.Bot.Client;
import de.jojii.matrixclientserver.Bot.Events.RoomEvent;
import de.jojii.matrixclientserver.File.FileHelper;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

public class MatrixClient {
    private static MatrixClient instance;
    private Config configInstance;

    private MatrixClient() {

    }

    public void onReady() {}

    public void connect() {
        configInstance = Config.getInstance();
        Client c = new Client(configInstance.getConfigData().getHost());
        try {
            if (configInstance.getConfigData().getPassword().trim().length() > 0) {
                final ConfigData finalConfigData = configInstance.getConfigData();

                c.login(configInstance.getConfigData().getUsername(), configInstance.getConfigData().getPassword(), data -> {
                    if (data.isSuccess()) {

                        JSONObject newConfig = new JSONObject();
                        newConfig.put("host", finalConfigData.getHost());
                        newConfig.put("username", finalConfigData.getUsername());
                        newConfig.put("password", "");
                        newConfig.put("usertoken", data.getAccess_token());
                        //save new configfile
                        FileHelper.writeFile(MatrixMinecraftBridge.CONFIGFILELOCATION, newConfig.toString());

                        clientLoggedIn(c);
                    } else {
                        System.err.println("error logging in! Check your credentials");
                    }
                });
            } else {
                c.login(configInstance.getConfigData().getToken(), data -> {
                    clientLoggedIn(c);
                });
            }
        } catch (Exception e) {
            System.out.println("Something went wrong while connecting to matrix");
        }
    }

    private void clientLoggedIn(Client c) {
        System.out.println(c.getLoginData().getAccess_token());
        c.registerRoomEventListener(roomEvents -> {
            for (RoomEvent event : roomEvents) {
                System.out.println(event.getRaw().toString());

                if (event.getType().equals("m.room.member") && event.getContent().has("membership") && event.getContent().getString("membership").equals("invite")) {
                    try {
                        //make bot autojoin
                        c.joinRoom(event.getRoom_id(), null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (event.getType().equals("m.room.message")) {
                    //Sends a readreceipt  for every received message
                    try {
                        c.sendReadReceipt(event.getRoom_id(), event.getEvent_id(), "m.read", null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (event.getContent().has("body")) {
                        String msg = RoomEvent.getBodyFromMessageEvent(event);
                        if (msg != null && msg.trim().length() > 0) {
                            if (msg.toLowerCase(Locale.ROOT).startsWith("i'm")) {
                                c.sendText(event.getRoom_id(), "Hello " + msg.substring(3), null);
                            }
                        }
                    }
                }
            }
        });
    }

    public static MatrixClient getInstance() {
        if (instance == null) {
            instance = new MatrixClient();
        }
        return instance;
    }
}