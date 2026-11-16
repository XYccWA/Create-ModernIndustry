package org.xyccwa.create_modern_industry.dataGen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import org.xyccwa.create_modern_industry.CreateModernIndustry;
import org.xyccwa.create_modern_industry.index.CMIBlocks;
import org.xyccwa.create_modern_industry.index.CMICreativeTabs;
import org.xyccwa.create_modern_industry.index.CMIItems;

/**
 * 简体中文语言文件数据生成。
 *
 * <p>英文（{@code en_us}）由 Registrate 根据注册名自动生成，因此这里只需要补中文；
 * 两边都只写「翻译」，不写业务逻辑。
 *
 * <p>产物：{@code src/generated/resources/assets/create_modern_industry/lang/zh_cn.json}
 */
public class CMIZhCnLangProvider extends LanguageProvider {

    public CMIZhCnLangProvider(PackOutput output) {
        super(output, CreateModernIndustry.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        // ---- 方块 / 物品 ----
        add(CMIBlocks.INDUSTRIAL_CASING.get(), "工业机壳");
        add(CMIItems.MODERN_STEEL_INGOT.get(), "现代钢锭");

        // ---- 创造模式物品栏 ----
        add("itemGroup." + CreateModernIndustry.MOD_ID + "." + CMICreativeTabs.MAIN_ID, "Create：现代工业");

        // ---- JEI 信息页 ----
        add("jei.create_modern_industry.modern_steel_ingot",
                "把铁锭送进高炉冶炼即可得到现代钢锭；它是现代工业产线的基础材料。");
        add("jei.create_modern_industry.industrial_casing",
                "由 8 个铁锭与 1 个现代钢锭合成的通用机壳，可作为建筑与机械外壳使用。");
    }
}
