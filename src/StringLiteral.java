import java.text.CharacterIterator;

public class StringLiteral extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        StringBuilder lexeme = new StringBuilder();
        
        if (code.current() != '"') {
            return null;
        }
        
        lexeme.append(code.current());
        code.next();
        
        while (code.current() != '"' && code.current() != CharacterIterator.DONE) {
            if (code.current() == '\\') {
                lexeme.append(code.current());
                code.next();
                if (code.current() != CharacterIterator.DONE) {
                    lexeme.append(code.current());
                    code.next();
                }
            } else {
                lexeme.append(code.current());
                code.next();
            }
        }
        
        if (code.current() == '"') {
            lexeme.append(code.current());
            code.next();
            return new Token("STRING", lexeme.toString());
        }
        
        throw new RuntimeException("String literal não fechada: " + lexeme.toString());
    }
}
