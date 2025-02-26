package org.vanautrui.languages.lexing.tokens;

import org.vanautrui.languages.lexing.collections.CharacterList;
import org.vanautrui.languages.lexing.tokens.utils.IToken;
import com.fasterxml.jackson.annotation.*;
import java.awt.*;
import java.nio.file.Paths;

public class SymbolToken implements IToken {

    //symbol tokens are syntactic

    public static final String[] symbols = new String[]{
            "{", "}",
            "[", "]",
            "(", ")",
            ",",";"
    };

    public String symbol;

    public SymbolToken(CharacterList list) throws Exception {
        super();
        //should be for keywords such as {,},[,],+,-,.,','

        for (String sym : symbols) {
            if (list.startsWith(sym)) {
                this.symbol = sym;
                list.consumeTokens(sym.length());
                return;
            }
        }

        throw new Exception("could not recognize a keyword");
    }

    public SymbolToken(String newcontents) throws Exception {
        this(new CharacterList(newcontents, Paths.get("/dev/null")));
    }

    @Override
    @JsonIgnore
    public String getContents() {
        return this.symbol;
    }

    @Override
	@JsonIgnore
    public Color getDisplayColor() {
        return Color.WHITE;
    }
}
