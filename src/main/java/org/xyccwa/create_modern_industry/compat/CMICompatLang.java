package org.xyccwa.create_modern_industry.compat;

import org.xyccwa.create_modern_industry.CreateModernIndustry;

/**
 * 兼容层里「没有对应注册项、必须手写」的语言键。
 *
 * <p>放在这里而不是直接写进 {@code CMIPonderScenario}/{@code CMIJeiPlugin} 的原因：
 * 那两个类引用了软依赖的 API，<b>类初始化</b>时就要求对方存在；
 * 而语言键在资源生成阶段就要用到，此时对方模组不一定装。这个类只有字符串，任何环境都安全。
 *
 * <p>中文翻译在 {@code dataGen.CMIZhCnLangProvider} 里，英文在这里。
 */
public final class CMICompatLang {

    /** JEI 信息页：现代钢锭 */
    public static final String JEI_MODERN_STEEL_INGOT = "jei.create_modern_industry.modern_steel_ingot";
    /** JEI 信息页：工业机壳 */
    public static final String JEI_INDUSTRIAL_CASING = "jei.create_modern_industry.industrial_casing";

    private CMICompatLang() {
    }

    /** 把英文文案写进 registrate 的 en_us 语言表（datagen 时输出）。 */
    public static void registerRawLang() {
        CreateModernIndustry.REGISTRATE.addRawLang(JEI_MODERN_STEEL_INGOT,
                "Smelt an iron ingot in a blast furnace to obtain a Modern Steel Ingot — the base material of this mod's production chain.");
        CreateModernIndustry.REGISTRATE.addRawLang(JEI_INDUSTRIAL_CASING,
                "Crafted from 8 iron ingots and 1 modern steel ingot. A general purpose casing for buildings and machines.");
    }
}
