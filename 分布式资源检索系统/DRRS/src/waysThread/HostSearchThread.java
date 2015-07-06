package waysThread;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import search.Search;

public class HostSearchThread implements Callable<Map<List<String>,Float>>{

	private String str1;
	private String []str2;
	private String []str3;
	private Map<List<String>,Float> IPMap;
	public HostSearchThread(String str1,String []str2,String []str3) {
		this.str1=str1;
		this.str2=str2;
		this.str3=str3;
		this.IPMap=new LinkedHashMap<List<String>,Float>();
	}

public Map<List<String>, Float> ipWays() {
		
		//查询远程主机的资源
			Search sea;
			try {
				sea = new Search();
				if(this.str1!=null){
				this.IPMap.putAll(sea.search(this.str1, this.str2));        //普通搜索
			}
			else{
				this.IPMap.putAll(sea.preciseSearch(this.str2, this.str3));    //精确搜索
			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return IPMap;
	}
	@Override
	public Map<List<String>, Float> call() throws Exception {
		// TODO Auto-generated method stub
		return ipWays();
	}

}
