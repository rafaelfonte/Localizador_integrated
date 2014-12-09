package locator.localizadormq;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.*;

public class EventList extends ListActivity{
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String [] label = new String[]{"MAIN","SECONDARY"};
        int [] layoutID = new int[]{android.R.id.text1,android.R.id.text2};

        List<Map<String,String>> listOfValues = new ArrayList<Map<String, String>>(Event.allEvents.size());
        for (final Event ev: Event.allEvents) {
            final Map<String, String> listItemMap = new HashMap<String, String>();
            listItemMap.put(label[0], ev.name);
            listItemMap.put(label[1], ev.description + " (" + ev.ownerId + ")");
            listOfValues.add(Collections.unmodifiableMap(listItemMap));
        }
        ListAdapter adapter = new SimpleAdapter(this,listOfValues,android.R.layout.simple_expandable_list_item_2,label,layoutID);
        /*String[] values = new String[Event.allEvents.size()];
        for(int i=0;i<Event.allEvents.size();i++)
        {
        	values[i] = Event.getEvent(i).name;
        }
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
    	   //Toast.makeText(this, position + " selected", Toast.LENGTH_LONG).show();
    	   intent.putExtra("ID", position);
    	   startActivity(intent);
      }
    @Override
    public void onBackPressed(){
        setResult(1);
        super.onBackPressed();
    }
}

