package locator.localizadormq;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class EventList extends ListActivity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setContentView(R.layout.my_groups);
       /* String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
                */
        String[] values = new String[Event.allEvents.size()];
        for(int i=0;i<Event.allEvents.size();i++)
        {
        	values[i] = Event.getEvent(i).name;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,values);
                
        /*String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,values);
                */
            setListAdapter(adapter);
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // do something with the data
    	final Intent intent = new Intent(this, ShowEvent.class);
    	//String item = (String) getListAdapter().getItem(position);
    	   Toast.makeText(this, position + " selected", Toast.LENGTH_LONG).show();
    	   intent.putExtra("ID", position);
    	   startActivity(intent);
      }
}

