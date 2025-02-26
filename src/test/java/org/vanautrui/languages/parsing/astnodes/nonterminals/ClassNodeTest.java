package org.vanautrui.languages.parsing.astnodes.nonterminals;

import org.junit.Test;
import org.vanautrui.languages.lexing.collections.TokenList;
import org.vanautrui.languages.lexing.tokens.*;
import org.vanautrui.languages.parsing.astnodes.nonterminal.upperscopes.ClassNode;

import java.nio.file.Paths;

public class ClassNodeTest {

    @Test
    public void test_can_parse_class_with_1_empty_method() throws Exception {

        TokenList list = new TokenList();
        list.add(new AccessModifierToken("public"));
        list.add(new KeywordToken("class"));
        list.add(new TypeIdentifierToken("Main"));
        list.add(new SymbolToken("{"));

        list.add(new AccessModifierToken("public"));
        list.add(new TypeIdentifierToken("Void"));
        list.add(new IdentifierToken("main"));
        list.add(new SymbolToken("("));
        list.add(new SymbolToken(")"));

        list.add(new SymbolToken("{"));
        //no statements here
        list.add(new SymbolToken("}"));

        list.add(new SymbolToken("}"));

        ClassNode classNode = new ClassNode(list, Paths.get("/dev/null"));
    }

    @Test
    public void test_can_parse_class_with_1_method() throws Exception {

        TokenList list = new TokenList();
        list.add(new AccessModifierToken("public"));
        list.add(new KeywordToken("class"));
        list.add(new TypeIdentifierToken("Main"));
        list.add(new SymbolToken("{"));

        list.add(new AccessModifierToken("public"));
        list.add(new TypeIdentifierToken("Void"));
        list.add(new IdentifierToken("main"));
        list.add(new SymbolToken("("));
        list.add(new SymbolToken(")"));

        list.add(new SymbolToken("{"));

        list.add(new IdentifierToken("main"));
        list.add(new SymbolToken("("));
        list.add(new SymbolToken(")"));
        list.add(new SymbolToken(";"));

        list.add(new SymbolToken("}"));

        list.add(new SymbolToken("}"));

        ClassNode classNode = new ClassNode(list,Paths.get("/dev/null"));
    }
}
