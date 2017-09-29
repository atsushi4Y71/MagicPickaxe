package upgradebasictools.item;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import upgradebasictools.ModMain;

public class MagicPickaxe extends ItemPickaxe {
    
    public static final int DEFAULT_LIMIT_RANGE = 10;

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
            this.destroyBlocksAtOnce(worldIn, state, pos);
        }
        
        return result;
    }
    
    private void destroyBlocksAtOnce(World worldIn, IBlockState state, BlockPos pos) {
        int min = -1, max = 1;
        boolean whileContinueBool = true;

        ArrayList<BlockPos> destoryBlocks;
        while(whileContinueBool) {
            destoryBlocks = getDestroyBlocks(min, max, worldIn, state, pos);
            
            destoryBlocks.forEach(destoryPos -> worldIn.destroyBlock(destoryPos, true));

            min--;max++;

            if(max > DEFAULT_LIMIT_RANGE || destoryBlocks.isEmpty()) { break; }
        }
    }
    
    private ArrayList<BlockPos> getDestroyBlocks(int min, int max, World worldIn, IBlockState state, BlockPos pos) {
        ArrayList<BlockPos> resultDestoryBlocks = new ArrayList<BlockPos>();
        
        Function<Integer, Function<Integer, Function<Integer, BlockPos>>> checker = (x) -> (y) -> (z) -> {
            BlockPos findPos = pos.add(x, y, z);
            IBlockState findState = worldIn.getBlockState(findPos);
            Block findBlock = findState.getBlock();
            Block destroyedBlock = state.getBlock();
            if(destroyedBlock.equals(findBlock)) {
                return findPos;
            }
            return null; 
        };
        
        Function<BlockPos, Consumer<ArrayList<BlockPos>>> adder = (findPos) -> (destroyBlocks) -> {
            if(findPos == null) { return; }
            destroyBlocks.add(findPos);
        };

        for(int i=min; i<= max; i++) {
            for(int j=min; j<=max; j++) {
                adder.apply(checker.apply(i).apply(j).apply(min)).accept(resultDestoryBlocks);
                adder.apply(checker.apply(i).apply(j).apply(max)).accept(resultDestoryBlocks);
                adder.apply(checker.apply(j).apply(min).apply(i)).accept(resultDestoryBlocks);
                adder.apply(checker.apply(j).apply(max).apply(i)).accept(resultDestoryBlocks);
                adder.apply(checker.apply(min).apply(i).apply(j)).accept(resultDestoryBlocks);
                adder.apply(checker.apply(max).apply(i).apply(j)).accept(resultDestoryBlocks);
            }
        }
        
        return resultDestoryBlocks;
    }
}
