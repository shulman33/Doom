package edu.yu.cs.intro.doomGame;

import java.util.*;

/**
 * Represents a player in the game.
 * A player whose health is <= 0 is dead.
 */

public class Player implements Comparable<Player> {

    /**
     * @param name the player's name
     * @param health the player's starting health level
     */
    private String name;
    private int health;
    private EnumMap<Weapon,Integer> weaponToAmmo;
    private Set<Weapon> weapons;
    private Map<Weapon,Integer> weaponDoesntHaveToAmmo;


    public Player(String name, int health) {
        this.name = name;
        this.health = health;
        this.weaponToAmmo = new EnumMap<>(Weapon.class);
        this.weapons = new HashSet<>();
        this.weaponDoesntHaveToAmmo = new HashMap<>();
        this.addWeapon(Weapon.FIST);
        this.changeAmmunitionRoundsForWeapon(Weapon.FIST,10000000);
    }

    public String getName(){
        return this.name;
    }

    /**
     * does this player have the given weapon?
     * @param w
     * @return
     */
    public boolean hasWeapon(Weapon w){
        return weapons.contains(w);
    }

    /**
     * how much ammunition does this player have for the given weapon?
     * @param w
     * @return
     */
    public int getAmmunitionRoundsForWeapon(Weapon w){
            if(weapons.contains(w)){
                return weaponToAmmo.get(w);
            }
            return weaponDoesntHaveToAmmo.get(w);
    }

    /**
     * Change the ammunition amount by a positive or negative amount
     * @param weapon weapon whose ammunition count is to be changed
     * @param change amount to change ammunition count for that weapon by
     * @return the new total amount of ammunition the player has for the weapon.
     */
    public int changeAmmunitionRoundsForWeapon(Weapon weapon, int change){
        int totalAmount = weaponToAmmo.get(weapon) + change;
        weaponToAmmo.put(weapon,totalAmount);
        return weaponToAmmo.get(weapon);
    }

    /**
     * A player can have ammunition for a weapon even without having the weapon itself.
     * @param weapon weapon for which we are adding ammunition
     * @param rounds number of rounds of ammunition to add
     * @return the new total amount of ammunition the player has for the weapon
     * @throws IllegalArgumentException if rounds < 0 or weapon is null
     * @throws IllegalStateException if the player is dead
     */
    protected int addAmmunition(Weapon weapon, int rounds){
        if(!weapons.contains(weapon)){
            weaponDoesntHaveToAmmo.put(weapon,rounds);
            return weaponDoesntHaveToAmmo.get(weapon);
        }
        if(weapon == null || weaponToAmmo.get(weapon) < 0){
            throw new IllegalArgumentException();
        }
        if(this.health < 0){
            throw new IllegalStateException();
        }
        int totalAmount = weaponToAmmo.get(weapon) + rounds;
        weaponToAmmo.put(weapon,totalAmount);
        return weaponToAmmo.get(weapon);
    }

    /**
     * When a weapon is first added to a player, the player should automatically be given 5 rounds of ammunition.
     * If the player already has the weapon before this method is called, this method has no effect at all.
     * @param weapon
     * @return true if the weapon was added, false if the player already had it
     * @throws IllegalArgumentException if weapon is null
     * @throws IllegalStateException if the player is dead
     */
    protected boolean addWeapon(Weapon weapon){
        if(weapon == null){
            throw new IllegalArgumentException();
        }
        if(this.health < 0){
            throw new IllegalStateException();
        }
        if(!hasWeapon(weapon)){
            weaponToAmmo.put(weapon,5);
            return weapons.add(weapon);
        }
        return false;
    }

    /**
     * Change the player's health level
     * @param amount a positive or negative number, to increase or decrease the player's health
     * @return the player's health level after the change
     * @throws IllegalStateException if the player is dead
     */
    public int changeHealth(int amount){
        if(this.health < 0){
            throw new IllegalStateException();
        }
        return this.health += amount;
    }

    /**
     * set player's current health level to the given level
     * @param amount
     */
    protected void setHealth(int amount){
        this.health = amount;
    }

    /**
     * get the player's current health level
     * @return
     */
    public int getHealth(){
        return this.health;
    }

    /**
     * is the player dead?
     * @return
     */
    public boolean isDead(){
        return this.health <= 0;
    }

    /**
     * Compare criteria, in order:
     * Does one have a greater weapon?
     * If they have the same greatest weapon, who has more ammunition for it?
     * If they are the same on weapon and ammunition, who has more health?
     * If they are the same on greatest weapon, ammunition for it, and health, they are equal.
     * Recall that all enums have a built-in implementation of Comparable, and they compare based on ordinal()
     *
     * @param other
     * @return
     */
    @Override
    public int compareTo(Player other) {
        int playerOneWeapon = 0;
        int playerTwoWeapon = 0;
        int ammoForPlayerOne = 0;
        int ammoForPlayerTwo = 0;
        for(Weapon weapon : this.weapons){

            if(weapon.ordinal() > playerOneWeapon){
                playerOneWeapon = weapon.ordinal();
                ammoForPlayerOne = this.getAmmunitionRoundsForWeapon(weapon);
            }
        }
        for(Weapon weapon : other.weapons){

            if(weapon.ordinal() > playerTwoWeapon){
                playerTwoWeapon = weapon.ordinal();
                ammoForPlayerTwo = other.getAmmunitionRoundsForWeapon(weapon);
            }
        }
        if(playerOneWeapon > playerTwoWeapon){
            return 1;
        }
        if(playerOneWeapon < playerTwoWeapon){
            return -1;
        }
        if(playerOneWeapon == playerTwoWeapon && ammoForPlayerOne > ammoForPlayerTwo){
            return 1;
        }
        if(playerOneWeapon == playerTwoWeapon && ammoForPlayerOne < ammoForPlayerTwo){
            return -1;
        }
        if(playerOneWeapon == playerTwoWeapon && ammoForPlayerOne == ammoForPlayerTwo && this.getHealth() > other.getHealth()){
            return 1;
        }
        if(playerOneWeapon == playerTwoWeapon && ammoForPlayerOne == ammoForPlayerTwo && this.getHealth() < other.getHealth()){
            return -1;
        }
        return 0;




    }

    /**
     * Only equal if it is literally the same player
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    /**
     * @return the hash code of the player's name
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
