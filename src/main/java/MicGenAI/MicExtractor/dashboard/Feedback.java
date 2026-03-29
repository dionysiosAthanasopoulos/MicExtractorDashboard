package MicGenAI.MicExtractor.dashboard;

import static java.lang.System.out;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Feedback {

	private int generatedSerNum;

	private double correctTagPercentage;

	private double correctDeclarationPercentage;

	private double correctSerPercentage;

	private double classMinimality;

	private double classCompleteness;

	private ArrayList<String> tagErrorMessages;

	private ArrayList<String> declarationErrorMessages;

	private ArrayList<String> serErrorMessages;

	private ArrayList<String> classMinimalityErrorMessages;

	private ArrayList<String> classCompletenessErrorMessages;

	private boolean serializationError;

	private String feedbackMessage;


	public Feedback() {}

	public double getCorrectTagPercentage() { return correctTagPercentage; }

	public double getCorrectDeclarationPercentage() { return correctDeclarationPercentage; }

	public double getCorrectSerPercentage() { return correctSerPercentage; }

	public ArrayList<String> getClassMinimalityErrorMessages() { return classMinimalityErrorMessages; }

	public ArrayList<String> getClassCompletenessErrorMessages() { return classCompletenessErrorMessages; }

	public boolean getSerializationError() { return serializationError; }


	public void print() { print(""); }

	public void print(String tabs) {

		out.println(tabs + "generatedSerNum = " + generatedSerNum + ", correctTagPercentage = " + correctTagPercentage + ", correctDeclarationPercentage = " + correctDeclarationPercentage + ", correctSerPercentage = " + correctSerPercentage + ", classMinimality = " + classMinimality + ", classCompleteness = " + classCompleteness + ", serializationError = " + serializationError);

		if(tagErrorMessages != null) out.println(tabs + tagErrorMessages);

		if(declarationErrorMessages != null) out.println(tabs + declarationErrorMessages);

		if(serErrorMessages != null) out.println(tabs + serErrorMessages);

		if(classMinimalityErrorMessages != null) out.println(tabs + classMinimalityErrorMessages);

		if(classCompletenessErrorMessages != null) out.println(tabs + classCompletenessErrorMessages);

		if(feedbackMessage != null) out.println(tabs + feedbackMessage);
	}
}