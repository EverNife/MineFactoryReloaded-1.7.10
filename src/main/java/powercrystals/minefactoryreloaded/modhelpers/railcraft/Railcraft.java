package powercrystals.minefactoryreloaded.modhelpers.railcraft;

import cofh.mod.ChildMod;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.CustomProperty;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;

import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import powercrystals.minefactoryreloaded.MineFactoryReloadedCore;
import powercrystals.minefactoryreloaded.setup.MFRThings;

@ChildMod(parent = MineFactoryReloadedCore.modId, mod = @Mod(modid = "MineFactoryReloaded|CompatRailcraft",
		name = "MFR Compat: Railcraft",
		version = MineFactoryReloadedCore.version,
		dependencies = "after:MineFactoryReloaded;after:Railcraft",
		customProperties = @CustomProperty(k = "cofhversion", v = "true")))
public class Railcraft {

	@EventHandler
	public void load(FMLInitializationEvent evt) {

		try {
			String id = Block.blockRegistry.getNameForObject(MFRThings.factoryDecorativeStoneBlock);
			FMLInterModComms.sendMessage("Railcraft", "balast", String.format("%s@%s", id, 8));
			FMLInterModComms.sendMessage("Railcraft", "balast", String.format("%s@%s", id, 9));
			// white sand? black sand?

			Object rockCrusher = Class.forName("mods.railcraft.api.crafting.RailcraftCraftingManager").getField("rockCrusher")
					.get(null);
			Method createNewRecipe = Class.forName("mods.railcraft.api.crafting.IRockCrusherCraftingManager").getMethod(
				"createNewRecipe", ItemStack.class, boolean.class, boolean.class);
			Method addOutput = Class.forName("mods.railcraft.api.crafting.IRockCrusherRecipe").getMethod("addOutput",
				ItemStack.class, float.class);

			Object recipe = createNewRecipe.invoke(rockCrusher, new ItemStack(MFRThings.factoryDecorativeStoneBlock, 1, 10),
				true, false);
			addOutput.invoke(recipe, new ItemStack(MFRThings.factoryDecorativeStoneBlock, 1, 2), 1.0f); // Paved Blackstone -> Cobble

			recipe = createNewRecipe
					.invoke(rockCrusher, new ItemStack(MFRThings.factoryDecorativeStoneBlock, 1, 11), true, false);
			addOutput.invoke(recipe, new ItemStack(MFRThings.factoryDecorativeStoneBlock, 1, 3), 1.0f); // Paved Whitestone -> Cobble

			recipe = createNewRecipe.invoke(rockCrusher, new ItemStack(MFRThings.factoryDecorativeStoneBlock, 1, 0), true, false);
			addOutput.invoke(recipe, new ItemStack(MFRThings.factoryDecorativeStoneBlock, 1, 2), 1.0f); // Smooth Blackstone -> Cobble

			recipe = createNewRecipe.invoke(rockCrusher, new ItemStack(MFRThings.factoryDecorativeStoneBlock, 1, 1), true, false);
			addOutput.invoke(recipe, new ItemStack(MFRThings.factoryDecorativeStoneBlock, 1, 3), 1.0f); // Smooth Whitestone -> Cobble

			recipe = createNewRecipe.invoke(rockCrusher, new ItemStack(MFRThings.factoryDecorativeStoneBlock, 1, 2), true, false);
			addOutput.invoke(recipe, new ItemStack(MFRThings.factoryDecorativeStoneBlock, 1, 8), 1.0f); // Cobble Blackstone -> Gravel + flint
			addOutput.invoke(recipe, new ItemStack(Items.flint, 1, 0), 0.05f);

			recipe = createNewRecipe.invoke(rockCrusher, new ItemStack(MFRThings.factoryDecorativeStoneBlock, 1, 3), true, false);
			addOutput.invoke(recipe, new ItemStack(MFRThings.factoryDecorativeStoneBlock, 1, 9), 1.0f); // Cobble Whitestone -> Gravel + flint
			addOutput.invoke(recipe, new ItemStack(Items.flint, 1, 0), 0.05f);
		} catch (Throwable $) {
			ModContainer This = FMLCommonHandler.instance().findContainerFor(this);
			LogManager.getLogger(This.getModId()).log(Level.ERROR, "There was a problem loading " + This.getName(), $);
		}
	}

}
