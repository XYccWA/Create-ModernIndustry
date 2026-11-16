package org.xyccwa.create_modern_industry.compat.sable;

import java.util.Optional;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.xyccwa.create_modern_industry.CreateModernIndustry;
import org.xyccwa.create_modern_industry.compat.CompatFlags;
import org.xyccwa.create_modern_industry.config.CMIConfig;

/**
 * Sable 软兼容入口。
 *
 * <p><b>为什么要拆成两层：</b>JVM 在解析到 {@code SableBridge} 里对 Sable 类的引用时会去加载
 * Sable 的 class。如果玩家没装 Sable，这一步会抛 {@code NoClassDefFoundError}。
 * 因此本类所有方法都先用 {@link #isAvailable()}（只看 {@link ModList}，不碰 Sable）拦一道，
 * 真正接触 Sable API 的代码全部关在内部私有类里，并再套一层 {@code catch (Throwable)}。
 *
 * <p><b>子层级是什么：</b>Sable 把航空学的飞船/载具放进独立的「子层级」里，它的坐标、朝向
 * 都和主世界解耦。判断「这个方块/实体是不是在船上」靠的就是这里的 {@link #subLevelNameAt}。
 */
public final class SableCompat {

    private SableCompat() {
    }

    /** 当前环境是否可以使用 Sable API（已安装 + 配置允许）。 */
    public static boolean isAvailable() {
        return CMIConfig.sableSubLevelAwareness() && CompatFlags.isSableLoaded();
    }

    /**
     * 查询某个世界坐标所处的 Sable 子层级名称。
     *
     * @return 子层级显示名；没有名字时退回 UUID；不在子层级里或 Sable 不可用时为空
     */
    public static Optional<String> subLevelNameAt(Level level, Vec3 position) {
        if (level == null || position == null || !isAvailable()) {
            return Optional.empty();
        }
        try {
            return SableBridge.subLevelNameAt(level, position);
        } catch (Throwable throwable) {
            // 版本不匹配、内部实现变更等任何问题都不应该影响到主流程
            CreateModernIndustry.LOGGER.warn("读取 Sable 子层级信息失败，已忽略", throwable);
            return Optional.empty();
        }
    }

    /** 该位置是否位于某个 Sable 子层级（航空学载具）内部。 */
    public static boolean isInsideSubLevel(Level level, Vec3 position) {
        return subLevelNameAt(level, position).isPresent();
    }

    /**
     * 只有 Sable 存在时才会被加载 —— 不要从其它地方直接引用这个类。
     */
    private static final class SableBridge {

        private SableBridge() {
        }

        private static Optional<String> subLevelNameAt(Level level, Vec3 position) {
            // Sable.HELPER 是 Sable 官方暴露的 companion 入口（ServiceLoader 注入）
            var access = dev.ryanhcode.sable.Sable.HELPER.getContaining(level, position);
            if (access == null) {
                return Optional.empty();
            }
            String name = access.getName();
            if (name == null || name.isBlank()) {
                return Optional.of(access.getUniqueId().toString());
            }
            return Optional.of(name);
        }
    }
}
