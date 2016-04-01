package wanwe17.springboot;

import java.io.File;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * Hello world!
 * 
 */
@SpringBootApplication
public class App {
	public static final String MEDIA_PATH="src/main/resources/public/videos";
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ApplicationContext ctx = SpringApplication.run(App.class, args);
		JdbcTemplate jdbcTemplate=(JdbcTemplate) ctx.getBean("jdbcTemplate");
		File path = new File(MEDIA_PATH);
		for(File v:path.listFiles()){
			
		}
	}

	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class SecurityConfiguration extends
			WebSecurityConfigurerAdapter {
		@Autowired
		DataSource dataSource;

		@Autowired
		public void configAuthentication(AuthenticationManagerBuilder auth)
				throws Exception {

			auth.jdbcAuthentication()
					.dataSource(dataSource)
					.usersByUsernameQuery(
							"select username,password, enabled from users where username=?")
					.authoritiesByUsernameQuery(
							"select username, role from user_roles where username=?");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.httpBasic().and().authorizeRequests()
					.antMatchers("/facebook.html","/app.html", "/home.html", "/login.html","/register.html", "/","/components/**","/images/**")
					.permitAll().anyRequest().authenticated().and()
					.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
					.csrf().csrfTokenRepository(csrfTokenRepository());
		}

		private CsrfTokenRepository csrfTokenRepository() {
			HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
			repository.setHeaderName("X-XSRF-TOKEN");
			return repository;
		}
	}

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		driverManagerDataSource
				.setUrl("jdbc:mysql://localhost:3306/springboot");
		driverManagerDataSource.setUsername("waiwang1113");
		driverManagerDataSource.setPassword("password");
		return driverManagerDataSource;
	
	}
	
	
	
	
	
	@Bean(name = "jdbcTemplate")
	@Autowired
	public JdbcTemplate userJdbcTemplate(DriverManagerDataSource dataSource) {
		JdbcTemplate t= new JdbcTemplate();
		t.setDataSource(dataSource);
		return t;
	
	}
}
