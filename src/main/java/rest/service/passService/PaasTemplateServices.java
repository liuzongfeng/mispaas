package rest.service.passService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rest.mybatis.dao.passDao.Imp.PaasTemplateImp;
import rest.mybatis.model.passModel.PaasTemplate;
import rest.page.util.Message;
import rest.page.util.PageUtil;
import rest.page.util.Pageinfo;
@RestController
public class PaasTemplateServices {
	@Autowired
	private PageUtil pageUtil;
	@Autowired
	private PaasTemplateImp paasTemplateImp;
	@RequestMapping("/rest/productService/getAllproduct")
	public Pageinfo getAllproductList(@RequestParam(value="ProductName",defaultValue="",required=false) String ProductName,@RequestParam(value="userModel",defaultValue="",required=false) String userModel,@RequestParam(value="templateCategory",defaultValue="",required=false) String templateCategory,@RequestParam(value="page",defaultValue="1",required=false) String page)
	{
		PaasTemplate paasTemplate=new PaasTemplate();
		paasTemplate.setTemplateName(ProductName);
		paasTemplate.setUserMode(userModel);
		paasTemplate.setTemplateCategory(templateCategory);
		int resultnum=paasTemplateImp.selectByCondition(paasTemplate);
		Pageinfo pi=pageUtil.initpage(resultnum, page);
		pi.setConditionObj(paasTemplate);
		List<PaasTemplate> resultlist=paasTemplateImp.selectObjByCondition(pi);
		pi.setResultObj(resultlist);
		return pi;
	}
	@RequestMapping("/rest/productService/editProduct")
	public Message updateProduct(@RequestParam(value="id",required=true) Integer id,@RequestParam(value="templateId",required=true) String templateId,@RequestParam(value="Version",required=true) String Version,@RequestParam(value="templateType",required=true) String templateType,@RequestParam(value="templateName",required=true) String templateName,@RequestParam(value="templateCategory",required=true) String templateCategory,@RequestParam(value="isPub",required=true) Integer isPub)
	{
		PaasTemplate pt=new PaasTemplate();
		pt.setId(id);
		pt.setTemplateId(templateId);
		pt.setVersion(Version);
		pt.setTemplateType(templateType);
		pt.setTemplateName(templateName);
		pt.setTemplateCategory(templateCategory);
		pt.setIsPub(isPub);
		return paasTemplateImp.updateProduct(pt);
	}
	
	/**
	 * 查询分类和状态信息
	 */
	@RequestMapping("/rest/productService/getTypeAndCategory")
	public Map<String, List> getTypeAndCategory()
	{
		Map<String, List> map=paasTemplateImp.getTypeAndMode();
		return map;
	}
	
	/**
	 * 获取门户信息
	 */
	@RequestMapping("/rest/productService/getIndexPageElement/{userid}")
	public Map<String, List> getIndexPageElement(@PathVariable(value="userid") Integer userid)
	{
		return null;
	}
}
