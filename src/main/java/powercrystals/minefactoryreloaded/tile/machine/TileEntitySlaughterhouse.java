package powercrystals.minefactoryreloaded.tile.machine;

import cofh.core.util.fluid.FluidTankAdv;

import java.util.List;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.core.GrindingDamage;
import powercrystals.minefactoryreloaded.setup.Machine;

public class TileEntitySlaughterhouse extends TileEntityGrinder
{
	public TileEntitySlaughterhouse()
	{
		super(Machine.Slaughterhouse);
		_damageSource = new GrindingDamage("mfr.slaughterhouse", 2);
		setManageSolids(false);
		_tanks[0].setLock(FluidRegistry.getFluid("meat"));
		_tanks[1].setLock(FluidRegistry.getFluid("pinkslime"));
	}

	@Override
	public void setWorldObj(World world)
	{
		super.setWorldObj(world);
		if (_grindingWorld != null)
			this._grindingWorld.setAllowSpawns(true);
	}

	@Override
	protected FluidTankAdv[] createTanks()
	{
		return new FluidTankAdv[]{new FluidTankAdv(4 * BUCKET_VOLUME),
				new FluidTankAdv(2 * BUCKET_VOLUME)};
	}

	@Override
	public boolean activateMachine()
	{
		_grindingWorld.cleanReferences();
		List<?> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, _areaManager.getHarvestArea().toAxisAlignedBB());

		entityList: for(Object o : entities)
		{
			EntityLivingBase e = (EntityLivingBase)o;
			for(Class<?> t : MFRRegistry.getSlaughterhouseBlacklist())
			{
				if(t.isInstance(e))
				{
					continue entityList;
				}
			}
			if((e instanceof EntityAgeable && ((EntityAgeable)e).getGrowingAge() < 0) || e.isEntityInvulnerable() ||
					e.getHealth() <= 0 || !_grindingWorld.addEntityForGrinding(e))
			{
				continue;
			}
			float massFound = (float)Math.pow(e.boundingBox.getAverageEdgeLength(), 2);
			damageEntity(e);
			if(e.getHealth() <= 0)
			{
				if (_rand.nextInt(8) != 0)
					fillTank(_tanks[0], "meat", massFound);
				else
					fillTank(_tanks[1], "pinkslime", massFound);
				setIdleTicks(10);
			}
			else
			{
				setIdleTicks(5);
			}
			return true;
		}
		setIdleTicks(getIdleTicksMax());
		return false;
	}

	@Override
	public void acceptXPOrb(EntityXPOrb orb)
	{
	}

	@Override
	protected void damageEntity(EntityLivingBase entity)
	{
		setRecentlyHit(entity, 0);
		entity.attackEntityFrom(_damageSource, DAMAGE);
	}
}
