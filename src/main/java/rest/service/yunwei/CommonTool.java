package rest.service.yunwei;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Component;

public class CommonTool{
	
	public static String obtainUrl(String needKey){
		
		Map<String, String> getenv = System.getenv();
		System.out.println("this is getenv:"+getenv);
		return getenv.get(needKey);
	}
	
	/**
	 * 
	 * @param file to byte[]
	 * @return
	 */
	public static byte[] File2byte(File file){
		byte [] buffer = null;
		
		try {
			FileInputStream fis = new FileInputStream(file);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte [] b = new byte[1024];
		int n =0;
		while((n = fis.read(b)) != -1){
			bos.write(b, 0, n);
		}
		fis.close();
		bos.close();
		buffer = bos.toByteArray();
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer;
	}
	
	/* public static void byte2File(byte[] buf, String filePath, String fileName)  
	    {  
	        BufferedOutputStream bos = null;  
	        FileOutputStream fos = null;  
	        File file = null;  
	        try  
	        {  
	            File dir = new File(filePath);  
	            if (!dir.exists() && dir.isDirectory())  
	            {  
	                dir.mkdirs();  
	            }  
	            file = new File(filePath + File.separator + fileName);  
	            fos = new FileOutputStream(file);  
	            bos = new BufferedOutputStream(fos);  
	            bos.write(buf);  
	        }  
	        catch (Exception e)  
	        {  
	            e.printStackTrace();  
	        }  
	        finally  
	        {  
	            if (bos != null)  
	            {  
	                try  
	                {  
	                    bos.close();  
	                }  
	                catch (IOException e)  
	                {  
	                    e.printStackTrace();  
	                }  
	            }  
	            if (fos != null)  
	            {  
	                try  
	                {  
	                    fos.close();  
	                }  
	                catch (IOException e)  
	                {  
	                    e.printStackTrace();  
	                }  
	            }  
	        }  
	    }  */
}
	
	

