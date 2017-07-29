package clickwarp.command;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import clickwarp.ClickWarp;
import clickwarp.PortalManager;
import clickwarp.player.PlayerSelections;
import clickwarp.warps.Warp;
import clickwarp.warps.WarpHolder;

public class ClickWarpCommandExecutor implements CommandExecutor {
	private ClickWarp plugin;
	private WarpHolder warps;
	private PlayerSelections selections;
	private PortalManager portals;
	
	public ClickWarpCommandExecutor(ClickWarp plugin, WarpHolder warps, PlayerSelections selections, PortalManager portals) {
		this.plugin = plugin;
		this.warps = warps;
		this.selections = selections;
		this.portals = portals;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("clickwarp")) {
			switch (args.length) {
			case 1:
				if (args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("clickwarp.reload")) {
						plugin.reloadConfig();
						plugin.reloadWarps();
						sender.sendMessage(ChatColor.GREEN+"[ClickWarp] Reload Complete");
						return true;
					} else {
						sender.sendMessage(ChatColor.DARK_RED+"[ClickWarp] You don't have permission to do this.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("unlink")) {
					if (sender.hasPermission("clickwarp.unlink")) {
						if (!(sender instanceof Player)) {
							sender.sendMessage(ChatColor.DARK_RED+"[ClickWarp] Only players can unlink portals.");
							return true;
						}
						Player player = (Player) sender;
						if (!selections.containsKey(player.getName())) {
							player.sendMessage(ChatColor.DARK_RED+"[ClickWarp] You must select a block to unlink.");
							return true;
						}
						portals.remove(selections.get(player.getName()));
						player.sendMessage(ChatColor.GREEN+"[ClickWarp] Portal removed!");
						return true;
					} else {
						sender.sendMessage(ChatColor.DARK_RED+"[ClickWarp] You don't have permission to do this.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("listwarps")) {
					if (sender.hasPermission("clickwarp.list")) {
						sender.sendMessage(ChatColor.DARK_AQUA+"[ClickWarp] Warps ("+warps.size()+"):");
						for (String key : warps.keySet()) {
							sender.sendMessage(ChatColor.DARK_AQUA+"[ClickWarp] - "+key+" ("+warps.get(key).getName()+")");
						}
						return true;
					} else {
						sender.sendMessage(ChatColor.DARK_RED+"[ClickWarp] You don't have permission to do this.");
						return true;
					}
				} else {
					return false;
				}
			case 2:
				if (args[0].equalsIgnoreCase("linkwarp")) {
					if (sender.hasPermission("clickwarp.link")) {
						if (!(sender instanceof Player)) {
							sender.sendMessage(ChatColor.DARK_RED+"[ClickWarp] Only players can link portals.");
							return true;
						}
						Player player = (Player) sender;
						if (!selections.containsKey(player.getName())) {
							player.sendMessage(ChatColor.DARK_RED+"[ClickWarp] You must select a block to link.");
							return true;
						} else if (selections.get(player.getName()).isEmpty()) {
							player.sendMessage(ChatColor.DARK_RED+"[ClickWarp] You must select a block to link.");
							return true;
						}
						if (portals.containsKey(selections.get(player.getName()))) {
							player.sendMessage(ChatColor.DARK_RED+"[ClickWarp] That block is already linked to a warp.");
							return true;
						}
						if (!warps.containsKey(args[1].toLowerCase())) {
							player.sendMessage(ChatColor.DARK_RED+"[ClickWarp] That warp does not exist.");
							return true;
						}
						portals.put(selections.get(player.getName()), warps.get(args[1].toLowerCase()));
						player.sendMessage(ChatColor.GREEN+"[ClickWarp] Block linked!");
						return true;
					} else {
						sender.sendMessage(ChatColor.DARK_RED+"[ClickWarp] You don't have permission to do this.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("setwarp")) {
					if (sender.hasPermission("clickwarp.setwarp")) {
						if (!(sender instanceof Player)) {
							sender.sendMessage(ChatColor.DARK_RED+"[ClickWarp] Only players can set warps.");
							return true;
						}
						Player p = (Player) sender;
						warps.put(args[1].toLowerCase(), new Warp(args[1].replace('_', ' '), p.getLocation()));
						p.sendMessage(ChatColor.GREEN+"[ClickWarp] Warp set!");
						return true;
					} else {
						sender.sendMessage(ChatColor.DARK_RED+"[ClickWarp] You don't have permission to do this.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("delwarp")) {
					if (sender.hasPermission("clickwarp.setwarp")) {
						warps.remove(args[1].toLowerCase());
						sender.sendMessage(ChatColor.GREEN+"[ClickWarp] Warp removed!");
						return true;
					} else {
						sender.sendMessage(ChatColor.DARK_RED+"[ClickWarp] You don't have permission to do this.");
						return true;
					}
				} else if (args[0].equalsIgnoreCase("goto")) {
					if (sender.hasPermission("clickwarp.goto")) {
						if (!(sender instanceof Player)) {
							sender.sendMessage(ChatColor.DARK_RED+"[ClickWarp] Only players can teleport to warps.");
							return true;
						}
						if (!warps.containsKey(args[1].toLowerCase())) {
							sender.sendMessage(ChatColor.DARK_RED+"[ClickWarp] That warp does not exist.");
							return true;
						}
						Warp warp = warps.get(args[1].toLowerCase());
						warp.teleportPlayer((Player) sender);
						if (plugin.getConfig().getBoolean("showTeleportMessage")) {
							sender.sendMessage(ChatColor.DARK_AQUA+"[ClickWarp] You have been teleported to "+warp.getName()+".");
						}
						return true;
					} else {
						sender.sendMessage(ChatColor.DARK_RED+"[ClickWarp] You don't have permission to do this.");
						return true;						
					}
				} else {
					return false;
				}
			default:
				return false;
			}
		} else {
			return true;
		}
	}

}
