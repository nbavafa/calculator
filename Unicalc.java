// Unicalc.java
// Original Author: Chris Stone, Harvey Mudd College
// TODO: Fill in and modify the functions below where the comments
//   suggest changes are needed so that these functions correctly parse
//   the Unicalc language

package calculator;

import java.util.*;        // Scanner, LinkedList, ...
import java.util.regex.*;  // Pattern, Matcher, ...

/**
 * @author Nicholas Bavafa
 * @date 12/4/17
 * HW8
 */

class Unicalc
{
    private String POWER = "^";     //Symbol representing the power function
    private String OPEN_PAREN = "(";//Symbol representing the paren function
    private String MINUS = "-";     //Symbol representing the minus function
    private String PLUS = "+";      //Symbol representing the plus function
    private String MULTIPLY = "*";  //Symbol representing the multiply function
    private String DIVIDE = "/";    //Symbol representing the divide function
    private String NORMAL = "#";    //Symbol representing the normal function
    private String DEFINE = "def";  //Symbol representing the def function


    private LinkedList<String> toks;  // Used by the parser functions
    //    to keep track of the remaining tokens.
    // Initialized by tokenize(...)
    // Parsing functions remove the tokens

    public Unicalc()
    {
        // Nothing we need to do in the constructor.
    }

    // method tokenize
    //   Takes a string, and sets this.toks to be a linked
    //     list of all Unicalc tokens in this string
    //     (ignoring whitespace, and other things that can't
    //     be part of a token.)
    //
    public void tokenize(String input)
    {
        this.toks = new LinkedList<String>( Tokenizer.tokenize( input ) );

        System.out.println("Tokens: " + this.toks);

        return;
    }

    // method parse
    // Tries to parse the contents of this.toks
    //    using recursive descent, starting with the start symbol.
    // output: an abstract syntax tree representing the input
    //
    public AST parse()
    {
        // Begin parsing with the start symbol
        AST answer = this.S();

        // Display results
        System.out.println("AST: " + answer);
        if (! toks.isEmpty()) {
            System.out.println("Extra tokens left over: " + toks);
        }
        System.out.println();

        return answer;
    }

    /**  S() handles S in the grammar, mainly the def command
     * @return the untokenized AST for this method, a call to the next grammer
     */
    public AST S()
    {
        //  S -> def W L | L
        if (!toks.isEmpty()) {

          if (DEFINE.equals(toks.peek())) {
            toks.pop();
            return new Define(toks.pop(), L());
          }

        }
        return L();
    }

    /**  L() handles L in the grammar, mainly the normalization (#) command
     * @return the untokenized AST for this method, a call to the next grammer
     */
    public AST L()
    {
        // L -> # E | E
        if (!toks.isEmpty()) {

          if (NORMAL.equals(toks.peek())) {
            toks.pop();
            return new Normalize(E());
          }

        }

        return E();  // I don't think we should *always* do this...

    }

    /**  E() handles E in the grammar, mainly addition and subtraction commands
     * @return the untokenized AST for this method, a call to the next grammer
     */
    public AST E()
    {
        //   E -> P + E | P - E | P

        AST p = P();
        if (!toks.isEmpty()) {

          if (PLUS.equals(toks.peek())) {
            toks.pop();
            return new Sum(p, E());
          }
          else if (MINUS.equals(toks.peek())) {
            toks.pop();
            return new Difference(p, E());
          }

        }

        return p;  // I don't think we should *always* do this...

    }

    /**  P() handles S in the grammar, mainly multiplication and division
     *       commands
     * @return the untokenized AST for this method, a call to the next grammer
     */
    public AST P()
    {
        //   P -> K * P | K / P | K

        AST k = K();
        if (!toks.isEmpty()) {

          if (MULTIPLY.equals(toks.peek())) {
            toks.pop();
            return new Product(k, P());
          }
          else if (DIVIDE.equals(toks.peek())) {
            toks.pop();
            return new Quotient(k, P());
          }

        }
        return k;  // I don't think we should *always* do this
    }

    /**  K() handles K in the grammar, mainly the negation command
     * @return the untokenized AST for this method, a call to the next grammer
     */
    public AST K()
    {
        // K -> - K | Q
        if (!toks.isEmpty()) {

          if (MINUS.equals(toks.peek())) {
            toks.pop();
            return new Negation(K());
          }

        }
        return Q();  // I don't think we should *always* do this
    }

