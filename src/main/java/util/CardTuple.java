package util;

import data.Token;

public class CardTuple {


    String encryptedText;


    Token.Type type;
    public Token.Type getType() {
        return type;
    }

    public void setType(Token.Type type) {
        this.type = type;
    }

    public String getEncryptedText() {
        return encryptedText;
    }

    public void setEncryptedText(String encryptedText) {
        this.encryptedText = encryptedText;
    }
}
