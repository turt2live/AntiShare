package com.turt2live.antishare.engine;

import com.turt2live.antishare.configuration.groups.GroupManager;
import com.turt2live.antishare.events.EventDispatcher;
import com.turt2live.antishare.events.engine.EngineShutdownEvent;
import com.turt2live.antishare.events.worldengine.WorldEngineCreateEvent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

/**
 * Represents the AntiShare engine
 *
 * @author turt2live
 */
public final class Engine {

    /**
     * The default cache increment (60 seconds)
     */
    public static final long DEFAULT_CACHE_INCREMENT = 60000; // 60 seconds
    /**
     * The default cache maximum time (120 seconds)
     */
    public static final long DEFAULT_CACHE_MAXIMUM = 120000; // 120 seconds
    /**
     * The default save interval (0, off)
     */
    public static final long DEFAULT_SAVE_INTERVAL = 0; // Default no save

    private static Engine instance;

    private long saveInterval = DEFAULT_SAVE_INTERVAL;
    private long cacheMaximum = DEFAULT_CACHE_MAXIMUM;
    private long cacheIncrement = DEFAULT_CACHE_INCREMENT;
    private ConcurrentMap<String, WorldEngine> engines = new ConcurrentHashMap<String, WorldEngine>();
    private Timer cacheTimer, saveTimer;
    private Logger logger = Logger.getLogger(getClass().getName());
    private GroupManager groupManager = null;
    private boolean attachmentsAsPlaced = false; // break attachments as placed flag
    private boolean attachmentsDeny = false; // Mismatched attachments versus block break deny flag
    private boolean physicsGrow = false; // grow-with-gamemode flag
    private boolean physicsBlockItems = false; // drop items (physics) as gamemode placed flag
    private boolean hoppersMixed = false; // hopper mixing of gamemodes flag

    private Engine() {
        newCacheTimer();
        newSaveTimer();
        setCacheIncrement(cacheIncrement);
        setSaveInterval(saveInterval);
    }

    /**
     * @deprecated For use by tests only
     */
    @Deprecated
    void forceNotInitialized() {
        groupManager = null;
    }

    /**
     * Determines if this engine is ready or not. If this engine
     * is not ready, {@link com.turt2live.antishare.engine.EngineNotInitializedException}
     * may be thrown from various methods.
     *
     * @return true if ready, false otherwise.
     */
    public boolean isReady() {
        if (groupManager == null) return false;

        return true;
    }

    /**
     * Gets the group manager instance for this engine.
     *
     * @return the group manager
     */
    public GroupManager getGroupManager() {
        if (!isReady()) throw new EngineNotInitializedException();

        return groupManager;
    }

    /**
     * Sets the new group manager for this engine to use. This will internally call
     * {@link com.turt2live.antishare.configuration.groups.GroupManager#loadAll()}.
     *
     * @param manager the new group manager, cannot be null
     */
    public void setGroupManager(GroupManager manager) {
        if (manager == null) throw new IllegalArgumentException("group manager cannot be null");

        DevEngine.log("[Engine] New group manager: " + manager.getClass().getName());

        this.groupManager = manager;
        this.groupManager.loadAll();
    }

    /**
     * Gets the logger for this engine
     *
     * @return the logger
     */
    public Logger getLogger() {
        if (!isReady()) throw new EngineNotInitializedException();

        return logger;
    }

    /**
     * Sets the new logger for this engine to use
     *
     * @param logger the new logger, cannot be null
     */
    public void setLogger(Logger logger) {
        if (logger == null) throw new IllegalArgumentException("logger may not be null");

        DevEngine.log("[Engine] New logger: " + logger.getClass().getName());

        this.logger = logger;
    }

    /**
     * Gets the engine for the specified world. If none exists, a new WorldEngine is
     * created and registered.
     *
     * @param world the world to lookup, cannot be null
     * @return the world engine
     */
    public WorldEngine getEngine(String world) {
        if (!isReady()) throw new EngineNotInitializedException();
        if (world == null) throw new IllegalArgumentException("world cannot be null");

        WorldEngine engine = engines.get(world);
        if (engine == null) engine = createWorldEngine(world);
        return engine;
    }

    /**
     * Creates a world engine for the supplied world. If the world engine already exists,
     * the existing world engine is created.
     *
     * @param world the world to create an engine for
     * @return the world engine
     */
    public WorldEngine createWorldEngine(String world) {
        if (!isReady()) throw new EngineNotInitializedException();
        if (world == null) throw new IllegalArgumentException("world cannot be null");
        if (engines.containsKey(world)) return engines.get(world);

        DevEngine.log("[Engine] Creating world engine for '" + world + "'...");

        WorldEngine engine = new WorldEngine(world);
        engines.put(world, engine);

        EventDispatcher.dispatch(new WorldEngineCreateEvent(engine));

        return engine;
    }

