package org.xyccwa.create_modern_industry.index;

import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import org.xyccwa.create_modern_industry.CreateModernIndustry;

/**
 * 方块注册入口。
 *
 * <p>约定：所有注册对象都在静态字段里构造，{@link #register()} 只负责触发类初始化。
 * 这样注册代码集中在字段声明处，链式写法也最直观。
 */
public final class CMIBlocks {

    private CMIBlocks() {
    }

    /**
     * 工业机壳：本模组最基础的建筑 / 机械外壳方块。
     *
     * <p>整条链做了四件事：
     * <ol>
     *     <li>属性：复制铁块后覆盖成偏灰的金属手感</li>
     *     <li>{@code pickaxeOnly()}：自动生成 {@code minecraft:mineable/pickaxe} 方块标签</li>
     *     <li>{@code simpleCubeAll(...)}：datagen 生成 cube_all 模型 + blockstate（贴图取 {@code block/industrial_casing}）</li>
     *     <li>{@code simpleItem()}：同时生成 BlockItem 与其物品模型</li>
     * </ol>
     * 战利品表 / 英文语言键由 Registrate 的默认规则自动生成。
     */
    public static final BlockEntry<Block> INDUSTRIAL_CASING = CreateModernIndustry.REGISTRATE
            .block("industrial_casing", Block::new)
            .initialProperties(() -> Blocks.IRON_BLOCK)
            .properties(properties -> properties
                    .mapColor(MapColor.COLOR_GRAY)
                    .sound(SoundType.METAL)
                    .strength(4.0F, 8.0F))
            .transform(TagGen.pickaxeOnly())
            .blockstate(BlockStateGen.simpleCubeAll("industrial_casing"))
            .simpleItem()
            .register();

    /** 触发本类的静态初始化（真正的注册工作发生在字段初始化时）。 */
    public static void register() {
        // NO-OP: 见上方字段声明
    }
}
