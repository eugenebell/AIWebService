package com.eugene.aiwebtester.ai.model;

import java.util.Arrays;
import java.util.List;

public enum StabilityImageStyle {

    CINEMATIC("cinematic"),
    MODEL_3D("3d-model"),
    ANALOG("analog-film"),
    ANIME("anime"),
    COMIC("comic-book"),
    DIGITAL("digital-art"),
    ENHANCE("enhance"),
    FANTASY("fantasy-art"),
    ISO("isometric"),
    LINE_ART("line-art"),
    LOW_POLY("low-poly"),
    MODELING("modeling-compound"),
    NEON_PUNK("neon-punk"),
    ORIGAMI("origami"),
    PHOTO("photographic"),
    PIXEL("pixel-art"),
    TILE_TEXTURE("tile-texture");

    public final String style;

    private StabilityImageStyle(String style) {
        this.style = style;
    }

    public static <T extends Enum<T>> List<T> getEnumList(Class<T> enumClass) {
        return Arrays.asList(enumClass.getEnumConstants());
    }
}
