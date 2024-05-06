package fi.septicuss.bettertooltips.integrations.nbtapi;

import org.bukkit.entity.Entity;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import fi.septicuss.bettertooltips.integrations.IntegratedPlugin;

public class NBTEntityWrapper extends NBTWrapper<Entity> {

	private NBTCompound compound;

	public NBTEntityWrapper(Entity entity) {
		super(entity);

		if (IntegratedPlugin.NBTAPI.isEnabled())
			this.compound = new NBTEntity(entity);
	}

	@Override
	public NBTCompound getCompound() {
		return compound;
	}

}
