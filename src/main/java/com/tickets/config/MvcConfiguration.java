package com.tickets.config;

import com.tickets.dao.*;
import com.tickets.model.Filter;
import com.tickets.model.Ticket;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages="com.tickets")
@Import({ WebSecurityConfig.class })
@EnableTransactionManagement
public class MvcConfiguration extends WebMvcConfigurerAdapter{

	@Autowired
	private Environment environment;

	@Bean
	public ViewResolver getViewResolver(){
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setViewClass(JstlView.class);
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp");
		return resolver;
	}

//	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

    @Bean public ResourceBundleMessageSource messageSource(){
        ResourceBundleMessageSource ResourceBundleMessageSource = new ResourceBundleMessageSource();
        ResourceBundleMessageSource.setBasename("messages");
        return ResourceBundleMessageSource;
    }

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//		dataSource.setUrl("jdbc:mysql://ticketsystem.ciao4vitmcqb.us-west-2.rds.amazonaws.com:3306/ticketsystem");
		dataSource.setUrl("jdbc:mysql://localhost:3306/ticketsystem");
		dataSource.setUsername("root");
		dataSource.setPassword("root123");
		
		return dataSource;
	}
	
	@Bean
	@Scope(value = WebApplicationContext.SCOPE_SESSION,
			proxyMode=ScopedProxyMode.TARGET_CLASS)
	public TicketDAO getTicketDAO() {
		return new TicketDAOImpl(getDataSource());
	}
	@Bean
	public TicketDAOHibernate getTicketDAOHibernate(){

    	return new TicketDAOHibernateImpl(getSessionFactory(getDataSource()));
	}
	@Bean
	public UserDAO getUserDAO() {
		return new UserDAOImpl(getDataSource());
	}

	@Bean
	public ChartsDAO getchartsDAO() {return new ChartsDAOImpl(getDataSource());}

	@Bean
	@Scope(value= WebApplicationContext.SCOPE_SESSION,
			proxyMode=ScopedProxyMode.TARGET_CLASS)
	public Filter getFilterConf(){return new Filter();
	}

	@Bean
	public SessionFactory getSessionFactory(DataSource dataSource) {
		LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(getDataSource());
		sessionBuilder.addAnnotatedClasses(Ticket.class);
		return sessionBuilder.buildSessionFactory();
	}

	@Bean
	public HibernateTransactionManager getTransactionManager(
			SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager(
				getSessionFactory(getDataSource()));

		return transactionManager;
	}
//	@Bean
//    FilterService filterService(){return new FilterServiceImpl();}

}
