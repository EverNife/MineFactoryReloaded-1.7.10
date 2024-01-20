package powercrystals.minefactoryreloaded.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import io.netty.buffer.ByteBuf;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import powercrystals.minefactoryreloaded.MFRRegistry;

public class EntityNeedle extends Entity implements IProjectile, IEntityAdditionalSpawnData {

	private String _owner;
	private int ticksInAir = 0;
	private ItemStack _ammoSource;
	private double distance;
	private boolean _falling;

	public EntityNeedle(World world) {

		super(world);
		renderDistanceWeight = 10.0D;
		setSize(0.5F, 0.5F);
	}

	public EntityNeedle(World world, EntityPlayer owner, ItemStack ammoSource, float spread) {

		this(world);

		_owner = owner.getCommandSenderName();
		_ammoSource = ammoSource;

		setLocationAndAngles(owner.posX, owner.posY + owner.getEyeHeight(), owner.posZ, owner.rotationYaw, owner.rotationPitch);
		setPosition(posX, posY, posZ);
		yOffset = 0.0F;
		motionX = (-MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI));
		motionZ = (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI));
		motionY = (-MathHelper.sin(rotationPitch / 180.0F * (float) Math.PI));
		setThrowableHeading(motionX, motionY, motionZ, 3.25F, spread);
		distance = 0;
		//world.spawnEntityInWorld(new DebugTracker(world, owner, this));
	}

	@Override
	public void writeSpawnData(ByteBuf buffer) {

	}

	@Override
	public void readSpawnData(ByteBuf additionalData) {

		// small hack; offsets on the client only to make it look like it came from the gun
		posX -= (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		posY -= 0.08D;
		posZ -= -(MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
	}

	@Override
	protected void entityInit() {

		dataWatcher.addObject(16, Byte.valueOf((byte) 0));
	}

	@Override
	public void setThrowableHeading(double x, double y, double z, float speedMult, float spreadConst) {

		double normal = MathHelper.sqrt_double(x * x + y * y + z * z);
		x /= normal;
		y /= normal;
		z /= normal;
		x += rand.nextGaussian() * 0.0075D * spreadConst;
		y += rand.nextGaussian() * 0.0075D * spreadConst;
		z += rand.nextGaussian() * 0.0075D * spreadConst;
		x *= speedMult;
		y *= speedMult;
		z *= speedMult;
		motionX = x;
		motionY = y;
		motionZ = z;
		float horizSpeed = MathHelper.sqrt_double(x * x + z * z);
		prevRotationYaw = rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
		prevRotationPitch = rotationPitch = (float) (Math.atan2(y, horizSpeed) * 180.0D / Math.PI);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int unknown) {

		setPosition(x, y, z);
		setRotation(yaw, pitch);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {

		motionX = x;
		motionY = y;
		motionZ = z;

		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(x * x + z * z);
			prevRotationYaw = rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch = (float) (Math.atan2(y, f) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch;
			prevRotationYaw = rotationYaw;
			setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
		}
	}

	@Override
	public void onUpdate() {

		super.onUpdate();

		if (prevRotationPitch == 0.0F && prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
			prevRotationYaw = rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);
			prevRotationPitch = rotationPitch = (float) (Math.atan2(motionY, f) * 180.0D / Math.PI);
		}

		++ticksInAir;
		Vec3 pos = Vec3.createVectorHelper(posX, posY, posZ);
		Vec3 nextPos = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);
		MovingObjectPosition hit = worldObj.func_147447_a(pos, nextPos, false, true, false);
		pos = Vec3.createVectorHelper(posX, posY, posZ);
		nextPos = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ);

		if (hit != null) {
			nextPos = Vec3.createVectorHelper(hit.hitVec.xCoord, hit.hitVec.yCoord, hit.hitVec.zCoord);
		}

		Entity entityHit = null;
		List<?> list = worldObj.getEntitiesWithinAABBExcludingEntity(this,
			boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0D, 1.0D, 1.0D));
		double closestRange = 0.0D;
		double collisionRange = 0.3D;
		EntityPlayer owner = _owner == null ? null : worldObj.getPlayerEntityByName(_owner);

		for (int l = 0; l < list.size(); ++l) {
			Entity e = (Entity) list.get(l);

			if ((e != owner | ticksInAir >= 2) && e.canBeCollidedWith()) {
				AxisAlignedBB entitybb = e.boundingBox.expand(collisionRange, collisionRange, collisionRange);
				MovingObjectPosition entityHitPos = entitybb.calculateIntercept(pos, nextPos);

				if (entityHitPos != null) {
					double range = pos.distanceTo(entityHitPos.hitVec);

					if (range < closestRange || closestRange == 0.0D) {
						entityHit = e;
						closestRange = range;
					}
				}
			}
		}

		if (entityHit != null) {
			hit = new MovingObjectPosition(entityHit);
		}

		if (hit != null && hit.entityHit != null && hit.entityHit instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) hit.entityHit;

			if (entityplayer.capabilities.disableDamage || (owner != null && !owner.canAttackPlayer(entityplayer))) {
				hit = null;
			}
		}

		float speed = 0.0F;
		speed = MathHelper.sqrt_double(motionX * motionX + motionY * motionY + motionZ * motionZ);
		distance += speed;
		if (hit != null && !worldObj.isRemote) {
			if (MFRRegistry.getNeedleAmmoTypes().containsKey(_ammoSource.getItem())) {
				if (hit.entityHit != null) {
					MFRRegistry.getNeedleAmmoTypes().get(_ammoSource.getItem()).onHitEntity(_ammoSource,
						owner, hit.entityHit, distance);
				} else {
					MFRRegistry.getNeedleAmmoTypes().get(_ammoSource.getItem()).onHitBlock(_ammoSource,
						owner, worldObj, hit.blockX, hit.blockY, hit.blockZ, hit.sideHit, distance);
				}
			}
			setDead();
		}

		posX += motionX;
		posY += motionY;
		posZ += motionZ;
		speed = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ);
		rotationYaw = (float) (Math.atan2(motionX, motionZ) * 180.0D / Math.PI);

		for (rotationPitch = (float) (Math.atan2(motionY, speed) * 180.0D / Math.PI); rotationPitch - prevRotationPitch < -180.0F; ) {
			prevRotationPitch -= 360.0F;
		}

		while (rotationPitch - prevRotationPitch >= 180.0F) {
			prevRotationPitch += 360.0F;
		}

		while (rotationYaw - prevRotationYaw < -180.0F) {
			prevRotationYaw -= 360.0F;
		}

		while (rotationYaw - prevRotationYaw >= 180.0F) {
			prevRotationYaw += 360.0F;
		}

		rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2F;
		rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2F;
		float speedDropoff = 0.995F;
		collisionRange = 0.05F;

		if (_falling | speed < 0.05) {
			_falling = true;
			motionY -= 0.01;
			speedDropoff = 0.99F;
		}

		if (isInWater()) {
			double particleOffset = 0.25D;
			for (int i = 0; i < 4; ++i) {
				worldObj.spawnParticle("bubble", posX - motionX * particleOffset, posY - motionY *
						particleOffset, posZ - motionZ * particleOffset, motionX, motionY, motionZ);
			}

			speedDropoff = 0.8F;
		}

		motionX *= speedDropoff;
		motionY *= speedDropoff;
		motionZ *= speedDropoff;
		setPosition(posX, posY, posZ);
		func_145775_I();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {

		tag.setTag("ammoSource", _ammoSource.writeToNBT(new NBTTagCompound()));
		tag.setDouble("distance", distance);
		tag.setBoolean("falling", _falling);
		if (_owner != null)
			tag.setString("owner", _owner);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {

		if (tag.hasKey("ammoSource")) {
			_ammoSource = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("ammoSource"));
			distance = tag.getDouble("distance");
			_falling = tag.getBoolean("falling");
			if (tag.hasKey("owner"))
				_owner = tag.getString("owner");
		}
	}

	@Override
	protected boolean canTriggerWalking() {

		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getShadowSize() {

		return 0.0F;
	}

	@Override
	public boolean canAttackWithItem() {

		return false;
	}
}
