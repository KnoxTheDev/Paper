package org.bukkit.inventory.meta.trim;

import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a material that may be used in an {@link ArmorTrim}.
 */
public interface TrimMaterial extends Keyed, Translatable {

    // Paper start - Generated/TrimMaterial
    // @GeneratedFrom 1.21.3
    TrimMaterial AMETHYST = getTrimMaterial("amethyst");

    TrimMaterial COPPER = getTrimMaterial("copper");

    TrimMaterial DIAMOND = getTrimMaterial("diamond");

    TrimMaterial EMERALD = getTrimMaterial("emerald");

    TrimMaterial GOLD = getTrimMaterial("gold");

    TrimMaterial IRON = getTrimMaterial("iron");

    TrimMaterial LAPIS = getTrimMaterial("lapis");

    TrimMaterial NETHERITE = getTrimMaterial("netherite");

    TrimMaterial QUARTZ = getTrimMaterial("quartz");

    TrimMaterial REDSTONE = getTrimMaterial("redstone");
    // Paper end - Generated/TrimMaterial

    @NotNull
    private static TrimMaterial getTrimMaterial(@NotNull String key) {
        return Registry.TRIM_MATERIAL.getOrThrow(NamespacedKey.minecraft(key));
    }

    // Paper start - adventure
    /**
     * Get the description of this {@link TrimMaterial}.
     *
     * @return the description
     */
    net.kyori.adventure.text.@org.jetbrains.annotations.NotNull Component description();

    /**
     * @deprecated this method assumes that {@link #description()} will
     * always be a translatable component which is not guaranteed.
     */
    @Override
    @Deprecated(forRemoval = true)
    @org.jetbrains.annotations.NotNull String getTranslationKey();
    // Paper end - adventure

    // Paper start - Registry#getKey
    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#TRIM_MATERIAL}. TrimMaterials can exist without a key.
     */
    @Deprecated(forRemoval = true, since = "1.20.4")
    @Override
    org.bukkit.@org.jetbrains.annotations.NotNull NamespacedKey getKey();
    // Paper end - Registry#getKey
}
