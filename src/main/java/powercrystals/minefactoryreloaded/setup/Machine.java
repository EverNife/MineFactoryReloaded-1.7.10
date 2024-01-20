package powercrystals.minefactoryreloaded.setup;

import static net.minecraft.util.EnumChatFormatting.*;
import static powercrystals.minefactoryreloaded.setup.Machine.Side.*;

import cofh.core.CoFHProps;
import cofh.lib.util.RegistryUtils;
import cofh.lib.util.helpers.StringHelper;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;

import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import powercrystals.minefactoryreloaded.block.BlockFactoryMachine;
import powercrystals.minefactoryreloaded.core.MFRUtil;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactory;
import powercrystals.minefactoryreloaded.tile.base.TileEntityFactoryGenerator;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityAutoAnvil;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityAutoBrewer;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityAutoDisenchanter;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityAutoDisenchanterFluid;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityAutoEnchanter;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityAutoJukebox;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityAutoSpawner;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityBioFuelGenerator;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityBioReactor;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityBlockBreaker;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityBlockPlacer;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityBlockSmasher;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityBreeder;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityChronotyper;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityChunkLoader;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityCollector;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityComposter;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityDeepStorageUnit;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityEjector;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityEnchantmentRouter;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityFertilizer;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityFisher;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityFountain;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityFruitPicker;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityGrinder;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityHarvester;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityItemRouter;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityLaserDrill;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityLaserDrillPrecharger;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityLavaFabricator;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityLiquiCrafter;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityLiquidRouter;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityMeatPacker;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityMobCounter;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityMobRouter;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityPlanter;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityRancher;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityRedNote;
import powercrystals.minefactoryreloaded.tile.machine.TileEntitySewer;
import powercrystals.minefactoryreloaded.tile.machine.TileEntitySlaughterhouse;
import powercrystals.minefactoryreloaded.tile.machine.TileEntitySludgeBoiler;
import powercrystals.minefactoryreloaded.tile.machine.TileEntitySteamBoiler;
import powercrystals.minefactoryreloaded.tile.machine.TileEntitySteamTurbine;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityUnifier;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityVet;
import powercrystals.minefactoryreloaded.tile.machine.TileEntityWeather;

public class Machine {

	public static final Material MATERIAL = new MachineMaterial(MapColor.ironColor);
	protected static List<Machine> _machines = new LinkedList<Machine>();
	protected static TIntObjectHashMap<Machine> _machineMappings = new TIntObjectHashMap<Machine>();
	protected static TIntArrayList _highestMetas = new TIntArrayList();

	public static Machine Planter = new Machine(0, 0, "Planter", TileEntityPlanter.class, 160, 8000);
	public static Machine Fisher = new Machine(0, 1, "Fisher", TileEntityFisher.class, 20, 16000);
	public static Machine Harvester = new Machine(0, 2, "Harvester", TileEntityHarvester.class, 240, 16000);
	public static Machine Rancher = new Machine(0, 3, "Rancher", TileEntityRancher.class, 320, 32000);
	public static Machine Fertilizer = new Machine(0, 4, "Fertilizer", TileEntityFertilizer.class, 960, 32000);
	public static Machine Vet = new Machine(0, 5, "Vet", TileEntityVet.class, 320, 32000);
	public static Machine ItemCollector = new Machine(0, 6, "ItemCollector", TileEntityCollector.class);
	public static Machine BlockBreaker = new Machine(0, 7, "BlockBreaker", TileEntityBlockBreaker.class, 960, 64000);
	public static Machine WeatherCollector = new Machine(0, 8, "WeatherCollector", TileEntityWeather.class, 10, 16000);
	public static Machine SludgeBoiler = new Machine(0, 9, "SludgeBoiler", TileEntitySludgeBoiler.class, 30, 16000);
	public static Machine Sewer = new Machine(0, 10, "Sewer", TileEntitySewer.class);
	public static Machine Composter = new Machine(0, 11, "Composter", TileEntityComposter.class, 40, 16000);
	public static Machine Breeder = new Machine(0, 12, "Breeder", TileEntityBreeder.class, 640, 16000);
	public static Machine Grinder = new Machine(0, 13, "Grinder", TileEntityGrinder.class, 3200, 32000);
	public static Machine AutoEnchanter = new Machine(0, 14, "AutoEnchanter", TileEntityAutoEnchanter.class, 160, 16000);
	public static Machine Chronotyper = new Machine(0, 15, "Chronotyper", TileEntityChronotyper.class, 1280, 16000);

