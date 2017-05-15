package rest.mybatis.dao.passDao.Imp;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import rest.mybatis.dao.passDao.PaasInstanceMapper;
import rest.mybatis.model.passModel.PaasInstance;
@Configuration
public class PaasInstanceImp implements PaasInstanceMapper {
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	@Override
	public int deleteByPrimaryKey(String instanceId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(PaasInstance record) {
		SqlSession session=sqlSessionFactory.openSession();
		int result=session.insert("createInstance",record);
		session.close();
		return result;
	}

	@Override
	public int insertSelective(PaasInstance record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PaasInstance selectByPrimaryKey(String instanceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(PaasInstance record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(PaasInstance record) {
		// TODO Auto-generated method stub
		return 0;
	}

}
