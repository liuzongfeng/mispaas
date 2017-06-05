package rest.mybatis.dao.passDao.Imp;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import rest.mybatis.dao.passDao.PaasOrderMapper;
import rest.mybatis.model.passModel.PaasOrder;
import rest.page.util.Pageinfo;
import rest.page.util.RequestUtil;
@Configuration
public class PaasOrderImp{
	@Autowired
	private SqlSessionFactory sqlSessionfactory;
	@Autowired
	private RequestUtil requestUtil;
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
	public List<PaasOrder> getOrderListbyCondition(Pageinfo record) throws IOException {
		SqlSession session=sqlSessionfactory.openSession();
		List<PaasOrder> list=session.selectList("selectPaasOrderObjByCondit",record);
		session.close();
		JSONObject jo=requestUtil.getAlladminContent();
		JSONArray jona=jo.getJSONArray("tenantList");
		for (int i = 0; i < list.size(); i++) {
			PaasOrder po=list.get(i);
			for (int j = 0; j < jona.size(); j++) {
				JSONObject joo=jona.getJSONObject(j);
				String tenlantid=joo.getString("id");
				if(po.getTenantId().equals(tenlantid))
				{
					po.setTenantName(joo.getString("name"));
					break;
				}
			}
		}
		return list;
	}
//	/**
//	 * 更新订单实例关联
//	 */
//	public void updateOrderWithInstance(Integer instanceId)
//	{
//		SqlSession session=sqlSessionfactory.openSession();
//	}
}
