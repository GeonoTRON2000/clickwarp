package clickwarp.warps;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import clickwarp.ClickWarp;


public class Warp implements ConfigurationSerializable {
	private String name;
	private String world;
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	
	public Warp (Map<String, Object> map) {
		this.name = (String) map.get("name");
		this.world = (String) map.get("world");
		this.x = (Double) map.get("x");
		this.y = (Double) map.get("y");
		this.z = (Double) map.get("z");
		this.yaw = ((Double) map.get("yaw")).floatValue();
		this.pitch = ((Double) map.get("pitch")).floatValue();
	}
	
	public Warp (String name, Location location) {
		this.name = name;
		this.world = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
	}
	
	public Warp(String name, double x, double y, double z) {
		this.name = name;
		this.world = ClickWarp.getMyServer().getWorlds().get(0).getName();
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = 0F;
		this.pitch = 0F;
	}
	
	public Warp(String name, World world, double x, double y, double z) {
		this.name = name;
		this.world = world.getName();
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = 0F;
		this.pitch = 0F;
	}
	
	public void rename(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Location getLocation() {
		return new Location(ClickWarp.getMyServer().getWorld(world), x, y, z, yaw, pitch);
	}
	
	public void setLocation(Location location) {
		this.world = location.getWorld().getName();
		this.x = location.getX();
		this.y = location.getY();
		this.z = location.getZ();
		this.yaw = location.getYaw();
		this.pitch = location.getPitch();
	}
	
	public void teleportPlayer(Player p) {
		World wld = ClickWarp.getMyServer().getWorld(world);
		if (wld == null) {
			p.sendMessage(ChatColor.DARK_RED+"[ClickWarp] That warp is in an invalid world.");
			return;
		}
		Location location = new Location(wld, x, y, z, yaw, pitch);
		p.teleport(location);
	}

	public Map<String, Object> serialize() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", name);
		map.put("world", world);
		map.put("x", x);
		map.put("y", y);
		map.put("z", z);
		map.put("yaw", yaw);
		map.put("pitch", pitch);
		return map;
	}
	
	public static Warp deserialize(Map<String, Object> map) {
		return new Warp(map);
	}
	
	public static Warp valueOf(Map<String, Object> map) {
		return deserialize(map);
	}
	
}
