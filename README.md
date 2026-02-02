# Compilador - C simplificado em Português

Compilador educacional de C simplificado em português, com léxico e parser, voltado para aprendizado.

## Visão geral
Este projeto implementa as etapas iniciais de um compilador para uma linguagem inspirada em C, porém **bem simplificada** e com palavras‑chave em português (ex.: `inteiro`, `se`, `senao`, `exibir`, `ler`, `retorna`).

Fluxo principal:
1. **Análise léxica** (Lexer) – gera tokens.
2. **Análise sintática** (Parser) – gera/mostra AST.
3. **Tradução para C++** – converte a entrada da linguagem em `saida.cpp`.
4. **Compilação** – gera o executável `programa.exe`.

## Estrutura de pastas
```
.
├─ src/                 # Código-fonte Java
├─ inputs/              # Arquivos de entrada (programas e gramática)
├─ scripts/             # Scripts de execução
├─ build/               # Classes compiladas (.class)
└─ out/                 # Saídas geradas (saida.cpp, programa.exe)
```

## Arquivos principais
- `src/MainToken.java` – ponto de entrada que imprime os tokens do arquivo.
- `src/MainParser.java` – ponto de entrada que imprime a AST do arquivo.
- `src/Lexer.java` – faz a análise léxica e gera a lista de tokens.
- `src/Parser.java` – faz a análise sintática e monta a árvore (AST).
- `src/Token.java` – estrutura base dos tokens.
- `src/Node.java` / `src/Tree.java` – estruturas da árvore sintática.
- `src/AFD.java` – autômato usado no reconhecimento de tokens.
- `src/Identifier.java`, `src/Number.java`, `src/StringLiteral.java`, `src/MathOperator.java`, `src/Comment.java` – classes auxiliares de tokens.
- `inputs/codigo.txt` – exemplo de programa na linguagem.
- `inputs/Gramatica.txt` – gramática completa da linguagem.
- `scripts/teste_completo.bat` – compila e executa o fluxo completo.

## Requisitos
- **Java JDK** (para `javac` e `java`)
- **G++** (para compilar `saida.cpp`)
- Windows com PowerShell

> Dica: verifique se `javac` e `g++` estão no PATH.

## Como executar
### Execução completa (recomendado)
Execute apenas o arquivo `.bat` dentro da pasta `scripts/`.
```pwsh
cd scripts
.\teste_completo.bat
```

O script:
1. Compila os `.java` para `build/`
2. Executa `MainToken` com `inputs/codigo.txt`
3. Executa `MainParser` com `inputs/codigo.txt`
4. Gera `out/saida.cpp`
5. Compila para `out/programa.exe`
6. Executa `out/programa.exe`

### Trocar arquivo de entrada
Edite o script `scripts/teste_completo.bat` e troque a linha:
```
java -cp build MainToken inputs\codigo.txt
java -cp build MainParser inputs\codigo.txt
```
para outro arquivo dentro de `inputs/`, por exemplo:
```
java -cp build MainToken inputs\entrada_calculadora.txt
java -cp build MainParser inputs\entrada_calculadora.txt
```

## Funcionalidades (resumo)
- **Tokens**: reconhecimento de palavras‑chave, identificadores, números, strings, operadores e delimitadores.
- **Parser**: construção/impressão da árvore sintática (AST).
- **Tradução para C++**: a entrada na linguagem simplificada é convertida para C++ em `out/saida.cpp`.
- **Compilação**: `out/saida.cpp` é compilado com `g++` para gerar `out/programa.exe`.

## Exemplos de entrada
Arquivos prontos em `inputs/`:
- `codigo.txt`
- `entrada_calculadora.txt`
- `entrada_certa.txt`
- `entrada_chamadas.txt`
- `entrada_exp.txt`

## Saídas geradas
- `out/saida.cpp` – código C++ gerado a partir da linguagem de entrada
- `out/programa.exe` – executável compilado do C++ gerado

## Dúvidas comuns
**1) Mudei um arquivo `.java`. Preciso fazer algo?**
Sim. Rode novamente `scripts/teste_completo.bat` para recompilar.

**2) O script falhou com "javac não é reconhecido"**
Instale o JDK e adicione o `bin` ao PATH.

**3) O script falhou com "g++ não é reconhecido"**
Instale o MinGW ou MSYS2 e adicione o `bin` ao PATH.

## Licença
Uso educacional.
