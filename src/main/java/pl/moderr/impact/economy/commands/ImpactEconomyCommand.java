package pl.moderr.impact.economy.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.impact.economy.ImpactEconomy;
import pl.moderr.impact.economy.container.UniversalUserCallback;
import pl.moderr.impact.economy.data.User;
import pl.moderr.impact.economy.util.ColorUtil;

import java.util.Arrays;
import java.util.List;

public class ImpactEconomyCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if(args.length == 0){
            sender.sendMessage(ColorUtil.colorWithPrefix("&7/" + label + " <get|set|add|subtract>"));
            return false;
        }
        if(args[0].equalsIgnoreCase("get")){
            if(args.length >= 2){
                String playerName = args[1];
                ImpactEconomy.getInstance().getUserContainer().getUniversalUser(playerName, new UniversalUserCallback() {
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
                        sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s never played on this server", playerName)));
                    }
                    @Override
                    public void playerNotRegistered() {
                        sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s has not been registered in storage", playerName)));
                    }
                    @Override
                    public void notLoaded() {
                        sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s isn't loaded", playerName)));
                    }
                });
                return true;
            }else{
                sender.sendMessage(ColorUtil.colorWithPrefix("&c/" + label + " get <nickname>"));
                return false;
            }
        }
        if(args[0].equalsIgnoreCase("set")){
            if(args.length >= 3){
                String playerName = args[1];
                long amount;
                try{
                    amount = Long.parseLong(args[2]);
                }catch (Exception ignored){
                    sender.sendMessage(ColorUtil.colorWithPrefix("&cAmount must be an integer!"));
                    return false;
                }
                ImpactEconomy.getInstance().getUserContainer().getUniversalUser(playerName, new UniversalUserCallback() {
                    @Override
                    public void online(Player player, User user) {
                        String tmp = user.formatBalance();
                        user.set(amount);
                        sender.sendMessage(ColorUtil.colorWithPrefix("&aChanged successfully"));
                        sender.sendMessage(ColorUtil.colorWithPrefix("&8[&aONLINE&8] &7" + player.getName() + " &c" + tmp + " &7-> &a" + user.formatBalance()));
                    }
                    @Override
                    public void offline(OfflinePlayer offlinePlayer, User user) {
                        String tmp = user.formatBalance();
                        user.set(amount);
                        ImpactEconomy.getInstance().getStorage().saveUser(user);
                        sender.sendMessage(ColorUtil.colorWithPrefix("&aChanged successfully"));
                        sender.sendMessage(ColorUtil.colorWithPrefix("&8[&cOFFLINE&8] &7" + offlinePlayer.getName() + " &c" + tmp + " &7-> &a" + user.formatBalance()));
                    }
                    @Override
                    public void playerNeverPlayed() {
                        sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s never played on this server", playerName)));
                    }
                    @Override
                    public void playerNotRegistered() {
                        sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s has not been registered in storage", playerName)));
                    }
                    @Override
                    public void notLoaded() {
                        sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s isn't loaded", playerName)));
                    }
                });
                return true;
            }else{
                sender.sendMessage(ColorUtil.colorWithPrefix("&c/" + label + " set <nickname> <amount>"));
                return false;
            }
        }
        if(args[0].equalsIgnoreCase("add")){
            if(args.length >= 3){
                String playerName = args[1];
                long amount;
                try{
                    amount = Long.parseLong(args[2]);
                }catch (Exception ignored){
                    sender.sendMessage(ColorUtil.colorWithPrefix("&cAmount must be an integer!"));
                    return false;
                }
                ImpactEconomy.getInstance().getUserContainer().getUniversalUser(playerName, new UniversalUserCallback() {
                    @Override
                    public void online(Player player, User user) {
                        String tmp = user.formatBalance();
                        user.add(amount);
                        sender.sendMessage(ColorUtil.colorWithPrefix("&aChanged successfully"));
                        sender.sendMessage(ColorUtil.colorWithPrefix("&8[&aONLINE&8] &7" + player.getName() + " &c" + tmp + " &7-> &a" + user.formatBalance()));
                    }
                    @Override
                    public void offline(OfflinePlayer offlinePlayer, User user) {
                        String tmp = user.formatBalance();
                        user.add(amount);
                        ImpactEconomy.getInstance().getStorage().saveUser(user);
                        sender.sendMessage(ColorUtil.colorWithPrefix("&aChanged successfully"));
                        sender.sendMessage(ColorUtil.colorWithPrefix("&8[&cOFFLINE&8] &7" + offlinePlayer.getName() + " &c" + tmp + " &7-> &a" + user.formatBalance()));
                    }
                    @Override
                    public void playerNeverPlayed() {
                        sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s never played on this server", playerName)));
                    }
                    @Override
                    public void playerNotRegistered() {
                        sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s has not been registered in storage", playerName)));
                    }
                    @Override
                    public void notLoaded() {
                        sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s isn't loaded", playerName)));
                    }
                });
                return true;
            }else{
                sender.sendMessage(ColorUtil.colorWithPrefix("&c/" + label + " add <nickname> <amount>"));
                return false;
            }
        }
        if(args[0].equalsIgnoreCase("subtract")){
            if(args.length >= 3){
                String playerName = args[1];
                long amount;
                try{
                    amount = Long.parseLong(args[2]);
                }catch (Exception ignored){
                    sender.sendMessage(ColorUtil.colorWithPrefix("&cAmount must be an integer!"));
                    return false;
                }
                ImpactEconomy.getInstance().getUserContainer().getUniversalUser(playerName, new UniversalUserCallback() {
                    @Override
                    public void online(Player player, User user) {
                        String tmp = user.formatBalance();
                        user.subtract(amount);
                        sender.sendMessage(ColorUtil.colorWithPrefix("&aChanged successfully"));
                        sender.sendMessage(ColorUtil.colorWithPrefix("&8[&aONLINE&8] &7" + player.getName() + " &c" + tmp + " &7-> &a" + user.formatBalance()));
                    }
                    @Override
                    public void offline(OfflinePlayer offlinePlayer, User user) {
                        String tmp = user.formatBalance();
                        user.subtract(amount);
                        ImpactEconomy.getInstance().getStorage().saveUser(user);
                        sender.sendMessage(ColorUtil.colorWithPrefix("&aChanged successfully"));
                        sender.sendMessage(ColorUtil.colorWithPrefix("&8[&cOFFLINE&8] &7" + offlinePlayer.getName() + " &c" + tmp + " &7-> &a" + user.formatBalance()));
                    }
                    @Override
                    public void playerNeverPlayed() {
                        sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s never played on this server", playerName)));
                    }
                    @Override
                    public void playerNotRegistered() {
                        sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s has not been registered in storage", playerName)));
                    }
                    @Override
                    public void notLoaded() {
                        sender.sendMessage(ColorUtil.colorWithPrefix(String.format("&cPlayer %s isn't loaded", playerName)));
                    }
                });
                return true;
            }else{
                sender.sendMessage(ColorUtil.colorWithPrefix("&c/" + label + " subtract <nickname> <amount>"));
                return false;
            }
        }
        if(args[0].equalsIgnoreCase("info")){
            sender.sendMessage(ColorUtil.colorWithPrefix(ImpactEconomy.getInstance().getName() + "-" + ImpactEconomy.VERSION + " by Moderr#4646"));
        }
        return false;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("impact.economy.users.modify"))
            return null;
        if(args.length == 1)
            return Arrays.asList("set", "add", "subtract", "get", "info");
        return null;
    }
}
