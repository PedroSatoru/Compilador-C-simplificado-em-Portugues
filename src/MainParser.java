import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MainParser {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Uso: java MainParser <arquivo-fonte>");
            System.exit(1);
        }

        String filePath = args[0];
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            Lexer lexer = new Lexer(content);
            List<Token> tokens = lexer.getTokens();

            Parser parser = new Parser(tokens);
            parser.analisarPrograma();
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.exit(2);
        }
    }
}
