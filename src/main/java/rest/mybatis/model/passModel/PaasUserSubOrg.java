package rest.mybatis.model.passModel;

public class PaasUserSubOrg {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_USER_SUB_ORG.ID
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_USER_SUB_ORG.BILL_NO
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    private String billNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_USER_SUB_ORG.TENANT_ID
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    private String tenantId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_USER_SUB_ORG.SUBSERVICE_ID
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    private String subserviceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_USER_SUB_ORG.USER_OR_ORD_ID
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    private String userOrOrdId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_USER_SUB_ORG.GRANULARITY
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    private Integer granularity;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_USER_SUB_ORG.ID
     *
     * @return the value of PAAS_USER_SUB_ORG.ID
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    
    private String orgId;
    

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_USER_SUB_ORG.ID
     *
     * @param id the value for PAAS_USER_SUB_ORG.ID
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_USER_SUB_ORG.BILL_NO
     *
     * @return the value of PAAS_USER_SUB_ORG.BILL_NO
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_USER_SUB_ORG.BILL_NO
     *
     * @param billNo the value for PAAS_USER_SUB_ORG.BILL_NO
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo == null ? null : billNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_USER_SUB_ORG.TENANT_ID
     *
     * @return the value of PAAS_USER_SUB_ORG.TENANT_ID
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_USER_SUB_ORG.TENANT_ID
     *
     * @param tenantId the value for PAAS_USER_SUB_ORG.TENANT_ID
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId == null ? null : tenantId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_USER_SUB_ORG.SUBSERVICE_ID
     *
     * @return the value of PAAS_USER_SUB_ORG.SUBSERVICE_ID
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    public String getSubserviceId() {
        return subserviceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_USER_SUB_ORG.SUBSERVICE_ID
     *
     * @param subserviceId the value for PAAS_USER_SUB_ORG.SUBSERVICE_ID
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    public void setSubserviceId(String subserviceId) {
        this.subserviceId = subserviceId == null ? null : subserviceId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_USER_SUB_ORG.USER_OR_ORD_ID
     *
     * @return the value of PAAS_USER_SUB_ORG.USER_OR_ORD_ID
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    public String getUserOrOrdId() {
        return userOrOrdId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_USER_SUB_ORG.USER_OR_ORD_ID
     *
     * @param userOrOrdId the value for PAAS_USER_SUB_ORG.USER_OR_ORD_ID
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    public void setUserOrOrdId(String userOrOrdId) {
        this.userOrOrdId = userOrOrdId == null ? null : userOrOrdId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_USER_SUB_ORG.GRANULARITY
     *
     * @return the value of PAAS_USER_SUB_ORG.GRANULARITY
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    public Integer getGranularity() {
        return granularity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_USER_SUB_ORG.GRANULARITY
     *
     * @param granularity the value for PAAS_USER_SUB_ORG.GRANULARITY
     *
     * @mbggenerated Fri Jun 16 13:48:44 CST 2017
     */
    public void setGranularity(Integer granularity) {
        this.granularity = granularity;
    }

	@Override
	public String toString() {
		return "PaasUserSubOrg [id=" + id + ", billNo=" + billNo
				+ ", tenantId=" + tenantId + ", subserviceId=" + subserviceId
				+ ", userOrOrdId=" + userOrOrdId + ", granularity="
				+ granularity + "]";
	}
    
}