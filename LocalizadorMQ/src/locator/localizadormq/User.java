package locator.localizadormq;

import java.util.ArrayList;
import java.util.Date;

import com.mapquest.android.maps.MapView;

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
	public void showNextEventRoom(Date now,MapView map)
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
            if(chosenEvent != null)
			    chosenEvent.setSpecialRoom(map);
		}
	}
	public boolean checkSubscription(Event e)
	{
        return events.contains(e);
	}
	public boolean unsubscribe(Event e)
	{
        /*for(int i = 0; i < events.size(); i++){
            if(e.id == events.get(i).id){
                events.remove(i);
                return true;
            }
        }
        return false;*/
		return events.remove(e);
	}
	
}
