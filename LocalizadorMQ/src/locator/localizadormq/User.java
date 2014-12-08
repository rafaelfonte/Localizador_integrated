package locator.localizadormq;

import java.util.ArrayList;
import java.util.Date;

import com.mapquest.android.maps.MapView;

public class User {
	String username;
	String password;
	ArrayList<Group> groups;
	ArrayList<Event> events; // Caso groups não seja implementado
	ArrayList<User> friends;
	
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
				Room.getRoom(e.roomId).draw(map);
			}
		}
	}
	
	//Função para procurar os eventos num determinado intervalo de tempo
	
}
