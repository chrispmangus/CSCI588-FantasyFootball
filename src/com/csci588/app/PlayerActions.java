package com.csci588.app;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class PlayerActions {
	
	public static void benchPlayer(String p_id, int pos_id, String team_id, String week_id, Context context){
		String benchQ = "SELECT ROSTERS.bn1, ROSTERS.bn2, ROSTERS.bn3, ROSTERS.bn4, ROSTERS.bn5 FROM ROSTERS WHERE ROSTERS.manager_id = "
				+ team_id + " and week = " + week_id;
		Cursor benchInfo = GamedayActivity.getDbHelp().getQuery(benchQ);
		boolean[] benchBlanks = new boolean[5];
		checkBlanks(benchInfo, benchBlanks);
		int spot = firstBlank(benchBlanks);
		
		if(spot != -1){
			String benQuery = "update rosters set bn" + (spot+1) + "= " + p_id + " where manager_id =" + team_id + " and week =" + week_id;
			Cursor cu = GamedayActivity.getDbHelp().getQuery(benQuery);
			cu.moveToFirst();
			cu.close();
			
			for(int i = 0; i < pos_strs.length; i++){
				String rosQuery = "update rosters set "+ pos_strs[i] + "='' where manager_id =" + team_id + " and week =" + week_id
						+" and " + pos_strs[i] + " = " + p_id;
				Cursor ca = GamedayActivity.getDbHelp().getQuery(rosQuery);
				ca.moveToFirst();
				ca.close();
			}
		}else{
			Toast toast = Toast.makeText(context, "Bench full: Can not drop player. Remove a player to bench this one.", Toast.LENGTH_LONG);
			toast.show();
		}
		benchInfo.close();
	}
	
	public static void startPlayer(String p_id, int pos_id, String team_id, String week_id, Context context){
		String rosterQ = "SELECT ROSTERS.qb, ROSTERS.wr1, ROSTERS.wr2, ROSTERS.rb1, ROSTERS.rb2, rosters.te, rosters.k FROM ROSTERS WHERE ROSTERS.manager_id = "
				+ team_id + " and week = " + week_id;
		Cursor rosterInfo = GamedayActivity.getDbHelp().getQuery(rosterQ);
		boolean[] rosterBlanks = new boolean[7];
		checkBlanks(rosterInfo,rosterBlanks);
		Log.i("pos_id", ""+pos_id);
		Log.i("blank", ""+rosterBlanks[4]);
		int spot = firstValidBlank(rosterBlanks,pos_id);
		Log.i("valb",""+spot);
		if(spot != -1){
			String rosQuery = "update rosters set "+ pos_strs[spot] + "=" + p_id + " where manager_id =" + team_id + " and week =" + week_id;
			Cursor ca = GamedayActivity.getDbHelp().getQuery(rosQuery);
			ca.moveToFirst();
			ca.close();			
			
			for(int i = 0; i < 5; i++){
				String benQuery = "update rosters set bn" + (i+1) + "= '' where manager_id =" + team_id + " and week =" + week_id
						+ " and bn" + (i+1) + "= " + p_id;
				Cursor cu = GamedayActivity.getDbHelp().getQuery(benQuery);
				cu.moveToFirst();
				cu.close();
			}
		}else{
			Toast toast = Toast.makeText(context, "Roster full: Can not drop player. Remove a player from play to change.", Toast.LENGTH_LONG);
			toast.show();
		}
		rosterInfo.close();
	}

	public static void addPlayer(String p_id, int pos_id, String team_id, String week_id, Context context){
		String benchQ = "SELECT ROSTERS.bn1, ROSTERS.bn2, ROSTERS.bn3, ROSTERS.bn4, ROSTERS.bn5 FROM ROSTERS WHERE ROSTERS.manager_id = "
				+ team_id + " and week = " + week_id;
		Cursor benchInfo = GamedayActivity.getDbHelp().getQuery(benchQ);
		boolean[] benchBlanks = new boolean[5];
		
		checkBlanks(benchInfo, benchBlanks);
		int spot = firstBlank(benchBlanks);
		if(spot != -1){
			String addQuery = "update rosters set bn" + (spot+1) + "= " + p_id + " where manager_id =" + team_id + " and week =" + week_id;
			Cursor cu = GamedayActivity.getDbHelp().getQuery(addQuery);
			cu.moveToFirst();
			cu.close();
		}else{
			String rosterQ = "SELECT ROSTERS.qb, ROSTERS.wr1, ROSTERS.wr2, ROSTERS.rb1, ROSTERS.rb2, rosters.te, rosters.k FROM ROSTERS WHERE ROSTERS.manager_id = "
					+ team_id + " and week = " + week_id;
			Cursor rosterInfo = GamedayActivity.getDbHelp().getQuery(rosterQ);
			boolean[] rosterBlanks = new boolean[7];
			rosterInfo.moveToFirst();
			checkBlanks(rosterInfo,rosterBlanks);
			int rosSpot = firstValidBlank(rosterBlanks,pos_id);
			if(rosSpot!= -1){
				String addQuery = "update rosters set " + pos_strs[rosSpot] + "= " + p_id + " where manager_id =" + team_id + " and week =" + week_id;
				Cursor cu = GamedayActivity.getDbHelp().getQuery(addQuery);
				cu.moveToFirst();
				cu.close();
			}
			else{
				Toast toast = Toast.makeText(context, "Team full: Can not add player. Drop a player to add this one.", Toast.LENGTH_LONG);
				toast.show();
			}
			rosterInfo.close();
		}
		
		benchInfo.close();
	}
	
	public static void dropPlayer(String p_id, String team_id, String week_id){
		String[] teamSpots = {"qb","wr1","wr2","wrt","rb1","rb2","k","te","bn1","bn2","bn3","bn4","bn5"	};
		for(int i =0 ; i < teamSpots.length; i++){
			String dropQuery = "update rosters set " + teamSpots[i] + "= '' where " + teamSpots[i] + "= " + p_id + " and manager_id=" + team_id + " and week= " + week_id;
			Cursor cu = GamedayActivity.getDbHelp().getQuery(dropQuery);
			cu.moveToFirst();
			cu.close();
		}
	}
	
	/*requires: filled blanks array
	 * this function checks through a bench roster cursor and sees if there are any
	 * empty spaces for filling
	 */
	private static void checkBlanks(Cursor cursor, boolean[] blanks){
		if(cursor.moveToFirst()){
			for(int i = 0; i < blanks.length; i++){
				if(cursor.getString(i) == null || cursor.getString(i).equals(""))
					blanks[i] = true;
			}
		}
	}
	
	private static int firstBlank(boolean[] blanks){
		for(int i = 0; i< blanks.length; i++){
			if(blanks[i] == true)
				return i;
		}
		return -1;
	}
	
	private static int firstValidBlank(boolean[] blanks, int pos_id){
		
		for(int i = 0; i< blanks.length; i++){
			if(blanks[i] == true && pos_ids[i] == pos_id){
				return i;
			}
		}
		return -1;
	}
	
	private static int[] pos_ids = { 0, 1, 1, 2, 2, 3, 4};
	private static String[] pos_strs = { "qb", "wr1", "wr2", "rb1", "rb2", "te", "k"};
}
