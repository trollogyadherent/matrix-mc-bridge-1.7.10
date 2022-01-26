package trollogyadherent.matrixminecraftbridge;

import com.google.common.base.Joiner;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AchievementEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class MinecraftListener {
    private Config configInstance;

    public MinecraftListener() {

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onServerChatMessage(ServerChatEvent event) {
        //System.out.println("RECEIVED MESSAGE");
        if (event.isCanceled() || event.player == null) return;

        if (event.player instanceof FakePlayer) {
            return;
        }
        String message = "[" + event.player.getDisplayName() + "] " + event.message;
        MatrixClient.getInstance().sendToMatrix(message);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onCommand(CommandEvent event) {
        if (event.isCanceled()) return;

        String commandName = event.command.getCommandName();
        ICommandSender sender = event.sender;

        if (commandName.equalsIgnoreCase("say") || commandName.equalsIgnoreCase("me")) {
            boolean isSayCommand = commandName.equalsIgnoreCase("say");

            if (sender instanceof FakePlayer) {
                return;
            }

            String message = "[SERVER] " + Joiner.on(" ").join(event.parameters);
            MatrixClient.getInstance().sendToMatrix(message);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerAchievement(AchievementEvent event) {
        if (configInstance == null) {
            configInstance = Config.getInstance();
        }
        if (event.isCanceled()) return;

        EntityPlayer entityPlayer = event.entityPlayer;

        if (entityPlayer != null && entityPlayer instanceof EntityPlayerMP) {
            StatisticsFile playerStats = ((EntityPlayerMP) entityPlayer).func_147099_x();

            if (playerStats.hasAchievementUnlocked(event.achievement) || !playerStats.canUnlockAchievement(event.achievement)) {
                return;
            }

            String username = entityPlayer.getDisplayName();
            Achievement achievement = event.achievement;
            //System.out.println(achievement.achievementDescription);
            String description = StatCollector.translateToLocalFormatted(achievement.achievementDescription, "KEY");
            String message = configInstance.getConfigData().getAchievementMessage().replace("%player%", username).replace("%achievement%", achievement.func_150951_e ().getUnformattedText()).replace("%description%", description);
            MatrixClient.getInstance().sendToMatrix(message);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (configInstance == null) {
            configInstance = Config.getInstance();
        }
        if (event.isCanceled() || event.player == null) return;

        String username = event.player.getDisplayName();
        String message = configInstance.getConfigData().getJoinMessage().replace("%player%", username);
        MatrixClient.getInstance().sendToMatrix(message);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        if (configInstance == null) {
            configInstance = Config.getInstance();
        }
        if (event.isCanceled() || event.player == null) return;

        String username = event.player.getDisplayName();
        String message = configInstance.getConfigData().getLeaveMessage().replace("%player%", username);
        MatrixClient.getInstance().sendToMatrix(message);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onPlayerDeath(LivingDeathEvent event) {
        if (configInstance == null) {
            configInstance = Config.getInstance();
        }
        EntityLivingBase entityLiving = event.entityLiving;

        if (event.isCanceled() || entityLiving == null) return;

        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
            String reason = entityPlayer.func_110142_aN().func_151521_b().getUnformattedText().replaceFirst(entityPlayer.getDisplayName(), "").trim();
            String username = entityPlayer.getDisplayName();
            String message = configInstance.getConfigData().getDeathMessage().replace("%player%", username).replace("%reason%", reason);
            MatrixClient.getInstance().sendToMatrix(message);
        }
    }
}
