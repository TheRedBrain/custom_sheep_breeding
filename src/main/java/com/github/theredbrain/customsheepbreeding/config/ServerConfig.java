package com.github.theredbrain.customsheepbreeding.config;

import com.github.theredbrain.customsheepbreeding.CustomSheepBreeding;
import me.fzzyhmstrs.fzzy_config.annotations.Comment;
import me.fzzyhmstrs.fzzy_config.annotations.ConvertFrom;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedBoolean;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.util.DyeColor;

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
    public String[] initial_colors = {
            "white:40918",
            "black:2500",
            "gray:2500",
            "light_gray:2500",
            "brown:1500",
            "pink:82"
    };
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
    public String[] mutation_colors = {
            "black:9",
            "light_blue:1",
            "lime:1",
            "pink:1"
    };
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
    public String[] color_blending_exceptions = {
            "black+white:gray",
            "white+black:light_gray",
            "black+light_gray:gray",
            "gray+white:light_gray",
            "black+light_blue:blue",
            "black+lime:green",
            "black+pink:red",
            "blue+pink:magenta",
            "red+light_blue:magenta",
            "black+magenta:purple",
            "red+blue:purple",
            "blue+green:cyan",
            "red+green:yellow",
            "cyan+red:brown",
            "yellow+red:orange"
    };
}
