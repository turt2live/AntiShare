package com.turt2live.antishare.log;

import javax.swing.JFrame;

public class LogAnalyzer extends JFrame {

	private static final long serialVersionUID = 3136306819338940602L;

	/* 
	 * Lines (severe):
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] Inventory Manager Conflict: <conflictname>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] Creative Mode Manager Conflict: <conflictname>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] World Manager Conflict: <conflictname>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] Inventory Manager Conflict: <conflictname>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] AntiShare will not deal with inventories because of the conflict
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] AntiShare will disable itself because of the conflict
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] AntiShare will not deal with allowance of world transfers because of the conflict
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [CONFLICT] AntiShare won't do anything, but there may be problems because of the conflict
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] Sanity check on block break failed.
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] Cannot handle region information: <errormessage>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] You do not have a MySQL driver, please install one. AntiShare will use Flat-File for now
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] Cannot connect to SQL! Check your settings. AntiShare will use Flat-File for now
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] Cannot close SQL connection: 
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] Something went wrong with the query. Send this to the developer:
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] QUERY: <query>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] MESSAGE: <errormessage>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] CANNOT LOAD INVENTORY FILE: <filename>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] CANNOT SAVE INVENTORY FILE: <filename>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] Cannot handle inventory: <errormessage>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] Cannot handle misc inventory: <errormessage>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] Save thread cannot be created.
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [Debugger] *** BUG REPORT ***
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [Debugger] Class: <class>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [Debugger] Exception: <exceptionmessage>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [Debugger] Message: <message>
	 * [<timestamp>] [AntiShare] [SEVERE] [<version>] [Debugger] World: <world>
	 * [<timestamp>] [AntiShare] [SEVERE] Critical: Exception is null.
	 * 
	 * Lines (warning):
	 * [<timestamp>] [AntiShare] [WARNING] [<version>] WorldEdit is not installed!
	 * [<timestamp>] [AntiShare] [WARNING] [<version>] Could not send usage statistics.
	 * [<timestamp>] [AntiShare] [WARNING] Configuration Problem: '<value>' is not a number
	 * [<timestamp>] [AntiShare] [WARNING] An error has occured.
	 * [<timestamp>] [AntiShare] [WARNING] Error Overflow. Output cancelled.
	 * [<timestamp>] [AntiShare] [WARNING] A plugin has chosen not to display the stack trace to you. (Do you have the debugger?)
	 * 
	 * Lines (info):
	 * [<timestamp>] [AntiShare] [INFO] Converting pre-3.0.0 creative blocks...
	 * [<timestamp>] [AntiShare] [INFO] Converted <x> blocks!
	 * [<timestamp>] [AntiShare] [INFO] Enabled! (turt2live)
	 * [<timestamp>] [AntiShare] [INFO] Disabled! (turt2live)
	 * [<timestamp>] [AntiShare] [INFO] Saving virtual storage to disk/SQL
	 * 
	 * Lines (general):
	 * [<timestamp>] [AntiShare] [<LEGAL/ILLEGAL>] <playername> <...> <variable>
	 * 
	 * Lines (events):
	 * [<timestamp>] [AntiShare] [<LEGAL/ILLEGAL>] <playername> <...> <variable>
	 * 
	 * Lines (full):
	 * [<timestamp>] [AntiShare] [<LEGAL/ILLEGAL>] <playername> <...> <variable>
	 * 
	 * Lines (technical):
	 * [<timestamp>] [AntiShare] Starting up...
	 * [<timestamp>] [AntiShare] Shutting down...
	 * [<timestamp>] [AntiShare] AntiShare Reloaded.
	 * 
	 */

	public static void main(String[] args){
		new LogAnalyzer();
	}

	public LogAnalyzer(){
		// TODO: Log analyzer
	}

}
