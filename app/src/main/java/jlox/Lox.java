package jlox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static final int EX_USAGE = 64;
    static final int EX_DATAERR = 65;

    private boolean hadError;

    public Lox() {
        hadError = false;
    }

    public void run(String [] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox <file>");
            System.exit(EX_USAGE);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }


    private void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String (bytes, Charset.defaultCharset()));
    }

    private void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;) {
            System.out.print("> ");
            String line = reader.readLine();

            // Respond to Ctrl-D as request to quit
            if (line == null) {
                break;
            }

            run(line);

            // Reset error condition to prevent system exit in run()
            hadError = false;
        }
    }

    private void run(String source) throws IOException {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();

        for (Token t : tokens) {
            System.out.println("<\t" + t);
        }

        if (hadError) {
            System.exit(EX_DATAERR);
        }
    }

    public void error(int line, String msg) {
        report(line, "", msg);
    }

    private void report(int line, String where, String msg) {
        System.err.println("[line: " + line + "] Error" + where + ": " + msg);
        hadError = true;
    }
}
