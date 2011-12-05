package com.csci588.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DriverActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.driver);

		Button b = (Button) this.findViewById(R.id.button1);
		b.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), GamedayActivity.class);
				myIntent.putExtra("flag", true);
				startActivityForResult(myIntent,0);
			}
		});
		
		b = (Button) this.findViewById(R.id.button2);
		b.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Intent myIntent = new Intent(v.getContext(), GamedayActivity.class);
				myIntent.putExtra("flag", false);
				startActivityForResult(myIntent,0);
			}
		});
	}
}