	public static Machine Ejector = new Machine(1, 0, "Ejector", TileEntityEjector.class);
	public static Machine ItemRouter = new Machine(1, 1, "ItemRouter", TileEntityItemRouter.class);
	public static Machine LiquidRouter = new Machine(1, 2, "LiquidRouter", TileEntityLiquidRouter.class);
	public static Machine DeepStorageUnit = new Machine(1, 3, "DeepStorageUnit", TileEntityDeepStorageUnit.class) {

		@Override
		public boolean hasTooltip(ItemStack stack) {

			return stack.hasTagCompound() && stack.getTagCompound().hasKey("storedStack");
		}

		@Override
		public void addInformation(ItemStack stack, EntityPlayer player, List<String> info, boolean adv) {

			NBTTagCompound c = stack.getTagCompound();
			if (c != null && c.hasKey("storedStack")) {
				ItemStack storedItem = ItemStack.loadItemStackFromNBT(c.getCompoundTag("storedStack"));
				int storedQuantity = c.getInteger("storedQuantity");
				if (storedItem != null & storedQuantity > 0) {
					info.add(String.format(MFRUtil.localize("tip.info.mfr.dsu.contains", true),
						storedQuantity + " " + storedItem.getDisplayName() +
								(adv ? " (" + Item.itemRegistry.getIDForObject(storedItem.getItem()) + ":" +
										storedItem.getItemDamageForDisplay() + ")" : "")));
				}
			}
			super.addInformation(stack, player, info, adv);
		}
	};
	public static Machine LiquiCrafter = new Machine(1, 4, "LiquiCrafter", TileEntityLiquiCrafter.class);
	public static Machine LavaFabricator = new Machine(1, 5, "LavaFabricator", TileEntityLavaFabricator.class, 800, 16000);
	public static Machine SteamBoiler = new Machine(1, 6, "SteamBoiler", TileEntitySteamBoiler.class);
	public static Machine AutoJukebox = new Machine(1, 7, "AutoJukebox", TileEntityAutoJukebox.class);
	public static Machine Unifier = new Machine(1, 8, "Unifier", TileEntityUnifier.class);
	public static Machine AutoSpawner = new Machine(1, 9, "AutoSpawner", TileEntityAutoSpawner.class, 600, 32000);
	public static Machine BioReactor = new Machine(1, 10, "BioReactor", TileEntityBioReactor.class);
	public static Machine BioFuelGenerator = new Machine(1, 11, "BioFuelGenerator", TileEntityBioFuelGenerator.class, 160, 10000);
	public static Machine AutoDisenchanter = new Machine(1, 12, "AutoDisenchanter", TileEntityAutoDisenchanter.class, 320, 16000) {

		@Override
		public void load(Configuration c) {

			if (MFRConfig.disenchanterEssence.getBoolean(false)) {
				_tileEntityClass = TileEntityAutoDisenchanterFluid.class;
			}
			super.load(c);
		}
	};
	public static Machine Slaughterhouse = new Machine(1, 13, "Slaughterhouse", TileEntitySlaughterhouse.class, 1000, 16000);
	public static Machine MeatPacker = new Machine(1, 14, "MeatPacker", TileEntityMeatPacker.class, 20, 16000);
	public static Machine EnchantmentRouter = new Machine(1, 15, "EnchantmentRouter", TileEntityEnchantmentRouter.class);

