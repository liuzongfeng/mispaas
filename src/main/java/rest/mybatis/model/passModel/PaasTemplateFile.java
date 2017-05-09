package rest.mybatis.model.passModel;

public class PaasTemplateFile {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_TEMPLATE_FILE.ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_TEMPLATE_FILE.TEMPLATE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private Integer templateId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_TEMPLATE_FILE.FILE_NAME
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private String fileName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_TEMPLATE_FILE.MODULE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private Integer moduleId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PAAS_TEMPLATE_FILE.FILE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    private byte[] file;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_TEMPLATE_FILE.ID
     *
     * @return the value of PAAS_TEMPLATE_FILE.ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_TEMPLATE_FILE.ID
     *
     * @param id the value for PAAS_TEMPLATE_FILE.ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_TEMPLATE_FILE.TEMPLATE_ID
     *
     * @return the value of PAAS_TEMPLATE_FILE.TEMPLATE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public Integer getTemplateId() {
        return templateId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_TEMPLATE_FILE.TEMPLATE_ID
     *
     * @param templateId the value for PAAS_TEMPLATE_FILE.TEMPLATE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_TEMPLATE_FILE.FILE_NAME
     *
     * @return the value of PAAS_TEMPLATE_FILE.FILE_NAME
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_TEMPLATE_FILE.FILE_NAME
     *
     * @param fileName the value for PAAS_TEMPLATE_FILE.FILE_NAME
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_TEMPLATE_FILE.MODULE_ID
     *
     * @return the value of PAAS_TEMPLATE_FILE.MODULE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public Integer getModuleId() {
        return moduleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_TEMPLATE_FILE.MODULE_ID
     *
     * @param moduleId the value for PAAS_TEMPLATE_FILE.MODULE_ID
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PAAS_TEMPLATE_FILE.FILE
     *
     * @return the value of PAAS_TEMPLATE_FILE.FILE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public byte[] getFile() {
        return file;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PAAS_TEMPLATE_FILE.FILE
     *
     * @param file the value for PAAS_TEMPLATE_FILE.FILE
     *
     * @mbggenerated Mon May 08 16:50:01 CST 2017
     */
    public void setFile(byte[] file) {
        this.file = file;
    }
}