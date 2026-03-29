package MicGenAI.MicExtractor.dashboard;


public enum UmlRelationshipTypes {

	DEPENDENCY("dependency"),

	AGGREGATION("aggregation"),

	COMPOSITION("composition"),

	INHERITANCE("inheritance"),

	ASSOCIATION("association"),

	REALIZATION("realization");


	public final String label;

	private UmlRelationshipTypes(String label) { this.label = label; }
}