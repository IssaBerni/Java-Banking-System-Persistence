package bank;

import com.google.gson.*;
import java.lang.reflect.Type;

/**
 * Custom Serializer and Deserializer for {@link Transaction} objects using Gson.
 * <p>
 * This class solves the problem of polymorphic deserialization by adding a custom
 * property "CLASSNAME" to the JSON output. This ensures that when reading the file,
 * we know whether to create a {@link Payment}, {@link Transfer}, etc.
 */
public class TransactionSerializer implements JsonSerializer<Transaction>, JsonDeserializer<Transaction> {

    /**
     * Serializes a Transaction object into a JSON element.
     * Adds the "CLASSNAME" property containing the specific class name (e.g., "Payment").
     *
     * @param src       the source Transaction object
     * @param typeOfSrc the type of the source object
     * @param context   the serialization context
     * @return the resulting JsonElement with the added CLASSNAME property
     */
    @Override
    public JsonElement serialize(Transaction src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src).getAsJsonObject();
        jsonObject.addProperty("CLASSNAME", src.getClass().getSimpleName());
        return jsonObject;
    }

    /**
     * Deserializes a JSON element back into a specific Transaction subclass.
     * Uses the "CLASSNAME" property to determine which class to instantiate.
     *
     * @param json    the JSON element to deserialize
     * @param typeOfT the type of the object to deserialize to
     * @param context the deserialization context
     * @return the deserialized Transaction object
     * @throws JsonParseException if the CLASSNAME property is missing or the class is unknown
     */
    @Override
    public Transaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        if (!jsonObject.has("CLASSNAME")) {
            throw new JsonParseException("Missing 'CLASSNAME' property in JSON: " + jsonObject);
        }

        String className = jsonObject.get("CLASSNAME").getAsString();

        try {
            Class<?> clazz = Class.forName("bank." + className);
            return context.deserialize(json, clazz);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown class: " + className, e);
        }
    }
}