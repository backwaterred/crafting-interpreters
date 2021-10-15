package jlox;

import java.util.ArrayList;
import java.util.Arrays;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.io.IOException;
import java.util.List;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static jlox.TokenType.*;

class ScannerTest {

    static class Fac {

        // Single-character tokens.
        static Token lParen() {
            return new Token(LEFT_PAREN, new String("("), null, 0);
        }
        static Token rParen() {
            return new Token(RIGHT_PAREN, new String(")"), null, 0);
        }
        static Token lBrace() {
            return new Token(LEFT_BRACE, new String("{"), null, 0);
        }
        static Token rBrace() {
            return new Token(RIGHT_BRACE, new String("}"), null, 0);
        }
        static Token comma() {
            return new Token(COMMA, new String(","), null, 0);
        }
        static Token dot() {
            return new Token(DOT, new String("."), null, 0);
        }
        static Token minus() {
            return new Token(MINUS, new String("-"), null, 0);
        }
        static Token plus() {
            return new Token(PLUS, new String("+"), null, 0);
        }
        static Token semi() {
            return new Token(SEMICOLON, new String(";"), null, 0);
        }
        static Token slash() {
            return new Token(SLASH, new String("/"), null, 0);
        }
        static Token star() {
            return new Token(STAR, new String("*"), null, 0);
        }

        // One or two character tokens.
        static Token bang() {
            return new Token(BANG, new String("!"), null, 0);
        }
        static Token bangEq() {
            return new Token(BANG_EQUAL, new String("!="), null, 0);
        }
        static Token eq() {
            return new Token(EQUAL, new String("="), null, 0);
        }
        static Token eqEq() {
            return new Token(EQUAL_EQUAL, new String("=="), null, 0);
        }
        static Token greater() {
            return new Token(GREATER, new String(">"), null, 0);
        }
        static Token greaterEq() {
            return new Token(GREATER_EQUAL, new String(">="), null, 0);
        }
        static Token less() {
            return new Token(LESS, new String("<"), null, 0);
        }
        static Token lessEq() {
            return new Token(LESS_EQUAL, new String("<="), null, 0);
        }

        // Literals.
        static Token id(String cs) {
            return new Token(IDENTIFIER, cs, null, 0);
        }
        static Token string(String val) {
            String surrounded = new String("\"" + val + "\"");
            return new Token(STRING, surrounded, val, 0);
        }
        static Token num(String str) {
            Double val = Double.parseDouble(str);
            return new Token(NUMBER, str, val, 0);
        }

        // Keywords.
        static Token and() {
            return new Token(AND, new String("and"), null, 0);
        }
        static Token keywordClass() {
            return new Token(CLASS, new String("class"), null, 0);
        }
        static Token condElse() {
            return new Token(ELSE, new String("else"), null, 0);
        }
        static Token f() {
            return new Token(FALSE, new String("false"), null, 0);
        }
        static Token fun() {
            return new Token(FUN, new String("fun"), null, 0);
        }
        static Token loopFor() {
            return new Token(FOR, new String("for"), null, 0);
        }
        static Token condIf() {
            return new Token(IF, new String("if"), null, 0);
        }
        static Token nil() {
            return new Token(NIL, new String("nil"), null, 0);
        }
        static Token or() {
            return new Token(OR, new String("or"), null, 0);
        }
        static Token print() {
            return new Token(PRINT, new String("print"), null, 0);
        }
        static Token keywordReturn() {
            return new Token(RETURN, new String("return"), null, 0);
        }
        static Token keywordSuper() {
            return new Token(SUPER, new String("super"), null, 0);
        }
        static Token keywordThis() {
            return new Token(THIS, new String("this"), null, 0);
        }
        static Token t() {
            return new Token(TRUE, new String("true"), null, 0);
        }
        static Token var() {
            return new Token(VAR, new String("var"), null, 0);
        }
        static Token loopWhile() {
            return new Token(WHILE, new String("while"), null, 0);
        }

        static Token eof() {
            return new Token(EOF, new String(""), null, 0);
        }
    }

