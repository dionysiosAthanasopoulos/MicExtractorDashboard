package MicGenAI.MicExtractor.dashboard;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.fasterxml.jackson.annotation.JsonAutoDetect;


@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UmlRel {

	private String cName;

	private ArrayList<UmlRelationshipTypes> type;

	public UmlRel() {}

	public String getCName() { return cName; }

	public ArrayList<UmlRelationshipTypes> getType() { return type; }

	public int calculateAllUmlRelNum() { return type != null ? type.size() : 0; }

	public void print(String tabs) {

		System.out.println(tabs + "target class name = " + cName);

		System.out.println(tabs + "relationship types = " + type);
	}
}