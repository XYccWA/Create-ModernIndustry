package org.xyccwa.create_modern_industry.compat.aeronautics;

import java.util.Optional;

import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.xyccwa.create_modern_industry.CreateModernIndustry;
import org.xyccwa.create_modern_industry.compat.CompatFlags;
import org.xyccwa.create_modern_industry.compat.sable.SableCompat;

/**
 * Create: Aeronautics（航空学）软兼容入口。
 *
 * <p>航空学不是一个模组，而是一套：
 * <ul>
 *     <li>{@code sable} —— 子层级与物理引擎（飞船独立的坐标系 / 碰撞 / 保存）</li>
 *     <li>{@code simulated} —— 传动与机械模拟（把 Create 的旋转动力接到力学系统上）</li>
 *     <li>{@code aeronautics} —— 航空内容本体（螺旋桨、机翼、飞艇之类）</li>
 *     <li>{@code offroad} —— 轮胎 / 地面载具内容</li>
 * </ul>
 *
 * <p>本类只做「探测 + 转发」，真正的物理查询放在 {@link SableCompat}。
 * 以后要给航空学加东西（例如让某个方块在子层级里表现不同），扩展点就写在这里。
 */
public final class AeronauticsCompat {

    private AeronauticsCompat() {
    }

    /** 启动时调用一次，把探测结果写进日志，并留作后续挂载兼容逻辑的地方。 */
    public static void init() {
        if (!CompatFlags.isSableLoaded() && !CompatFlags.isSimulatedLoaded() && !CompatFlags.isAeronauticsLoaded()) {
            CreateModernIndustry.LOGGER.info("[{}] 未检测到 Create: Aeronautics，航空相关功能保持关闭",
                    CreateModernIndustry.MOD_NAME);
            return;
        }

        CreateModernIndustry.LOGGER.info("[{}] 检测到 Create: Aeronautics（sable={} simulated={} aeronautics={} offroad={}）",
                CreateModernIndustry.MOD_NAME,
                CompatFlags.versionOf(CompatFlags.SABLE),
                CompatFlags.versionOf(CompatFlags.SIMULATED),
                CompatFlags.versionOf(CompatFlags.AERONAUTICS),
                CompatFlags.versionOf(CompatFlags.OFFROAD));

        // 后续要注册 Sable 的物理钩子（力组、约束、碰撞回调……）时，在这里按 GAME 总线挂事件，
        // 并且继续遵守「先判断、后接触 API」的写法。
    }

    /** 航空学整套是否齐全。 */
    public static boolean isAvailable() {
        return CompatFlags.isAeronauticsSuiteComplete();
    }

    /** 物理/子层级查询是否可用（只要 Sable 在就行）。 */
    public static boolean isPhysicsAvailable() {
        return SableCompat.isAvailable();
    }

    /**
     * 查询坐标所在航空载具（子层级）的名字。
     *
     * @see SableCompat#subLevelNameAt(Level, Vec3)
     */
    public static Optional<String> subLevelNameAt(Level level, Vec3 position) {
        return SableCompat.subLevelNameAt(level, position);
    }

    /** 该坐标是否在一块航空载具（子层级）上。 */
    public static boolean isOnAircraft(Level level, Vec3 position) {
        return SableCompat.isInsideSubLevel(level, position);
    }
}
