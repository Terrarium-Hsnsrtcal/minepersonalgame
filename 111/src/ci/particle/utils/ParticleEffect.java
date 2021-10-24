package ci.particle.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public enum ParticleEffect {

	EXPLOSION_NORMAL("explode", 			0,  -1, 	ParticleProperty.DIRECTIONAL),
	EXPLOSION_LARGE("largeexplode", 		1,  -1),
	EXPLOSION_HUGE("hugeexplosion", 		2,  -1),
	FIREWORKS_SPARK("fireworksSpark", 		3,  -1, 	ParticleProperty.DIRECTIONAL),
	WATER_BUBBLE("bubble", 					4,  -1, 	ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_WATER),
	WATER_SPLASH("splash", 					5,  -1, 	ParticleProperty.DIRECTIONAL),
	WATER_WAKE("wake", 						6,   7, 	ParticleProperty.DIRECTIONAL),
	SUSPENDED("suspended", 					7,  -1, 	ParticleProperty.REQUIRES_WATER),
	SUSPENDED_DEPTH("depthSuspend",		 	8,  -1, 	ParticleProperty.DIRECTIONAL),
	CRIT("crit", 							9,  -1, 	ParticleProperty.DIRECTIONAL),
	CRIT_MAGIC("magicCrit", 				10, -1, 	ParticleProperty.DIRECTIONAL),
	SMOKE_NORMAL("smoke", 					11, -1, 	ParticleProperty.DIRECTIONAL),
	SMOKE_LARGE("largesmoke", 				12, -1, 	ParticleProperty.DIRECTIONAL),
	SPELL("spell", 							13, -1),
	SPELL_INSTANT("instantSpell", 			14, -1),
	SPELL_MOB("mobSpell", 					15, -1, 	ParticleProperty.COLORABLE),
	SPELL_MOB_AMBIENT("mobSpellAmbient", 	16, -1, 	ParticleProperty.COLORABLE),
	SPELL_WITCH("witchMagic", 				17, -1),
	DRIP_WATER("dripWater", 				18, -1),
	DRIP_LAVA("dripLava", 					19, -1),
	VILLAGER_ANGRY("angryVillager", 		20, -1),
	VILLAGER_HAPPY("happyVillager", 		21, -1, 	ParticleProperty.DIRECTIONAL),
	TOWN_AURA("townaura", 					22, -1, 	ParticleProperty.DIRECTIONAL),
	NOTE("note", 							23, -1, 	ParticleProperty.COLORABLE),
	PORTAL("portal", 						24, -1, 	ParticleProperty.DIRECTIONAL),
	ENCHANTMENT_TABLE("enchantmenttable", 	25, -1, 	ParticleProperty.DIRECTIONAL),
	FLAME("flame", 							26, -1, 	ParticleProperty.DIRECTIONAL),
	LAVA("lava", 							27, -1),
	FOOTSTEP("footstep", 					28, -1),
	CLOUD("cloud", 							29, -1, 	ParticleProperty.DIRECTIONAL),
	REDSTONE("reddust", 					30, -1, 	ParticleProperty.COLORABLE),
	SNOWBALL("snowballpoof", 				31, -1),
	SNOW_SHOVEL("snowshovel", 				32, -1, 	ParticleProperty.DIRECTIONAL),
	SLIME("slime", 							33, -1),
	HEART("heart", 							34, -1),
	BARRIER("barrier", 						35,  8),
	ITEM_CRACK("iconcrack", 				36, -1, 	ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
	BLOCK_CRACK("blockcrack", 				37, -1, 	ParticleProperty.REQUIRES_DATA),
	BLOCK_DUST("blockdust", 				38,  7, 	ParticleProperty.DIRECTIONAL, ParticleProperty.REQUIRES_DATA),
	
	WATER_DROP("droplet", 					39,  8),
	ITEM_TAKE("take", 						40,  8),
	MOB_APPEARANCE("mobappearance", 		41,  8);

	private final String name;
	private final int id;
	private final int requiredVersion;
	private final List<ParticleProperty> properties;

	private ParticleEffect(String name, int id, int requiredVersion, ParticleProperty... properties) {
		this.name = name;
		this.id = id;
		this.requiredVersion = requiredVersion;
		this.properties = Arrays.asList(properties);
	}

	public String getName() {
		return name;
	}
	public int getId() {
		return id;
	}
	public int getRequiredVersion() {
		return requiredVersion;
	}
	public boolean hasProperty(ParticleProperty property) {
		return properties.contains(property);
	}
	public boolean isSupported() {
		if (this.requiredVersion == -1) return true;
		
		return UtilServer.is1_8() && this.requiredVersion == 8;
	}
	private static boolean isWater(Location location) {
		Material material = location.getBlock().getType();
		return material == Material.WATER || material == Material.STATIONARY_WATER;
	}
	private static boolean isLongDistance(Location location, List<Player> players) {
		String world = location.getWorld().getName();
		for (Player player : players) {
			Location playerLocation = player.getLocation();
			if (!world.equals(playerLocation.getWorld().getName()) || playerLocation.distanceSquared(location) < 65536) {
				continue;
			}
			return true;
		}
		return false;
	}
	
	public static ParticleEffect fromName(String name) {
		for (ParticleEffect effect : ParticleEffect.values()) {
			if (effect.name().equalsIgnoreCase(name)) {
				return effect;
			}
		}
		return null;
	}
	public static ParticleEffect fromId(int id) {
		for (ParticleEffect effect : ParticleEffect.values()) {
			if (effect.getId() == id) {
				return effect;
			}
		}
		return null;
	}
	
	public boolean isWaterSupport(Location center) {
		return hasProperty(ParticleProperty.REQUIRES_WATER) && isWater(center);
	}
	
	public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) {
		new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, range > 256, null, center).sendTo(center, range);
	}
	public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players) {
		new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, isLongDistance(center, players), null, center).sendTo(players);
	}

	public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, Player... players) {
		display(offsetX, offsetY, offsetZ, speed, amount, center, Arrays.asList(players));
	}

	public void display(Vector direction, float speed, Location center, double range) {
		new ParticlePacket(this, direction, speed, range > 256, null, center).sendTo(center, range);
	}

	public void display(Vector direction, float speed, Location center, List<Player> players) {
		new ParticlePacket(this, direction, speed, isLongDistance(center, players), null, center).sendTo(players);
	}

	public void display(Vector direction, float speed, Location center, Player... players) {
		display(direction, speed, center, Arrays.asList(players));
	}

	public void display(ParticleColor color, Location center, double range){
		new ParticlePacket(this, color, range > 256, center).sendTo(center, range);
	}

	public void display(ParticleColor color, Location center, List<Player> players) {
		new ParticlePacket(this, color, isLongDistance(center, players), center).sendTo(players);
	}

	public void display(ParticleColor color, Location center, Player... players) {
		display(color, center, Arrays.asList(players));
	}

	public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, double range) {
		new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, range > 256, data, center).sendTo(center, range);
	}

	public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, List<Player> players){
		new ParticlePacket(this, offsetX, offsetY, offsetZ, speed, amount, isLongDistance(center, players), data, center).sendTo(players);
	}

	public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center, Player... players) {
		display(data, offsetX, offsetY, offsetZ, speed, amount, center, Arrays.asList(players));
	}

	public void display(ParticleData data, Vector direction, float speed, Location center, double range) {
		new ParticlePacket(this, direction, speed, range > 256, data, center).sendTo(center, range);
	}

	public void display(ParticleData data, Vector direction, float speed, Location center, List<Player> players) {
		new ParticlePacket(this, direction, speed, isLongDistance(center, players), data, center).sendTo(players);
	}

	public void display(ParticleData data, Vector direction, float speed, Location center, Player... players){
		display(data, direction, speed, center, Arrays.asList(players));
	}

}