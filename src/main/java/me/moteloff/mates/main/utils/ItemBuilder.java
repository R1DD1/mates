package me.moteloff.mates.main.utils;

import me.moteloff.mates.main.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemBuilder {
    private final Main plugin = Main.getInstance();

    private ItemStack itemStack;
    private Component displayName = Component.text("Предмет");
    private List<Component> lore = new ArrayList<>();
    private Enchantment[] enchantments = new Enchantment[]{};
    private ItemFlag[] itemFlags = new ItemFlag[]{};

    public ItemBuilder(Material type, int amount) {
        itemStack = new ItemStack(type, amount);
    }

    public ItemBuilder(Material type) {
        itemStack = new ItemStack(type);
    }

    public ItemBuilder displayName(String string) {
        this.displayName = Component.text(Formatting.translate(string));
        return this;
    }

    public ItemBuilder displayName(Component component) {
        this.displayName = component;
        return this;
    }

    public ItemBuilder flags(ItemFlag... flag) {
        this.itemFlags = flag;
        return this;
    }

    public ItemBuilder lore(String[] lore) {
        for (String str: lore) {
            this.lore.add(Component.text(Formatting.translate(str)));
        }
        return this;
    }

    public ItemBuilder data(String key, String value) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        container.set(new NamespacedKey(plugin, key), PersistentDataType.STRING, value);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack build() {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(displayName);
        itemMeta.lore(lore);
        itemMeta.addItemFlags(itemFlags);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