    // private method isNumber
    //   Checks that the given string is non-null
    //      and could be converted to a double
    //      (via the Double.parseDouble function)
    //      without generating an error
    //
    private static boolean isNumber(String s) {
        if (s == null) return false;

        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // private method isAlphabetic
    //   Checks that the given string is non-null
    //      and consists of (ASCII) letters or underscores
    //      (but not digits).
    //
    private static boolean isAlphabetic(String s) {
        return s != null && s.matches("[a-zA-Z_]+");
    }

    /**  Q() handles Q in the grammar, deciding numerals or alphanumerics
     * @return the untokenized AST for this method, a call to the next grammer
     */
    public AST Q()
    {
        // Q -> R | R Q

        AST r = R();
        if (!toks.isEmpty()) {

          boolean num = isNumber(toks.peek());
          boolean alph = isAlphabetic(toks.peek());
          boolean isPar = OPEN_PAREN.equals(toks.peek());

          if (num || alph || isPar) {
            return new Product(r, Q()); //If not a command, resurvie call
          }

        }

        return r;

        // I don't think I should *always* do this
        //   (e.g., if I peek and the R is followed
        //    by a number, word, or left parenthesis,
        //    I should try to recurively grab at least
        //    one more R via Q...)

    }

    /**  R() handles R in the grammar, mainly the power command
     * @return the untokenized AST for this method, a call to the next grammer
     */
    public AST R()
    {
        // R -> V | V ^ J
        AST v = V();

        if (!toks.isEmpty()) {

          if (toks.peek().equals(POWER)) {
            toks.pop();
            return new Power(v, J());
          }

        }

        return v;  // I don't think I should *always* do this
    }

    //  --------------------------------------------------
    //   The following functions should need no changes.


    // Function for grabbing tokens corresponding to V in the grammar,
    //    and returning a corresponding abstract syntax tree.
    // Should work without changes, but you should make sure
    //    you understand how it works.
    public AST V()
    {
        // V -> D | W | ( L )

        List<String> emp = Collections.<String>emptyList();  // an empty list

        String next = toks.peek();

        if ( isNumber(next) ) {
            // D
            double d1 = D();
            return new Value(new Quantity(d1, emp, emp));

        } else if ( isAlphabetic(next) ) {
            // W
            String unitName = toks.pop();
            return new Value(new Quantity(1.0, Arrays.asList(unitName), emp));

        } else if ( "(".equals(next) ) {
            // ( L )

            toks.pop();  // Skip the left parenthesis
            AST l = L(); // Recursively grab the contents, which should match L.

            String after_l = toks.peek();

            // Immediately following that L should be a right parenthesis. Check
            if ( ")".equals(after_l) ) {
                // It's there, so get rid of it (since we want to remove all
                //    the tokens corresponding to V)
                toks.pop();
                return l;     // A parenthesized L has the same tree as a non-parenthesized L
                //   (Since the tree structure *already* tells us the way that
                //    our subexpressions are grouped, keeping around these
                //    parentheses in the AST is unnecessary; they wouldn't affect
                //    the final answer.)
            } else {
                throw new ParseError("Expected close-parenthesis, but found: '" +
                                     after_l + "'");
            }

        } else {
            // The first token can't possibly be part of a V.
            throw new ParseError("Expected number or identifier or subexpression, but found: '"
                                 + next + "'");
        }
    }


    // Parsing function for J in the grammar.
    //   Note: unlike most of the parsing functions,
    //         we're returning the corresponding int rather than an AST.
    //
    public int J()
    {
        // J -> -I | I

        String next = toks.peek();

        if ( "-".equals(next) ) {
            toks.pop();       // Get rid of the minus-sign token
            int i = I();      // Following it should be something matching I
            return (- i);     // Combine the minus sign and i
        } else {
            return I();       // No minus sign, so look for something matching I
        }
    }

    // Parsing function for I in the grammar.
    //   Note: unlike most of the parsing functions,
    //         we're returning the corresponding int rather than an AST.
    //
    public int I()
    {
        // I -> (any nonnegative integer)

        String next = toks.peek();
        if (next == null) {
            throw new ParseError("Expected an integer, but found null");
        }
        try {
            toks.pop();
            return Integer.parseInt(next);
        } catch (NumberFormatException e) {
            throw new ParseError("Expected an integer, but found: '" + next + "'");
        }
    }

    // Parsing function for D in the grammar.
    //   Note: unlike most of the parsing functions,
    //         we're returning the corresponding double-precision
    //         floating-point number, rather than an AST.
    public double D() {
        // D -> (any nonnegative number, integer or floating-point)

        String next = toks.peek();
        try {
            toks.pop();
            return Double.parseDouble(next);
        } catch (NumberFormatException e) {
            throw new ParseError("Expected a number, but found: '" + next + "'");
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////

    /** method main
     * inputs: the usual
     * outputs: none, but this method has the Unicalc read-eval-print loop
     */
    public static void main(String[] args)
    {
        Scanner console = new Scanner(System.in);
        Unicalc unicalc = new Unicalc();

        Map<String,Quantity> env = QuantityDB.getDB();

        while (true)
        {
            System.out.print("input>  ");
            String input = console.nextLine();

            if (input.equals("")) break;  // Quit if no input

            unicalc.tokenize(input);

            AST ast = null;
            try {
                ast = unicalc.parse();
                System.out.println("Result: " + ast.eval(env) + "\n");

            } catch (ParseError e) {
                System.out.println( e );
            }
        }
    }

}

// ParseError class
//    A new kind of exception to be thrown if there is
//    an error in parsing

class ParseError extends RuntimeException {
    public ParseError(String message) {
        // Create a ParseError object containing
        // the given message as its error message.
        super(message);
    }
}
