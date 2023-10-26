package modele;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class DeserialisationPersonne implements JsonDeserializer<Personne> {
	@Override
	public Personne deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)	throws JsonParseException {
		if (!(json instanceof JsonObject)) {
			throw new JsonParseException("Personne doit être un JsonObject et non un "+json.getClass().getCanonicalName());
		}
		JsonObject jo = (JsonObject) json;
		Personne rep = Personne.get(jo.get("login").getAsString());
		if (rep == null) {
			if (jo.get("poste") == null) {
				rep = new Personne(jo.get("login").getAsString(), jo.get("nom").getAsString(), jo.get("prenom").getAsString());
			} else {
				rep = new Personne(jo.get("login").getAsString(), jo.get("nom").getAsString(), jo.get("prenom").getAsString(), jo.get("poste").getAsString());
			}
		}
		return rep;
	}
}
