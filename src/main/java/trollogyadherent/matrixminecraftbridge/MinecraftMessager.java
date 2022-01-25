package trollogyadherent.matrixminecraftbridge;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.event.ClickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinecraftMessager {
    private static MinecraftMessager instance;
    private Util utilInstance;

    public MinecraftMessager() {

    }

    public void sendToMinecraft(String message) {
        if (utilInstance == null) {
            utilInstance = Util.getInstance();
        }
        System.out.println("GOT MATRIX MESSAGE: " + message);
        ChatComponentText componentMessage = utilInstance.getChatCompWithLinks(message);
        MinecraftServer minecraftServer = MinecraftServer.getServer();
        final List<EntityPlayerMP> players = new ArrayList<>();
        minecraftServer.getConfigurationManager().playerEntityList
                .forEach(playerEntity -> {
                    if (playerEntity instanceof EntityPlayerMP) {
                        players.add((EntityPlayerMP) playerEntity);
                    }
                });
        for (EntityPlayerMP player : players) {
            player.addChatMessage(componentMessage);//new ChatComponentText(message.getFormattedTextMinecraft()));
        }
    }

    public static MinecraftMessager getInstance() {
        if (instance == null) {
            instance = new MinecraftMessager();
        }
        return instance;
    }
}
