package ci.particle.utils;

public final class NoteColor extends ParticleColor {
	private final int note;

	public NoteColor(int note) throws IllegalArgumentException {
		if (note < 0) {
			throw new IllegalArgumentException("The note value is lower than 0");
		}
		if (note > 24) {
			throw new IllegalArgumentException("The note value is higher than 24");
		}
		this.note = note;
	}

	@Override
	public float getValueX() {
		return (float) note / 24F;
	}

	@Override
	public float getValueY() {
		return 0;
	}
	@Override
	public float getValueZ() {
		return 0;
	}

}