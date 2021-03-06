package rest.mybatis.model.passModel;

public class PaasInstance {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_INSTANCE.INSTANCE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private String instanceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_INSTANCE.TEMPLATE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private Integer templateId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_INSTANCE.INSTANCE_NAME
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private String instanceName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_INSTANCE.INSTANCE_STATUS
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private Integer instanceStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_INSTANCE.ACCESS_KEY
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private String accessKey;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_INSTANCE.PUB_DNS
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private String pubDns;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_INSTANCE.URL_PREFIX
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private String urlPrefix;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_INSTANCE.ORDER_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private Integer orderId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_INSTANCE.INSTANCE_ID
     *
     * @return the value of PAAS_INSTANCE.INSTANCE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    
    private String version;
    
 public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

private PaasTemplate paasTemplate;
    
    public PaasTemplate getPaasTemplate() {
		return paasTemplate;
	}

	public void setPaasTemplate(PaasTemplate paasTemplate) {
		this.paasTemplate = paasTemplate;
	}
    public String getInstanceId() {
        return instanceId;
    }
//新增手输Id字段
    private String stackId;
    
    public String getStackId() {
		return stackId;
	}

	public void setStackId(String stackId) {
		this.stackId = stackId;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_INSTANCE.INSTANCE_ID
     *
     * @param instanceId the value for PAAS_INSTANCE.INSTANCE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId == null ? null : instanceId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_INSTANCE.TEMPLATE_ID
     *
     * @return the value of PAAS_INSTANCE.TEMPLATE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public Integer getTemplateId() {
        return templateId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_INSTANCE.TEMPLATE_ID
     *
     * @param templateId the value for PAAS_INSTANCE.TEMPLATE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_INSTANCE.INSTANCE_NAME
     *
     * @return the value of PAAS_INSTANCE.INSTANCE_NAME
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_INSTANCE.INSTANCE_NAME
     *
     * @param instanceName the value for PAAS_INSTANCE.INSTANCE_NAME
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName == null ? null : instanceName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_INSTANCE.INSTANCE_STATUS
     *
     * @return the value of PAAS_INSTANCE.INSTANCE_STATUS
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public Integer getInstanceStatus() {
        return instanceStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_INSTANCE.INSTANCE_STATUS
     *
     * @param instanceStatus the value for PAAS_INSTANCE.INSTANCE_STATUS
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setInstanceStatus(Integer instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_INSTANCE.ACCESS_KEY
     *
     * @return the value of PAAS_INSTANCE.ACCESS_KEY
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_INSTANCE.ACCESS_KEY
     *
     * @param accessKey the value for PAAS_INSTANCE.ACCESS_KEY
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey == null ? null : accessKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_INSTANCE.PUB_DNS
     *
     * @return the value of PAAS_INSTANCE.PUB_DNS
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public String getPubDns() {
        return pubDns;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_INSTANCE.PUB_DNS
     *
     * @param pubDns the value for PAAS_INSTANCE.PUB_DNS
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setPubDns(String pubDns) {
        this.pubDns = pubDns == null ? null : pubDns.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_INSTANCE.URL_PREFIX
     *
     * @return the value of PAAS_INSTANCE.URL_PREFIX
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public String getUrlPrefix() {
        return urlPrefix;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_INSTANCE.URL_PREFIX
     *
     * @param urlPrefix the value for PAAS_INSTANCE.URL_PREFIX
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix == null ? null : urlPrefix.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_INSTANCE.ORDER_ID
     *
     * @return the value of PAAS_INSTANCE.ORDER_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_INSTANCE.ORDER_ID
     *
     * @param orderId the value for PAAS_INSTANCE.ORDER_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

	@Override
	public String toString() {
		return "PaasInstance [instanceId=" + instanceId + ", templateId="
				+ templateId + ", instanceName=" + instanceName
				+ ", instanceStatus=" + instanceStatus + ", accessKey="
				+ accessKey + ", pubDns=" + pubDns + ", urlPrefix=" + urlPrefix
				+ ", orderId=" + orderId + ", version=" + version
				+ ", paasTemplate=" + paasTemplate + "]";
	}
    
}