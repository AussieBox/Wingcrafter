package org.aussiebox.wingcrafter.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.input.Scroller;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import org.aussiebox.wingcrafter.Wingcrafter;
import org.aussiebox.wingcrafter.client.WingcrafterClient;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    // what do you mean i have to mixin to the user's MOUSE???

    @Shadow @Final private MinecraftClient client;

    @Shadow @Final private Scroller scroller;

    @Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;setSelectedSlot(I)V"), cancellable = true)
    private void wingcrafter$mouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        PlayerEntity player = this.client.player;
        if (player == null) return;

        ItemStack stack = null;
        if (player.getOffHandStack().isIn(TagKey.of(RegistryKeys.ITEM, Wingcrafter.id("spellcasters")))) {
            stack = player.getOffHandStack();
        } else if (player.getMainHandStack().isIn(TagKey.of(RegistryKeys.ITEM, Wingcrafter.id("spellcasters")))) {
            stack = player.getMainHandStack();
        }
        if (stack == null) return;
        if (!WingcrafterClient.mouseScrollModifierKeybind.isPressed()) return;

        int spellSlot = stack.getOrDefault(ModDataComponentTypes.SPELLCASTER_SELECTED_SLOT, 0);
        int slotMax = stack.getOrDefault(ModDataComponentTypes.SPELLCASTER_SLOT_MAXIMUM, 3);
        if (stack.contains(ModDataComponentTypes.SPELLCASTER_SPELLS))
            slotMax = WingcrafterClient.removeNones(stack.getOrDefault(ModDataComponentTypes.SPELLCASTER_SPELLS, new ArrayList<>())).size();

        Wingcrafter.LOGGER.info("{} {}", slotMax, stack.getOrDefault(ModDataComponentTypes.SPELLCASTER_SPELLS, new ArrayList<>()));

        boolean discrete = this.client.options.getDiscreteMouseScroll().getValue();
        double sensitivity = this.client.options.getMouseWheelSensitivity().getValue();
        double hScroll = (discrete ? Math.signum(horizontal) : horizontal) * sensitivity;
        double vScroll = (discrete ? Math.signum(vertical) : vertical) * sensitivity;
        Vector2i vector2i = this.scroller.update(hScroll, vScroll);

        spellSlot -= vector2i.y == 0 ? -vector2i.x : vector2i.y;
        if (spellSlot > slotMax) spellSlot = 0;
        if (spellSlot < 0) spellSlot = slotMax;
        stack.set(ModDataComponentTypes.SPELLCASTER_SELECTED_SLOT, Math.clamp(spellSlot, 0, slotMax));

        ci.cancel();
    }
}
