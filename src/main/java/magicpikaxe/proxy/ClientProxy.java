package magicpikaxe.proxy;

import magicpikaxe.ModItems;
import magicpikaxe.ModMain;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

/***
 * クライアント側でリソースを読み込みます。
 * 
 * @author atsushi
 *
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ModMain.logger.info("ClientProxy.preInit");
    }

    /***
     * ブロックやアイテムのモデル（テクスチャは何を使うとかテクスチャの向きとか定義したファイル）を読み込みます。<br>
     * 
     * @param event
     */
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModMain.logger.info("ClientProxy.registerModels");

        ModelLoader.setCustomModelResourceLocation(
                ModItems.MAGIC_PICKAXE,
                0,
                new ModelResourceLocation(
                        new ResourceLocation(ModMain.MODID, "magic_pickaxe"), "inventory"));
    }
}
