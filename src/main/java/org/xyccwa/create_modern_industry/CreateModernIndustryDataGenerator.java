package org.xyccwa.create_modern_industry;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.xyccwa.create_modern_industry.dataGen.CMIRecipeProvider;
import org.xyccwa.create_modern_industry.dataGen.CMIZhCnLangProvider;

/**
 * 数据生成（datagen）总入口。
 *
 * <p>运行：{@code gradlew runData}，产物写入 {@code src/generated/resources/}（已被构建自动纳入资源目录）。
 *
 * <p>这里只注册「手写 provider」：
 * <ul>
 *     <li>{@link CMIRecipeProvider} —— 配方</li>
 *     <li>{@link CMIZhCnLangProvider} —— 简体中文语言文件</li>
 * </ul>
 * blockstate / 模型 / 战利品表 / 英文语言由 Registrate 依据 {@code index} 包里的链式规则自动生成。
 */
@EventBusSubscriber(modid = CreateModernIndustry.MOD_ID)
public final class CreateModernIndustryDataGenerator {

    private CreateModernIndustryDataGenerator() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // 客户端资源
        generator.addProvider(event.includeClient(), new CMIZhCnLangProvider(output));

        // 服务端数据
        generator.addProvider(event.includeServer(), new CMIRecipeProvider(output, lookupProvider));
    }
}
