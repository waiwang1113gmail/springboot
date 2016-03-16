package wanwe17.springboot;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@RequestMapping("/resource")
	public Map<String, Object> home() {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("id", UUID.randomUUID().toString());
		model.put("content", "Hello World");
		return model;
	}

	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
	
	@Autowired
	JdbcTemplate tmpl;
	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public ResponseEntity<String> register(@RequestBody User user) { 
		
		System.out.println(user.getUsername());
		
		String SQL = "SELECT username FROM users WHERE username=?";
		SqlRowSet result=tmpl.queryForRowSet(SQL,user.getUsername());
		if(result.next()){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username already exists");
		}
		String addSQL="INSERT INTO users(username,password,enabled) values (?,?,1)";
		tmpl.update(addSQL, user.getUsername(),user.getPassword());
		String addRole="INSERT INTO user_roles(username,role) values (?,\"ROLE_USER\")";
		tmpl.update(addRole, user.getUsername());
		return ResponseEntity.ok("\"pass\"");
	}
	 
}
