package jlox;

import java.util.Objects;

class Token {
    final TokenType type;
    // concrete string-value for this token
    final String lexeme;
    // data structure appropriate to the token
    final Object literal;
    final int line;

    // Ex. "This is a string"
    // type: TokenType::STRING
    // lexeme: "This is a string"
    // literal: new String("This is a string")
    // line: <num>
    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Token)) {
            return false;
        } else if (this.literal != null) {
            final Token other = (Token) obj;
            return this.type == other.type &&
                   this.lexeme.equals(other.lexeme) &&
                   this.literal.equals(other.literal);
        } else {
            final Token other = (Token) obj;
            return this.type == other.type &&
                   this.lexeme.equals(other.lexeme) &&
                   this.literal == other.literal;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, lexeme, literal);
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
