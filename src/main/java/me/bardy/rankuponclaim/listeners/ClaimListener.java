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
package me.bardy.rankuponclaim.listeners;

import me.bardy.rankuponclaim.RankupOnClaim;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class ClaimListener implements Listener {

    private static final Pattern CLAIM_COMMAND_PATTERN = Pattern.compile("/?(rg|region) claim(.*)", Pattern.CASE_INSENSITIVE);

    private final RankupOnClaim plugin;

    public ClaimListener(final RankupOnClaim plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClaimCommand(PlayerCommandPreprocessEvent event) {
        if (!CLAIM_COMMAND_PATTERN.matcher(event.getMessage()).matches()) return;
        final Player player = event.getPlayer();

        final User user = plugin.getLuckPerms().getPlayerAdapter(Player.class).getUser(player);
        final InheritanceNode node = InheritanceNode.builder(plugin.getGroupFrom()).build();
        final Collection<Group> groups = user.getInheritedGroups(QueryOptions.nonContextual());

        if (groups.contains(plugin.getGroupFrom())) {
            user.data().remove(node);
            user.data().add(InheritanceNode.builder(plugin.getGroupTo()).build());
            plugin.getLuckPerms().getUserManager().saveUser(user);

            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    Objects.requireNonNull(plugin.getConfig().getString("message", "&aYou are now a Citizen, Welcome Home!")))
            );
        }
    }
}
