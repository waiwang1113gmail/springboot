package wanwe17.springboot;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	
	@Autowired
    protected AuthenticationManager authenticationManager;
	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public ResponseEntity<String> register(@RequestBody User user,HttpServletRequest request) { 
		 
		String SQL = "SELECT username FROM users WHERE username=?";
		SqlRowSet result=tmpl.queryForRowSet(SQL,user.getUsername());
		if(result.next()){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("\"username already exists\"");
		}
		String addSQL="CALL new_user(?,?)";
		tmpl.update(addSQL, user.getUsername(),user.getPassword()); 
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername()	, user.getPassword());
		request.getSession();
		token.setDetails(new WebAuthenticationDetails(request));
		Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
		return ResponseEntity.ok("\"pass\"");
	}
	
	 
}
