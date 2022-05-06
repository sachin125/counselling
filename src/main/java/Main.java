import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Main {

	
	public static void main(String args[]) {
		//private PasswordEncoder defaultPasswordEncoderForMatches = new UnmappedIdPasswordEncoder();
		
		org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder bcryptPasswordEncoder = 
				new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
		
		Map<String,PasswordEncoder> map = new HashMap();
		map.put("bcrypt", bcryptPasswordEncoder);
		DelegatingPasswordEncoder passwordEncoder= new DelegatingPasswordEncoder("bcrypt",map);
		
		String password="admin";
		
		System.out.println("passwordEncoder.encode(valueDecoded1): "+passwordEncoder.encode(password));
		
		try {
			if (passwordEncoder.matches(""+password,"{bcrypt}$2a$10$cAf1LIYIpiahoZW8IJch6.bWTl8eRDgOvUWD/D.Xccee8lveCxbri"
					)) {
				System.out.println("true");
			}else {
				System.out.println("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
