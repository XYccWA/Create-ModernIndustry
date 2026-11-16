package org.xyccwa.create_modern_industry.dataGen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.xyccwa.create_modern_industry.index.CMIBlocks;
import org.xyccwa.create_modern_industry.index.CMIItems;

/**
 * 配方数据生成。
 *
 * <p>方块 / 物品 / 模型 / 战利品表由 Registrate 自动生成，只有「配方」这类需要手写逻辑的
 * 数据才放到这里。运行方式：
 * <pre>gradlew runData</pre>
 * 产物会写入 {@code src/generated/resources/data/create_modern_industry/recipe/}。
 *
 * <p>注意：Create 自己的机械配方（{@code CreateRecipeProvider} 体系）以后要单独再挂一个
 * provider，这里先用原版配方把基础链路跑通。
 */
public class CMIRecipeProvider extends RecipeProvider {

    public CMIRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // 工业机壳：8 铁锭 + 1 现代钢锭
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, CMIBlocks.INDUSTRIAL_CASING.get())
                .define('I', Items.IRON_INGOT)
                .define('S', CMIItems.MODERN_STEEL_INGOT.get())
                .pattern("III")
                .pattern("ISI")
                .pattern("III")
                .unlockedBy("has_modern_steel_ingot", has(CMIItems.MODERN_STEEL_INGOT.get()))
                .save(recipeOutput);

        // 现代钢锭：铁锭高炉冶炼
        SimpleCookingRecipeBuilder
                .blasting(Ingredient.of(Items.IRON_INGOT), RecipeCategory.MISC,
                        CMIItems.MODERN_STEEL_INGOT.get(), 0.7F, 200)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(recipeOutput);
    }
}
