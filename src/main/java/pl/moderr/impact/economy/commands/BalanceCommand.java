package pl.moderr.impact.economy.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.impact.economy.ImpactEconomy;
import pl.moderr.impact.economy.container.UniversalUserCallback;
import pl.moderr.impact.economy.container.UnknownUserException;
import pl.moderr.impact.economy.data.User;
import pl.moderr.impact.economy.util.ColorUtil;

public class BalanceCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length >= 1 && sender.hasPermission("impact.economy.users.view")){
            String username = args[0];
            ImpactEconomy.getInstance().getUserContainer().getUniversalUser(username, new UniversalUserCallback() {
                @Override
                public void online(Player player, User user) {
                    sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&8[&aONLINE&8] &a%s balance: " + user.formatBalance(), player.getName())));
                }
                @Override
                public void offline(OfflinePlayer offlinePlayer, User user) {
                    sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&8[&cOFFLINE&8] &a%s balance: " + user.formatBalance(), offlinePlayer.getName())));
                }
                @Override
                public void playerNeverPlayed() {
                    sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s never played on this server", username)));
                }
                @Override
                public void playerNotRegistered() {
                    sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s has not been registered in storage", username)));
                }
                @Override
                public void notLoaded() {
                    sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s isn't loaded", username)));
                }
            });
            return true;
        }
        if(sender instanceof ConsoleCommandSender){
            sender.sendMessage("Console doesn't have balance! Usage: " + label + " <nickname>");
            return false;
        }
        if(sender instanceof Player p){
            if(ImpactEconomy.getInstance().getUserContainer().isUserLoaded(p.getUniqueId())){
                User user;
                try {
                    user = ImpactEconomy.getInstance().getUserContainer().getUser(p.getUniqueId());
                } catch (UnknownUserException e) { return false; }
                p.sendMessage(ColorUtil.colorWithPrefix(String.format("&aBalance: %s", user.formatBalance())));
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.5f,1);
                return true;
            }
            p.sendMessage(ColorUtil.colorWithPrefix("&cCannot get your balance, please re login"));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO,0.5f, 1);
        }
        return false;
    }
}
