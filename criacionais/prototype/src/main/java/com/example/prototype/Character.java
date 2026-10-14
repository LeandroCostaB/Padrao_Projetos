package com.example.prototype;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Character implements Prototype<Character> {

    private String name;
    private final String type;
    private int health;
    private int attack;
    private int defense;
    private String weapon;
    private final List<String> skills;

    public Character(
            String name,
            String type,
            int health,
            int attack,
            int defense,
            String weapon,
            List<String> skills) {
        this.name = Objects.requireNonNull(name, "name não pode ser nulo");
        this.type = Objects.requireNonNull(type, "type não pode ser nulo");
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.weapon = Objects.requireNonNull(weapon, "weapon não pode ser nulo");
        this.skills = new ArrayList<>(Objects.requireNonNull(skills, "skills não pode ser nulo"));
    }

    /** Construtor de cópia. A lista de habilidades recebe uma cópia profunda. */
    private Character(Character prototype) {
        this(
                prototype.name,
                prototype.type,
                prototype.health,
                prototype.attack,
                prototype.defense,
                prototype.weapon,
                prototype.skills);
    }

    @Override
    public Character clone() {
        return new Character(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name não pode ser nulo");
    }

    public String getType() {
        return type;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = Objects.requireNonNull(weapon, "weapon não pode ser nulo");
    }

    /** Retorna uma visão somente de leitura, preservando o encapsulamento da lista. */
    public List<String> getSkills() {
        return List.copyOf(skills);
    }

    public void addSkill(String skill) {
        skills.add(Objects.requireNonNull(skill, "skill não pode ser nula"));
    }

    public void removeSkill(String skill) {
        skills.remove(skill);
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", health=" + health +
                ", attack=" + attack +
                ", defense=" + defense +
                ", weapon='" + weapon + '\'' +
                ", skills=" + skills +
                '}';
    }
}
