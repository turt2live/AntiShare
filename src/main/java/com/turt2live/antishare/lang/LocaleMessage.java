package com.turt2live.antishare.lang;

public enum LocaleMessage{

    UPDATE_READY("update-ready"),
    UPDATE_LINK("update-link"),
    BUG_FILES_REMOVE("bug-files-removed"),
    ENABLED("enabled"),
    DISABLED("disabled"),
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
    SERVICE_CONFLICT("service.conflict"),
    DICT_DIRECTORY("dictionary.directory"),
    DICT_CONFIG_FILES("dictionary.config-files"),
    STATUS_INVENTORIES("status.inventories"),
    
    ;

    private String node;

    private LocaleMessage(String node){
        this.node = node;
    }

    public String getConfigurationNode(){
        return node;
    }

}
