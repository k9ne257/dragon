package org.vanautrui.languages.parsing.astnodes.nonterminal.statements;

import org.vanautrui.languages.lexing.collections.TokenList;
import org.vanautrui.languages.lexing.tokens.SymbolToken;
import org.vanautrui.languages.parsing.IASTNode;
import org.vanautrui.languages.parsing.astnodes.IExpressionComputable;
import org.vanautrui.languages.parsing.astnodes.ITermNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.ExpressionNode;
import org.vanautrui.languages.parsing.astnodes.terminal.IdentifierNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MethodCallNode implements IASTNode, IStatementNode, IExpressionComputable , ITermNode {

    public String getMethodName(){
        return this.identifierMethodName.name;
    }

    public IdentifierNode identifierMethodName;

    public List<ExpressionNode> argumentList = new ArrayList<>();

    public MethodCallNode(TokenList tokens) throws Exception {

        //System.out.println("try parse DragonMethodCallNode");

        TokenList copy = tokens.copy();

        this.identifierMethodName = new IdentifierNode(copy);

        copy.expectAndConsumeOtherWiseThrowException(new SymbolToken("("));

        boolean success_argument = true;
        try {
            this.argumentList.add(new ExpressionNode(copy));
        } catch (Exception e) {
            success_argument=false;
        }
        while (success_argument) {
            try {
                TokenList copy2=new TokenList(copy);

                copy2.expectAndConsumeOtherWiseThrowException(new SymbolToken(","));
                this.argumentList.add(new ExpressionNode(copy2));

                copy.set(copy2);
            } catch (Exception e) {
                success_argument = false;
            }
        }

        copy.expectAndConsumeOtherWiseThrowException(new SymbolToken(")"));

        //System.out.println("success");
        tokens.set(copy);
    }

    @Override
    public String toSourceCode() {
        return this.identifierMethodName.toSourceCode()
                +"("+this.argumentList.stream().map(
                ExpressionNode::toSourceCode
                ).collect(Collectors.joining(","))
                +")"
                +";"
        ;
    }

}
