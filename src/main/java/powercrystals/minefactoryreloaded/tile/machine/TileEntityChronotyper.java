package powercrystals.minefactoryreloaded.tile.machine;

import cofh.lib.util.position.BlockPosition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.nbt.NBTTagCompound;

import powercrystals.minefactoryreloaded.gui.client.GuiChronotyper;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.container.ContainerChronotyper;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;

public class TileEntityChronotyper extends TileEntityFactoryPowered {

	private boolean _moveOld;

	public TileEntityChronotyper() {

		super(Machine.Chronotyper);
		createEntityHAM(this);
		setCanRotate(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer) {

		return new GuiChronotyper(getContainer(inventoryPlayer), this);
	}

	@Override
	public ContainerChronotyper getContainer(InventoryPlayer inventoryPlayer) {

		return new ContainerChronotyper(this, inventoryPlayer);
	}

	@Override
	public int getSizeInventory() {

		return 0;
	}

	@Override
	protected boolean activateMachine() {

		List<?> entities = worldObj.getEntitiesWithinAABB(EntityAgeable.class, _areaManager.getHarvestArea().toAxisAlignedBB());

		for (Object o : entities) {
			if (!(o instanceof EntityAgeable)) {
				continue;
			}
			EntityAgeable a = (EntityAgeable) o;
			if ((a.getGrowingAge() < 0 && !_moveOld) || (a.getGrowingAge() >= 0 && _moveOld)) {
				BlockPosition bp = BlockPosition.fromRotateableTile(this);
				bp.moveBackwards(1);
				a.setPosition(bp.x + 0.5, bp.y + 0.5, bp.z + 0.5);

				return true;
			}
		}
		setIdleTicks(getIdleTicksMax());
		return false;
	}

	public boolean getMoveOld() {

		return _moveOld;
	}

	public void setMoveOld(boolean moveOld) {

		_moveOld = moveOld;
	}

	@Override
	public int getWorkMax() {

		return 1;
	}

	@Override
	public int getIdleTicksMax() {

		return 200;
	}

	@Override
	public void writePortableData(EntityPlayer player, NBTTagCompound tag) {

		tag.setBoolean("moveOld", _moveOld);
	}

	@Override
	public void readPortableData(EntityPlayer player, NBTTagCompound tag) {

		_moveOld = tag.getBoolean("moveOld");
	}

	@Override
	public void writeItemNBT(NBTTagCompound tag) {

		super.writeItemNBT(tag);

		if (_moveOld)
			tag.setBoolean("moveOld", _moveOld);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {

		super.readFromNBT(tag);
		_moveOld = tag.getBoolean("moveOld");
	}

}
