package edu.yu.cs.intro.doomGame;

import java.util.HashMap;
import java.util.Map;

/**
 * A specific monster
 */
public class Monster implements Comparable<Monster>{
    /**
     * create a monster with no customr protectors; its protectors will be determined by its MonsterType
     * @param type the type of monster to create
     */
    private MonsterType monsterType;
    private MonsterType customProtectedBy;
    private Map<Monster,Room> monsterToRoom;
    private int monsterHealth;
    private boolean dead;
    private Room room;

    protected Monster(MonsterType type){
        monsterToRoom = new HashMap<>();
        this.monsterHealth = type.ammunitionCountNeededToKill;
        this.monsterType = type;
    }
    /**
     * create a monster with a custom protector, i.e. a different protector than the one specified in its MonsterType
     * @param type
     * @param customProtectedBy
     */
    public Monster(MonsterType type, MonsterType customProtectedBy){
            this(type);
            switch (monsterType){
                case IMP:
                    this.customProtectedBy = customProtectedBy;
                    break;
                case DEMON:
                    this.customProtectedBy = customProtectedBy;
                    break;
                case SPECTRE:
                    this.customProtectedBy = customProtectedBy;
                    break;
                case BARON_OF_HELL:
                    this.customProtectedBy = customProtectedBy;
                    break;
            }
    }

    /**
     * set the room that the Monster is located in
     * @param room
     */
    protected void setRoom(Room room){
        this.room = room;
    }

    public MonsterType getMonsterType(){
        return this.monsterType;
    }

    /**
     * Attack this monster with the given weapon, firing the given number of rounds at it
     * @param weapon
     * @param rounds
     * @return indicates if the monster is dead after this attack
     * @throws IllegalArgumentException if the weapon is one that dones't hurt this monster, if the weapon is null, or if rounds < 1
     * @throws IllegalStateException if the monster is already dead
     */
    protected boolean attack(Weapon weapon, int rounds){
        if(isDead()){
            throw new IllegalStateException();
        }
        if(weapon == null || this.getMonsterType().weaponNeededToKill.ordinal() > weapon.ordinal() || rounds < 1){
            throw new IllegalArgumentException();
        }
        if(rounds >= this.monsterHealth){

            this.dead = true;
            return true;
        }else{
            this.monsterHealth -= rounds;
        }
        return false;
    }

    /**
     * @return is this monster dead?
     */
    public boolean isDead(){
        return this.dead;
    }

    /**
     * if this monster has its customProtectedBy set, return it. Otherwise, return the protectedBy of this monster's type
     * @return
     */
    public MonsterType getProtectedBy(){
        if(this.customProtectedBy == null){
            return monsterType.getProtectedBy();
        }
        return this.customProtectedBy;
    }

    /**
     * Used to sort a set of monsters into the order in which they must be killed, assuming they are in the same room.
     * If the parameter refers to this monster, return 0
     * If this monster is protected by the other monster's type, return 1
     * If this monster's type protects the other monster, return -1
     * If this monster's ordinal is < the other's, return -1
     * If this monster's ordinal is > the other's, retuen 1
     * If(this.hashCode() < other.hashCode()), then return -1
     * Otherwise, return 1
     * @param other the other monster
     * @return see above
     */
    @Override
    public int compareTo(Monster other) {
        if(other == this){
            return 0;
        }else if(this.getProtectedBy() == other.getMonsterType()){
            return 1;
        }else if(other.getProtectedBy() == this.getMonsterType()){
            return -1;
        }else if(this.getMonsterType().ordinal() < other.getMonsterType().ordinal()){
            return -1;
        }else if(this.getMonsterType().ordinal() > other.getMonsterType().ordinal()){
            return 1;
        }else if(this.hashCode() < other.hashCode()){
            return -1;
        }
        return 1;
    }
}