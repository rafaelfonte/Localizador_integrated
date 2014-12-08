package locator.localizadormq;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mapquest.android.maps.GeoPoint;

public class ShowEvent extends BasicMap{
	
	@Override
    protected void init() {
		Intent intent = getIntent();
		
		int id = intent.getIntExtra("ID", -1);
		Toast.makeText(getApplicationContext(), "Event ID:"+ id, Toast.LENGTH_SHORT).show();
		Event showedEvent = Event.getEvent(id);
		TextView name = (TextView) findViewById(R.id.showEventName);	
		name.setText(showedEvent.name);
		Room room = Room.getRoom(showedEvent.roomId);
		this.setupMapView(new GeoPoint(room.baseLat,room.baseLng), 18);
		Room.drawRooms(map);
		room.draw(map);
		TextView roomName = (TextView) findViewById(R.id.showEventRoom);
		roomName.setText(room.name);
		TextView date = (TextView) findViewById(R.id.showEventDate);	
		date.setText(showedEvent.date.toString());
    	
    	
    	
    	final Intent test = new Intent(this, EnrichedMap.class);
        
        final Button button2 = (Button) findViewById(R.id.test);
        button2.setOnClickListener(
        		new View.OnClickListener() 
        		{
        				public void onClick(View v) 
        				{
        					Toast.makeText(getApplicationContext(), "Going to List Events", Toast.LENGTH_SHORT).show();
        					startActivity(test);
        				}
        		}
        );
      
    }
	
	@Override
	protected int getLayoutId() {
	    return R.layout.show_event;
	}
}
