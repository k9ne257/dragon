package org.vanautrui.languages.lexing.tokens;

import org.vanautrui.languages.lexing.CharacterList;

public class KeywordToken implements DragonToken {

    public static final String[] keywords = new String[]{
            "class", "method", "function"
    };

    public String keyword;

    public KeywordToken(CharacterList list) throws Exception {

        for (String sym : keywords) {
            if (list.startsWith(sym)) {
                this.keyword = sym;
                list.consumeTokens(sym.length());
                return;
            }
        }

        throw new Exception("could not recognize a keyword");
    }

    @Override
    public String getContents() {
        return this.keyword;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other instanceof KeywordToken) {

            return this.keyword.equals(
                    ((KeywordToken) other).keyword
            );
        }

        return false;
    }
}