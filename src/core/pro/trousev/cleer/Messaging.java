package pro.trousev.cleer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Messaging {
	public interface Message
	{
		
	}
	public interface Event
	{
		void messageReceived(Message message);
	}
	@SuppressWarnings("rawtypes")
	private static Map<Class, List<Event> > _responders = new ConcurrentHashMap<Class, List<Event>>();
	@SuppressWarnings("rawtypes")
	public static void subscribe(Class type, Event event)
	{
		List<Event> events = _responders.get(type);
		if(events == null)
		{
			events = new ArrayList<Messaging.Event>();
			_responders.put(type, events);
		}
		events.add(event);
	}
	public static void fire(Message message)
	{
		List<Event> events = _responders.get(message.getClass());
		if(events != null) for(Event e: events)
			e.messageReceived(message);
	}
}
