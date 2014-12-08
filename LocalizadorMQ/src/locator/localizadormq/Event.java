package locator.localizadormq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Event {
	//Room room;
	String name;
	int roomId;
	Date date;
	//Date?
	static ArrayList<Event> allEvents = new ArrayList<Event>();
	public Event(int rm, String dateString)
	{
		roomId = rm;
		name = "Event"+roomId;
		//date = new Date("2014-12-25 14:00:00");
		//date = Date.valueOf("2014-12-25 14:00:00");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
	public static Event getEvent(int id)
	{
		return allEvents.get(id);
	}

}
