package com.lnjoying.justice.iam.authz.opa.gen;// Generated from E:/feng/2023/5/java/antlr-rego/antlr-rego/src/antlr\RegoParser.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link RegoParser}.
 */
public interface RegoParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link RegoParser#root}.
	 * @param ctx the parse tree
	 */
	void enterRoot(RegoParser.RootContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#root}.
	 * @param ctx the parse tree
	 */
	void exitRoot(RegoParser.RootContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#stmt}.
	 * @param ctx the parse tree
	 */
	void enterStmt(RegoParser.StmtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#stmt}.
	 * @param ctx the parse tree
	 */
	void exitStmt(RegoParser.StmtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#regoPackage}.
	 * @param ctx the parse tree
	 */
	void enterRegoPackage(RegoParser.RegoPackageContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#regoPackage}.
	 * @param ctx the parse tree
	 */
	void exitRegoPackage(RegoParser.RegoPackageContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#importDirective}.
	 * @param ctx the parse tree
	 */
	void enterImportDirective(RegoParser.ImportDirectiveContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#importDirective}.
	 * @param ctx the parse tree
	 */
	void exitImportDirective(RegoParser.ImportDirectiveContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#regoRules}.
	 * @param ctx the parse tree
	 */
	void enterRegoRules(RegoParser.RegoRulesContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#regoRules}.
	 * @param ctx the parse tree
	 */
	void exitRegoRules(RegoParser.RegoRulesContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#ruleHead}.
	 * @param ctx the parse tree
	 */
	void enterRuleHead(RegoParser.RuleHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#ruleHead}.
	 * @param ctx the parse tree
	 */
	void exitRuleHead(RegoParser.RuleHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#ruleBody}.
	 * @param ctx the parse tree
	 */
	void enterRuleBody(RegoParser.RuleBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#ruleBody}.
	 * @param ctx the parse tree
	 */
	void exitRuleBody(RegoParser.RuleBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#ruleExt}.
	 * @param ctx the parse tree
	 */
	void enterRuleExt(RegoParser.RuleExtContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#ruleExt}.
	 * @param ctx the parse tree
	 */
	void exitRuleExt(RegoParser.RuleExtContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#regoElse}.
	 * @param ctx the parse tree
	 */
	void enterRegoElse(RegoParser.RegoElseContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#regoElse}.
	 * @param ctx the parse tree
	 */
	void exitRegoElse(RegoParser.RegoElseContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#regoBody}.
	 * @param ctx the parse tree
	 */
	void enterRegoBody(RegoParser.RegoBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#regoBody}.
	 * @param ctx the parse tree
	 */
	void exitRegoBody(RegoParser.RegoBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#nonEmptyBraceEnclosedBody}.
	 * @param ctx the parse tree
	 */
	void enterNonEmptyBraceEnclosedBody(RegoParser.NonEmptyBraceEnclosedBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#nonEmptyBraceEnclosedBody}.
	 * @param ctx the parse tree
	 */
	void exitNonEmptyBraceEnclosedBody(RegoParser.NonEmptyBraceEnclosedBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(RegoParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(RegoParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(RegoParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(RegoParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#literalExpr}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpr(RegoParser.LiteralExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#literalExpr}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpr(RegoParser.LiteralExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#withKeyword}.
	 * @param ctx the parse tree
	 */
	void enterWithKeyword(RegoParser.WithKeywordContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#withKeyword}.
	 * @param ctx the parse tree
	 */
	void exitWithKeyword(RegoParser.WithKeywordContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void enterFunctionCall(RegoParser.FunctionCallContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#functionCall}.
	 * @param ctx the parse tree
	 */
	void exitFunctionCall(RegoParser.FunctionCallContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#exprTermPair}.
	 * @param ctx the parse tree
	 */
	void enterExprTermPair(RegoParser.ExprTermPairContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#exprTermPair}.
	 * @param ctx the parse tree
	 */
	void exitExprTermPair(RegoParser.ExprTermPairContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#termPair}.
	 * @param ctx the parse tree
	 */
	void enterTermPair(RegoParser.TermPairContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#termPair}.
	 * @param ctx the parse tree
	 */
	void exitTermPair(RegoParser.TermPairContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#exprTermPairList}.
	 * @param ctx the parse tree
	 */
	void enterExprTermPairList(RegoParser.ExprTermPairListContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#exprTermPairList}.
	 * @param ctx the parse tree
	 */
	void exitExprTermPairList(RegoParser.ExprTermPairListContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#exprTerm}.
	 * @param ctx the parse tree
	 */
	void enterExprTerm(RegoParser.ExprTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#exprTerm}.
	 * @param ctx the parse tree
	 */
	void exitExprTerm(RegoParser.ExprTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#exprTermList}.
	 * @param ctx the parse tree
	 */
	void enterExprTermList(RegoParser.ExprTermListContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#exprTermList}.
	 * @param ctx the parse tree
	 */
	void exitExprTermList(RegoParser.ExprTermListContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#relationExpr}.
	 * @param ctx the parse tree
	 */
	void enterRelationExpr(RegoParser.RelationExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#relationExpr}.
	 * @param ctx the parse tree
	 */
	void exitRelationExpr(RegoParser.RelationExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#bitwiseOrExpr}.
	 * @param ctx the parse tree
	 */
	void enterBitwiseOrExpr(RegoParser.BitwiseOrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#bitwiseOrExpr}.
	 * @param ctx the parse tree
	 */
	void exitBitwiseOrExpr(RegoParser.BitwiseOrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#bitwiseAndExpr}.
	 * @param ctx the parse tree
	 */
	void enterBitwiseAndExpr(RegoParser.BitwiseAndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#bitwiseAndExpr}.
	 * @param ctx the parse tree
	 */
	void exitBitwiseAndExpr(RegoParser.BitwiseAndExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#arithExpr}.
	 * @param ctx the parse tree
	 */
	void enterArithExpr(RegoParser.ArithExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#arithExpr}.
	 * @param ctx the parse tree
	 */
	void exitArithExpr(RegoParser.ArithExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#factorExpr}.
	 * @param ctx the parse tree
	 */
	void enterFactorExpr(RegoParser.FactorExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#factorExpr}.
	 * @param ctx the parse tree
	 */
	void exitFactorExpr(RegoParser.FactorExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(RegoParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(RegoParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#arrayComprehension}.
	 * @param ctx the parse tree
	 */
	void enterArrayComprehension(RegoParser.ArrayComprehensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#arrayComprehension}.
	 * @param ctx the parse tree
	 */
	void exitArrayComprehension(RegoParser.ArrayComprehensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#setComprehension}.
	 * @param ctx the parse tree
	 */
	void enterSetComprehension(RegoParser.SetComprehensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#setComprehension}.
	 * @param ctx the parse tree
	 */
	void exitSetComprehension(RegoParser.SetComprehensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#objectComprehension}.
	 * @param ctx the parse tree
	 */
	void enterObjectComprehension(RegoParser.ObjectComprehensionContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#objectComprehension}.
	 * @param ctx the parse tree
	 */
	void exitObjectComprehension(RegoParser.ObjectComprehensionContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#object_}.
	 * @param ctx the parse tree
	 */
	void enterObject_(RegoParser.Object_Context ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#object_}.
	 * @param ctx the parse tree
	 */
	void exitObject_(RegoParser.Object_Context ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#objectItem}.
	 * @param ctx the parse tree
	 */
	void enterObjectItem(RegoParser.ObjectItemContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#objectItem}.
	 * @param ctx the parse tree
	 */
	void exitObjectItem(RegoParser.ObjectItemContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#array_}.
	 * @param ctx the parse tree
	 */
	void enterArray_(RegoParser.Array_Context ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#array_}.
	 * @param ctx the parse tree
	 */
	void exitArray_(RegoParser.Array_Context ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#set_}.
	 * @param ctx the parse tree
	 */
	void enterSet_(RegoParser.Set_Context ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#set_}.
	 * @param ctx the parse tree
	 */
	void exitSet_(RegoParser.Set_Context ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#emptySet}.
	 * @param ctx the parse tree
	 */
	void enterEmptySet(RegoParser.EmptySetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#emptySet}.
	 * @param ctx the parse tree
	 */
	void exitEmptySet(RegoParser.EmptySetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#nonEmptySet}.
	 * @param ctx the parse tree
	 */
	void enterNonEmptySet(RegoParser.NonEmptySetContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#nonEmptySet}.
	 * @param ctx the parse tree
	 */
	void exitNonEmptySet(RegoParser.NonEmptySetContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#ref}.
	 * @param ctx the parse tree
	 */
	void enterRef(RegoParser.RefContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#ref}.
	 * @param ctx the parse tree
	 */
	void exitRef(RegoParser.RefContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#refOperand}.
	 * @param ctx the parse tree
	 */
	void enterRefOperand(RegoParser.RefOperandContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#refOperand}.
	 * @param ctx the parse tree
	 */
	void exitRefOperand(RegoParser.RefOperandContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#refOperandDot}.
	 * @param ctx the parse tree
	 */
	void enterRefOperandDot(RegoParser.RefOperandDotContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#refOperandDot}.
	 * @param ctx the parse tree
	 */
	void exitRefOperandDot(RegoParser.RefOperandDotContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#refOperandCanonical}.
	 * @param ctx the parse tree
	 */
	void enterRefOperandCanonical(RegoParser.RefOperandCanonicalContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#refOperandCanonical}.
	 * @param ctx the parse tree
	 */
	void exitRefOperandCanonical(RegoParser.RefOperandCanonicalContext ctx);
	/**
	 * Enter a parse tree produced by {@link RegoParser#scalar}.
	 * @param ctx the parse tree
	 */
	void enterScalar(RegoParser.ScalarContext ctx);
	/**
	 * Exit a parse tree produced by {@link RegoParser#scalar}.
	 * @param ctx the parse tree
	 */
	void exitScalar(RegoParser.ScalarContext ctx);
}