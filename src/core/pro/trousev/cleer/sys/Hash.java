package pro.trousev.cleer.sys;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.Provider.Service;
import java.util.ArrayList;
import java.util.List;


public class Hash 
{
	public static String hashMethod = "SHA1";
    private static String convertToHex(byte[] data) 
    { 
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) { 
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do { 
                if ((0 <= halfbyte) && (halfbyte <= 9)) 
                    buf.append((char) ('0' + halfbyte));
                else 
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = data[i] & 0x0F;
            } while(two_halfs++ < 1);
        } 
        return buf.toString();
    } 
    public static String[] hashVariants()
    {
    	List<String> list = new ArrayList<String>();
    	for(Provider prov : Security.getProviders())
    	{
    		//list.add(prov.getName());
    		for(Service s : prov.getServices())
    		{
    			if(s.getType().equals("MessageDigest") )
    				list.add(s.getAlgorithm());
    		}
    	}
    	String[] rez = new String[list.size()];
    	list.toArray(rez);
    	return rez;
    }
    public static String hash(byte[] text, String algorithm)
    {
	    try 
	    {
		    MessageDigest md;
			md = MessageDigest.getInstance(algorithm);
		    byte[] sha1hash = new byte[40];
		    md.update(text, 0, text.length);
		    sha1hash = md.digest();
		    return convertToHex(sha1hash);
		} 
	    catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
    public static String hash(String text, String algorithm)
    {
	    try 
	    {
		    MessageDigest md;
			md = MessageDigest.getInstance(algorithm);
		    byte[] sha1hash = new byte[40];
		    md.update(text.getBytes(), 0, text.length());
		    sha1hash = md.digest();
		    return convertToHex(sha1hash);
		} 
	    catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
			return null;
		}
    	
    }
    public static String hash(String text)  
    {
    	return hash(text, hashMethod);
    } 
	
}
