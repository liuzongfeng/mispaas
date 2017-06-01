package rest.mybatis.dao.passDao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import rest.mybatis.model.passModel.PaasOrder;
import rest.page.util.Pageinfo;

public interface PaasOrderMapper {
	
	List<Integer> selectByInstanceId(String instanceId);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_ORDER
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_ORDER
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    int insert(PaasOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_ORDER
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    int insertSelective(PaasOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_ORDER
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    PaasOrder selectByPrimaryKey(Integer id);
    PaasOrder selectByUUID(String uuid);
    List<PaasOrder> selectByCondition(@Param("page") Integer page,
    		@Param("tenantId") String tenantId,
			@Param("instanceName") String instanceName,
			@Param("templateCategory") String templateCategory,
			@Param("counm") Integer counm
			);
    List<PaasOrder> selectByInstanceName(@Param("page") Integer page,
    		@Param("tenantId") String tenantId,
			@Param("instanceName") String instanceName,
			@Param("templateCategory") String templateCategory,
			@Param("counm") Integer counm
			);
    Integer  selectOrderCount(String tenantId);
    List selectOrderCountBycondition(@Param("tenantId") String tenantId,
			@Param("instanceName") String instanceName,
			@Param("templateCategory") String templateCategory);
    PaasOrder selectByPrimaryId(Integer id);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_ORDER
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    int updateByPrimaryKeySelective(PaasOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PAAS_ORDER
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    int updateByPrimaryKey(PaasOrder record);
    
    public int getOrderNumbyCondition(PaasOrder record);
    public List<PaasOrder> getOrderListbyCondition(Pageinfo record);
}