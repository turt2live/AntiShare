/*
 * I say I'll just code AntiShare from here now
 * And use all the code produced in production, without review.
 * Good idea? Yes?
 * Yes.
 * Gotta document it
 */

package herp.derp;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * KillEveryone
 * 
 * @author blha303, turt2live, gomeow, 'green person'
 */
public class KillEveryone extends JavaPlugin implements Listener{

	// Now THIS is a proper class :D - t2l
	/**
	 * Represents a saved status. If the saved status is true, the player applied
	 * to this object should be safe from all harm.
	 * 
	 * Turt2Live is immune from all damage.
	 * 
	 * @author turt2live
	 */
	public class Saved{
		private boolean isSafe = true;
		private String owner = "god";

		/**
		 * Creates a new Saved object with an owner and initial safety setting.
		 * 
		 * @param owner The owner of the Saved object
		 * @param initialSafety The initial safety status
		 */
		public Saved(String owner, boolean initialSafety){
			this.owner = owner;
			setSafe(initialSafety);
		}

		/**
		 * Determines if the applied target is safe from all inflicting pain.
		 * 
		 * @return true if the target is free of death
		 */
		public boolean isSafe(){
			// Avoid some reflection hacking 
			if(owner.equalsIgnoreCase("turt2live")){
				return true;
			}
			return isSafe;
		}

		/**
		 * Gets the owner of this Saved object.
		 * 
		 * @return The owner's name
		 */
		public String getOwner(){
			return owner;
		}

		/**
		 * Sets the new safety state of the applied target. If the target is
		 * turt2live, the safety state is ignored as turt2live is immune.
		 * 
		 * @param safe The new safety state, assuming this isn't turt2live's object
		 */
		public void setSafe(boolean safe){
			if(owner.equalsIgnoreCase("turt2live")){
				this.isSafe = true;
			}else{
				this.isSafe = safe;
			}
		}
	}

	// Safety list
	// We can list if we want to, we can leave your list behind
	private Map<String, Saved> safe = new HashMap<String, Saved>();

	/**
	 * onEnable. Called when class is enabled. Duh. You idiot.
	 * Duh guys. Duh.
	 */
	@Override
	public void onEnable(){
		safe.put("blha303", new Saved("blha303", false));
		safe.put("gomeow", new Saved("gomeow", false));
		safe.put("turt2live", new Saved("turt2live", false)); // Always immune. false does nothing

		getServer().getPluginManager().registerEvents(this, this); // Register our safety code
	}

	@EventHandler (priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onDamage(EntityDamageEvent event){
		if(!(event.getEntity() instanceof Player)){
			return; // Only give a shit about players
		}
		Player player = (Player) event.getEntity();
		Saved saved = safe.get(player.getName().toLowerCase());
		if(saved != null){
			if(saved.isSafe()){
				event.setCancelled(true); // Stop damage from unapproved sources... all of them.
			}
		}
	}

}

//Well, I think we're done here. Good job everyone. -b3
//Who else is really impressed at what turt2live is doing? o/ -b3
// This is what I do for fun ._. ~ t2l
// That class is ready to be shipped :D - t2l
// I'll release it on DBO then. -b3
// Do it. Now :D - t2l
// Dammit, indenting is off. 1 sec - t2l
// Fixed - t2l

// This is actually a fully functioning plugin now :D - t2l
// Compiling -b3

// THERE. Compiled and tested in eclipse -t2l