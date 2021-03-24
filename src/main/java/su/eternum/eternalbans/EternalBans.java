package su.eternum.eternalbans;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;

public class EternalBans extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getSLF4JLogger().info(ChatColor.DARK_GREEN + "=========================================================");
        this.getSLF4JLogger().info(ChatColor.DARK_GREEN + "|                                                       |");
        this.getSLF4JLogger().info(ChatColor.DARK_GREEN + "|" + ChatColor.AQUA
                + "                      EternalBans                      " + ChatColor.DARK_GREEN + "|");
        this.getSLF4JLogger().info(ChatColor.DARK_GREEN + "|" + ChatColor.AQUA
                + "           Единая система банов от EternumMC           " + ChatColor.DARK_GREEN + "|");
        this.getSLF4JLogger().info(ChatColor.DARK_GREEN + "|                                                       |");
        this.getSLF4JLogger().info(ChatColor.DARK_GREEN + "=========================================================");

        this.getSLF4JLogger().info("Проверка статуса API...");
        CompletableFuture.runAsync(() -> {
           if (EternalBansAPI.isAvailable())
               this.getSLF4JLogger().info("API доступно.");
           else
               this.getSLF4JLogger().info("Что-то пошло не так, мы не можем связаться с API...");
        });
    }

    @Override
    public void onDisable() {
        this.getSLF4JLogger().info(ChatColor.DARK_GREEN + "Отключение EternalBans...");
    }
}
