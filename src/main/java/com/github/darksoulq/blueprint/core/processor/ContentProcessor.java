package com.github.darksoulq.blueprint.core.processor;

import com.github.darksoulq.blueprint.core.ResourcesHolder;
import java.io.File;

public interface ContentProcessor {
    default void preProcess(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {}

    default void process(String namespace, File parentDir, File dataFile, ResourcesHolder holder) {}
}