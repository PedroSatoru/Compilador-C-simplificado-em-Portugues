import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Parser {
    List<Token> tokens;
    Token token;
    Tree tree;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.tree = new Tree();
    }

    public Token getNextToken() {
        if (tokens.size() > 0) return tokens.remove(0);
        return null;
    }

    private void erro(String regra) {
        String erro_formatado = "Erro sintático! \nRegra: " + regra + "\n" + "Token inválido: " + token.lexema;
        throw new RuntimeException(erro_formatado);
    }


    public void analisarPrograma() {
        token = getNextToken();
        Node root = new Node("analisarPrograma");
        tree.setRoot(root);
    
        header();
        do {
            if(!analisarDeclaracaoFuncao(root)) erro("DeclaracaoFuncao");
        } while (!estaNoFim());
        if (!estaNoFim()) erro("DeclaracaoFuncao");
        tree.printTree();
    }

    private boolean analisarDeclaracaoFuncao(Node node) {
        Node declaracaoFuncao = node.addNode("declaracaoFuncao");

        if (!eTipoKeyword(declaracaoFuncao)) {
            erro("KEYWORD");
            return false;
        }

        if (!matchT("ID", token.lexema + " ", declaracaoFuncao)) {
            erro("ID");
            return false;
        }

    
        if (!matchT("LPAREN", "( ", declaracaoFuncao)) {
            erro("LPAREN");
            return false;
        }

        if (token != null && "RPAREN".equals(token.tipo)) {
            if (!matchT("RPAREN", ") ", declaracaoFuncao)) { erro("RPAREN"); return false; }
        } else if (token != null && "KEYWORD".equals(token.tipo)) {
            if (!analisarArgumentosDef(declaracaoFuncao)) { erro("Argumentos"); return false; }
            if (!matchT("RPAREN", ") ", declaracaoFuncao)) { erro("RPAREN"); return false; }
        } else {
            erro("RPAREN ou argumentos");
            return false;
        }
    
        if (!analisarBloco(declaracaoFuncao)) {
            erro("Bloco");
            return false;
        }
    
        return true;
    }

    private boolean analisarArgumentosDef(Node node) {
        Node argumentos = node.addNode("ArgumentosDef");
        analisarParametro(argumentos);
        while (matchT("COMMA", ", ", argumentos)) {
            if (!analisarParametro(argumentos)) return false;
        }
        return true;
    }

    private boolean analisarArgumentosChamada(Node node) {
        Node argumentos = node.addNode("ArgumentosChamada");
        if (!analisarExpressao(argumentos)) return false;
        while (matchT("COMMA", ", ", argumentos)) {
            if (!analisarExpressao(argumentos)) return false;
        }
        return true;
    }

    private boolean analisarParametro(Node node) {
        Node parametro = node.addNode("Parametros");
        if (!eTipoKeyword(parametro)) {
            erro("KEYWORD");
            return false;
        }
        if (!matchT("ID", token.lexema + " ", parametro)) {
            erro("ID");
            return false;
        }
        return true;
    }

    private boolean analisarBloco(Node node) {
        Node bloco = node.addNode("Bloco");

        if (matchT("LCHAVE", "{\n", bloco) && 
            analisarComandos(bloco) && 
            matchT("RCHAVE", "}\n", bloco)) {
            return true;
        }
        return false;
    }

    private boolean analisarComandos(Node node) {
        Node comandos = node.addNode("Comandos");
        if (!analisarComando(comandos)) return false;
        while (analisarComando(comandos)) {
            continue;
        }
        return true;
    }

    private boolean analisarComando(Node node) {
        if (token == null || "RCHAVE".equals(token.tipo)) {
            return false;
        }

        Node comando = node.addNode("Comando");
        
        if (token.lexema != null && token.lexema.equals("retorna")) {
            return analisarRetorna(comando);
        }
        
        if (token.lexema != null && token.lexema.equals("se")) {
            return analisarCondicional(comando);
        }
        
        if (token.lexema != null && token.lexema.equals("enquanto")) {
            return analisarEnquanto(comando);
        }
        
        if (token.lexema != null && token.lexema.equals("para")) {
            return analisarPara(comando);
        }
        
        if (token.lexema != null && token.lexema.equals("ler")) {
            return analisarLer(comando);
        }
        
        if (token.lexema != null && token.lexema.equals("exibir")) {
            return analisarExibir(comando);
        }

        if (eTipoKeyword(comando)) {
            if (!matchT("ID", token.lexema + " ", comando)) erro("ID");
            if (matchT("ASSIGN", "= ", comando)) {
                if (!analisarExpressao(comando)) erro("Expressao");
                if (!matchT("SEMICOLON", ";\n", comando)) erro("SEMICOLON");
            } else {
                if (!matchT("SEMICOLON", ";\n", comando)) erro("SEMICOLON");
            }
            return true;
        }

        if ("ID".equals(token.tipo)) {
            Token next = (tokens.size() > 0) ? tokens.get(0) : null;
            
            if (next != null && "LPAREN".equals(next.tipo)) {
                if (!matchT("ID", token.lexema + " ", comando)) return false;
                if (!matchT("LPAREN", "( ", comando)) return false;
                
                if (token != null && "RPAREN".equals(token.tipo)) {
                    if (!matchT("RPAREN", ") ", comando)) return false;
                } else {
                    if (!analisarArgumentosChamada(comando)) return false;
                    if (!matchT("RPAREN", ") ", comando)) return false;
                }
                
                if (!matchT("SEMICOLON", ";\n", comando)) return false;
                return true;
            } else if (next != null && "ASSIGN".equals(next.tipo)) {
                if (analisarAtribuicao(comando) && matchT("SEMICOLON", ";\n", comando)) return true;
                erro("Comando");
                return false;
            } else {
                erro("Esperado '=' ou '(' após identificador");
                return false;
            }
        }

        return false;
    }

    private boolean analisarRetorna(Node node) {
        Node retorna = node.addNode("Retorna");

        if (!matchL("retorna", "return ", retorna)) return false;
        if (matchT("SEMICOLON", ";\n", retorna)) return true;
        if (matchT("ID", token.lexema + " ", retorna) || 
            matchT("NUM", token.lexema + " ", retorna) || 
            matchT("FLOAT", token.lexema + " ", retorna) || 
            matchT("STRING", token.lexema + " ", retorna)) {
            if (matchT("SEMICOLON", ";\n", retorna)) {
                return true;
            }
            erro("Esperado ';' após retorno");
            return false;
        }        
        erro("ID ou NUM ou STRING esperado após 'retorna'");
        return false;
    }

    private boolean analisarLer(Node node) {
        Node ler = node.addNode("Ler");
        
        if (!matchL("ler", "", ler)) return false;
        if (!matchT("LPAREN", "", ler)) {
            erro("Esperado '(' após 'ler'");
            return false;
        }
        
        if (!token.tipo.equals("ID")) {
            erro("Esperado variável após 'ler('");
            return false;
        }
        
        String varName = token.lexema;
        ler.addNode(varName);
        traduz("std::cin >> " + varName + " ");
        token = getNextToken();
        
        if (!matchT("RPAREN", "", ler)) {
            erro("Esperado ')' após variável em 'ler'");
            return false;
        }
        
        if (!matchT("SEMICOLON", ";\n", ler)) {
            erro("Esperado ';' após 'ler(...)'");
            return false;
        }
        
        return true;
    }

    private boolean analisarExibir(Node node) {
        Node exibir = node.addNode("Exibir");
        
        if (!matchL("exibir", "", exibir)) return false;
        if (!matchT("LPAREN", "", exibir)) {
            erro("Esperado '(' após 'exibir'");
            return false;
        }
        
        if (token.tipo.equals("STRING")) {
            String str = token.lexema;
            exibir.addNode(str);
            traduz("std::cout << " + str + " ");
            token = getNextToken();
        } else if (token.tipo.equals("ID")) {
            String varName = token.lexema;
            exibir.addNode(varName);
            traduz("std::cout << " + varName + " ");
            token = getNextToken();
        } else if (token.tipo.equals("NUM") || token.tipo.equals("FLOAT")) {
            String num = token.lexema;
            exibir.addNode(num);
            traduz("std::cout << " + num + " ");
            token = getNextToken();
        } else {
            erro("Esperado string, variável ou número após 'exibir('");
            return false;
        }
        
        if (!matchT("RPAREN", "", exibir)) {
            erro("Esperado ')' após argumento em 'exibir'");
            return false;
        }
        
        if (!matchT("SEMICOLON", ";\n", exibir)) {
            erro("Esperado ';' após 'exibir(...)'");
            return false;
        }
        
        return true;
    }

    private boolean analisarCondicional(Node node) {
        Node condicional = node.addNode("Condicional");
        if (matchL("se", "if ", condicional) &&
            matchT("LPAREN", "( ", condicional) &&
            analisarCondicao(condicional) &&
            matchT("RPAREN", ") ", condicional) &&
            analisarBloco(condicional)) {
                if (token != null && "KEYWORD".equals(token.tipo) && "senao".equals(token.lexema)) {
                    if (matchL("senao", "else ", condicional) && analisarBloco(condicional)) {
                        return true;
                    }
                    return false;
                }
                return true;
            }
        return false;
    }

    private boolean analisarEnquanto(Node node) {
        Node enquanto = node.addNode("Enquanto");
        if (matchL("enquanto", "while ", enquanto) &&
            matchT("LPAREN", "( ", enquanto) &&
            analisarCondicao(enquanto) &&
            matchT("RPAREN", ") ", enquanto) &&
            analisarBloco(enquanto)) {
                return true;
            }
        return false;
    }

    private boolean analisarPara(Node node) {
        Node para = node.addNode("Para");
        if (matchL("para", "for ", para) &&
            matchT("LPAREN", "( ", para) &&
            analisarAtribuicao(para) &&
            matchT("SEMICOLON", ";\n", para) &&
            analisarCondicao(para) &&
            matchT("SEMICOLON", ";\n", para) &&
            analisarPasso(para) &&
            matchT("RPAREN", ") ", para) &&
            analisarBloco(para)) {
                return true;
            }
        return false;
    }

    private boolean analisarCondicao(Node node) {
        Node condicao = node.addNode("Condicao");
        if (matchT("ID", token.lexema + " ", condicao)) {
            if (matchT("LESS", "< ", condicao) ||
                matchT("GREATER", "> ", condicao) ||
                matchT("EQUALS", "== ", condicao) ||
                matchT("LESS_EQUALS", "<= ", condicao) ||
                matchT("GREATER_EQUALS", ">= ", condicao) ||
                matchT("NOT_EQUALS", "!= ", condicao)) {
                    if (matchT("NUM", token.lexema + " ", condicao)) {
                        return true;
                    } else if (matchT("ID", token.lexema + " ", condicao)) {
                        return true;
                    }
                    erro("Condicao");
                    return false;
                }
            erro("Condicao");
            return false;
        }
        erro("Condicao");
        return false;
    }

    private boolean analisarPasso(Node node) {
        Node passo = node.addNode("Passo");
        if (token == null || !"ID".equals(token.tipo)) {
            erro("Passo");
            return false;
        }
        
        String varName = token.lexema;
        token = getNextToken();
        passo.addNode(varName);
        
        String operator;
        if (token != null && "SOMA".equals(token.tipo)) {
            operator = "+";
            token = getNextToken();
        } else if (token != null && "SUB".equals(token.tipo)) {
            operator = "-";
            token = getNextToken();
        } else if (token != null && "MULT".equals(token.tipo)) {
            operator = "*";
            token = getNextToken();
        } else if (token != null && "DIV".equals(token.tipo)) {
            operator = "/";
            token = getNextToken();
        } else {
            erro("Passo - operador");
            return false;
        }
        
        if (token != null && "NUM".equals(token.tipo)) {
            String numValue = token.lexema;
            passo.addNode(operator);
            passo.addNode(numValue);
            traduz(varName + " = " + varName + " " + operator + " " + numValue + " ");
            token = getNextToken();
            return true;
        }
        
        erro("Passo - NUM");
        return false;
    }

    private boolean analisarAtribuicao(Node node) {
        Node atribuicao = node.addNode("Atribuição");
        if (matchT("ID", token.lexema + " ", atribuicao) &&
            matchT("ASSIGN", "= ", atribuicao)) {
                if (analisarExpressao(atribuicao)) {
                    return true;
                }
            }
        return false;
        }

    private boolean analisarExpressao(Node node) {
        Node exp = node.addNode("Expressao");
        return analisarE(exp);
    }

    private boolean analisarE(Node node) {
        Node n = node.addNode("E");
        if (!analisarT(n)) return false;
        return analisarEPrime(n);
    }

    private boolean analisarEPrime(Node node) {
        Node n = node.addNode("E'");
        while (token != null && ("SOMA".equals(token.tipo) || "SUB".equals(token.tipo))) {
            if (matchT("SOMA", "+ ", n)) {
                if (!analisarT(n)) { erro("SOMA"); return false; }
            } else if (matchT("SUB", "- ", n)) {
                if (!analisarT(n)) { erro("SUB"); return false; }
            } else {
                break;
            }
        }
        return true;
    }

    private boolean analisarT(Node node) {
        Node n = node.addNode("T");
        if (!analisarF(n)) return false;
        return analisarTPrime(n);
    }

    private boolean analisarTPrime(Node node) {
        Node n = node.addNode("T'");
        while (token != null && ("MULT".equals(token.tipo) || "DIV".equals(token.tipo))) {
            if (matchT("MULT", "* ", n)) {
                if (!analisarF(n)) { erro("MULT"); return false; }
            } else if (matchT("DIV", "/ ", n)) {
                if (!analisarF(n)) { erro("DIV"); return false; }
            } else {
                break;
            }
        }
        return true;
    }

    private boolean analisarF(Node node) {
        Node f = node.addNode("F");
        if (token == null) return false;

        if ("LPAREN".equals(token.tipo)) {
            if (!matchT("LPAREN", "( ", f)) return false;
            if (!analisarE(f)) { erro("F"); return false; }
            if (!matchT("RPAREN", ") ", f)) { erro("T"); return false; }
            return true;
        }

        if ("ID".equals(token.tipo)) {
            Token next = (tokens.size() > 0) ? tokens.get(0) : null;
            
            if (next != null && "LPAREN".equals(next.tipo)) {
                if (!matchT("ID", token.lexema + " ", f)) return false;
                if (!matchT("LPAREN", "( ", f)) return false;

                if (token != null && "RPAREN".equals(token.tipo)) {
                    if (!matchT("RPAREN", ") ", f)) { erro("RPAREN"); return false; }
                    return true;
                } else {
                    if (!analisarArgumentosChamada(f)) { erro("Argumentos"); return false; }
                    if (!matchT("RPAREN", ") ", f)) { erro("RPAREN"); return false; }
                    return true;
                }
            } else {
                return matchT("ID", token.lexema + " ", f);
            }
        }

        if ("NUM".equals(token.tipo) || "FLOAT".equals(token.tipo)) {
            return matchT(token.tipo, token.lexema + " ", f);
        }

        if ("STRING".equals(token.tipo)) {
            return matchT("STRING", token.lexema + " ", f);
        }

        return false;
    }

    private boolean eTipoKeyword(Node node) {
        Node keyword = node.addNode("KEYWORD");
    
        if (token == null) return false;
        if (!"KEYWORD".equals(token.tipo)) return false;
        return matchL("inteiro", "int ", keyword) ||
               matchL("vazio",   "void ", keyword) ||
               matchL("real",    "float ", keyword) ||
               matchL("caractere","char ", keyword) ||
               matchL("duplo",   "double ", keyword) ||
               matchL("booleano","bool ", keyword);
    }

    private boolean matchT(String tipo, String newcode, Node node) {
        if (token.tipo.equals(tipo)) {
            if (token.lexema.equals("principal")) traduz("main"); else traduz(newcode);
            node.addNode(token.lexema);
            token = getNextToken();
            return true;
        }
        return false;
    }

    private boolean matchL(String lexema, String newcode, Node node) {
        if (token.lexema.equals(lexema)) {
            traduz(newcode);
            node.addNode(token.lexema);
            token = getNextToken();
            return true;
        }
        return false;
    }

    private void header() {
        String conteudo = "#include <iostream>\n#include <string>\n\n";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("saida.txt", false))) {
            writer.write(conteudo);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo saida.txt: " + e.getMessage());
        }
    }

    private void traduz(String conteudo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("saida.txt", true))) {
            writer.write(conteudo);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo saida.txt: " + e.getMessage());
        }
    }

    private boolean estaNoFim() {
        return token.tipo.equals("EOF");
    }
}
