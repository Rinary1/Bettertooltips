package fi.septicuss.bettertooltips;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fi.septicuss.bettertooltips.api.TooltipsAPI;
import fi.septicuss.bettertooltips.commands.TooltipsCommand;
import fi.septicuss.bettertooltips.commands.subcommands.EvalCommand;
import fi.septicuss.bettertooltips.commands.subcommands.ListVarsCommand;
import fi.septicuss.bettertooltips.commands.subcommands.ReloadCommand;
import fi.septicuss.bettertooltips.commands.subcommands.SendPresetCommand;
import fi.septicuss.bettertooltips.commands.subcommands.SendThemeCommand;
import fi.septicuss.bettertooltips.commands.subcommands.VarsCommand;
import fi.septicuss.bettertooltips.integrations.AreaProvider;
import fi.septicuss.bettertooltips.integrations.FurnitureProvider;
import fi.septicuss.bettertooltips.integrations.IntegratedPlugin;
import fi.septicuss.bettertooltips.integrations.crucible.CrucibleFurnitureProvider;
import fi.septicuss.bettertooltips.integrations.itemsadder.ItemsAdderFurnitureProvider;
import fi.septicuss.bettertooltips.integrations.oraxen.OraxenFurnitureProvider;
import fi.septicuss.bettertooltips.integrations.papi.TooltipsExpansion;
import fi.septicuss.bettertooltips.integrations.worldguard.WorldGuardAreaProvider;
import fi.septicuss.bettertooltips.listener.PlayerConnectionListener;
import fi.septicuss.bettertooltips.listener.PlayerInteractListener;
import fi.septicuss.bettertooltips.listener.PlayerMovementListener;
import fi.septicuss.bettertooltips.object.icon.IconManager;
import fi.septicuss.bettertooltips.object.preset.PresetManager;
import fi.septicuss.bettertooltips.object.preset.condition.ConditionManager;
import fi.septicuss.bettertooltips.object.preset.condition.argument.Argument;
import fi.septicuss.bettertooltips.object.preset.condition.impl.BlockNbtEquals;
import fi.septicuss.bettertooltips.object.preset.condition.impl.BlockStateEquals;
import fi.septicuss.bettertooltips.object.preset.condition.impl.Compare;
import fi.septicuss.bettertooltips.object.preset.condition.impl.Day;
import fi.septicuss.bettertooltips.object.preset.condition.impl.EntityNbtEquals;
import fi.septicuss.bettertooltips.object.preset.condition.impl.Equipped;
import fi.septicuss.bettertooltips.object.preset.condition.impl.Gamemode;
import fi.septicuss.bettertooltips.object.preset.condition.impl.InCuboid;
import fi.septicuss.bettertooltips.object.preset.condition.impl.ItemNbtEquals;
import fi.septicuss.bettertooltips.object.preset.condition.impl.Location;
import fi.septicuss.bettertooltips.object.preset.condition.impl.LookingAtBlock;
import fi.septicuss.bettertooltips.object.preset.condition.impl.LookingAtCitizen;
import fi.septicuss.bettertooltips.object.preset.condition.impl.LookingAtEntity;
import fi.septicuss.bettertooltips.object.preset.condition.impl.LookingAtFurniture;
import fi.septicuss.bettertooltips.object.preset.condition.impl.LookingAtMythicMob;
import fi.septicuss.bettertooltips.object.preset.condition.impl.Night;
import fi.septicuss.bettertooltips.object.preset.condition.impl.Op;
import fi.septicuss.bettertooltips.object.preset.condition.impl.Permission;
import fi.septicuss.bettertooltips.object.preset.condition.impl.Region;
import fi.septicuss.bettertooltips.object.preset.condition.impl.Sneaking;
import fi.septicuss.bettertooltips.object.preset.condition.impl.StandingOn;
import fi.septicuss.bettertooltips.object.preset.condition.impl.TileEntityNbtEquals;
import fi.septicuss.bettertooltips.object.preset.condition.impl.Time;
import fi.septicuss.bettertooltips.object.preset.condition.impl.World;
import fi.septicuss.bettertooltips.object.schema.SchemaManager;
import fi.septicuss.bettertooltips.object.theme.ThemeManager;
import fi.septicuss.bettertooltips.pack.PackGenerator;
import fi.septicuss.bettertooltips.pack.impl.IconGenerator;
import fi.septicuss.bettertooltips.pack.impl.LineGenerator;
import fi.septicuss.bettertooltips.pack.impl.SpaceGenerator;
import fi.septicuss.bettertooltips.pack.impl.TextureGenerator;
import fi.septicuss.bettertooltips.pack.impl.ThemeGenerator;
import fi.septicuss.bettertooltips.tooltip.TooltipManager;
import fi.septicuss.bettertooltips.tooltip.building.text.TextLine;
import fi.septicuss.bettertooltips.tooltip.runnable.TooltipRunnableManager;
import fi.septicuss.bettertooltips.utils.FileSetup;
import fi.septicuss.bettertooltips.utils.FileUtils;
import fi.septicuss.bettertooltips.utils.Messaging;
import fi.septicuss.bettertooltips.utils.Utils;
import fi.septicuss.bettertooltips.utils.cache.furniture.FurnitureCache;
import fi.septicuss.bettertooltips.utils.cache.player.LookingAtCache;
import fi.septicuss.bettertooltips.utils.cache.tooltip.TooltipCache;
import fi.septicuss.bettertooltips.utils.font.Widths;
import fi.septicuss.bettertooltips.utils.font.Widths.SizedChar;
import fi.septicuss.bettertooltips.utils.placeholder.Placeholders;
import fi.septicuss.bettertooltips.utils.placeholder.impl.SimplePlaceholderParser;
import fi.septicuss.bettertooltips.utils.variable.Variables;

