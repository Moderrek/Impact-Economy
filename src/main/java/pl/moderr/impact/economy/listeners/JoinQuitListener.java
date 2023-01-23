package pl.moderr.impact.economy.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import pl.moderr.impact.economy.ImpactEconomy;
import pl.moderr.impact.economy.container.UnknownUserException;
import pl.moderr.impact.economy.container.UserIsLoadedException;
import pl.moderr.impact.economy.util.ColorUtil;

import java.util.UUID;

public class JoinQuitListener implements Listener {
    @EventHandler
    public void onJoin(@NotNull PlayerJoinEvent e){
        Player player = e.getPlayer();
        UUID id = player.getUniqueId();
        if(!ImpactEconomy.getInstance().getUserContainer().isUserLoaded(id)){
            try {
                ImpactEconomy.getInstance().getUserContainer().loadUser(id, true, null);
            } catch (UserIsLoadedException | UnknownUserException ex) {
                ex.printStackTrace();
                player.kickPlayer(ColorUtil.color("&cThere was a problem on our part, we're sorry. Try again."));
                e.setJoinMessage(null);
            }
        }
    }
    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent e){
        Player player = e.getPlayer();
        UUID id = player.getUniqueId();
        if(ImpactEconomy.getInstance().getUserContainer().isUserLoaded(id)){
            try {
                ImpactEconomy.getInstance().getUserContainer().unloadUser(id, true);
            } catch (UnknownUserException ignored) {}
        }else{
            e.setQuitMessage(null);
        }
    }
}