	public static Machine LaserDrill = new Machine(2, 0, "LaserDrill", TileEntityLaserDrill.class);
	public static Machine LaserDrillPrecharger = new Machine(2, 1, "LaserDrillPrecharger", TileEntityLaserDrillPrecharger.class,
			5000, 96000);
	public static Machine AutoAnvil = new Machine(2, 2, "AutoAnvil", TileEntityAutoAnvil.class, 160, 16000);
	public static Machine BlockSmasher = new Machine(2, 3, "BlockSmasher", TileEntityBlockSmasher.class, 10, 16000);
	public static Machine RedNote = new Machine(2, 4, "RedNote", TileEntityRedNote.class);
	public static Machine AutoBrewer = new Machine(2, 5, "AutoBrewer", TileEntityAutoBrewer.class, 40, 16000);
	public static Machine FruitPicker = new Machine(2, 6, "FruitPicker", TileEntityFruitPicker.class, 320, 16000);
	public static Machine BlockPlacer = new Machine(2, 7, "BlockPlacer", TileEntityBlockPlacer.class, 10, 16000);
	public static Machine MobCounter = new Machine(2, 8, "MobCounter", TileEntityMobCounter.class);
	public static Machine SteamTurbine = new Machine(2, 9, "SteamTurbine", TileEntitySteamTurbine.class, 160, 10000);
	public static Machine ChunkLoader = new Machine(2, 10, "ChunkLoader", TileEntityChunkLoader.class, 10, Integer.MAX_VALUE, false) {

		@Override
		public void load(Configuration c) {

			if (!MFRConfig.enableConfigurableCLEnergy.getBoolean(false))
				_activationEnergy = 0;
			super.load(c);
		}
	};
	public static Machine Fountain = new Machine(2, 11, "Fountain", TileEntityFountain.class, 80, 16000);
	public static Machine MobRouter = new Machine(2, 12, "MobRouter", TileEntityMobRouter.class, 2560, 16000);

	protected final int _blockIndex;
	protected final int _meta;
	protected final int _machineIndex;

	protected IIcon[] _iconsActive = new IIcon[6];
	protected IIcon[] _iconsIdle = new IIcon[6];

	protected final String _name;
	protected final String _internalName;
	protected final String _tileEntityName;
	protected Class<? extends TileEntityFactory> _tileEntityClass;

	protected int _activationEnergy;
	protected int _energyStoredMax;
	protected boolean _useDaRF;
	protected boolean _generator;

	protected Property _isRecipeEnabled;

	protected Machine(int blockIndex, int meta, String name,
			Class<? extends TileEntityFactory> tileEntityClass) {

		this(blockIndex, meta, name, tileEntityClass, 0, 0);
	}

	protected Machine(int blockIndex, int meta, String name,
			Class<? extends TileEntityFactory> tileEntityClass,
			int activationEnergy, int energyStoredMax) {

		this(blockIndex, meta, name, tileEntityClass, activationEnergy, energyStoredMax, true);
	}

	protected Machine(int blockIndex, int meta, String name,
			Class<? extends TileEntityFactory> tileEntityClass,
			int activationEnergy, int energyStoredMax, boolean configurable) {

		_blockIndex = blockIndex;
		_meta = meta;
		_machineIndex = _meta | (_blockIndex << 4);

		if (_meta > 15) {
			throw new IllegalArgumentException("Maximum meta value for machines is 15");
		}

		if (_machineMappings.get(_machineIndex) != null) {
			throw new IllegalArgumentException("Machine with index " + blockIndex + " and meta " +
					meta + " already exists.");
		}

		_name = name;
		_internalName = "tile.mfr.machine." + name.toLowerCase(Locale.US);
		_tileEntityName = "factory" + name;
		_tileEntityClass = tileEntityClass;

		_activationEnergy = activationEnergy;
		_energyStoredMax = energyStoredMax;
		_useDaRF = configurable;

		_generator = TileEntityFactoryGenerator.class.isAssignableFrom(tileEntityClass);

		_machineMappings.put(_machineIndex, this);
		_machines.add(this);

		_highestMetas.ensureCapacity(_blockIndex);
		if (_highestMetas.getQuick(_blockIndex) < _meta) {
			_highestMetas.setQuick(_blockIndex, _meta);
		}
	}

