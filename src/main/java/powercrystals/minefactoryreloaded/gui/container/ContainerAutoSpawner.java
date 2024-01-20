package powercrystals.minefactoryreloaded.gui.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;

import powercrystals.minefactoryreloaded.gui.slot.SlotAcceptReusableSafariNet;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityAutoSpawner;

public class ContainerAutoSpawner extends ContainerFactoryPowered
{
	public ContainerAutoSpawner(TileEntityAutoSpawner te, InventoryPlayer inv)
	{
		super(te, inv);
	}

	@Override
	protected void addSlots()
	{
		addSlotToContainer(new SlotAcceptReusableSafariNet(_te, 0, 8, 24));
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for(int i = 0; i < crafters.size(); i++)
		{
			((ICrafting)crafters.get(i)).sendProgressBarUpdate(this, 100, ((TileEntityAutoSpawner)_te).getSpawnExact() ? 1 : 0);
			((ICrafting)crafters.get(i)).sendProgressBarUpdate(this, 101, ((TileEntityFactoryPowered)_te).getWorkMax() & 65535);
			((ICrafting)crafters.get(i)).sendProgressBarUpdate(this, 102, ((TileEntityFactoryPowered)_te).getWorkMax() >>> 16);
		}
	}

	@Override
	public void updateProgressBar(int var, int value)
	{
		super.updateProgressBar(var, value);
		if(var == 100) ((TileEntityAutoSpawner)_te).setSpawnExact(value == 0 ? false : true);
		else if(var == 101) workTemp = (value & 65535);
		else if(var == 102) ((TileEntityAutoSpawner)_te).setWorkMax(((value & 65535) << 16) | workTemp);
	}
}
