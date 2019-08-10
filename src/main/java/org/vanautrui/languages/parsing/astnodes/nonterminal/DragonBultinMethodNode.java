package org.vanautrui.languages.parsing.astnodes.nonterminal;

import org.vanautrui.languages.lexing.tokens.DragonToken;
import org.vanautrui.languages.lexing.tokens.IdentifierToken;
import org.vanautrui.languages.parsing.DragonTokenList;
import org.vanautrui.languages.parsing.IDragonASTNode;
import org.vanautrui.languages.parsing.astnodes.terminal.DragonIdentifierNode;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DragonBultinMethodNode implements IDragonASTNode {

    public final static List<String> builtin_methods = Arrays.asList(
            "print", "println"
    );

    public DragonIdentifierNode methodname;

    public DragonBultinMethodNode(DragonTokenList tokens) throws Exception {

        DragonToken token = tokens.get(0);

        if (token instanceof IdentifierToken) {
            IdentifierToken identifierToken = (IdentifierToken) token;
            if (builtin_methods.contains(identifierToken.getContents())) {
                this.methodname = new DragonIdentifierNode(tokens);
            }
        }
    }

    @Override
    public String toSourceCode() {
        return this.methodname.toSourceCode();
    }

    @Override
    public void doTypeCheck(Set<DragonAST> asts, Optional<DragonClassNode> currentClass, Optional<DragonMethodNode> currentMethod) throws Exception {
        //TODO: figure out what to do here
    }
}