	public static Machine getMachineFromIndex(int blockIndex, int meta) {

		return _machineMappings.get(meta | (blockIndex << 4));
	}

	public static Machine getMachineFromId(BlockFactoryMachine block, int meta) {

		return _machineMappings.get(meta | (block.getBlockIndex() << 4));
	}

	public static int getHighestMetadata(int blockIndex) {

		return _highestMetas.getQuick(blockIndex);
	}

	public static List<Machine> values() {

		return _machines;
	}

	public static void LoadTextures(int blockIndex, IIconRegister ir) {

		for (Machine m : _machines) {
			if (m.getBlockIndex() == blockIndex) {
				m.loadIcons(ir);
			}
		}
	}

	private String getTooltipText() {

		return "tip.info.mfr." + _name.toLowerCase(Locale.US);
	}

	public boolean hasTooltip(ItemStack stack) {

		if (stack.stackTagCompound != null)
			if (_energyStoredMax > 0 && stack.stackTagCompound.hasKey("energyStored"))
				return true;
		return _activationEnergy > 0 || StatCollector.canTranslate(getTooltipText());
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List<String> info, boolean adv) {

		if (stack.stackTagCompound != null) {
			NBTTagCompound tag = stack.stackTagCompound;
			if (_energyStoredMax > 0 && tag.hasKey("energyStored")) {
				String max = StringHelper.getScaledNumber(_energyStoredMax);
				String cur = StringHelper.getScaledNumber(Math.min(_energyStoredMax, tag.getInteger("energyStored")));
				info.add(MFRUtil.localize("info.cofh.energyStored", true) + ": " + cur + " / " + max + " RF");
			}
		}
		if (_activationEnergy > 0) {
			if (_generator) {
				info.add(MFRUtil.localize("info.cofh.energyProduce", true) + ": " + GREEN + _activationEnergy + " RF/t" + RESET);
				info.add(MFRUtil.localize("tip.info.mfr.generator.produces", true));
			} else
				info.add(MFRUtil.localize("info.cofh.energyConsume", true) + ": " + RED + _activationEnergy + " RF/Wk" + RESET);
		}
		String s = getTooltipText();
		if (StatCollector.canTranslate(s)) {
			s = StatCollector.translateToLocal(s);
			if (s.contains("\n"))
				info.addAll(Arrays.asList(s.split("\n")));
			else
				info.add(s);
		}
	}

	public final String getName() {

		return _name;
	}

	public final String getInternalName() {

		return _internalName;
	}

	public Block getBlock() {

		return MFRThings.machineBlocks.get(_blockIndex);
	}

	public ItemStack getItemStack() {

		return new ItemStack(MFRThings.machineBlocks.get(_blockIndex), 1, _meta);
	}

	public final int getMeta() {

		return _meta;
	}

	public final int getBlockIndex() {

		return _blockIndex;
	}

	public final boolean getIsRecipeEnabled() {

		return _isRecipeEnabled.getBoolean(true);
	}

	public TileEntityFactory getNewTileEntity() {

		try {
			TileEntityFactory tileEntity = _tileEntityClass.newInstance();
			return tileEntity;
		} catch (IllegalAccessException x) {
			FMLLog.severe("Unable to create instance of TileEntity from %s", _tileEntityClass.getName());
			return null;
		} catch (InstantiationException x) {
			FMLLog.severe("Unable to create instance of TileEntity from %s", _tileEntityClass.getName());
			return null;
		}
	}

	public final int getActivationEnergyDaRF() {

		return _activationEnergy / 10;
	}

	public final int getActivationEnergy() {

		return _activationEnergy;
	}

	public final int getMaxEnergyStorage() {

		return _energyStoredMax;
	}

