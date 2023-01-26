# CrumbSMP Minecraft Plugin

Plugin made for my minecraft plugin that adds MMORPG elements to the game.

# How to Run:

1. You must compile the code to a JAR file.
1. Create a local Minecraft server running the `spigot` or `paper` jar.
1. Add the compiled jar file to the `plugins` folder.
1. Start the server and if you have a numbers above your hotbar, it worked.

# Features:

* Completely custom stats system, health, mana, defense, strength, crit damage, crit chance.
* Custom damage system with damage indicators
* Easily edit vanilla mobs in `plugins/CrumbMMO/CustomMobs.yml`
* Easily add custom items in `plugins/CrumbMMO/CustomItems.yml`

# Custom Items:

With this plugin you can easily add new items to the game by going to `plugins/CrumbMMO/CustomItems.yml`, here is a quick tutorial.

1. Create an ID for your item, this will be the id that the code looks for when running commands, Ex: `god_sword:`
1. Under the ID is where you will define your items attributes, you will indent 2 spaces and then you can enter one of these fields:
  * `name:` (Required) The display name of the item
  * `damage:` The base damage of the item (see Custom Stats section for more info)
  * `strength:` The strength multiplier of the item (see Custom Stats section for more info)
  * `crit-damage:` The damage multiplier on a critical (see Custom Stats section for more info)
  * `crit-chance:` The bonus chance to preform a critical (see Custom Stats section for more info)
  * `health:` The bonus health the item gives (see Custom Stats section for more info)
  * `defense:` The bonus defense the item gives (see Custom Stats section for more info)
  * `mana:` This bonus mana the item gives (see Custom Stats section for more info)
  * `rarity:` (Required) Numbers `0-5` (doesnt do much atm but have plans in the future) in order are [SPECIAL, COMMON, UNCOMMON, RARE, EPIC, LEGENDARY]
  * `type:` (Required) Can be anything, just gives the user a way of putting items into classes
  * `material:` (Required) The normal Minecraft item that will be used, Ex: `DIAMOND_SWORD`
3. After you have made the item you will need to register the ID by going to/adding `registered-ids:` and adding `- itemId` under it
1. Now save your file, go ingame and type: `/creload` if you dont have access to the command, go to the server terminal and type `op <username>`
1. Then you can type `/cgive <itemID>`
 Example of CustomItems.yml: 
  ```
  registered-ids:
  - god_sword
    
  
  god_sword:
    name: God Sword
    damage: 999
    strength: 999
    crit-damage: 999
    crit-chance: 999
    health: 999
    defense: 999
    mana: 999
    rarity: 5
    type: sword
    material: DIAMOND_SWORD
  ```

# Custom Stats:

The plugin adds an entire new stats system to the game, here is what they do:

* `damage` Is the base damage that you will deal without any modifiers
* `strength` Is a multiplier to your damage
* `crit damage` Works the same as `strength` but is only used when you deal a critical hit
* `crit chance` Is the % chance that you will land a critical hit
* `health` I don't need to explain this
* `mana` Works kind of like health but is used for magicial items (Don't really exist yet)

The damage calculation is as follows:
`(5 + damage) * (1 + (strength / 100) * (1 + (crit damage / 100)))`

Defense reduction is:
`incoming-damage * (defense / (defense + 100))`
