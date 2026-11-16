package org.xyccwa.create_modern_industry.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

/**
 * 本模组用到的标签。
 *
 * <p>材料类标签一律挂在 {@code c:} 命名空间下（NeoForge 的跨模组通用标签空间），
 * 这样别的模组只要也声明 {@code c:ingots/modern_steel} 就能与本模组互换材料。
 */
public final class CMITags {

    private CMITags() {
    }

    /** 物品标签 */
    public static final class Items {

        private Items() {
        }

        /** {@code c:ingots/modern_steel} —— 现代钢锭 */
        public static final TagKey<Item> INGOTS_MODERN_STEEL = commonItem("ingots/modern_steel");
    }

    private static TagKey<Item> commonItem(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }
}
