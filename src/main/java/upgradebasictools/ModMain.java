package upgradebasictools;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import upgradebasictools.proxy.CommonProxy;

/***
 * Modの本体で、@Modを宣言することで本体として認識されます。<br>
 * modidの文字列はmcmod.infoのmodidと同一にしなければ、mcmod.infoが読み込まれないので設定値を合わせる必要があります。<br>
 * このクラスでMinecraftにブロックやアイテムの登録を行います。<br>
 * 
 * @author atsushi
 * @version forge-1.12-14.21.1.2426
 */
@Mod(modid = ModMain.MODID)
public class ModMain {

    // Modの識別やリソースのドメイン名に利用されます
    public static final String MODID = "upgradebasictools";

    // プロキシ（読み込み処理）のパッケージ階層
    public static final String CLIENT_PROXY = "upgradebasictools.proxy.ClientProxy";
    public static final String SERVER_PROXY = "upgradebasictools.proxy.ServerProxy";

    // サーバー、クライアントを識別しインスタンスを保持します
    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static CommonProxy proxy;

    // いまいち必要性が分からない
    @Mod.Instance
    public static ModMain instance;

    // ログ出力で使います
    public static Logger logger;

    /***
     * Mod本体のメソッド中最初に呼ばれます。<br>
     * 変数の初期化など後続の処理に影響のあることを済ませます。
     * 
     * @param event
     */
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    /***
     * preinitの後に呼ばれます。<br>
     * 言わばこのModの本体とも言える主要なメソッドです。<br>
     * とにかく主要なことはここで処理します。
     * 
     * @param event
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    /***
     * initの後に呼ばれます。つまりMod本体の中では最後に呼ばれます。<br>
     * リソースの開放や不要なデータの削除などお片付け的なことをします。
     * 
     * @param event
     */
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
