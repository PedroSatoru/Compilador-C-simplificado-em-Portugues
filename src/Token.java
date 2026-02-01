public class Token {

    String tipo;
    String lexema;
    int linha;
    int coluna;

    public Token(String tipo, String lexema) {
    this.lexema = lexema;
    this.tipo = tipo;

    }

    @Override
    public String toString() {
    return "<" + tipo + ", " + lexema + ">";

    }

}