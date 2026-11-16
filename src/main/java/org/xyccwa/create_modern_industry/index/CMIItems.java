package org.xyccwa.create_modern_industry.index;

import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.item.Item;
import org.xyccwa.create_modern_industry.CreateModernIndustry;
import org.xyccwa.create_modern_industry.util.CMITags;

/**
 * 物品注册入口。
 *
 * <p>{@code item(name, factory)} 的默认规则已经包含：
 * <ul>
 *     <li>{@code item/generated} 物品模型（贴图 {@code item/<name>}）</li>
 *     <li>英文语言键（下划线自动转空格并词首大写）</li>
 * </ul>
 * 中文语言由 {@code dataGen.CMIZhCnLangProvider} 生成。
 */
public final class CMIItems {

    private CMIItems() {
    }

    /** 现代钢锭：本模组的基础材料，沿用 c: 通用标签方便与别的模组互相识别 */
    public static final ItemEntry<Item> MODERN_STEEL_INGOT = CreateModernIndustry.REGISTRATE
            .item("modern_steel_ingot", Item::new)
            .tag(CMITags.Items.INGOTS_MODERN_STEEL)
            .register();

    /** 触发本类的静态初始化（真正的注册工作发生在字段初始化时）。 */
    public static void register() {
        // NO-OP: 见上方字段声明
    }
}
