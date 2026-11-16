package org.xyccwa.create_modern_industry;

import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.xyccwa.create_modern_industry.compat.ponder.CMIPonderPlugin;

/**
 * 纯客户端初始化。
 *
 * <p>所有只在客户端存在的类（Ponder 场景、界面、渲染器、按键）都必须只在
 * {@link Dist#CLIENT} 下被引用，避免专用服务器加载到客户端类而崩溃。
 *
 * <p>NeoForge 1.21.1 起 {@code @EventBusSubscriber} 不再需要（且已弃用）{@code bus} 属性，
 * 事件总线会按监听方法的参数类型自动判断。
 */
@EventBusSubscriber(modid = CreateModernIndustry.MOD_ID, value = Dist.CLIENT)
public final class CreateModernIndustryClient {

    private CreateModernIndustryClient() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Ponder 索引需要在客户端 setup 阶段把插件交上去，索引本身稍后统一构建
        event.enqueueWork(() -> PonderIndex.addPlugin(new CMIPonderPlugin()));
    }
}
