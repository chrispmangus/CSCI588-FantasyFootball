package com.csci588.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class CompareActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compare_page);
		
		one_id = getIntent().getStringExtra("one_id");
		two_id = getIntent().getStringExtra("two_id");
		
		positions = new String[5];
		positions[0] = "QB";
		positions[1] = "WR";
		positions[2] = "RB";
		positions[3] = "TE";
		positions[4] = "K";
		
		setup(one_id,two_id, false);
		
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
		setup(one_id,two_id, true);
	}
	
	private void setup(final String one_id,final String two_id, boolean restart){
		String oneTeamQuery = "select rosters.manager_id from rosters where (qb = "+one_id + 
				" or rb1 = " + one_id +
				" or rb2 = " + one_id +
				" or wr1 = " + one_id +
				" or wr2 = " + one_id +
				" or wrt = " + one_id +
				" or te = " +  one_id +
				" or k = " +   one_id +
				" or bn1 = " +   one_id +
				" or bn2 = " +   one_id +
				" or bn3 = " +   one_id +
				" or bn4 = " +   one_id +
				" or bn5 = " +   one_id +
				") AND week = 9";
		String oneOwnerQuery = "select managers.username,_id from managers where managers._id = (" + oneTeamQuery + ")";
		Cursor oneManagerInfo = GamedayActivity.getDbHelp().getQuery(oneTeamQuery);
		final Cursor onePlayerInfo = GamedayActivity.getDbHelp().getQuery("SELECT * from nfl_players where _id = " + one_id);
		Cursor oneTeamInfo = GamedayActivity.getDbHelp().getQuery(oneOwnerQuery);
		
		
		String twoTeamQuery = "select rosters.manager_id from rosters where (qb = "+two_id + 
				" or rb1 = " + two_id +
				" or rb2 = " + two_id +
				" or wr1 = " + two_id +
				" or wr2 = " + two_id +
				" or wrt = " + two_id +
				" or te = " +  two_id +
				" or k = " +   two_id +
				" or bn1 = " +   two_id +
				" or bn2 = " +   two_id +
				" or bn3 = " +   two_id +
				" or bn4 = " +   two_id +
				" or bn5 = " +   two_id +
				") AND week = 9";
		String twoOwnerQuery = "select managers.username,_id from managers where managers._id = (" + twoTeamQuery + ")";
		Cursor twoManagerInfo = GamedayActivity.getDbHelp().getQuery(twoTeamQuery);
		final Cursor twoPlayerInfo = GamedayActivity.getDbHelp().getQuery("SELECT * from nfl_players where _id = " + two_id);
		Cursor twoTeamInfo = GamedayActivity.getDbHelp().getQuery(twoOwnerQuery);
		
		setFantasyTeamName(oneTeamInfo, true);
		setPositionTeam(onePlayerInfo, true);
		setPlayerName(onePlayerInfo, true);
		setNextGame(onePlayerInfo, setByeWeek(onePlayerInfo, true), true);
		if(!restart)
			setStats(onePlayerInfo,one_id, true);
		
		setFantasyTeamName(twoTeamInfo, false);
		setPositionTeam(twoPlayerInfo, false);
		setPlayerName(twoPlayerInfo, false);
		setNextGame(twoPlayerInfo, setByeWeek(twoPlayerInfo, false), false);
		if(!restart)
			setStats(twoPlayerInfo,two_id, false);
		
		ImageView iV = (ImageView) this.findViewById(R.id.onePlayerPic);
		iV.setBackgroundResource(PictureActions.pickPicture(Integer.parseInt(one_id)));
		
		iV = (ImageView) this.findViewById(R.id.twoPlayerPic);
		iV.setBackgroundResource(PictureActions.pickPicture(Integer.parseInt(two_id)));
		
		
		oneTeamInfo.close();
		onePlayerInfo.close();
		oneManagerInfo.close();
		
		twoTeamInfo.close();
		twoPlayerInfo.close();
		twoManagerInfo.close();
	}
	
	private void setPlayerName(Cursor cursor, boolean one){
		TextView tv;
		if(one)
			tv = (TextView) this.findViewById(R.id.onePlayerName);
		else
			tv = (TextView) this.findViewById(R.id.twoPlayerName);
		if(cursor.moveToFirst()){
			final String p_id = cursor.getString(0);
			tv.setText(cursor.getString(1) + " " + cursor.getString(2));
			tv.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					
					Intent myIntent = new Intent(v.getContext(), PlayerActivity.class);
					myIntent.putExtra("player_id", p_id);
					v.getContext().startActivity(myIntent);
				}
			});
		}
	}
		
	private void setFantasyTeamName(Cursor cursor, boolean one){
		TextView tv;
		if(one)
			tv = (TextView) this.findViewById(R.id.onePlayerTeam);
		else
			tv = (TextView) this.findViewById(R.id.twoPlayerTeam);
		if(cursor.moveToFirst()){
			final String team_id = cursor.getString(1);
			tv.setText("Owned by: " + cursor.getString(0));
			tv.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					
					Intent myIntent = new Intent(v.getContext(), LineupActivity.class);
					myIntent.putExtra("team_id", team_id);
					v.getContext().startActivity(myIntent);
				}
			});
		}
		else{
			tv.setText("Free Agent");
		}
	}
	
	private void setPositionTeam(Cursor cursor, boolean one){
		TextView tv;
		if(one)
			tv = (TextView) this.findViewById(R.id.onePlayerPosition);
		else
			tv = (TextView) this.findViewById(R.id.twoPlayerPosition);
		String textBox = "";
		if(cursor.moveToFirst()){
			textBox += positions[cursor.getInt(4)];
			String teamQuery = "select nfl_teams.abbreviation from nfl_teams where _id = " + cursor.getString(3);
			Cursor teamAbv =  GamedayActivity.getDbHelp().getQuery(teamQuery);
			teamAbv.moveToFirst();
			textBox += " ( " + teamAbv.getString(0) + " )";
			teamAbv.close();
		}
		tv.setText(textBox);
	}
	
	private int setByeWeek(Cursor cursor, boolean one){
		int bye = 0;
		TextView tv;
		if(one)
			tv = (TextView) this.findViewById(R.id.onePlayerBye);
		else
			tv = (TextView) this.findViewById(R.id.twoPlayerBye);
		if(cursor.moveToFirst()){
			String teamQuery = "select nfl_teams.bye from nfl_teams where _id = " + cursor.getString(3);
			Cursor teamBye =  GamedayActivity.getDbHelp().getQuery(teamQuery);
			teamBye.moveToFirst();
			tv.setText("Bye Week: " + teamBye.getString(0));
			bye = teamBye.getInt(0);
			teamBye.close();
			
		}
		return bye;
	}
	
	
	private void setNextGame(Cursor cursor, int bye, boolean one){
		TextView tv;
		if(one)
			tv = (TextView) this.findViewById(R.id.onePlayerNext);
		else
			tv = (TextView) this.findViewById(R.id.twoPlayerNext);
		if(cursor.moveToFirst() && bye != 9){
			String gameQuery = "SELECT away_team, home_team from nfl_schedule where week = 9 AND ( home_team = " + cursor.getString(3) +
					" OR away_team = " + cursor.getString(3) + ")";
			Cursor game =  GamedayActivity.getDbHelp().getQuery(gameQuery);
			game.moveToFirst();
			String gamesTeamQuery = "SELECT nfl_teams.abbreviation from nfl_teams where nfl_teams._id IN("
					+ game.getString(0) + " ," + game.getString(1) + ")";
			Cursor teams = GamedayActivity.getDbHelp().getQuery(gamesTeamQuery);
			teams.moveToFirst();
			String matchup = "Next Game: " + teams.getString(0) + " @ ";
			teams.moveToNext();
			matchup += teams.getString(0);
			teams.close();
			game.close();
			tv.setText(matchup);
		}else{
			tv.setText("Off this week");
		}
	}

	private void setStats(Cursor cursor, String p_id, boolean one){
		
		LinearLayout ll;
		RatingBar rb;
		if(one){
			ll= (LinearLayout) this.findViewById(R.id.onePlayerStats);
			rb = (RatingBar) this.findViewById(R.id.onePlayerRating);
		}
		else{
			ll= (LinearLayout) this.findViewById(R.id.twoPlayerStats);
			rb = (RatingBar) this.findViewById(R.id.twoPlayerRating);
		}
		LayoutInflater lf = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		cursor.moveToFirst();
		int position = cursor.getInt(4);
		if(position == 0){
			String statsQuery = "select avgpts, passing_yds, passing_tds, int from nfl_player_stats where _id ="
					+ p_id;
			Cursor statsInfo = GamedayActivity.getDbHelp().getQuery(statsQuery);
			statsInfo.moveToFirst();
			
			View v = lf.inflate(R.layout.qb_stat_view, null);
			
			TextView tV = (TextView)v.findViewById(R.id.qbPtsHead);
			SpannableString content = new SpannableString("Avg.Points");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.qbPts);
			tV.setText(statsInfo.getString(0));
			rb.setRating(rating(statsInfo.getInt(0)));
			
			tV = (TextView)v.findViewById(R.id.qbYdsHead);
			content = new SpannableString("Passing Yards");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.qbYds);
			tV.setText(statsInfo.getString(1));
			
			tV = (TextView)v.findViewById(R.id.qbTdsHead);
			content = new SpannableString("Passing Touchdowns");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.qbTds);
			tV.setText(statsInfo.getString(2));
			
			tV = (TextView)v.findViewById(R.id.qbIntsHead);
			content = new SpannableString("Interceptions");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.qbInts);
			tV.setText(statsInfo.getString(3));

			ll.addView(v);
			statsInfo.close();
		}else if(position == 1 || position == 3){
			String statsQuery = "select avgpts, rushing_yds, receiving_tds from nfl_player_stats where _id ="
					+ p_id;
			Cursor statsInfo = GamedayActivity.getDbHelp().getQuery(statsQuery);
			statsInfo.moveToFirst();
			
			View v = lf.inflate(R.layout.rb_stat_view, null);
			
			TextView tV = (TextView)v.findViewById(R.id.rbPtsHead);
			SpannableString content = new SpannableString("Avg. Points");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.rbPts);
			tV.setText(statsInfo.getString(0));
			rb.setRating(rating(statsInfo.getInt(0)));
			
			tV = (TextView)v.findViewById(R.id.rbYdsHead);
			content = new SpannableString("Receiving Yards");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.rbYds);
			tV.setText(statsInfo.getString(1));
			
			tV = (TextView)v.findViewById(R.id.rbTdsHead);
			content = new SpannableString("Receiving Touchdowns");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.rbTds);
			tV.setText(statsInfo.getString(2));
			
			ll.addView(v);
			statsInfo.close();
		}else if(position == 2){
			String statsQuery = "select avgpts, rushing_yds, rushing_tds from nfl_player_stats where _id ="
					+ p_id;
			Cursor statsInfo = GamedayActivity.getDbHelp().getQuery(statsQuery);
			statsInfo.moveToFirst();
			
			View v = lf.inflate(R.layout.rb_stat_view, null);
			
			TextView tV = (TextView)v.findViewById(R.id.rbPtsHead);
			SpannableString content = new SpannableString("Avg. Points");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.rbPts);
			tV.setText(statsInfo.getString(0));
			rb.setRating(rating(statsInfo.getInt(0)));
			
			tV = (TextView)v.findViewById(R.id.rbYdsHead);
			content = new SpannableString("Rushing Yards");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.rbYds);
			tV.setText(statsInfo.getString(1));
			
			tV = (TextView)v.findViewById(R.id.rbTdsHead);
			content = new SpannableString("Rushing Touchdowns");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.rbTds);
			tV.setText(statsInfo.getString(2));
			
			ll.addView(v);
			statsInfo.close();
		}else if(position == 4){
			String statsQuery = "select avgpts, fgm, fg_30_39, fg_40_49 from nfl_player_stats where _id ="
					+ p_id;
			Cursor statsInfo = GamedayActivity.getDbHelp().getQuery(statsQuery);
			statsInfo.moveToFirst();
			
			View v = lf.inflate(R.layout.qb_stat_view, null);
			
			TextView tV = (TextView)v.findViewById(R.id.qbPtsHead);
			SpannableString content = new SpannableString("Avg. Points");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.qbPts);
			tV.setText(statsInfo.getString(0));
			rb.setRating(rating(statsInfo.getInt(0)));
			
			tV = (TextView)v.findViewById(R.id.qbYdsHead);
			content = new SpannableString("Field Goals Made");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.qbYds);
			tV.setText(statsInfo.getString(1));
			
			tV = (TextView)v.findViewById(R.id.qbTdsHead);
			content = new SpannableString("Field Goals 30-39 yards");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.qbTds);
			tV.setText(statsInfo.getString(2));
			
			tV = (TextView)v.findViewById(R.id.qbIntsHead);
			content = new SpannableString("Field Goals 40-49 yards");
			content.setSpan(new UnderlineSpan(), 0 , content.length(), 0);
			tV.setText(content);
			
			tV = (TextView)v.findViewById(R.id.qbInts);
			tV.setText(statsInfo.getString(3));

			
			ll.addView(v);
			statsInfo.close();
		}
	}
	
	private int rating(int score){
		if(score>0 && score < 10)
			return 1;
		else if(score >= 10 && score < 20)
			return 2;
		else if(score >= 20 && score < 30)
			return 3;
		else if(score >= 30 && score < 40)
			return 4;
		else
			return 5;
	}
	
	private String[] positions;
	private String one_id;
	private String two_id;
}
