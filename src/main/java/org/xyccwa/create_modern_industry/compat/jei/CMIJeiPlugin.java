package org.xyccwa.create_modern_industry.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.xyccwa.create_modern_industry.CreateModernIndustry;
import org.xyccwa.create_modern_industry.compat.CMICompatLang;
import org.xyccwa.create_modern_industry.index.CMIBlocks;
import org.xyccwa.create_modern_industry.index.CMIItems;

/**
 * JEI 插件（软依赖）。
 *
 * <p>安全性：本类只在 JEI 的插件扫描阶段被 JEI 自己加载（靠 {@link JeiPlugin} 注解发现）。
 * 玩家没装 JEI 时这个类永远不会被 JVM 加载，因此 {@code compileOnly} 引用的 JEI API 不会造成崩溃。
 *
 * <p>这里做两件最基础的事：
 * <ul>
 *     <li>给本模组的物品挂「信息页」（JEI 用途/来源面板里的文字说明）</li>
 *     <li>留好配方分类 / 催化剂的注册入口（以后加机械类配方时在这里注册）</li>
 * </ul>
 */
@JeiPlugin
public class CMIJeiPlugin implements IModPlugin {

    private static final ResourceLocation UID = CreateModernIndustry.asResource("jei_plugin");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addIngredientInfo(
                new ItemStack(CMIItems.MODERN_STEEL_INGOT.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable(CMICompatLang.JEI_MODERN_STEEL_INGOT));

        registration.addIngredientInfo(
                new ItemStack(CMIBlocks.INDUSTRIAL_CASING.get()),
                VanillaTypes.ITEM_STACK,
                Component.translatable(CMICompatLang.JEI_INDUSTRIAL_CASING));
    }

    // 以后加自定义配方分类时再实现：
    // @Override
    // public void registerCategories(IRecipeCategoryRegistration registration) { ... }
    //
    // 需要把机器登记成某个分类的催化剂时：
    // @Override
    // public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) { ... }
}
