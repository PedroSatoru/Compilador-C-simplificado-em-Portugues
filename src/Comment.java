import java.text.CharacterIterator;

public class Comment extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {
        StringBuilder lexeme = new StringBuilder();
        
        if (code.getIndex() + 1 < ((java.text.StringCharacterIterator)code).getEndIndex()) {
            int currentPos = code.getIndex();
            
            if (code.current() == '/') {
                code.next();
                if (code.current() == '/') {
                    code.setIndex(currentPos);
                    while (code.current() != '\n' && code.current() != CharacterIterator.DONE) {
                        lexeme.append(code.current());
                        code.next();
                    }
                    return new Token("COMMENT", lexeme.toString());
                }
                code.setIndex(currentPos);
            }
            
            if (code.current() == '#') {
                code.next();
                if (code.current() == '#') {
                    code.setIndex(currentPos);
                    while (code.current() != '\n' && code.current() != CharacterIterator.DONE) {
                        lexeme.append(code.current());
                        code.next();
                    }
                    return new Token("COMMENT", lexeme.toString());
                }
                code.setIndex(currentPos);
            }
        }
        
        if (code.current() == '/' && 
            code.getIndex() + 1 < ((java.text.StringCharacterIterator)code).getEndIndex()) {
            
            int currentPos = code.getIndex();
            code.next();
            
            if (code.current() == '*') {
                code.setIndex(currentPos);
                
                lexeme.append(code.current());
                code.next();
                lexeme.append(code.current());
                code.next();
                
                while (code.current() != CharacterIterator.DONE) {
                    if (code.current() == '*') {
                        lexeme.append(code.current());
                        code.next();
                        if (code.current() == '/') {
                            lexeme.append(code.current());
                            code.next();
                            return new Token("COMMENT", lexeme.toString());
                        }
                    } else {
                        lexeme.append(code.current());
                        code.next();
                    }
                }
                
                throw new RuntimeException("Comentário de bloco não fechado: " + lexeme.toString());
            }
            
            code.setIndex(currentPos);
        }
        
        return null;
    }
}
