package pro.trousev.cleer.android.service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class RusTag {
	public String change(String s){
		if (s == null)
			return null;
		if (s.isEmpty())
			return s;
		Charset C= Charset.forName("windows-1251");
		ByteBuffer buffer=null;
		try{
				if ((int)s.charAt(0)<0400){
				return C.decode(buffer.wrap(s.getBytes("UTF-16LE"))).toString();}
				else return s;
		}catch(UnsupportedEncodingException e){
			return s;
		}
	}
}
