package locator.localizadormq;


import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.mapquest.android.maps.GeoPoint;

public class EnrichedMap extends BasicMap 
{

	private float rotation = 0f;
	private static Context mContext;
	   
    @Override
    protected void init() 
    {
        setupMapView(new GeoPoint(-30.068334,-51.120298), 18);
        
        //RelativeLayout container = (RelativeLayout)findViewById(R.id.container);
        
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(
        		new View.OnClickListener() 
        		{			
        			@Override
        			public void onClick(View v) 
        			{
        				rotation = rotation + 45.f;
						map.getController().setMapRotation(rotation);
        			}
        		}
        );
        /*
        
        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
          	  Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
          	  startActivity(intent);
            }
        });*/
       // container.addView(button);
        final Intent intent = new Intent(this, EventList.class);
        
        final Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(
        		new View.OnClickListener() 
        		{
        				public void onClick(View v) 
        				{
        					Toast.makeText(getApplicationContext(), "Going to List Events", Toast.LENGTH_SHORT).show();
        					startActivity(intent);
        				}
        		}
        );
      
        mContext = getApplicationContext();
        Room.init();
        /*Room bigRoom = new Room("Sala1",Room.predio1Lat,Room.predio1Lng,this.map);
        Room bigRoom2 = new Room("Sala2",Room.predio1Lat+Room.salaTopStepLat,Room.predio1Lng+Room.salaTopStepLng,this.map);
        Room bigRoom3 = new Room("Sala2",Room.predio1Lat+Room.salaTopStepLat+Room.salaSideStepLat,Room.predio1Lng+Room.salaTopStepLng+Room.salaSideStepLng,this.map);
        Room bigRoom4 = new Room("Sala2",Room.predio1Lat+Room.salaSideStepLat,Room.predio1Lng+Room.salaSideStepLng,this.map);
        */
        
        
        Toast.makeText(getApplicationContext(), "Rooms: "+Room.allRooms.size(), Toast.LENGTH_SHORT).show();
        Room.drawRooms(this.map);
       /* Event eventArray[] = new Event[12];
        for(int i=0;i<12;i++)
        {
        	eventArray[i] = new Event(12-i);
        }
        */
        
        final Spinner spinner=(Spinner) findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	//spinner.getSelectedItem().toString();
            	Date startDate = new Date();
            	Date endDate = new Date();
            	Calendar cal1 = Calendar.getInstance();
            	Date today = new Date();
            	cal1.setTime(today);
            	//Calendar cal2 = Calendar.getInstance();
            	switch(pos)
            	{
            		case 0:
            			;
            	}
            	User.mainUser.selectiveShow(startDate, endDate, map);
                //User.mainUser.selectiveShow(,this.map)
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        
    }

	    public static Context getContext() 
	    {
	        return mContext;
	    }
}
