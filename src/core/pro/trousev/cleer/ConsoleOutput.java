package pro.trousev.cleer;

import java.util.List;

public interface ConsoleOutput {
	//public boolean write
	public interface Callback
	{
		public void trigger(Plugin.Interface iface, Object context);
	}
	enum Type
	{
		MessageTypeError, MessageTypeInfo, MessageTypeQuestion, MessageTypeWarning
	}
	public void printMessage(String message, Type messageType, Callback dismissAction);
	public void printException(Throwable t, String additionalMessage, Callback dismissAction);
	public void printTrackList(List<Item> tracks, int selected_track, Callback songSelectAction);
	public void printTrackList(Playlist tracks, int selected_track, Callback songSelectAction);
}
