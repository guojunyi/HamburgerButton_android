package com.hamburgerbutton;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends FragmentActivity {
	Context mContext;
	HamburgerButton button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		
		button = (HamburgerButton) findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(button.isToogleOn()){
					button.toogleOff();
				}else{
					button.toogleOn();
				}
			}
			
		});
		
	}
	
	
	
}
