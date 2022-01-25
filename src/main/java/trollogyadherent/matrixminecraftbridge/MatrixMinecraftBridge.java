package trollogyadherent.matrixminecraftbridge;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@Mod(modid  = MatrixMinecraftBridge.MODID, name = MatrixMinecraftBridge.NAME, version = MatrixMinecraftBridge.VERSION)
public class MatrixMinecraftBridge {
    public static final String MODID = "matrixminecraftbridge";
    public static final String NAME = "Matrix Minecraft Bridge 1.7.10";
    public static final String VERSION = "@version@";
    public static final String CONFIGFILELOCATION = "config/matrix-bridge.json";

    private Config configInstance;
    private MatrixClient matrixClientInstance;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("nigger");
        configInstance = Config.getInstance();
        if (configInstance.getConfigData() == null) {
            return;
        }
        System.out.println(configInstance.getConfigData().getHost());
        matrixClientInstance = MatrixClient.getInstance();
        matrixClientInstance.connect();
    }
}
