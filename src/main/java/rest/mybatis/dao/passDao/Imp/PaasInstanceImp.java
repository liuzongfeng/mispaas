package rest.mybatis.dao.passDao.Imp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import rest.mybatis.dao.passDao.PaasInstanceMapper;
import rest.mybatis.dao.passDao.PaasUserSubOrgMapper;
import rest.mybatis.model.passModel.PaasInstance;
import rest.mybatis.model.passModel.PaasOrdTenantOrgR;
import rest.mybatis.model.passModel.PaasOrder;
import rest.mybatis.model.passModel.PaasSubservice;
import rest.mybatis.model.passModel.PaasTemplateFile;
import rest.mybatis.model.passModel.PaasUserSubOrg;
@Configuration
public class PaasInstanceImp{
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	public int insert(PaasInstance record) {
		SqlSession session=sqlSessionFactory.openSession();
		int result=session.insert("createInstance",record);
		session.close();
		return result;
	}

	public boolean isexistInstance(Integer orderid) {
		SqlSession session=sqlSessionFactory.openSession();
		List list=session.selectList("isexistInstance",orderid);
		Integer result=null;
		if(list.size()>0)
		{
			result=(Integer)list.get(0);
		}
		session.close();
		return (result!=null && result>0)?true:false;
	}
	
	public List<PaasInstance> selectInstanceByirderud(String instanceid) {
		SqlSession session=sqlSessionFactory.openSession();
		List<PaasInstance> list=session.selectList("selectInstanceByinstanceid",instanceid);
		session.close();
		return list;
	}
	//将实例置为撤销状态
	public void deleteAllinstanceByOrderid(Integer templateId)
	{
		SqlSession session=sqlSessionFactory.openSession();
		int result=session.delete("deleteAllinstanceBytemplateId",templateId);
		session.close();
	}
	
	public PaasInstance getInstanceByTemplateId(Integer templateId)
	{
		SqlSession session=sqlSessionFactory.openSession();
		List<PaasInstance> list=session.selectList("selecttheInstanceByTemplateId",templateId);
		session.close();
		return list.size()>0?list.get(0):null;
	}
	//更具用户组织机构 查询实例
	public List<PaasOrder> getInstanceBytenantId(String orgid)
	{
		SqlSession session=sqlSessionFactory.openSession();
		List<PaasOrder> list=session.selectList("selectOrderbyorgid",orgid);
		for (int i = 0; i < list.size(); i++) {
			PaasOrder poor=list.get(i);
//			String instanceid=poor.getInstanceId();
//			//实例列表
//			List<PaasInstance> inslist=session.selectList("selectRuningInstancebyid", instanceid);
//			poor.setPaasInstance(inslist.size()>0?inslist.get(0):null);
			//模块文件列表
			List<PaasTemplateFile> fileslist=session.selectList("selectFilesBytemplate", poor.getProId());
			poor.setPaasTemplateFile(fileslist);
			Integer templateid=poor.getProId();
			//模块信息
			List<PaasSubservice> serviceList=session.selectList("selectSubservicebyTemid",templateid);
			poor.setPaasSubservices(serviceList);
		}
		session.close();
		return list;
	}
	public boolean getInstancesByorgid(String orgid,String url)
	{
		Map<String, String> map=new HashMap<String, String>();
		map.put("orgid", orgid);
		map.put("url", url);
		SqlSession session=sqlSessionFactory.openSession();
		List<PaasOrder> list=session.selectList("selectOrderandInsbyorgid",map);
		session.close();
		return list.size()>0?true:false;
	}
	/**
	 * 更具组织机构，用户ID 获取模块信息
	 */
	public List<PaasOrder> getInstancesByOrdIdandUserid(String orgid,String userid)
	{
		PaasOrder paasOrder = null;
		List<PaasOrder> Orderlist=new ArrayList<PaasOrder>();
		Map<String, String> map=new HashMap<String, String>();
		map.put("orgid", orgid);
		map.put("userid", userid);
		System.out.println(map);
		SqlSession session=sqlSessionFactory.openSession();
		List<PaasUserSubOrg> list=session.selectList("getPaasUserSubOrg",map);
//		订单和模块ID的MAP值
		Map<String, List<String>> mapth=new HashMap<String, List<String>>();
		for (int i = 0; i < list.size(); i++) {
			PaasUserSubOrg puso=list.get(i);
			if(mapth.containsKey(puso.getBillNo())){
				List<String> subserviceids=mapth.get(puso.getBillNo());
				subserviceids.add(puso.getSubserviceId());
			}else{
				List<String> serviceidslist=new ArrayList<String>();
				serviceidslist.add(puso.getSubserviceId());
				mapth.put(puso.getBillNo(), serviceidslist);
			}
		}
		if(mapth.size()>0){
			Collection keycol=map.keySet();
			Iterator it=keycol.iterator();
			while(it.hasNext()){
				String orderid=(String)it.next();
				List<PaasOrder> orderlist=session.selectList("selectOrderbyBillNo", orderid);
				if(orderlist.size()>0){
					paasOrder=orderlist.get(0);
					List<PaasTemplateFile> filelist=session.selectList("selectFilesBytemplateIds",mapth.get(orderid));
					paasOrder.setPaasTemplateFile(filelist);
					List<PaasSubservice> paasSubservicelist=session.selectList("selectSubservicebyTemid",paasOrder.getId());
					paasOrder.setPaasSubservices(paasSubservicelist);
				}
				Orderlist.add(paasOrder);
			}
		}
		session.close();
		return Orderlist;
	}
}

