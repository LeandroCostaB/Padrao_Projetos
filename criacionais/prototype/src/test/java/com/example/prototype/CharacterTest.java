package com.example.prototype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CharacterTest {

    @Test
    void cloneHasTheSameValuesButIsANewInstance() {
        Character prototype = CharacterPrototypes.warriorPrototype();
        Character clone = prototype.clone();

        assertNotSame(prototype, clone);
        assertEquals(prototype.getName(), clone.getName());
        assertEquals(prototype.getType(), clone.getType());
        assertEquals(prototype.getHealth(), clone.getHealth());
        assertEquals(prototype.getAttack(), clone.getAttack());
        assertEquals(prototype.getDefense(), clone.getDefense());
        assertEquals(prototype.getWeapon(), clone.getWeapon());
        assertEquals(prototype.getSkills(), clone.getSkills());
    }

    @Test
    void changingCloneDoesNotChangePrototype() {
        Character prototype = CharacterPrototypes.warriorPrototype();
        Character clone = prototype.clone();

        clone.setName("Brakus");
        clone.setWeapon("Battle Axe");
        clone.addSkill("Berserk");

        assertEquals("Warrior Model", prototype.getName());
        assertEquals("Sword", prototype.getWeapon());
        assertFalse(prototype.getSkills().contains("Berserk"));
        assertTrue(clone.getSkills().contains("Berserk"));
    }

    @Test
    void exposedSkillsCannotBeChangedWithoutCharacterMethods() {
        Character character = CharacterPrototypes.newMage();

        assertThrows(
                UnsupportedOperationException.class,
                () -> character.getSkills().add("Unexpected skill"));
    }
}
