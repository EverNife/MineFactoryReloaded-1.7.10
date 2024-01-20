package powercrystals.minefactoryreloaded.farmables.ranchables;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import powercrystals.minefactoryreloaded.api.IFactoryRanchable;
import powercrystals.minefactoryreloaded.api.RanchedItem;

public class RanchableChicken implements IFactoryRanchable {

	protected Random rand = new Random();

	@Override
	public Class<? extends EntityLivingBase> getRanchableEntity() {

		return EntityChicken.class;
	}

	@Override
	public List<RanchedItem> ranch(World world, EntityLivingBase entity, IInventory rancher) {

		List<RanchedItem> drops = new LinkedList<RanchedItem>();
		EntityChicken chicken = ((EntityChicken) entity);
		if (chicken.timeUntilNextEgg < 500) {
			chicken.playSound("mob.chicken.plop", 1.0F, (chicken.getRNG().nextFloat() - chicken.getRNG().nextFloat()) * 0.2F + 1.0F);
			chicken.attackEntityFrom(DamageSource.generic, 0);
			chicken.setRevengeTarget(chicken); // panic
			chicken.timeUntilNextEgg = chicken.getRNG().nextInt(6000) + 7800;
			if (rand.nextInt(4) != 0) {
				drops.add(new RanchedItem(Items.egg));
			} else {
				int k = chicken.getRNG().nextInt(4) + 1;
				drops.add(new RanchedItem(Items.feather, k));
			}
		}
		return drops;
	}

}
