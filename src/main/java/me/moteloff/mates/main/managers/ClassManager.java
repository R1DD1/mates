package me.moteloff.mates.main.managers;

import me.moteloff.mates.main.Main;
import me.moteloff.mates.main.utils.Formatting;
import me.moteloff.mates.main.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class ClassManager implements Manager{
    @Override
    public void register() {
        initItems();
    }

    @Override
    public void unregister() {
    }

    private void initItems() {
        ItemStack swordsman = new ItemBuilder(Material.IRON_SWORD)
                .flags(ItemFlag.HIDE_ATTRIBUTES)
                .displayName("swordsman")
                .lore(Formatting.translate("test \ntest").split("\n"))
                .data("rpg_class", "Swordsman")
                .build();
        Main.classInv.addItem(swordsman);
    }
}
