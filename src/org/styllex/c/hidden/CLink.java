package org.styllex.c.hidden;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.block.ContainerBlock;

import com.alta189.sqllitelib.sqlCore;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class CLink extends JavaPlugin{
	
	private HashMap<String, Block> sets = new HashMap<String, Block>();
	private HashMap<Block, Block> links = new HashMap<Block, Block>();
	
	private PermissionHandler perm;
	private sqlCore sql;
	private boolean useperm;
	private String depNum = "0";
	
	@Override
	
	public void onDisable(){
		if(sql!=null){
			sql.close();
		}
	}
	
	@Override
	
	public void onEnable(){
		
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
		
	    if(this.perm == null){
	    	if(permissionsPlugin != null){
	    		this.perm = ((Permissions) permissionsPlugin).getHandler();
	    		this.useperm=true;
	    	}else{
	    		Logger.getLogger("Minecraft").info("Permission system not detected - Linked Chests");
	    		this.useperm=false;
	    	}
	    }
	    this.sql=new sqlCore(Logger.getLogger("Minecraft"), "[LinkedChest]", "lcin_bin_rec", "/plugins/LC");
	    
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		if(label.equalsIgnoreCase("linkc")){
			
			if(args.length==0){
				return false;
			}
			
			if(args[0].equalsIgnoreCase("link")){
				//Permission Check
				if(useperm){//If permissions is enabled
					if(sender instanceof Player){//if the sender is not the console (is a Player)
						if(!(hasPerms((Player)sender, "linkchest.bind")||hasPerms((Player)sender, "linkchest.link"))){//If player does not have permission nodes
							sender.sendMessage(ChatColor.RED+"You Do Not Have Permission to Do That");//Send warning message
							return true;//return true to exit the method, run no more of the script
						}
					}
				}
				
				//If the sender is a console or has permission then continue
				
				if(args.length<=3){//If the person does not have at least 4 words after /linkc (including "link")
					return true;//return true to break the method, stop running more script
				}
				
				if(!(sets.containsKey(args[1])&&sets.containsKey(args[2])&&sets.containsKey(args[3]))){
					sender.sendMessage(ChatColor.RED+"One of listed set chests do not exist");
					return true;
				}
				
				if(same(sets.get(args[1]), sets.get(args[2]), sets.get(args[3]))){
					sender.sendMessage(ChatColor.RED+"The two listed set chests refer to the same chest, can't link");
					return true;
				}
				
				links.put(sets.get(args[1]), sets.get(args[3]));
				links.put(sets.get(args[2]), sets.get(args[3]));
				
				sender.sendMessage(ChatColor.GREEN+"ChestLink Suceeded");
				
				return true;
			}
			if(args[0].equalsIgnoreCase("set")){
				
				String used;
				
				if(!(sender instanceof Player)){
					System.out.println("Console can not a Block to add to set");
					return true;
				}
				
				Player player = (Player)sender;
				
				Block block = player.getTargetBlock(null, 50);
				
				if(block==null||block.getType()==null||block.getType()==Material.AIR){
					player.sendMessage(ChatColor.RED+"You Are Not Pointing At A Chest");
					player.sendMessage(ChatColor.RED+"Note : Chests Can Only Be Within 50 Units Of Your Location");
					return true;
				}
				
				if(!(block.getType()==Material.CHEST)){
					player.sendMessage(ChatColor.RED+"You Are Not Pointing At A Chest");
					return true;
				}
				
				if(args.length<=1){//if no chosen Name, make it a number next up
					sender.sendMessage("No Specified Name Shosen - Defaulting to next available Integer");
					depNum = nextIntOfString(depNum);
					used = depNum;
					player.sendMessage(ChatColor.GOLD+"Chest Set As Name '"+used+"'");
				}else{
					used=args[1];//Else use chosen name
				}
				
				sets.put(used, block);//Add the block to the list by its name
				return true;
				
			}
			
		}
		
		return false;
		
	}
	public boolean same(Object l1, Object l2, Object l3){
		return (l1.equals(l2)||l2.equals(l3)||l3.equals(l1));
	}
	public String nextIntOfString(String s){
		int num = Integer.parseInt(s);
		num++;
		return String.valueOf(num);
	}
	public Block getFirstParrellelChestHostContainerItem(Block block){
		Location loc = block.getLocation();
		Location loc2 = loc;
		loc2.setX(loc.getX()-1);
		if(block.getWorld().getBlockAt(loc2).getType()==Material.CHEST){
			if(block.getWorld().getBlockAt(loc2) instanceof ContainerBlock){
				return block.getWorld().getBlockAt(loc2);
			}
		}
		loc2.setX(loc.getX()+1);
		if(block.getWorld().getBlockAt(loc2).getType()==Material.CHEST){
			if(block.getWorld().getBlockAt(loc2) instanceof ContainerBlock){
				return block.getWorld().getBlockAt(loc2);
			}
		}
		loc2.setX(loc.getX());
		loc2.setX(loc.getZ()-1);
		if(block.getWorld().getBlockAt(loc2).getType()==Material.CHEST){
			if(block.getWorld().getBlockAt(loc2) instanceof ContainerBlock){
				return block.getWorld().getBlockAt(loc2);
			}
		}
		loc2.setX(loc.getZ()+1);
		if(block.getWorld().getBlockAt(loc2).getType()==Material.CHEST){
			if(block.getWorld().getBlockAt(loc2) instanceof ContainerBlock){
				return block.getWorld().getBlockAt(loc2);
			}
		}
		return null;
	}
	
	public boolean linksContains(Object st){
		return links.containsKey(st);
	}

	public Block getLinks(Block key) {
		return links.get(key);
	}
	public boolean hasPerms(Player player, String node){
		if(!useperm){
			return player.isOp();
		}else{
			return this.perm.has(player, node);
		}
	}
}
