package MicGenAI.MicExtractor.dashboard;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


@XmlAccessorType(XmlAccessType.FIELD)
public class Ser {

	private String name;

	private ArrayList<Pack> pack;


	public Ser() {}

	public String getName() { return name; }

	public ArrayList<Pack> getPack(){ return pack; }

	public int calculateClassNum() {

		int num = 0;

		for(int i = 0; pack != null && i < pack.size(); ++i) num += pack.get(i).calculateClassNum();

		return num;
	}

	public int calculateAllUmlRelNum() {

		int num = 0;

		for(int i = 0; pack != null && i < pack.size(); ++i) num += pack.get(i).calculateAllUmlRelNum();

		return num;
	}

	public void print(String tabs) {

		if(name != null) System.out.println(tabs + "Service name = " + name);

		for(int i = 0; pack != null && i < pack.size(); ++i) {

			System.out.println(tabs + "pack[" + (i+1) + "/" + pack.size() + "]:");

			pack.get(i).print(tabs + "\t");
		}
	}
}