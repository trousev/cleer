package pro.trousev.cleer.android.service;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class RusTag {
	public String change(String s){
		Charset C= Charset.forName("windows-1251");
		ByteBuffer buffer=null;
		try{
			return (C.decode(buffer.wrap(s.getBytes("UTF-16LE")))).toString();
		}catch(UnsupportedEncodingException e){
			return s;
		}
	}
}
