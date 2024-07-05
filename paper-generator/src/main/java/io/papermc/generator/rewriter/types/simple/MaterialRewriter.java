package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.rewriter.types.registry.EnumRegistryRewriter;
import io.papermc.generator.utils.BlockStateMapping;
import io.papermc.generator.utils.Formatting;
import io.papermc.typewriter.preset.SwitchRewriter;
import io.papermc.typewriter.preset.model.CodeBlock;
import io.papermc.typewriter.preset.model.EnumValue;
import io.papermc.typewriter.preset.model.SwitchCases;
import io.papermc.typewriter.preset.model.SwitchContent;
import java.util.Comparator;
import java.util.Locale;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallSignBlock;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.EquipmentSlot;

import static io.papermc.generator.utils.Formatting.asCode;

@Deprecated(forRemoval = true)
public class MaterialRewriter {

    // blocks

    public static class Blocks extends EnumRegistryRewriter<Block> {

        public Blocks() {
            super(Registries.BLOCK, false);
        }

        @Override
        protected Iterable<Holder.Reference<Block>> getValues() {
            return BuiltInRegistries.BLOCK.listElements().filter(reference -> !reference.value().equals(net.minecraft.world.level.block.Blocks.AIR))
                .sorted(Formatting.alphabeticKeyOrder(reference -> reference.key().location().getPath()))::iterator;
        }

        @Override
        protected EnumValue.Builder rewriteEnumValue(Holder.Reference<Block> reference) {
            EnumValue.Builder value = super.rewriteEnumValue(reference);
            Block block = reference.value();
            if (BlockStateMapping.MAPPING.containsKey(block.getClass())) {
                // some block can also be represented as item in that enum
                // doing a double job
                Optional<Item> equivalentItem = BuiltInRegistries.ITEM.getOptional(reference.key().location());

                if (equivalentItem.isEmpty() && block instanceof WallSignBlock) {
                    // wall sign block stack size is 16 for some reason like the sign item?
                    // but that rule doesn't work for the wall hanging sign block??
                    equivalentItem = Optional.of(block.asItem());
                }

                Class<?> blockData = BlockStateMapping.getBestSuitedApiClass(block.getClass());
                if (blockData == null) {
                    blockData = BlockData.class;
                }
                if (equivalentItem.isPresent() && equivalentItem.get().getDefaultMaxStackSize() != Item.DEFAULT_MAX_STACK_SIZE) {
                    return value.arguments(Integer.toString(-1), Integer.toString(equivalentItem.get().getDefaultMaxStackSize()), this.importCollector.getShortName(blockData).concat(".class"));
                }
                return value.arguments(Integer.toString(-1), this.importCollector.getShortName(blockData).concat(".class"));
            }
            return value.argument(Integer.toString(-1)); // id not needed for non legacy material
        }
    }

    /* todo test is broken
    public static class IsTransparent extends SwitchCaseRewriter {

        public IsTransparent() {
            super(false);
        }

        @Override
        protected Iterable<String> getCases() {
            return BuiltInRegistries.BLOCK.holders().filter(reference -> reference.value().defaultBlockState().useShapeForLightOcclusion())
            .map(reference -> reference.key().location().getPath().toUpperCase(Locale.ENGLISH)).sorted(Formatting.ALPHABETIC_KEY_ORDER)::iterator;
        }
    }*/

    // items

    public static class Items extends EnumRegistryRewriter<Item> {

        public Items() {
            super(Registries.ITEM, false);
        }

        @Override
        protected Iterable<Holder.Reference<Item>> getValues() {
            return BuiltInRegistries.ITEM.listElements().filter(reference -> BuiltInRegistries.BLOCK.getOptional(reference.key().location()).isEmpty() || reference.value().equals(net.minecraft.world.item.Items.AIR))
                .sorted(Formatting.alphabeticKeyOrder(reference -> reference.key().location().getPath()))::iterator;
        }

        @Override
        protected EnumValue.Builder rewriteEnumValue(Holder.Reference<Item> reference) {
            EnumValue.Builder value = super.rewriteEnumValue(reference);
            Item item = reference.value();
            int maxStackSize = item.getDefaultMaxStackSize();
            int maxDamage = item.components().getOrDefault(DataComponents.MAX_DAMAGE, 0);

            if (maxStackSize != Item.DEFAULT_MAX_STACK_SIZE) {
                if (maxDamage != 0) {
                    value.arguments(asCode(-1, maxStackSize, maxDamage));
                } else {
                    value.arguments(asCode(-1, maxStackSize));
                }
                return value;
            }

            return value.argument(Integer.toString(-1)); // id not needed for non legacy material
        }
    }

    public static class GetEquipmentSlot extends SwitchRewriter {

        @Override
        protected SwitchContent getContent() {
            SortedMap<net.minecraft.world.entity.EquipmentSlot, SwitchCases.SwitchCasesChain> cases = new TreeMap<>(
                Comparator.comparing(Enum::ordinal, (i1, i2) -> Integer.compare(i2, i1)) // reversed (BODY -> HAND)
            );

            net.minecraft.world.entity.EquipmentSlot defaultValue = net.minecraft.world.entity.EquipmentSlot.MAINHAND;
            CodeBlock defaultBlock = CodeBlock.of("return " + asCode(EquipmentSlot.HAND) + ";");

            BuiltInRegistries.ITEM.listElements().forEach(reference -> {
                Equippable equippable = reference.value().components().get(DataComponents.EQUIPPABLE);
                net.minecraft.world.entity.EquipmentSlot slot = equippable == null ? defaultValue : equippable.slot();
                if (slot != defaultValue) {
                    cases.computeIfAbsent(slot, key -> {
                        return SwitchCases.chain()
                            .sortValues(Formatting.ALPHABETIC_KEY_ORDER)
                            .enclosingContent(CodeBlock.of("return " + asCode(EquipmentSlot.values()[slot.ordinal()]) + ";"));
                    }).add(reference.key().location().getPath().toUpperCase(Locale.ENGLISH));
                }
            });

            return SwitchContent.of(cases.values().stream().map(SwitchCases.SwitchCasesChain::build).toList()).withDefault(defaultBlock);
        }
    }
}
