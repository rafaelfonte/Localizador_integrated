package locator.localizadormq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.mapquest.android.maps.MapView;

public class Event {
	//Room room;
	String name;
    String description;
    int id;
	int roomId;
    String ownerId;
    int capacity;
    int capacity_taken;
    boolean isPrivate;
	Date date;
	Date duration;
	//Date?
	static ArrayList<Event> allEvents = new ArrayList<Event>();
	public Event(int rm, String dateString)
	{
		roomId = rm;
		name = "Event"+roomId;
		//date = new Date("2014-12-25 14:00:00");
		//date = Date.valueOf("2014-12-25 14:00:00");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		try
		{
			//date = simpleDateFormat.parse("2014-12-25 14:00:00");

			date = simpleDateFormat.parse(dateString);
		}
		catch (Exception e)
		{
			System.out.println("Problem parsing date");
		}
			//System.out.println(date);
		allEvents.add(this);
	}
	public Event(int rm, String dateString,String durationString)
	{
		roomId = rm;
		name = "Event"+roomId;
		//date = new Date("2014-12-25 14:00:00");
		//date = Date.valueOf("2014-12-25 14:00:00");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
		SimpleDateFormat simpleDurationFormat = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
		try
		{
			date = simpleDateFormat.parse(dateString);
			duration = simpleDurationFormat.parse(durationString);
		}
		catch (Exception e)
		{
			System.out.println("Problem parsing date");
		}
			//System.out.println(date);
		allEvents.add(this);
	}

    public Event(int id, int room, String ownerId, String name, String description, int capacity, int capacity_taken, String dateString,String durationString, boolean isPrivate)
    {
        this.roomId = room;
        this.name = name;
        this.ownerId = ownerId;
        this.id = id;
        this.description = description;
        this.capacity = capacity;
        this.capacity_taken = capacity_taken;
        this.isPrivate = isPrivate;
        //date = new Date("2014-12-25 14:00:00");
        //date = Date.valueOf("2014-12-25 14:00:00");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        SimpleDateFormat simpleDurationFormat = new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        try
        {
            date = simpleDateFormat.parse(dateString);
            duration = simpleDurationFormat.parse(durationString);
        }
        catch (Exception e)
        {
            System.out.println("Problem parsing date");
        }
        //System.out.println(date);
        allEvents.add(this);
    }
	public static Event getEvent(int id)
	{
		return allEvents.get(id);
	}
	public void setSpecialRoom(MapView map)
	{
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yy HH:mm",Locale.getDefault());
        String str = timeFormat.format(date);
		Room.getRoom(roomId).drawSpecial(str,map);
	}

}
