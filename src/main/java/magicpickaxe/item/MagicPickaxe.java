package magicpickaxe.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import magicpickaxe.ModMain;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MagicPickaxe extends ItemPickaxe {
    
    public static final int DEFAULT_LIMIT_RANGE = 10;

    public MagicPickaxe() {
        super(ToolMaterial.DIAMOND);

        setRegistryName(ModMain.MODID, "magic_pickaxe");
        // 言語ファイルから一致する値をアイテム名として表示します
        setUnlocalizedName(ModMain.MODID + ".magicPickaxe");
    }
    
    /***
     * ブロックを破壊した時に呼び出される。
     * @param stack 破壊した時に手に持っているアイテム
     * @param worldIn
     * @param state IBlockState
     * @param pos BlockPos 破壊したブロックの座標
     * @param entityLiving entityLiving
     */
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        // 上書き元の処理をこちらに書くくらいなら呼び出した方が楽だと判断しました
        // 条件の意味は分かりませんでしたが、手持ちのアイテムにダメージを与えている所を見ると耐久度を減らす処理のようです
        boolean result = super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);

        // 破壊したブロックがピッケル適性の場合だけ隣接するブロックを破壊します
        if(state.getBlock().getHarvestTool(state).equals("pickaxe") ) {

            // 隣接する同種ブロックを一括破壊する
            this.destroyBlocksAtOnce(worldIn, state, pos);
            
            // 周辺のモブやドロップアイテムをエンティティと呼ぶんですが、それをまとめて取得しています
            List<EntityItem> list = worldIn.getEntities(EntityItem.class, EntitySelectors.IS_ALIVE);
            EntityItem entityItem = null;
            
            // エンティティを一つずつ破壊したブロックと同じか調べ、
            // 同じ場合は一つのエンティティにまとめます
            // ばらばらに落ちているアイテムを拾いに行くの面倒ですよね
            for(EntityItem entity : list) {
                if(entity.getItem().getItem().getUnlocalizedName().equals(state.getBlock().getUnlocalizedName())) {
                    if(entityItem == null) {
                        entityItem = entity;
                    } else {
                        entityItem.getItem().setCount(entityItem.getItem().getCount() + entity.getItem().getCount());
                        worldIn.removeEntity(entity);
                    }
                }
            }
        }
        
        return result;
    }

    /***
     * 隣接する破壊したブロックと同種のブロックを一括破壊する
     * @param worldIn
     * @param state
     * @param pos
     */
    private void destroyBlocksAtOnce(World worldIn, IBlockState state, BlockPos pos) {
        int min = -1, max = 1;
        boolean whileContinueBool = true;

        ArrayList<BlockPos> destoryBlocks;
        // 破壊したブロックの周囲を範囲を拡大しながらブロックを破壊する
        while(whileContinueBool) {
            destoryBlocks = getDestroyBlocks(min, max, worldIn, state, pos);
            
            // リスト内のブロックを破壊する
            destoryBlocks.forEach(destoryPos -> worldIn.destroyBlock(destoryPos, true));

            // 破壊範囲を拡大する
            min--;max++;

            // 破壊範囲が上限に到達するかリストが空の場合は処理を終える
            if(max > DEFAULT_LIMIT_RANGE || destoryBlocks.isEmpty()) { break; }
        }
    }
    
    /***
     * 破壊するブロックを取得する。
     * @param min 破壊する範囲の最小値
     * @param max 破壊する範囲の最大値
     * @param worldIn
     * @param state
     * @param pos
     * @return 破壊するブロックの配列
     */
    private ArrayList<BlockPos> getDestroyBlocks(int min, int max, World worldIn, IBlockState state, BlockPos pos) {
        // 呼び出し元に返却する破壊するブロックの配列
        ArrayList<BlockPos> resultDestoryBlocks = new ArrayList<BlockPos>();
        
        // 破壊するブロックの座標を取得する
        // ここはラムダ式なので後続の処理から呼び出される
        Function<Integer, Function<Integer, Function<Integer, BlockPos>>> checker = (x) -> (y) -> (z) -> {
            // 壊したブロックを起点に隣接するブロックを取得
            BlockPos findPos = pos.add(x, y, z);
            IBlockState findState = worldIn.getBlockState(findPos);
            Block findBlock = findState.getBlock();

            // 壊したブロック
            Block destroyedBlock = state.getBlock();

            // 同種のブロックであれば破壊するブロックとして取得する
            if(destroyedBlock.equals(findBlock)) {
                return findPos;
            }
            return null;
        };
        
        // ブロックの座標がNULLの場合はリストに追加しない
        Function<BlockPos, Consumer<ArrayList<BlockPos>>> adder = (findPos) -> (destroyBlocks) -> {
            if(findPos == null) { return; }
            destroyBlocks.add(findPos);
        };

        // 破壊するブロックのリストを作成する
        for(int i=min; i<= max; i++) {
            for(int j=min; j<=max; j++) {
                /* 
                 * どう説明していいのか分からない
                 * 破壊したブロックを起点に次の順番に探索を行います
                 * x y zの最小値
                 * x y zの最大値
                 * z x yの最小値
                 * z x yの最大値
                 * y z xの最小値
                 * y z xの最大値
                 */
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
