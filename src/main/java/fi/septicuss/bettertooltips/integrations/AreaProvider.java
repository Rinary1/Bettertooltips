package fi.septicuss.bettertooltips.integrations;

import java.util.List;

import org.bukkit.Location;

public interface AreaProvider {

	public List<String> getApplicableAreas(Location location);

}
