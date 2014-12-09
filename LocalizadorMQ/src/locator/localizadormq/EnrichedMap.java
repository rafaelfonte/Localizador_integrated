package locator.localizadormq;


import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.mapquest.android.maps.GeoPoint;
import locator.servcomm.ClientThread;
import locator.servcomm.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

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
        final Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        //SETS
                        String username = User.mainUser.username,password = User.mainUser.password;
                        //Update rooms
                        try{
                            ClientThread rooms = new ClientThread(mContext,Constants.ctGetRoomsAct,new String[]{username,password});
                            rooms.execute();
                            String [] resultQuery = rooms.get();
                            JSONArray jArr = new JSONArray(resultQuery[1]);
                            JSONObject handler;
                            for(int i = 0; i < jArr.length(); i++){
                                handler = jArr.getJSONObject(i);
                                new Room(handler.getString("name"),handler.getString("description"),0,handler.getInt("side"),handler.getInt("pos"),handler.getInt("max_cap"),handler.getInt("ID") - 1);
                            }
                        }
                        catch(Exception e){
                            Log.e("MainActivity", "An exception occurred room fetch: ", e);
                        }
                        User.mainUser = new User(username,password);
                        //Update
                        try{
                            ClientThread events = new ClientThread(mContext, Constants.ctGetAct,new String[]{User.mainUser.username,User.mainUser.password});
                            events.execute();
                            String [] resultQuery = events.get();
                            //System.out.println("result query: " + resultQuery[0] + ";;;;" + resultQuery[1]);
                            String eventsSubscribed = resultQuery[2];
                            int [] subscriptions = Constants.cvrtSubscriptions(eventsSubscribed);
                            System.out.println("Events subscribed: " + eventsSubscribed);
                            JSONArray jArr = new JSONArray(resultQuery[1]);
                            JSONObject handler;
                            for(int i = 0; i < jArr.length(); i++){
                                handler = jArr.getJSONObject(i);
                                Event ev = new Event(handler.getInt("ID") - 1, handler.getInt("roomID"), handler.getString("idOwner"), handler.getString("name"),
                                        handler.getString("description"),handler.getInt("capacity"),handler.getInt("cap-taken"),handler.getString("timestamp"),
                                        handler.getString("duration"),handler.getString("private").equals("t") ? true : false);
                                if(Constants.contains(handler.getInt("ID"), subscriptions)){
                                    System.out.println("Yes! Contains! " + handler.getInt("ID"));
                                    User.mainUser.addEvent(ev);
                                }
                            }
                        }
                        catch(Exception e){
                            Log.e("MainActivity", "An exception occurred room fetch: ", e);
                        }


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
            	if(pos == 0)
            	{
            		User.mainUser.showNextEventRoom(new Date(),map);
            	}
            	else
            	{
	            	Date startDate = getStartDate(pos);
	            	//Toast.makeText(getApplicationContext(), "Start Date: "+startDate.toString(), Toast.LENGTH_SHORT).show();
	            	Date endDate = getEndDate(pos);
	            	//Toast.makeText(getApplicationContext(), "End Date: "+endDate.toString(), Toast.LENGTH_SHORT).show();
	            	map.getOverlays().clear();
	            	map.invalidate();
	            	Room.drawRooms(map);
	            	User.mainUser.selectiveShow(startDate, endDate, map);
            	}
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
	    public static Date getStartDate(int pos)
	    {
	    	Date startDate = new Date();
	    	Calendar cal1 = Calendar.getInstance();
        	cal1.setTime(new Date());
        	switch(pos)
        	{
        		case 1:
        			//Today
                    break;
        		case 2:
        			//This Week
        			cal1.set((Calendar.DAY_OF_WEEK), cal1.getActualMinimum(Calendar.DAY_OF_WEEK));
        			break;
        		case 4:
        			//This Year
        			cal1.set(Calendar.MONTH, cal1.getActualMinimum(Calendar.MONTH));
        		case 3:
        			//This Month
        			cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMinimum(Calendar.DAY_OF_MONTH));
        			break;
        		
        	}
        	cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);
            startDate = cal1.getTime();
	    	return startDate;
	    }
	    public static Date getEndDate(int pos)
	    {
	    	Date endDate = new Date();
	    	Calendar cal1 = Calendar.getInstance();
        	cal1.setTime(new Date());
        	switch(pos)
        	{
        		case 1:
        			//Today
                    break;
        		case 2:
        			//This Week
        			cal1.set((Calendar.DAY_OF_WEEK), cal1.getActualMaximum(Calendar.DAY_OF_WEEK));
        			break;
        		case 4:
        			//This Year
        			cal1.set(Calendar.MONTH, cal1.getActualMaximum(Calendar.MONTH));
        		case 3:
        			//This Month
        			cal1.set(Calendar.DAY_OF_MONTH, cal1.getActualMaximum(Calendar.DAY_OF_MONTH));
        			break;
        		
        	}
        	cal1.set(Calendar.HOUR_OF_DAY, 23);
            cal1.set(Calendar.MINUTE, 59);
            cal1.set(Calendar.SECOND, 59);
            cal1.set(Calendar.MILLISECOND, 999);
    	
            endDate = cal1.getTime();
	    	return endDate;
	    }

	    
	    
}
