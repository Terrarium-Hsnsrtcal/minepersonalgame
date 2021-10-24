package ci.particle.utils;

import org.bukkit.Material;

public final class BlockData extends ParticleData {
	
	public BlockData(Material material, byte data) throws IllegalArgumentException {
		super(material, data);
		
		if (!material.isBlock()) throw new IllegalArgumentException("The material is not a block");
	}
}