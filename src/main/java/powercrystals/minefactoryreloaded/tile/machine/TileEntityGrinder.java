package powercrystals.minefactoryreloaded.tile.machine;

import cofh.core.util.fluid.FluidTankAdv;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import powercrystals.minefactoryreloaded.MFRRegistry;
import powercrystals.minefactoryreloaded.api.IFactoryGrindable;
import powercrystals.minefactoryreloaded.api.MobDrop;
import powercrystals.minefactoryreloaded.core.GrindingDamage;
import powercrystals.minefactoryreloaded.core.ITankContainerBucketable;
import powercrystals.minefactoryreloaded.core.MFRLiquidMover;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryInventory;
import powercrystals.minefactoryreloaded.gui.client.GuiFactoryPowered;
import powercrystals.minefactoryreloaded.gui.container.ContainerFactoryPowered;
import powercrystals.minefactoryreloaded.setup.Machine;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryPowered;
import powercrystals.minefactoryreloaded.world.GrindingWorldServer;

public class TileEntityGrinder extends TileEntityFactoryPowered implements ITankContainerBucketable
{
	public static final float DAMAGE = 0x1.fffffeP+120f;

	protected Random _rand;
	protected GrindingWorldServer _grindingWorld;
	protected GrindingDamage _damageSource;

	protected TileEntityGrinder(Machine machine)
	{
		super(machine);
		createEntityHAM(this);
		_rand = new Random();
		setManageSolids(true);
		setCanRotate(true);
		_tanks[0].setLock(FluidRegistry.getFluid("mobessence"));
	}

	public TileEntityGrinder()
	{
		this(Machine.Grinder);
		_damageSource = new GrindingDamage();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiFactoryInventory getGui(InventoryPlayer inventoryPlayer)
	{
		return new GuiFactoryPowered(getContainer(inventoryPlayer), this);
	}

	@Override
	public ContainerFactoryPowered getContainer(InventoryPlayer inventoryPlayer)
	{
		return new ContainerFactoryPowered(this, inventoryPlayer);
	}

	@Override
	public void setWorldObj(World world)
	{
		super.setWorldObj(world);
		if(_grindingWorld != null)
		{
			_grindingWorld.clearReferences();
			_grindingWorld.setMachine(null);
		}
		if(this.worldObj instanceof WorldServer)
			_grindingWorld = new GrindingWorldServer((WorldServer)this.worldObj, this);
	}

	@Override
	public void onChunkUnload()
	{
		super.onChunkUnload();
		if(_grindingWorld != null)
		{
			_grindingWorld.clearReferences();
			_grindingWorld.setMachine(null);
		}
		_grindingWorld = null;
	}

	public Random getRandom()
	{
		return _rand;
	}

	@Override
	protected boolean shouldPumpLiquid()
	{
		return true;
	}

	@Override
	public int getWorkMax()
	{
		return 1;
	}

	@Override
	public int getIdleTicksMax()
	{
		return 200;
	}

	@Override
	public boolean activateMachine()
	{
        if (_grindingWorld == null){
            return false;
        }
		_grindingWorld.cleanReferences();
		List<?> entities = worldObj.getEntitiesWithinAABB(EntityLivingBase.class, _areaManager.getHarvestArea().toAxisAlignedBB());

		entityList: for(Object o : entities)
		{
			EntityLivingBase e = (EntityLivingBase)o;
			if(e instanceof EntityAgeable && ((EntityAgeable)e).getGrowingAge() < 0 || e.isEntityInvulnerable() || e.getHealth() <= 0)
			{
				continue;
			}

			processEntity:
			{
				if(MFRRegistry.getGrindables().containsKey(e.getClass()))
				{
					IFactoryGrindable r = MFRRegistry.getGrindables().get(e.getClass());
					List<MobDrop> drops = r.grind(e.worldObj, e, getRandom());
					if(drops != null && drops.size() > 0 && WeightedRandom.getTotalWeight(drops) > 0)
					{
						ItemStack drop = ((MobDrop)WeightedRandom.getRandomItem(_rand, drops)).getStack();
						doDrop(drop);
					}
					if(r.processEntity(e))
					{
						if(e.getHealth() <= 0)
						{
							continue entityList;
						}
						break processEntity;
					}
				}

				for(Class<?> t : MFRRegistry.getGrinderBlacklist())
				{
					if(t.isInstance(e))
					{
						continue entityList;
					}
				}
			}

			if(!_grindingWorld.addEntityForGrinding(e))
			{
				continue entityList;
			}

			damageEntity(e);
			if(e.getHealth() <= 0)
			{
				//fillTank(_tanks[0], "mobessence", 1);
				setIdleTicks(20);
			}
			else
			{
				setIdleTicks(10);
			}
			return true;
		}
		setIdleTicks(getIdleTicksMax());
		return false;
	}

	protected void setRecentlyHit(EntityLivingBase entity, int t)
	{
		entity.recentlyHit = t;
	}

	protected void damageEntity(EntityLivingBase entity)
	{
		setRecentlyHit(entity, 100);
		entity.attackEntityFrom(_damageSource, DAMAGE);
	}

	public void acceptXPOrb(EntityXPOrb orb)
	{
		MFRLiquidMover.fillTankWithXP(_tanks[0], orb);
	}

	@Override
	public int getSizeInventory()
	{
		return 0;
	}

	protected void fillTank(FluidTankAdv tank, String fluid, float amount)
	{
		tank.fill(FluidRegistry.getFluidStack(fluid, (int)(100 * amount)), true);
		markDirty();
	}

	@Override
	protected FluidTankAdv[] createTanks()
	{
		return new FluidTankAdv[]{new FluidTankAdv(4 * BUCKET_VOLUME)};
	}

	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
	{
		return 0;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
	{
		return drain(maxDrain, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
	{
		return drain(resource, doDrain);
	}

	@Override
	public boolean allowBucketDrain(ItemStack stack)
	{
		return true;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid)
	{
		return false;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid)
	{
		return true;
	}
}
