import java.text.CharacterIterator;

public class Number extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        StringBuilder lexeme = new StringBuilder();
        
        if (!Character.isDigit(code.current())) {
            return null;
        }
        
        while (Character.isDigit(code.current())) {
            lexeme.append(code.current());
            code.next();
        }
        
        if (code.current() == '.') {
            lexeme.append(code.current());
            code.next();
            
            if (!Character.isDigit(code.current())) {
                code.previous();
                lexeme.deleteCharAt(lexeme.length() - 1);
                return new Token("NUM", lexeme.toString());
            }
            
            while (Character.isDigit(code.current())) {
                lexeme.append(code.current());
                code.next();
            }
            
            return new Token("FLOAT", lexeme.toString());
        }
        
        return new Token("NUM", lexeme.toString());
    }
}
