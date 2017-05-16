package rest.mybatis.dao.passDao.Imp;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import rest.mybatis.model.passModel.PaasOrder;
import rest.page.util.Pageinfo;

@Configuration
public class PaasOrderImp{
	@Autowired
	private SqlSessionFactory sqlSessionfactory;
	
	public int updateByPrimaryKeySelective(PaasOrder record) {
		SqlSession session=sqlSessionfactory.openSession();
		int result=session.update("updateOrderStatue",record);
		session.close();
		return result;
	}

	public int getOrderNumbyCondition(PaasOrder record) {
		SqlSession session=sqlSessionfactory.openSession();
		List list=session.selectList("selectPaasOrderByCondit",record);
		session.close();
		return list.size()>0?(Integer)list.get(0):0;
	}
	public List<PaasOrder> getOrderListbyCondition(Pageinfo record) {
		SqlSession session=sqlSessionfactory.openSession();
		List<PaasOrder> list=session.selectList("selectPaasOrderObjByCondit",record);
		session.close();
		return list;
	}
}
