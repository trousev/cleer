package pro.trousev.jplay.commands;

import java.text.Collator;
import java.util.Locale;

import pro.trousev.jplay.Plugin;

public abstract class Command implements Plugin.Command{
	public int compareTo(Plugin.Command compareObject)
	{
	    Collator collator = Collator.getInstance(Locale.ENGLISH);
	    collator.setStrength(Collator.SECONDARY);
	    return collator.compare(name(), compareObject.name());
	}

}
