package clickwarp.listener;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import clickwarp.ClickWarp;
import clickwarp.PortalManager;
import clickwarp.player.PlayerSelections;
import clickwarp.warps.Warp;

public class ClickWarpListener implements Listener {
	private ClickWarp plugin;
	private PlayerSelections selections;
	private PortalManager portals;

	public ClickWarpListener(ClickWarp plugin, PlayerSelections selections, PortalManager portals) {
		this.plugin = plugin;
		this.selections = selections;
		this.portals = portals;
	}
	
	@EventHandler
	public void onBlockBreak (BlockBreakEvent e) {
		Block b = e.getBlock();
		if (portals.containsKey(b)) {
			if (e.getPlayer().hasPermission("clickwarp.unlink"))
				portals.remove(b);
			else {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.DARK_RED+"[ClickWarp] This block is a portal.  You can't destroy portals.");
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract (PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		Material key = asMaterial(plugin.getConfig().getString("key"));
		if (e.getPlayer().getItemInHand().getType() == key && e.getPlayer().hasPermission("clickwarp.select")) {
			selections.put(e.getPlayer().getName(), e.getClickedBlock());
			e.getPlayer().sendMessage(ChatColor.DARK_AQUA+"[ClickWarp] Block selected!");
			return;
		} else if (portals.containsKey(e.getClickedBlock()) && e.getPlayer().hasPermission("clickwarp.use")) {
			e.setCancelled(true);
			Warp warp = portals.get(e.getClickedBlock());
			warp.teleportPlayer(e.getPlayer());
			if (plugin.getConfig().getBoolean("showTeleportMessage")) {
				e.getPlayer().sendMessage(ChatColor.DARK_AQUA+"[ClickWarp] You have been teleported to "+warp.getName()+".");
			}
			return;
		}
	}
	
	private Material asMaterial(String s) {
		if (s == null) {
			return Material.GOLD_AXE;
		} else {
			try {
				return Material.valueOf(s);
			} catch (Throwable e) {
				return Material.GOLD_AXE;
			}
		}
	}
	
}
