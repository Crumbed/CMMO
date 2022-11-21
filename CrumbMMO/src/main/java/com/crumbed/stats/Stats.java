package com.crumbed.stats;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class Stats {

    private int health;
    private int baseHealth;
    private int mana;
    private int maxMana;
    private int baseMana;
    private int defense;
    private int baseDefense;
    private int damage;
    private int strength;
    private int critDamage;
    private float critChance;
    private float speed;
    private Stats lastAddStat = null;

    public Stats(boolean blankStat) {
        if (blankStat) {
            baseHealth = 0;
            health = 0;
            baseDefense = 0;
            defense = 0;
            baseMana = 0;
            maxMana = 0;
            mana = 0;
            damage = 0;
            strength = 0;
            critDamage = 0;
            critChance = 0.0f;
            speed = 0.0f;
        } else {
            baseHealth = 100;
            health = 0;
            baseDefense = 0;
            defense = 0;
            baseMana = 100;
            maxMana = 100;
            mana = 100;
            damage = 0;
            strength = 0;
            critDamage = 50;
            critChance = 0.3f;
            speed = 0.1f;
        }
    }
    public Stats(int baseHealth, int baseDefense, int defense, int baseMana, int maxMana, int mana, int dmg, int str, int cDmg, float cChance, float speed) {
        this.baseHealth = baseHealth;
        health = 0;
        this.baseDefense = baseDefense;
        this.defense = defense;
        this.baseMana = baseMana;
        this.maxMana = maxMana;
        this.mana = mana;
        this.damage = dmg;
        this.strength = str;
        this.critDamage = cDmg;
        this.critChance = cChance;
        this.speed = speed;
    }

    public Stats(int health, int defense, int mana, int dmg, int str, int cDmg, float cChance, float speed) {
        baseHealth = 0;
        this.health = health;
        baseDefense = 0;
        this.defense = defense;
        baseMana = 0;
        maxMana = 0;
        this.mana = mana;
        this.damage = dmg;
        this.strength = str;
        this.critDamage = cDmg;
        this.critChance = cChance;
        this.speed = speed;
    }

    public int getBaseHealth()      { return baseHealth; }
    public int getHealth()          { return health; }
    public int getBaseDefense()     { return baseDefense; }
    public int getDefense()         { return defense; }
    public int getBaseMana()        { return baseMana; }
    public int getMaxMana()         { return maxMana; }
    public int getMana()            { return mana; }
    public int getDamage()          { return damage; }
    public int getStrength()        { return strength; }
    public int getCritDamage()      { return critDamage; }
    public float getCritChance()    { return critChance; }
    public float getSpeed()         { return speed; }

    public void setBaseHealth(int health)       { this.baseHealth = health; }
    public void setHealth(int health)           { this.health = health; }
    public void setBaseDefense(int defense)     { this.baseDefense = defense; }
    public void setDefense(int defense)         { this.defense = defense; }
    public void setBaseMana(int mana)           { this.baseMana = mana; }
    public void setMaxMana(int mana)            { this.maxMana = mana; }
    public void setMana(int mana)               { this.mana = mana; }
    public void setDamage(int dmg)              { this.damage = dmg; }
    public void setStrength (int str)           { this.strength = str; }
    public void setCritDamage (int cDmg)        { this.critDamage = cDmg; }
    public void setCritChance (float cChance )  { this.critChance = cChance; }
    public void setSpeed (float speed)          { this.speed = speed; }


    public void addStats (Stats stats, Player player) {
        if(lastAddStat != null)     removeAddStats(lastAddStat, player);
        if (stats == null)          stats = new Stats(true);

        this.health         = stats.getHealth();
        this.maxMana        = this.baseMana + stats.getMana();
        this.defense        = this.baseDefense + stats.getDefense();
        this.damage         += stats.getDamage();
        this.strength       += stats.getStrength();
        this.critDamage     += stats.getCritDamage();
        this.critChance     += stats.getCritChance();
        this.speed          = stats.getSpeed();

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + health);
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() + speed);
        lastAddStat = stats;
    }

    public void removeAddStats (Stats stats, Player player) {

        this.health         = stats.getHealth();
        this.maxMana        = this.baseMana;
        this.defense        = this.baseDefense;
        this.damage         -= stats.getDamage();
        this.strength       -= stats.getStrength();
        this.critDamage     -= stats.getCritDamage();
        this.critChance     -= stats.getCritChance();
        this.speed          = stats.getSpeed();

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() - health);
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() - speed);
    }


}
