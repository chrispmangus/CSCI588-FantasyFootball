package com.csci588.app;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;


public class RosterActivity extends ListActivity {
	
	/*called when activity is first created */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.roster_layout);
		
		String query = "SELECT NFL_PLAYERS._id,NFL_PLAYERS.fName, NFL_PLAYERS.lName FROM NFL_PLAYERS";
		Cursor dataCursor = GamedayActivity.getDbHelp().getQuery(query);
		setListAdapter(new RosterCursorAdapter(this,dataCursor));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
	}

}
