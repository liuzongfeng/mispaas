package rest.mybatis.dao.passDao;

import rest.mybatis.model.passModel.PassTemplate;

public interface PassTemplateMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PASS_TEMPLATE
     *
     * @mbggenerated Mon May 08 13:48:44 CST 2017
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PASS_TEMPLATE
     *
     * @mbggenerated Mon May 08 13:48:44 CST 2017
     */
    int insert(PassTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PASS_TEMPLATE
     *
     * @mbggenerated Mon May 08 13:48:44 CST 2017
     */
    int insertSelective(PassTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PASS_TEMPLATE
     *
     * @mbggenerated Mon May 08 13:48:44 CST 2017
     */
    PassTemplate selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PASS_TEMPLATE
     *
     * @mbggenerated Mon May 08 13:48:44 CST 2017
     */
    int updateByPrimaryKeySelective(PassTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PASS_TEMPLATE
     *
     * @mbggenerated Mon May 08 13:48:44 CST 2017
     */
    int updateByPrimaryKeyWithBLOBs(PassTemplate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PASS_TEMPLATE
     *
     * @mbggenerated Mon May 08 13:48:44 CST 2017
     */
    int updateByPrimaryKey(PassTemplate record);
}