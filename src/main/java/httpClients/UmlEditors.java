package httpClients;

import java.util.ArrayList;


public enum UmlEditors {

	VIOLET_3_0_0(".class.violet.html");


	public final String label;

    private UmlEditors(String label) { this.label = label; }

    public static ArrayList<String> getKeys() {

		ArrayList<String> result = new ArrayList<String>();

		for(int i = 0; i < UmlEditors.class.getEnumConstants().length; ++i) result.add(String.valueOf(UmlEditors.class.getEnumConstants()[i]));

		return result;
    }

    public static UmlEditors getKey(String key) {

    	if(key == null) return null;

		for(int i = 0; i < UmlEditors.class.getEnumConstants().length; ++i) if(key.equals(UmlEditors.class.getEnumConstants()[i].toString())) return UmlEditors.class.getEnumConstants()[i];

		return null;
    }
}