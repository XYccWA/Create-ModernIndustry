package org.xyccwa.create_modern_industry;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.xyccwa.create_modern_industry.command.CMICommands;
import org.xyccwa.create_modern_industry.compat.CMICompatLang;
import org.xyccwa.create_modern_industry.compat.CompatFlags;
import org.xyccwa.create_modern_industry.compat.aeronautics.AeronauticsCompat;
import org.xyccwa.create_modern_industry.config.CMIConfig;
import org.xyccwa.create_modern_industry.index.CMIBlocks;
import org.xyccwa.create_modern_industry.index.CMICreativeTabs;
import org.xyccwa.create_modern_industry.index.CMIItems;

/**
 * Create: Modern Industry 主类。
 *
 * <p>这里只做「接线」，不含任何游戏逻辑：
 * <ul>
 *     <li>注册配置</li>
 *     <li>初始化 Registrate（Create 系模组统一的注册/数据生成中枢）</li>
 *     <li>挂上创造模式物品栏，再注册方块 / 物品</li>
 *     <li>探测兼容模组并初始化软兼容层</li>
 *     <li>注册命令</li>
 * </ul>
 *
 * <p>后续内容请按目录约定新增：
 * {@code content/} 放玩法内容（方块、方块实体、配方、界面……），
 * {@code index/} 只放注册表入口，{@code compat/} 放所有软依赖相关的代码。
 */
@Mod(CreateModernIndustry.MOD_ID)
public class CreateModernIndustry {

    /** 模组 ID / 所有注册名的命名空间 */
    public static final String MOD_ID = "create_modern_industry";
    /** 展示名，与 gradle.properties 的 mod_name 保持一致 */
    public static final String MOD_NAME = "Create: Modern Industry";

    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Create 风格的注册中枢。
     *
     * <p>用 {@code CreateRegistrate} 而不是裸的 {@code DeferredRegister} 的原因：
     * 它自带 blockstate / 模型 / 语言 / 战利品表的 datagen 规则，并且能直接接上
     * Create 的 {@code BlockStateGen}、{@code TagGen}、{@code CreateBlockEntityBuilder} 等工具。
     */
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public CreateModernIndustry(IEventBus modEventBus, ModContainer modContainer) {
        // ---- 配置 ----
        modContainer.registerConfig(ModConfig.Type.COMMON, CMIConfig.SPEC);
        modEventBus.addListener((ModConfigEvent.Loading event) -> {
            if (event.getConfig().getSpec() == CMIConfig.SPEC) {
                CMIConfig.refresh();
            }
        });
        modEventBus.addListener((ModConfigEvent.Reloading event) -> {
            if (event.getConfig().getSpec() == CMIConfig.SPEC) {
                CMIConfig.refresh();
            }
        });

        // ---- Registrate ----
        REGISTRATE.registerEventListeners(modEventBus);

        // 顺序很重要：必须先把创造模式物品栏挂成 registrate 的默认栏，
        // 之后由 REGISTRATE 创建的方块物品 / 物品才会自动进入该栏。
        CMICreativeTabs.register(modEventBus);
        CMIBlocks.register();
        CMIItems.register();

        // ---- 兼容层 ----
        CMICompatLang.registerRawLang();
        CompatFlags.logDetected();
        AeronauticsCompat.init();

        // ---- 命令 ----
        NeoForge.EVENT_BUS.addListener(CMICommands::register);
    }

    /** {@code create_modern_industry:<path>} 的简写 */
    public static ResourceLocation asResource(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
