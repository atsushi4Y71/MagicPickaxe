package upgradebasictools.item;

import net.minecraft.item.ItemPickaxe;
import upgradebasictools.ModMain;

public class MagicPickaxe extends ItemPickaxe {

    public MagicPickaxe() {
        super(ToolMaterial.DIAMOND);

        setRegistryName(ModMain.MODID, "magic_pickaxe");
        // 言語ファイルから一致する値をアイテム名として表示します
        setUnlocalizedName(ModMain.MODID + ".magic_pickaxe");
    }
}
