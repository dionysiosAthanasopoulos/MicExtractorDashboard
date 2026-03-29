package httpClients;

import java.text.DecimalFormat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SourceCodeRequest {

	@JsonInclude(JsonInclude.Include.ALWAYS)
	private String path;

	private String accessToken;

	private Double iscCutoff;

	private Double escCutoff;

	private String userDefinedSys;

	private UmlEditors umlEditor;


	public SourceCodeRequest(String path, UmlEditors umlEditor, double iscCutoff, double escCutoff, String userDefinedSys, String accessToken) {

		this.path = path;

		this.umlEditor = umlEditor;

		this.iscCutoff = setDecimalsPrecision(iscCutoff, 2);

		this.escCutoff = setDecimalsPrecision(escCutoff, 2);

		this.userDefinedSys = userDefinedSys;

		this.accessToken = accessToken;
	}

	private double setDecimalsPrecision(double number, int numberOfDecimals){

		DecimalFormat decimalFormat = new DecimalFormat();

		decimalFormat.setMaximumFractionDigits(numberOfDecimals);

	    return Double.parseDouble(decimalFormat.format(number));
	}

	public String getPath() { return path; }

	public UmlEditors getUmlEditor() { return umlEditor; }

	public Double getIscCutoff() { return iscCutoff; }

	public Double getEscCutoff() { return escCutoff; }

	public String getUserDefinedSys() { return userDefinedSys; }

	public String getAccessToken() { return accessToken; }

	public String generateMessage() {

		return "path = " + path + ", umlEditor = " + umlEditor + ", iscCutoff = " + iscCutoff + ", escCutoff = " + escCutoff + ", userDefinedSys = " + userDefinedSys + ", accessToken = " + accessToken;
	}
}