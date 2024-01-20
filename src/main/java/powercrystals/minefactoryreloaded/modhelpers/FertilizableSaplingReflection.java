package powercrystals.minefactoryreloaded.modhelpers;

import java.lang.reflect.Method;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import powercrystals.minefactoryreloaded.api.FertilizerType;
import powercrystals.minefactoryreloaded.api.IFactoryFertilizable;

public class FertilizableSaplingReflection implements IFactoryFertilizable
{
	private Method _fertilize;
	private Block _blockId;
	
	public FertilizableSaplingReflection(Block blockId, Method fertilize)
	{
		_blockId = blockId;
		_fertilize = fertilize;
	}
	
	@Override
	public Block getPlant()
	{
		return _blockId;
	}
	
	@Override
	public boolean canFertilize(World world, int x, int y, int z, FertilizerType fertilizerType)
	{
		return fertilizerType == FertilizerType.GrowPlant;
	}
	
	@Override
	public boolean fertilize(World world, Random rand, int x, int y, int z, FertilizerType fertilizerType)
	{
		try
		{
			_fertilize.invoke(_blockId, world, x, y, z);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return world.getBlock(x, y, z) != _blockId;
	}
}
