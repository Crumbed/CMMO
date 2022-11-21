package com.crumbed.abilities;

import java.util.ArrayList;
import java.util.Arrays;

public class Ability {

    public String name;
    public ArrayList<String> description;

    public Ability(String name, String[] description) {
        this.name = name;
        this.description = new ArrayList<>(Arrays.asList(description));
    }

}
