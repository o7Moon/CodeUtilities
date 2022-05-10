package io.github.codeutilities.screen.script;

import io.github.codeutilities.CodeUtilities;
import io.github.codeutilities.screen.CScreen;
import io.github.codeutilities.screen.widget.CItem;
import io.github.codeutilities.screen.widget.CTextField;
import io.github.codeutilities.script.Script;
import io.github.codeutilities.script.action.ScriptAction;
import io.github.codeutilities.script.argument.ScriptNumberArgument;
import io.github.codeutilities.script.argument.ScriptTextArgument;
import io.github.codeutilities.script.argument.ScriptVariableArgument;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;

public class ScriptAddArgumentScreen extends CScreen {

    private final Script script;
    private final ScriptAction action;

    public ScriptAddArgumentScreen(Script script, ScriptAction action, int index) {
        super(100, 50);
        this.script = script;
        this.action = action;

        CTextField input = new CTextField("Input", 2, 2, 96, 35, true);

        ItemStack textIcon = new ItemStack(Items.BOOK);
        textIcon.setCustomName(new LiteralText("Text")
            .fillStyle(Style.EMPTY.withItalic(false)));

        ItemStack numberIcon = new ItemStack(Items.SLIME_BALL);
        numberIcon.setCustomName(new LiteralText("Number")
            .fillStyle(Style.EMPTY.withItalic(false)));

        ItemStack variableIcon = new ItemStack(Items.MAGMA_CREAM);
        variableIcon.setCustomName(new LiteralText("Variable")
            .fillStyle(Style.EMPTY.withItalic(false)));

        ItemStack clientValueIcon = new ItemStack(Items.NAME_TAG);
        clientValueIcon.setCustomName(new LiteralText("Client Value")
            .fillStyle(Style.EMPTY.withItalic(false)));

        ItemStack ActionReturnIcon = new ItemStack(Items.REDSTONE_BLOCK);
        ActionReturnIcon.setCustomName(new LiteralText("Action Return Value")
            .fillStyle(Style.EMPTY.withItalic(false)));

        CItem addNumber = new CItem(2, 40, numberIcon);
        CItem addText = new CItem(12, 40, textIcon);
        CItem addVariable = new CItem(22, 40, variableIcon);
        CItem addClientValue = new CItem(32, 40, clientValueIcon);
        CItem addReturnValue = new CItem(42,40,ActionReturnIcon);

        input.setChangedListener(() -> input.textColor = 0xFFFFFF);

        addText.setClickListener((btn) -> {
            action.getArguments().add(index, new ScriptTextArgument(input.getText()));
            CodeUtilities.MC.setScreen(new ScriptEditActionScreen(action, script));
        });

        addNumber.setClickListener((btn) -> {
            try {
                double number = Double.parseDouble(input.getText());
                action.getArguments().add(index, new ScriptNumberArgument(number));
                CodeUtilities.MC.setScreen(new ScriptEditActionScreen(action, script));
            } catch (Exception err) {
                input.textColor = 0xFF3333;
            }
        });

        addVariable.setClickListener((btn) -> {
            action.getArguments().add(index, new ScriptVariableArgument(input.getText()));
            CodeUtilities.MC.setScreen(new ScriptEditActionScreen(action, script));
        });

        addClientValue.setClickListener((btn) -> {
            CodeUtilities.MC.setScreen(new ScriptAddClientValueScreen(action, script, index));
        });

        addReturnValue.setClickListener((btn) -> {
            CodeUtilities.MC.setScreen(new ScriptAddActionReturnArgumentScreen(action, script, index));
        });

        widgets.add(input);
        widgets.add(addNumber);
        widgets.add(addText);
        widgets.add(addVariable);
        widgets.add(addClientValue);
        widgets.add(addReturnValue);
    }

    @Override
    public void close() {
        CodeUtilities.MC.setScreen(new ScriptEditActionScreen(action, script));
    }
}
