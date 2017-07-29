package clickwarp.warps;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import clickwarp.ClickWarp;

public class WarpHolder extends HashMap<String, Warp> {
	private static final long serialVersionUID = 4232360153377820102L;
	private ClickWarp plugin;
	
	public WarpHolder(ClickWarp plugin) {
		this.plugin = plugin;
	}
	
	public WarpHolder(ClickWarp plugin, Map<String, Object> map, boolean unsaved) {
		if (unsaved) {
			this.plugin = plugin;
			castedPutAllUnsaved(map);
		} else {
			this.plugin = plugin;
			castedPutAll(map);
		}
	}
	
	public WarpHolder(ClickWarp plugin, Map<String, Object> map) {
		this.plugin = plugin;
		castedPutAll(map);
	}

	public void castedPutAll(Map<? extends String, ? extends Object> map) {
		for (String k : map.keySet()) {
			put(k, (Warp) map.get(k));
		}
	}
	
	public void castedPutAllUnsaved(Map<? extends String, ? extends Object> map) {
		for (String k : map.keySet()) {
			putUnsaved(k, (Warp) map.get(k));
		}
	}
	
	public Warp putUnsaved(String key, Warp value) {
		return super.put(key, value);
	}
	
	public Warp put (String key, Warp value) {
		Warp returnMe = super.put(key, value);
		try {
			plugin.saveWarps();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnMe;
	}
	
}
