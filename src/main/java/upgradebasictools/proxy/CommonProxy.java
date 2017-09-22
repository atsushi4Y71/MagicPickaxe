package upgradebasictools.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import upgradebasictools.ModItems;
import upgradebasictools.ModMain;

/***
 * クライアントとサーバー共通でアイテム、ブロック、ツールの読み込みます。
 * 
 * @author atsushi
 *
 */
@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        ModMain.logger.info("CommonProxy.preInit");
    }

    public void init(FMLInitializationEvent event) {
        ModMain.logger.info("CommonProxy.init");
    }

    public void postInit(FMLPostInitializationEvent event) {
        ModMain.logger.info("CommonProxy.postInit");
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        ModMain.logger.info("CommonProxy.registerBlocks");
    }

    /***
     * Modで追加したいアイテムを読み込みます。<br>
     * 勿論アイテムのクラスは自分で定義する必要があります。
     * 
     * @param event
     */
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        ModMain.logger.info("CommonProxy.registerItems");
        event.getRegistry().register(ModItems.MAGIC_PICKAXE);
    }
}
