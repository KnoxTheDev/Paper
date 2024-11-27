package io.papermc.generator.rewriter.types.simple.trial;

import java.util.List;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

class ConstantInfo {

    private @MonotonicNonNull String constantName;
    private @MonotonicNonNull List<String> javadocs;

    public void constantName(String name) {
        this.constantName = name;
    }

    public void javadocs(List<String> javadocs) {
        this.javadocs = javadocs;
    }

    public String constantName() {
        return this.constantName;
    }

    public List<String> javadocs() {
        return this.javadocs;
    }

    public boolean isComplete() {
        return this.constantName != null && this.javadocs != null;
    }
}
