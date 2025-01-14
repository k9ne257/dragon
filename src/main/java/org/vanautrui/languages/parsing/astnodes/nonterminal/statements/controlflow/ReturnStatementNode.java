package org.vanautrui.languages.parsing.astnodes.nonterminal.statements.controlflow;

import org.vanautrui.languages.lexing.collections.TokenList;
import org.vanautrui.languages.lexing.tokens.KeywordToken;
import org.vanautrui.languages.lexing.tokens.SymbolToken;
import org.vanautrui.languages.parsing.IASTNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.ExpressionNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.statements.IStatementNode;

public class ReturnStatementNode implements IASTNode, IStatementNode {

    public ExpressionNode returnValue;

    public ReturnStatementNode(TokenList tokens)throws Exception{

        TokenList copy = new TokenList(tokens);

        copy.expectAndConsumeOtherWiseThrowException(new KeywordToken("return"));

        this.returnValue = new ExpressionNode(copy);

        copy.expectAndConsumeOtherWiseThrowException(new SymbolToken(";"));

        tokens.set(copy);
    }

    @Override
    public String toSourceCode() {
        return " return " + this.returnValue.toSourceCode() + " ; ";
    }
}
