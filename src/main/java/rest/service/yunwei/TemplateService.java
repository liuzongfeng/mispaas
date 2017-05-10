package rest.service.yunwei;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ho.yaml.Yaml;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@Controller
public class TemplateService {
	
	public static void main(String[] args) {
		String cc = "D:/aaa/PaaSSampleCatalog";
		//obtailYmlFile(cc);
		deleteDir("D:/aaa");
		File aaa = new File("D:/aaa");
		aaa.delete();
	}
	/**
	 * TODO 通过路径查找文件,用于遍历找到子文件
	 * @param filePath
	 */
	//查找要解析yml文件
	public  void obtailYmlFile(String filePath){
		
		File dir = new File(filePath);
		File [] fm = dir.listFiles();
		for(File file : fm){
			if(file.isDirectory()){
				obtailYmlFile(file.getAbsolutePath());
			}
			if(file.isFile()){
				String fileName = file.getName();
				
				Map<String,String> templateMap = new HashMap<String,String>();
				//--------1-------------------------//config.yml 文件
				String configFilePath = null;
				
				if(fileName.indexOf("config")!= -1){
					configFilePath = file.getAbsolutePath();   //预存config.yml文件绝对路径，为保存使用
					Map father = null;
					try {
						/*Object load = Yaml.load(file);
						System.out.println(load.toString());*/
						father = Yaml.loadType(file, LinkedHashMap.class);
						for(Object objkey:father.keySet()){
				            System.out.println(objkey+":\t"+father.get(objkey).toString());
				            
				            String tplId = (String)father.get("name");                            //模板id
				            String tplName = (String)father.get("description");                   //模板名称
				            String tplCategory = (String)father.get("category");                  //模板分类
				            
				            templateMap.put("tplId", tplId);
				            templateMap.put("tplName", tplName);
				            templateMap.put("tplCategory", tplCategory);
				            
				        }
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				Map<String,String> moduleNameMap = new HashMap<String,String>();           //暂存模块名称
				Map<String,String> moduleIdMap = new LinkedHashMap<String,String>();       //暂存模块id 还模板id
				
				
				//-------------------------2-----------------docker-compose.yml
				if(fileName.indexOf("docker-compose")!= -1){                                 
					System.out.println("----------------------------------------");
					Map father = null;
					try {
						/*Object load = Yaml.load(file);
						System.out.println(load.toString());*/
						father = Yaml.loadType(file, LinkedHashMap.class);                       //template
						for(Object objkey:father.keySet()){ 
				            System.out.println(objkey+":\t"+father.get(objkey).toString());
				            
				            String strkey = (String)objkey;
				            if(strkey.equals("services")){
				            	
				            	Map servicesMap = (Map)father.get("services");                       //services
					            for(Object serviceKey : servicesMap.keySet()){
					            	System.out.println("-----------"+serviceKey.toString()+"-----------------------------");
					            	Map serviceMap = (Map)servicesMap.get(serviceKey);               //service
					            	Map labelsMap = (Map)serviceMap.get("labels");                   //labels
					            	 
					            	for(Object labelKey : labelsMap.keySet()){
					            		//解析labels下面的内容保存到模板
					            		System.out.println(labelKey+":\t"+labelsMap.get(labelKey).toString());
					            		
					            		//1.处理全局变量
					            		String tpltype =(String)labelsMap.get("com.dayang.paas.tpltype");             //模板类型
					            		String sharemode = (String)labelsMap.get("com.dayang.paas.sharemode");        //模板使用模式
					            		String version = (String)labelsMap.get("com.dayang.paas.version");            //模板版本号
					            		
					            		templateMap.put("tpltype", tpltype);
					            		templateMap.put("sharemode", sharemode);
					            		templateMap.put("version", version);
					            		templateMap.put("price", null);
					            		templateMap.put("ifPub", null);
					            		
					            		//调用dao保存 模板：template ,返回模板数据库templateId ,
					            		//<"templateId",templateId> 存放moduleIdMap
					            		
					            		
					            		//2.解析每个服务对应的模块
					            		String modules = (String)labelsMap.get("com.dayang.paas.subservices");   //获得模块的名称、url、是否展示
					            		if(modules.indexOf(";")!= -1){//对应多个子模块
					            			String[] module_array = modules.split(";");
					            			for(String module : module_array){
					            				String[] moduleContent = module.split(",");
					            				String moduleName = moduleContent[0];                       //模块名称
					            				String moduleUrl = moduleContent[1];                        //模块Url
					            				String moduleIsShow = moduleContent[2];                     //模块是否展示
					            				String moduleDesKey = "com.dayang.paas.subservice."+moduleName;
					            				String moduleDes = (String)labelsMap.get("moduleDesKey");   //模块描述
					            				
					            				moduleNameMap.put(moduleName, moduleName);
					            				
					            				//调用dao层保存模块 ：Module,返回ModuleId
					            				//<moduleName,ModuleId>存放到 moduleIdMap
					            				
					            			}
					            		}else{//只有一个子模块
					            			String[] moduleContent = modules.split(",");
					            			String moduleName = moduleContent[0];                       //模块名称
				            				String moduleUrl = moduleContent[1];                        //模块Url
				            				String moduleIsShow = moduleContent[2];                     //模块是否展示
				            				
				            				String moduleDesKey = "com.dayang.paas.subservice."+moduleName;
				            				String moduleDes = (String)labelsMap.get("moduleDesKey");   //模块描述
				            				
				            				moduleNameMap.put(moduleName, moduleName);
				            				//调用dao层保存模块 ：Module,返回ModuleId
				            				//<moduleName,ModuleId>存放到 moduleIdMap
					            		}
					            		
					            		
					            		
					            	}
					            }
				            }
				            
				        }
						String templateId = moduleIdMap.get("templateId");   //模板文件id
						//----------------------保存docker.yml文件
						String dockerName = file.getName();
						//文件 file 本身
						//模块id 为null
						//创建docker文件 ，调用dao保存docker.yml文件
						
						
					    //--------------保存config.yml文件
						File configFile = new File(configFilePath);
						if(configFile.exists()){
							String configName = configFile.getName();   //config.yml文件名称
							//文件 configFile 本身
							//模块id 为null
							//创建config文件，调用dao保存config.yml文件
							
						}
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				// --------------------------3----------------处理图片文件
				if(fileName.indexOf(".png") != -1 || fileName.indexOf(".img") != -1 || fileName.indexOf(".jpeg") != -1){   
					//遍历模块的名
					if(!moduleNameMap.isEmpty()){
						
						for(String imgNameKey : moduleNameMap.keySet()){
							
							String imgNamePart = moduleNameMap.get(imgNameKey);
							if(fileName.indexOf(imgNamePart) != -1){
								//找到对应的模块id
								String moduleId = moduleIdMap.get(imgNameKey);  //模块id
								//fileName ---  图片名称
								//file    -----图片
								
								//         ----模板id
								String templateId = moduleIdMap.get("templateId");
								
								//创建图片文件，调用dao 保存图片文件
								
							}
						}
					}
				}
				
				//-------------------------4 ----------           .svg文件
				if(fileName.indexOf(".svg") != -1){   
					
					//fileName ---  图片名称
					//file    -----图片
					
					//         ----模板id 为null
					String templateId = moduleIdMap.get("templateId");
					
					// 创建svg文件对象 ，调用dao 保存图片文件
				}
				
			}
		}
		
		
	}
	
	
	//解压.zip文件
	public void unzip(File zipFile,String dest){
		
		try {
			ZipFile zFile =new ZipFile(zipFile); //首先创建ZipFile指向磁盘上的.zip文件
			zFile.setFileNameCharset("GBK");
			if(!zFile.isValidZipFile()){
				throw new ZipException("压缩文件不合法");
			}
			File destDir = new File(dest);
			if(destDir.isDirectory() && !destDir.exists()){
				destDir.mkdir();
			}
			
			zFile.extractAll(dest);
			
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/testUploadFile", method = RequestMethod.POST)
	public void testUploadFile(HttpServletRequest req,
	    MultipartHttpServletRequest multiReq) {
		
	
		//-----------------------------------------上传文件到服务器暂存---------
	    // 获取上传文件的路径
	    String uploadFilePath = multiReq.getFile("file1").getOriginalFilename();
	    System.out.println("uploadFlePath:" + uploadFilePath);
	    // 截取上传文件的文件名
	    String uploadFileName = uploadFilePath.substring(
	        uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.indexOf('.'));
	    System.out.println("multiReq.getFile()" + uploadFileName);
	    // 截取上传文件的后缀
	    String uploadFileSuffix = uploadFilePath.substring(
	        uploadFilePath.indexOf('.') + 1, uploadFilePath.length());
	    System.out.println("uploadFileSuffix:" + uploadFileSuffix);
	    File zFile = new File("D:/uploadFiles//" + uploadFileName
		          + "."+ uploadFileSuffix);
	    
	    FileOutputStream fos = null;
	    FileInputStream fis = null;
	    try {
	      fis = (FileInputStream) multiReq.getFile("file1").getInputStream();
	      fos = new FileOutputStream(zFile);
	      byte[] temp = new byte[1024];
	      int i = fis.read(temp);
	      while (i != -1){
	        fos.write(temp,0,temp.length);
	        fos.flush();
	        i = fis.read(temp);
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    } finally {
	      if (fis != null) {
	        try {
	          fis.close();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
	      if (fos != null) {
	        try {
	          fos.close();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
	      try {
			//---------------------------------解压该文件 到指定目录-------
			  unzip(zFile,"D:/aaa");
			  
			  //---------------------------------读取目录文件----------------
			  obtailYmlFile("D:/aaa/"+uploadFileName);
			  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			//-------------------------------删除暂存的文件-----------------------
		      
		      zFile.delete();
		     
		      deleteDir("D:/aaa");
		      
		      File aaa = new File("D:/aaa");
		      aaa.delete();
		      System.out.println("===========逻辑处理完毕=============");
		}
	      
	      
	      
	      
	      
	    }
	  }
	
	public static void deleteDir(String dirPath) {
		File f = new File(dirPath);
        if (f.isDirectory()) {
            File[] listFiles = f.listFiles();
            for(File f1 : listFiles){
            	if(f1.isDirectory()){
            		
            		deleteDir(f1.getAbsolutePath());
            	}
            	System.out.println("---删除："+f1.getName());
            	f1.delete();
            }
            
        }
    }


}
