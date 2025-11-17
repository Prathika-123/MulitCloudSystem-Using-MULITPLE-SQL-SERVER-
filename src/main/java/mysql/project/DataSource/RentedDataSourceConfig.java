package mysql.project.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "mysql.project.rented.repo",  // Base package for repositories
        entityManagerFactoryRef = "rentedEntityManagerFactory",
        transactionManagerRef = "rentedTransactionManager"
)

public class RentedDataSourceConfig {

    @Bean(name = "rentedDataSource")
    public DataSource rentedDataSource(Environment env) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(env.getProperty("spring.datasource.secondary.rent.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.secondary.rent.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.secondary.rent.password"));
        dataSource.setDriverClassName(env.getProperty("spring.datasource.secondary.rent.driver-class-name"));
        return dataSource;
    }

    @Bean(name = "rentedEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("rentedDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("mysql.project.rentedEntity");  // Scan student entities
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        return factory;
    }

    @Bean(name = "rentedTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("rentedEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
