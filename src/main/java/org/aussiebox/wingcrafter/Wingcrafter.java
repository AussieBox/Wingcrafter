package org.aussiebox.wingcrafter;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.aussiebox.wingcrafter.block.ModBlockEntities;
import org.aussiebox.wingcrafter.block.ModBlocks;
import org.aussiebox.wingcrafter.block.blockentities.ScrollBlockEntity;
import org.aussiebox.wingcrafter.component.ModDataComponentTypes;
import org.aussiebox.wingcrafter.component.SoulScrollSpells;
import org.aussiebox.wingcrafter.effect.ModEffects;
import org.aussiebox.wingcrafter.init.ScreenHandlerTypeInit;
import org.aussiebox.wingcrafter.item.ModItems;
import org.aussiebox.wingcrafter.network.CastSpellPayload;
import org.aussiebox.wingcrafter.network.ScrollTextPayload;
import org.aussiebox.wingcrafter.network.SoulKillPayload;
import org.aussiebox.wingcrafter.network.UpdateSoulScrollDataPayload;
import org.aussiebox.wingcrafter.spells.Spells;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static org.aussiebox.wingcrafter.block.custom.ScrollBlock.TITLED;
import static org.aussiebox.wingcrafter.block.custom.ScrollBlock.WRITTEN;

public class Wingcrafter implements ModInitializer {
    public static final String MOD_ID = "wingcrafter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    // "i don't know bro stop harassing me!" -my friend, replying to "give me a comment to put in my code"

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {

        PayloadTypeRegistry.playC2S().register(ScrollTextPayload.ID, ScrollTextPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ScrollTextPayload.ID, (payload, context) -> {
            ScrollBlockEntity blockEntity = (ScrollBlockEntity) context.player().getEntityWorld().getBlockEntity(payload.pos());
            if (blockEntity instanceof ScrollBlockEntity scrollBlockEntity) {
                scrollBlockEntity.setTitle(payload.titleText());
                scrollBlockEntity.setText(payload.text());
                if (!Objects.equals(payload.text(), "")) {
                    Objects.requireNonNull(blockEntity.getWorld()).setBlockState(blockEntity.getPos(), blockEntity.getWorld().getBlockState(blockEntity.getPos()).with(WRITTEN, true));
                } else {
                    Objects.requireNonNull(blockEntity.getWorld()).setBlockState(blockEntity.getPos(), blockEntity.getWorld().getBlockState(blockEntity.getPos()).with(WRITTEN, false));
                }
                if (!Objects.equals(payload.titleText(), "")) {
                    Objects.requireNonNull(blockEntity.getWorld()).setBlockState(blockEntity.getPos(), blockEntity.getWorld().getBlockState(blockEntity.getPos()).with(TITLED, true));
                } else {
                    Objects.requireNonNull(blockEntity.getWorld()).setBlockState(blockEntity.getPos(), blockEntity.getWorld().getBlockState(blockEntity.getPos()).with(TITLED, false));
                }

                Objects.requireNonNull(scrollBlockEntity.getWorld()).updateListeners(payload.pos(), scrollBlockEntity.getWorld().getBlockState(payload.pos()), scrollBlockEntity.getWorld().getBlockState(payload.pos()), Block.NOTIFY_LISTENERS);
            }
        });

        PayloadTypeRegistry.playC2S().register(SoulKillPayload.ID, SoulKillPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SoulKillPayload.ID, (payload, context) -> {
            PlayerEntity player = context.player();
            World world = player.getEntityWorld();
            RegistryKey<DamageType> SOUL_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(Wingcrafter.MOD_ID, "soul"));
            DamageSource damageSource = new DamageSource(
                    world.getRegistryManager()
                            .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                            .getEntry(SOUL_DAMAGE.getValue()).get()
            );
            player.damage((ServerWorld) world, damageSource, 1337);
        });

        PayloadTypeRegistry.playC2S().register(UpdateSoulScrollDataPayload.ID, UpdateSoulScrollDataPayload.PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(UpdateSoulScrollDataPayload.ID, (payload, context) -> {
            PlayerEntity player = context.player();
            ItemStack itemStack = payload.itemStack();
            int slot = player.getInventory().getSlotWithStack(payload.itemStack());

            itemStack.set(ModDataComponentTypes.SOUL_SCROLL_SPELLS, new SoulScrollSpells(payload.spell1(), payload.spell2(), payload.spell3()));
            player.getInventory().setStack(slot, itemStack);
            player.getInventory().markDirty();
        });

        PayloadTypeRegistry.playC2S().register(CastSpellPayload.ID, CastSpellPayload.PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(CastSpellPayload.ID, (payload, context) -> {
            Spells.cast(payload.spellID(), context.player());
        });

        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (source.isBuiltin() && LootTables.ANCIENT_CITY_CHEST.equals(key)) {
                LootPool.Builder poolBuilder = LootPool.builder().with(ItemEntry.builder(ModItems.SOUL_SCROLL))
                        .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 1.0F)))
                        .conditionally(RandomChanceLootCondition.builder(0.025f));
                tableBuilder.pool(poolBuilder);
            }
        });

        ScreenHandlerTypeInit.init();
        ModItems.registerModItems();
        ModBlockEntities.registerModBlockEntities();
        ModDataComponentTypes.registerDataComponentTypes();
        ModBlocks.registerModBlocks();
        ModEffects.registerStatusEffects();
    }
}
