package org.xyccwa.create_modern_industry.index;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.xyccwa.create_modern_industry.CreateModernIndustry;

/**
 * 创造模式物品栏。
 *
 * <p>这里用普通 {@link DeferredRegister} 而不是 registrate 自动生成的栏，
 * 好处是物品栏的 id / 标题 key 完全可控：
 * <pre>itemGroup.create_modern_industry.main</pre>
 *
 * <p>栏内的物品由 registrate 通过 {@code BuildCreativeModeTabContentsEvent} 自动填充，
 * 因此 {@link #MAIN} 的 {@code displayItems} 保持为空即可；
 * 想手动塞额外物品（例如把别的模组的物品也列进来）再往这里写。
 */
public final class CMICreativeTabs {

    /** 物品栏 id：{@code create_modern_industry:main} */
    public static final String MAIN_ID = "main";

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CreateModernIndustry.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register(MAIN_ID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + CreateModernIndustry.MOD_ID + "." + MAIN_ID))
            .icon(() -> new ItemStack(CMIBlocks.INDUSTRIAL_CASING.get()))
            .displayItems((parameters, output) -> {
                // 物品由 registrate 自动加入；需要额外排序/追加时在这里处理
            })
            .build());

    private CMICreativeTabs() {
    }

    /**
     * 必须在注册任何方块 / 物品之前调用。
     */
    public static void register(IEventBus modBus) {
        TABS.register(modBus);
        // 把本栏设为 registrate 的默认栏：之后 REGISTRATE 建的所有物品都会自动进这个栏
        CreateModernIndustry.REGISTRATE.defaultCreativeTab(MAIN.getKey());
        // 栏标题（英文）由 registrate 的 datagen 写出；这里直接写死一份，避免依赖自动命名
        CreateModernIndustry.REGISTRATE.addRawLang(
                "itemGroup." + CreateModernIndustry.MOD_ID + "." + MAIN_ID,
                CreateModernIndustry.MOD_NAME);
    }
}
