package com.lnjoying.justice.iam.authz.opa.gen;// Generated from E:/feng/2023/5/java/antlr-rego/antlr-rego/src/antlr\RegoParser.g4 by ANTLR 4.12.0
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class RegoParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		Comment=1, SomeComment=2, String=3, Bool=4, Null=5, As=6, Default=7, Else=8, 
		Import=9, Package=10, Not=11, With=12, Set=13, LSBrace=14, LCBrace=15, 
		LParan=16, RSBrace=17, RCBrace=18, RParan=19, Mid=20, FactorOperator=21, 
		ArithOperator=22, RelationOperator=23, EqOper=24, Comma=25, Semicolon=26, 
		Colon=27, Ampersand=28, Dot=29, Whitespace=30, Newline=31, WhiteSpace=32, 
		LineEnd=33, WindowsLineEnd=34, Name=35, SomeA=36, UnsignedNumber=37;
	public static final int
		RULE_root = 0, RULE_stmt = 1, RULE_regoPackage = 2, RULE_importDirective = 3, 
		RULE_regoRules = 4, RULE_ruleHead = 5, RULE_ruleBody = 6, RULE_ruleExt = 7, 
		RULE_regoElse = 8, RULE_regoBody = 9, RULE_nonEmptyBraceEnclosedBody = 10, 
		RULE_query = 11, RULE_literal = 12, RULE_literalExpr = 13, RULE_withKeyword = 14, 
		RULE_functionCall = 15, RULE_exprTermPair = 16, RULE_termPair = 17, RULE_exprTermPairList = 18, 
		RULE_exprTerm = 19, RULE_exprTermList = 20, RULE_relationExpr = 21, RULE_bitwiseOrExpr = 22, 
		RULE_bitwiseAndExpr = 23, RULE_arithExpr = 24, RULE_factorExpr = 25, RULE_term = 26, 
		RULE_arrayComprehension = 27, RULE_setComprehension = 28, RULE_objectComprehension = 29, 
		RULE_object_ = 30, RULE_objectItem = 31, RULE_array_ = 32, RULE_set_ = 33, 
		RULE_emptySet = 34, RULE_nonEmptySet = 35, RULE_ref = 36, RULE_refOperand = 37, 
		RULE_refOperandDot = 38, RULE_refOperandCanonical = 39, RULE_scalar = 40;
	private static String[] makeRuleNames() {
		return new String[] {
			"root", "stmt", "regoPackage", "importDirective", "regoRules", "ruleHead", 
			"ruleBody", "ruleExt", "regoElse", "regoBody", "nonEmptyBraceEnclosedBody", 
			"query", "literal", "literalExpr", "withKeyword", "functionCall", "exprTermPair", 
			"termPair", "exprTermPairList", "exprTerm", "exprTermList", "relationExpr", 
			"bitwiseOrExpr", "bitwiseAndExpr", "arithExpr", "factorExpr", "term", 
			"arrayComprehension", "setComprehension", "objectComprehension", "object_", 
			"objectItem", "array_", "set_", "emptySet", "nonEmptySet", "ref", "refOperand", 
			"refOperandDot", "refOperandCanonical", "scalar"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, "'null'", "'as'", "'default'", "'else'", 
			"'import'", "'package'", "'not'", "'with'", "'set('", "'['", "'{'", "'('", 
			"']'", "'}'", "')'", "'|'", null, null, null, null, "','", "';'", "':'", 
			"'&'", "'.'", null, null, null, null, null, null, "'some'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "Comment", "SomeComment", "String", "Bool", "Null", "As", "Default", 
			"Else", "Import", "Package", "Not", "With", "Set", "LSBrace", "LCBrace", 
			"LParan", "RSBrace", "RCBrace", "RParan", "Mid", "FactorOperator", "ArithOperator", 
			"RelationOperator", "EqOper", "Comma", "Semicolon", "Colon", "Ampersand", 
			"Dot", "Whitespace", "Newline", "WhiteSpace", "LineEnd", "WindowsLineEnd", 
			"Name", "SomeA", "UnsignedNumber"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "RegoParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public RegoParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RootContext extends ParserRuleContext {
		public RegoPackageContext regoPackage() {
			return getRuleContext(RegoPackageContext.class,0);
		}
		public TerminalNode EOF() { return getToken(RegoParser.EOF, 0); }
		public List<StmtContext> stmt() {
			return getRuleContexts(StmtContext.class);
		}
		public StmtContext stmt(int i) {
			return getRuleContext(StmtContext.class,i);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRoot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRoot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRoot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			regoPackage();
			setState(86);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 171803011768L) != 0)) {
				{
				{
				setState(83);
				stmt();
				}
				}
				setState(88);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(89);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StmtContext extends ParserRuleContext {
		public ImportDirectiveContext importDirective() {
			return getRuleContext(ImportDirectiveContext.class,0);
		}
		public RegoRulesContext regoRules() {
			return getRuleContext(RegoRulesContext.class,0);
		}
		public RegoBodyContext regoBody() {
			return getRuleContext(RegoBodyContext.class,0);
		}
		public StmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stmt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterStmt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitStmt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitStmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StmtContext stmt() throws RecognitionException {
		StmtContext _localctx = new StmtContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stmt);
		try {
			setState(94);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(91);
				importDirective();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(92);
				regoRules();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(93);
				regoBody();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RegoPackageContext extends ParserRuleContext {
		public TerminalNode Package() { return getToken(RegoParser.Package, 0); }
		public RefContext ref() {
			return getRuleContext(RefContext.class,0);
		}
		public RegoPackageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_regoPackage; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRegoPackage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRegoPackage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRegoPackage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RegoPackageContext regoPackage() throws RecognitionException {
		RegoPackageContext _localctx = new RegoPackageContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_regoPackage);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(96);
			match(Package);
			setState(97);
			ref();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ImportDirectiveContext extends ParserRuleContext {
		public RefContext import_target;
		public RefContext import_target_rename_as;
		public TerminalNode Import() { return getToken(RegoParser.Import, 0); }
		public List<RefContext> ref() {
			return getRuleContexts(RefContext.class);
		}
		public RefContext ref(int i) {
			return getRuleContext(RefContext.class,i);
		}
		public TerminalNode As() { return getToken(RegoParser.As, 0); }
		public ImportDirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importDirective; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterImportDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitImportDirective(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitImportDirective(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportDirectiveContext importDirective() throws RecognitionException {
		ImportDirectiveContext _localctx = new ImportDirectiveContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_importDirective);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			match(Import);
			setState(100);
			((ImportDirectiveContext)_localctx).import_target = ref();
			setState(103);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==As) {
				{
				setState(101);
				match(As);
				setState(102);
				((ImportDirectiveContext)_localctx).import_target_rename_as = ref();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RegoRulesContext extends ParserRuleContext {
		public TerminalNode Default() { return getToken(RegoParser.Default, 0); }
		public TerminalNode Name() { return getToken(RegoParser.Name, 0); }
		public TerminalNode EqOper() { return getToken(RegoParser.EqOper, 0); }
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public RuleHeadContext ruleHead() {
			return getRuleContext(RuleHeadContext.class,0);
		}
		public List<RuleBodyContext> ruleBody() {
			return getRuleContexts(RuleBodyContext.class);
		}
		public RuleBodyContext ruleBody(int i) {
			return getRuleContext(RuleBodyContext.class,i);
		}
		public RegoRulesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_regoRules; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRegoRules(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRegoRules(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRegoRules(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RegoRulesContext regoRules() throws RecognitionException {
		RegoRulesContext _localctx = new RegoRulesContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_regoRules);
		try {
			int _alt;
			setState(116);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Default:
				enterOuterAlt(_localctx, 1);
				{
				setState(105);
				match(Default);
				setState(106);
				match(Name);
				setState(107);
				match(EqOper);
				setState(108);
				term();
				}
				break;
			case Name:
				enterOuterAlt(_localctx, 2);
				{
				setState(109);
				ruleHead();
				setState(113);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
				while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(110);
						ruleBody();
						}
						} 
					}
					setState(115);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RuleHeadContext extends ParserRuleContext {
		public TerminalNode Name() { return getToken(RegoParser.Name, 0); }
		public TerminalNode LParan() { return getToken(RegoParser.LParan, 0); }
		public TerminalNode RParan() { return getToken(RegoParser.RParan, 0); }
		public TerminalNode LSBrace() { return getToken(RegoParser.LSBrace, 0); }
		public List<ExprTermContext> exprTerm() {
			return getRuleContexts(ExprTermContext.class);
		}
		public ExprTermContext exprTerm(int i) {
			return getRuleContext(ExprTermContext.class,i);
		}
		public TerminalNode RSBrace() { return getToken(RegoParser.RSBrace, 0); }
		public TerminalNode EqOper() { return getToken(RegoParser.EqOper, 0); }
		public ExprTermListContext exprTermList() {
			return getRuleContext(ExprTermListContext.class,0);
		}
		public RuleHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRuleHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRuleHead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRuleHead(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleHeadContext ruleHead() throws RecognitionException {
		RuleHeadContext _localctx = new RuleHeadContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_ruleHead);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			match(Name);
			setState(124);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				{
				setState(119);
				match(LParan);
				setState(121);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 171803011128L) != 0)) {
					{
					setState(120);
					exprTermList();
					}
				}

				setState(123);
				match(RParan);
				}
				break;
			}
			setState(130);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(126);
				match(LSBrace);
				setState(127);
				exprTerm();
				setState(128);
				match(RSBrace);
				}
				break;
			}
			setState(134);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==EqOper) {
				{
				setState(132);
				match(EqOper);
				setState(133);
				exprTerm();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RuleBodyContext extends ParserRuleContext {
		public NonEmptyBraceEnclosedBodyContext nonEmptyBraceEnclosedBody() {
			return getRuleContext(NonEmptyBraceEnclosedBodyContext.class,0);
		}
		public TerminalNode Else() { return getToken(RegoParser.Else, 0); }
		public TerminalNode EqOper() { return getToken(RegoParser.EqOper, 0); }
		public ExprTermContext exprTerm() {
			return getRuleContext(ExprTermContext.class,0);
		}
		public RuleBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRuleBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRuleBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRuleBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleBodyContext ruleBody() throws RecognitionException {
		RuleBodyContext _localctx = new RuleBodyContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_ruleBody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Else) {
				{
				setState(136);
				match(Else);
				setState(139);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EqOper) {
					{
					setState(137);
					match(EqOper);
					setState(138);
					exprTerm();
					}
				}

				}
			}

			setState(143);
			nonEmptyBraceEnclosedBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RuleExtContext extends ParserRuleContext {
		public RegoElseContext regoElse() {
			return getRuleContext(RegoElseContext.class,0);
		}
		public NonEmptyBraceEnclosedBodyContext nonEmptyBraceEnclosedBody() {
			return getRuleContext(NonEmptyBraceEnclosedBodyContext.class,0);
		}
		public RuleExtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ruleExt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRuleExt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRuleExt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRuleExt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RuleExtContext ruleExt() throws RecognitionException {
		RuleExtContext _localctx = new RuleExtContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_ruleExt);
		try {
			setState(147);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Else:
				enterOuterAlt(_localctx, 1);
				{
				setState(145);
				regoElse();
				}
				break;
			case LCBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(146);
				nonEmptyBraceEnclosedBody();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RegoElseContext extends ParserRuleContext {
		public TerminalNode Else() { return getToken(RegoParser.Else, 0); }
		public TerminalNode EqOper() { return getToken(RegoParser.EqOper, 0); }
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public NonEmptyBraceEnclosedBodyContext nonEmptyBraceEnclosedBody() {
			return getRuleContext(NonEmptyBraceEnclosedBodyContext.class,0);
		}
		public RegoElseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_regoElse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRegoElse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRegoElse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRegoElse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RegoElseContext regoElse() throws RecognitionException {
		RegoElseContext _localctx = new RegoElseContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_regoElse);
		try {
			setState(156);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(149);
				match(Else);
				setState(150);
				match(EqOper);
				setState(151);
				term();
				setState(152);
				nonEmptyBraceEnclosedBody();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(154);
				match(Else);
				setState(155);
				nonEmptyBraceEnclosedBody();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RegoBodyContext extends ParserRuleContext {
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public NonEmptyBraceEnclosedBodyContext nonEmptyBraceEnclosedBody() {
			return getRuleContext(NonEmptyBraceEnclosedBodyContext.class,0);
		}
		public RegoBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_regoBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRegoBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRegoBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRegoBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RegoBodyContext regoBody() throws RecognitionException {
		RegoBodyContext _localctx = new RegoBodyContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_regoBody);
		try {
			setState(160);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(158);
				query();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(159);
				nonEmptyBraceEnclosedBody();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NonEmptyBraceEnclosedBodyContext extends ParserRuleContext {
		public TerminalNode LCBrace() { return getToken(RegoParser.LCBrace, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode RCBrace() { return getToken(RegoParser.RCBrace, 0); }
		public NonEmptyBraceEnclosedBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonEmptyBraceEnclosedBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterNonEmptyBraceEnclosedBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitNonEmptyBraceEnclosedBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitNonEmptyBraceEnclosedBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonEmptyBraceEnclosedBodyContext nonEmptyBraceEnclosedBody() throws RecognitionException {
		NonEmptyBraceEnclosedBodyContext _localctx = new NonEmptyBraceEnclosedBodyContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_nonEmptyBraceEnclosedBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162);
			match(LCBrace);
			setState(163);
			query();
			setState(164);
			match(RCBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class QueryContext extends ParserRuleContext {
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public List<TerminalNode> Semicolon() { return getTokens(RegoParser.Semicolon); }
		public TerminalNode Semicolon(int i) {
			return getToken(RegoParser.Semicolon, i);
		}
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterQuery(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitQuery(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitQuery(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_query);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(166);
			literal();
			setState(173);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(168);
					_errHandler.sync(this);
					_la = _input.LA(1);
					if (_la==Semicolon) {
						{
						setState(167);
						match(Semicolon);
						}
					}

					setState(170);
					literal();
					}
					} 
				}
				setState(175);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,15,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public LiteralExprContext literalExpr() {
			return getRuleContext(LiteralExprContext.class,0);
		}
		public TerminalNode Not() { return getToken(RegoParser.Not, 0); }
		public List<WithKeywordContext> withKeyword() {
			return getRuleContexts(WithKeywordContext.class);
		}
		public WithKeywordContext withKeyword(int i) {
			return getRuleContext(WithKeywordContext.class,i);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(176);
				match(Not);
				}
				break;
			}
			setState(179);
			literalExpr();
			setState(183);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==With) {
				{
				{
				setState(180);
				withKeyword();
				}
				}
				setState(185);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralExprContext extends ParserRuleContext {
		public List<ExprTermContext> exprTerm() {
			return getRuleContexts(ExprTermContext.class);
		}
		public ExprTermContext exprTerm(int i) {
			return getRuleContext(ExprTermContext.class,i);
		}
		public List<TerminalNode> EqOper() { return getTokens(RegoParser.EqOper); }
		public TerminalNode EqOper(int i) {
			return getToken(RegoParser.EqOper, i);
		}
		public LiteralExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literalExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterLiteralExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitLiteralExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitLiteralExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralExprContext literalExpr() throws RecognitionException {
		LiteralExprContext _localctx = new LiteralExprContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_literalExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			exprTerm();
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==EqOper) {
				{
				{
				setState(187);
				match(EqOper);
				setState(188);
				exprTerm();
				}
				}
				setState(193);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WithKeywordContext extends ParserRuleContext {
		public TerminalNode With() { return getToken(RegoParser.With, 0); }
		public List<ExprTermContext> exprTerm() {
			return getRuleContexts(ExprTermContext.class);
		}
		public ExprTermContext exprTerm(int i) {
			return getRuleContext(ExprTermContext.class,i);
		}
		public TerminalNode As() { return getToken(RegoParser.As, 0); }
		public WithKeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_withKeyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterWithKeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitWithKeyword(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitWithKeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WithKeywordContext withKeyword() throws RecognitionException {
		WithKeywordContext _localctx = new WithKeywordContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_withKeyword);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			match(With);
			setState(195);
			exprTerm();
			setState(196);
			match(As);
			setState(197);
			exprTerm();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FunctionCallContext extends ParserRuleContext {
		public RefContext ref() {
			return getRuleContext(RefContext.class,0);
		}
		public TerminalNode LParan() { return getToken(RegoParser.LParan, 0); }
		public TerminalNode RParan() { return getToken(RegoParser.RParan, 0); }
		public ExprTermListContext exprTermList() {
			return getRuleContext(ExprTermListContext.class,0);
		}
		public FunctionCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterFunctionCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitFunctionCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitFunctionCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionCallContext functionCall() throws RecognitionException {
		FunctionCallContext _localctx = new FunctionCallContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_functionCall);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			ref();
			setState(200);
			match(LParan);
			setState(202);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 171803011128L) != 0)) {
				{
				setState(201);
				exprTermList();
				}
			}

			setState(204);
			match(RParan);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprTermPairContext extends ParserRuleContext {
		public List<ExprTermContext> exprTerm() {
			return getRuleContexts(ExprTermContext.class);
		}
		public ExprTermContext exprTerm(int i) {
			return getRuleContext(ExprTermContext.class,i);
		}
		public TerminalNode Colon() { return getToken(RegoParser.Colon, 0); }
		public ExprTermPairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprTermPair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterExprTermPair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitExprTermPair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitExprTermPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprTermPairContext exprTermPair() throws RecognitionException {
		ExprTermPairContext _localctx = new ExprTermPairContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_exprTermPair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			exprTerm();
			setState(207);
			match(Colon);
			setState(208);
			exprTerm();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TermPairContext extends ParserRuleContext {
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public TerminalNode Colon() { return getToken(RegoParser.Colon, 0); }
		public TermPairContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_termPair; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterTermPair(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitTermPair(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitTermPair(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermPairContext termPair() throws RecognitionException {
		TermPairContext _localctx = new TermPairContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_termPair);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			term();
			setState(211);
			match(Colon);
			setState(212);
			term();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprTermPairListContext extends ParserRuleContext {
		public List<ExprTermPairContext> exprTermPair() {
			return getRuleContexts(ExprTermPairContext.class);
		}
		public ExprTermPairContext exprTermPair(int i) {
			return getRuleContext(ExprTermPairContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(RegoParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(RegoParser.Comma, i);
		}
		public ExprTermPairListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprTermPairList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterExprTermPairList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitExprTermPairList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitExprTermPairList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprTermPairListContext exprTermPairList() throws RecognitionException {
		ExprTermPairListContext _localctx = new ExprTermPairListContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_exprTermPairList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			exprTermPair();
			setState(219);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(215);
				match(Comma);
				setState(216);
				exprTermPair();
				}
				}
				setState(221);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprTermContext extends ParserRuleContext {
		public List<RelationExprContext> relationExpr() {
			return getRuleContexts(RelationExprContext.class);
		}
		public RelationExprContext relationExpr(int i) {
			return getRuleContext(RelationExprContext.class,i);
		}
		public List<TerminalNode> RelationOperator() { return getTokens(RegoParser.RelationOperator); }
		public TerminalNode RelationOperator(int i) {
			return getToken(RegoParser.RelationOperator, i);
		}
		public ExprTermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprTerm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterExprTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitExprTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitExprTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprTermContext exprTerm() throws RecognitionException {
		ExprTermContext _localctx = new ExprTermContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_exprTerm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(222);
			relationExpr();
			setState(227);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==RelationOperator) {
				{
				{
				setState(223);
				match(RelationOperator);
				setState(224);
				relationExpr();
				}
				}
				setState(229);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExprTermListContext extends ParserRuleContext {
		public List<ExprTermContext> exprTerm() {
			return getRuleContexts(ExprTermContext.class);
		}
		public ExprTermContext exprTerm(int i) {
			return getRuleContext(ExprTermContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(RegoParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(RegoParser.Comma, i);
		}
		public ExprTermListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exprTermList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterExprTermList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitExprTermList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitExprTermList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprTermListContext exprTermList() throws RecognitionException {
		ExprTermListContext _localctx = new ExprTermListContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_exprTermList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			exprTerm();
			setState(235);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Comma) {
				{
				{
				setState(231);
				match(Comma);
				setState(232);
				exprTerm();
				}
				}
				setState(237);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelationExprContext extends ParserRuleContext {
		public List<BitwiseOrExprContext> bitwiseOrExpr() {
			return getRuleContexts(BitwiseOrExprContext.class);
		}
		public BitwiseOrExprContext bitwiseOrExpr(int i) {
			return getRuleContext(BitwiseOrExprContext.class,i);
		}
		public List<TerminalNode> Mid() { return getTokens(RegoParser.Mid); }
		public TerminalNode Mid(int i) {
			return getToken(RegoParser.Mid, i);
		}
		public RelationExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRelationExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRelationExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRelationExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RelationExprContext relationExpr() throws RecognitionException {
		RelationExprContext _localctx = new RelationExprContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_relationExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(238);
			bitwiseOrExpr();
			setState(243);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Mid) {
				{
				{
				setState(239);
				match(Mid);
				setState(240);
				bitwiseOrExpr();
				}
				}
				setState(245);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BitwiseOrExprContext extends ParserRuleContext {
		public List<BitwiseAndExprContext> bitwiseAndExpr() {
			return getRuleContexts(BitwiseAndExprContext.class);
		}
		public BitwiseAndExprContext bitwiseAndExpr(int i) {
			return getRuleContext(BitwiseAndExprContext.class,i);
		}
		public List<TerminalNode> Ampersand() { return getTokens(RegoParser.Ampersand); }
		public TerminalNode Ampersand(int i) {
			return getToken(RegoParser.Ampersand, i);
		}
		public BitwiseOrExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bitwiseOrExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterBitwiseOrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitBitwiseOrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitBitwiseOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BitwiseOrExprContext bitwiseOrExpr() throws RecognitionException {
		BitwiseOrExprContext _localctx = new BitwiseOrExprContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_bitwiseOrExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			bitwiseAndExpr();
			setState(251);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==Ampersand) {
				{
				{
				setState(247);
				match(Ampersand);
				setState(248);
				bitwiseAndExpr();
				}
				}
				setState(253);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BitwiseAndExprContext extends ParserRuleContext {
		public List<ArithExprContext> arithExpr() {
			return getRuleContexts(ArithExprContext.class);
		}
		public ArithExprContext arithExpr(int i) {
			return getRuleContext(ArithExprContext.class,i);
		}
		public List<TerminalNode> ArithOperator() { return getTokens(RegoParser.ArithOperator); }
		public TerminalNode ArithOperator(int i) {
			return getToken(RegoParser.ArithOperator, i);
		}
		public BitwiseAndExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bitwiseAndExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterBitwiseAndExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitBitwiseAndExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitBitwiseAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BitwiseAndExprContext bitwiseAndExpr() throws RecognitionException {
		BitwiseAndExprContext _localctx = new BitwiseAndExprContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_bitwiseAndExpr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			arithExpr();
			setState(259);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(255);
					match(ArithOperator);
					setState(256);
					arithExpr();
					}
					} 
				}
				setState(261);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArithExprContext extends ParserRuleContext {
		public List<FactorExprContext> factorExpr() {
			return getRuleContexts(FactorExprContext.class);
		}
		public FactorExprContext factorExpr(int i) {
			return getRuleContext(FactorExprContext.class,i);
		}
		public List<TerminalNode> FactorOperator() { return getTokens(RegoParser.FactorOperator); }
		public TerminalNode FactorOperator(int i) {
			return getToken(RegoParser.FactorOperator, i);
		}
		public ArithExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arithExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterArithExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitArithExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitArithExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArithExprContext arithExpr() throws RecognitionException {
		ArithExprContext _localctx = new ArithExprContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_arithExpr);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			factorExpr();
			setState(267);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==FactorOperator) {
				{
				{
				setState(263);
				match(FactorOperator);
				setState(264);
				factorExpr();
				}
				}
				setState(269);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class FactorExprContext extends ParserRuleContext {
		public TerminalNode LParan() { return getToken(RegoParser.LParan, 0); }
		public ExprTermContext exprTerm() {
			return getRuleContext(ExprTermContext.class,0);
		}
		public TerminalNode RParan() { return getToken(RegoParser.RParan, 0); }
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public FactorExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factorExpr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterFactorExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitFactorExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitFactorExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FactorExprContext factorExpr() throws RecognitionException {
		FactorExprContext _localctx = new FactorExprContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_factorExpr);
		try {
			setState(275);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LParan:
				enterOuterAlt(_localctx, 1);
				{
				setState(270);
				match(LParan);
				setState(271);
				exprTerm();
				setState(272);
				match(RParan);
				}
				break;
			case String:
			case Bool:
			case Null:
			case Not:
			case Set:
			case LSBrace:
			case LCBrace:
			case ArithOperator:
			case Name:
			case UnsignedNumber:
				enterOuterAlt(_localctx, 2);
				{
				setState(274);
				term();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TermContext extends ParserRuleContext {
		public ArrayComprehensionContext arrayComprehension() {
			return getRuleContext(ArrayComprehensionContext.class,0);
		}
		public ObjectComprehensionContext objectComprehension() {
			return getRuleContext(ObjectComprehensionContext.class,0);
		}
		public SetComprehensionContext setComprehension() {
			return getRuleContext(SetComprehensionContext.class,0);
		}
		public Object_Context object_() {
			return getRuleContext(Object_Context.class,0);
		}
		public Array_Context array_() {
			return getRuleContext(Array_Context.class,0);
		}
		public Set_Context set_() {
			return getRuleContext(Set_Context.class,0);
		}
		public ScalarContext scalar() {
			return getRuleContext(ScalarContext.class,0);
		}
		public TerminalNode ArithOperator() { return getToken(RegoParser.ArithOperator, 0); }
		public FunctionCallContext functionCall() {
			return getRuleContext(FunctionCallContext.class,0);
		}
		public RefContext ref() {
			return getRuleContext(RefContext.class,0);
		}
		public TerminalNode Not() { return getToken(RegoParser.Not, 0); }
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterTerm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitTerm(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitTerm(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_term);
		int _la;
		try {
			setState(292);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,30,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(277);
				arrayComprehension();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(278);
				objectComprehension();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(279);
				setComprehension();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(280);
				object_();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(281);
				array_();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(282);
				set_();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(284);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==ArithOperator) {
					{
					setState(283);
					match(ArithOperator);
					}
				}

				setState(286);
				scalar();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(287);
				functionCall();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(289);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Not) {
					{
					setState(288);
					match(Not);
					}
				}

				setState(291);
				ref();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ArrayComprehensionContext extends ParserRuleContext {
		public TerminalNode LSBrace() { return getToken(RegoParser.LSBrace, 0); }
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TerminalNode Mid() { return getToken(RegoParser.Mid, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode RSBrace() { return getToken(RegoParser.RSBrace, 0); }
		public ArrayComprehensionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayComprehension; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterArrayComprehension(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitArrayComprehension(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitArrayComprehension(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayComprehensionContext arrayComprehension() throws RecognitionException {
		ArrayComprehensionContext _localctx = new ArrayComprehensionContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_arrayComprehension);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(294);
			match(LSBrace);
			setState(295);
			term();
			setState(296);
			match(Mid);
			setState(297);
			query();
			setState(298);
			match(RSBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class SetComprehensionContext extends ParserRuleContext {
		public TerminalNode LCBrace() { return getToken(RegoParser.LCBrace, 0); }
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public TerminalNode Mid() { return getToken(RegoParser.Mid, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode RCBrace() { return getToken(RegoParser.RCBrace, 0); }
		public SetComprehensionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_setComprehension; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterSetComprehension(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitSetComprehension(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitSetComprehension(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SetComprehensionContext setComprehension() throws RecognitionException {
		SetComprehensionContext _localctx = new SetComprehensionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_setComprehension);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			match(LCBrace);
			setState(301);
			term();
			setState(302);
			match(Mid);
			setState(303);
			query();
			setState(304);
			match(RCBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ObjectComprehensionContext extends ParserRuleContext {
		public TerminalNode LCBrace() { return getToken(RegoParser.LCBrace, 0); }
		public TermPairContext termPair() {
			return getRuleContext(TermPairContext.class,0);
		}
		public TerminalNode Mid() { return getToken(RegoParser.Mid, 0); }
		public QueryContext query() {
			return getRuleContext(QueryContext.class,0);
		}
		public TerminalNode RCBrace() { return getToken(RegoParser.RCBrace, 0); }
		public ObjectComprehensionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectComprehension; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterObjectComprehension(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitObjectComprehension(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitObjectComprehension(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectComprehensionContext objectComprehension() throws RecognitionException {
		ObjectComprehensionContext _localctx = new ObjectComprehensionContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_objectComprehension);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(306);
			match(LCBrace);
			setState(307);
			termPair();
			setState(308);
			match(Mid);
			setState(309);
			query();
			setState(310);
			match(RCBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Object_Context extends ParserRuleContext {
		public TerminalNode LCBrace() { return getToken(RegoParser.LCBrace, 0); }
		public TerminalNode RCBrace() { return getToken(RegoParser.RCBrace, 0); }
		public List<ObjectItemContext> objectItem() {
			return getRuleContexts(ObjectItemContext.class);
		}
		public ObjectItemContext objectItem(int i) {
			return getRuleContext(ObjectItemContext.class,i);
		}
		public List<TerminalNode> Comma() { return getTokens(RegoParser.Comma); }
		public TerminalNode Comma(int i) {
			return getToken(RegoParser.Comma, i);
		}
		public Object_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_object_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterObject_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitObject_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitObject_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Object_Context object_() throws RecognitionException {
		Object_Context _localctx = new Object_Context(_ctx, getState());
		enterRule(_localctx, 60, RULE_object_);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(312);
			match(LCBrace);
			setState(324);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 171798691896L) != 0)) {
				{
				setState(313);
				objectItem();
				setState(318);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
				while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(314);
						match(Comma);
						setState(315);
						objectItem();
						}
						} 
					}
					setState(320);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,31,_ctx);
				}
				setState(322);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==Comma) {
					{
					setState(321);
					match(Comma);
					}
				}

				}
			}

			setState(326);
			match(RCBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ObjectItemContext extends ParserRuleContext {
		public TerminalNode Colon() { return getToken(RegoParser.Colon, 0); }
		public TermContext term() {
			return getRuleContext(TermContext.class,0);
		}
		public ScalarContext scalar() {
			return getRuleContext(ScalarContext.class,0);
		}
		public RefContext ref() {
			return getRuleContext(RefContext.class,0);
		}
		public ObjectItemContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectItem; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterObjectItem(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitObjectItem(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitObjectItem(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectItemContext objectItem() throws RecognitionException {
		ObjectItemContext _localctx = new ObjectItemContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_objectItem);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(330);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case String:
			case Bool:
			case Null:
			case UnsignedNumber:
				{
				setState(328);
				scalar();
				}
				break;
			case Name:
				{
				setState(329);
				ref();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(332);
			match(Colon);
			setState(333);
			term();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Array_Context extends ParserRuleContext {
		public TerminalNode LSBrace() { return getToken(RegoParser.LSBrace, 0); }
		public TerminalNode RSBrace() { return getToken(RegoParser.RSBrace, 0); }
		public ExprTermListContext exprTermList() {
			return getRuleContext(ExprTermListContext.class,0);
		}
		public Array_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterArray_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitArray_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitArray_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Array_Context array_() throws RecognitionException {
		Array_Context _localctx = new Array_Context(_ctx, getState());
		enterRule(_localctx, 64, RULE_array_);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			match(LSBrace);
			setState(337);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 171803011128L) != 0)) {
				{
				setState(336);
				exprTermList();
				}
			}

			setState(339);
			match(RSBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Set_Context extends ParserRuleContext {
		public EmptySetContext emptySet() {
			return getRuleContext(EmptySetContext.class,0);
		}
		public NonEmptySetContext nonEmptySet() {
			return getRuleContext(NonEmptySetContext.class,0);
		}
		public Set_Context(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_set_; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterSet_(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitSet_(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitSet_(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Set_Context set_() throws RecognitionException {
		Set_Context _localctx = new Set_Context(_ctx, getState());
		enterRule(_localctx, 66, RULE_set_);
		try {
			setState(343);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Set:
				enterOuterAlt(_localctx, 1);
				{
				setState(341);
				emptySet();
				}
				break;
			case LCBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(342);
				nonEmptySet();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EmptySetContext extends ParserRuleContext {
		public TerminalNode Set() { return getToken(RegoParser.Set, 0); }
		public TerminalNode RParan() { return getToken(RegoParser.RParan, 0); }
		public EmptySetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_emptySet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterEmptySet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitEmptySet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitEmptySet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EmptySetContext emptySet() throws RecognitionException {
		EmptySetContext _localctx = new EmptySetContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_emptySet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(345);
			match(Set);
			setState(346);
			match(RParan);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class NonEmptySetContext extends ParserRuleContext {
		public TerminalNode LCBrace() { return getToken(RegoParser.LCBrace, 0); }
		public ExprTermListContext exprTermList() {
			return getRuleContext(ExprTermListContext.class,0);
		}
		public TerminalNode RCBrace() { return getToken(RegoParser.RCBrace, 0); }
		public NonEmptySetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonEmptySet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterNonEmptySet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitNonEmptySet(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitNonEmptySet(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NonEmptySetContext nonEmptySet() throws RecognitionException {
		NonEmptySetContext _localctx = new NonEmptySetContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_nonEmptySet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(348);
			match(LCBrace);
			setState(349);
			exprTermList();
			setState(350);
			match(RCBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RefContext extends ParserRuleContext {
		public TerminalNode Name() { return getToken(RegoParser.Name, 0); }
		public List<RefOperandContext> refOperand() {
			return getRuleContexts(RefOperandContext.class);
		}
		public RefOperandContext refOperand(int i) {
			return getRuleContext(RefOperandContext.class,i);
		}
		public RefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ref; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefContext ref() throws RecognitionException {
		RefContext _localctx = new RefContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_ref);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(352);
			match(Name);
			setState(356);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
			while ( _alt!=2 && _alt!= ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(353);
					refOperand();
					}
					} 
				}
				setState(358);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,37,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RefOperandContext extends ParserRuleContext {
		public RefOperandDotContext refOperandDot() {
			return getRuleContext(RefOperandDotContext.class,0);
		}
		public RefOperandCanonicalContext refOperandCanonical() {
			return getRuleContext(RefOperandCanonicalContext.class,0);
		}
		public RefOperandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refOperand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRefOperand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRefOperand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRefOperand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefOperandContext refOperand() throws RecognitionException {
		RefOperandContext _localctx = new RefOperandContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_refOperand);
		try {
			setState(361);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Dot:
				enterOuterAlt(_localctx, 1);
				{
				setState(359);
				refOperandDot();
				}
				break;
			case LSBrace:
				enterOuterAlt(_localctx, 2);
				{
				setState(360);
				refOperandCanonical();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RefOperandDotContext extends ParserRuleContext {
		public TerminalNode Dot() { return getToken(RegoParser.Dot, 0); }
		public TerminalNode Name() { return getToken(RegoParser.Name, 0); }
		public RefOperandDotContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refOperandDot; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRefOperandDot(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRefOperandDot(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRefOperandDot(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefOperandDotContext refOperandDot() throws RecognitionException {
		RefOperandDotContext _localctx = new RefOperandDotContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_refOperandDot);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(363);
			match(Dot);
			setState(364);
			match(Name);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RefOperandCanonicalContext extends ParserRuleContext {
		public TerminalNode LSBrace() { return getToken(RegoParser.LSBrace, 0); }
		public ExprTermContext exprTerm() {
			return getRuleContext(ExprTermContext.class,0);
		}
		public TerminalNode RSBrace() { return getToken(RegoParser.RSBrace, 0); }
		public RefOperandCanonicalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refOperandCanonical; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterRefOperandCanonical(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitRefOperandCanonical(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitRefOperandCanonical(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefOperandCanonicalContext refOperandCanonical() throws RecognitionException {
		RefOperandCanonicalContext _localctx = new RefOperandCanonicalContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_refOperandCanonical);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(366);
			match(LSBrace);
			setState(367);
			exprTerm();
			setState(368);
			match(RSBrace);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ScalarContext extends ParserRuleContext {
		public TerminalNode UnsignedNumber() { return getToken(RegoParser.UnsignedNumber, 0); }
		public TerminalNode String() { return getToken(RegoParser.String, 0); }
		public TerminalNode Bool() { return getToken(RegoParser.Bool, 0); }
		public TerminalNode Null() { return getToken(RegoParser.Null, 0); }
		public ScalarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scalar; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).enterScalar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof RegoParserListener) ((RegoParserListener)listener).exitScalar(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof RegoParserVisitor) return ((RegoParserVisitor<? extends T>)visitor).visitScalar(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScalarContext scalar() throws RecognitionException {
		ScalarContext _localctx = new ScalarContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_scalar);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(370);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 137438953528L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001%\u0175\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0002$\u0007$\u0002%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002"+
		"(\u0007(\u0001\u0000\u0001\u0000\u0005\u0000U\b\u0000\n\u0000\f\u0000"+
		"X\t\u0000\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0003\u0001_\b\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u0003h\b\u0003\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0005\u0004"+
		"p\b\u0004\n\u0004\f\u0004s\t\u0004\u0003\u0004u\b\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0003\u0005z\b\u0005\u0001\u0005\u0003\u0005}\b\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005\u0083\b\u0005"+
		"\u0001\u0005\u0001\u0005\u0003\u0005\u0087\b\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0003\u0006\u008c\b\u0006\u0003\u0006\u008e\b\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0003\u0007\u0094\b\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b\u009d\b\b\u0001"+
		"\t\u0001\t\u0003\t\u00a1\b\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b"+
		"\u0001\u000b\u0003\u000b\u00a9\b\u000b\u0001\u000b\u0005\u000b\u00ac\b"+
		"\u000b\n\u000b\f\u000b\u00af\t\u000b\u0001\f\u0003\f\u00b2\b\f\u0001\f"+
		"\u0001\f\u0005\f\u00b6\b\f\n\f\f\f\u00b9\t\f\u0001\r\u0001\r\u0001\r\u0005"+
		"\r\u00be\b\r\n\r\f\r\u00c1\t\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0003\u000f\u00cb"+
		"\b\u000f\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001"+
		"\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0011\u0001\u0012\u0001"+
		"\u0012\u0001\u0012\u0005\u0012\u00da\b\u0012\n\u0012\f\u0012\u00dd\t\u0012"+
		"\u0001\u0013\u0001\u0013\u0001\u0013\u0005\u0013\u00e2\b\u0013\n\u0013"+
		"\f\u0013\u00e5\t\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0005\u0014"+
		"\u00ea\b\u0014\n\u0014\f\u0014\u00ed\t\u0014\u0001\u0015\u0001\u0015\u0001"+
		"\u0015\u0005\u0015\u00f2\b\u0015\n\u0015\f\u0015\u00f5\t\u0015\u0001\u0016"+
		"\u0001\u0016\u0001\u0016\u0005\u0016\u00fa\b\u0016\n\u0016\f\u0016\u00fd"+
		"\t\u0016\u0001\u0017\u0001\u0017\u0001\u0017\u0005\u0017\u0102\b\u0017"+
		"\n\u0017\f\u0017\u0105\t\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0005"+
		"\u0018\u010a\b\u0018\n\u0018\f\u0018\u010d\t\u0018\u0001\u0019\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0001\u0019\u0003\u0019\u0114\b\u0019\u0001\u001a"+
		"\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a"+
		"\u0003\u001a\u011d\b\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0003\u001a"+
		"\u0122\b\u001a\u0001\u001a\u0003\u001a\u0125\b\u001a\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001c\u0001"+
		"\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001c\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001d\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0001\u001e\u0005\u001e\u013d\b\u001e\n\u001e\f\u001e"+
		"\u0140\t\u001e\u0001\u001e\u0003\u001e\u0143\b\u001e\u0003\u001e\u0145"+
		"\b\u001e\u0001\u001e\u0001\u001e\u0001\u001f\u0001\u001f\u0003\u001f\u014b"+
		"\b\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001 \u0001 \u0003 \u0152"+
		"\b \u0001 \u0001 \u0001!\u0001!\u0003!\u0158\b!\u0001\"\u0001\"\u0001"+
		"\"\u0001#\u0001#\u0001#\u0001#\u0001$\u0001$\u0005$\u0163\b$\n$\f$\u0166"+
		"\t$\u0001%\u0001%\u0003%\u016a\b%\u0001&\u0001&\u0001&\u0001\'\u0001\'"+
		"\u0001\'\u0001\'\u0001(\u0001(\u0001(\u0000\u0000)\u0000\u0002\u0004\u0006"+
		"\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,."+
		"02468:<>@BDFHJLNP\u0000\u0001\u0002\u0000\u0003\u0005%%\u017a\u0000R\u0001"+
		"\u0000\u0000\u0000\u0002^\u0001\u0000\u0000\u0000\u0004`\u0001\u0000\u0000"+
		"\u0000\u0006c\u0001\u0000\u0000\u0000\bt\u0001\u0000\u0000\u0000\nv\u0001"+
		"\u0000\u0000\u0000\f\u008d\u0001\u0000\u0000\u0000\u000e\u0093\u0001\u0000"+
		"\u0000\u0000\u0010\u009c\u0001\u0000\u0000\u0000\u0012\u00a0\u0001\u0000"+
		"\u0000\u0000\u0014\u00a2\u0001\u0000\u0000\u0000\u0016\u00a6\u0001\u0000"+
		"\u0000\u0000\u0018\u00b1\u0001\u0000\u0000\u0000\u001a\u00ba\u0001\u0000"+
		"\u0000\u0000\u001c\u00c2\u0001\u0000\u0000\u0000\u001e\u00c7\u0001\u0000"+
		"\u0000\u0000 \u00ce\u0001\u0000\u0000\u0000\"\u00d2\u0001\u0000\u0000"+
		"\u0000$\u00d6\u0001\u0000\u0000\u0000&\u00de\u0001\u0000\u0000\u0000("+
		"\u00e6\u0001\u0000\u0000\u0000*\u00ee\u0001\u0000\u0000\u0000,\u00f6\u0001"+
		"\u0000\u0000\u0000.\u00fe\u0001\u0000\u0000\u00000\u0106\u0001\u0000\u0000"+
		"\u00002\u0113\u0001\u0000\u0000\u00004\u0124\u0001\u0000\u0000\u00006"+
		"\u0126\u0001\u0000\u0000\u00008\u012c\u0001\u0000\u0000\u0000:\u0132\u0001"+
		"\u0000\u0000\u0000<\u0138\u0001\u0000\u0000\u0000>\u014a\u0001\u0000\u0000"+
		"\u0000@\u014f\u0001\u0000\u0000\u0000B\u0157\u0001\u0000\u0000\u0000D"+
		"\u0159\u0001\u0000\u0000\u0000F\u015c\u0001\u0000\u0000\u0000H\u0160\u0001"+
		"\u0000\u0000\u0000J\u0169\u0001\u0000\u0000\u0000L\u016b\u0001\u0000\u0000"+
		"\u0000N\u016e\u0001\u0000\u0000\u0000P\u0172\u0001\u0000\u0000\u0000R"+
		"V\u0003\u0004\u0002\u0000SU\u0003\u0002\u0001\u0000TS\u0001\u0000\u0000"+
		"\u0000UX\u0001\u0000\u0000\u0000VT\u0001\u0000\u0000\u0000VW\u0001\u0000"+
		"\u0000\u0000WY\u0001\u0000\u0000\u0000XV\u0001\u0000\u0000\u0000YZ\u0005"+
		"\u0000\u0000\u0001Z\u0001\u0001\u0000\u0000\u0000[_\u0003\u0006\u0003"+
		"\u0000\\_\u0003\b\u0004\u0000]_\u0003\u0012\t\u0000^[\u0001\u0000\u0000"+
		"\u0000^\\\u0001\u0000\u0000\u0000^]\u0001\u0000\u0000\u0000_\u0003\u0001"+
		"\u0000\u0000\u0000`a\u0005\n\u0000\u0000ab\u0003H$\u0000b\u0005\u0001"+
		"\u0000\u0000\u0000cd\u0005\t\u0000\u0000dg\u0003H$\u0000ef\u0005\u0006"+
		"\u0000\u0000fh\u0003H$\u0000ge\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000"+
		"\u0000h\u0007\u0001\u0000\u0000\u0000ij\u0005\u0007\u0000\u0000jk\u0005"+
		"#\u0000\u0000kl\u0005\u0018\u0000\u0000lu\u00034\u001a\u0000mq\u0003\n"+
		"\u0005\u0000np\u0003\f\u0006\u0000on\u0001\u0000\u0000\u0000ps\u0001\u0000"+
		"\u0000\u0000qo\u0001\u0000\u0000\u0000qr\u0001\u0000\u0000\u0000ru\u0001"+
		"\u0000\u0000\u0000sq\u0001\u0000\u0000\u0000ti\u0001\u0000\u0000\u0000"+
		"tm\u0001\u0000\u0000\u0000u\t\u0001\u0000\u0000\u0000v|\u0005#\u0000\u0000"+
		"wy\u0005\u0010\u0000\u0000xz\u0003(\u0014\u0000yx\u0001\u0000\u0000\u0000"+
		"yz\u0001\u0000\u0000\u0000z{\u0001\u0000\u0000\u0000{}\u0005\u0013\u0000"+
		"\u0000|w\u0001\u0000\u0000\u0000|}\u0001\u0000\u0000\u0000}\u0082\u0001"+
		"\u0000\u0000\u0000~\u007f\u0005\u000e\u0000\u0000\u007f\u0080\u0003&\u0013"+
		"\u0000\u0080\u0081\u0005\u0011\u0000\u0000\u0081\u0083\u0001\u0000\u0000"+
		"\u0000\u0082~\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000\u0000"+
		"\u0083\u0086\u0001\u0000\u0000\u0000\u0084\u0085\u0005\u0018\u0000\u0000"+
		"\u0085\u0087\u0003&\u0013\u0000\u0086\u0084\u0001\u0000\u0000\u0000\u0086"+
		"\u0087\u0001\u0000\u0000\u0000\u0087\u000b\u0001\u0000\u0000\u0000\u0088"+
		"\u008b\u0005\b\u0000\u0000\u0089\u008a\u0005\u0018\u0000\u0000\u008a\u008c"+
		"\u0003&\u0013\u0000\u008b\u0089\u0001\u0000\u0000\u0000\u008b\u008c\u0001"+
		"\u0000\u0000\u0000\u008c\u008e\u0001\u0000\u0000\u0000\u008d\u0088\u0001"+
		"\u0000\u0000\u0000\u008d\u008e\u0001\u0000\u0000\u0000\u008e\u008f\u0001"+
		"\u0000\u0000\u0000\u008f\u0090\u0003\u0014\n\u0000\u0090\r\u0001\u0000"+
		"\u0000\u0000\u0091\u0094\u0003\u0010\b\u0000\u0092\u0094\u0003\u0014\n"+
		"\u0000\u0093\u0091\u0001\u0000\u0000\u0000\u0093\u0092\u0001\u0000\u0000"+
		"\u0000\u0094\u000f\u0001\u0000\u0000\u0000\u0095\u0096\u0005\b\u0000\u0000"+
		"\u0096\u0097\u0005\u0018\u0000\u0000\u0097\u0098\u00034\u001a\u0000\u0098"+
		"\u0099\u0003\u0014\n\u0000\u0099\u009d\u0001\u0000\u0000\u0000\u009a\u009b"+
		"\u0005\b\u0000\u0000\u009b\u009d\u0003\u0014\n\u0000\u009c\u0095\u0001"+
		"\u0000\u0000\u0000\u009c\u009a\u0001\u0000\u0000\u0000\u009d\u0011\u0001"+
		"\u0000\u0000\u0000\u009e\u00a1\u0003\u0016\u000b\u0000\u009f\u00a1\u0003"+
		"\u0014\n\u0000\u00a0\u009e\u0001\u0000\u0000\u0000\u00a0\u009f\u0001\u0000"+
		"\u0000\u0000\u00a1\u0013\u0001\u0000\u0000\u0000\u00a2\u00a3\u0005\u000f"+
		"\u0000\u0000\u00a3\u00a4\u0003\u0016\u000b\u0000\u00a4\u00a5\u0005\u0012"+
		"\u0000\u0000\u00a5\u0015\u0001\u0000\u0000\u0000\u00a6\u00ad\u0003\u0018"+
		"\f\u0000\u00a7\u00a9\u0005\u001a\u0000\u0000\u00a8\u00a7\u0001\u0000\u0000"+
		"\u0000\u00a8\u00a9\u0001\u0000\u0000\u0000\u00a9\u00aa\u0001\u0000\u0000"+
		"\u0000\u00aa\u00ac\u0003\u0018\f\u0000\u00ab\u00a8\u0001\u0000\u0000\u0000"+
		"\u00ac\u00af\u0001\u0000\u0000\u0000\u00ad\u00ab\u0001\u0000\u0000\u0000"+
		"\u00ad\u00ae\u0001\u0000\u0000\u0000\u00ae\u0017\u0001\u0000\u0000\u0000"+
		"\u00af\u00ad\u0001\u0000\u0000\u0000\u00b0\u00b2\u0005\u000b\u0000\u0000"+
		"\u00b1\u00b0\u0001\u0000\u0000\u0000\u00b1\u00b2\u0001\u0000\u0000\u0000"+
		"\u00b2\u00b3\u0001\u0000\u0000\u0000\u00b3\u00b7\u0003\u001a\r\u0000\u00b4"+
		"\u00b6\u0003\u001c\u000e\u0000\u00b5\u00b4\u0001\u0000\u0000\u0000\u00b6"+
		"\u00b9\u0001\u0000\u0000\u0000\u00b7\u00b5\u0001\u0000\u0000\u0000\u00b7"+
		"\u00b8\u0001\u0000\u0000\u0000\u00b8\u0019\u0001\u0000\u0000\u0000\u00b9"+
		"\u00b7\u0001\u0000\u0000\u0000\u00ba\u00bf\u0003&\u0013\u0000\u00bb\u00bc"+
		"\u0005\u0018\u0000\u0000\u00bc\u00be\u0003&\u0013\u0000\u00bd\u00bb\u0001"+
		"\u0000\u0000\u0000\u00be\u00c1\u0001\u0000\u0000\u0000\u00bf\u00bd\u0001"+
		"\u0000\u0000\u0000\u00bf\u00c0\u0001\u0000\u0000\u0000\u00c0\u001b\u0001"+
		"\u0000\u0000\u0000\u00c1\u00bf\u0001\u0000\u0000\u0000\u00c2\u00c3\u0005"+
		"\f\u0000\u0000\u00c3\u00c4\u0003&\u0013\u0000\u00c4\u00c5\u0005\u0006"+
		"\u0000\u0000\u00c5\u00c6\u0003&\u0013\u0000\u00c6\u001d\u0001\u0000\u0000"+
		"\u0000\u00c7\u00c8\u0003H$\u0000\u00c8\u00ca\u0005\u0010\u0000\u0000\u00c9"+
		"\u00cb\u0003(\u0014\u0000\u00ca\u00c9\u0001\u0000\u0000\u0000\u00ca\u00cb"+
		"\u0001\u0000\u0000\u0000\u00cb\u00cc\u0001\u0000\u0000\u0000\u00cc\u00cd"+
		"\u0005\u0013\u0000\u0000\u00cd\u001f\u0001\u0000\u0000\u0000\u00ce\u00cf"+
		"\u0003&\u0013\u0000\u00cf\u00d0\u0005\u001b\u0000\u0000\u00d0\u00d1\u0003"+
		"&\u0013\u0000\u00d1!\u0001\u0000\u0000\u0000\u00d2\u00d3\u00034\u001a"+
		"\u0000\u00d3\u00d4\u0005\u001b\u0000\u0000\u00d4\u00d5\u00034\u001a\u0000"+
		"\u00d5#\u0001\u0000\u0000\u0000\u00d6\u00db\u0003 \u0010\u0000\u00d7\u00d8"+
		"\u0005\u0019\u0000\u0000\u00d8\u00da\u0003 \u0010\u0000\u00d9\u00d7\u0001"+
		"\u0000\u0000\u0000\u00da\u00dd\u0001\u0000\u0000\u0000\u00db\u00d9\u0001"+
		"\u0000\u0000\u0000\u00db\u00dc\u0001\u0000\u0000\u0000\u00dc%\u0001\u0000"+
		"\u0000\u0000\u00dd\u00db\u0001\u0000\u0000\u0000\u00de\u00e3\u0003*\u0015"+
		"\u0000\u00df\u00e0\u0005\u0017\u0000\u0000\u00e0\u00e2\u0003*\u0015\u0000"+
		"\u00e1\u00df\u0001\u0000\u0000\u0000\u00e2\u00e5\u0001\u0000\u0000\u0000"+
		"\u00e3\u00e1\u0001\u0000\u0000\u0000\u00e3\u00e4\u0001\u0000\u0000\u0000"+
		"\u00e4\'\u0001\u0000\u0000\u0000\u00e5\u00e3\u0001\u0000\u0000\u0000\u00e6"+
		"\u00eb\u0003&\u0013\u0000\u00e7\u00e8\u0005\u0019\u0000\u0000\u00e8\u00ea"+
		"\u0003&\u0013\u0000\u00e9\u00e7\u0001\u0000\u0000\u0000\u00ea\u00ed\u0001"+
		"\u0000\u0000\u0000\u00eb\u00e9\u0001\u0000\u0000\u0000\u00eb\u00ec\u0001"+
		"\u0000\u0000\u0000\u00ec)\u0001\u0000\u0000\u0000\u00ed\u00eb\u0001\u0000"+
		"\u0000\u0000\u00ee\u00f3\u0003,\u0016\u0000\u00ef\u00f0\u0005\u0014\u0000"+
		"\u0000\u00f0\u00f2\u0003,\u0016\u0000\u00f1\u00ef\u0001\u0000\u0000\u0000"+
		"\u00f2\u00f5\u0001\u0000\u0000\u0000\u00f3\u00f1\u0001\u0000\u0000\u0000"+
		"\u00f3\u00f4\u0001\u0000\u0000\u0000\u00f4+\u0001\u0000\u0000\u0000\u00f5"+
		"\u00f3\u0001\u0000\u0000\u0000\u00f6\u00fb\u0003.\u0017\u0000\u00f7\u00f8"+
		"\u0005\u001c\u0000\u0000\u00f8\u00fa\u0003.\u0017\u0000\u00f9\u00f7\u0001"+
		"\u0000\u0000\u0000\u00fa\u00fd\u0001\u0000\u0000\u0000\u00fb\u00f9\u0001"+
		"\u0000\u0000\u0000\u00fb\u00fc\u0001\u0000\u0000\u0000\u00fc-\u0001\u0000"+
		"\u0000\u0000\u00fd\u00fb\u0001\u0000\u0000\u0000\u00fe\u0103\u00030\u0018"+
		"\u0000\u00ff\u0100\u0005\u0016\u0000\u0000\u0100\u0102\u00030\u0018\u0000"+
		"\u0101\u00ff\u0001\u0000\u0000\u0000\u0102\u0105\u0001\u0000\u0000\u0000"+
		"\u0103\u0101\u0001\u0000\u0000\u0000\u0103\u0104\u0001\u0000\u0000\u0000"+
		"\u0104/\u0001\u0000\u0000\u0000\u0105\u0103\u0001\u0000\u0000\u0000\u0106"+
		"\u010b\u00032\u0019\u0000\u0107\u0108\u0005\u0015\u0000\u0000\u0108\u010a"+
		"\u00032\u0019\u0000\u0109\u0107\u0001\u0000\u0000\u0000\u010a\u010d\u0001"+
		"\u0000\u0000\u0000\u010b\u0109\u0001\u0000\u0000\u0000\u010b\u010c\u0001"+
		"\u0000\u0000\u0000\u010c1\u0001\u0000\u0000\u0000\u010d\u010b\u0001\u0000"+
		"\u0000\u0000\u010e\u010f\u0005\u0010\u0000\u0000\u010f\u0110\u0003&\u0013"+
		"\u0000\u0110\u0111\u0005\u0013\u0000\u0000\u0111\u0114\u0001\u0000\u0000"+
		"\u0000\u0112\u0114\u00034\u001a\u0000\u0113\u010e\u0001\u0000\u0000\u0000"+
		"\u0113\u0112\u0001\u0000\u0000\u0000\u01143\u0001\u0000\u0000\u0000\u0115"+
		"\u0125\u00036\u001b\u0000\u0116\u0125\u0003:\u001d\u0000\u0117\u0125\u0003"+
		"8\u001c\u0000\u0118\u0125\u0003<\u001e\u0000\u0119\u0125\u0003@ \u0000"+
		"\u011a\u0125\u0003B!\u0000\u011b\u011d\u0005\u0016\u0000\u0000\u011c\u011b"+
		"\u0001\u0000\u0000\u0000\u011c\u011d\u0001\u0000\u0000\u0000\u011d\u011e"+
		"\u0001\u0000\u0000\u0000\u011e\u0125\u0003P(\u0000\u011f\u0125\u0003\u001e"+
		"\u000f\u0000\u0120\u0122\u0005\u000b\u0000\u0000\u0121\u0120\u0001\u0000"+
		"\u0000\u0000\u0121\u0122\u0001\u0000\u0000\u0000\u0122\u0123\u0001\u0000"+
		"\u0000\u0000\u0123\u0125\u0003H$\u0000\u0124\u0115\u0001\u0000\u0000\u0000"+
		"\u0124\u0116\u0001\u0000\u0000\u0000\u0124\u0117\u0001\u0000\u0000\u0000"+
		"\u0124\u0118\u0001\u0000\u0000\u0000\u0124\u0119\u0001\u0000\u0000\u0000"+
		"\u0124\u011a\u0001\u0000\u0000\u0000\u0124\u011c\u0001\u0000\u0000\u0000"+
		"\u0124\u011f\u0001\u0000\u0000\u0000\u0124\u0121\u0001\u0000\u0000\u0000"+
		"\u01255\u0001\u0000\u0000\u0000\u0126\u0127\u0005\u000e\u0000\u0000\u0127"+
		"\u0128\u00034\u001a\u0000\u0128\u0129\u0005\u0014\u0000\u0000\u0129\u012a"+
		"\u0003\u0016\u000b\u0000\u012a\u012b\u0005\u0011\u0000\u0000\u012b7\u0001"+
		"\u0000\u0000\u0000\u012c\u012d\u0005\u000f\u0000\u0000\u012d\u012e\u0003"+
		"4\u001a\u0000\u012e\u012f\u0005\u0014\u0000\u0000\u012f\u0130\u0003\u0016"+
		"\u000b\u0000\u0130\u0131\u0005\u0012\u0000\u0000\u01319\u0001\u0000\u0000"+
		"\u0000\u0132\u0133\u0005\u000f\u0000\u0000\u0133\u0134\u0003\"\u0011\u0000"+
		"\u0134\u0135\u0005\u0014\u0000\u0000\u0135\u0136\u0003\u0016\u000b\u0000"+
		"\u0136\u0137\u0005\u0012\u0000\u0000\u0137;\u0001\u0000\u0000\u0000\u0138"+
		"\u0144\u0005\u000f\u0000\u0000\u0139\u013e\u0003>\u001f\u0000\u013a\u013b"+
		"\u0005\u0019\u0000\u0000\u013b\u013d\u0003>\u001f\u0000\u013c\u013a\u0001"+
		"\u0000\u0000\u0000\u013d\u0140\u0001\u0000\u0000\u0000\u013e\u013c\u0001"+
		"\u0000\u0000\u0000\u013e\u013f\u0001\u0000\u0000\u0000\u013f\u0142\u0001"+
		"\u0000\u0000\u0000\u0140\u013e\u0001\u0000\u0000\u0000\u0141\u0143\u0005"+
		"\u0019\u0000\u0000\u0142\u0141\u0001\u0000\u0000\u0000\u0142\u0143\u0001"+
		"\u0000\u0000\u0000\u0143\u0145\u0001\u0000\u0000\u0000\u0144\u0139\u0001"+
		"\u0000\u0000\u0000\u0144\u0145\u0001\u0000\u0000\u0000\u0145\u0146\u0001"+
		"\u0000\u0000\u0000\u0146\u0147\u0005\u0012\u0000\u0000\u0147=\u0001\u0000"+
		"\u0000\u0000\u0148\u014b\u0003P(\u0000\u0149\u014b\u0003H$\u0000\u014a"+
		"\u0148\u0001\u0000\u0000\u0000\u014a\u0149\u0001\u0000\u0000\u0000\u014b"+
		"\u014c\u0001\u0000\u0000\u0000\u014c\u014d\u0005\u001b\u0000\u0000\u014d"+
		"\u014e\u00034\u001a\u0000\u014e?\u0001\u0000\u0000\u0000\u014f\u0151\u0005"+
		"\u000e\u0000\u0000\u0150\u0152\u0003(\u0014\u0000\u0151\u0150\u0001\u0000"+
		"\u0000\u0000\u0151\u0152\u0001\u0000\u0000\u0000\u0152\u0153\u0001\u0000"+
		"\u0000\u0000\u0153\u0154\u0005\u0011\u0000\u0000\u0154A\u0001\u0000\u0000"+
		"\u0000\u0155\u0158\u0003D\"\u0000\u0156\u0158\u0003F#\u0000\u0157\u0155"+
		"\u0001\u0000\u0000\u0000\u0157\u0156\u0001\u0000\u0000\u0000\u0158C\u0001"+
		"\u0000\u0000\u0000\u0159\u015a\u0005\r\u0000\u0000\u015a\u015b\u0005\u0013"+
		"\u0000\u0000\u015bE\u0001\u0000\u0000\u0000\u015c\u015d\u0005\u000f\u0000"+
		"\u0000\u015d\u015e\u0003(\u0014\u0000\u015e\u015f\u0005\u0012\u0000\u0000"+
		"\u015fG\u0001\u0000\u0000\u0000\u0160\u0164\u0005#\u0000\u0000\u0161\u0163"+
		"\u0003J%\u0000\u0162\u0161\u0001\u0000\u0000\u0000\u0163\u0166\u0001\u0000"+
		"\u0000\u0000\u0164\u0162\u0001\u0000\u0000\u0000\u0164\u0165\u0001\u0000"+
		"\u0000\u0000\u0165I\u0001\u0000\u0000\u0000\u0166\u0164\u0001\u0000\u0000"+
		"\u0000\u0167\u016a\u0003L&\u0000\u0168\u016a\u0003N\'\u0000\u0169\u0167"+
		"\u0001\u0000\u0000\u0000\u0169\u0168\u0001\u0000\u0000\u0000\u016aK\u0001"+
		"\u0000\u0000\u0000\u016b\u016c\u0005\u001d\u0000\u0000\u016c\u016d\u0005"+
		"#\u0000\u0000\u016dM\u0001\u0000\u0000\u0000\u016e\u016f\u0005\u000e\u0000"+
		"\u0000\u016f\u0170\u0003&\u0013\u0000\u0170\u0171\u0005\u0011\u0000\u0000"+
		"\u0171O\u0001\u0000\u0000\u0000\u0172\u0173\u0007\u0000\u0000\u0000\u0173"+
		"Q\u0001\u0000\u0000\u0000\'V^gqty|\u0082\u0086\u008b\u008d\u0093\u009c"+
		"\u00a0\u00a8\u00ad\u00b1\u00b7\u00bf\u00ca\u00db\u00e3\u00eb\u00f3\u00fb"+
		"\u0103\u010b\u0113\u011c\u0121\u0124\u013e\u0142\u0144\u014a\u0151\u0157"+
		"\u0164\u0169";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}