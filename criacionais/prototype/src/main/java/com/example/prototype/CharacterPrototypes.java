package com.example.prototype;

import java.util.List;

/** Catálogo central dos modelos de personagens disponíveis no jogo. */
public final class CharacterPrototypes {

    private static final Character WARRIOR = new Character(
            "Warrior Model", "Warrior", 100, 30, 20, "Sword",
            List.of("Power Strike", "Shield Block"));

    private static final Character MAGE = new Character(
            "Mage Model", "Mage", 70, 40, 10, "Staff",
            List.of("Fireball", "Magic Shield"));

    private static final Character ARCHER = new Character(
            "Archer Model", "Archer", 80, 35, 15, "Bow",
            List.of("Precise Shot", "Evasion"));

    private CharacterPrototypes() {
    }

    public static Character newWarrior() {
        return WARRIOR.clone();
    }

    public static Character newMage() {
        return MAGE.clone();
    }

    public static Character newArcher() {
        return ARCHER.clone();
    }

    // Estes métodos retornam cópias para que os modelos internos não sejam alterados.
    public static Character warriorPrototype() {
        return WARRIOR.clone();
    }

    public static Character magePrototype() {
        return MAGE.clone();
    }

    public static Character archerPrototype() {
        return ARCHER.clone();
    }
}
