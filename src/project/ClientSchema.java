/**
 * 
 */
package project;

import java.util.ArrayList;

/**
 * @author Mohan Palukuri
 *
 */
public class ClientSchema {

	String fieldName;
	Integer startIndex;
	Integer endIndex;

	public ClientSchema(String fieldName, Integer startIndex, Integer endIndex) {
		super();
		this.fieldName = fieldName;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	public ClientSchema() {

	}

	public ArrayList<ClientSchema> getClientSchema() {
		ArrayList<ClientSchema> clientSchema = new ArrayList<ClientSchema>();
		clientSchema.add(new ClientSchema("Client Reference", 601, 610));
		clientSchema.add(new ClientSchema("Start Date", 41, 49));
		clientSchema.add(new ClientSchema("DOB", 49, 57));
		clientSchema.add(new ClientSchema("DOD", 57, 65));
		clientSchema.add(new ClientSchema("Marital Status", 66, 67));
		clientSchema.add(new ClientSchema("Seks", 68, 69));
		clientSchema.add(new ClientSchema("Last Name", 78, 113));
		clientSchema.add(new ClientSchema("Last Name", 113, 183));
		clientSchema.add(new ClientSchema("Seks Short", 183, 186));
		clientSchema.add(new ClientSchema("Seks Full", 186, 189));
		clientSchema.add(new ClientSchema("Title", 198, 200));
		clientSchema.add(new ClientSchema("Short Name", 220, 245));
		clientSchema.add(new ClientSchema("Full Name", 245, 315));
		clientSchema.add(new ClientSchema("Initials", 315, 321));
		clientSchema.add(new ClientSchema("Middle Name", 321, 331));
		clientSchema.add(new ClientSchema("Country Code", 332, 334));

		return clientSchema;

	}

}
