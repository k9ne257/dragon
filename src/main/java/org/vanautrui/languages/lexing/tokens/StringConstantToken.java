package org.vanautrui.languages.lexing.tokens;

import org.vanautrui.languages.lexing.collections.CharacterList;
import org.vanautrui.languages.lexing.tokens.utils.IToken;
import com.fasterxml.jackson.annotation.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringConstantToken implements IToken {

    private static final String regex_string_constant = "^\"[^\"]*\"";

    public static final int MAX_STRING_CONSTANT_LENGTH = 1000;

    public String content;

    public StringConstantToken(CharacterList list) throws Exception {
        super();
        Pattern p = Pattern.compile(regex_string_constant);

        Matcher m = p.matcher(list.getLimitedStringMaybeShorter(MAX_STRING_CONSTANT_LENGTH));

        if (m.find()) {
            String finding = m.group(0);
            this.content = finding.substring(1,finding.length()-1);
            list.consumeTokens(finding.length());
        } else {
            throw new Exception("could not recognize identifier");
        }
    }

    public StringConstantToken(String newcontents) throws Exception {
        super();
        this.content = newcontents;
    }

    @Override
    @JsonIgnore
    public String getContents() {
        return this.content;
    }

    @Override
	@JsonIgnore
    public Color getDisplayColor() {
        return Color.WHITE;
    }


}
