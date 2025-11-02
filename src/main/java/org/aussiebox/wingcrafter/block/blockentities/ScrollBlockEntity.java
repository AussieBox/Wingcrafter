package org.aussiebox.wingcrafter.block.blockentities;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import org.aussiebox.wingcrafter.block.ModBlockEntities;
import org.aussiebox.wingcrafter.network.BlockPosPayload;
import org.aussiebox.wingcrafter.screenhandler.ScrollBlockScreenHandler;
import org.jetbrains.annotations.Nullable;

public class ScrollBlockEntity extends BlockEntity implements ExtendedScreenHandlerFactory<BlockPosPayload> {
    public static final Text TITLE = Text.empty();

    public ScrollBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SCROLL_BLOCK_ENTITY, pos, state);
    }

    private String text = "";
    private String title = "";
    public String getTitle() {
        if (title == null) {
            title = "";
        }
        return title;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
        if (title == null) {
            title = "";
        }
        markDirty();
    }

    public String getText() {
        if (text == null) {
            text = "";
        }
        return text;
    }

    public void setText(String newText) {
        text = newText;
        if (text == null) {
            text = "";
        }
        markDirty();
    }

    @Override
    protected void writeData(WriteView writeView) {
        writeView.putString("text", text);
        writeView.putString("title", title);
        super.writeData(writeView);
    }

    @Override
    protected void readData(ReadView readView) {
        super.readData(readView);
        text = readView.getString("text", "Data Read Failed.");
        title = readView.getString("title", "Data Read Failed.");
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public BlockPosPayload getScreenOpeningData(ServerPlayerEntity serverPlayerEntity) {
        text = getText();
        title = getTitle();
        return new BlockPosPayload(this.pos, text, title);
    }

    @Override
    public Text getDisplayName() {
        return TITLE;
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new ScrollBlockScreenHandler(syncId, playerInventory, this);
    }
}