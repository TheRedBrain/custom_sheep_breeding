package com.github.theredbrain.customsheepbreeding.config;

import com.github.theredbrain.customsheepbreeding.CustomSheepBreeding;
import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.util.Walkable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedMap;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedAny;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.util.DyeColor;

import java.util.HashMap;

@ConvertFrom(fileName = "server.json5", folder = "customsheepbreeding")
public class ServerConfig extends Config {
    public ServerConfig() {
        super(CustomSheepBreeding.identifier("server"));
    }
    @Comment("""
            When set to true, the color of sheep can't be changed permanently with dye items.
            When a dye item is used, the sheep's color is changed until it is sheared.
            The natural color is used when determining the child color.
            
            For vanilla behaviour change this to false
            """)
    public ValidatedBoolean enable_natural_colors = new ValidatedBoolean(true);
    public ValidatedBoolean enable_custom_colors = new ValidatedBoolean(true);
    @Comment("""
            Initial spawn colors and their weight
            
            (in the default configuration this translates to:
            a 81.836% chance for white,
            a 5% chance for black,
            a 5% chance for gray,
            a 5% chance for light_gray,
            a 3% chance for brown,
            a 0.164% chance for pink)
            
            The default chances are equal to the vanilla behaviour
            """)
    public ValidatedMap<DyeColor, Integer> initial_colors = new ValidatedMap<>(new HashMap<>() {{
        put(DyeColor.WHITE, 40918);
        put(DyeColor.BLACK, 2500);
        put(DyeColor.GRAY, 2500);
        put(DyeColor.LIGHT_GRAY, 2500);
        put(DyeColor.BROWN, 1500);
        put(DyeColor.PINK, 82);
    }}, new ValidatedEnum<>(DyeColor.WHITE, ValidatedEnum.WidgetType.CYCLING), new ValidatedInt(0));
    @Comment("""
            Weight for the child to get the color of the first parent
            
            (in the default configuration this translates to a 38% chance)
            
            For vanilla behaviour change this to 0
            """)
    public ValidatedInt parent_color_1_weight = new ValidatedInt(114);
    @Comment("""
            Weight for the child to get the color of the second parent
            
            (in the default configuration this translates to a 38% chance)
            
            For vanilla behaviour change this to 0
            """)
    public ValidatedInt parent_color_2_weight = new ValidatedInt(114);
    @Comment("""
            Special mutation colors and their weight
            
            (in the default configuration this translates to:
            a 3% chance for black,
            a 0.33% chance for light_blue,
            a 0.33% chance for lime,
            a 0.33% chance for pink)
            
            For vanilla behaviour change this to []
            """)
    public ValidatedMap<DyeColor, Integer> mutation_colors = new ValidatedMap<>(new HashMap<>() {{
        put(DyeColor.BLACK, 9);
        put(DyeColor.LIGHT_BLUE, 1);
        put(DyeColor.LIME, 1);
        put(DyeColor.PINK, 1);
    }}, new ValidatedEnum<>(DyeColor.WHITE, ValidatedEnum.WidgetType.CYCLING), new ValidatedInt(0));
    @Comment("""
            Weight for the child's color to be a blend of the colors of both parents
            (in the default configuration this translates to a 20% chance)
            
            By default the blend has an equal chance to be one of the parents colors
            Exceptions can be defined below
            
            For vanilla behaviour change this to 1
            """)
    public ValidatedInt blending_color_weight = new ValidatedInt(60);
    @Comment("""
            Blending exceptions are defined here
            
            Note that by default both permutations for the color pair of black and white are defined
            This means when breeding a black and white sheep there is an equal chance for the child to become gray or light_gray
            
            For vanilla behaviour change this to
            ["black+white:gray","blue+green:cyan","blue+red:purple","blue+white:light_blue","gray+white:light_gray","green+white:lime","pink+purple:magenta","red+white:pink","red+yellow:orange"]
            """)
    public ValidatedMap<DyeColorPair, DyeColor> color_blending_exceptions = new ValidatedMap<>(new HashMap<>() {{
        put(new DyeColorPair(DyeColor.BLACK, DyeColor.WHITE), DyeColor.GRAY);
        put(new DyeColorPair(DyeColor.WHITE, DyeColor.BLACK), DyeColor.LIGHT_GRAY);
        put(new DyeColorPair(DyeColor.BLACK, DyeColor.LIGHT_GRAY), DyeColor.GRAY);
        put(new DyeColorPair(DyeColor.GRAY, DyeColor.WHITE), DyeColor.LIGHT_GRAY);
        put(new DyeColorPair(DyeColor.BLACK, DyeColor.LIGHT_BLUE), DyeColor.BLUE);
        put(new DyeColorPair(DyeColor.BLACK, DyeColor.LIME), DyeColor.GREEN);
        put(new DyeColorPair(DyeColor.BLACK, DyeColor.PINK), DyeColor.RED);
        put(new DyeColorPair(DyeColor.BLUE, DyeColor.PINK), DyeColor.MAGENTA);
        put(new DyeColorPair(DyeColor.RED, DyeColor.LIGHT_BLUE), DyeColor.MAGENTA);
        put(new DyeColorPair(DyeColor.BLACK, DyeColor.MAGENTA), DyeColor.PURPLE);
        put(new DyeColorPair(DyeColor.RED, DyeColor.BLUE), DyeColor.PURPLE);
        put(new DyeColorPair(DyeColor.BLUE, DyeColor.GREEN), DyeColor.CYAN);
        put(new DyeColorPair(DyeColor.RED, DyeColor.GREEN), DyeColor.YELLOW);
        put(new DyeColorPair(DyeColor.CYAN, DyeColor.RED), DyeColor.BROWN);
        put(new DyeColorPair(DyeColor.YELLOW, DyeColor.RED), DyeColor.ORANGE);
    }}, new ValidatedAny<>(new DyeColorPair()), new ValidatedEnum<>(DyeColor.WHITE, ValidatedEnum.WidgetType.CYCLING));

    @Translation(prefix = "customsheepbreeding.server.dye_color_pair")
    public static class DyeColorPair implements Walkable {

        public DyeColorPair() {
            new DyeColorPair(DyeColor.WHITE, DyeColor.WHITE);
        }

        public DyeColorPair(DyeColor color_1, DyeColor color_2) {
            this.color_1 = color_1;
            this.color_2 = color_2;
        }

        public DyeColor color_1;
        public DyeColor color_2;

        public String toString() {
            return "color_1: " + this.color_1 + ", color_2: " + this.color_2;
        }


    }
}
