package trollogyadherent.matrixminecraftbridge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.*;
import net.minecraftforge.common.MinecraftForge;


@Mod(modid  = MatrixMinecraftBridge.MODID, name = MatrixMinecraftBridge.NAME, version = MatrixMinecraftBridge.VERSION, acceptableRemoteVersions = "*")
public class MatrixMinecraftBridge {
    public static final String MODID = "matrixminecraftbridge";
    public static final String NAME = "Matrix Minecraft Bridge 1.7.10";
    public static final String VERSION = "@version@";
    public static final String CONFIGFILELOCATION = "config/matrix-bridge.json";

    private Config configInstance;
    private MatrixClient matrixClientInstance;

    private MinecraftListener minecraftListener = new MinecraftListener();

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(minecraftListener);
        FMLCommonHandler.instance().bus().register(minecraftListener);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        configInstance = Config.getInstance();
        if (configInstance.getConfigData() == null) {
            System.out.println("Warning! Incorrect or missing config file!");
            return;
        }
        System.out.println(configInstance.getConfigData().getHost());
        matrixClientInstance = MatrixClient.getInstance();
        matrixClientInstance.connect();
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
        if (configInstance == null) {
            configInstance = Config.getInstance();
        }
        MatrixClient.getInstance().sendToMatrix(configInstance.getConfigData().getStartupMessage());
    }

    @Mod.EventHandler
    public void onServerStopped(FMLServerStoppedEvent event) {
        if (configInstance == null) {
            configInstance = Config.getInstance();
        }
        MatrixClient.getInstance().sendToMatrix(configInstance.getConfigData().getStopMessage());
    }
}
