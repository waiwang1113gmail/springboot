package wanwe17.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App 
{
	
    public static void main( String[] args )
    {
    	@SuppressWarnings("unused")
		ApplicationContext ctx = SpringApplication.run(App.class, args);
    } 
    @Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class SecurityConfiguration extends
			WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			System.out.println("eeeeeeeeeeeeeeeeeeeeeeee");
			http.httpBasic()
					.and()
					.authorizeRequests()
					.antMatchers("/app.html", "/home.html",
							"/login.html", "/").permitAll().anyRequest()
					.authenticated();
		}
	}
    
}
