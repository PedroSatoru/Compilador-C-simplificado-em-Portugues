import java.text.CharacterIterator;

public class Identifier extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        StringBuilder lexeme = new StringBuilder();
        
        if (!Character.isLetter(code.current()) && code.current() != '_') {
            return null;
        }
        
        while (Character.isLetterOrDigit(code.current()) || code.current() == '_') {
            lexeme.append(code.current());
            code.next();
        }
        
        String identifier = lexeme.toString();
        
        if (isKeyword(identifier)) {
            return new Token("KEYWORD", identifier);
        }
        
        return new Token("ID", identifier);
    }
    
    private boolean isKeyword(String word) {
        switch (word.toLowerCase()) {
            // Estruturas de controle
            case "se":          // if
            case "senao":       // else
            case "enquanto":    // while
            case "para":        // for
            case "execute":     // do
            case "pare":        // break
            case "continue":    // continue (mantém em inglês)
            case "retorna":     // return
            case "troque":      // switch
            case "caso":        // case
            case "padrao":      // default
            // Tipos de dados
            case "vazio":       // void
            case "inteiro":     // int
            case "real":        // float
            case "duplo":       // double
            case "caractere":   // char
            case "booleano":    // boolean
            case "verdadeiro":  // true
            case "falso":       // false
            case "texto":       // String
            case "incluir":     // include
            case "nulo":        // null
            // I/O
            case "ler":         // scanf/cin
            case "exibir":      // printf/cout
                return true;
            default:
                return false;
        }
    }
}
