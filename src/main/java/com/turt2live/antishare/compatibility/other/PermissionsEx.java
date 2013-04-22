package com.turt2live.antishare.compatibility.other;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.events.PermissionSystemEvent;
import ru.tehkode.permissions.events.PermissionSystemEvent.Action;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.PermissionNodes;

/**
 * PermissionsEx compatibility
 * 
 * @author turt2live
 */
public class PermissionsEx implements Listener{

	private ru.tehkode.permissions.bukkit.PermissionsEx pex;
	private Map<String, List<String>> groups = new HashMap<String, List<String>>();
	private List<Permission> asPermissions = AntiShare.p.getDescription().getPermissions();

	public PermissionsEx(Plugin plugin){
		this.pex = (ru.tehkode.permissions.bukkit.PermissionsEx) plugin;

		buildPermissions();

		AntiShare.p.getServer().getPluginManager().registerEvents(this, AntiShare.p);
	}

	/**
	 * Determines if a target has a permission in PermissionsEx
	 * 
	 * @param target the target to check
	 * @param permission the permission to check
	 * @return true if the target has the permission, false otherwise
	 */
	public boolean hasPermission(CommandSender target, String permission){
		if(target instanceof Player){
			Player player = (Player) target;
			PermissionUser user = ru.tehkode.permissions.bukkit.PermissionsEx.getPermissionManager().getUser(player);
			boolean pexHas = user.has(permission);
			List<String> nodes = new ArrayList<String>();
			for(PermissionGroup group : user.getGroups()){
				nodes = merge(nodes, groups.get(group.getName().toLowerCase()));
			}
			if(existsInList(permission, nodes)){
				return pexHas;
			}else{
				return has(player, permission);
			}
		}
		return target.hasPermission(permission);
	}

	@EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPermissionsUpdate(PermissionSystemEvent event){
		if(event.getAction() == Action.REINJECT_PERMISSIBLES || event.getAction() == Action.RELOADED){
			buildPermissions(); // Rebuild
		}
	}

	private boolean has(Player player, String permission){
		Permission p = null;
		for(Permission perm : asPermissions){
			if(perm.getName().equalsIgnoreCase(permission)){
				p = perm;
				break;
			}
		}
		if(p != null){
			switch (p.getDefault()){
			case OP:
				return player.isOp();
			case NOT_OP:
				return !player.isOp();
			case TRUE:
				return true;
			case FALSE: // Fall into default
			default:
				return false; // Safety
			}
		}
		return false; // Safety
	}

	private List<String> merge(List<String> listone, List<String> listtwo){
		if(listtwo == null){
			return listone;
		}
		List<String> list = new ArrayList<String>();
		for(String item : listone){
			list.add(item);
		}
		for(String item : listtwo){
			list.add(item);
		}
		return list;
	}

	private boolean existsInList(String value, List<String> list){
		for(String item : list){
			if(item.equalsIgnoreCase(value)){
				return true;
			}
		}
		return false;
	}

	private void buildPermissions(){
		groups.clear();

		// Manually cache all groups
		List<String> antishare = PermissionNodes.getAllPermissions();
		EnhancedConfiguration permissionList = new EnhancedConfiguration(new File(pex.getDataFolder(), "permissions.yml"));
		permissionList.load();
		for(PermissionGroup group : ru.tehkode.permissions.bukkit.PermissionsEx.getPermissionManager().getGroups()){
			List<String> appliedPermissions = permissionList.getStringList("groups." + group.getName() + ".pemrissions");
			List<String> applicable = new ArrayList<String>();
			if(appliedPermissions != null){
				for(String permission : appliedPermissions){
					if(permission.toLowerCase().startsWith("antishare.")){
						if(existsInList(permission, antishare)){
							applicable.add(permission);
						}
					}
				}
			}
			groups.put(group.getName().toLowerCase(), applicable);
		}
	}

}
