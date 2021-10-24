package ci.particle.utils;

import org.bukkit.Color;

public final class OrdinaryColor extends ParticleColor {
	
	private final int red;
	private final int green;
	private final int blue;

	public OrdinaryColor(int red, int green, int blue) throws IllegalArgumentException {
		if (red < 0) {
			throw new IllegalArgumentException("The red value is lower than 0");
		}
		if (red > 255) {
			throw new IllegalArgumentException("The red value is higher than 255");
		}
		this.red = red;
		if (green < 0) {
			throw new IllegalArgumentException("The green value is lower than 0");
		}
		if (green > 255) {
			throw new IllegalArgumentException("The green value is higher than 255");
		}
		this.green = green;
		if (blue < 0) {
			throw new IllegalArgumentException("The blue value is lower than 0");
		}
		if (blue > 255) {
			throw new IllegalArgumentException("The blue value is higher than 255");
		}
		this.blue = blue;
	}

	public OrdinaryColor(Color color) {
		this(color.getRed(), color.getGreen(), color.getBlue());
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	@Override
	public float getValueX() {
		return (float) red / 255F;
	}

	@Override
	public float getValueY() {
		return (float) green / 255F;
	}

	@Override
	public float getValueZ() {
		return (float) blue / 255F;
	}
}