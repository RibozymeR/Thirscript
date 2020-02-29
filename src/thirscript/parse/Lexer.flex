package thirscript.parse;

import thirscript.parse.Token.TokenType;

import java.io.IOException;
import java.util.Iterator;

%%

%public
%class Lexer
%public
%final
%type Token
%unicode
%line
%column

%{

    StringBuilder string = new StringBuilder();

    private Token token(TokenType type)
    {
        return new Token(type, yyline, yycolumn);
    }
    
    private Token text_token(TokenType type)
    {
        return new Token(type, yyline, yycolumn, yytext());
    }
    
    private Token text_token(TokenType type, String text)
    {
        return new Token(type, yyline, yycolumn, text);
    }
    
    public Iterator<Token> iterator()
    {
        return new Iterator<Token>() {
            @Override
            public boolean hasNext()
            {
                return !zzEOFDone;
            }
            @Override
            public Token next()
            {
                try
                {
                    return yylex();
                } catch (IOException e)
                {
                    e.printStackTrace();
                    return Token.EOF;
                }
            }
        };
    }
%}

/* main character classes */
LineTerminator = \r|\n|\r\n

Whitespace = {LineTerminator} | [ \t\f]+

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} 

TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" [^\r\n]* {LineTerminator}?

/* identifiers */

Identifier = [_a-zA-Z][_0-9a-zA-Z]*

/* int literals */

IntLiteral = 0 | [1-9][0-9]*

%state STRING

%%

<YYINITIAL> {

  /* keywords */
  
  "if"      { return token(TokenType.IF); }
  "else"    { return token(TokenType.ELSE); }
  "while"   { return token(TokenType.WHILE); }
  "new"     { return token(TokenType.NEW); }

  /* literals */
  
  {IntLiteral}
            { return text_token(TokenType.INT); }
  \"        { string.setLength(0); yybegin(STRING); }

  /* separators */
  
  "."           { return token(TokenType.PERIOD); }
  ","           { return token(TokenType.COMMA); }
  "="           { return token(TokenType.ASSIGN); }
  ":="          { return token(TokenType.ASSIGNC); }
  "#"           { return token(TokenType.FUNC); } 
  "("           { return token(TokenType.LPAREN); }
  ")"           { return token(TokenType.RPAREN); }
  "{"           { return token(TokenType.LBRACE); }
  "}"           { return token(TokenType.RBRACE); }
  "["           { return token(TokenType.LBRACKET); }
  "]"           { return token(TokenType.RBRACKET); }

  /* operators */
  "+" | "-" | "*" | "/" | "%" |
  "~" | "!" |
  "|" | "&" | "^" |
  "==" | "!=" | "<" | "<=" | ">" | ">="
                { return text_token(TokenType.OP); }

  {Identifier}  { return text_token(TokenType.IDENTIFIER); }
  
  {Comment}     { }
  {Whitespace}  { }
}

<STRING> {
  \'            { yybegin(YYINITIAL); return text_token(TokenType.STRING, string.toString()); }
  [^\'\\]+      { string.append( yytext() ); }
  \\t           { string.append('\t'); }
  \\n           { string.append('\n'); }
  \\r           { string.append('\r'); }
  \\\'          { string.append('\''); }
  \\            { string.append('\\'); }
}


/* error fallback */
[^]                              {  }
<<EOF>>                          { return Token.EOF; }
