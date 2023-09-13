package thaumicenergistics.part;

import appeng.api.parts.IPartModel;
import appeng.api.util.AEPartLocation;
import appeng.container.interfaces.IInventorySlotAware;
import appeng.core.AppEng;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.GuiHostType;
import appeng.parts.reporting.AbstractPartReporting;
import appeng.parts.reporting.PartCraftingTerminal;
import appeng.tile.inventory.AppEngInternalInventory;
import appeng.util.Platform;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.items.IItemHandler;
import thaumicenergistics.client.gui.GuiBase;
import thaumicenergistics.init.ModGUIs;
import thaumicenergistics.network.PacketHandler;
import thaumicenergistics.network.packets.PacketOpenGUI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class NewPartArcaneTerminal extends PartCraftingTerminal {
	private final AppEngInternalInventory crystals = new AppEngInternalInventory(this, 6);
	private final AppEngInternalInventory upgrades = new AppEngInternalInventory(this, 1);
	
	public NewPartArcaneTerminal(ItemStack is) {
		super(is);
	}
	
	@Override
	public void getDrops(List<ItemStack> drops, boolean wrenched) {
		super.getDrops(drops, wrenched);
		for (final ItemStack crystal : crystals) {
			if (!crystal.isEmpty())
				drops.add(crystal);
		}
		for (final ItemStack upgrade : upgrades) {
			if (!upgrade.isEmpty())
				drops.add(upgrade);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound data) {
		super.readFromNBT(data);
		this.crystals.readFromNBT(data, "crystals");
		this.upgrades.readFromNBT(data, "upgrades");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound data) {
		super.writeToNBT(data);
		this.crystals.writeToNBT(data, "crystals");
		this.upgrades.writeToNBT(data, "upgrades");
	}
	
	@Override
	public boolean onPartActivate(EntityPlayer player, EnumHand hand, Vec3d pos) {
		try {
			// this will run ONLY AbstractPartReporting#onPartActivate
			//      and then throw a NullPointerException if that returns false.
			// This is because we can't make our own GUIs because GuiBridge is an enum that can't be extended,
			//      so we need to override this functionality while still checking for wrenches in AbstractPartReporting's method.
			return super.onPartActivate(player, hand, pos);
		} catch (NullPointerException ignore) { // this is guaranteed to throw
			PacketHandler.sendToServer(new PacketOpenGUI(ModGUIs.ARCANE_TERMINAL, this.getTile().getPos(), this.getSide()));
			return true;
		}
	}
	
	@Override
	public GuiBridge getGui(EntityPlayer p) {
		return null; // Allows us to catch a NPE and use our own GUI.
	}
	
	@Override
	public IItemHandler getInventoryByName(String name) {
		return super.getInventoryByName(name);
	}
	
	@Override
	public IPartModel getStaticModels() {
		return super.getStaticModels();
	}
	
	
}
