package team.unnamed.uracle.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.unnamed.uracle.Uracle;
import team.unnamed.uracle.UraclePlugin;
import team.unnamed.uracle.resourcepack.ResourcePack;

public class UracleCommand implements CommandExecutor {

    private final UraclePlugin plugin;
    private final Uracle uracle;

    public UracleCommand(UraclePlugin plugin, Uracle uracle) {
        this.plugin = plugin;
        this.uracle = uracle;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        // check if sender has permission to do this
        if (!sender.hasPermission("uracle") && !sender.isOp()) {
            // no permission and no server operator
            sender.sendMessage(plugin.getMessage("permission"));
            return true;
        }

        // check arguments length
        if (args.length <= 0) {
            // invalid command usage, send help (message)
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload": {
                // reload configuration (these messages are only seen by administrators,
                // so there is no necessity to make them configurable, but anyways, TODO)
                // these messages are important because UX
                sender.sendMessage(ChatColor.LIGHT_PURPLE + "Reloading...");
                long start = System.currentTimeMillis();
                plugin.loadConfiguration();
                long time = System.currentTimeMillis() / start;
                sender.sendMessage(String.format(
                        "%sSuccessfully reloaded configuration in %.2f second(s)",
                        ChatColor.LIGHT_PURPLE,
                        time / 1000F
                ));
                break;
            }

            case "apply": {
                ResourcePack pack = plugin.getPack();

                if (pack == null) {
                    // no resource pack to apply
                    sender.sendMessage(ChatColor.RED + "No resource-pack to apply");
                    break;
                }

                int count = 0;
                if (args.length == 1) {
                    // no more arguments, select everyone
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        uracle.getSender().send(player, pack);
                        count++;
                    }
                } else {
                    // add all next players if they exist
                    for (int i = 1; i < args.length; i++) {
                        Player player = Bukkit.getPlayerExact(args[i]);
                        if (player != null) {
                            uracle.getSender().send(player, pack);
                            count++;
                        }
                    }
                }

                sender.sendMessage(String.format(
                        "%sSuccessfully sent resource-pack update to %s players",
                        ChatColor.LIGHT_PURPLE,
                        count
                ));
                return true;
            }

            default: {
                sendHelp(sender);
                return true;
            }
        }

        return true;
    }

    private void sendHelp(CommandSender receiver) {
        receiver.sendMessage(plugin.getMessage("help"));
    }

}