public class Tooltips extends JavaPlugin {

	public static Gson GSON = new GsonBuilder().create();
	public static boolean SUPPORT_DISPLAY_ENTITIES;
	public static List<EntityType> FURNITURE_ENTITIES;
	private static Tooltips INSTANCE;
	private static Logger LOGGER;
	private static boolean USE_SPACES;

	private ProtocolManager protocolManager;
	private SchemaManager schemaManager;
	private IconManager iconManager;
	private ThemeManager themeManager;
	private PresetManager presetManager;
	private ConditionManager conditionManager;
	private TooltipManager tooltipManager;
	private TooltipRunnableManager runnableManager;

	private FurnitureProvider furnitureProvider;
	private AreaProvider areaProvider;

	private PlayerInteractListener playerInteractListener;

	// ------------------------------------------------------

	public Tooltips() {
		INSTANCE = this;
		SUPPORT_DISPLAY_ENTITIES = checkIfSupportsDisplayEntities();
		LOGGER = getLogger();

		FURNITURE_ENTITIES = Lists.newArrayList();
		FURNITURE_ENTITIES.add(EntityType.ITEM_FRAME);
		FURNITURE_ENTITIES.add(EntityType.ARMOR_STAND);

		if (SUPPORT_DISPLAY_ENTITIES) {
			FURNITURE_ENTITIES.add(EntityType.ITEM_DISPLAY);
			FURNITURE_ENTITIES.add(EntityType.BLOCK_DISPLAY);
			FURNITURE_ENTITIES.add(EntityType.INTERACTION);
		}

	}

