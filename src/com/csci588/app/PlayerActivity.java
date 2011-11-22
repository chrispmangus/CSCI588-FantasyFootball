package com.csci588.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class PlayerActivity extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_screen_layout);
		
		positions = new String[5];
		positions[0] = "QB";
		positions[1] = "WR";
		positions[2] = "RB";
		positions[3] = "TE";
		positions[4] = "K";
		
		final String p_id = getIntent().getStringExtra("player_id");
		
		
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
		String ownerQuery = "select managers.username from managers where managers._id = (" + teamQuery + ")";
		final Cursor managerInfo = GamedayActivity.getDbHelp().getQuery(teamQuery);
		final Cursor playerInfo = GamedayActivity.getDbHelp().getQuery("SELECT * from nfl_players where _id = " + p_id);
		Cursor teamInfo = GamedayActivity.getDbHelp().getQuery(ownerQuery);
		
		playerInfo.moveToFirst();
		final int pos_id = playerInfo.getInt(4);
				
		String dialogQuery = "select nfl_players._id,fName,lName, real_stats from nfl_players,nfl_fantasy_stats " +
				"where nfl_players.position_id = " + pos_id +
				" and nfl_players._id = nfl_fantasy_stats._id";
		final Cursor dialogInfo = GamedayActivity.getDbHelp().getQuery(dialogQuery);
		final CompareCursorAdapter s = new CompareCursorAdapter(this, dialogInfo);
		
		String tradeQuery = "select nfl_players._id,fName,lName, real_stats from nfl_players, nfl_fantasy_stats, rosters " +
				"where nfl_players.position_id = "+pos_id+" and nfl_players._id = nfl_fantasy_stats._id " +
				"and manager_id = 1 and rosters.week = 9 and nfl_players._id IN (qb,rb1,rb2,wr1,wr2,te,k)";
		final Cursor tradeInfo = GamedayActivity.getDbHelp().getQuery(tradeQuery);
		final CompareCursorAdapter trade = new CompareCursorAdapter(this, tradeInfo);
		
		/*determines the buttons for the player, based on thier team status
		 * the 4 states are:
		 * 		no team
		 * 		not on our team
		 * 		on our team on bench
		 * 		on our team in play 
		 */
		
		//if  (player has a team? )
		if (teamInfo.moveToFirst()) {
			managerInfo.moveToFirst();
			/* if players in on our manager_id team
			 * which is currently set to 1
			 */
			if (managerInfo.getInt(0) != 1) {
				LinearLayout ll = (LinearLayout) this
						.findViewById(R.id.buttonLayout);
				ll.setOrientation(LinearLayout.VERTICAL);
				Button cmp = new Button(this);
				cmp.setText("Compare");
				cmp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				cmp.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(final View v) {
						//Intent myIntent = new Intent(v.getContext(), CompareActivity.class);
						//startActivityForResult(myIntent,0);
						AlertDialog dialog = new AlertDialog.Builder(v.getContext()).setIcon(R.drawable.icon)
								.setTitle("Pick a player")
								.setSingleChoiceItems(s,0,new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface dialog, int which) {
										Intent myIntent = new Intent(v.getContext(), CompareActivity.class);
										myIntent.putExtra("one_id", p_id);
										myIntent.putExtra("two_id", playerIdFromDialog(dialogInfo,which));
										startActivityForResult(myIntent,0);
									}
								})
								.create();
						dialog.show();
						
					}
				});
				ll.addView(cmp);

				Button trd = new Button(this);
				trd.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT));
				trd.setText("Trade For");
				final String oteam = managerInfo.getString(0);
				trd.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(final View v) {
						//Intent myIntent = new Intent(v.getContext(), CompareActivity.class);
						//startActivityForResult(myIntent,0);
						AlertDialog dialog = new AlertDialog.Builder(v.getContext()).setIcon(R.drawable.icon)
								.setTitle("Pick to trade")
								.setSingleChoiceItems(trade,0,new DialogInterface.OnClickListener() {
									
									public void onClick(DialogInterface dialog, int which) {
										PlayerActions.tradePlayer(playerIdFromDialog(tradeInfo,which), p_id, "1", oteam);
										Intent intent = getIntent();
						            	 finish();
						            	 startActivity(intent);
									}
								})
								.create();
						dialog.show();
						
					}
				});
				ll.addView(trd);
			} 
			else {
				String teamStateQuery = "select (qb = " + p_id + " OR wr1 = "
						+ p_id + " or wr2 = " + p_id + " or wrt = " + p_id
						+ " or rb1 = " + p_id + " or rb2 = " + p_id
						+ " or k = " + p_id + " or te = " + p_id
						+ ") from rosters where manager_id = "
						+ managerInfo.getString(0) + " and week = " + "9";
				Cursor teamStateInfo = GamedayActivity.getDbHelp().getQuery(
						teamStateQuery);
				teamStateInfo.moveToFirst();
				
				if (teamStateInfo.moveToFirst()) {
					// if ( in play or not)
					if (teamStateInfo.getInt(0) == 1) {
						LinearLayout ll = (LinearLayout) this
								.findViewById(R.id.buttonLayout);
						ll.setOrientation(LinearLayout.VERTICAL);
						Button cmp = new Button(this);
						cmp.setText("Compare");
						cmp.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						cmp.setOnClickListener(new View.OnClickListener() {
							
							public void onClick(final View v) {
								//Intent myIntent = new Intent(v.getContext(), CompareActivity.class);
								//startActivityForResult(myIntent,0);
								AlertDialog dialog = new AlertDialog.Builder(v.getContext()).setIcon(R.drawable.icon)
										.setTitle("Pick a player")
										.setSingleChoiceItems(s,0,new DialogInterface.OnClickListener() {
											
											public void onClick(DialogInterface dialog, int which) {
												Intent myIntent = new Intent(v.getContext(), CompareActivity.class);
												myIntent.putExtra("one_id", p_id);
												myIntent.putExtra("two_id", playerIdFromDialog(dialogInfo,which));
												startActivityForResult(myIntent,0);
											}
										})
										.create();
								dialog.show();
								
							}
						});
						ll.addView(cmp);

						Button add = new Button(this);
						add.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						add.setText("Bench");
						add.setOnClickListener(new View.OnClickListener() {
				             public void onClick(View v) {
				                 // Perform action on click
				            	 PlayerActions.benchPlayer(p_id,pos_id, "1", "9", PlayerActivity.this);
				            	 Intent intent = getIntent();
				            	 finish();
				            	 startActivity(intent);
				             }
				         });
						ll.addView(add);

						Button drop = new Button(this);
						drop.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						drop.setText("Drop");
						drop.setOnClickListener(new View.OnClickListener() {
				             public void onClick(View v) {
				                 // Perform action on click
				            	 PlayerActions.dropPlayer(p_id, "1", "9");
				            	 Intent intent = getIntent();
				            	 finish();
				            	 startActivity(intent);
				             }
				         });
						ll.addView(drop);
					} else {
						LinearLayout ll = (LinearLayout) this
								.findViewById(R.id.buttonLayout);
						ll.setOrientation(LinearLayout.VERTICAL);
						Button cmp = new Button(this);
						cmp.setText("Compare");
						cmp.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						cmp.setOnClickListener(new View.OnClickListener() {
							
							public void onClick(final View v) {
								//Intent myIntent = new Intent(v.getContext(), CompareActivity.class);
								//startActivityForResult(myIntent,0);
								AlertDialog dialog = new AlertDialog.Builder(v.getContext()).setIcon(R.drawable.icon)
										.setTitle("Pick a player")
										.setSingleChoiceItems(s,0,new DialogInterface.OnClickListener() {
											
											public void onClick(DialogInterface dialog, int which) {
												Intent myIntent = new Intent(v.getContext(), CompareActivity.class);
												myIntent.putExtra("one_id", p_id);
												myIntent.putExtra("two_id", playerIdFromDialog(dialogInfo,which));
												startActivityForResult(myIntent,0);
											}
										})
										.create();
								dialog.show();
								
							}
						});
						ll.addView(cmp);

						Button strt = new Button(this);
						strt.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						strt.setText("Start");
						strt.setOnClickListener(new View.OnClickListener() {
				             public void onClick(View v) {
				                 // Perform action on click
				            	 PlayerActions.startPlayer(p_id,pos_id, "1", "9", PlayerActivity.this);
				            	 Intent intent = getIntent();
				            	 finish();
				            	 startActivity(intent);
				             }
				         });
						ll.addView(strt);

						Button drop = new Button(this);
						drop.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.WRAP_CONTENT));
						drop.setText("Drop");
						drop.setOnClickListener(new View.OnClickListener() {
				             public void onClick(View v) {
				                 // Perform action on click
				            	 PlayerActions.dropPlayer(p_id, "1", "9");
				            	 Intent intent = getIntent();
				            	 finish();
				            	 startActivity(intent);
				            	 
				             }
				         });
						ll.addView(drop);
					}
				}
				teamStateInfo.close();
			}
		}else{
			// else :   not on a team
			LinearLayout ll = (LinearLayout) this.findViewById(R.id.buttonLayout);
			ll.setOrientation(LinearLayout.VERTICAL);
			Button cmp = new Button(this);
			cmp.setText("Compare");
			cmp.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			cmp.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(final View v) {
					//Intent myIntent = new Intent(v.getContext(), CompareActivity.class);
					//startActivityForResult(myIntent,0);
					AlertDialog dialog = new AlertDialog.Builder(v.getContext()).setIcon(R.drawable.icon)
							.setTitle("Pick a player")
							.setSingleChoiceItems(s,0,new DialogInterface.OnClickListener() {
								
								public void onClick(DialogInterface dialog, int which) {
									Intent myIntent = new Intent(v.getContext(), CompareActivity.class);
									myIntent.putExtra("one_id", p_id);
									myIntent.putExtra("two_id", playerIdFromDialog(dialogInfo,which));
									startActivityForResult(myIntent,0);
								}
							})
							.create();
					dialog.show();
					
				}
			});
			ll.addView(cmp);
			
			Button add = new Button(this);
			add.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			add.setText("Add");
			add.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 // Perform action on click
	            	 PlayerActions.addPlayer(p_id, pos_id, "1", "9", PlayerActivity.this);
	            	 Intent intent = getIntent();
	            	 finish();
	            	 startActivity(intent);
	             }
	         });
			ll.addView(add);
		}
		
		ImageView iV = (ImageView) this.findViewById(R.id.player_pic);
		iV.setBackgroundResource(PictureActions.pickPicture(Integer.parseInt(p_id)));
		if(managerInfo.moveToFirst())
			setFantasyTeamName(teamInfo,managerInfo.getString(0));
		else
			setFantasyTeamName();
		setPositionTeam(playerInfo);
		setPlayerName(playerInfo);
		setNextGame(playerInfo, setByeWeek(playerInfo));
		setStats(playerInfo,p_id);
		playerInfo.close();
		teamInfo.close();
		managerInfo.close();
	}
	
	private void setPlayerName(Cursor cursor){
		TextView tv = (TextView) this.findViewById(R.id.player_name);
		if(cursor.moveToFirst()){
			tv.setText(cursor.getString(1) + " " + cursor.getString(2));
		}
	}
		
	private void setFantasyTeamName(){
		TextView tv = (TextView) this.findViewById(R.id.player_fantasy);
		tv.setText("Free Agent");
	}
	
	private void setFantasyTeamName(Cursor cursor,final String team_id){
		TextView tv = (TextView) this.findViewById(R.id.player_fantasy);
		if(cursor.moveToFirst()){
			tv.setText("Owned by: " + cursor.getString(0));
			tv.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					
					Intent myIntent = new Intent(v.getContext(), LineupActivity.class);
					myIntent.putExtra("team_id", team_id);
					startActivityForResult(myIntent,0);
				}
			});
		}
		else{
			tv.setText("Free Agent");
		}
	}
	
	
	private void setPositionTeam(Cursor cursor){
		TextView tv = (TextView) this.findViewById(R.id.player_posteam);
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
	
	private int setByeWeek(Cursor cursor){
		int bye = 0;
		TextView tv = (TextView) this.findViewById(R.id.player_bye);
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
	
	
	private void setNextGame(Cursor cursor, int bye){
		TextView tv = (TextView) this.findViewById(R.id.player_nextgame);
		if(cursor.moveToFirst() && bye != 9){
			String gameQuery = "SELECT away_team, home_team from nfl_schedule where week = 9 AND ( home_team = " + cursor.getString(3) +
					" OR away_team = " + cursor.getString(3) + ")";
			Cursor game =  GamedayActivity.getDbHelp().getQuery(gameQuery);
			game.moveToFirst();
			Log.i("HERE?",cursor.getString(3));
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
	
	private void setStats(Cursor cursor, String p_id){
		LinearLayout ll = (LinearLayout) this.findViewById(R.id.stats_layout);
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
	
	private String playerIdFromDialog(Cursor cursor, int num){
		int count = 0;
		if(cursor.moveToFirst()){
			do{
				if(count == num){
					return cursor.getString(0);
				}
				count++;
			}while(cursor.moveToNext());
		}
		return "-1";
	}
	
	private String[] positions;
}
