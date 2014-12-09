package locator.localizadormq;

import java.text.SimpleDateFormat;
import java.util.Locale;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mapquest.android.maps.GeoPoint;

public class ShowEvent extends BasicMap{
	
	Button subscribeButton;
	TextView subscribeText;
	TextView dateText;
	TextView durationText;
	TextView roomName;
	TextView name;
	Event showedEvent;
	Intent intent;
	
	@Override
    protected void init() {
		intent = getIntent();
		
		int id = intent.getIntExtra("ID", -1);
		Toast.makeText(getApplicationContext(), "Event ID:"+ id, Toast.LENGTH_SHORT).show();
		showedEvent = Event.getEvent(id);
		name = (TextView) findViewById(R.id.showEventName);	
		name.setText(showedEvent.name);
		Room room = Room.getRoom(showedEvent.roomId);
		this.setupMapView(new GeoPoint(room.baseLat,room.baseLng), 18);
		Room.drawRooms(map);
		room.draw(map);
		roomName = (TextView) findViewById(R.id.showEventRoom);
		roomName.setText(room.name);
		
		dateText = (TextView) findViewById(R.id.showEventDate);	
		durationText = (TextView) findViewById(R.id.showEventDuration);	
		SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yy HH:mm",Locale.getDefault());
		SimpleDateFormat durationFormat = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
		
		String str = timeFormat.format(showedEvent.date);
		String durationStr = durationFormat.format(showedEvent.duration);
		dateText.setText(str);
		durationText.setText("Total duration: " + durationStr);
		
    	boolean subscribed = User.mainUser.checkSubscription(showedEvent);
    	subscribeButton = (Button) findViewById(R.id.subscribeButton);
    	subscribeText = (TextView) findViewById(R.id.subscribeText);
    	
    	if(subscribed)
    		setUnsubscribeButton();
    	else
    		setSubscribeButton();
    	
      	
    }
	
	private void setSubscribeButton()
	{
		subscribeText.setText("Not Subscribed");
		subscribeButton.setText("    Subscribe    ");
    	subscribeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
          	  Toast.makeText(getApplicationContext(), "Subscribing...", Toast.LENGTH_SHORT).show();
          	  User.mainUser.unsubscribe(showedEvent);
              System.out.println("The result for unsubscription is " + User.mainUser.checkSubscription(showedEvent));
          	  setUnsubscribeButton();
            }
        });
	}
	
	private void setUnsubscribeButton()
	{
		subscribeText.setText("    Subscribed    ");
		subscribeButton.setText("   Unsubscribe   ");
    	subscribeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
          	  Toast.makeText(getApplicationContext(), "Unsubscribing...", Toast.LENGTH_SHORT).show();
          	  User.mainUser.addEvent(showedEvent);
                System.out.println("The result for subscription is " + User.mainUser.checkSubscription(showedEvent));
          	  setSubscribeButton();
            }
        });
	}
	
	@Override
	protected int getLayoutId() {
	    return R.layout.show_event;
	}
}
