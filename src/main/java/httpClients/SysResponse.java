package httpClients;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(value = { "SYS", "FEEDBACK", "ERROR" })
public class SysResponse {

	private HashMap<String, String> body;

	public static final String SYS = "sys";

	public static final String FEEDBACK = "feedback";

	public static final String ERROR = "error";


	public HashMap<String, String> getBody() { return body; }
}