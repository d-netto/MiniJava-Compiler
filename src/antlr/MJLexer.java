// Generated from MJLexer.g4 by ANTLR 4.9.2
package antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MJLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		CLASS_KW=1, PUBLIC_KW=2, STATIC_KW=3, VOID_KW=4, MAIN_KW=5, STRING_KW=6, 
		INT_KW=7, BOOLEAN_KW=8, LPARENS=9, RPARENS=10, LBRACKET=11, RBRACKET=12, 
		CURLY_LBRACKET=13, CURLY_RBRACKET=14, IF=15, WHILE=16, PRINTLN=17, AND=18, 
		NOT=19, LT=20, PLUS=21, MINUS=22, MULT=23, DOT=24, TRUE=25, FALSE=26, 
		THIS=27, NEW=28, ID=29, WS=30, COMMENT=31;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"CLASS_KW", "PUBLIC_KW", "STATIC_KW", "VOID_KW", "MAIN_KW", "STRING_KW", 
			"INT_KW", "BOOLEAN_KW", "LPARENS", "RPARENS", "LBRACKET", "RBRACKET", 
			"CURLY_LBRACKET", "CURLY_RBRACKET", "IF", "WHILE", "PRINTLN", "AND", 
			"NOT", "LT", "PLUS", "MINUS", "MULT", "DOT", "TRUE", "FALSE", "THIS", 
			"NEW", "ID", "WS", "COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'class'", "'public'", "'static'", "'void'", "'main'", "'String'", 
			"'int'", "'boolean'", "'('", "')'", "'['", "']'", "'{'", "'}'", "'if'", 
			"'while'", "'System.out.println'", "'&&'", "'!'", "'<'", "'+'", "'-'", 
			"'*'", "'.'", "'true'", "'false'", "'this'", "'new'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "CLASS_KW", "PUBLIC_KW", "STATIC_KW", "VOID_KW", "MAIN_KW", "STRING_KW", 
			"INT_KW", "BOOLEAN_KW", "LPARENS", "RPARENS", "LBRACKET", "RBRACKET", 
			"CURLY_LBRACKET", "CURLY_RBRACKET", "IF", "WHILE", "PRINTLN", "AND", 
			"NOT", "LT", "PLUS", "MINUS", "MULT", "DOT", "TRUE", "FALSE", "THIS", 
			"NEW", "ID", "WS", "COMMENT"
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


	public MJLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "MJLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2!\u00da\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\13\3\13"+
		"\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22"+
		"\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3\25\3\25"+
		"\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\32\3\32\3\33"+
		"\3\33\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3\35"+
		"\3\36\6\36\u00bf\n\36\r\36\16\36\u00c0\3\36\7\36\u00c4\n\36\f\36\16\36"+
		"\u00c7\13\36\3\37\6\37\u00ca\n\37\r\37\16\37\u00cb\3\37\3\37\3 \3 \3 "+
		"\3 \7 \u00d4\n \f \16 \u00d7\13 \3 \3 \2\2!\3\3\5\4\7\5\t\6\13\7\r\b\17"+
		"\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+"+
		"\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!\3\2\6\4\2C\\c|\6\2\62"+
		";C\\aac|\5\2\13\f\17\17\"\"\5\2UUYY\u0080\u0080\2\u00dd\2\3\3\2\2\2\2"+
		"\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2"+
		"\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2"+
		"\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2"+
		"\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2"+
		"\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2"+
		"\2\2?\3\2\2\2\3A\3\2\2\2\5G\3\2\2\2\7N\3\2\2\2\tU\3\2\2\2\13Z\3\2\2\2"+
		"\r_\3\2\2\2\17f\3\2\2\2\21j\3\2\2\2\23r\3\2\2\2\25t\3\2\2\2\27v\3\2\2"+
		"\2\31x\3\2\2\2\33z\3\2\2\2\35|\3\2\2\2\37~\3\2\2\2!\u0081\3\2\2\2#\u0087"+
		"\3\2\2\2%\u009a\3\2\2\2\'\u009d\3\2\2\2)\u009f\3\2\2\2+\u00a1\3\2\2\2"+
		"-\u00a3\3\2\2\2/\u00a5\3\2\2\2\61\u00a7\3\2\2\2\63\u00a9\3\2\2\2\65\u00ae"+
		"\3\2\2\2\67\u00b4\3\2\2\29\u00b9\3\2\2\2;\u00be\3\2\2\2=\u00c9\3\2\2\2"+
		"?\u00cf\3\2\2\2AB\7e\2\2BC\7n\2\2CD\7c\2\2DE\7u\2\2EF\7u\2\2F\4\3\2\2"+
		"\2GH\7r\2\2HI\7w\2\2IJ\7d\2\2JK\7n\2\2KL\7k\2\2LM\7e\2\2M\6\3\2\2\2NO"+
		"\7u\2\2OP\7v\2\2PQ\7c\2\2QR\7v\2\2RS\7k\2\2ST\7e\2\2T\b\3\2\2\2UV\7x\2"+
		"\2VW\7q\2\2WX\7k\2\2XY\7f\2\2Y\n\3\2\2\2Z[\7o\2\2[\\\7c\2\2\\]\7k\2\2"+
		"]^\7p\2\2^\f\3\2\2\2_`\7U\2\2`a\7v\2\2ab\7t\2\2bc\7k\2\2cd\7p\2\2de\7"+
		"i\2\2e\16\3\2\2\2fg\7k\2\2gh\7p\2\2hi\7v\2\2i\20\3\2\2\2jk\7d\2\2kl\7"+
		"q\2\2lm\7q\2\2mn\7n\2\2no\7g\2\2op\7c\2\2pq\7p\2\2q\22\3\2\2\2rs\7*\2"+
		"\2s\24\3\2\2\2tu\7+\2\2u\26\3\2\2\2vw\7]\2\2w\30\3\2\2\2xy\7_\2\2y\32"+
		"\3\2\2\2z{\7}\2\2{\34\3\2\2\2|}\7\177\2\2}\36\3\2\2\2~\177\7k\2\2\177"+
		"\u0080\7h\2\2\u0080 \3\2\2\2\u0081\u0082\7y\2\2\u0082\u0083\7j\2\2\u0083"+
		"\u0084\7k\2\2\u0084\u0085\7n\2\2\u0085\u0086\7g\2\2\u0086\"\3\2\2\2\u0087"+
		"\u0088\7U\2\2\u0088\u0089\7{\2\2\u0089\u008a\7u\2\2\u008a\u008b\7v\2\2"+
		"\u008b\u008c\7g\2\2\u008c\u008d\7o\2\2\u008d\u008e\7\60\2\2\u008e\u008f"+
		"\7q\2\2\u008f\u0090\7w\2\2\u0090\u0091\7v\2\2\u0091\u0092\7\60\2\2\u0092"+
		"\u0093\7r\2\2\u0093\u0094\7t\2\2\u0094\u0095\7k\2\2\u0095\u0096\7p\2\2"+
		"\u0096\u0097\7v\2\2\u0097\u0098\7n\2\2\u0098\u0099\7p\2\2\u0099$\3\2\2"+
		"\2\u009a\u009b\7(\2\2\u009b\u009c\7(\2\2\u009c&\3\2\2\2\u009d\u009e\7"+
		"#\2\2\u009e(\3\2\2\2\u009f\u00a0\7>\2\2\u00a0*\3\2\2\2\u00a1\u00a2\7-"+
		"\2\2\u00a2,\3\2\2\2\u00a3\u00a4\7/\2\2\u00a4.\3\2\2\2\u00a5\u00a6\7,\2"+
		"\2\u00a6\60\3\2\2\2\u00a7\u00a8\7\60\2\2\u00a8\62\3\2\2\2\u00a9\u00aa"+
		"\7v\2\2\u00aa\u00ab\7t\2\2\u00ab\u00ac\7w\2\2\u00ac\u00ad\7g\2\2\u00ad"+
		"\64\3\2\2\2\u00ae\u00af\7h\2\2\u00af\u00b0\7c\2\2\u00b0\u00b1\7n\2\2\u00b1"+
		"\u00b2\7u\2\2\u00b2\u00b3\7g\2\2\u00b3\66\3\2\2\2\u00b4\u00b5\7v\2\2\u00b5"+
		"\u00b6\7j\2\2\u00b6\u00b7\7k\2\2\u00b7\u00b8\7u\2\2\u00b88\3\2\2\2\u00b9"+
		"\u00ba\7p\2\2\u00ba\u00bb\7g\2\2\u00bb\u00bc\7y\2\2\u00bc:\3\2\2\2\u00bd"+
		"\u00bf\t\2\2\2\u00be\u00bd\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\u00be\3\2"+
		"\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00c5\3\2\2\2\u00c2\u00c4\t\3\2\2\u00c3"+
		"\u00c2\3\2\2\2\u00c4\u00c7\3\2\2\2\u00c5\u00c3\3\2\2\2\u00c5\u00c6\3\2"+
		"\2\2\u00c6<\3\2\2\2\u00c7\u00c5\3\2\2\2\u00c8\u00ca\t\4\2\2\u00c9\u00c8"+
		"\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00c9\3\2\2\2\u00cb\u00cc\3\2\2\2\u00cc"+
		"\u00cd\3\2\2\2\u00cd\u00ce\b\37\2\2\u00ce>\3\2\2\2\u00cf\u00d0\7\61\2"+
		"\2\u00d0\u00d1\7\61\2\2\u00d1\u00d5\3\2\2\2\u00d2\u00d4\t\5\2\2\u00d3"+
		"\u00d2\3\2\2\2\u00d4\u00d7\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d5\u00d6\3\2"+
		"\2\2\u00d6\u00d8\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d8\u00d9\b \2\2\u00d9"+
		"@\3\2\2\2\7\2\u00c0\u00c5\u00cb\u00d5\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}