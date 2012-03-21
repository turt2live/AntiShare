package com.turt2live.antishare.log;

import javax.swing.JFrame;

public class LogAnalyzer extends JFrame {

	private static final long serialVersionUID = 3136306819338940602L;

	/*
	 * Log Formats:
	 * 
	 * AntiShare - General: [AntiShare] [<timestamp>] <line>
	 * AntiShare - Full:    [AntiShare] [<timestamp>] <line>
	 * AntiShare - Events:  [<timestamp>] <playername> <...> <variable>
	 * AntiShare - Tech:    [AntiShare] [<timestamp>] <line>
	 * 
	 * Lines (severe):
	 * [<timestamp>] [SEVERE] [<version>] [CONFLICT] Inventory Manager Conflict: <conflictname>
	 * [<timestamp>] [SEVERE] [<version>] [CONFLICT] Creative Mode Manager Conflict: <conflictname>
	 * [<timestamp>] [SEVERE] [<version>] [CONFLICT] World Manager Conflict: <conflictname>
	 * [<timestamp>] [SEVERE] [<version>] [CONFLICT] Inventory Manager Conflict: <conflictname>
	 * [<timestamp>] [SEVERE] [<version>] [CONFLICT] AntiShare will not deal with inventories because of the conflict
	 * [<timestamp>] [SEVERE] [<version>] [CONFLICT] AntiShare will disable itself because of the conflict
	 * [<timestamp>] [SEVERE] [<version>] [CONFLICT] AntiShare will not deal with allowance of world transfers because of the conflict
	 * [<timestamp>] [SEVERE] [<version>] [CONFLICT] AntiShare won't do anything, but there may be problems because of the conflict
	 * [<timestamp>] [SEVERE] [<version>] Sanity check on block break failed.
	 * [<timestamp>] [SEVERE] [<version>] Cannot handle region information: <errormessage>
	 * [<timestamp>] [SEVERE] [<version>] You do not have a MySQL driver, please install one. AntiShare will use Flat-File for now
	 * [<timestamp>] [SEVERE] [<version>] Cannot connect to SQL! Check your settings. AntiShare will use Flat-File for now
	 * [<timestamp>] [SEVERE] [<version>] Cannot close SQL connection: 
	 * [<timestamp>] [SEVERE] [<version>] Something went wrong with the query. Send this to the developer:
	 * [<timestamp>] [SEVERE] [<version>] QUERY: <query>
	 * [<timestamp>] [SEVERE] [<version>] MESSAGE: <errormessage>
	 * [<timestamp>] [SEVERE] [<version>] CANNOT LOAD INVENTORY FILE: <filename>
	 * [<timestamp>] [SEVERE] [<version>] CANNOT SAVE INVENTORY FILE: <filename>
	 * [<timestamp>] [SEVERE] [<version>] Cannot handle inventory: <errormessage>
	 * [<timestamp>] [SEVERE] [<version>] Cannot handle misc inventory: <errormessage>
	 * [<timestamp>] [SEVERE] [<version>] Save thread cannot be created.
	 * 
	 * Lines (warning):
	 * [<timestamp>] [WARNING] [<version>] WorldEdit is not installed!
	 * [<timestamp>] [WARNING] [<version>] Could not send usage statistics.
	 * [<timestamp>] [WARNING] Configuration Problem: '<value>' is not a number
	 * 
	 * Lines (info):
	 * [<timestamp>] [INFO] Converting pre-3.0.0 creative blocks...
	 * [<timestamp>] [INFO] Converted " + converted + " blocks!
	 * [<timestamp>] [INFO] Converted <x> blocks!
	 * [<timestamp>] [INFO] Enabled! (turt2live)
	 * [<timestamp>] [INFO] Disabled! (turt2live)
	 * [<timestamp>] [INFO] Saving virtual storage to disk/SQL
	 * 
	 * Lines (general):
	 * [AntiShare] [<timestamp>] <playername> <...> <variable>
	 * 
	 * Lines (events):
	 * [<timestamp>] <playername> <...> <variable>
	 * 
	 * Lines (full):
	 * [AntiShare] [<timestamp>] <playername> <...> <variable>
	 * 
	 * Lines (technical):
	 * [AntiShare] [<timestamp>] Starting up...
	 * [AntiShare] [<timestamp>] Shutting down...
	 * [AntiShare] [<timestamp>] AnitShare Reloaded.
	 * 
	 */

	public static void main(String[] args){
		new LogAnalyzer();
	}

	public LogAnalyzer(){
		// TODO: Log analyzer
	}

}
