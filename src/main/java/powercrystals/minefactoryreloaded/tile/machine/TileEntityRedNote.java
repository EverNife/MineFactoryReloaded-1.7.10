package powercrystals.minefactoryreloaded.tile.machine;

import net.minecraftforge.common.util.ForgeDirection;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactory;

public class TileEntityRedNote extends TileEntityFactory
{
	private static final String[] _noteNames = new String[] { "harp", "bd", "snare", "hat", "bassattack" };
	
	public TileEntityRedNote()
	{
		super(Machine.RedNote);
	}
	
	@Override
	public boolean canUpdate()
	{
		return false;
	}
	
	@Override
	public void onRedNetChanged(ForgeDirection side, int value)
	{
		if (value <= 0 || value > 120)
		{
			return;
		}

		value--;
		int instrument = value / 25;
		int note = value % 25;
		
		float f = (float)Math.pow(2.0D, (note - 12) / 12.0D);
		
		worldObj.playSoundEffect(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, "note." + _noteNames[instrument], 3.0F, f);
	}
}
