package com.github.theredbrain.customsheepbreeding.mixin.entity.passive;

import com.github.theredbrain.customsheepbreeding.CustomSheepBreeding;
import com.github.theredbrain.customsheepbreeding.config.ServerConfig;
import com.github.theredbrain.customsheepbreeding.entity.passive.DuckSheepEntityMixin;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends AnimalEntity implements DuckSheepEntityMixin {

    @Shadow
    public abstract DyeColor getColor();

    @Shadow
    public abstract void setColor(DyeColor color);

    @Unique
    private static final TrackedData<Byte> NATURAL_COLOR;

    protected SheepEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void customsheepbreeding$initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(NATURAL_COLOR, (byte) 0);
    }

    @Inject(method = "sheared", at = @At("TAIL"))
    public void customsheepbreeding$sheared(SoundCategory shearedSoundCategory, CallbackInfo ci) {
        if (CustomSheepBreeding.SERVER_CONFIG.enable_natural_colors.get()) {
            this.setColor(this.customsheepbreeding$getNaturalColor());
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void customsheepbreeding$writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putByte("natural_color", (byte) this.customsheepbreeding$getNaturalColor().getId());
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void customsheepbreeding$readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("natural_color")) {
            this.customsheepbreeding$setNaturalColor(DyeColor.byId(nbt.getByte("natural_color")));
        } else {
            this.customsheepbreeding$setNaturalColor(DyeColor.byId(nbt.getByte("Color")));
        }
    }

    @Inject(method = "initialize", at = @At("RETURN"))
    public void customsheepbreeding$initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        this.customsheepbreeding$setNaturalColor(this.getColor());
    }

    @WrapOperation(
            method = "initialize",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/SheepEntity;generateDefaultColor(Lnet/minecraft/util/math/random/Random;)Lnet/minecraft/util/DyeColor;")
    )
    private DyeColor customsheepbreeding$wrap_generateDefaultColor(Random random, Operation<DyeColor> original) {
        if (CustomSheepBreeding.SERVER_CONFIG.enable_custom_colors.get()) {
            return customsheepbreeding$generateOverhauledDefaultColor(random);
        } else {
            return original.call(random);
        }
    }

    @Unique
    private static DyeColor customsheepbreeding$generateOverhauledDefaultColor(Random random) {
        String[] initial_colors = CustomSheepBreeding.SERVER_CONFIG.initial_colors;

        int total_weight = 0;
        for (String string : initial_colors) {
            String[] stringArray = string.split(":");
            DyeColor dyeColor = DyeColor.byName(stringArray[0], null);
            if (dyeColor != null) {
                total_weight += Integer.parseInt(stringArray[1]);
            }
        }

        int randomInt = random.nextInt(total_weight);
        int threshold = 0;
        for (String string : initial_colors) {
            String[] stringArray = string.split(":");
            DyeColor dyeColor = DyeColor.byName(stringArray[0], null);
            if (dyeColor != null) {
                threshold += Integer.parseInt(stringArray[1]);
                if (randomInt < threshold) {
                    return dyeColor;
                }
            }
        }

        // fallback
        return DyeColor.WHITE;
    }

    @WrapOperation(
            method = "createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/SheepEntity;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/SheepEntity;getChildColor(Lnet/minecraft/entity/passive/AnimalEntity;Lnet/minecraft/entity/passive/AnimalEntity;)Lnet/minecraft/util/DyeColor;")
    )
    private DyeColor customsheepbreeding$wrap_getChildColor(SheepEntity instance, AnimalEntity firstParent, AnimalEntity secondParent, Operation<DyeColor> original) {
        if (CustomSheepBreeding.SERVER_CONFIG.enable_custom_colors.get()) {
            return customsheepbreeding$getOverhauledChildColor(firstParent, secondParent);
        } else {
            return original.call(instance, firstParent, secondParent);
        }
    }

    @Inject(method = "createChild(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/PassiveEntity;)Lnet/minecraft/entity/passive/SheepEntity;", at = @At("RETURN"), cancellable = true)
    public void customsheepbreeding$createChild(ServerWorld serverWorld, PassiveEntity passiveEntity, CallbackInfoReturnable<SheepEntity> cir) {
        SheepEntity sheepEntity = cir.getReturnValue();
        if (sheepEntity != null) {
            ((DuckSheepEntityMixin) sheepEntity).customsheepbreeding$setNaturalColor(sheepEntity.getColor());
        }

        cir.setReturnValue(sheepEntity);
    }

    @Unique
    private DyeColor customsheepbreeding$getOverhauledChildColor(AnimalEntity firstParent, AnimalEntity secondParent) {
        ServerConfig config = CustomSheepBreeding.SERVER_CONFIG;
        DyeColor parentColor1 = config.enable_natural_colors.get() ? ((DuckSheepEntityMixin) firstParent).customsheepbreeding$getNaturalColor() : ((SheepEntity)firstParent).getColor();
        DyeColor parentColor2 = config.enable_natural_colors.get() ? ((DuckSheepEntityMixin) secondParent).customsheepbreeding$getNaturalColor() : ((SheepEntity)secondParent).getColor();
        DyeColor blendingColor = customsheepbreeding$getBlendingColor(parentColor1, parentColor2, this);

        int parent_color_1_weight = config.parent_color_1_weight.get();
        if (parent_color_1_weight < 0) {
            parent_color_1_weight = 0;
        }
        int parent_color_2_weight = config.parent_color_2_weight.get();
        if (parent_color_2_weight < 0) {
            parent_color_2_weight = 0;
        }
        int blending_color_weight = config.blending_color_weight.get();
        if (blending_color_weight < 1) {
            blending_color_weight = 1;
        }
        String[] mutation_colors = config.mutation_colors;

        int total_weight = parent_color_1_weight + parent_color_2_weight + blending_color_weight;
        for (String string : mutation_colors) {
            String[] stringArray = string.split(":");
            DyeColor dyeColor = DyeColor.byName(stringArray[0], null);
            if (dyeColor != null) {
                total_weight += Integer.parseInt(stringArray[1]);
            }
        }

        int randomInt = this.getWorld().random.nextInt(total_weight);
        int threshold = parent_color_1_weight;
        if (randomInt < threshold) {
            return parentColor1;
        } else {
            threshold += parent_color_2_weight;
            if (randomInt < threshold) {
                return parentColor2;
            } else {
                threshold += blending_color_weight;
                if (randomInt < threshold) {
                    return blendingColor;
                } else {
                    for (String string : mutation_colors) {
                        String[] stringArray = string.split(":");
                        DyeColor dyeColor = DyeColor.byName(stringArray[0], null);
                        if (dyeColor != null) {
                            threshold += Integer.parseInt(stringArray[1]);
                            if (randomInt < threshold) {
                                return dyeColor;
                            }
                        }
                    }
                }
            }
        }
        // fallback
        return DyeColor.WHITE;
    }

    @Unique
    private static DyeColor customsheepbreeding$getBlendingColor(DyeColor parentColor1, DyeColor parentColor2, AnimalEntity entity) {
        String[] color_blending_exceptions = CustomSheepBreeding.SERVER_CONFIG.color_blending_exceptions;

        // enables random blending when two different blending colors are defined for a pair of colors, should have no effect otherwise
        if (entity.getWorld().random.nextBoolean()) {
            DyeColor temp = parentColor1;
            parentColor1 = parentColor2;
            parentColor2 = temp;
        }

        DyeColor dyeColor = null;
        for (String string : color_blending_exceptions) {
            String[] stringArray = string.split(":");
            if (stringArray[0].equals(parentColor1.getName() + "+" + parentColor2.getName()) || stringArray[0].equals(parentColor2.getName() + "+" + parentColor1.getName())) {
                String dyeColorString = stringArray[1];
                dyeColor = DyeColor.byName(dyeColorString, null);
            }
        }
        return dyeColor != null ? dyeColor : entity.getWorld().random.nextBoolean() ? parentColor1 : parentColor2;
    }

    static {
        NATURAL_COLOR = DataTracker.registerData(SheepEntity.class, TrackedDataHandlerRegistry.BYTE);
    }

    @Override
    public DyeColor customsheepbreeding$getNaturalColor() {
        return DyeColor.byId((Byte) this.dataTracker.get(NATURAL_COLOR) & 15);
    }

    @Override
    public void customsheepbreeding$setNaturalColor(DyeColor color) {
        byte b = (Byte) this.dataTracker.get(NATURAL_COLOR);
        this.dataTracker.set(NATURAL_COLOR, (byte) (b & 240 | color.getId() & 15));
    }
}
