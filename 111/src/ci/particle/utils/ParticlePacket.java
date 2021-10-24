package ci.particle.utils;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.comphenix.protocol.wrappers.EnumWrappers.Particle;


public final class ParticlePacket {

	private final ParticleEffect effect;
	
	private float offsetX;
	private float offsetY;
	private float offsetZ;
	
	private float speed;
	private int amount;
	private boolean longDistance;
	
	private final ParticleData data;
	private WrapperPlayServerWorldParticles packet;

	public ParticlePacket(ParticleEffect effect, float offsetX, float offsetY, float offsetZ, float speed, int amount, boolean longDistance, ParticleData data, Location center){
		if (speed < 0) throw new IllegalArgumentException("The speed is lower than 0");
		if (amount < 0) throw new IllegalArgumentException("The amount is lower than 0");
		
		this.effect = effect;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.speed = speed;
		this.amount = amount;
		this.longDistance = longDistance;
		this.data = data;
		
		createPacket(center);
	}

	public ParticlePacket(ParticleEffect effect, Vector direction, float speed, boolean longDistance, ParticleData data, Location center) {
		this(effect, (float) direction.getX(), (float) direction.getY(), (float) direction.getZ(), speed, 0, longDistance, data, center);
	}

	public ParticlePacket(ParticleEffect effect, ParticleColor color, boolean longDistance, Location center) {
		this(effect, color.getValueX(), color.getValueY(), color.getValueZ(), 1, 0, longDistance, null, center);
		
		if (effect == ParticleEffect.REDSTONE && color instanceof OrdinaryColor && ((OrdinaryColor) color).getRed() == 0) {
			offsetX = Float.MIN_NORMAL;
		}
	}

	public void createPacket(Location center) {
		this.packet = new WrapperPlayServerWorldParticles();
		
		this.packet.setParticleType(Particle.valueOf(this.effect.name()));
		
		if (this.data != null) {
			int[] packetData = data.getPacketData();
			
			this.packet.setData(effect == ParticleEffect.ITEM_CRACK ? packetData : new int[] { packetData[0] | (packetData[1] << 12) });
		}

		this.packet.setX((float) center.getX());
		this.packet.setY((float) center.getY());
		this.packet.setZ((float) center.getZ());
		
		this.packet.setOffsetX(this.offsetX);
		this.packet.setOffsetY(this.offsetY);
		this.packet.setOffsetZ(this.offsetZ);

		this.packet.setParticleData(this.speed);
		this.packet.setNumberOfParticles(this.amount);
		this.packet.setLongDistance(this.longDistance);
	}

	public void sendTo(Player player) {
		this.packet.sendPacket(player);
	}

	public void sendTo(List<Player> players) {
		players.forEach(player -> sendTo(player));
	}
	public void sendTo(Location center, double radius) {
		if (radius < 1) return;
		
		World world = center.getWorld();
		
		for (Player player : world.getPlayers()) {
			if (player.getLocation().distance(center) > radius) continue;
			
			sendTo(player);
		}
	}
}