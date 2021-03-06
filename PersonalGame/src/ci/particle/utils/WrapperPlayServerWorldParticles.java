package ci.particle.utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.Particle;

public class WrapperPlayServerWorldParticles extends AbstractPacket {
	
	public static final PacketType TYPE = PacketType.Play.Server.WORLD_PARTICLES;

	public WrapperPlayServerWorldParticles() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}

	public WrapperPlayServerWorldParticles(PacketContainer packet) {
		super(packet, TYPE);
	}

	public Particle getParticleType() {
		return handle.getParticles().read(0);
	}

	public void setParticleType(Particle value) {
		handle.getParticles().write(0, value);
	}

	public float getX() {
		return handle.getFloat().read(0);
	}

	public void setX(float value) {
		handle.getFloat().write(0, value);
	}

	public float getY() {
		return handle.getFloat().read(1);
	}
	public void setY(float value) {
		handle.getFloat().write(1, value);
	}
	public float getZ() {
		return handle.getFloat().read(2);
	}
	public void setZ(float value) {
		handle.getFloat().write(2, value);
	}
	public float getOffsetX() {
		return handle.getFloat().read(3);
	}
	public void setOffsetX(float value) {
		handle.getFloat().write(3, value);
	}
	public float getOffsetY() {
		return handle.getFloat().read(4);
	}
	public void setOffsetY(float value) {
		handle.getFloat().write(4, value);
	}
	public float getOffsetZ() {
		return handle.getFloat().read(5);
	}
	public void setOffsetZ(float value) {
		handle.getFloat().write(5, value);
	}
	public float getParticleData() {
		return handle.getFloat().read(6);
	}
	public void setParticleData(float value) {
		handle.getFloat().write(6, value);
	}
	public int getNumberOfParticles() {
		return handle.getIntegers().read(0);
	}
	public void setNumberOfParticles(int value) {
		handle.getIntegers().write(0, value);
	}
	public boolean getLongDistance() {
		return handle.getBooleans().read(0);
	}
	public void setLongDistance(boolean value) {
		handle.getBooleans().write(0, value);
	}
	public int[] getData() {
		return handle.getIntegerArrays().read(0);
	}
	public void setData(int[] value) {
		handle.getIntegerArrays().write(0, value);
	}
}