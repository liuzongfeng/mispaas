package rest.page.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;
@Configuration
//@RestController
public class OrgRequestUtil {
	/*private static String getUserUrl="http://100.0.10.100:8080/usermanager/api/authorization/organizations?pageStart=1&pageSize=1000";*/
//	@RequestMapping("/getContent")
//	public JSONObject getContent() throws IOException
//	{
	public String getContent(String geturl) throws IOException
	{
		URL url=new URL(geturl);
		URLConnection uc=url.openConnection();
		HttpURLConnection connection = (HttpURLConnection)uc;
		InputStream is=connection.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String readline="";
		StringBuffer result=new StringBuffer();
		while((readline=br.readLine())!=null)
		{
			result.append(readline);
		}
		JSONObject jo=JSONObject.fromObject(result.toString()); 
		return result.toString();
	}
	
	public Map<String,Object> getContentMap(String geturl) throws IOException {
		
		URL url=new URL(geturl);
		URLConnection uc=url.openConnection();
		HttpURLConnection connection = (HttpURLConnection)uc;
		InputStream is=connection.getInputStream();
		BufferedReader br=new BufferedReader(new InputStreamReader(is));
		String readline="";
		StringBuffer result=new StringBuffer();
		while((readline=br.readLine())!=null)
		{
			result.append(readline);
		}
		JSONObject jo=JSONObject.fromObject(result.toString());
		
		Map<String,Object> map = new HashMap<String,Object>();
		for(Iterator iter = jo.keys(); iter.hasNext();){
			String key = (String)iter.next();
			map.put(key, jo.get(key));
		};
		return map;
	}
}
