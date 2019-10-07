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
%char

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

/* identifiers */

Identifier = [_a-zA-Z][_0-9a-zA-Z]*

/* int literals */

IntLiteral = 0 | [1-9][0-9]*

%%

<YYINITIAL> {

  /* keywords */
  
  "if"      { return token(TokenType.IF); }
  "else"    { return token(TokenType.ELSE); }
  "while"   { return token(TokenType.WHILE); }

  /* literals */
  
  {IntLiteral}
            { return text_token(TokenType.INT); }

  /* separators */
  
  "."           { return token(TokenType.PERIOD); }
  ","           { return token(TokenType.COMMA); }
  "="           { return token(TokenType.ASSIGN); }
  "#"           { return token(TokenType.FUNC); } 
  "("           { return token(TokenType.LPAREN); }
  ")"           { return token(TokenType.RPAREN); }
  "{"           { return token(TokenType.LBRACE); }
  "}"           { return token(TokenType.RBRACE); }

  /* operators */
  "+" | "-" | "*" | "/" | "%" |
  "~" | "!" |
  "|" | "&" | "^" |
  "==" | "!=" | "<" | "<=" | ">" | ">="
                { return text_token(TokenType.OP); }

  {Identifier}  { return text_token(TokenType.IDENTIFIER); }
  
  {Whitespace}  { }
}


/* error fallback */
[^]                              {  }
<<EOF>>                          { return Token.EOF; }