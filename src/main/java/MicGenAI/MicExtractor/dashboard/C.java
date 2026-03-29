package MicGenAI.MicExtractor.dashboard;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;


@XmlAccessorType(XmlAccessType.FIELD)
public class C {

	private String name;

	private ArrayList<UmlRel> uRel;

	public C() {}


	public String getName() { return name; }

	public int calculateAllUmlRelNum() {

		int num = 0;

		for(int i = 0; uRel != null && i < uRel.size(); ++i) num += uRel.get(i).calculateAllUmlRelNum();

		return num;
	}

	void print(String tabs) {

		System.out.println(tabs + "class name = " + name);

		for(int i = 0; uRel != null && i < uRel.size(); ++i) {

			System.out.println(tabs + "uRel[" + (i+1) + "/" + uRel.size() + "]:");

			uRel.get(i).print(tabs + "\t");
		}
	}
}