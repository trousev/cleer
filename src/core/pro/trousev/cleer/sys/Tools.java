package pro.trousev.cleer.sys;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class Tools {
	public static String Serialize(Serializable object)
	{
		try {
			ByteArrayOutputStream serializator = new ByteArrayOutputStream(256);
			ObjectOutputStream oos = new ObjectOutputStream(serializator);
			oos.writeObject(object);
			oos.flush();
			oos.close();
			return Base64.encode(serializator.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	public static <Type> Type Deserialize(String contents)
	{
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(Base64.decode(contents));
			ObjectInputStream iis = new ObjectInputStream(in);
			Object ans = (Type) iis.readObject();
			return (Type) ans;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

    public static String StringJoin( List<String> list , String replacement  ) {
        StringBuilder b = new StringBuilder();
        for( String item: list ) { 
            b.append( replacement ).append( item );
        }
        return b.toString().substring( replacement.length() );
    }
	public static String playlist_hash(String name)
	{
		return "pl"+Hash.hash(name);
	}


}
