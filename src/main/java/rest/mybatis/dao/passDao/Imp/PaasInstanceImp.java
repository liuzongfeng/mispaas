package rest.mybatis.dao.passDao.Imp;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import rest.mybatis.dao.passDao.PaasInstanceMapper;
import rest.mybatis.model.passModel.PaasInstance;
import rest.mybatis.model.passModel.PaasOrdTenantOrgR;
import rest.mybatis.model.passModel.PaasOrder;
import rest.mybatis.model.passModel.PaasSubservice;
import rest.mybatis.model.passModel.PaasTemplateFile;
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
}
