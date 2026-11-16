package org.xyccwa.create_modern_industry.compat.ponder;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.xyccwa.create_modern_industry.CreateModernIndustry;

/**
 * Ponder（Create 的「思考」教学场景）客户端插件。
 *
 * <p>在 {@code CreateModernIndustryClient} 里通过 {@code PonderIndex.addPlugin} 注册。
 *
 * <p><b>加一个教学场景需要两样东西：</b>
 * <ol>
 *     <li>结构文件：{@code assets/create_modern_industry/ponder/<scene>.nbt}
 *         （在游戏里用 Create 的蓝图与笔框选后保存，再改名为该路径）</li>
 *     <li>代码：在 {@link #registerScenes} 里用
 *         {@code helper.addStoryBoard(blockId, "scene", storyBoard)} 绑定，
 *         其中 {@code storyBoard} 是 {@code (scene, util) -> { ... }} 的 lambda，
 *         用 {@code scene.title / showBasePlate / world().showSection / overlay().showText} 写剧本</li>
 * </ol>
 *
 * <p>还有第三样可选的东西：{@link #registerTags} 里的 Ponder 标签（索引页分组）。
 */
public class CMIPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return CreateModernIndustry.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        // 示例（等结构文件与内容就绪后取消注释）：
        //
        // helper.addStoryBoard(
        //         CMIBlocks.INDUSTRIAL_CASING.getId(),
        //         "industrial_casing",
        //         (scene, util) -> {
        //             scene.title("industrial_casing", "工业机壳");
        //             scene.showBasePlate();
        //             scene.idle(10);
        //             scene.world().showSection(util.select().position(2, 1, 2), Direction.DOWN);
        //             scene.idle(20);
        //             scene.overlay().showText(60)
        //                     .text("现代工业的基础外壳")
        //                     .pointAt(util.vector().topOf(2, 1, 2));
        //             scene.idle(70);
        //         });
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        // 示例：
        //
        // helper.registerTag(CreateModernIndustry.asResource("modern_industry"))
        //         .title("现代工业")
        //         .description("Create: Modern Industry 的机械与材料")
        //         .addToIndex()
        //         .item(CMIBlocks.INDUSTRIAL_CASING.get())
        //         .register();
    }
}
