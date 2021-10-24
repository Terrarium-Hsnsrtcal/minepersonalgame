package game.main;

import game.character.Characters;
import game.character.CharactersOpt;
import game.character.GameOptSkillMap;
import game.skill.o.MatchSkill;


public class GameOptions {

	public static void setCharacterOpt() {
		
		CharactersOpt.setCharacterSkin(Characters.MECHMEN,    "Ding8888");
		CharactersOpt.setCharacterSkin(Characters.FIREMAD,    "Fire"    );
		CharactersOpt.setCharacterSkin(Characters.SHADOWFEAR, "Shadow"  );
		
		MatchSkill.matchMethods("thunder", 30);
		
		CharactersOpt.setSkillMap(GameOptSkillMap.C_1, 0, "mechtrap");
		CharactersOpt.setSkillMap(GameOptSkillMap.C_1, 1, "thunder" );
		
		CharactersOpt.addCharacter(Characters.MECHMEN,    GameOptSkillMap.C_1);
		CharactersOpt.addCharacter(Characters.FIREMAD,    GameOptSkillMap.C_2);
		CharactersOpt.addCharacter(Characters.SHADOWFEAR, GameOptSkillMap.C_3);
	}
	
}
