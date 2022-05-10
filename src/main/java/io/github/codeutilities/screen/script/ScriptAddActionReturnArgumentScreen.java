package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CItem;
import io.github.codeutilities.screen.widget.CScrollPanel;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.action.ScriptAction;
import io.github.codeutilities.script.action.ScriptActionType;
import io.github.codeutilities.script.argument.ScriptActionReturnArgument;
import io.github.codeutilities.script.argument.ScriptArgument;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;

public class ScriptAddActionReturnArgumentScreen extends CScreen {

    private final Script script;
    private final ScriptAction action;
    private final int insertIndex;
    private static final int WIDTH = 55;

    public ScriptAddActionReturnArgumentScreen(ScriptAction action, Script script, int insertIndex){
        super(WIDTH, 100);
        this.script = script;
        this.action = action;
        this.insertIndex = insertIndex;

        int x = 5;
        int y = 5;
        CScrollPanel panel = new CScrollPanel(0,0,WIDTH,100);
        for (ScriptActionType t : ScriptActionType.values()){
            if (!t.doesReturn()){
                continue;
            }
            CItem item = new CItem(x,y,t.getIcon());
            item.setClickListener((btn) -> {
                ScriptActionReturnArgument arg = new ScriptActionReturnArgument(new ScriptAction(t,new ArrayList<>()));
                action.getArguments().add(insertIndex,arg);
                CodeUtilities.MC.setScreen(new ScriptEditActionScreen(action, script));
            });
            panel.add(item);
            x += 10;
            if (x >= WIDTH-10){
                x = 5;
                y += 10;
            }
        }
        widgets.add(panel);
    }

    @Override
    public void close(){
        CodeUtilities.MC.setScreen(new ScriptAddArgumentScreen(script, action, insertIndex));
    }
}
