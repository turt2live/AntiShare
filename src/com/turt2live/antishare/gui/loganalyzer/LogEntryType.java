package com.turt2live.antishare.gui.loganalyzer;

public enum LogEntryType{

	// Severe
	SEVERE_CONFLICT_INVENTORY("[<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] Inventory Manager Conflict: <conflictname>"),
	SEVERE_CONFLICT_CREATIVE("[<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] Creative Mode Manager Conflict: <conflictname>"),
	SEVERE_CONFLICT_WORLD("[<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] World Manager Conflict: <conflictname>"),
	SEVERE_CONFLICT_OTHER("[<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] Other Conflict: <conflictname>"),
	SEVERE_CONFLICT_INVENTORY_MESSAGE("[<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] AntiShare will not deal with inventories because of the conflict"),
	SEVERE_CONFLICT_CREATIVE_MESSAGE("[<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] AntiShare will disable itself because of the conflict"),
	SEVERE_CONFLICT_WORLD_MESSAGE("[<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] AntiShare will not deal with allowance of world transfers because of the conflict"),
	SEVERE_CONFLICT_OTHER_MESSAGE("[<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] AntiShare won't do anything, but there may be problems because of the conflict"),
	SEVERE_SANITY_BLOCK_BREAK("[<timestamp>] [AntiShare] [SEVERE] [<version>] Sanity check on block break failed."),
	SEVERE_REGION_HANDLE_INFORMATION("[<timestamp>] [AntiShare] [SEVERE] [<version>] Cannot handle region information: <errormessage>"),
	SEVERE_SQL_NO_DRIVER("[<timestamp>] [AntiShare] [SEVERE] [<version>] You do not have a MySQL driver, please install one. AntiShare will use Flat-File for now"),
	SEVERE_SQL_CANNOT_CONNECT("[<timestamp>] [AntiShare] [SEVERE] [<version>] Cannot connect to SQL! Check your settings. AntiShare will use Flat-File for now"),
	SEVERE_SQL_CANNOT_CLOSE("[<timestamp>] [AntiShare] [SEVERE] [<version>] Cannot close SQL connection: "),
	SEVERE_SQL_IMPROPER_QUERY("[<timestamp>] [AntiShare] [SEVERE] [<version>] Something went wrong with the query. Send this to the developer:"),
	SEVERE_SQL_IMPROPER_QUERY_QUERY("[<timestamp>] [AntiShare] [SEVERE] [<version>] QUERY: <query>"),
	SEVERE_SQL_IMPROPER_QUERY_MESSAGE("[<timestamp>] [AntiShare] [SEVERE] [<version>] MESSAGE: <errormessage>"),
	SEVERE_INVENTORY_CANNOT_LOAD_FILE("[<timestamp>] [AntiShare] [SEVERE] [<version>] CANNOT LOAD INVENTORY FILE: <filename>"),
	SEVERE_INVENTORY_CANNOT_SAVE_FILE("[<timestamp>] [AntiShare] [SEVERE] [<version>] CANNOT SAVE INVENTORY FILE: <filename>"),
	SEVERE_INVENTORY_CANNOT_HANDLE("[<timestamp>] [AntiShare] [SEVERE] [<version>] Cannot handle inventory: <errormessage>"),
	SEVERE_INVENTORY_CANNOT_HANDLE_MISC("[<timestamp>] [AntiShare] [SEVERE] [<version>] Cannot handle misc inventory: <errormessage>"),
	SEVERE_TIMED_SAVE("[<timestamp>] [AntiShare] [SEVERE] [<version>] Save thread cannot be created."),
	SEVERE_DEBUGGER_BUG_HEADER("[<timestamp>] [AntiShare] [SEVERE] [<version>] [Debugger] *** BUG REPORT ***"),
	SEVERE_DEBUGGER_BUG_CLASS("[<timestamp>] [AntiShare] [SEVERE] [<version>] [Debugger] Class: <class>"),
	SEVERE_DEBUGGER_BUG_EXCEPTION("[<timestamp>] [AntiShare] [SEVERE] [<version>] [Debugger] Exception: <exceptionmessage>"),
	SEVERE_DEBUGGER_BUG_MESSAGE("[<timestamp>] [AntiShare] [SEVERE] [<version>] [Debugger] Message: <message>"),
	SEVERE_DEBUGGER_BUG_WORLD("[<timestamp>] [AntiShare] [SEVERE] [<version>] [Debugger] World: <world>"),
	SEVERE_BUG_EXCEPTION_NULL("[<timestamp>] [AntiShare] [SEVERE] Critical: Exception is null."),
	SEVERE_RELOAD("[<timestamp>] [AntiShare] [SEVERE] Reloads may break AntiShare!"),

