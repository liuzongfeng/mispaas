package rest.page.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import net.sf.json.JSONObject;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
@Configuration
//@RestController
public class RequestUtil {
	private static String messageCenter="http://100.0.10.201:8080/messagecenter/api/message/count";
	private static String getUserUrl="http://100.0.10.100:8080/usermanager/api/authorization/users/";
	private static String getAlladminUrl="http://100.0.10.100:8080/usermanager/api/authorization/tenants";
//	@RequestMapping("/getContent")
//	public JSONObject getContent() throws IOException
//	{
	public JSONObject getContent(String userid) throws IOException
	{
		String geturl=getUserUrl+userid;
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
		return jo;
	}
	@RequestMapping("/getContent")
	public JSONObject getAlladminContent() throws IOException
	{
		URL url=new URL(getAlladminUrl);
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
		return jo;
	}
	public JSONObject gettenantsContent(String name) throws IOException
	{
		URL url=new URL(getAlladminUrl+"/query?tenantName="+name);
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
		return jo;
	}
	
	public JSONObject getmessageNum(JSONObject messages) throws IOException
	{
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> response = template.postForEntity(messageCenter, messages.toString(), String.class);
		JSONObject jo=JSONObject.fromObject(response.getBody()); 
		return jo;
	}
}
