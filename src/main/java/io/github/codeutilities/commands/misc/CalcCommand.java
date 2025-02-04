package io.github.codeutilities.commands.misc;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import io.github.codeutilities.commands.Command;
import io.github.codeutilities.util.Regex;
import io.github.codeutilities.util.chat.ChatType;
import io.github.codeutilities.util.chat.ChatUtil;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;

public class CalcCommand implements Command {

    private static int exec(CommandContext<FabricClientCommandSource> ctx) {
        String exp = ctx.getArgument("exp", String.class);

        try {
            ChatUtil.sendMessage(exp + " §e=§f " + BigDecimal.valueOf(calc(exp)).toPlainString(), ChatType.SUCCESS);
        } catch (Exception err) {
            err.printStackTrace();
            ChatUtil.sendMessage("Error while calculating §6" + exp, ChatType.FAIL);
        }

        return 1;
    }

    public static double calc(String exp) throws Exception {
        String result = exp.toLowerCase();

        if (result.contains("(")) {
            int open = -1;
            int close;
            int pos = 0;
            while (pos < result.length()) {
                if (result.charAt(pos) == '(') {
                    open = pos;
                } else if (result.charAt(pos) == ')') {
                    if (open != -1) {
                        close = pos;

                        String pre = result.substring(0, open);
                        String res = String.valueOf(
                            calcPart(result.substring(open + 1, close)));
                        String post = result.substring(close + 1);

                        return calc(pre + res + post);

                    } else {
                        throw new Exception("Invalid Brackets");
                    }
                }

                pos++;
            }
            throw new Exception("Invalid Brackets");
        }

        return calcPart(result);
    }

    public static double calcPart(String exp) {
        String texp = exp.replaceAll(" ", "");
        String old = null;

        while (!Objects.equals(old, texp)) {
            old = texp;

            Matcher powm = Regex.of(
                "(?<n1>-?(\\.\\d+|\\d+\\.\\d+|\\d+))\\^(?<n2>-?(\\.\\d+|\\d+\\.\\d+|\\d+))"
            ).getMatcher(texp);
            Matcher mulm = Regex.of(
                "(?<n1>-?(\\.\\d+|\\d+\\.\\d+|\\d+))(?<e>\\*|\\/|%)(?<n2>-?(\\.\\d+|\\d+\\.\\d+|\\d+))"
            ).getMatcher(texp);
            Matcher addm = Regex.of(
                "(?<n1>-?(\\.\\d+|\\d+\\.\\d+|\\d+))(?<e>\\+|\\-)(?<n2>-?(\\.\\d+|\\d+\\.\\d+|\\d+))"
            ).getMatcher(texp);

            if (powm.find()) {
                double num1 = Double.parseDouble(powm.group("n1"));
                double num2 = Double.parseDouble(powm.group("n2"));
                texp = powm.replaceFirst(BigDecimal.valueOf(Math.pow(num1, num2)).toPlainString());
            } else if (mulm.find()) {
                double num1 = Double.parseDouble(mulm.group("n1"));
                double num2 = Double.parseDouble(mulm.group("n2"));
                if (Objects.equals(mulm.group("e"), "*")) {
                    texp = mulm.replaceFirst(new BigDecimal(num1 * num2).toPlainString());
                } else if (Objects.equals(mulm.group("e"), "%")) {
                    texp = mulm.replaceFirst(new BigDecimal(num1 % num2).toPlainString());
                } else {
                    texp = mulm.replaceFirst(new BigDecimal(num1 / num2).toPlainString());
                }


            } else if (addm.find()) {
                double num1 = Double.parseDouble(addm.group("n1"));
                double num2 = Double.parseDouble(addm.group("n2"));
                if (Objects.equals(addm.group("e"), "+")) {
                    texp = addm.replaceFirst(new BigDecimal(num1 + num2).toPlainString());
                } else {
                    texp = addm.replaceFirst(new BigDecimal(num1 - num2).toPlainString());
                }
            }
        }

        return Double.parseDouble(texp);
    }

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> cd) {
        cd.register(literal("calc")
            .then(argument("exp", StringArgumentType.greedyString())
                .executes(CalcCommand::exec)
            )
        );
    }

    /*
    @Override
    public String getDescription() {
        return "[blue]/calc <expression>[reset]\n\nCalculates simple math expressions.";
    }

    @Override
    public String getName() {
        return "/calc";
    }
    */
}
