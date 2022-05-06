import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inn.counselling.utils.ConfigUtil;

public class main {

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	public static void main(String[] args) {
		String error = "java.lang.Exception: Your account is disabled, please contact administrator.";
		
		String errormsg = error.replaceAll(":", "-");
		errormsg = errormsg.substring(errormsg.indexOf("Exception")+11, errormsg.length()-1);

		System.out.println(errormsg);
		
		main m = new main();
		String value="darwaja@125";
//		String decodedValue = "d2VkaWJ1ZGR5MTI1";
//		System.out.println(ConfigUtil.decode(decodedValue));
		System.out.println(ConfigUtil.encode(value));
		//System.out.println(m.getEncryptedEncodedPassword(decodedValue));

	}
	
	public String getEncryptedEncodedPassword(String password) {
		byte[] valueDecoded = Base64.decodeBase64("d2VkaWJ1ZGR5MTI1".getBytes());
		String encryptedPassword = passwordEncoder.encode(new String(valueDecoded));
		return encryptedPassword;
	}
}
