package com.crumbed.customMobs;

public class CMMOEntity {

    private String id;
    private String name;
    private int dmg;
    private int str;
    private int def;
    private int hp;
    private float speed;

    public CMMOEntity(String id, String name, int dmg, int str, int def, int hp, float speed) {
        this.id = id;
        this.name = name;
        this.dmg = dmg;
        this.str = str;
        this.def = def;
        this.hp = hp;
        this.speed = speed;
    }

    public float getSpeed()             { return speed; }
    public int getDef()                 { return def; }
    public int getDmg()                 { return dmg; }
    public int getHp()                  { return hp; }
    public int getStr()                 { return str; }
    public String getName()             { return name; }
    public String getId()               { return id; }

    public void setDef(int def)         { this.def = def; }
    public void setDmg(int dmg)         { this.dmg = dmg; }
    public void setHp(int hp)           { this.hp = hp; }
    public void setName(String name)    { this.name = name; }
    public void setSpeed(float speed)   { this.speed = speed; }
    public void setStr(int str)         { this.str = str; }
    public void setId(String id)        { this.id = id; }
}
