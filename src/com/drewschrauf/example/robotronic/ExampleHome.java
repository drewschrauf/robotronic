package com.drewschrauf.example.robotronic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ExampleHome extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		final Context context = this;

		Button button1 = (Button) findViewById(R.id.Button01);
		Button button2 = (Button) findViewById(R.id.Button02);
		
		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				context.startActivity(new Intent(context, ExampleSimple.class));				
			}
		});

		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				context.startActivity(new Intent(context, ExampleList.class));
			}
		});
	}
}
