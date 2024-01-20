package powercrystals.minefactoryreloaded.gui.client;

import static cofh.core.CoFHProps.PATH_GUI_STORAGE;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import powercrystals.minefactoryreloaded.gui.container.ContainerBag;

@SideOnly(Side.CLIENT)
public class GUIBag extends GuiContainer
{
    private static final ResourceLocation guiTextures = new ResourceLocation(PATH_GUI_STORAGE + "Storage5.png");
    private ContainerBag bag;

    public GUIBag(ContainerBag container)
    {
        super(container);
        this.bag = container;
        this.allowUserInput = false;
        this.ySize = 148;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
    {
        fontRendererObj.drawString(this.bag.getInventoryName(), 8, 6, 4210752);
        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 3, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    @Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(guiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
