package upgradebasictools.item;

import java.util.function.Function;
import java.util.function.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import upgradebasictools.ModMain;

public class MagicPickaxe extends ItemPickaxe {
    
    public static final int LIMIT_RANGE = 10;

    public MagicPickaxe() {
        super(ToolMaterial.DIAMOND);

        setRegistryName(ModMain.MODID, "magic_pickaxe");
        // 言語ファイルから一致する値をアイテム名として表示します
        setUnlocalizedName(ModMain.MODID + ".magickPickaxe");
    }
    
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
        boolean result = super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);

        if(state.getBlock().getHarvestTool(state).equals("pickaxe") ) {
            this.bulkDestroyBlock(worldIn, state, pos);
        }
        
        return result;
    }
    
    private void bulkDestroyBlock(World worldIn, IBlockState state, BlockPos pos) {
        int min = -1, max = 1;

        boolean whileContinueBool = true;

        Function<Integer, Function<Integer, Predicate<Integer>>> runner = (x) -> (y) -> (z) -> {
            BlockPos findPos = pos.add(x, y, z);
            IBlockState findState = worldIn.getBlockState(findPos);
            Block findBlock = findState.getBlock();
            Block destroyedBlock = state.getBlock();
            if(destroyedBlock.equals(findBlock)) {
                worldIn.destroyBlock(findPos, false);
                return true;
            }
            return false;
        };

        while(whileContinueBool) {
            whileContinueBool = checkDestroyBlocks(min, max, runner);

            min--;max++;

            if(max > LIMIT_RANGE) { break; }
        }
    }
    
    private boolean checkDestroyBlocks(int min, int max, Function<Integer, Function<Integer, Predicate<Integer>>> runner) {
        boolean checkContinue = false;
        
        for(int x = min; x <= max; x++) {
            for(int y = min; y <= max; y++) {
                if(runner.apply(x).apply(y).test(min)) {
                    checkContinue = true;
                }
                if(runner.apply(x).apply(y).test(max)) {
                    checkContinue = true;
                }
                if(runner.apply(min).apply(x).test(y)) {
                    checkContinue = true;
                }
                if(runner.apply(max).apply(x).test(y)) {
                    checkContinue = true;
                }
                if(runner.apply(y).apply(min).test(x)) {
                    checkContinue = true;
                }
                if(runner.apply(y).apply(max).test(x)) {
                    checkContinue = true;
                }
            }
        }
        
        return checkContinue;
    }
}
