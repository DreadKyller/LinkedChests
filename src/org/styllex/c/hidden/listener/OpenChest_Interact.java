package org.styllex.c.hidden.listener;

import net.minecraft.server.EntityPlayer;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.styllex.c.hidden.CLink;

public class OpenChest_Interact extends PlayerListener{
	private CLink plugin;
	public OpenChest_Interact(CLink plugin){
		this.plugin=plugin;
	}
	public void onPlayerInteract(PlayerInteractEvent event){
		if(!(event.getAction()==Action.RIGHT_CLICK_BLOCK)){
			return;
		}
		if(!this.plugin.hasPerms(event.getPlayer(), "linkchest.open")){
			return;
		}
		Block block = event.getClickedBlock();
		if(block.getType()==Material.CHEST){
			if(block.getState() instanceof ContainerBlock){
				if(this.plugin.linksContains(block)){
					event.setCancelled(true);
					Block block2 = plugin.getLinks(block);
					if(block2.getState() instanceof ContainerBlock){
						
						EntityPlayer entityPlayer = ((CraftPlayer)event.getPlayer()).getHandle();
						
						CraftInventory inventory = (CraftInventory)((ContainerBlock)block2.getState()).getInventory();
						entityPlayer.a(inventory.getInventory());
						
						return;
					}else{
						Block block3 = plugin.getFirstParrellelChestHostContainerItem(block2);
						
						EntityPlayer entityPlayer = ((CraftPlayer)event.getPlayer()).getHandle();
						
						CraftInventory inventory = (CraftInventory)((ContainerBlock)block3.getState()).getInventory();
						entityPlayer.a(inventory.getInventory());
						
						return;
					}
				}
			}else{
				Block block4 = plugin.getFirstParrellelChestHostContainerItem(block);
				if(this.plugin.linksContains(block4)){
					event.setCancelled(true);
					Block block2 = plugin.getLinks(block4);
					if(block2.getState() instanceof ContainerBlock){
						
						EntityPlayer entityPlayer = ((CraftPlayer)event.getPlayer()).getHandle();
						
						CraftInventory inventory = (CraftInventory)((ContainerBlock)block2.getState()).getInventory();
						entityPlayer.a(inventory.getInventory());
						
						return;
					}else{
						Block block3 = plugin.getFirstParrellelChestHostContainerItem(block2);
						
						EntityPlayer entityPlayer = ((CraftPlayer)event.getPlayer()).getHandle();
						
						CraftInventory inventory = (CraftInventory)((ContainerBlock)block3.getState()).getInventory();
						entityPlayer.a(inventory.getInventory());
						
						return;
					}
				}
			}
		}
	}
}
