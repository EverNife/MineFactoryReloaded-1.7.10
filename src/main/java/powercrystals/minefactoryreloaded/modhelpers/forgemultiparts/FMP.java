package powercrystals.minefactoryreloaded.modhelpers.forgemultiparts;

import cofh.mod.ChildMod;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.CustomProperty;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.block.ItemBlockFactory;
import powercrystals.minefactoryreloaded.setup.MFRThings;

@ChildMod(parent = MineFactoryReloadedCore.modId, mod = @Mod(modid = "MineFactoryReloaded|CompatForgeMicroblock",
		name = "MFR Compat: ForgeMicroblock",
		version = MineFactoryReloadedCore.version,
		dependencies = "after:MineFactoryReloaded",
		customProperties = @CustomProperty(k = "cofhversion", v = "true")))
public class FMP {

	@Mod.EventHandler
	public void Init(FMLInitializationEvent evt) {

		try {
			addSubtypes((ItemBlockFactory) Item.getItemFromBlock(MFRThings.factoryDecorativeBrickBlock));
			addSubtypes((ItemBlockFactory) Item.getItemFromBlock(MFRThings.factoryDecorativeStoneBlock));
			addSubtypes((ItemBlockFactory) Item.getItemFromBlock(MFRThings.factoryGlassBlock));
			addSubtypes((ItemBlockFactory) Item.getItemFromBlock(MFRThings.rubberLeavesBlock));
			addSubtypes((ItemBlockFactory) Item.getItemFromBlock(MFRThings.factoryRoadBlock));
			for (Block block : MFRThings.machineBlocks.valueCollection())
				addSubtypes((ItemBlockFactory) Item.getItemFromBlock(block));
			sendComm(new ItemStack(MFRThings.rubberWoodBlock, 1, 0));
		} catch (Throwable $) {
			ModContainer This = FMLCommonHandler.instance().findContainerFor(this);
			LogManager.getLogger(This.getModId()).log(Level.ERROR, "There was a problem loading " + This.getName(), $);
		}
	}

	private void addSubtypes(ItemBlockFactory item) {

		List<ItemStack> items = new LinkedList<ItemStack>();
		item.getSubItems(item, items);
		for (int i = items.size(); i-- > 0;)
			sendComm(items.get(i));
	}

	private void sendComm(ItemStack data) {

		FMLInterModComms.sendMessage("ForgeMicroblock", "microMaterial", data);
	}

}
