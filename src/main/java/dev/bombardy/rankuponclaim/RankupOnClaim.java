package dev.bombardy.rankuponclaim;

import dev.bombardy.rankuponclaim.listeners.ClaimListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class RankupOnClaim extends JavaPlugin {

    private LuckPerms luckPerms;

    private Group groupFrom;
    private Group groupTo;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        if (!Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            getLogger().severe("LuckPerms not found! This plugin cannot work without LuckPerms! Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (luckPermsProvider == null) {
            getLogger().severe("For some reason, I couldn't find the LuckPerms registration.");
            return;
        }

        luckPerms = luckPermsProvider.getProvider();

        groupFrom = luckPerms.getGroupManager().getGroup(Objects.requireNonNull(getConfig().getString("group_from", "member")));
        groupTo = luckPerms.getGroupManager().getGroup(Objects.requireNonNull(getConfig().getString("group_to", "citizen")));

        Bukkit.getPluginManager().registerEvents(new ClaimListener(this), this);
    }

    public LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public Group getGroupFrom() {
        return groupFrom;
    }

    public Group getGroupTo() {
        return groupTo;
    }
}
