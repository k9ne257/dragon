package org.vanautrui.languages.parsing;

import org.junit.Assert;
import org.junit.Test;
import org.vanautrui.languages.model.DragonAST;
import org.vanautrui.languages.model.tokens.DragonToken;
import org.vanautrui.languages.model.tokens.ClassToken;
import org.vanautrui.languages.model.tokens.IdentifierToken;

import java.util.ArrayList;

public class DragonParserTest {

    DragonParser parser=new DragonParser();

    @Test
    public void test_can_create_correct_classnode() throws Exception{
        ArrayList<DragonToken> tokens=new ArrayList<>();
        tokens.add(new ClassToken("class"));

        String classname = "exampleclass";

        tokens.add(new IdentifierToken(classname));

        DragonAST ast = parser.parse(tokens);

        Assert.assertEquals(classname, ast.classNodeList.get(0).getName());
    }
}