	// Warning
	WARNING_WORLD_EDIT_MISSING("[<timestamp>] [AntiShare] [WARNING] [<version>] WorldEdit is not installed!"),
	WARNING_USAGE_STATS_NOT_SENT("[<timestamp>] [AntiShare] [WARNING] [<version>] Could not send usage statistics."),
	WARNING_CONFIGURATION_VALUE_NOT_NUMBER("[<timestamp>] [AntiShare] [WARNING] Configuration Problem: '<value>' is not a number"),
	WARNING_GENERAL_ERROR("[<timestamp>] [AntiShare] [WARNING] An error has occured."),
	WARNING_ERROR_OVERFLOW("[<timestamp>] [AntiShare] [WARNING] Error Overflow. Output cancelled."),
	WARNING_STACK_TRACE_HIDDEN("[<timestamp>] [AntiShare] [WARNING] A plugin has chosen not to display the stack trace to you. (Do you have the debugger?)"),

	// Info
	INFO_CONVERT_START("[<timestamp>] [AntiShare] [INFO] Converting pre-3.0.0 creative blocks..."),
	INFO_CONVERT_END("[<timestamp>] [AntiShare] [INFO] Converted <x> blocks!"),
	INFO_ENABLED("[<timestamp>] [AntiShare] [INFO] Enabled! (turt2live)"),
	INFO_DISABLED("[<timestamp>] [AntiShare] [INFO] Disabled! (turt2live)"),
	INFO_SAVE("[<timestamp>] [AntiShare] [INFO] Saving virtual storage to disk/SQL"),

	// General/Events
	GENERAL_NOTIFICATION("[<timestamp>] [AntiShare] [<LEGAL/ILLEGAL>] <playername> <...> <variable>"),

	// Technical
	TECH_STARTUP("[<timestamp>] [AntiShare] Starting up..."),
	TECH_SHUTDOWN("[<timestamp>] [AntiShare] Shutting down..."),
	TECH_RELOAD("[<timestamp>] [AntiShare] AntiShare Reloaded."),

	// Other
	UNKNOWN("<INVALID>");

	private String format;

	LogEntryType(String format){
		this.format = format;
	}

	public String getRawFormat(){
		return format;
	}

	public static LogEntryType getType(String line){
		String[] parts = line.split("]");
		String message = parts[parts.length - 1].trim();
		if(line.contains("[ILLEGAL]") || line.contains("[LEGAL]")){
			return LogEntryType.GENERAL_NOTIFICATION;
		}else if(line.contains("[INFO] Converted ")){
			return LogEntryType.INFO_CONVERT_END;
		}else if(line.contains("[WARNING] Configuration Problem:")){
			return LogEntryType.WARNING_CONFIGURATION_VALUE_NOT_NUMBER;
		}else if(line.contains("[Debugger] Class:")){
			return LogEntryType.SEVERE_DEBUGGER_BUG_CLASS;
		}else if(line.contains("[Debugger] Exception:")){
			return LogEntryType.SEVERE_DEBUGGER_BUG_EXCEPTION;
		}else if(line.contains("[Debugger] Message:")){
			return LogEntryType.SEVERE_DEBUGGER_BUG_MESSAGE;
		}else if(line.contains("[Debugger] World:")){
			return LogEntryType.SEVERE_DEBUGGER_BUG_WORLD;
		}else if(line.contains("QUERY:")){
			return LogEntryType.SEVERE_SQL_IMPROPER_QUERY_QUERY;
		}else if(line.contains("MESSAGE:")){
			return LogEntryType.SEVERE_SQL_IMPROPER_QUERY_MESSAGE;
		}else if(line.contains("CANNOT LOAD INVENTORY FILE:")){
			return LogEntryType.SEVERE_INVENTORY_CANNOT_LOAD_FILE;
		}else if(line.contains("CANNOT SAVE INVENTORY FILE:")){
			return LogEntryType.SEVERE_INVENTORY_CANNOT_SAVE_FILE;
		}else if(line.contains("Cannot handle inventory:")){
			return LogEntryType.SEVERE_INVENTORY_CANNOT_HANDLE;
		}else if(line.contains("Cannot handle misc inventory:")){
			return LogEntryType.SEVERE_INVENTORY_CANNOT_HANDLE_MISC;
		}else if(line.contains("[CONFLICT] Inventory Manager Conflict:")){
			return LogEntryType.SEVERE_CONFLICT_INVENTORY;
		}else if(line.contains("[CONFLICT] Creative Mode Manager Conflict:")){
			return LogEntryType.SEVERE_CONFLICT_CREATIVE;
		}else if(line.contains("[CONFLICT] World Manager Conflict:")){
			return LogEntryType.SEVERE_CONFLICT_WORLD;
		}else if(line.contains("[CONFLICT] Other Conflict:")){
			return LogEntryType.SEVERE_CONFLICT_OTHER;
		}else if(line.contains("Cannot handle region information:")){
			return LogEntryType.SEVERE_REGION_HANDLE_INFORMATION;
		}else if(line.contains("Reloads may break AntiShare!")){
			return LogEntryType.SEVERE_RELOAD;
		}else if(line.contains("left the region")
				|| line.contains("entered the region")
				|| line.contains("changed to gamemode")){
			return LogEntryType.GENERAL_NOTIFICATION;

		}
		for(LogEntryType type : LogEntryType.values()){
			if(type.getRawFormat().contains(message)){
				return type;
			}
		}
		return LogEntryType.UNKNOWN;
	}
}
