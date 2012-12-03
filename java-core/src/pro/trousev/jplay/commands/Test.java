package pro.trousev.jplay.commands;

import java.io.PrintStream;
import java.util.List;
import org.jaudiotagger.audio.mp4.EncoderType;


import pro.trousev.jplay.Plugin.Interface;
import pro.trousev.jplay.Queue.EnqueueMode;
import pro.trousev.jplay.Track;

public class Test extends Command{

	@Override
	public String name() {
		return "test";
	}

	@Override
	public String help(boolean is_short) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean main(List<String> args, PrintStream stdout, Interface iface) {
		List<Track> pl = iface.library().playlist("music");
		iface.queue().enqueue(pl, EnqueueMode.Immidiaely);
			
		return false;
	}
	
}
