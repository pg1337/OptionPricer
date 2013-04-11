package com.pg1337.optionpricer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.Math;

import cern.jet.stat.Probability;

public class Pricer extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricer);
        
        Button calbutton= (Button) findViewById(R.id.button_calculate);
        calbutton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				try
					{
						//Check for valid inputs in all fields
					
						EditText stock= (EditText) findViewById(R.id.edit_stockprice);
						double S= Double.parseDouble(stock.getText().toString());
		
						EditText strike= (EditText) findViewById(R.id.edit_strike);
						double K= Double.parseDouble(strike.getText().toString());
						
						EditText voltext= (EditText) findViewById(R.id.edit_vol);
						double vol= Double.parseDouble(voltext.getText().toString());
						
						EditText irtext= (EditText) findViewById(R.id.edit_interestrate);
						double r= Double.parseDouble(irtext.getText().toString());
						
						EditText timetext= (EditText) findViewById(R.id.edit_timetoexpiry);
						double t= Double.parseDouble(timetext.getText().toString());
						
						RadioGroup type= (RadioGroup) findViewById(R.id.radio_group);
						int selected= type.getCheckedRadioButtonId();
						RadioButton choice= (RadioButton) findViewById(selected);
						String OptType= choice.getText().toString();
						
						System.out.println("radio selected is "+OptType);
						
						//	Calculate option value
				        double val= CalculatePrice(S,K,r,vol,t,OptType);
				        //	Call price.	        
				        System.out.println("option value is "+Double.toString(val));
				        
				        TextView value= (TextView) findViewById(R.id.text_optionvalue);
				        value.setText("Option value: "+Double.toString(val));
					}
				catch(NumberFormatException e)
				{
					Toast.makeText(getApplicationContext(),"Please enter a valid numbers for all fields!",Toast.LENGTH_LONG).show();
				}
		        catch(ArithmeticException e)
		        {
					Toast.makeText(getApplicationContext(),"Unexpected calculation error!",Toast.LENGTH_LONG).show();
		        	System.out.println("arithmetic error!");
		        	System.out.println(e.toString());
		        }
			}
        	
        });
        
        
    }
    
    public double CalculatePrice(double S, double K, double r, double vol, double t, String type) throws ArithmeticException
    {
    	double d1= (Math.log((S/K)) + (r+ vol*vol/2)*t) / (vol*t);
        double d2= d1- vol*Math.sqrt(t);
   
        double c= S* Probability.normal(0,1,d1) - K*(Math.exp((-1)*r*t))* Probability.normal(0,1,d2);
        if(type.equals("Call"))
        {
            return c;
        }
        else
        {
            double p= c-S+ (K*Math.exp((-1)*r*t));
            return p;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pricer, menu);
        return true;
    }
}
