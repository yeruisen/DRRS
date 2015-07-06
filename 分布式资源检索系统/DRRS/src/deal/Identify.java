package deal;

import java.io.UnsupportedEncodingException;

public class Identify {
	public static boolean identify(String[] a) throws UnsupportedEncodingException{
		int i=0;
		for(i=0;i<a.length;i++){
			if(a[i].length()!=0)
				return false;
		}
		return true;
	}

}
