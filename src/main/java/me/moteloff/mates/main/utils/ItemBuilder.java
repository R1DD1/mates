package me.moteloff.mates.main.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private Material type = Material.ACACIA_BOAT;
    private int amount = 1;
    private Component displayName = Component.text("Предмет");
    private List<Component> lore = new ArrayList<>();
    private Enchantment[] enchantments = new Enchantment[]{};
    private ItemFlag[] itemFlags = new ItemFlag[]{};

    public ItemBuilder type(Material type) {
        this.type = type;
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
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

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(type, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(displayName);
        itemMeta.lore(lore);
        itemMeta.addItemFlags(itemFlags);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
