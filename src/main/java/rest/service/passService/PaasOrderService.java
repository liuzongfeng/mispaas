package rest.service.passService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import rest.mybatis.dao.passDao.PaasInstanceMapper;
import rest.mybatis.dao.passDao.PaasOrdTenantOrgRMapper;
import rest.mybatis.dao.passDao.PaasOrderMapper;
import rest.mybatis.model.passModel.PaasInstance;
import rest.mybatis.model.passModel.PaasOrdTenantOrgR;
import rest.mybatis.model.passModel.PaasOrder;

@RestController
public class PaasOrderService {
	
	@Autowired
	private  PaasOrderMapper paasOrderMApper;
	@Autowired
	private  PaasOrdTenantOrgRMapper paasOrdTenantOrgMapper;
	@Autowired
	private  PaasInstanceMapper paasInstanceMapper;
	@Autowired
	private PaasOrdTenantOrgRMapper paasOrdTenantOrgRMapper;
	
	//创建订单he维护订单租户和组织机构的关系
	@RequestMapping(value="/passService/createPaasOrder",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	public void createPaasOrder(@RequestBody PaasOrder paasOrder,@RequestParam("ids")ArrayList<Integer> ids,@RequestParam("tenantId") Integer tenantId){
		PaasOrdTenantOrgR orgR = new PaasOrdTenantOrgR();
		String uuid = UUID.randomUUID().toString();
		paasOrder.setBillNo(uuid);
		paasOrderMApper.insertSelective(paasOrder);
		PaasOrder order = paasOrderMApper.selectByUUID(uuid);
		orgR.setOrdId(order.getId());
		orgR.setTenantId(tenantId);
		for (Integer integer : ids) {
			orgR.setOrgId(integer);
			paasOrdTenantOrgMapper.insert(orgR);
		}
	}
	//查询已购买的应用（订单，模板，实例）
	@RequestMapping(value="/passService/showApplicationList",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<PaasOrder> showApplicationList(@RequestParam(value="page") Integer page,
			@RequestParam(value="tenantId") String tenantId,
			@RequestParam(value="instanceName") String instanceName,
			@RequestParam(value="templateCategory") String templateCategory,
			@RequestParam(value="counm",defaultValue="10") Integer counm){
		
			List<PaasOrder> list = paasOrderMApper.selectByCondition(page, tenantId, instanceName, templateCategory, counm);
		return list;
	}
	//查询应用详情(实例详情)
	@RequestMapping(value="/passService/showApplicationDetails",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public PaasInstance showApplicationDetails(@RequestParam("orderId") Integer orderId){
		PaasInstance paasInstance = paasInstanceMapper.selectByorderId(orderId);
		return paasInstance;
	}
	//获取应用实例和组织机构的关系
	@RequestMapping(value="/passService/getInstanceAndOrgShip",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<PaasOrdTenantOrgR> getInstanceAndOrgShip(@RequestParam("orderId") Integer orderId){
		List<PaasOrdTenantOrgR> list = paasOrdTenantOrgRMapper.selectPaasOrdTenantOrgRByOrderID(orderId);
		return list;
	}
	//插入订单组织机构关系
	@RequestMapping(value="/passService/addInstanceAndOrgShip",method=RequestMethod.POST,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void addInstanceAndOrgShip(@RequestParam("ids")ArrayList<Integer> ids,@RequestParam("tenantId") Integer tenantId,@RequestParam("orderId") Integer orderId){
		PaasOrdTenantOrgR orgR = new PaasOrdTenantOrgR();
		orgR.setTenantId(tenantId);
		orgR.setOrdId(orderId);
		for (Integer integer : ids) {
			orgR.setOrgId(integer);
			paasOrdTenantOrgMapper.insert(orgR);
		}
	}
	
	//删除订单组织机构关系
	@RequestMapping(value="/passService/deleteInstanceAndOrgShip",method=RequestMethod.DELETE,produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void deleteInstanceAndOrgShip(@RequestParam("orderId") Integer orderId){
		paasOrdTenantOrgMapper.deleteByOrderID(orderId);
	}
}
