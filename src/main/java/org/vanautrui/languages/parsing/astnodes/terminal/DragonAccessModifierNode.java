package org.vanautrui.languages.parsing.astnodes.terminal;

import org.vanautrui.languages.lexing.tokens.AccessModifierToken;
import org.vanautrui.languages.lexing.tokens.DragonToken;
import org.vanautrui.languages.parsing.DragonTokenList;
import org.vanautrui.languages.parsing.IDragonASTNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.DragonAST;
import org.vanautrui.languages.parsing.astnodes.nonterminal.DragonClassNode;
import org.vanautrui.languages.parsing.astnodes.nonterminal.DragonMethodNode;

import java.util.Optional;
import java.util.Set;

public class DragonAccessModifierNode implements IDragonASTNode {

    public boolean is_public;

    public DragonAccessModifierNode(DragonTokenList tokens) throws Exception {

        DragonToken token1 = tokens.get(0);

        if (token1 instanceof AccessModifierToken) {
            this.is_public = ((AccessModifierToken) token1).is_public;
            tokens.consume(1);
        } else {
            //otherwise, it is just public. no access modifier is also an access modifier
            this.is_public = true;
        }
    }

    @Override
    public String toSourceCode() {
        return (is_public) ? "public" : "private";
    }

    @Override
    public void doTypeCheck(Set<DragonAST> asts, Optional<DragonClassNode> currentClass, Optional<DragonMethodNode> currentMethod) throws Exception {
        return;
    }
}
