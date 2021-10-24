package ci.particle.utils;

import org.bukkit.Bukkit;

public class UtilServer {
	public static String getBukkitVersion() {
		String[] v = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3].split("-")[0]
				.split("_");
		return v[0].replace("v", "") + "." + v[1];
	}

	public static boolean is1_8() {
		return bukkitVersion == 1.8;
	}

	public static Double bukkitVersion = 1.8;

	static {
		String bukVer = getBukkitVersion();
		if (bukVer.startsWith("1.8")) {
			bukkitVersion = 1.8;
		} else if (bukVer.startsWith("1.9")) {
			bukkitVersion = 1.9;
		} else if (bukVer.startsWith("1.10")) {
			bukkitVersion = 1.10;
		} else if (bukVer.startsWith("1.11")) {
			bukkitVersion = 1.11;
		} else if (bukVer.startsWith("1.12")) {
			bukkitVersion = 1.12;
		} else if (bukVer.startsWith("1.13")) {
			bukkitVersion = 1.13;
		} else if (bukVer.startsWith("1.14")) {
			bukkitVersion = 1.14;
		}
	}
}
