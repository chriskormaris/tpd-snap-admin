package gr.tpd.oracle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tech.ailef.snapadmin.external.SnapAdminProperties;

import javax.sql.DataSource;
import java.util.Properties;

@ConditionalOnProperty(name = "snapadmin.enabled", matchIfMissing = false)
@ComponentScan
@EnableConfigurationProperties(SnapAdminProperties.class)
@Configuration
@EnableTransactionManagement
public class OracleConfiguration {

    @Autowired
    private SnapAdminProperties snapAdminProperties;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private JpaProperties jpaProperties;

    @Bean
    DataSource oracleDataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(dataSourceProperties.getDriverClassName());
        dataSourceBuilder.url(dataSourceProperties.getUrl());

        dataSourceBuilder.username(dataSourceProperties.getUsername());
        dataSourceBuilder.password(dataSourceProperties.getPassword());
        return dataSourceBuilder.build();
    }

    @Bean
    @Primary
    LocalContainerEntityManagerFactoryBean oracleEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(oracleDataSource());
        factoryBean.setPersistenceUnitName("oracle");
        factoryBean.setPackagesToScan(snapAdminProperties.getModelsPackage());
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", jpaProperties.getDatabasePlatform());
        factoryBean.setJpaProperties(properties);
        factoryBean.afterPropertiesSet();
        return factoryBean;
    }

    @Bean
    TransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(oracleEntityManagerFactory().getObject());
        return transactionManager;
    }

}