package com.lnjoying.justice.iam.authz.opa.gen;// Generated from E:/feng/2023/5/java/antlr-rego/antlr-rego/src/antlr\RegoParser.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link RegoParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface RegoParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link RegoParser#root}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRoot(RegoParser.RootContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStmt(RegoParser.StmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#regoPackage}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegoPackage(RegoParser.RegoPackageContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#importDirective}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImportDirective(RegoParser.ImportDirectiveContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#regoRules}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegoRules(RegoParser.RegoRulesContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#ruleHead}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleHead(RegoParser.RuleHeadContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#ruleBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleBody(RegoParser.RuleBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#ruleExt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRuleExt(RegoParser.RuleExtContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#regoElse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegoElse(RegoParser.RegoElseContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#regoBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegoBody(RegoParser.RegoBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#nonEmptyBraceEnclosedBody}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonEmptyBraceEnclosedBody(RegoParser.NonEmptyBraceEnclosedBodyContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQuery(RegoParser.QueryContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(RegoParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#literalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralExpr(RegoParser.LiteralExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#withKeyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWithKeyword(RegoParser.WithKeywordContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#functionCall}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionCall(RegoParser.FunctionCallContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#exprTermPair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprTermPair(RegoParser.ExprTermPairContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#termPair}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermPair(RegoParser.TermPairContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#exprTermPairList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprTermPairList(RegoParser.ExprTermPairListContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#exprTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprTerm(RegoParser.ExprTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#exprTermList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprTermList(RegoParser.ExprTermListContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#relationExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelationExpr(RegoParser.RelationExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#bitwiseOrExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwiseOrExpr(RegoParser.BitwiseOrExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#bitwiseAndExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBitwiseAndExpr(RegoParser.BitwiseAndExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#arithExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithExpr(RegoParser.ArithExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#factorExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactorExpr(RegoParser.FactorExprContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(RegoParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#arrayComprehension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArrayComprehension(RegoParser.ArrayComprehensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#setComprehension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSetComprehension(RegoParser.SetComprehensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#objectComprehension}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectComprehension(RegoParser.ObjectComprehensionContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#object_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject_(RegoParser.Object_Context ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#objectItem}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectItem(RegoParser.ObjectItemContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#array_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_(RegoParser.Array_Context ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#set_}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSet_(RegoParser.Set_Context ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#emptySet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEmptySet(RegoParser.EmptySetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#nonEmptySet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNonEmptySet(RegoParser.NonEmptySetContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRef(RegoParser.RefContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#refOperand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefOperand(RegoParser.RefOperandContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#refOperandDot}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefOperandDot(RegoParser.RefOperandDotContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#refOperandCanonical}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRefOperandCanonical(RegoParser.RefOperandCanonicalContext ctx);
	/**
	 * Visit a parse tree produced by {@link RegoParser#scalar}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScalar(RegoParser.ScalarContext ctx);
}