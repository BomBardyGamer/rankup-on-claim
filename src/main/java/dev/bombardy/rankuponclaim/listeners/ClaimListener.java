package dev.bombardy.rankuponclaim.listeners;

import dev.bombardy.rankuponclaim.RankupOnClaim;
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
        Player player = event.getPlayer();

        User user = plugin.getLuckPerms().getPlayerAdapter(Player.class).getUser(player);
        InheritanceNode node = InheritanceNode.builder(plugin.getGroupFrom()).build();
        Collection<Group> groups = user.getInheritedGroups(QueryOptions.nonContextual());

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
