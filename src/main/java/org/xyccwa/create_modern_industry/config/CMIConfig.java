package org.xyccwa.create_modern_industry.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 通用配置（{@code config/create_modern_industry-common.toml}）。
 *
 * <p>NeoForge 的配置值在对应配置文件被加载前读取会抛异常，所以这里统一用
 * {@link #refresh()} 在 {@code ModConfigEvent.Loading/Reloading} 时把值缓存成普通字段，
 * 游戏内其它地方只读缓存字段。
 */
public final class CMIConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue LOG_COMPAT_DETECTION;
    private static final ModConfigSpec.BooleanValue SABLE_SUBLEVEL_AWARENESS;

    static {
        BUILDER.push("compat");
        LOG_COMPAT_DETECTION = BUILDER
                .comment("启动时把检测到的兼容模组（Create / Sable / Simulated / Aeronautics / Offroad / JEI）写入日志。")
                .define("logDetection", true);
        SABLE_SUBLEVEL_AWARENESS = BUILDER
                .comment("允许读取 Sable 子层级（航空学飞船）信息。关闭后本模组完全不碰 Sable API。")
                .define("sableSubLevelAwareness", true);
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    private static volatile boolean logCompatDetection = true;
    private static volatile boolean sableSubLevelAwareness = true;

    private CMIConfig() {
    }

    /** 由 {@code ModConfigEvent.Loading/Reloading} 调用，把配置值搬进缓存字段。 */
    public static void refresh() {
        logCompatDetection = read(LOG_COMPAT_DETECTION, true);
        sableSubLevelAwareness = read(SABLE_SUBLEVEL_AWARENESS, true);
    }

    public static boolean logCompatDetection() {
        return logCompatDetection;
    }

    public static boolean sableSubLevelAwareness() {
        return sableSubLevelAwareness;
    }

    private static boolean read(ModConfigSpec.BooleanValue value, boolean fallback) {
        try {
            return value.get();
        } catch (RuntimeException ignored) {
            // 配置尚未加载（例如刚注册、或数据生成环境）：退回默认值
            return fallback;
        }
    }
}
