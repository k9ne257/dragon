package org.vanautrui.languages.parsing.astnodes.nonterminal.statements.controlflow;

import org.vanautrui.languages.lexing.collections.DragonTokenList;
import org.vanautrui.languages.lexing.tokens.KeywordToken;
import org.vanautrui.languages.lexing.tokens.SymbolToken;
import org.vanautrui.languages.parsing.IDragonASTNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.DragonExpressionNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.statements.DragonStatementNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.statements.IDragonStatementNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.upperscopes.DragonAST;
import org.vanautrui.languages.parsing.astnodes.nonterminal.upperscopes.DragonClassNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.upperscopes.DragonMethodNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DragonWhileStatementNode implements IDragonASTNode, IDragonStatementNode {

    public DragonExpressionNode condition;

    public List<DragonStatementNode> statements=new ArrayList<>();

    public DragonWhileStatementNode(DragonTokenList tokens)throws Exception{

        DragonTokenList copy = new DragonTokenList(tokens);

        copy.expectAndConsumeOtherWiseThrowException(new KeywordToken("while"));

        copy.expectAndConsumeOtherWiseThrowException(new SymbolToken("("));

        this.condition =new DragonExpressionNode(copy);

        copy.expectAndConsumeOtherWiseThrowException(new SymbolToken(")"));

        copy.expectAndConsumeOtherWiseThrowException(new SymbolToken("{"));

        //maybe there be some statements
        boolean success_statements = true;
        while (success_statements) {
            try {
                this.statements.add(new DragonStatementNode(copy));
            } catch (Exception e) {
                success_statements = false;
            }
        }

        copy.expectAndConsumeOtherWiseThrowException(new SymbolToken("}"));

        tokens.set(copy);
    }

    @Override
    public String toSourceCode() {
        return null;
    }
}