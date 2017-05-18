package rest.mybatis.dao.passDao.Imp;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import rest.mybatis.dao.passDao.PaasInstanceMapper;
import rest.mybatis.model.passModel.PaasInstance;
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
	//删除实例
	public void deleteAllinstanceByOrderid(Integer orderid)
	{
		SqlSession session=sqlSessionFactory.openSession();
		int result=session.delete("deleteAllinstanceByorderid",orderid);
		session.close();
	}
	
	public PaasInstance getInstanceByTemplateId(Integer templateId)
	{
		SqlSession session=sqlSessionFactory.openSession();
		List<PaasInstance> list=session.selectList("selecttheInstanceByTemplateId",templateId);
		return list.size()>0?list.get(0):null;
	}
}
