package powercrystals.minefactoryreloaded.gui.container;

import cofh.lib.gui.slot.SlotAcceptInsertable;
import cofh.lib.gui.slot.SlotRemoveOnly;
import cofh.lib.gui.slot.SlotViewOnly;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;

import powercrystals.minefactoryreloaded.tile.machine.TileEntityAutoAnvil;

public class ContainerAutoAnvil extends ContainerFactoryPowered
{
	private TileEntityAutoAnvil _anvil;
	private boolean repairOnly;

	public ContainerAutoAnvil(TileEntityAutoAnvil anvil, InventoryPlayer inv)
	{
		super(anvil, inv);
		_anvil = anvil;
		repairOnly = !anvil.getRepairOnly();
	}

	@Override
	protected void addSlots()
	{
		addSlotToContainer(new SlotAcceptInsertable(_te, 0, 8, 24));
		addSlotToContainer(new SlotAcceptInsertable(_te, 1, 26, 24));
		addSlotToContainer(new SlotRemoveOnly(_te, 2, 8, 48));
		addSlotToContainer(new SlotViewOnly(_te, 2, 45, 24, true) {
			@Override
			public ItemStack getStack() {
				return _anvil.getRepairOutput();
			}
			@Override
			public void onSlotChanged() {}
		});

		getSlot(1).setBackgroundIcon(ContainerAutoDisenchanter.background);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		if (_anvil.getRepairOnly() != repairOnly)
		{
			repairOnly = _anvil.getRepairOnly();
			int data = (repairOnly ? 1 : 0);
			for(int i = 0; i < crafters.size(); i++)
			{
				((ICrafting)crafters.get(i)).sendProgressBarUpdate(this, 100, data);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int var, int value)
	{
		super.updateProgressBar(var, value);

		if (var == 100)
		{
			_anvil.setRepairOnly((value & 1) == 1);
		}
	}
}
