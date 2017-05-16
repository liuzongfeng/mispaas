package rest.service.yunwei;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ho.yaml.Yaml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import rest.mybatis.dao.passDao.PaasInstanceMapper;
import rest.mybatis.dao.passDao.PaasSubserviceMapper;
import rest.mybatis.dao.passDao.PaasTemplateFileMapper;
import rest.mybatis.dao.passDao.PaasTemplateMapper;
import rest.mybatis.model.passModel.PaasInstance;
import rest.mybatis.model.passModel.PaasSubservice;
import rest.mybatis.model.passModel.PaasTemplate;
import rest.mybatis.model.passModel.PaasTemplateFile;

@Controller
public class TemplateService<T> {
	
	/**
	 * 注入dao
	 */
	@Autowired
	private PaasTemplateMapper paasTemplateMapper;
	@Autowired
	private PaasSubserviceMapper paasSubserviceMapper;
	@Autowired
	private PaasTemplateFileMapper paasTemplateFileMapper;
	@Autowired
	private PaasInstanceMapper paasInstanceMapper;
	
	
	public static void main(String[] args) {
		/*String cc = "D:/aaa/PaaSSampleCatalog";
		
		File aaa = new File("D:/aaa");
		aaa.delete();*/
		
		
	}
	/**
	 * TODO 通过路径查找文件,用于遍历找到子文件
	 * @param filePath
	 */
	private PaasTemplate passTemplate= null;    //template对象，---存放数据
	private String configFilePath = null;                     //存放config.yml文件的绝对路径 ，为了读取并保存。
	private Map<String,Integer> moduleIdMap = null;       //暂存模块id 还模板id
	private Map<String,String> moduleNameMap = null;           //暂存模块名称
	private Map<String,String> messageMap = null;
	
	////////////////////////////////接口区域：start//////////////////////////////////////////////////////////////////////////
	
	 
	
	
	
	
	
