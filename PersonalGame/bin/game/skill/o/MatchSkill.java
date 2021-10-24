package game.skill.o;

import java.lang.reflect.*;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class MatchSkill {

	public static HashMap<String, Method> METHOD_MAP   = new HashMap<String, Method>();
	public static HashMap<Method, Integer> METHOD_MANA = new HashMap<Method, Integer>();
	
	public static void matchMethods(String key, int mana) {
		METHOD_MAP.put(key, getMethod(key));
		METHOD_MANA.put(getMethod(key), mana);
	}
	
	public static Method getMethodFromMap(String key) {
		return METHOD_MAP.get(key);
	}
	
	public static Method getMethod(String key) {
		Method m = null;
		for(Method me : Skills.class.getDeclaredMethods()) {
			if(!me.getName().equals(key)) continue;
			m = me;
		}
		return m;
	}
	
	public static void setInvoke(String key, Player player){
		
		Method setN = null;
		try {
			setN = Skills.class.getDeclaredMethod(key, Player.class);
		} catch (NoSuchMethodException | SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(!SkillMana.decrease(player, METHOD_MANA.get(setN))) return;
		try {
			setN.invoke(null, player);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
