import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MainToken{

    public static void main(String[] args){
        if (args.length > 0) {
            String filePath = args[0];
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                Lexer lexer = new Lexer(content);
                List<Token> tokens = lexer.getTokens();
                System.out.println("=========================================");
                System.out.println("=== Tokens do arquivo: " + filePath + " ===");
                System.out.println("=========================================");
                for (Token t : tokens) {
                    System.out.println(t);
                }
            } catch (IOException e) {
                System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            }
        } 
    }
}