package locator.localizadormq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.util.Log;
import android.widget.CheckedTextView;
import android.widget.Toast;
import com.mapquest.android.maps.MapView;
import locator.servcomm.ClientThread;
import locator.servcomm.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

public class User {
	public static String username;
    public static String password;
    public static ArrayList<Group> groups;
    public static ArrayList<Event> events; // Caso groups n�o seja implementado
    public static ArrayList<User> friends;
	
	static User mainUser;
	
	public User(String name, String pass)
	{
		username = name;
		password = pass;
		events = new ArrayList<Event>();
		groups = new ArrayList<Group>();
		friends = new ArrayList<User>();
	}
	public void addEvent(Event e)
	{
		events.add(e);
	}

	public void addFriend(User u)
	{
		friends.add(u);
	}
	public void addGroup(Group g)
	{
		groups.add(g);
	}
	public void selectiveShow(Date startDate, Date endDate, MapView map)
	{
		for(Event e: events)
		{
			if(e.date.after(startDate)&&e.date.before(endDate))
			{
				e.setSpecialRoom(map);
			}
		}
	}
	public void showNextEventRoom(Date now,MapView map,CheckedTextView tx)
	{
		if(events.size()>0)
		{
			Event chosenEvent = null;
			Event e;
			for(int i =0; i<events.size();i++)
			{
				e=events.get(i);
				if(e.date.after(now))
				{
					if(chosenEvent==null)
					{
						chosenEvent = e;
					}
					else
					{
						if(chosenEvent.date.after(e.date))
						chosenEvent = e;
					}
				}
			}
            if(chosenEvent != null){
			    chosenEvent.setSpecialRoom(map);
                SimpleDateFormat timeFormat = new SimpleDateFormat("EEE, dd/MM/yyyy HH:mm", Locale.getDefault());

                tx.setText(chosenEvent.name + "\n" + Room.getRoom(chosenEvent.roomId).name + "\n" + timeFormat.format(chosenEvent.date));
            }
		}
	}
	public boolean checkSubscription(Event e, Context ctx)
	{
        try{
            ClientThread auth = new ClientThread(ctx, Constants.ctGetAct,new String[]{username,password});
            auth.execute();
            String [] response = auth.get();
            if(response != null && response[0].equals(Constants.ctOkMsg)){
                return Constants.contains(e.id + 1,Constants.cvrtSubscriptions(response[2]));
            }
            else{
                Toast.makeText(ctx,response[1],Toast.LENGTH_LONG).show();
                return false;
            }
        }
        catch(Exception excep){
            System.out.println("Exception in checkSubscription: " + excep);
        }
        return events.contains(e);
	}

    public boolean subscribe(Event e, Context ctx){
        try{
            ClientThread subs = new ClientThread(ctx, Constants.ctSubsEventAct,new String[]{username,password,"" + (e.id+1)});
            subs.execute();
            String [] response = subs.get();
            if(response != null && response[0].equals(Constants.ctOkMsg)){
                Toast.makeText(ctx,response[1],Toast.LENGTH_LONG).show();
                events.add(e);
                return true;
            }
            else{
                Toast.makeText(ctx,response[1],Toast.LENGTH_LONG).show();
                return false;
            }
        }
        catch(Exception excep){
            System.out.println("Exception in checkSubscription: " + excep);
        }
        return false;
    }

	public boolean unsubscribe(Event e, Context ctx){
        try{
            ClientThread unsubs = new ClientThread(ctx, Constants.ctUnsubsEventAct,new String[]{username,password,"" + (e.id+1)});
            unsubs.execute();
            String [] response = unsubs.get();
            if(response != null && response[0].equals(Constants.ctOkMsg)){
                Toast.makeText(ctx,response[1],Toast.LENGTH_LONG).show();
                events.remove(e);
                return true;
            }
            else{
                Toast.makeText(ctx,response[1],Toast.LENGTH_LONG).show();
                return false;
            }
        }
        catch(Exception excep){
            System.out.println("Exception in checkSubscription: " + excep);
        }
        return false;
//		return events.remove(e);
//	}
	}

    public static void loadEvents(Context activity_ctx,String user, String pass){
        try{
            ClientThread events = new ClientThread(activity_ctx,Constants.ctGetAct,new String[]{user,pass});
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
                if(Constants.contains(handler.getInt("ID"),subscriptions)){
                    System.out.println("Yes! Contains! " + handler.getInt("ID"));
                    User.mainUser.addEvent(ev);
                }
            }
        }
        catch(Exception e){
            Log.e("MainActivity", "An exception occurred room fetch: ", e);
        }
    }

    public static void loadRooms(Context activity_ctx,String user, String pass){
        try{
            ClientThread rooms = new ClientThread(activity_ctx,Constants.ctGetRoomsAct,new String[]{user,pass});
            rooms.execute();
            String [] resultQuery = rooms.get();
            System.out.println("result query: " + resultQuery[0] + ";;;;" + resultQuery[1]);
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
    }

}
