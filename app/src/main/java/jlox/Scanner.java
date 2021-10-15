package jlox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jlox.TokenType.*;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    // Offsets start and current indicate the position
    // of the current token in the source string
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }

    Scanner(String source) {
        this.source =  source;
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
            (c == '_');
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    List<Token> scanTokens() {
        while(!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));

        return tokens;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
        case '(':
            addToken(LEFT_PAREN);
            break;
        case ')':
            addToken(RIGHT_PAREN);
            break;
        case '{':
            addToken(LEFT_BRACE);
            break;
        case '}':
            addToken(RIGHT_BRACE);
            break;
        case ',':
            addToken(COMMA);
            break;
        case '.':
            addToken(DOT);
            break;
        case '-':
            addToken(MINUS);
            break;
        case '+':
            addToken(PLUS);
            break;
        case ';':
            addToken(SEMICOLON);
            break;
        case '*':
            addToken(STAR);
            break;
        case '!':
            addToken(match('=') ? BANG_EQUAL : BANG);
            break;
        case '=':
            addToken(match('=') ? EQUAL_EQUAL : EQUAL);
            break;
        case '<':
            addToken(match('=') ? LESS_EQUAL : LESS);
            break;
        case '>':
            addToken(match('=') ? GREATER_EQUAL : GREATER);
            break;
        case '/':
            if (match('/')) {
                // ignore chars until newline
                while (peek() != '\n' && !isAtEnd()) {
                    advance();
                }
            } else {
                addToken(SLASH);
            }
            break;
        case ' ':
        case '\t':
        case '\r':
            break;
        case '\n':
            line++;
            break;
        case '"':
            string();
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            number();
            break;
        default:
            if (isAlpha(c)) {
                identifier();
            } else {
                App.error(line, "Unexpected character.");
            }
            break;
        }
    }

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        } else if (source.charAt(current) != expected) {
            return false;
        } else {
            current++;
            return true;
        }
    }

    private char advance() {
        return source.charAt(current++);
    }

    private char peek() {
        return lookAhead(0);
    }

    /// Look ahead of current by specified amount.
    private char lookAhead(int offset) {
        if (current + offset >= source.length()) {
            return '\0';
        } else {
            return source.charAt(current + offset);
        }
    }

    private void string() {
        // Advance current token marker until end of string
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') {
                line++;
            }
            advance();
        }

        if (isAtEnd()) {
            App.error(line, "Unterminated string.");
        } else {
            // consume the closing string termination char '"'
            advance();

            String value = source.substring(start + 1, current - 1);
            addToken(STRING, value);
        }
    }

    private void number() {
        // Advance current token marker until end of number or '.'
        while (isDigit(peek()) && !isAtEnd()) {
            advance();
        }
        // Consume '.' if present, and followed by another digit
        if (peek() == '.' && isDigit(lookAhead(1))) {
            advance();
        }
        // Advance current token marker until end of number
        while (isDigit(peek()) && !isAtEnd()) {
            advance();
        }

        Double value = Double.parseDouble(source.substring(start, current));
        addToken(NUMBER, value);
    }

    private void identifier() {
        while (isAlphaNumeric(peek()) && !isAtEnd()) {
            advance();
        }

        String text = source.substring(start, current);
        TokenType type;
        if (keywords.containsKey(text)) {
            type = keywords.get(text);
        } else {
            type = IDENTIFIER;
        }
        addToken(type);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

}
