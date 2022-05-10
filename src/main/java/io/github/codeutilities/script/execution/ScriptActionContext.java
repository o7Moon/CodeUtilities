package io.github.codeutilities.script.execution;

import io.github.codeutilities.event.system.Event;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.action.ScriptAction;
import io.github.codeutilities.script.argument.ScriptArgument;
import io.github.codeutilities.script.argument.ScriptVariableArgument;
import io.github.codeutilities.script.values.ScriptValue;
import io.github.codeutilities.util.chat.ChatUtil;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public record ScriptActionContext(ScriptContext context, List<ScriptArgument> arguments, Event event, Consumer<Runnable> inner, ScriptTask task, HashMap<String, List<ScriptArgument>> argMap, Script script, boolean isArg,
                                  ScriptAction action) {

    public ScriptActionContext(ScriptContext context, List<ScriptArgument> arguments, Event event, Consumer<Runnable> inner, ScriptTask task, HashMap<String, List<ScriptArgument>> argMap, Script script){
        this(context, arguments, event, inner, task, argMap, script, false, null);
    }

    public void setArg(String name, List<ScriptArgument> args) {
        argMap.put(name, args);
    }

    public List<ScriptArgument> pluralArg(String messages) {
        return argMap.get(messages);
    }

    public ScriptArgument arg(String name) {
        return argMap.get(name).get(0);
    }

    public ScriptValue value(String name) {
        return arg(name).getValue(event,context);
    }

    public List<ScriptValue> pluralValue(String name) {
        return pluralArg(name).stream().map(arg -> arg.getValue(event,context)).collect(Collectors.toList());
    }

    public ScriptVariableArgument variable(String name) {
        return (ScriptVariableArgument) arg(name);
    }

    public void scheduleInner(Runnable runnable) {
        inner.accept(runnable);
    }

    public void scheduleInner() {
        inner.accept(null);
    }

    public void Return(ScriptValue val){
        if (!isArg()){
            context().setVariable(variable("Result").name(),val);
        } else {
            action().returnValue = val;
        }
    }
}
