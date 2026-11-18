package com.github.darksoulq.blueprint.core.codec;

public record GlyphDef(String type, String file, int height, int ascent, boolean allowInChat, String chatPermission, boolean showInDictionary) {
}