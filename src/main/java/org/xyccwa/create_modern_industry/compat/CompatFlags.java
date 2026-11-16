package org.xyccwa.create_modern_industry.compat;

import net.neoforged.fml.ModList;
import org.xyccwa.create_modern_industry.CreateModernIndustry;
import org.xyccwa.create_modern_industry.config.CMIConfig;

/**
 * 兼容模组探测。
 *
 * <p>这里只使用 {@link ModList}，不引用任何软依赖的类，因此在任何环境下都能安全调用。
 * 需要真正调用某个软依赖 API 时，请把代码放到 {@code compat.sable} / {@code compat.jei}
 * 这类独立包里，并且先过一遍这里的判断——这是「缺了它也能跑」的关键。
 */
public final class CompatFlags {

    public static final String CREATE = "create";
    public static final String FLYWHEEL = "flywheel";
    public static final String SABLE = "sable";
    public static final String SIMULATED = "simulated";
    public static final String AERONAUTICS = "aeronautics";
    public static final String OFFROAD = "offroad";
    public static final String JEI = "jei";

    private CompatFlags() {
    }

    public static boolean isLoaded(String modId) {
        ModList modList = ModList.get();
        return modList != null && modList.isLoaded(modId);
    }

    /** Sable：子层级 / 物理库，Create: Aeronautics 的地基。 */
    public static boolean isSableLoaded() {
        return isLoaded(SABLE);
    }

    /** Simulated：Create: Aeronautics 的传动 / 机械模拟部分。 */
    public static boolean isSimulatedLoaded() {
        return isLoaded(SIMULATED);
    }

    /** Aeronautics：航空内容本体（螺旋桨、机翼、飞艇……）。 */
    public static boolean isAeronauticsLoaded() {
        return isLoaded(AERONAUTICS);
    }

    /** 航空学整套齐活（Sable + Simulated + Aeronautics 都在）。 */
    public static boolean isAeronauticsSuiteComplete() {
        return isSableLoaded() && isSimulatedLoaded() && isAeronauticsLoaded();
    }

    public static boolean isJeiLoaded() {
        return isLoaded(JEI);
    }

    /** 取模组版本号，未安装时返回 {@code "-"}。 */
    public static String versionOf(String modId) {
        ModList modList = ModList.get();
        if (modList == null) {
            return "-";
        }
        return modList.getModContainerById(modId)
                .map(container -> container.getModInfo().getVersion().toString())
                .orElse("-");
    }

    /** 启动时打印一行兼容性摘要，排查「到底加载了哪几个兼容模组」时很有用。 */
    public static void logDetected() {
        if (!CMIConfig.logCompatDetection()) {
            return;
        }
        CreateModernIndustry.LOGGER.info(
                "[{}] 兼容探测: create={} flywheel={} | sable={} simulated={} aeronautics={} offroad={} | jei={}",
                CreateModernIndustry.MOD_NAME,
                versionOf(CREATE), versionOf(FLYWHEEL),
                versionOf(SABLE), versionOf(SIMULATED), versionOf(AERONAUTICS), versionOf(OFFROAD),
                versionOf(JEI));
    }
}