	public void load(Configuration c) {

		_isRecipeEnabled = c.get("Machine." + _name, "Recipe.Enabled", true).setRequiresMcRestart(true);
		if (_activationEnergy > 0) {
			String comment = "The energy cost for this machine to complete one work cycle";
			if (_generator)
				comment = "The amount of energy generated by this machine in one tick";
			if (_useDaRF)
				_activationEnergy = c.get("Machine." + _name, "ActivationCostDaRF", getActivationEnergyDaRF(),
					comment + ", in units of 10 RF (i.e., 2 DaRF = 20 RF)").setRequiresMcRestart(true).getInt() * 10;
			else
				_activationEnergy = c.get("Machine." + _name, "ActivationCostRF", getActivationEnergy(),
					comment + ", in units of **1** RF").setRequiresMcRestart(true).getInt();
		}

		MFRThings.machineBlocks.get(_blockIndex).setHarvestLevel("pickaxe", 0, _meta);
		GameRegistry.registerTileEntity(_tileEntityClass, _tileEntityName);
	}

	public IIcon getIcon(int side, boolean isActive) {

		return (isActive ? _iconsActive : _iconsIdle)[side];
	}

	public void loadIcons(IIconRegister ir) {

		_iconsActive[0] = ir.registerIcon(loadIcon(bottom, true));
		_iconsActive[1] = ir.registerIcon(loadIcon(top, true));
		_iconsActive[2] = ir.registerIcon(loadIcon(front, true));
		_iconsActive[3] = ir.registerIcon(loadIcon(back, true));
		_iconsActive[4] = ir.registerIcon(loadIcon(left, true));
		_iconsActive[5] = ir.registerIcon(loadIcon(right, true));
		_iconsIdle[0] = ir.registerIcon(loadIcon(bottom, false));
		_iconsIdle[1] = ir.registerIcon(loadIcon(top, false));
		_iconsIdle[2] = ir.registerIcon(loadIcon(front, false));
		_iconsIdle[3] = ir.registerIcon(loadIcon(back, false));
		_iconsIdle[4] = ir.registerIcon(loadIcon(left, false));
		_iconsIdle[5] = ir.registerIcon(loadIcon(right, false));
	}

	protected String loadIcon(Side side, boolean active) {

		final String base = "minefactoryreloaded:machines/";
		final String a = side.getMain(active);
		final String name = getInternalName() + ".";
		String t;
		if (CoFHProps.enableColorBlindTextures) {
			final String cb = ".cb";
			if (RegistryUtils.blockTextureExists(t = base + name + a + cb))
				return t;
			else if (side.hasAlt && RegistryUtils.blockTextureExists(t = base + name + side.getAlt(active) + cb))
				return t;
			else if (RegistryUtils.blockTextureExists(t = base + name + side.name + cb))
				return t;
			else if (side.hasAlt && RegistryUtils.blockTextureExists(t = base + name + side.alt + cb))
				return t;
		}
		if (RegistryUtils.blockTextureExists(t = base + name + a))
			return t;
		else if (side.hasAlt && RegistryUtils.blockTextureExists(t = base + name + side.getAlt(active)))
			return t;
		else if (RegistryUtils.blockTextureExists(t = base + name + side.name))
			return t;
		else if (side.hasAlt && RegistryUtils.blockTextureExists(t = base + name + side.alt))
			return t;
		else if (RegistryUtils.blockTextureExists(t = base + "tile.mfr.machine.0." + a))
			return t;
		else if (side.hasAlt && RegistryUtils.blockTextureExists(t = base + "tile.mfr.machine.0." + side.getAlt(active)))
			return t;
		else if (RegistryUtils.blockTextureExists(t = base + "tile.mfr.machine.0." + side.name))
			return t;
		else
			return base + "tile.mfr.machine.0." + side.alt;
	}

	protected static enum Side {
		bottom(null),
		top(null),
		front("side"),
		back("side"),
		left("side"),
		right("side");

		private Side(String _alt) {

			name = name();
			hasAlt = _alt != null;
			if (hasAlt)
				alt = _alt;
			else
				alt = name;
		}

		protected String active = "active.";
		protected String idle = "idle.";
		public final String name;
		public final String alt;
		public boolean hasAlt;

		public String getMain(boolean _active) {

			return (_active ? active : idle) + name();
		}

		public String getAlt(boolean _active) {

			return hasAlt ? ((_active ? active : idle) + alt) : "";
		}
	}
}
