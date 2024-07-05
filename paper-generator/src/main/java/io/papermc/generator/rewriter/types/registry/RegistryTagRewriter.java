package io.papermc.generator.rewriter.types.registry;

import com.mojang.logging.LogUtils;
import io.papermc.generator.Main;
import io.papermc.generator.rewriter.utils.Annotations;
import io.papermc.generator.utils.Formatting;
import io.papermc.generator.utils.experimental.SingleFlagHolder;
import io.papermc.typewriter.ClassNamed;
import io.papermc.typewriter.SourceFile;
import io.papermc.typewriter.replace.SearchMetadata;
import io.papermc.typewriter.replace.SearchReplaceRewriter;
import java.util.Iterator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.bukkit.Keyed;
import org.bukkit.Tag;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;

import static io.papermc.generator.utils.Formatting.quoted;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

@NullMarked
public class RegistryTagRewriter<T> extends SearchReplaceRewriter {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Registry<T> registry;
    private final Class<? extends Keyed> apiClass;
    private final String fetchMethod = "getTag";

    public RegistryTagRewriter(ResourceKey<? extends Registry<T>> registryKey, Class<? extends Keyed> apiClass) {
        this.registry = Main.REGISTRY_ACCESS.lookupOrThrow(registryKey);
        this.apiClass = apiClass;
    }

    @Override
    public boolean registerFor(SourceFile file) {
        ClassNamed holderClass = this.options.targetClass().orElse(file.mainClass());
        if (holderClass.knownClass() != null) {
            try {
                holderClass.knownClass().getDeclaredMethod(this.fetchMethod, String.class);
            } catch (NoSuchMethodException e) {
                LOGGER.error("Fetch method not found, skipping the rewriter for registry tag fields of {}", this.registry.key(), e);
                return false;
            }
        }

        return super.registerFor(file);
    }

    @Override
    protected void insert(SearchMetadata metadata, StringBuilder builder) {
        Iterator<? extends TagKey<T>> keyIterator = this.registry.listTagIds().sorted(Formatting.alphabeticKeyOrder(reference -> reference.location().getPath())).iterator();

        while (keyIterator.hasNext()) {
            TagKey<T> tagKey = keyIterator.next();

            String featureFlagName = Main.EXPERIMENTAL_TAGS.get(tagKey);
            if (featureFlagName != null) {
                Annotations.experimentalAnnotations(builder, metadata.indent(), this.importCollector, SingleFlagHolder.fromVanillaName(featureFlagName));
            }

            builder.append(metadata.indent());
            builder.append("%s %s %s ".formatted(PUBLIC, STATIC, FINAL));

            builder.append("%s<%s>".formatted(Tag.class.getSimpleName(), this.apiClass.getSimpleName())).append(' ').append(this.rewriteFieldName(tagKey));
            builder.append(" = ");
            builder.append(this.rewriteFieldValue(tagKey));
            builder.append(';');

            builder.append('\n');
            if (keyIterator.hasNext()) {
                builder.append('\n');
            }
        }
    }

    protected String rewriteFieldName(TagKey<T> tagKey) {
        return Formatting.formatKeyAsField(tagKey.location().getPath());
    }

    protected String rewriteFieldValue(TagKey<T> tagKey) {
        return "%s(%s)".formatted(this.fetchMethod, quoted(tagKey.location().getPath()));
    }
}
