package MicGenAI.MicExtractor.dashboard;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


@XmlAccessorType(XmlAccessType.FIELD)
public class Pack {

	private String name;

	private ArrayList<Pack> pack;

	private ArrayList<C> c;


	public Pack() {}

	public String getName() { return name; }

	public ArrayList<Pack> getPack(){ return pack; }

	public ArrayList<C> getC(){ return c; }

	public int calculateClassNum() {

		int num = 0;

		for(int i = 0; pack != null && i < pack.size(); ++i) num += pack.get(i).calculateClassNum();

		num += c != null ? c.size() : 0;

		return num;
	}

	public int calculateAllUmlRelNum() {

		int num = 0;

		for(int i = 0; pack != null && i < pack.size(); ++i) num += pack.get(i).calculateAllUmlRelNum();

		for(int i = 0; c != null && i < c.size(); ++i) num += c.get(i).calculateAllUmlRelNum();

		return num;
	}

	public void print(String tabs) {

		System.out.println(tabs + "package = " + name);

		for(int i = 0; c != null && i < c.size(); ++i) {

			System.out.println(tabs + "c[" + (i+1) + "/" + c.size() + "]:");

			c.get(i).print(tabs + "\t");
		}

		for(int i = 0; pack != null && i < pack.size(); ++i) {

			System.out.println(tabs + "pack[" + (i+1) + "/" + pack.size() + "]:");

			pack.get(i).print(tabs  + "\t");
		}
	}
}