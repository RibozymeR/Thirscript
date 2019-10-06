package thirscript.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import thirscript.exec.AssignExpr;
import thirscript.exec.BlockExpr;
import thirscript.exec.CmpExpr;
import thirscript.exec.IfExpr;
import thirscript.exec.IntLiteral;
import thirscript.exec.Expr;
import thirscript.exec.OpExpr;
import thirscript.exec.PrintExpr;
import thirscript.exec.UnaryOpExpr;
import thirscript.exec.VarExpr;
import thirscript.exec.WhileExpr;
import thirscript.parse.Token.TokenType;

public class Parser
{
    private Token current_token;
    private Iterator<Token> token_iter;

    // EVAL

    // parses expressions until EOF or }
    public Expr parse()
    {
        BlockExpr main = new BlockExpr();
        while (get() != Token.EOF && get().type != TokenType.RBRACE)
            main.addChild(parseCmp());
        return main;
    }

    // parses expression at current position
    // will end with pointer directly after expression
    public Expr parseExpr()
    {
        List<Character> pre_op = new ArrayList<>();
        Token t;
        while ((t = get()).type == TokenType.OP && "+ - ~ !".indexOf((String) t.value) >= 0)
        {
            next();
            pre_op.add(t.value.charAt(0));
        }

        Expr n;

        t = get();
        TokenType type = t.type;
        if (t.type == TokenType.LPAREN)
        {
            next();
            n = parseCmp();
            assertType(next(), TokenType.RPAREN);
        } else if (type == TokenType.LBRACE)
        {
            next();
            n = parse();
            assertType(next(), TokenType.RBRACE);
        } else if (t.type == TokenType.INT)
        {
            next();
            n = new IntLiteral(Long.parseLong(t.value));
        } else if (type == TokenType.IDENTIFIER)
        {
            VarExpr v = parseVar();
            if (get().type == TokenType.ASSIGN)
            {
                next();
                n = new AssignExpr(v, parseCmp());
            } else
                n = v;
        } else if (type == TokenType.PRINT)
        {
            next();
            assertType(next(), TokenType.LPAREN);
            n = new PrintExpr(parseCmp());
            assertType(next(), TokenType.RPAREN);
        } else if (type == TokenType.IF)
        {
            next();
            assertType(next(), TokenType.LPAREN);
            Expr test = parseCmp();
            assertType(next(), TokenType.RPAREN);

            Expr do_if = parseCmp();

            if (get().type == TokenType.ELSE)
            {
                next();
                n = new IfExpr(test, do_if, parseCmp());
            } else
                n = new IfExpr(test, do_if);
        } else if (type == TokenType.WHILE)
        {
            next();
            assertType(next(), TokenType.LPAREN);
            Expr test = parseCmp();
            assertType(next(), TokenType.RPAREN);

            n = new WhileExpr(test, parseCmp());
        } else
            throw new ParseException("Not a valid statement at " + t.pos);

        if (pre_op.size() > 0)
        {
            Collections.reverse(pre_op);
            n = new UnaryOpExpr(n, pre_op);
        }

        return n;
    }

    public Expr parseCmp()
    {
        Expr n = parseSum();

        Token t = get();
        if (t.type == TokenType.OP)
        {
            if ("== < <= > >= !=".indexOf(t.value) == -1) throw new ParseException("Not a valid operator at " + t.pos);
            next();
            return new CmpExpr(n, parseSum(), t.value);
        } else
            return n;
    }

    private Expr parseSum()
    {
        OpExpr n = new OpExpr(parseProd());
        Token t;
        while ((t = get()).type == TokenType.OP && "+-&|^".indexOf(t.value) != -1)
        {
            next();
            n.addChild(parseProd(), t.value.charAt(0));
        }
        return n.compress();
    }

    private Expr parseProd()
    {
        OpExpr n = new OpExpr(parseExpr());
        Token t;
        while ((t = get()).type == TokenType.OP && "*/%".indexOf(t.value) != -1)
        {
            next();
            n.addChild(parseExpr(), t.value.charAt(0));
        }
        return n.compress();
    }

    private VarExpr parseVar()
    {
        Token t = next();
        assertType(t, TokenType.IDENTIFIER);
        return new VarExpr(t.value);
    }

    // MISC

    // current token
    private Token get()
    {
        return current_token;
    }

    // current token, also advances pointer
    private Token next()
    {
        Token t = current_token;
        current_token = token_iter.next();
        return t;
    }

    private boolean assertType(Token token, TokenType type)
    {
        if (token.type == type)
            return true;
        else
            throw new ParseException("Token at " + token.pos + " (" + token.type + ") should be " + type);
    }

    public Expr parse(Iterator<Token> tokens)
    {
        token_iter = tokens;
        current_token = token_iter.next();

        return parse();
    }
}
