package com.lnjoying.justice.iam.authz.opa.util;

import com.lnjoying.justice.iam.authz.opa.gen.RegoLexer;
import com.lnjoying.justice.iam.authz.opa.gen.RegoParser;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/5/16 16:10
 */

@Slf4j
public final class RegoSyntaxChecker
{
    private RegoSyntaxChecker(){}

    public static boolean regoSyntaxCheckError(String regoText)
    {
        CodePointCharStream charStream = CharStreams.fromString(regoText);
        RegoLexer lexer = new RegoLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        RegoParser parser = new RegoParser(tokens);
        parser.removeErrorListeners(); //
        BaseErrorListener errorListener = new LogErrorListener();
        parser.addErrorListener(errorListener);
        RegoParser.RootContext root = parser.root();
        int numberOfSyntaxErrors = parser.getNumberOfSyntaxErrors();
        if (numberOfSyntaxErrors > 0)
        {
           return true;
        }

        return false;
    }

    public static class LogErrorListener extends BaseErrorListener
    {
        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)  {
            log.error("line {}: {} {}, error:{}", line, charPositionInLine, msg, e);
        }
    }
}