    /**
     * Unloads a world engine from the core engine. If the passed world is
     * null or not found, this will do nothing.
     *
     * @param world the world to unload
     */
    public void unloadWorldEngine(String world) {
        if (!isReady()) throw new EngineNotInitializedException();
        if (world != null) {
            WorldEngine engine = engines.get(world);
            if (engine != null) {
                DevEngine.log("[Engine] Unloading world engine for '" + world + "'...");
                engine.prepareShutdown();
                engines.remove(world);
            }
        }
    }

    /**
     * Prepares the engine for shutdown. This will save all world engines, cancel the
     * cache timer, and revoke all listeners.
     */
    public void prepareShutdown() {
        EventDispatcher.dispatch(new EngineShutdownEvent());

        DevEngine.log("[Engine] Shutting down");

        newCacheTimer(); // Cancels internally, resetting the timer to no task
        newSaveTimer(); // Cancels internally, resetting the timer to no task
        for (WorldEngine engine : engines.values())
            engine.prepareShutdown();

        engines.clear();
    }

    /**
     * Gets the maximum time the cache is permitted to hold an object
     *
     * @return the maximum cache time, in milliseconds
     */
    public long getCacheMaximum() {
        if (!isReady()) throw new EngineNotInitializedException();

        return cacheMaximum;
    }

    /**
     * Sets the cache maximum. The value is a millisecond value for how long an object
     * may remain stale before being removed
     *
     * @param cacheMaximum the new cache maximum, cannot be less than or equal to zero
     */
    public void setCacheMaximum(long cacheMaximum) {
        if (cacheMaximum <= 0) throw new IllegalArgumentException("maximum cannot be less than or equal to zero");

        DevEngine.log("[Engine] New cache maximum: " + cacheMaximum);

        this.cacheMaximum = cacheMaximum;
    }

    /**
     * Gets the number of milliseconds it takes for the cache timer to tick
     *
     * @return the milliseconds for a tick
     */
    public long getCacheIncrement() {
        if (!isReady()) throw new EngineNotInitializedException();

        return cacheIncrement;
    }

