/**
Класс Messaging, позволяющий посылать и получать сообщения.

Использование состоит из двух use-case-ов:

1. Посылка сообщения.

Для этого надо создать объект-сообщение, который и будет посылаться. Это должна быть имплементация Messaging.Event

    class MyEvent implements Messaging.Event
    {
        public String message;
    }

После этого внутри кода можно слать объекты подобного типа:

MyEvent event = new MyEvent ;
event.message = "Hello, World!";
Messaging.fire(event);

Обратите внимание -- _кому_ слать сообщение -- не указано. Каждый класс сам решает, что он хочет получать.

2. Получение сообщений.

Для того, чтобы получать сообщения, класс должен (в конструкторе, например) _подписаться_ на определенный тип сообщений. Для этого следует использовать

    Messaging.subscribe(MyEvent.class, new Event() {
        @Override
        public void messageReceived(Message message) {
            MyEvent sender =  (MyEvent) message;
            System.out.println(sender.message);
        }
    });


Сообщение будет получено всеми подписавшимися классами.

 */
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