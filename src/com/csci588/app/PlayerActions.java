package com.csci588.app;

import android.database.Cursor;

public class PlayerActions {

	public static void addPlayer(String p_id, String team_id, String week_id){
		String q1 = "SELECT ROSTERS.bn1, ROSTERS.bn2, ROSTERS.bn3, ROSTERS.bn4, ROSTERS.bn5 FROM ROSTERS WHERE ROSTERS.manager_id = "
				+ team_id + " and week = " + week_id;
		Cursor benchInfo = GamedayActivity.getDbHelp().getQuery(q1);
		boolean[] benchBlanks = new boolean[5];
		
		checkBenchBlanks(benchInfo, benchBlanks);
		int spot = firstBlank(benchBlanks);
		if(spot != -1){
			String addQuery = "update rosters set bn" + (spot+1) + "= " + p_id + " where manager_id =" + team_id + " and week =" + week_id;
			Cursor cu = GamedayActivity.getDbHelp().getQuery(addQuery);
			cu.moveToFirst();
			cu.close();
		}
	}
	
	public static void dropPlayer(String p_id, String team_id, String week_id){
		String[] teamSpots = {"qb","wr1","wr2","wrt","rb1","rb2","k","te","bn1","bn2","bn3","bn4","bn5"	};
		for(int i =0 ; i < teamSpots.length; i++){
			String dropQuery = "update rosters set " + teamSpots[i] + "= null where " + teamSpots[i] + "= " + p_id + " and manager_id=" + team_id + " and week= " + week_id;
			Cursor cu = GamedayActivity.getDbHelp().getQuery(dropQuery);
			cu.moveToFirst();
			cu.close();
		}
	}
	
	private static void checkBenchBlanks(Cursor cursor, boolean[] benchBlanks){
		if(cursor.moveToFirst()){
			for(int i = 0; i < 5; i++){
				if(cursor.getString(i) == null)
					benchBlanks[i] = true;
			}
		}
	}
	
	private static int firstBlank(boolean[] benchBlanks){
		for(int i = 0; i< 5; i++){
			if(benchBlanks[i] == true)
				return i;
		}
		return -1;
	}
	
}
