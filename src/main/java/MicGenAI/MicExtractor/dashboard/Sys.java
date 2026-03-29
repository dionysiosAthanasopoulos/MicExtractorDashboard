package MicGenAI.MicExtractor.dashboard;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Sys {

	private String name;

	private ArrayList<Ser> ser;

	private ArrayList<Pack> pack;

	private String outputMessage;


	public Sys() {}


	public String getName() { return name; }

	public ArrayList<Ser> getSer() { return ser; }

	public ArrayList<Pack> getPack() { return pack; }

	public void setOutputMessage(String outputMessage) { this.outputMessage = outputMessage; }

	public String getOutputMessage() { return outputMessage; }

	public int calculateClassNum() {

		int num = 0;

		for(int i = 0; ser != null && i < ser.size(); ++i) num += ser.get(i).calculateClassNum();

		return num;
	}

	public int calculateAllUmlRelNum() {

		int num = 0;

		for(int i = 0; ser != null && i < ser.size(); ++i) num += ser.get(i).calculateAllUmlRelNum();

		return num;
	}

	public void print() {

		if(name != null) System.out.println("System name = " + name);

		for(int i = 0; ser != null && i < ser.size(); ++i) {

			System.out.println("ser[" + (i+1) + "/" + ser.size() + "]:");

			ser.get(i).print("\t");
		}

		if(outputMessage != null) System.out.println("outputMessage = " + outputMessage);
	}
}