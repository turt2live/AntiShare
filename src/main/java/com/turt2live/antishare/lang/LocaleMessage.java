package com.turt2live.antishare.lang;

public enum LocaleMessage{

    UPDATE_READY("update-ready"),
    UPDATE_LINK("update-link"),
    BUG_FILES_REMOVE("bug-files-removed"),
    ENABLED("enabled"),
    DISABLED("disabled"),
    RELOADING("reloading"),
    RELOADED("reloaded"),
    NO_PERMISSION("no-permission"),
    NOT_A_PLAYER("not-a-player"),
    SYNTAX("syntax"),
    NO_REGIONS("no-regions"),
    EXTENDED_HELP("help"),
    REGION_CREATED("region-created"),
    REGION_SAVED("region-saved"),
    REGION_REMOVED("region-removed"),
    HAVE_TOOL("tool-have"),
    GET_TOOL("tool-get"),
    NO_CUBOID_TOOL("no-cuboid-tool"),
    NOT_ENABLED("not-enabled"),
    FINE_REWARD_TOGGLE("fine-reward-toggle"),
    FINE_REWARD("fine-reward"),
    NEED_INV_SPACE("inv-space"),
    MIRROR_WELCOME("inv-welcome"),
    MIRROR_WELCOME_ENDER("ender-welcome"),
    MIRROR_EDIT("inv-edit"),
    TOOL_GENERIC("tool.generic"),
    TOOL_SET("tool.set"),
    TOOL_CUBOID("tool.cuboid"),
    CUBOID_REMOVED("cuboid.removed"),
    CUBOID_MISSING("cuboid.missing"),
    SN_ON("simplenotice.on"),
    SN_OFF("simplenotice.off"),
    SN_MISSING("simplenotice.missing"),
    ERROR_INVENTORIES("error.inventories"),
    ERROR_NO_PLAYER("error.no-player"),
    ERROR_ASSUME("error.assume"),
    ERROR_INV_MISSING("error.inv-missing"),
    ERROR_UNKNOWN("error.unknown"),
    ERROR_NO_CUBOID_TOOL("error.cuboid-tool"),
    ERROR_NAME_IN_USE("error.name-used"),
    ERROR_REGION_MISSING("error.no-region"),
    ERROR_REGION_STAND_MISSING("error.no-stand-region"),
    ERROR_NO_PAGE("error.page"),
    ERROR_BAD_FILE("error.bad-file"),
    ERROR_BAD_KEY("error.bad-key"),
    ERROR_NO_VAULT("error.no-vault"),
    ERROR_ONLY_CLEAR("error.cannot-set"),
    ERROR_NO_MONEY_TAB("error.no-money-tab"),
    ERROR_NO_MONEY_VAULT("error.no-money-vault"),
    ERROR_WORLD_SPLIT("error.world-split"),
    START_CHECK_CONFIG("startup.config"),
    START_VERSION_STRING("startup.version-string"),
    START_SIMPLENOTICE("startup.simplenotice"),
    START_OFFLINE_1("startup.offline.line1"),
    START_OFFLINE_2("startup.offline.line2"),
    START_OFFLINE_3("startup.offline.line3"),
    START_SETUP("startup.setup"),
    START_START("startup.start"),
    START_REGISTER("startup.register"),
    START_SCHEDULE("startup.schedule"),
    START_COMPAT_FOLDERS("startup.compat.folders"),
    START_COMPAT_BLOCKS("startup.compat.blocks"),
    START_COMPAT_WORLDS("startup.compat.worlds"),
    START_COMPAT_PLAYERS("startup.compat.players"),
    START_COMPAT_INVENTORIES("startup.compat.inventories"),
    START_COMPAT_CLEANUP("startup.compat.cleanup"),
    STOP_FLUSH("shutdown.flush"),
    STOP_SAVE("shutdown.save"),
    RELOAD_RELOAD("reload.reload"),
    SERVICE_METRICS("service.metrics"),
    SERVICE_SIMPLE_NOTICE("service.simplenotice"),
    SERVICE_BLOCKS("service.blcoks"),
    SERVICE_INVENTORIES("service.inventories"),
    SERVICE_SIGNS("service.signs"),
    SERVICE_PERMISSIONS("service.permissions"),
    SERVICE_ITEM_MAP("service.item-map"),
    SERVICE_METRICS_TRACKERS("service.metrics-trackers"),
    SERVICE_LISTENER("service.listener"),
    SERVICE_ALERTS("service.alerts"),
    SERVICE_MESSAGES("service.messages"),
    SERVICE_UPDATE("service.update"),
    SERVICE_COMMANDS("service.commands"),
    SERVICE_REGION_INVENTORY_UPDATE("service.region-inventory-update"),
    SERVICE_REGIONS("service.regions"),
    SERVICE_FEATURES("service.features"),
    SERVICE_CUBOID("service.cuboid"),
    SERVICE_MONEY("service.money"),
    SERVICE_HOOKS("service.hooks"),
    DICT_DIRECTORY("dictionary.directory"),
    DICT_CONFIG_FILES("dictionary.config-files"),
    DICT_INVENTORY("dictionary.inventory"),
    DICT_WORLD("dictionary.world"),
    DICT_NUMBER("dictionary.number"),
    DICT_IS_IN("dictionary.is-in"),
    DICT_NO_ONE("dictionary.no-one"),
    DICT_KEY("dictionary.key"),
    DICT_VALUE("dictionary.value"),
    DICT_REGIONS("dictionary.regions"),
    DICT_GETTING("dictionary.getting"),
    DICT_NOT_GETTING("dictionary.not-getting"),
    DICT_NOT_SET("dictionary.not-set"),
    DICT_SET_AS("dictionary.set-as"),
    DICT_NATURAL("dictionary.natural"),
    DICT_IS("dictionary.is"),
    DICT_WAS("dictionary.was"),
    DICT_THAT("dictionary.that"),
    DICT_BLOCK("dictionary.block"),
    DICT_REMOVED("dictionary.removed"),
    STATUS_INVENTORIES("status.inventories"),
    STATUS_CREATIVE_BLOCKS("status.creative-blocks"),
    STATUS_SURVIVAL_BLOCKS("status.survival-blocks"),
    STATUS_ADVENTURE_BLOCKS("status.adventure-blocks"),
    STATUS_CREATIVE_ENTITIES("status.creative-entities"),
    STATUS_SURVIVAL_ENTITIES("status.survival-entities"),
    STATUS_ADVENTURE_ENTITIES("status.adventure-entities"),
    STATUS_CUBOIDS("status.cuboids"),
    STATUS_FINES("status.fines"),
    STATUS_REWARDS("status.rewards"),
    STATUS_REGIONS("status.regions"),
    STATUS_SIGNS("status.signs"),
    STATUS_LINKED_INVENTORIES("status.linked-inventories"),
    STATUS_CLEAN("status.clean"),
    STATUS_ARCHIVE("status.archive"),
    STATUS_INV_CONVERT("status.inv-convert"),
    STATUS_REGION_MIGRATE("status.region-migrate"),
    STATUS_BLOCKS_CONVERTED("status.blocks-converted"),
    STATUS_WORLD_MIGRATE("status.world-migrate"),
    BLOCK_MAN_WAIT("blockman.wait"),
    BLOCK_MAN_PERCENT("blockman.percent"),
    TAB_NONE("tab.no-more"),
    TAB_REGION("tab.region"),
    TAB_REMOVE_REGION("tab.rmregion"),
    TAB_EDIT_REGION("tab.editregion"),
    TAB_LIST_REGION("tab.listregion"),
    WARNING_REMOVE_WORLD("warning.remove-world"),
    WARNING_REMOVE_WORLD2("warning.remove-world2"),
    WARNING_MOVE_BLOCK("warning.move-block"),
    FINES_REWARDS_FINE_FAILED("fines-and-rewards.fine.failed"),
    FINES_REWARDS_FINE_SUCCESS("fines-and-rewards.fine.success"),
    FINES_REWARDS_REWARD_FAILED("fines-and-rewards.reward.failed"),
    FINES_REWARDS_REWARD_SUCCESS("fines-and-rewards.reward.success"),
    FINES_REWARDS_BALANCE("fines-and-rewards.balance"),
    PHRASE_CREATE("phrase.create-a"),
    PHRASE_SPAWN("phrase.spawned-a"),
    PHRASE_BREAK("phrase.break-a"),
    PHRASE_BROKE("phrase.broke"),
    PHRASE_PLACE("phrase.place-a"),
    PHRASE_PLACED("phrase.placed"),
    PHRASE_USE("phrase.use-a"),
    PHRASE_USED("phrase.used"),
    PHRASE_THROW("phrase.throw-a"),
    PHRASE_THREW("phrase.threw"),
    PHRASE_PICK("phrase.picked-a"),
    PHRASE_PICKED("phrase.picked"),
    PHRASE_DIED("phrase.died"),
    PHRASE_COMMAND("phrase.command"),
    PHRASE_COMMANDED("phrase.commanded"),
    PHRASE_HIT_A("phrase.hit-a"),
    PHRASE_HIT("phrase.hit"),
    PHRASE_CRAFT("phrase.crafted-a"),
    PHRASE_CRAFTED("phrase.crafted"),
    PHRASE_INV_CHANGE("phrase.inv-change"),
    PHRASE_CHANGE_GAMEMODE("phrase.change-gm"),
    PHRASE_COOLDOWN("phrase.wait"),
    PHRASE_REGION("phrase.not-in-region"),
    PHRASE_CANNOT_CHANGE("phrase.no-gm-change"),
    PHRASE_GM_BREAK("phrase.gm-break"),
    PHRASE_GM_BROKE("phrase.gm-broke"),
    PHRASE_ATTACH("phrase.attach-a"),
    PHRASE_ATTACHED("phrase.attached"),
    PHRASE_REGION_ENTER("phrase.region-enter"),
    PHRASE_REGION_LEAVE("phrase.region-leave");

    private String node;

    private LocaleMessage(String node){
        this.node = node;
    }

    public String getConfigurationNode(){
        return node;
    }

}
