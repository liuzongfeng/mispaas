package rest.mybatis.dao.passDao.Imp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import rest.mybatis.dao.passDao.PaasTemplateMapper;
import rest.mybatis.model.passModel.PaasTemplate;
import rest.page.util.Message;
import rest.page.util.Pageinfo;
@Configuration
public class PaasTemplateImp implements PaasTemplateMapper {
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(PaasTemplate record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(PaasTemplate record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PaasTemplate selectByPrimaryKey(Integer id) {
		SqlSession session=sqlSessionFactory.openSession();
		List<PaasTemplate> list=session.selectList("selectByid",id);
		return list.size()>0?list.get(0):null;
	}

	@Override
	public int updateByPrimaryKeySelective(PaasTemplate record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(PaasTemplate record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(PaasTemplate record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int selectByCondition(PaasTemplate record) {
		SqlSession session=sqlSessionFactory.openSession();
		List list=session.selectList("selectPaasTemplateByCondit", record);
		session.close();
		return list.size()>0?(Integer)list.get(0):0;
	}
	//安条件查询信息
	@Override
	public List<PaasTemplate> selectObjByCondition(Pageinfo record) {
		SqlSession session=sqlSessionFactory.openSession();
		List<PaasTemplate> list=session.selectList("selectPaasTemplateObjByCondit", record);
		session.close();
		return list;
	}
	//编辑产品信息
	@Override
	public Message updateProduct(PaasTemplate record) {
		SqlSession session=sqlSessionFactory.openSession();
		int result=session.update("UpdateProduct",record);
		session.close();
		if(result>0)
		{
			return new Message("success", "更新成功", new Date());
		}else
		{
			return new Message("fail", "更新失败", new Date());
		}
	}

	@Override
	public Map<String, List> getTypeAndMode() {
		Map<String, List> map=new HashMap<String, List>();
		SqlSession session=sqlSessionFactory.openSession();
		List Categorylist=session.selectList("selectCategory");
		List Usermodelist=session.selectList("selectUserMode");
		map.put("category", Categorylist);
		map.put("usermode", Usermodelist);
		session.close();
		return map;
	}

	@Override
	public List<PaasTemplate> obtainTemplateList(Map paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaasTemplate selectByTemplateId(String id) {
		return null;
	}
	public List<PaasTemplate> selectpaasTemplateList(Integer page, Integer id, String templateId,
			String templateCategory, Integer counm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PaasTemplate> selecttemplateCategory() {
		// TODO Auto-generated method stub
		return null;
	}

}