    /**
     * Sets the new cache increment. This is a millisecond value for how often a cache
     * cleanup check is issued. Once this is called with a valid value, the cache timer
     * is rescheduled to occur immediately and will have a period equal to the value
     * passed.
     *
     * @param cacheIncrement the new increment, cannot be less than or equal to zero
     */
    public void setCacheIncrement(long cacheIncrement) {
        if (cacheIncrement <= 0)
            throw new IllegalArgumentException("cache increment must not be less than or equal to zero");

        DevEngine.log("[Engine] New cache increment: " + cacheIncrement);

        this.cacheIncrement = cacheIncrement;
        newCacheTimer();
        cacheTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (WorldEngine engine : engines.values()) {
                    engine.getBlockManager().cleanup();
                }
            }
        }, 0L, cacheIncrement);
    }

    /**
     * Gets the save interval for the periodic save function. If the returned value
     * is less than or equal to zero, the periodic save function is disabled and not
     * operating. Any other positive value is used to indicate the period by which
     * the engine triggers a save.
     *
     * @return the save interval
     */
    public long getSaveInterval() {
        if (!isReady()) throw new EngineNotInitializedException();

        return saveInterval;
    }

    /**
     * Sets the new save interval. This is a millisecond value for how often the engine
     * should periodically save data in the subsequent world engines and itself. Values
     * less than or equal to zero are considered to be "do not save periodically" and
     * strictly follow that behaviour. Once called with a value that will trigger a
     * periodic save, the timer will save immediately and fire every interval until
     * cancelled.
     *
     * @param saveInterval the new save interval
     */
    public void setSaveInterval(long saveInterval) {
        DevEngine.log("[Engine] New save interval: " + saveInterval);

        this.saveInterval = saveInterval;
        newSaveTimer();
        if (saveInterval > 0) {
            saveTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    for (WorldEngine engine : engines.values()) {
                        engine.getBlockManager().saveAll();
                    }
                }
            }, 0, saveInterval);
        }
    }

    /**
     * Sets the physics settings for the AntiShare engine.
     * <p/>
     * 'growWithGamemode' is the flag used to determine whether or not 'growing' blocks
     * will grow with the gamemode inherited from their parent (source) block. If true,
     * a creative vine (for example) will grow to have a creative child. If false, the
     * child block will have no stored gamemode.
     * <p/>
     * 'dropAsGamemode' is the flag used for determining how items should be spawned due
     * to 'random' events (such as cacti breaking). If this is true, then the 'random' events
     * will be modified to have their drops conform to how the placed block state was. If false,
     * Minecraft is left to do it's damage.
     *
     * @param growWithGamemode the flag for block growing
     * @param dropAsGamemode   the flag for block drops
     */
    // TODO: Unit test
    public void setPhysicsSettings(boolean growWithGamemode, boolean dropAsGamemode) {
        DevEngine.log("[Engine] New physics settings: growWithGamemode(" + growWithGamemode + "), dropAsGamemode(" + dropAsGamemode + ")");

        this.physicsGrow = growWithGamemode;
        this.physicsBlockItems = dropAsGamemode;
    }

    /**
     * Determines if the flag for growing blocks with inherited gamemodes is set
     *
     * @return the flag setting
     * @see #setPhysicsSettings(boolean, boolean)
     */
    // TODO: Unit test
    public boolean isPhysicsGrowWithGamemode() {
        return physicsGrow;
    }

    /**
     * Determines if the flag for 'random' block breaks breaking as placed gamemode
     * is set
     *
     * @return the flag setting
     * @see #setPhysicsSettings(boolean, boolean)
     */
    // TODO: Unit test
    public boolean isPhysicsBreakAsGamemode() {
        return physicsBlockItems;
    }

    /**
     * Sets the attachment settings for the AntiShare engine.
     * <p/>
     * 'breakAsPlaced' represents whether or not any attachments on the sides of
     * blocks should be broken as they were placed (such as creative attachments
     * dropping nothing). This is only applicable is 'denyMismatchBreak' is false.
     * <p/>
     * 'denyMismatchBreak' represents the flag for denying a block break due to it's
     * attachments. For example, a natural block with a creative attachment being broken
     * by a survival player will result in the block being denied (if this flag is true).
     * If false, this flag simply forwards the logic to 'breakAsPlaced'.
     *
     * @param breakAsPlaced     the break-as-placed flag
     * @param denyMismatchBreak the deny-break flag
     */
    // TODO: Unit test
    public void setAttachmentSettings(boolean breakAsPlaced, boolean denyMismatchBreak) {
        DevEngine.log("[Engine] New attachment settings: breakAsPlaced(" + breakAsPlaced + "), denyMismatchBreak(" + denyMismatchBreak + ")");

        this.attachmentsAsPlaced = breakAsPlaced;
        this.attachmentsDeny = denyMismatchBreak;
    }

    /**
     * Determines if attached blocks should be broken as placed, or not.
     *
     * @return true for 'break as placed', false otherwise
     * @see #setAttachmentSettings(boolean, boolean)
     * @see #isAttachmentsDenyMismatchBreak()
     */
    // TODO: Unit test
    public boolean isAttachmentsBreakAsPlaced() {
        return attachmentsAsPlaced;
    }

    /**
     * Determines if attached blocks will also prevent blocks from being broken
     *
     * @return true to deny breaking due to attachments, false otherwise
     * @see #setAttachmentSettings(boolean, boolean)
     */
    // TODO: Unit test
    public boolean isAttachmentsDenyMismatchBreak() {
        return attachmentsDeny;
    }

    /**
     * Sets whether or not hoppers (and other inventory transfers) are permitted
     * to pull/push items between blocks of differing gamemodes. If the flag is
     * set to true, inventory transfers will be denied for blocks which are of
     * different gamemodes (such as a 'creative' hopper to a 'survival' hopper),
     * however if at least one of the blocks is of type 'none' or both are the
     * same type, interaction is not restricted. If the flag is set as false, natural
     * hopper/item transfer behaviour can occur.
     *
     * @param flag the flag for hopper interaction mixing
     */
    // TODO: Unit test
    public void setHoppersDenyMixed(boolean flag) {
        DevEngine.log("[Engine] New hopper deny mixed: " + flag);

        this.hoppersMixed = flag;
    }

    /**
     * Determines if hopper interaction between block types is
     * denied.
     *
     * @return true if denied, false otherwise
     * @see #setHoppersDenyMixed(boolean)
     */
    // TODO: Unit test
    public boolean isHopperMixedInteractionDenied() {
        return hoppersMixed;
    }

    private void newCacheTimer() {
        if (cacheTimer != null) cacheTimer.cancel();
        cacheTimer = new Timer();
    }

    private void newSaveTimer() {
        if (saveTimer != null) saveTimer.cancel();
        saveTimer = new Timer();
    }

    /**
     * Gets the engine instance
     *
     * @return the engine instance
     */
    public static Engine getInstance() {
        if (instance == null) instance = new Engine();
        return instance;
    }
}
