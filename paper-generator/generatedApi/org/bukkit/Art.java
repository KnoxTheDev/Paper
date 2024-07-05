package org.bukkit;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Locale;
import org.bukkit.packs.DataPack;
import org.bukkit.util.OldEnum;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the art on a painting.
 * <p>
 * The arts listed in this interface are present in the default server
 * or can be enabled via a {@link FeatureFlag}.
 * There may be additional arts present in the server, for example from a {@link DataPack}
 * which can be accessed via {@link Registry#ART}.
 */
public interface Art extends OldEnum<Art>, Keyed {
    // Paper start - Generated/Art
    // @GeneratedFrom 1.21.3
    Art ALBAN = getArt("alban");

    Art AZTEC = getArt("aztec");

    Art AZTEC2 = getArt("aztec2");

    Art BACKYARD = getArt("backyard");

    Art BAROQUE = getArt("baroque");

    Art BOMB = getArt("bomb");

    Art BOUQUET = getArt("bouquet");

    Art BURNING_SKULL = getArt("burning_skull");

    Art BUST = getArt("bust");

    Art CAVEBIRD = getArt("cavebird");

    Art CHANGING = getArt("changing");

    Art COTAN = getArt("cotan");

    Art COURBET = getArt("courbet");

    Art CREEBET = getArt("creebet");

    Art DONKEY_KONG = getArt("donkey_kong");

    Art EARTH = getArt("earth");

    Art ENDBOSS = getArt("endboss");

    Art FERN = getArt("fern");

    Art FIGHTERS = getArt("fighters");

    Art FINDING = getArt("finding");

    Art FIRE = getArt("fire");

    Art GRAHAM = getArt("graham");

    Art HUMBLE = getArt("humble");

    Art KEBAB = getArt("kebab");

    Art LOWMIST = getArt("lowmist");

    Art MATCH = getArt("match");

    Art MEDITATIVE = getArt("meditative");

    Art ORB = getArt("orb");

    Art OWLEMONS = getArt("owlemons");

    Art PASSAGE = getArt("passage");

    Art PIGSCENE = getArt("pigscene");

    Art PLANT = getArt("plant");

    Art POINTER = getArt("pointer");

    Art POND = getArt("pond");

    Art POOL = getArt("pool");

    Art PRAIRIE_RIDE = getArt("prairie_ride");

    Art SEA = getArt("sea");

    Art SKELETON = getArt("skeleton");

    Art SKULL_AND_ROSES = getArt("skull_and_roses");

    Art STAGE = getArt("stage");

    Art SUNFLOWERS = getArt("sunflowers");

    Art SUNSET = getArt("sunset");

    Art TIDES = getArt("tides");

    Art UNPACKED = getArt("unpacked");

    Art VOID = getArt("void");

    Art WANDERER = getArt("wanderer");

    Art WASTELAND = getArt("wasteland");

    Art WATER = getArt("water");

    Art WIND = getArt("wind");

    Art WITHER = getArt("wither");
    // Paper end - Generated/Art

    @NotNull
    private static Art getArt(@NotNull String key) {
        return Registry.ART.getOrThrow(NamespacedKey.minecraft(key));
    }

    /**
     * Gets the width of the painting, in blocks
     *
     * @return The width of the painting, in blocks
     */
    int getBlockWidth();

    /**
     * Gets the height of the painting, in blocks
     *
     * @return The height of the painting, in blocks
     */
    int getBlockHeight();

    /**
     * Get the ID of this painting.
     *
     * @return The ID of this painting
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    int getId();

    // Paper start - deprecate getKey
    /**
     * @deprecated use {@link Registry#getKey(Keyed)}, {@link io.papermc.paper.registry.RegistryAccess#getRegistry(io.papermc.paper.registry.RegistryKey)},
     * and {@link io.papermc.paper.registry.RegistryKey#PAINTING_VARIANT}. Painting variants can exist without a key.
     */
    @Deprecated(since = "1.21")
    @Override
    @NotNull NamespacedKey getKey();
    // Paper end - deprecate getKey

    // Paper start - name and author components, assetId key
    /**
     * Get the painting's title.
     *
     * @return the title
     */
    net.kyori.adventure.text.@Nullable Component title();

    /**
     * Get the painting's author.
     *
     * @return the author
     */
    net.kyori.adventure.text.@Nullable Component author();

    /**
     * Get the painting's asset id
     *
     * @return the asset id
     */
    net.kyori.adventure.key.@NotNull Key assetId();
    // Paper end - name and author components, assetId key

    /**
     * Get a painting by its numeric ID
     *
     * @param id The ID
     * @return The painting
     * @deprecated Magic value
     */
    @Deprecated(since = "1.6.2")
    @Nullable
    static Art getById(int id) {
        for (Art art : Registry.ART) {
            if (id == art.getId()) {
                return art;
            }
        }

        return null;
    }

    /**
     * Get a painting by its unique name
     * <p>
     * This ignores capitalization
     *
     * @param name The name
     * @return The painting
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @Deprecated(since = "1.21.3")
    @Nullable
    static Art getByName(@NotNull String name) {
        Preconditions.checkArgument(name != null, "Name cannot be null");

        return Bukkit.getUnsafe().get(Registry.ART, NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
    }

    /**
     * @param name of the art.
     * @return the art with the given name.
     * @deprecated only for backwards compatibility, use {@link Registry#get(NamespacedKey)} instead.
     */
    @NotNull
    @Deprecated(since = "1.21.3")
    static Art valueOf(@NotNull String name) {
        Art art = Bukkit.getUnsafe().get(Registry.ART, NamespacedKey.fromString(name.toLowerCase(Locale.ROOT)));
        Preconditions.checkArgument(art != null, "No art found with the name %s", name);
        return art;
    }

    /**
     * @return an array of all known arts.
     * @deprecated use {@link Registry#iterator()}.
     */
    @NotNull
    @Deprecated(since = "1.21.3")
    static Art[] values() {
        return Lists.newArrayList(Registry.ART).toArray(new Art[0]);
    }
}
