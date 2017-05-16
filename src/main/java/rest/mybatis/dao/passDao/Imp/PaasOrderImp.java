package rest.mybatis.dao.passDao.Imp;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import rest.mybatis.dao.passDao.PaasOrderMapper;
import rest.mybatis.model.passModel.PaasOrder;
import rest.page.util.Pageinfo;
@Configuration
public class PaasOrderImp implements PaasOrderMapper {
	@Autowired
	private SqlSessionFactory sqlSessionfactory;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(PaasOrder record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(PaasOrder record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PaasOrder selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(PaasOrder record) {
		SqlSession session=sqlSessionfactory.openSession();
		int result=session.update("updateOrderStatue",record);
		session.close();
		return result;
	}

	@Override
	public int updateByPrimaryKey(PaasOrder record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOrderNumbyCondition(PaasOrder record) {
		SqlSession session=sqlSessionfactory.openSession();
		List list=session.selectList("selectPaasOrderByCondit",record);
		session.close();
		return list.size()>0?(Integer)list.get(0):0;
	}
	@Override
	public List<PaasOrder> getOrderListbyCondition(Pageinfo record) {
		SqlSession session=sqlSessionfactory.openSession();
		List<PaasOrder> list=session.selectList("selectPaasOrderObjByCondit",record);
		session.close();
		return list;
	}

	@Override
	public PaasOrder selectByUUID(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PaasOrder> selectByCondition(Integer page, String tenantId, String instanceName,
			String templateCategory, Integer counm) {
		// TODO Auto-generated method stub
		return null;
	}
}
