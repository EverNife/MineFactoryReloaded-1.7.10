package powercrystals.minefactoryreloaded.farmables.grindables;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import powercrystals.minefactoryreloaded.api.IFactoryGrindable;
import powercrystals.minefactoryreloaded.api.MobDrop;
import powercrystals.minefactoryreloaded.core.MFRUtil;

public class GrindableZombiePigman implements IFactoryGrindable
{
	private static final UUID signBoostModifierUUID = UUID.fromString("1CBBA087-C48F-4F33-8BD4-0E85FC2F6A0A");
	private static final AttributeModifier modifier = (new AttributeModifier(signBoostModifierUUID,
			"Battlesign boost", 10D, 0)).setSaved(false);
	public static final ItemStack sign;
	static {
		sign = new ItemStack(Items.sign);
		sign.addEnchantment(Enchantment.sharpness, 4);
		sign.addEnchantment(Enchantment.knockback, 2);
		sign.addEnchantment(Enchantment.fireAspect, 1);
		NBTTagList list = new NBTTagList();
		list.appendTag(MFRUtil.writeModifierToNBT(
				SharedMonsterAttributes.maxHealth.getAttributeUnlocalizedName(), modifier));
		sign.setTagInfo("AttributeModifiers", list);
	}

	@Override
	public Class<? extends EntityLivingBase> getGrindableEntity()
	{
		return EntityPigZombie.class;
	}

	@Override
	public List<MobDrop> grind(World world, EntityLivingBase entity, Random random)
	{
		List<MobDrop> drops = new ArrayList<MobDrop>();

		if (random.nextInt(3000) == 0)
			drops.add(new MobDrop(10, sign.copy()));

		return drops;
	}

	@Override
	public boolean processEntity(EntityLivingBase entity)
	{
		return false;
	}
}
