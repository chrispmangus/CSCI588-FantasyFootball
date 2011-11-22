package com.csci588.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TopPerformerList {

	private Context context;
	
	TopPerformerList(Context context){
		this.context = context;
	}
	
	public LinearLayout createPerformerList(String query, DatabaseHelper db, boolean filter){
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		Cursor dataCursor = db.getQuery(query);
		if(dataCursor.moveToFirst()){
			do{
				View v = itemCreator(dataCursor,filter);
				if(v != null)
					ll.addView(v);
			}
			while(dataCursor.moveToNext());
		}
		dataCursor.close();
		return ll;		
	}
	
	
	private View itemCreator(Cursor cursor, boolean filter){
		final String p_id = cursor.getString(2);
		LayoutInflater lf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = lf.inflate(R.layout.top_performer, null);
		
		ImageView iv = (ImageView)view.findViewById(R.id.player__Pic);
		iv.setBackgroundResource(PictureActions.pickPicture(Integer.parseInt(p_id)));
		
		String teamQuery = "select rosters.manager_id from rosters where (qb = "+p_id + 
				" or rb1 = " + p_id +
				" or rb2 = " + p_id +
				" or wr1 = " + p_id +
				" or wr2 = " + p_id +
				" or wrt = " + p_id +
				" or te = " +  p_id +
				" or k = " +   p_id +
				" or bn1 = " +   p_id +
				" or bn2 = " +   p_id +
				" or bn3 = " +   p_id +
				" or bn4 = " +   p_id +
				" or bn5 = " +   p_id +
				") AND week = 9";
		String ownerQuery = "select managers._id from managers where managers._id = (" + teamQuery + ")";
		Cursor managerInfo = GamedayActivity.getDbHelp().getQuery(ownerQuery);
		if(managerInfo.moveToFirst()){
			if(filter){
				managerInfo.close();
				return null;
			}	
			String statsQuery = "select real_stats, proj_stats, username from nfl_fantasy_stats,managers where managers._id = " +
					managerInfo.getString(0)  + " and nfl_fantasy_stats._id = " +p_id;
			Cursor statsInfo = GamedayActivity.getDbHelp().getQuery(statsQuery);
			statsInfo.moveToFirst();
			TextView text = (TextView) view.findViewById(R.id.playerName);
			text.setText(cursor.getString(0) + " " + cursor.getString(1));
		 	text = (TextView) view.findViewById(R.id.teamName);
		 	text.setText("Team: " + statsInfo.getString(2) + "'s team");
		 	text = (TextView) view.findViewById(R.id.realPoints);
		 	text.setText("Real Points: " + statsInfo.getString(0));
		 	text = (TextView) view.findViewById(R.id.projPoints);
		 	text.setText("Proj Points: " + statsInfo.getString(1));
		 	statsInfo.close();
		}
		if(!managerInfo.moveToFirst()){
			String statsQuery = "select real_stats, proj_stats from nfl_fantasy_stats where nfl_fantasy_stats._id = "+p_id;
			Cursor statsInfo = GamedayActivity.getDbHelp().getQuery(statsQuery);
			statsInfo.moveToFirst();
			TextView text = (TextView) view.findViewById(R.id.playerName);
			text.setText(cursor.getString(0) + " " + cursor.getString(1));
		 	text = (TextView) view.findViewById(R.id.teamName);
		 	text.setText("Free Agent");
		 	text = (TextView) view.findViewById(R.id.realPoints);
		 	text.setText("Real Points: " + statsInfo.getString(0));
		 	text = (TextView) view.findViewById(R.id.projPoints);
		 	text.setText("Proj Points: " + statsInfo.getString(1));
		 	statsInfo.close();
		}
	 	
	 	view.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), PlayerActivity.class);
				myIntent.putExtra("player_id", p_id);
				v.getContext().startActivity(myIntent);
			}
		});
	 	
	 	managerInfo.close();
	 	return view;
	}
}
