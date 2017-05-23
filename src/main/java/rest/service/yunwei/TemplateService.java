package rest.service.yunwei;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.ho.yaml.Yaml;
import org.ho.yaml.exception.YamlException;
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
	 * 依赖注入DAO接口
	 */
	@Autowired
	private PaasTemplateMapper paasTemplateMapper;
	@Autowired
	private PaasSubserviceMapper paasSubserviceMapper;
	@Autowired
	private PaasTemplateFileMapper paasTemplateFileMapper;
	@Autowired
	private PaasInstanceMapper paasInstanceMapper;
	
	/**
	 * TODO 使用ThreadLocal处理多线程相关
	 * @param filePath
	 */
	private static MyThreadLocalTool<PaasTemplate> passTemplateThread = new MyThreadLocalTool<PaasTemplate>();   //处理多线程:预先保存paasTemplate,为子服务、文件提供模板id
	private static MyThreadLocalTool<PaasTemplate> old_passTemplateThread = new MyThreadLocalTool<PaasTemplate>();   //处理多线程:预先paasTemplate,为出错后进行回滚还原模板
	private static MyThreadLocalTool<String> configFilePathThread = new MyThreadLocalTool<String>();   //处理多线程：预先保留config.yml文件的绝对路径，为存储该文件
	private static MyThreadLocalTool<Map<String,Integer>> moduleIdMapThread = new MyThreadLocalTool<Map<String,Integer>>();   //处理多线程：预先保管子服务的id,为文件图片提供子服务ID
	private static MyThreadLocalTool<Map<String,String>> moduleNameMapThread = new MyThreadLocalTool<Map<String,String>>();   //处理多线程：预先保管子服务名称，为匹配图片和子服务
	private static MyThreadLocalTool<String> overWriteExistThread = new MyThreadLocalTool<String>();   //处理多线程：保管是否要覆盖的决定
	private static MyThreadLocalTool<List<Object>> forRollBackThread = new MyThreadLocalTool<List<Object>>();   //处理多线程：预先保管要删除的子服务和文件，为回滚使用
	
	////////////////////////////////接口区域：start//////////////////////////////////////////////////////////////////////////
	/**
	 * TODO 导入模板 接口
	 * @param req
	 * @param multiReq
	 */
	@RequestMapping(value = "/testUploadFile", method = RequestMethod.POST)
	@ResponseBody
	public String  testUploadFile(HttpServletRequest req,
	    MultipartHttpServletRequest multiReq) {
		
		FileOutputStream fos = null;
	    FileInputStream fis = null;
	    File zFile = null;
	    String uzipPath = null;
	   
		try {
			String serverPath=Thread.currentThread().getContextClassLoader().getResource("").getPath();  //服务所在绝对路径，作为暂存路径使用
			String tempDriName = Thread.currentThread().getName();   //当前线程名称作为临时存放解压的文件
			forRollBackThread.getTl().set(new ArrayList<Object>());
			overWriteExistThread.getTl().set(req.getParameter("overWriteExist"));              //将是否覆盖存到ThreadLocal中
			//-----------------------------------------上传文件到服务器暂存---------
		    // 获取上传文件的路径
			MultipartFile uploadzipfile = multiReq.getFile("file1");
			
			if(null == uploadzipfile){
				throw new Exception("系统异常，获取文件失败");
			}
		    
			String uploadFilePath = uploadzipfile.getOriginalFilename();
		    // 截取上传文件的文件名
		    String uploadFileName = uploadFilePath.substring(
		        uploadFilePath.lastIndexOf('\\') + 1, uploadFilePath.indexOf('.'));
		    // 截取上传文件的后缀
		    String uploadFileSuffix = uploadFilePath.substring(
		        uploadFilePath.indexOf('.') + 1, uploadFilePath.length());
		    
		    if(null != uploadFileSuffix && uploadFileSuffix.indexOf("zip") == -1){
		    	throw new Exception("压缩文件不合法");
		    }
		    //将文件先暂存本地磁盘，之后删除
		    zFile = new File( serverPath+ uploadFileName + "."+ uploadFileSuffix);
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
		    uzipPath = serverPath+tempDriName+"/";
		    unzip(zFile,uzipPath);
		    //---------------------------------读取目录文件，并解析----------------
		    obtailYmlFile(uzipPath+uploadFileName);
		    //没有异常则返回导入文件成功提示信息
		    return "uploadOK";
		  
		} catch (YamlException e) { //解析yml文件出错
			e.printStackTrace();
			String message = e.getMessage().split("\\:")[1]+":"+e.getMessage().split("\\:")[2];
			if(message.indexOf("docker-compose")!= -1){ //解析docker-compose.yml文件出错
				if(null != overWriteExistThread.getTl().get() && "on".equals(overWriteExistThread.getTl().get())){
					//1.先删除所有
					deleteSubserviceFile();
					paasTemplateMapper.deleteByPrimaryKey(passTemplateThread.getTl().get().getId());
					//2.将信息还原
					rollbackObject();
				}else{
					delAfterUploadFail();  //删除已经新建的模板
				}
			}
			return message;
		}catch (Exception e) {  //其他异常
			// TODO Auto-generated catch block
			e.printStackTrace();
			String message = e.getMessage();
			if(null != overWriteExistThread.getTl().get() && "on".equals(overWriteExistThread.getTl().get())){
				//1.先删除所有。不考虑是否实例
				deleteSubserviceFile();
				paasTemplateMapper.deleteByPrimaryKey(passTemplateThread.getTl().get().getId());
				//2.将信息还原
				rollbackObject();
			}else{
				//删除所有
				delAfterUploadFail();  //删除已经新建的模板
			}
			return message;
		}finally{
//--------------关闭输入输出流--------------------------------------------------------------			
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
//--------------关闭线程释放资源--------------------------------------------------
	      try {
			closeAllThread();
	      } catch (Exception e1) {
	    	  e1.printStackTrace();
	      }
//-------------------------------删除暂存的文件-----------------------
	      try {
	    	 //1.删除.zip文件 
	    	 zFile.delete();     
	    	 //2.循环删除文件、文件夹
			 deleteDir(uzipPath);
			 //3.删除最外层文件
			 new File(uzipPath).delete();
		     
	      } catch (Exception e) {
			e.printStackTrace();
	      }
		}
	  }
	
	/**
	 * TODO 获得模板分类
	 * @return
	 */
	@RequestMapping(value = "/obtainTemplateCategory", method = RequestMethod.GET)
	@ResponseBody
	public List<String> obtainTemplateCategory(){
		
		List<String> catetorys = paasTemplateMapper.obtainTemplateCategory();
		return catetorys != null ? catetorys : new ArrayList<String>() ;
	}
	
	/**
	 * TODO 查询模板列表
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/obtainTemplateList", method = RequestMethod.GET)
	@ResponseBody
	public PageInfo obtainTemplateList(HttpServletRequest req){
		
		String pageNo = req.getParameter("pageNo");    //当前页
		String pageSize = req.getParameter("pageSize");//每页展示的条数
		String templateCategory = req.getParameter("templateCategory");
		String templateName = req.getParameter("templateName");
		
		Integer intPageNo = 1;
		Integer intpageSize = 10;
		if(null != pageSize && null != pageNo){
			intPageNo = Integer.parseInt(pageNo);
			intpageSize = Integer.parseInt(pageSize);
		}
		if("".equals(templateName)){
			templateName = null;
		}
		if("".equals(templateCategory)){
			templateCategory = null;
		}
		
		return queryListByPage(templateName,templateCategory,intPageNo,intpageSize);
		
	}
	
	/**
	 * TODO 删除模板接口
	 * @param templateId
	 * @return
	 */
	@RequestMapping(value = "/deleteTemplateByTemplateId", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,String> deleteTemplateById(String templateId){
		String message = null;
		Map<String,String> mm = new HashMap<String,String>();
		if(null == templateId){
			 message = "未获取对应模板";
			 mm.put("error", message);
			 return mm;
		}
		
		//1.根据模板id,查询是否存在实例：有-->提示有实例，无-->直接删除：模板、子服务、对应文件
		List<PaasInstance> instances = paasInstanceMapper.selectInstanceByTemplateId(templateId);
		if(null != instances && instances.size() > 0){
			message = "该模板有实例，不能够删除";
			mm.put("error", message);
			return mm;
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
				PaasTemplate template = paasTemplateMapper.selectByTemplateId(templateId);
				if(null != template){
					paasTemplateMapper.deleteByPrimaryKey(template.getId());
				}
				message = "deleteok";
				mm.put("ok", message);
				return mm;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message = "系统异常，删除失败";
				mm.put("ok", message);
				return mm;
			}
			
		}
		
	}
	
	/**
	 * TODO 根据模板id 查询模块列表
	 * @param templateId
	 * @return
	 */
	/*@RequestMapping(value = "/obtainSubServiceByTemplateId", method = RequestMethod.GET)
	@ResponseBody
	public List<PaasSubservice> obtainSubServiceByTemplateId(String templateId){
		
		if(null == templateId){
			return new ArrayList<PaasSubservice>();
		}
		
		//调用dao查询PaasSubservice 列表
		List<PaasSubservice> subServices = paasSubserviceMapper.selectSubServiceByTPlId(templateId);
		
		return subServices != null ? subServices : new ArrayList<PaasSubservice>(); 
	}*/
	
	////////////////////////////////内部方法：start//////////////////////////////////////////////////////////////////////////
	/**
	 * TODO 关闭线程，释放资源
	 */
	public void closeAllThread(){
		
		passTemplateThread.getTl().remove();
		old_passTemplateThread.getTl().remove();
		configFilePathThread.getTl().remove();
		moduleIdMapThread.getTl().remove();
		moduleNameMapThread.getTl().remove();
		overWriteExistThread.getTl().remove();
		forRollBackThread.getTl().remove();
	}
	
	/**
	 * TODO 删除避免模板存在实例
	 * @param templateId
	 */
	public void deleteSubserviceFile(){
		Integer templateId_up = old_passTemplateThread.getTl().get().getId();
		String templateId_up_s = null;
		if(null != templateId_up){
			templateId_up_s = String.valueOf(templateId_up);
		}
		//2.删掉之前模板所关联的子服务、和文件
		//1.根据模板id查询子服务,并删除
		List<PaasSubservice> subServices = paasSubserviceMapper.selectSubServiceByTPlId(templateId_up_s);
		if(null != subServices && subServices.size() >0){
			for(PaasSubservice subService : subServices){
				forRollBackThread.getTl().get().add(subService);
				paasSubserviceMapper.deleteByPrimaryKey(subService.getId());
			}
		}
		//2.根据模板id查询文件,并删除
		List<PaasTemplateFile> templateFiles = paasTemplateFileMapper.selectByTemplateId(templateId_up_s);
		if(null != templateFiles && templateFiles.size() >0){
			for(PaasTemplateFile paasTemplateFile : templateFiles){
				forRollBackThread.getTl().get().add(paasTemplateFile);
				paasTemplateFileMapper.deleteByPrimaryKey(paasTemplateFile.getId());
			}
		}
		
	}
	
	/**
	 * TODO 还原数据
	 */
	public void rollbackObject(){
		//1.将模板还原
		paasTemplateMapper.insert(old_passTemplateThread.getTl().get());
		//2.保存子服务、文件
		for(Object o :forRollBackThread.getTl().get()){
			if(o instanceof PaasSubservice){
				PaasSubservice oSubservice = (PaasSubservice)o;
				paasSubserviceMapper.insert(oSubservice);
			}else if(o instanceof PaasTemplateFile){
				PaasTemplateFile oTemplateFile = (PaasTemplateFile)o;
				paasTemplateFileMapper.insert(oTemplateFile);
			}
		}
	}
	
	/**
	 * TODO 上传失败后删除创建的表数据
	 */
	public void delAfterUploadFail(){
		
		PaasTemplate paasTemplate_del = passTemplateThread.getTl().get();
		deleteTemplateById(String.valueOf(paasTemplate_del.getId()));
	}
	
	/**
	 * TODO 供分页查询使用
	 * @param templateName
	 * @param templateCategory
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public PageInfo queryListByPage(String templateName,String templateCategory, Integer pageNo,Integer pageSize) {
		
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo, pageSize);
		
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("templateName", templateName);
		paramMap.put("templateCategory", templateCategory);
		List<PaasTemplate> templateList = paasTemplateMapper.obtainTemplateList(paramMap);
		PageInfo pageInfo = new PageInfo(templateList);
		
		return pageInfo;
		
	}
	
	
	
	/**
	 * TODO 循环遍历文件夹，解析模板文件
	 * @param filePath
	 * @throws Exception
	 */
	@Transactional
	public void obtailYmlFile (String filePath) throws Exception{
		try{
			File dir = new File(filePath);
			File [] fm = dir.listFiles();
			//先对要解析的文件进行排序
			
			if(null != fm && fm.length > 0){
				//1.先将解析文件的顺序调整1config.yml 2docker-compose.yml
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
				//2.开始解析文件
				for(File file : fm){
					if(file.isFile()){
						String fileName = file.getName();
//-----------------1-------------------------//config.yml 文件
						if(fileName.indexOf("config")!= -1){
							moduleNameMapThread.getTl().set(new HashMap<String,String>());
							moduleIdMapThread.getTl().set(new LinkedHashMap<String,Integer>());
							configFilePathThread.getTl().set(file.getAbsolutePath()); //预存config.yml文件绝对路径，为保存使用
							Map father = null;
							try {
								father = Yaml.loadType(file, LinkedHashMap.class);
							} catch (YamlException e) {
								e.printStackTrace();
								throw new YamlException("解析config.yml文件失败。可能原因："+e.getMessage());
							}
						    if(null != father) {
						    	String tplId = (String)father.get("name");                            //模板id
					            if(null != overWriteExistThread.getTl().get() && "on".equals(overWriteExistThread.getTl().get())){//确认是覆盖
					            	//根据templateId 查询template
					            	List<PaasTemplate> paasTemplate_Exi = paasTemplateMapper.selectByTemplateIdConfig(tplId);
					            	if(null != paasTemplate_Exi && paasTemplate_Exi.size() >0){
					            		//1.得到要覆盖的模板
					            		PaasTemplate passTemplate = (PaasTemplate)paasTemplate_Exi.get(0);
					            		old_passTemplateThread.getTl().set(passTemplate);   //保存原有的模板，以便发生异常，再进行还原
					            		passTemplateThread.getTl().set(passTemplate);
					            		deleteSubserviceFile(); //只对应的删除子服务和文件
					            	}
					            }else{//不进行覆盖
					            	PaasTemplate passTemplate = new PaasTemplate();
					            	paasTemplateMapper.insert(passTemplate);
					            	passTemplateThread.getTl().set(passTemplate);
					            }
				            	//添加模板信息
					            passTemplateThread.getTl().get().setTemplateId(tplId);                         				//模板id
					            passTemplateThread.getTl().get().setTemplateName((String)father.get("description"));        //模板名称
					            passTemplateThread.getTl().get().setTemplateCategory((String)father.get("category"));       //模板分类
					            passTemplateThread.getTl().get().setUploadPerson("上传人");									//上传人
					            passTemplateThread.getTl().get().setUploadDate(new Date());									//上传时间
					            passTemplateThread.getTl().get().setProductName((String)father.get("description"));			//产品名称
						    }       
						}else
//-------------------------2-----------------docker-compose.yml
						if(fileName.indexOf("docker-compose")!= -1){                                 
							Map father = null;
							try {
								father = Yaml.loadType(file, LinkedHashMap.class);
							} catch (YamlException e) {
								e.printStackTrace();
								throw new YamlException("解析docker-compose.yml文件失败。可能原因："+e.getMessage());
							}                      
							if(null != father){
								for(Object objkey:father.keySet()){ 
									String strkey = (String)objkey;
						            if(null != strkey && strkey.equals("services")){
						            	Map servicesMap = (Map)father.get("services");                       //services
							            for(Object serviceKey : servicesMap.keySet()){
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
								            		//1.处理全局变量
							            			String tpltype =(String)labelsMap.get("com.dayang.paas.tpltype");             //模板类型
								            		if((templateResult_up == 0) && null != tpltype){
								            			passTemplateThread.getTl().get().setTemplateType(tpltype);
								            		}
							            			String sharemode = (String)labelsMap.get("com.dayang.paas.sharemode");        //模板使用模式
								            		if((templateResult_up == 0) && null != sharemode){
								            			passTemplateThread.getTl().get().setUserMode(sharemode);
								            		}
							            			String version = (String)labelsMap.get("com.dayang.paas.version");            //模板版本号
								            		if((templateResult_up == 0) && null != version){
								            			//添加模板信息
								            			passTemplateThread.getTl().get().setVersion(version);
								            			passTemplateThread.getTl().get().setPrice(0.0);
								            			passTemplateThread.getTl().get().setIsPub(0);
								            		}
//---1----------------------------------------------------PAAS_Template对象-------------------------------------------------------------------------							            		
								            		if((templateResult_up == 0) && (null != tpltype || null != sharemode || null != version)){
									            		 templateResult_up = paasTemplateMapper.updateByPrimaryKeySelective(passTemplateThread.getTl().get());
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
										            				paasSubservice.setTemplateId(passTemplateThread.getTl().get().getId());
										            				if(null != passTemplateThread.getTl().get().getId() && null == paasSubservice.getId()){
										            					int subServiceResult = paasSubserviceMapper.insert(paasSubservice);
										            					if(subServiceResult >0 && null != paasSubservice.getId()){
										            						moduleNameMapThread.getTl().get().put(moduleName, moduleName);
										            						moduleIdMapThread.getTl().get().put(moduleName, paasSubservice.getId());
										            						createModuleFlag = 1;
										            					}
										            				}
										            			}
										            		}else{//只有一个子模块
										            			String[] moduleContent = modules.split(",");
										            			String moduleName = moduleContent[0];                       //模块名称
									            				String moduleUrl = moduleContent[1];                        //模块Url
									            				String moduleIsShow = moduleContent[2];                     //模块是否展示
									            				String moduleDesKey = "com.dayang.paas.subservice."+moduleName;
									            				String moduleDes = (String)labelsMap.get(moduleDesKey);   //模块描述
									            				moduleNameMapThread.getTl().get().put(moduleName, moduleName);
									            				
									            				PaasSubservice paasSubservice = new PaasSubservice();
									            				paasSubservice.setModuleName(moduleName);
									            				paasSubservice.setModuleUrl("/"+moduleUrl);
									            				paasSubservice.setModuleIsDis(moduleIsShow);
									            				paasSubservice.setModuleDes(moduleDes);
									            				paasSubservice.setTemplateId(passTemplateThread.getTl().get().getId());
									            				if(null != passTemplateThread.getTl().get().getId() && null == paasSubservice.getId()){
									            					int subServiceResult = paasSubserviceMapper.insert(paasSubservice);
									            					if(subServiceResult >0 && null != paasSubservice.getId()){
									            						moduleNameMapThread.getTl().get().put(moduleName, moduleName);
									            						moduleIdMapThread.getTl().get().put(moduleName, paasSubservice.getId());
									            						createModuleFlag = 1;
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
							}
//---3---------------------------------------------保存docker.yml文件-----------------------------------------------------
							String dockerName = file.getName();
							//创建docker文件 ，调用dao保存docker.yml文件
							PaasTemplateFile dockerYmlFile = new PaasTemplateFile();
							dockerYmlFile.setFileName(dockerName);  
							dockerYmlFile.setTemplateId(passTemplateThread.getTl().get().getId());
							byte[] dockerByte = CommonTool.File2byte(file);;
							dockerYmlFile.setFile(dockerByte);
							paasTemplateFileMapper.insert(dockerYmlFile);
//---4----------------------------------------------保存config.yml文件-------------------------------------------------------------------
							File configFile = new File(configFilePathThread.getTl().get());
							if(configFile.exists()){
								String configName = configFile.getName();   //config.yml文件名称
								//创建config文件，调用dao保存config.yml文件
								PaasTemplateFile configYmlFile = new PaasTemplateFile();
								configYmlFile.setFileName(configName);
								configYmlFile.setTemplateId(passTemplateThread.getTl().get().getId());
								byte[] configByte = CommonTool.File2byte(configFile);
								configYmlFile.setFile(configByte);
								paasTemplateFileMapper.insert(configYmlFile);
							}
						}else
//---5---------------------------------------处理图片文件--------------------
						if(fileName.indexOf(".png") != -1 || fileName.indexOf(".img") != -1 || fileName.indexOf(".jpeg") != -1){   
							//遍历模块的名
							if(!moduleNameMapThread.getTl().get().isEmpty()){
								for(String imgNameKey : moduleNameMapThread.getTl().get().keySet()){
									String imgNamePart = moduleNameMapThread.getTl().get().get(imgNameKey);
									if(fileName.indexOf(imgNamePart) != -1){
										//找到对应的模块id
										Integer moduleId = moduleIdMapThread.getTl().get().get(imgNameKey);  //模块id
										//创建图片文件，调用dao 保存图片文件
										PaasTemplateFile pictureFile = new PaasTemplateFile();
										pictureFile.setFileName(fileName);
										pictureFile.setModuleId(moduleId);
										pictureFile.setTemplateId(passTemplateThread.getTl().get().getId());
										byte[] imgByte = CommonTool.File2byte(file);
										pictureFile.setFile(imgByte);
										int pictureResult = paasTemplateFileMapper.insert(pictureFile);
									}
								}
							}
						}else
//---6--------------------------------.svg文件-----------------------------
						if(fileName.indexOf(".svg") != -1){   
							PaasTemplateFile svgFile = new PaasTemplateFile();
							svgFile.setFileName(fileName);
							svgFile.setTemplateId(passTemplateThread.getTl().get().getId());
							byte [] svgBytes = CommonTool.File2byte(file);
							svgFile.setFile(svgBytes);
							paasTemplateFileMapper.insert(svgFile);
						}else
//---7-------------------------------------readme.md文件--------------------
						if(fileName.indexOf(".md") != -1){
							PaasTemplateFile readMeFile = new PaasTemplateFile();
							readMeFile.setFileName(fileName);
							readMeFile.setTemplateId(passTemplateThread.getTl().get().getId());
							byte[] readMeByte = CommonTool.File2byte(file);
							readMeFile.setFile(readMeByte);
							paasTemplateFileMapper.insert(readMeFile);
						}
						else{
							//其他文件
						}
					}
				}
			}
			
		}catch(YamlException e){
			e.printStackTrace();
			throw e;
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("系统异常请联系管理员");
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
	            	if(f1.exists()){
	            		System.gc();
	            		forceDelDir(f1);//直至解除占用后删除
	            	}
	            }
	            
	        }
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("delfilewrong");
		}
		
    }
	//直至解除占用后删除
	public void forceDelDir(File f){
		f.delete();
		if(f.exists()){
    		System.gc();
    		forceDelDir(f);
    	}
	}


}
