package rest.mybatis;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import com.github.pagehelper.PageHelper;

@Configuration
@EnableTransactionManagement
public class MyBatisConfig implements TransactionManagementConfigurer {

    @Autowired
    DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("rest.mybatis.model");
        
        
      //分页插件
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        //设置方言
        properties.setProperty("dialect","mysql");                                         
        //该参数默认为false;设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用;和startPage中的pageNum效果一样
        properties.setProperty("offsetAsPageNum", "true"); 
        //该参数默认为false;置为true时，使用RowBounds分页会进行count查询
        properties.setProperty("rowBoundsWithCount", "true");
        //设置为true时，如果pageSize=0或者RowBounds.limit = 0就会查询出全部的结果;相当于没有执行分页查询，但是返回结果仍然是Page类型
        properties.setProperty("pageSizeZero", "true");
   
        //启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页 ;禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据
        properties.setProperty("reasonable", "false");
//        properties.setProperty("supportMethodsArguments", "true");
        properties.setProperty("returnPageInfo", "check");
        //可以配置pageNum,pageSize,count,pageSizeZero,reasonable,不配置映射的用默认值
        properties.setProperty("params", "pageNum=start;pageSize=limit;");
        //always总是返回PageInfo类型,check检查返回类型是否为PageInfo,none返回Page
        pageHelper.setProperties(properties);

        //添加插件
        bean.setPlugins(new Interceptor[]{pageHelper});


      //添加XML目录
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            bean.setMapperLocations(resolver.getResources("classpath:rest/mybatis/mapping/*/*.xml"));
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
    
    
}