package me.moteloff.mates.main.utils;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import java.util.Arrays;
import java.util.function.Consumer;

public class LabelsHelper {
    public static void getLabels(Location center, int radius, int[] yValues, String labelName, Consumer<Sign> signConsumer) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        for (int chunkX = -chunkRadius; chunkX <= chunkRadius; chunkX++) {
            for (int chunkZ = -chunkRadius; chunkZ <= chunkRadius; chunkZ++) {
                Chunk chunk = center.getWorld().getChunkAt(center.getBlockX() + (chunkX * 16), center.getBlockZ() + (chunkZ * 16));
                for (BlockState tileEntity : chunk.getTileEntities()) {
                    if (tileEntity instanceof Sign) {
                        Sign sign = (Sign) tileEntity;
                        String[] lines = sign.getLines();
                        if (lines.length > 0 && lines[0].equalsIgnoreCase("-p")) {
                            signConsumer.accept(sign);
                            System.out.println(true);
                        }
                        System.out.println(false);
                    }
                }
            }
        }
    }
}