	/**
	 * TODO 导入模板 接口
	 * @param req
	 * @param multiReq
	 */
	@RequestMapping(value = "/testUploadFile", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public synchronized String  testUploadFile(HttpServletRequest req,
	    MultipartHttpServletRequest multiReq) {
		messageMap = new HashMap<String,String>();         //处理返回逻辑
		String uploadMessage = "";
		FileOutputStream fos = null;
	    FileInputStream fis = null;
	    File zFile = null;
		try {
			//-----------------------------------------上传文件到服务器暂存---------
			String overWriteExist = req.getParameter("overWriteExist");
			System.out.println(overWriteExist);
		    // 获取上传文件的路径
			MultipartFile uploadzipfile = multiReq.getFile("file1");
			if(null == uploadzipfile){
				throw new Exception("系统异常，导入文件失败");
			}
		    String uploadFilePath = uploadzipfile.getOriginalFilename();
		    System.out.println("uploadFlePath:" + uploadFilePath);
		    // 截取上传文件的文件名
		    String uploadFileName = uploadFilePath.substring(
		        uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.indexOf('.'));
		    System.out.println("multiReq.getFile()" + uploadFileName);
		    // 截取上传文件的后缀
		    String uploadFileSuffix = uploadFilePath.substring(
		        uploadFilePath.indexOf('.') + 1, uploadFilePath.length());
		    
		    if(null != uploadFileSuffix && uploadFileSuffix.indexOf("zip") == -1){
		    	throw new Exception("压缩文件不合法");
		    }
		    
		    //------------------------打印语句===================
		    System.out.println("uploadFileSuffix:" + uploadFileSuffix);
		    
		    
		    //将文件先暂存本地磁盘，之后删除
		    zFile = new File("D:/uploadFiles//" + uploadFileName + "."+ uploadFileSuffix);
		    fis = (FileInputStream) uploadzipfile.getInputStream();
		    fos = new FileOutputStream(zFile);
		    byte[] temp = new byte[1024];
		    int i = fis.read(temp);
		    while (i != -1){
		    	fos.write(temp,0,temp.length);
		    	fos.flush();
		    	i = fis.read(temp);
		    }
		    //---------------------------------解压该文件 到指定目录-------
		    unzip(zFile,"D:/aaa");
		    //---------------------------------读取目录文件----------------
		    obtailYmlFile("D:/aaa/"+uploadFileName);
		    return "uploadOK";
		  
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String message = e.getMessage();
			
			return message;
			
		}finally{
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
	      //-------------------------------删除暂存的文件-----------------------
	      
	      try {
	    	 zFile.delete();
			 deleteDir("D:/aaa");
			 File aaa = new File("D:/aaa");
		     aaa.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	     
	      //------------------------------删除未用到的template
	      //-------交给定时任务，存储过程删掉没有templateId 的template
	      System.out.println("===========逻辑处理完毕=============");
		}
	  }
	
	/**
	 * TODO 查询模板列表
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/obtainTemplateList", method = RequestMethod.GET)
	@ResponseBody
	public PageInfo obtainTemplateList(HttpServletRequest req){
		
		String pageNo = req.getParameter("pageNo");
		String pageSize = req.getParameter("pageSize");
		Integer intPageNo = 1;
		Integer intpageSize = 10;
		if(null != pageSize && null != pageNo){
			intPageNo = Integer.parseInt(pageNo);
			intpageSize = Integer.parseInt(pageSize);
		}
		
		
		return queryListByPage(null,intPageNo,intpageSize);
		
	}
	
	@RequestMapping(value = "/deleteTemplateByTemplateId", method = RequestMethod.GET)
	@ResponseBody
	public String deleteTemplateById(String templateId){
		
		if(null == templateId){
			return "未获取对应模板";
		}
		
		//1.根据模板id,查询是否存在实例：有-->提示有实例，无-->直接删除：模板、子服务、对应文件
		List<PaasInstance> instances = paasInstanceMapper.selectInstanceByTemplateId(templateId);
		if(null != instances && instances.size() > 0){
			return "该模板有实例，不能够删除";
		}else{
			try {
				//1.根据模板id查询子服务,并删除
				List<PaasSubservice> subServices = paasSubserviceMapper.selectSubServiceByTPlId(templateId);
				if(null != subServices && subServices.size() >0){
					for(PaasSubservice subService : subServices){
						paasSubserviceMapper.deleteByPrimaryKey(subService.getId());
					}
				}
				//2.根据模板id查询文件,并删除
				List<PaasTemplateFile> templateFiles = paasTemplateFileMapper.selectByTemplateId(templateId);
				if(null != templateFiles && templateFiles.size() >0){
					for(PaasTemplateFile paasTemplateFile : templateFiles){
						paasTemplateFileMapper.deleteByPrimaryKey(paasTemplateFile.getId());
					}
				}
				//3.根据模板id查询模板，并删除
				PaasTemplate template = paasTemplateMapper.selectByPrimaryKey(Integer.parseInt(templateId));
				if(null != template){
					paasTemplateMapper.deleteByPrimaryKey(template.getId());
				}
				return "deleteok";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "系统异常，删除失败";
			}
			
		}
		
	}
	
	/**
	 * TODO 根据模板id 查询模块列表
	 * @param templateId
	 * @return
	 */
	@RequestMapping(value = "/obtainSubServiceByTemplateId", method = RequestMethod.GET)
	@ResponseBody
	public List<PaasSubservice> obtainSubServiceByTemplateId(String templateId){
		
		if(null == templateId){
			return new ArrayList<PaasSubservice>();
		}
		
		//调用dao查询PaasSubservice 列表
		List<PaasSubservice> subServices = paasSubserviceMapper.selectSubServiceByTPlId(templateId);
		
		return subServices != null ? subServices : new ArrayList<PaasSubservice>(); 
	}
	
	////////////////////////////////内部方法：start//////////////////////////////////////////////////////////////////////////
	public PageInfo queryListByPage(String templateName, Integer pageNo,Integer pageSize) {
		
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo, pageSize);
		
		List<PaasTemplate> templateList = paasTemplateMapper.obtainTemplateList();
		PageInfo pageInfo = new PageInfo(templateList);
		
		//测试PageInfo全部属性
		System.out.println(pageInfo.getPageNum());
		System.out.println(pageInfo.getPageSize());
		System.out.println(pageInfo.getStartRow());
		System.out.println(pageInfo.getEndRow());
		System.out.println(pageInfo.getTotal());
		System.out.println(pageInfo.getPages());
		System.out.println(pageInfo.getList());
		
		return pageInfo;
		
	}
	
	
	
	/**
	 * TODO 解析模板文件
	 * @param filePath
	 * @throws Exception
	 */
	public void obtailYmlFile (String filePath) throws Exception{
		try{
			File dir = new File(filePath);
			File [] fm = dir.listFiles();
			//先对要解析的文件进行排序
			
			if(null != fm && fm.length > 0){
				for(int fm_i =0;fm_i < fm.length; fm_i ++){
					File file = fm[fm_i];
					if(file.isDirectory()){
						obtailYmlFile(file.getAbsolutePath());
					}
					if(file.isFile()){
						String fileName = file.getName();
						if(fileName.indexOf("config")!= -1){
							File t0 = fm[0] ;
							fm[0] = fm[fm_i];
							fm[fm_i] = t0;
						}
						if(fileName.indexOf("docker-compose")!= -1){
							File t1 = fm[1] ;
							fm[1] = fm[fm_i];
							fm[fm_i] = t1;
						}
						
						
					}
				}
			
				for(File file : fm){
					if(file.isFile()){
						String fileName = file.getName();
						
						
						//-----------------1-------------------------//config.yml 文件
						if(fileName.indexOf("config")!= -1){
							//预先创建模板，获得模板id,
							passTemplate = new PaasTemplate();
							int templateResult = paasTemplateMapper.insert(passTemplate);
							moduleNameMap = new HashMap<String,String>();
							moduleIdMap = new LinkedHashMap<String,Integer>();
							configFilePath = file.getAbsolutePath();   //预存config.yml文件绝对路径，为保存使用
//							//-------------打印语句----------------
//							Object load = Yaml.load(file);
//							System.out.println(load.toString());
							Map father =  Yaml.loadType(file, LinkedHashMap.class);
						    if(null != father) {
						    	String tplId = (String)father.get("name");                            //模板id
					            String tplName = (String)father.get("description");                   //模板名称
					            String tplCategory = (String)father.get("category");                  //模板分类
					            //保存模板信息：模板ID、模板名称、模板分类
					            passTemplate.setTemplateId(tplId);
					            passTemplate.setTemplateName(tplName);
					            passTemplate.setTemplateCategory(tplCategory);
						    }       
						}
						//-------------------------2-----------------docker-compose.yml
						if(fileName.indexOf("docker-compose")!= -1){                                 
//							System.out.println("----------------------------------------");
//							Object load = Yaml.load(file);
//							System.out.println(load.toString());
							Map father = Yaml.loadType(file, LinkedHashMap.class);                       //template
							if(null != father){
								for(Object objkey:father.keySet()){ 
//									System.out.println(objkey+":\t"+father.get(objkey).toString());
						            
									String strkey = (String)objkey;
						            if(null != strkey && strkey.equals("services")){
						            	Map servicesMap = (Map)father.get("services");                       //services
							            for(Object serviceKey : servicesMap.keySet()){
							            	System.out.println("-----------"+serviceKey.toString()+"-----------------------------");
							            	Map serviceMap = (Map)servicesMap.get(serviceKey);               //service
							            	Map labelsMap = null;
							            	if(null != serviceMap){
							            		labelsMap = (Map)serviceMap.get("labels");                   //labels
							            	}
							            	
							            	int createModuleFlag = 0;                                        //避免重复创建subservice 标志
							            	int templateResult_up = 0;                                       //避免重复更新template 标志
							            	if(null != labelsMap){
							            		for(Object labelKey : labelsMap.keySet()){
								            		//解析labels下面的内容保存到模板
								            		System.out.println(labelKey+":\t"+labelsMap.get(labelKey).toString());
								            		
								            		//1.处理全局变量
							            			String tpltype =(String)labelsMap.get("com.dayang.paas.tpltype");             //模板类型
								            		if((templateResult_up == 0) && null != tpltype){
								            			passTemplate.setTemplateType(tpltype);
								            		}
							            			String sharemode = (String)labelsMap.get("com.dayang.paas.sharemode");        //模板使用模式
								            		if((templateResult_up == 0) && null != sharemode){
								            			passTemplate.setUserMode(sharemode);
								            		}
							            			String version = (String)labelsMap.get("com.dayang.paas.version");            //模板版本号
								            		if((templateResult_up == 0) && null != version){
									            		passTemplate.setVersion(version);
									            		passTemplate.setPrice(0.0);                                               //价格
									            		passTemplate.setIsPub(0);                                                 //是否发布
								            		}
									            		
	//---1----------------------------------------------------PAAS_Template对象-------------------------------------------------------------------------							            		
								            		if((templateResult_up == 0) && (null != tpltype || null != sharemode || null != version)){
									            		 templateResult_up = paasTemplateMapper.updateByPrimaryKeySelective(passTemplate);
									            		
//									            		//---------------打印语句
//									            		if(templateResult_up > 0){
//									            			System.out.println("=========插入template成功"+passTemplate.getId());
//									            		}else{
//									            			System.out.println("===========插入template失败=============");
//									            		}
								            		}
								            		
	//---2----------------------------------------------------PAAS_Sbuservice对象------------------------------------------------------------------							            		
								            		if(createModuleFlag == 0){//避免重复创建
								            			//2.解析每个服务对应的模块
									            		String modules = (String)labelsMap.get("com.dayang.paas.subservices");   //获得模块的名称、url、是否展示
									            		
									            		if(null != modules){
									            			if(modules.indexOf(";")!= -1){//对应多个子模块
										            			String[] module_array = modules.split(";");
										            			for(String module : module_array){
										            				String[] moduleContent = module.split(",");
										            				String moduleName = moduleContent[0];                       //模块名称
										            				String moduleUrl = moduleContent[1];                        //模块Url
										            				String moduleIsShow = moduleContent[2];                     //模块是否展示
										            				String moduleDesKey = "com.dayang.paas.subservice."+moduleName;
										            				String moduleDes = (String)labelsMap.get(moduleDesKey);   //模块描述
										            				
										            				PaasSubservice paasSubservice = new PaasSubservice();
										            				paasSubservice.setModuleName(moduleName);
										            				paasSubservice.setModuleUrl("/"+moduleUrl);
										            				paasSubservice.setModuleIsDis(moduleIsShow);
										            				paasSubservice.setModuleDes(moduleDes);
										            				paasSubservice.setTemplateId(passTemplate.getId());
										            				if(null != passTemplate.getId() && null == paasSubservice.getId()){
										            					int subServiceResult = paasSubserviceMapper.insert(paasSubservice);
										            					if(subServiceResult >0 && null != paasSubservice.getId()){
										            						moduleNameMap.put(moduleName, moduleName);
										            						moduleIdMap.put(moduleName, paasSubservice.getId());
										            						createModuleFlag = 1;
										            					}
										            					
//										            					//---------------------打印语句
//										            					if(subServiceResult >0){
//										            						System.out.println("=========模块"+moduleName+"===保存成功========id"+paasSubservice.getId());
//										            					}else{
//										            						System.out.println("!!!!!==模块"+moduleName+"===保存失败！！！========id"+paasSubservice.getId());
//			
//										            					}
										            				}
										            				
										            				
										            			}
										            		}else{//只有一个子模块
										            			String[] moduleContent = modules.split(",");
										            			String moduleName = moduleContent[0];                       //模块名称
									            				String moduleUrl = moduleContent[1];                        //模块Url
									            				String moduleIsShow = moduleContent[2];                     //模块是否展示
									            				String moduleDesKey = "com.dayang.paas.subservice."+moduleName;
									            				String moduleDes = (String)labelsMap.get(moduleDesKey);   //模块描述
									            				moduleNameMap.put(moduleName, moduleName);
									            				
									            				PaasSubservice paasSubservice = new PaasSubservice();
									            				paasSubservice.setModuleName(moduleName);
									            				paasSubservice.setModuleUrl("/"+moduleUrl);
									            				paasSubservice.setModuleIsDis(moduleIsShow);
									            				paasSubservice.setModuleDes(moduleDes);
									            				paasSubservice.setTemplateId(passTemplate.getId());
									            				if(null != passTemplate.getId() && null == paasSubservice.getId()){
									            					int subServiceResult = paasSubserviceMapper.insert(paasSubservice);
									            					if(subServiceResult >0 && null != paasSubservice.getId()){
									            						moduleNameMap.put(moduleName, moduleName);
									            						moduleIdMap.put(moduleName, paasSubservice.getId());
									            						createModuleFlag = 1;
									            					}
									            					
//									            					//---------------------打印语句
//									            					if(subServiceResult >0){
//									            						System.out.println("=========模块"+moduleName+"===保存成功========id"+paasSubservice.getId());
//									            					}else{
//									            						System.out.println("!!!!!==模块"+moduleName+"===保存失败！！！========id"+paasSubservice.getId());
//			
//									            					}
									            				}
										            		}
									            		}
								            		}
								            	}
							            	}
							            	
							            }
						            }
						            
						        }
							}
					
								
								
	//---3---------------------------------------------保存docker.yml文件-----------------------------------------------------
							String dockerName = file.getName();
							//文件 file 本身
							//模块id 为null
							//创建docker文件 ，调用dao保存docker.yml文件
							PaasTemplateFile dockerYmlFile = new PaasTemplateFile();
							dockerYmlFile.setFileName(dockerName);  
							dockerYmlFile.setTemplateId(passTemplate.getId());
							byte[] dockerByte = CommonTool.File2byte(file);;
							dockerYmlFile.setFile(dockerByte);
							
							int dockerResult = paasTemplateFileMapper.insert(dockerYmlFile);
//							//-----------------------打印语句
//							if(dockerResult > 0){
//								System.out.println("----------------------保存docker.yml 成功");
//							}else{
//								System.out.println("----------------------保存docker.yml 失败");
//							}
								
								
	//---4----------------------------------------------保存config.yml文件-------------------------------------------------------------------
							File configFile = new File(configFilePath);
							if(configFile.exists()){
								String configName = configFile.getName();   //config.yml文件名称
								//文件 configFile 本身
								//模块id 为null
								//创建config文件，调用dao保存config.yml文件
								PaasTemplateFile configYmlFile = new PaasTemplateFile();
								configYmlFile.setFileName(configName);
								configYmlFile.setTemplateId(passTemplate.getId());
								byte[] configByte = CommonTool.File2byte(configFile);
								configYmlFile.setFile(configByte);
								
								int configResult = paasTemplateFileMapper.insert(configYmlFile);
//								//-----------------------打印语句
//								if(configResult > 0){
//									System.out.println("----------------------保存config.yml 成功");
//								}else{
//									System.out.println("----------------------保存config.yml 失败");
//								}
							}
						}
						
	//---5---------------------------------------处理图片文件--------------------
						if(fileName.indexOf(".png") != -1 || fileName.indexOf(".img") != -1 || fileName.indexOf(".jpeg") != -1){   
							//遍历模块的名
							if(!moduleNameMap.isEmpty()){
								for(String imgNameKey : moduleNameMap.keySet()){
									String imgNamePart = moduleNameMap.get(imgNameKey);
									if(fileName.indexOf(imgNamePart) != -1){
										//找到对应的模块id
										Integer moduleId = moduleIdMap.get(imgNameKey);  //模块id
										//创建图片文件，调用dao 保存图片文件
										PaasTemplateFile pictureFile = new PaasTemplateFile();
										pictureFile.setFileName(fileName);
										pictureFile.setModuleId(moduleId);
										pictureFile.setTemplateId(passTemplate.getId());
										byte[] imgByte = CommonTool.File2byte(file);
										pictureFile.setFile(imgByte);
										int pictureResult = paasTemplateFileMapper.insert(pictureFile);
//										//-----------------------打印语句
//										if(pictureResult > 0){
//											System.out.println("----------------------保存图片"+ fileName+"成功");
//										}else{
//											System.out.println("----------------------保存图片"+ fileName+"失败");
//										}
									}
								}
							}
						}
						
	//---6--------------------------------.svg文件-----------------------------
						if(fileName.indexOf(".svg") != -1){   
							PaasTemplateFile svgFile = new PaasTemplateFile();
							svgFile.setFileName(fileName);
							svgFile.setTemplateId(passTemplate.getId());
							byte [] svgBytes = CommonTool.File2byte(file);
							svgFile.setFile(svgBytes);
							int svgResult = paasTemplateFileMapper.insert(svgFile);
							
//							//-----------------------打印语句
//							if(svgResult > 0){
//								System.out.println("----------------------保存"+ fileName+"成功");
//							}else{
//								System.out.println("----------------------保存"+ fileName+"失败");
//							}
						}
						
	//---7-------------------------------------readme.md文件--------------------
						if(fileName.indexOf(".md") != -1){
							PaasTemplateFile readMeFile = new PaasTemplateFile();
							readMeFile.setFileName(fileName);
							readMeFile.setTemplateId(passTemplate.getId());
							byte[] readMeByte = CommonTool.File2byte(file);
							readMeFile.setFile(readMeByte);
							
							int readMeResult = paasTemplateFileMapper.insert(readMeFile);
							
//							//-----------------------打印语句
//							if(readMeResult > 0){
//								System.out.println("----------------------保存readme.md 成功");
//							}else{
//								System.out.println("----------------------保存readme.md 失败");
//							}
						}
					}
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("解析模板文件失败");
		}
	}
	
	
	/**
	 * TODO 解压指定.zip包 到指定目录
	 * @param zipFile
	 * @param dest
	 * @throws Exception 
	 * @throws ZipException 
	 */
	public void unzip(File zipFile,String dest) throws Exception{
		try{
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
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("压缩文件不合法");
		}
	}
	
	
	
	
	/**
	 * TODO 删除指定目录下的所有子文件
	 * @param dirPath
	 * @throws Exception 
	 */
	public void deleteDir(String dirPath) throws Exception{
		try{
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
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("delfilewrong");
		}
		
    }


}
