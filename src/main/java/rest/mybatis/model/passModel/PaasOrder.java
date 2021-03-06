package rest.mybatis.model.passModel;

import java.util.Date;
import java.util.List;

public class PaasOrder {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_ORDER.ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_ORDER.BILL_NO
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private String billNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_ORDER.PRO_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private Integer proId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_ORDER.TENANT_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private String tenantId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_ORDER.STATUS
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_ORDER.CRATE_DATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private Date crateDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_ORDER.APPROVE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private String approveId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_ORDER.APPROVE_DATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private Date approveDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_ORDER.APPROVE_DESCIBE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private String approveDescibe;
    
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_ORDER.ID
     *
     * @return the value of PAAS_ORDER.ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private PaasInstance paasInstance;
    private PaasTemplate paasTemplate;
    private List<PaasSubservice> paasSubservices;
    private List<PaasTemplateFile> paasTemplateFile;
    
    public List getPaasSubservices() {
		return paasSubservices;
	}

	public void setPaasSubservices(List paasSubservices) {
		this.paasSubservices = paasSubservices;
	}

	public List<PaasTemplateFile> getPaasTemplateFile() {
		return paasTemplateFile;
	}

	public void setPaasTemplateFile(List<PaasTemplateFile> paasTemplateFile) {
		this.paasTemplateFile = paasTemplateFile;
	}

	private Date startDate;
    private Date endDate;
    
    public PaasInstance getPaasInstance() {
		return paasInstance;
	}

	public void setPaasInstance(PaasInstance paasInstance) {
		this.paasInstance = paasInstance;
	}
	

	public PaasTemplate getPaasTemplate() {
		return paasTemplate;
	}

	public void setPaasTemplate(PaasTemplate paasTemplate) {
		this.paasTemplate = paasTemplate;
	}
	
	private String instanceId;
	
	
    public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_ORDER.ID
     *
     * @param id the value for PAAS_ORDER.ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_ORDER.BILL_NO
     *
     * @return the value of PAAS_ORDER.BILL_NO
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_ORDER.BILL_NO
     *
     * @param billNo the value for PAAS_ORDER.BILL_NO
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo == null ? null : billNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_ORDER.PRO_ID
     *
     * @return the value of PAAS_ORDER.PRO_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public Integer getProId() {
        return proId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_ORDER.PRO_ID
     *
     * @param proId the value for PAAS_ORDER.PRO_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setProId(Integer proId) {
        this.proId = proId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_ORDER.TENANT_ID
     *
     * @return the value of PAAS_ORDER.TENANT_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_ORDER.TENANT_ID
     *
     * @param tenantId the value for PAAS_ORDER.TENANT_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_ORDER.STATUS
     *
     * @return the value of PAAS_ORDER.STATUS
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_ORDER.STATUS
     *
     * @param status the value for PAAS_ORDER.STATUS
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_ORDER.CRATE_DATE
     *
     * @return the value of PAAS_ORDER.CRATE_DATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public Date getCrateDate() {
        return crateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_ORDER.CRATE_DATE
     *
     * @param crateDate the value for PAAS_ORDER.CRATE_DATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setCrateDate(Date crateDate) {
        this.crateDate = crateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_ORDER.APPROVE_ID
     *
     * @return the value of PAAS_ORDER.APPROVE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public String getApproveId() {
        return approveId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_ORDER.APPROVE_ID
     *
     * @param approveId the value for PAAS_ORDER.APPROVE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setApproveId(String approveId) {
        this.approveId = approveId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_ORDER.APPROVE_DATE
     *
     * @return the value of PAAS_ORDER.APPROVE_DATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public Date getApproveDate() {
        return approveDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_ORDER.APPROVE_DATE
     *
     * @param approveDate the value for PAAS_ORDER.APPROVE_DATE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_ORDER.APPROVE_DESCIBE
     *
     * @return the value of PAAS_ORDER.APPROVE_DESCIBE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public String getApproveDescibe() {
        return approveDescibe;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_ORDER.APPROVE_DESCIBE
     *
     * @param approveDescibe the value for PAAS_ORDER.APPROVE_DESCIBE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setApproveDescibe(String approveDescibe) {
        this.approveDescibe = approveDescibe == null ? null : approveDescibe.trim();
    }
    
    private String tenantName;

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "PaasOrder [id=" + id + ", billNo=" + billNo + ", proId="
				+ proId + ", tenantId=" + tenantId + ", status=" + status
				+ ", crateDate=" + crateDate + ", approveId=" + approveId
				+ ", approveDate=" + approveDate + ", approveDescibe="
				+ approveDescibe + ", paasInstance=" + paasInstance
				+ ", paasTemplate=" + paasTemplate + ", paasSubservices="
				+ paasSubservices + ", startDate=" + startDate + ", endDate="
				+ endDate + ", instanceId=" + instanceId + ", tenantName="
				+ tenantName + "]";
	}
    
}