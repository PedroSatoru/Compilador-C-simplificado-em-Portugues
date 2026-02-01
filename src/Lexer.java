import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    List<Token> tokens;
    List<AFD> afds;
    CharacterIterator code;

    public Lexer(String code) {
        tokens = new ArrayList<>();
        this.code = new StringCharacterIterator(code);
        afds = new ArrayList<>();
        
        // Adiciona os AFDs na ordem de prioridade
        afds.add(new Comment());          // Comentários têm alta prioridade
        afds.add(new StringLiteral());    // Strings literais têm alta prioridade
        afds.add(new Number());           // Números
        afds.add(new Identifier());       // Identificadores e palavras-chave
        afds.add(new MathOperator());     // Operadores e símbolos
    }

    int linha_atual = 1;
    int coluna_atual = 1;
    
    public void skipWhiteSpace(){
        while (code.current() == ' ' || code.current() == '\n' || code.current() == '\t' || code.current() == '\r') {
            if (code.current() == '\n') {
                linha_atual++;
                coluna_atual = 1;
            } else {
                coluna_atual++;
            }
            code.next();
        }
    }

    public void error() {
        throw new RuntimeException("Erro: Token não reconhecido '" + code.current() + "' na linha " + linha_atual );
    }

    private Token searchNextToken() {
        int pos = code.getIndex();
        int startLine = linha_atual;
        int startCol = coluna_atual;
        
        for (AFD afd : afds) {
            Token t = afd.evaluate(code);
            if (t != null) {
                t.linha = startLine;
                t.coluna = startCol;
                
                String lx = t.lexema != null ? t.lexema : "";
                for (int i = 0; i < lx.length(); i++) {
                    char ch = lx.charAt(i);
                    if (ch == '\n') {
                        linha_atual++;
                        coluna_atual = 1;
                    } else {
                        coluna_atual++;
                    }
                }
                return t;
            }
            code.setIndex(pos);
        }
        return null;
    }

    public List<Token> getTokens() {
        Token t;
        do {
            skipWhiteSpace();
            t = searchNextToken();
            if (t == null) error();
            if (!"COMMENT".equals(t.tipo)) {
                tokens.add(t);
            }
        } while (!"EOF".equals(t.tipo));
        return tokens;
    }
}