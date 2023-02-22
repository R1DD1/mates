package me.moteloff.mates.main.utils.entitybuilder;

import me.moteloff.mates.main.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class EntityBuilder {

    private Main plugin = Main.getInstance();
    private EntityType entityType;
    private double health;
    private double damage;
    private ItemStack weapon;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private double speed;
    private Consumer<EntityDamageByEntityEvent> onHit;
    private Consumer<EntityDamageEvent> onDamaged;

    public EntityBuilder(EntityType entityType) {
        this.entityType = entityType;
    }

    public EntityBuilder setHealth(double health) {
        this.health = health;
        return this;
    }

    public EntityBuilder setDamage(double damage) {
        this.damage = damage;
        return this;
    }

    public EntityBuilder setWeapon(ItemStack weapon) {
        this.weapon = weapon;
        return this;
    }

    public EntityBuilder setHelmet(ItemStack helmet) {
        this.helmet = helmet;
        return this;
    }

    public EntityBuilder setChestplate(ItemStack chestplate) {
        this.chestplate = chestplate;
        return this;
    }

    public EntityBuilder setLeggings(ItemStack leggings) {
        this.leggings = leggings;
        return this;
    }

    public EntityBuilder setBoots(ItemStack boots) {
        this.boots = boots;
        return this;
    }

    public EntityBuilder setSpeed(double speed) {
        this.speed = speed;
        return this;
    }

    public EntityBuilder onHit(Consumer<EntityDamageByEntityEvent> onHit) {
        this.onHit = onHit;
        return this;
    }

    public EntityBuilder onDamaged(Consumer<EntityDamageEvent> onDamaged) {
        this.onDamaged = onDamaged;
        return this;
    }

    public LivingEntity spawnEntity(Location location) {
        LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
        entity.setHealth(health);
        entity.setCustomName(entityType.toString());
        entity.setCustomNameVisible(true);
        entity.getEquipment().setItemInMainHand(weapon);
        entity.getEquipment().setHelmet(helmet);
        entity.getEquipment().setChestplate(chestplate);
        entity.getEquipment().setLeggings(leggings);
        entity.getEquipment().setBoots(boots);
        entity.getAttribute(org.bukkit.attribute.Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(damage);
        entity.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);


        if (onHit != null && onDamaged != null) {
            plugin.getServer().getPluginManager().registerEvents(new CustomEntityListener(onHit, onDamaged), plugin);
        }

        return entity;
    }

    public static EntityBuilder testZombie() {
        return new EntityBuilder(EntityType.ZOMBIE)
                .setHealth(20)
                .setDamage(3)
                .setHelmet(new ItemStack(Material.IRON_HELMET))
                .setChestplate(new ItemStack(Material.IRON_CHESTPLATE))
                .setLeggings(new ItemStack(Material.IRON_LEGGINGS))
                .setBoots(new ItemStack(Material.IRON_BOOTS))
                .setWeapon(new ItemStack(Material.IRON_SWORD))
                .setSpeed(0.23);
    }

    public static EntityBuilder testSkeleton() {
        return new EntityBuilder(EntityType.SKELETON)
                .setHealth(20)
                .setDamage(3)
                .setHelmet(new ItemStack(Material.IRON_HELMET))
                .setChestplate(new ItemStack(Material.IRON_CHESTPLATE))
                .setLeggings(new ItemStack(Material.IRON_LEGGINGS))
                .setBoots(new ItemStack(Material.IRON_BOOTS))
                .setWeapon(new ItemStack(Material.BOW))
                .setSpeed(0.25);
    }
}