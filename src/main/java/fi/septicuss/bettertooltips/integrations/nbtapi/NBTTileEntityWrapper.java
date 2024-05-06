package fi.septicuss.bettertooltips.integrations.nbtapi;

import org.bukkit.block.BlockState;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTTileEntity;
import fi.septicuss.bettertooltips.integrations.IntegratedPlugin;

public class NBTTileEntityWrapper extends NBTWrapper<BlockState> {

	private NBTCompound compound;

	public NBTTileEntityWrapper(BlockState state) {
		super(state);

		if (IntegratedPlugin.NBTAPI.isEnabled())
			this.compound = new NBTTileEntity(state);
	}

	public NBTCompound getCompound() {
		return compound;
	}

}
