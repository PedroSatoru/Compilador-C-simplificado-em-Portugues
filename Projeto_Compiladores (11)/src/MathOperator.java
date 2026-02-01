import java.text.CharacterIterator;

public class MathOperator extends AFD {

    @Override
    public Token evaluate(CharacterIterator code) {

        switch (code.current()) {
        case '+':
            code.next();
            return new Token("SOMA", "+");

        case '-':
            code.next();
            return new Token("SUB", "-");

        case '*':
            code.next();
            return new Token("MULT", "*");

        case '/':
            code.next();
            return new Token("DIV", "/");

        case '(':
            code.next();
            return new Token("LPAREN", "(");

        case ')':
            code.next();
            return new Token("RPAREN", ")");

        case '}':
            code.next();
            return new Token("RCHAVE", "}");

        case '{':
            code.next();
            return new Token("LCHAVE", "{");

        case '=':
            code.next();
            if (code.current() == '=') {
                code.next();
                return new Token("EQUALS", "==");
            }
            return new Token("ASSIGN", "=");

        case '!':
            code.next();
            if (code.current() == '=') {
                code.next();
                return new Token("NOT_EQUALS", "!=");
            }
            return new Token("NOT", "!");

        case '<':
            code.next();
            if (code.current() == '=') {
                code.next();
                return new Token("LESS_EQUALS", "<=");
            }
            return new Token("LESS", "<");

        case '>':
            code.next();
            if (code.current() == '=') {
                code.next();
                return new Token("GREATER_EQUALS", ">=");
            }
            return new Token("GREATER", ">");

        case ';':
            code.next();
            return new Token("SEMICOLON", ";");

        case ',':
            code.next();
            return new Token("COMMA", ",");

        case '.':
            code.next();
            return new Token("DOT", ".");

        case ':':
            code.next();
            return new Token("COLON", ":");

        case CharacterIterator.DONE:
            return new Token("EOF", "$");

        default:
            return null;
        }
    }
}