	private static boolean checkIfSupportsDisplayEntities() {
		try {
			Class.forName("org.bukkit.entity.ItemDisplay");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	// ------------------------------------------------------

	@Override
	public void onLoad() {
		registerDefaultConditions();
	}
	
	@Override
	public void onEnable() {
		protocolManager = ProtocolLibrary.getProtocolManager();

		FileSetup.performMigration(this);
		FileSetup.setupFiles(this);

		loadVariables();
		loadIntegrations();
		loadListeners();

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			TooltipsExpansion expansion = new TooltipsExpansion();
			if (expansion.isRegistered())
				expansion.unregister();
			expansion.register();
		}

		conditionManager = new ConditionManager();

		addLocalPlaceholders();
		loadCommands();
		reload();
	}

	@Override
	public void onDisable() {
		if (this.runnableManager != null)
			this.runnableManager.stop();

		Variables.PERSISTENT.save();
	}

	// ------------------------------------------------------

	private void registerDefaultConditions() {
		TooltipsAPI.registerCondition("day", new Day());
		TooltipsAPI.registerCondition("night", new Night());
		TooltipsAPI.registerCondition("world", new World());
		TooltipsAPI.registerCondition("gamemode", new Gamemode());
		TooltipsAPI.registerCondition("sneaking", new Sneaking());
		TooltipsAPI.registerCondition("compare", new Compare());
		TooltipsAPI.registerCondition("lookingatblock", new LookingAtBlock());
		TooltipsAPI.registerCondition("lookingatfurniture", new LookingAtFurniture());
		TooltipsAPI.registerCondition("lookingatentity", new LookingAtEntity());
		TooltipsAPI.registerCondition("lookingatmythicmob", new LookingAtMythicMob());
		TooltipsAPI.registerCondition("region", new Region());
		TooltipsAPI.registerCondition("incuboid", new InCuboid());
		TooltipsAPI.registerCondition("location", new Location());
		TooltipsAPI.registerCondition("standingon", new StandingOn());
		TooltipsAPI.registerCondition("itemnbtequals", new ItemNbtEquals());
		TooltipsAPI.registerCondition("entitynbtequals", new EntityNbtEquals());
		TooltipsAPI.registerCondition("tileentitynbtequals", new TileEntityNbtEquals());
		TooltipsAPI.registerCondition("blocknbtequals", new BlockNbtEquals());
		TooltipsAPI.registerCondition("blockstateequals", new BlockStateEquals());
		TooltipsAPI.registerCondition("time", new Time());
		TooltipsAPI.registerCondition("equipped", new Equipped());
		TooltipsAPI.registerCondition("op", new Op());
		TooltipsAPI.registerCondition("lookingatcitizen", new LookingAtCitizen());
		TooltipsAPI.registerCondition("permission", new Permission());
	}
	
	private void loadVariables() {
		final File variablesDirectory = new File(getDataFolder(), ".data/variables");
		Variables.PERSISTENT.load(variablesDirectory);
	}

	private void loadIntegrations() {

		PluginManager pluginManager = Bukkit.getPluginManager();

		// For each IntegratedPlugin, check if enabled on the server
		for (IntegratedPlugin integratedPlugin : IntegratedPlugin.values()) {

			final var bukkitPlugin = pluginManager.getPlugin(integratedPlugin.getName());
			final boolean required = integratedPlugin.isRequired();
			final boolean enabled = bukkitPlugin != null;

			// Required plugin not present
			if (!enabled && required) {
				log(integratedPlugin.getName() + " is required to run Tooltips");
				pluginManager.disablePlugin(this);
				return;
			}

			integratedPlugin.setEnabled(enabled);
		}

		final String preferredFurniturePlugin = getConfig().getString("furniture-plugin", "automatic").toLowerCase();
		final boolean chooseAutomatically = (preferredFurniturePlugin.equals("auto")
				|| preferredFurniturePlugin.equals("automatic"));

		// Load providers for appropriate furniture plugins
		for (IntegratedPlugin furniturePlugin : IntegratedPlugin.FURNITURE_PLUGINS) {

			if (!furniturePlugin.isEnabled()) {
				continue;
			}

			if (chooseAutomatically || preferredFurniturePlugin.equalsIgnoreCase(furniturePlugin.getName())) {

				this.furnitureProvider = switch (furniturePlugin) {
				case ORAXEN -> new OraxenFurnitureProvider();
				case ITEMSADDER -> new ItemsAdderFurnitureProvider();
				case CRUCIBLE -> new CrucibleFurnitureProvider();
				default -> null;
				};

				if (this.furnitureProvider != null) {
					log("Used furniture plugin: " + this.furnitureProvider.getClass().getSimpleName());
					break;
				}

			}
		}

		// Load area provider for appropriate area plugins
		for (IntegratedPlugin areaPlugin : IntegratedPlugin.AREA_PLUGINS) {

			if (!areaPlugin.isEnabled()) {
				continue;
			}

			this.areaProvider = switch (areaPlugin) {
			case WORLDGUARD -> new WorldGuardAreaProvider();
			default -> null;
			};

		}

	}

	private void loadListeners() {
		PluginManager pluginManager = Bukkit.getPluginManager();

		if (this.areaProvider != null) {
			pluginManager.registerEvents(new PlayerMovementListener(this.areaProvider), this);
		}

		this.playerInteractListener = new PlayerInteractListener();
		pluginManager.registerEvents(this.playerInteractListener, this);
		pluginManager.registerEvents(new PlayerConnectionListener(), this);
	}

	private void loadCommands() {
		TooltipsCommand tooltipsCommand = new TooltipsCommand(this);
		tooltipsCommand.register("sendtheme", new SendThemeCommand(this));
		tooltipsCommand.register("sendpreset", new SendPresetCommand(this));
		tooltipsCommand.register("reload", new ReloadCommand(this));
		tooltipsCommand.register("eval", new EvalCommand(this));
		tooltipsCommand.register("vars", new VarsCommand());
		tooltipsCommand.register("listvars", new ListVarsCommand());

		PluginCommand tooltipsPluginCommand = getCommand("tooltips");
		tooltipsPluginCommand.setExecutor(tooltipsCommand);
		tooltipsPluginCommand.setTabCompleter(tooltipsCommand);
	}

	public void reload() {

		// Stop possible previous tooltip runnable
		if (this.runnableManager != null) {
			this.runnableManager.stop();
		}

		this.reloadConfig();

		clearCache();
		fillCache();

		FileSetup.setupFiles(this);

		USE_SPACES = this.getConfig().getBoolean("use-spaces", true);
		final int checkFrequency = this.getConfig().getInt("condition-check-frequency", 3);

		this.schemaManager = new SchemaManager();
		this.iconManager = new IconManager();
		this.themeManager = new ThemeManager();
		this.presetManager = new PresetManager();
		this.tooltipManager = new TooltipManager(this);

		schemaManager.loadFrom(new File(getDataFolder(), ".data/schemas"));
		iconManager.loadFrom(this, FileUtils.getAllConfigsFrom(this, "icons"));
		themeManager.loadFrom(FileUtils.getAllConfigsFrom(this, "themes"));
		presetManager.loadFrom(this, FileUtils.getAllConfigsFrom(this, "presets"));

		Widths.loadCustomWidths(new File(getDataFolder(), ".data/widths.yml"));
		
		addSpaceCharWidth(USE_SPACES);

		PackGenerator packGenerator = new PackGenerator(this);
		packGenerator.registerGenerator(new SpaceGenerator(USE_SPACES));
		packGenerator.registerGenerator(new ThemeGenerator(themeManager));
		packGenerator.registerGenerator(new LineGenerator(schemaManager));
		packGenerator.registerGenerator(new IconGenerator(iconManager));
		packGenerator.registerGenerator(new TextureGenerator());
		packGenerator.generate();

		this.runnableManager = new TooltipRunnableManager(this);
		this.runnableManager.run(this, checkFrequency);

		if (this.playerInteractListener != null)
			playerInteractListener.setRunnableManager(this.runnableManager);

	}

	private void fillCache() {
		Bukkit.getScheduler().runTaskLater(this, () -> {
			if (furnitureProvider != null) {
				FurnitureCache.cacheAll(furnitureProvider.getAllFurniture());
			}
		}, 40L);
	}

	private void clearCache() {
		TextLine.clearReplaceables();
		TooltipCache.clear();
		FurnitureCache.clear();
	}

	private void addSpaceCharWidth(boolean useSpaces) {

		SizedChar space = new SizedChar(' ');

		if (useSpaces) {
			space.setHeight(1);
			space.setAbsoluteWidth(1);
			space.setImageHeight(1);
		} else {
			space.setHeight(1);
			space.setAbsoluteWidth(1);
			space.setImageHeight(1);
		}

		Widths.add(space);

	}

	private void addLocalPlaceholders() {

		if (furnitureProvider != null) {
			Placeholders.addLocal("furniture", new SimplePlaceholderParser((p, s) -> {
				if (!s.equalsIgnoreCase("furniture_id") && !s.equalsIgnoreCase("furniture_name")) {
					return null;
				}

				final boolean name = s.equalsIgnoreCase("furniture_name");

				// Already cached by LookingAtFurniture condition
				if (LookingAtCache.contains(p)) {
					final String cachedId = LookingAtCache.get(p);
					return (name ? Utils.getFurnitureDisplayName(furnitureProvider, cachedId) : cachedId);
				}

				Predicate<Block> blockPredicate = (block -> {
					if (block == null)
						return false;
					return furnitureProvider.isFurniture(block);
				});

				Predicate<Entity> entityFilter = (entity -> {
					if (entity == null)
						return false;
					if (entity.equals(p))
						return false;
					return Tooltips.FURNITURE_ENTITIES.contains(entity.getType());
				});

				var rayTrace = Utils.getRayTrace(p, 10, blockPredicate, entityFilter);

				if (rayTrace == null)
					return null;

				if (rayTrace.getHitBlock() != null) {
					final Block hitBlock = rayTrace.getHitBlock();
					final String id = furnitureProvider.getFurnitureId(hitBlock);
					return (name ? Utils.getFurnitureDisplayName(furnitureProvider, id) : id);
				}

				if (rayTrace.getHitEntity() != null) {
					final Entity hitEntity = rayTrace.getHitEntity();
					final String id = furnitureProvider.getFurnitureId(hitEntity);
					return (name ? Utils.getFurnitureDisplayName(furnitureProvider, id) : id);
				}

				return null;
			}));
		}

		Placeholders.addLocal("var", new SimplePlaceholderParser((p, s) -> {
			if (!s.startsWith("var_"))
				return null;
			boolean global = s.startsWith("var_global_");
			int cutIndex = (global ? 11 : 4);

			String variableName = s.substring(cutIndex);
			variableName = Placeholders.replacePlaceholders(p, variableName);

			Argument returnArgument = null;

			if (global) {
				returnArgument = Variables.LOCAL.getVar(variableName);
			} else {
				returnArgument = Variables.LOCAL.getVar(p, variableName);
			}

			if (returnArgument == null || returnArgument.getAsString() == null)
				return "0";

			return returnArgument.getAsString();
		}));

		Placeholders.addLocal("persistentvar", new SimplePlaceholderParser((p, s) -> {
			if (!s.startsWith("persistentvar_"))
				return null;

			boolean global = s.startsWith("persistentvar_global_");
			int cutIndex = (global ? 21 : 14);

			String variableName = s.substring(cutIndex);
			variableName = Placeholders.replacePlaceholders(p, variableName);

			Argument returnArgument = null;

			if (global) {
				returnArgument = Variables.PERSISTENT.getVar(variableName);
			} else {
				returnArgument = Variables.PERSISTENT.getVar(p, variableName);
			}

			if (returnArgument == null || returnArgument.getAsString() == null)
				return "0";

			return returnArgument.getAsString();
		}));

	}

	// ------------------------------------------------------

	public static Tooltips get() {
		return INSTANCE;
	}

	public ProtocolManager getProtocolManager() {
		return protocolManager;
	}

	public ThemeManager getThemeManager() {
		return themeManager;
	}

	public PresetManager getPresetManager() {
		return presetManager;
	}

	public ConditionManager getConditionManager() {
		return conditionManager;
	}

	public IconManager getIconManager() {
		return iconManager;
	}

	public TooltipManager getTooltipManager() {
		return tooltipManager;
	}

	public FurnitureProvider getFurnitureProvider() {
		return furnitureProvider;
	}

	public static Logger logger() {
		return LOGGER;
	}
	
	public boolean isUseSpaces() {
		return USE_SPACES;
	}

	public static void warn(String message) {
		Messaging.send(Bukkit.getConsoleSender(), ChatColor.RED + "[Tooltips] WARNING: " + message);
	}

	public static void log(String message) {
		Messaging.send(Bukkit.getConsoleSender(), "[Tooltips] " + message);
	}

	public static File getPackAssetsFolder() {
		return new File(INSTANCE.getDataFolder(), "pack/assets");
	}

}
