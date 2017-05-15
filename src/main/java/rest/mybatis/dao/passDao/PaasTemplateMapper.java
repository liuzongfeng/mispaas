package rest.mybatis.dao.passDao;

import java.util.List;
import java.util.Map;

import rest.mybatis.model.passModel.PaasTemplate;
import rest.page.util.Message;
import rest.page.util.Pageinfo;

public interface PaasTemplateMapper {
	
	/**
	 * TODO 查询模板列表
	 * @return
	 */
	List<PaasTemplate> obtainTemplateList();
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_TEMPLATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_TEMPLATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    int insert(PaasTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_TEMPLATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    int insertSelective(PaasTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_TEMPLATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    PaasTemplate selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_TEMPLATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    int updateByPrimaryKeySelective(PaasTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_TEMPLATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(PaasTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_TEMPLATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    int updateByPrimaryKey(PaasTemplate record);
    
    public int selectByCondition(PaasTemplate record);
    public List<PaasTemplate> selectObjByCondition(Pageinfo record);
    public Message updateProduct(PaasTemplate record);
    public Map<String, List> getTypeAndMode();
}