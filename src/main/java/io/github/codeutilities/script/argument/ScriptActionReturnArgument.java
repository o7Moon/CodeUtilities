package io.github.codeutilities.script.argument;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.codeutilities.event.system.Event;
import io.github.codeutilities.script.action.ScriptAction;
import io.github.codeutilities.script.action.ScriptActionArgument;
import io.github.codeutilities.script.action.ScriptActionArgument.ScriptActionArgumentType;
import io.github.codeutilities.script.execution.ScriptContext;
import io.github.codeutilities.script.values.ScriptValue;

import java.lang.reflect.Type;

public record ScriptActionReturnArgument(ScriptAction action) implements ScriptArgument {

    @Override
    public ScriptValue getValue(Event event, ScriptContext context) {
        return action.returningInvoke(event,context,null, null, context.script);
    }

    @Override
    public boolean convertableTo(ScriptActionArgumentType type) {
        ScriptActionArgumentType type2 = action().getType().getReturnType();
        return type2.convertableTo(type);
    }

    public static class Serializer implements JsonSerializer<ScriptActionReturnArgument> {

        @Override
        public JsonElement serialize(ScriptActionReturnArgument src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("type", "ACTION");
            object.add("value", context.serialize(src.action(),ScriptAction.class));
            return object;
        }
    }
}
