package wanwe17.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

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
}