    Scanner getScanner(String srcPath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(srcPath));
        String src = new String (bytes, Charset.defaultCharset());
        return new Scanner(src);
    }

    List<Token> getExpected(Token[] tokens) {
        return new ArrayList<Token>(Arrays.asList(tokens));
    }

    @Test void scanSingleCharTokens() throws IOException {
        Scanner scanner = new Scanner(new String ("(){},.-+;/*"));
        List<Token> actualTokens = scanner.scanTokens();
        List<Token> expectedTokens = getExpected(new Token[] {
                Fac.lParen(),
                Fac.rParen(),
                Fac.lBrace(),
                Fac.rBrace(),
                Fac.comma(),
                Fac.dot(),
                Fac.minus(),
                Fac.plus(),
                Fac.semi(),
                Fac.slash(),
                Fac.star(),

                Fac.eof(),
            });

        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

    @Test void scanOneOrTwoCharTokens() throws IOException {
        Scanner scanner = new Scanner(new String ("! != = == > >= < <="));
        List<Token> actualTokens = scanner.scanTokens();
        List<Token> expectedTokens = getExpected(new Token[] {
                Fac.bang(),
                Fac.bangEq(),
                Fac.eq(),
                Fac.eqEq(),
                Fac.greater(),
                Fac.greaterEq(),
                Fac.less(),
                Fac.lessEq(),

                Fac.eof(),
            });

        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

    @Test void scanIdTokens() throws IOException {
        Scanner scanner = new Scanner(new String ("happy_sad poTat0 sh0rtbr3ad_"));
        List<Token> actualTokens = scanner.scanTokens();
        List<Token> expectedTokens = getExpected(new Token[] {
                Fac.id("happy_sad"),
                Fac.id("poTat0"),
                Fac.id("sh0rtbr3ad_"),

                Fac.eof(),
            });

        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

    @Test void scanStringTokens() throws IOException {
        Scanner scanner = new Scanner(new String ("\"happy_sad\" \"poTat0\" \"sh0rtbr3ad_\""));
        List<Token> actualTokens = scanner.scanTokens();
        List<Token> expectedTokens = getExpected(new Token[] {
                Fac.string("happy_sad"),
                Fac.string("poTat0"),
                Fac.string("sh0rtbr3ad_"),

                Fac.eof(),
            });

        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

    @Test void scanNumberTokens() throws IOException {
        Scanner scanner = new Scanner(new String ("1 2.0 3.1415926"));
        List<Token> actualTokens = scanner.scanTokens();
        List<Token> expectedTokens = getExpected(new Token[] {
                Fac.num("1"),
                Fac.num("2.0"),
                Fac.num("3.1415926"),

                Fac.eof(),
            });

        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

    @Test void scanKeywardTokens() throws IOException {
        Scanner scanner = new Scanner(new String ("and class else false fun for if nil or print return super this true var while"));
        List<Token> actualTokens = scanner.scanTokens();
        List<Token> expectedTokens = getExpected(new Token[] {
                Fac.and(),
                Fac.keywordClass(),
                Fac.condElse(),
                Fac.f(),
                Fac.fun(),
                Fac.loopFor(),
                Fac.condIf(),
                Fac.nil(),
                Fac.or(),
                Fac.print(),
                Fac.keywordReturn(),
                Fac.keywordSuper(),
                Fac.keywordThis(),
                Fac.t(),
                Fac.var(),
                Fac.loopWhile(),

                Fac.eof(),
            });

        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

    @Test void scan_mt() throws IOException {
        Scanner scanner = getScanner("build/resources/test/mt.lox");
        List<Token> actualTokens = scanner.scanTokens();
        List<Token> expectedTokens = getExpected(new Token[] {
                Fac.eof(),
            });

        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

    @Test void scan_var() throws IOException {
        Scanner scanner  = getScanner("build/resources/test/var.lox");
        List<Token> actualTokens = scanner.scanTokens();
        List<Token> expectedTokens = getExpected(new Token[] {
                Fac.var(),
                Fac.id("x"),
                Fac.eq(),
                Fac.t(),
                Fac.semi(),
                Fac.eof(),
            });

        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

    @Test void scan_cond() throws IOException {
        Scanner scanner  = getScanner("build/resources/test/cond.lox");
        List<Token> actualTokens = scanner.scanTokens();
        List<Token> expectedTokens = getExpected(new Token[] {

                Fac.var(),
                Fac.id("b"),
                Fac.eq(),
                Fac.t(),
                Fac.semi(),

                Fac.condIf(),
                // scrt
                Fac.lParen(),
                Fac.id("b"),
                Fac.rParen(),
                // branch_t
                Fac.lBrace(),
                Fac.id("b"),
                Fac.eq(),
                Fac.id("b"),
                Fac.and(),
                Fac.id("b"),
                Fac.semi(),
                Fac.rBrace(),
                // branch_f
                Fac.condElse(),
                Fac.lBrace(),
                Fac.id("b"),
                Fac.eq(),
                Fac.id("b"),
                Fac.or(),
                Fac.id("b"),
                Fac.semi(),
                Fac.rBrace(),

                Fac.condIf(),
                // scrt
                Fac.lParen(),
                Fac.id("b"),
                Fac.rParen(),
                // branch_t
                Fac.lBrace(),
                Fac.print(),
                Fac.string("true"),
                Fac.semi(),
                Fac.rBrace(),
                // branch_f
                Fac.condElse(),
                Fac.lBrace(),
                Fac.print(),
                Fac.string("false"),
                Fac.semi(),
                Fac.rBrace(),

                Fac.eof(),
            });

        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

    @Test void scan_loop() throws IOException {
        Scanner scanner  = getScanner("build/resources/test/loop.lox");
        List<Token> actualTokens = scanner.scanTokens();
        List<Token> expectedTokens = getExpected(new Token[] {
                Fac.var(),
                Fac.id("b"),
                Fac.eq(),
                Fac.t(),
                Fac.semi(),

                Fac.loopWhile(),
                Fac.lParen(),
                Fac.id("b"),
                Fac.rParen(),

                // while body
                Fac.lBrace(),
                Fac.print(),
                Fac.string("while-loop"),
                Fac.semi(),

                Fac.loopFor(),
                // for init; test; step
                Fac.lParen(),
                Fac.var(),
                Fac.id("n"),
                Fac.eq(),
                Fac.num("0"),
                Fac.semi(),
                Fac.id("n"),
                Fac.bangEq(),
                Fac.num("0"),
                Fac.semi(),
                Fac.rParen(),
                Fac.lBrace(),
                Fac.print(),
                Fac.string("for-loop"),
                Fac.semi(),
                Fac.rBrace(),

                Fac.id("b"),
                Fac.eq(),
                Fac.f(),
                Fac.semi(),

                Fac.rBrace(),

                Fac.print(),
                Fac.string("done!"),
                Fac.semi(),

                Fac.eof(),
            });

        assertArrayEquals(expectedTokens.toArray(), actualTokens.toArray());
    }

}
