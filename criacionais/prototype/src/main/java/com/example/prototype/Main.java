package com.example.prototype;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        Character warrior = CharacterPrototypes.newWarrior();
        warrior.setName("Arthos");

        Character anotherWarrior = CharacterPrototypes.newWarrior();
        anotherWarrior.setName("Brakus");
        anotherWarrior.setWeapon("Battle Axe");
        anotherWarrior.addSkill("Berserk");

        Character mage = CharacterPrototypes.newMage();
        mage.setName("Merlin");
        mage.setWeapon("Ancient Wand");

        Character archer = CharacterPrototypes.newArcher();
        archer.setName("Lyra");
        archer.addSkill("Rain of Arrows");

        System.out.println("=== Personagens clonados ===");
        System.out.println(warrior);
        System.out.println(anotherWarrior);
        System.out.println(mage);
        System.out.println(archer);

        System.out.println("\n=== Protótipos preservados ===");
        System.out.println(CharacterPrototypes.warriorPrototype());
        System.out.println(CharacterPrototypes.magePrototype());
        System.out.println(CharacterPrototypes.archerPrototype());

        verifyDeepCopy(anotherWarrior);
    }

    private static void verifyDeepCopy(Character modifiedWarrior) {
        Character originalWarrior = CharacterPrototypes.warriorPrototype();

        if (originalWarrior.getSkills().contains("Berserk")) {
            throw new IllegalStateException("A lista de habilidades foi compartilhada!");
        }
        if (!modifiedWarrior.getSkills().contains("Berserk")) {
            throw new IllegalStateException("A habilidade não foi adicionada ao clone!");
        }

        System.out.println("\nCópia profunda verificada: o protótipo não possui a habilidade Berserk.");
    }
}
