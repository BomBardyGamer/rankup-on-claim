/*
 * This project is licensed under the MIT license.
 *
 * Copyright (c) 2021 Callum Seabrook <callum.seabrook@prevarinite.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

        final RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
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
