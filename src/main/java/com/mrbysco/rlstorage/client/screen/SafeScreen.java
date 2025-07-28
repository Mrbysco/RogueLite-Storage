package com.mrbysco.rlstorage.client.screen;

import com.mrbysco.rlstorage.menu.SafeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SafeScreen extends AbstractContainerScreen<SafeMenu> implements MenuAccess<SafeMenu> {

	private static final ResourceLocation CONTAINER_BACKGROUND = ResourceLocation.withDefaultNamespace("textures/gui/container/generic_54.png");

	public SafeScreen(SafeMenu container, Inventory playerInventory, Component title) {
		super(container, playerInventory, title);
		this.imageHeight = 132;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	@Override
	public void render(GuiGraphics matrixStack, int mouseX, int mouseY, float partialTicks) {
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float p_282334_, int p_282603_, int p_282158_) {
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		graphics.blit(RenderType::guiTextured, CONTAINER_BACKGROUND, i, j, 0.0F, 0.0F, this.imageWidth, 35, 256, 256);
		graphics.blit(RenderType::guiTextured, CONTAINER_BACKGROUND, i, j + 35, 0.0F, 126.0F, this.imageWidth, 96, 256, 256);
	}

	@Override
	protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
		super.renderLabels(graphics, mouseX, mouseY);
	}

}
