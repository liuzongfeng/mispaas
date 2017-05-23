package rest.page.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import net.sf.json.JSONObject;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
