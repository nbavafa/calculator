package calculator;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

/**
 * @author Nicholas Bavafa
 *         A13442275, cs15fiy
 * @date 12/4/17
 * HW8
 */

public class ASTTester {
  /*
    Product
    Quotient
    Sum
    Difference
    Power
    Negation
    Normalize
    Define
    Value
  */
  private AST theTester;  //Type AST that will be initialized in each test

  private AST val_1;    //A value to use in tests, units m/s
  private AST val_2;    //A value to use in tests, units s
  private AST val_3;    //A value to use in tests, units 1/s
  private AST val_4;    //A value to use in tests, units m/s
  private AST toNormal; //A value to normalize, units mph

  private int SQUARE = 2; //A constant that represents the power when squaring

  private QuantityDB data = new QuantityDB(); //Initialize a new database
  private Map<String,Quantity> db; //Declare a new variable to hold the database

  @Before
  public void setUp() {
    db = data.getDB();  //Get the database

    val_1 = new Value(new Quantity(10, Arrays.asList("m"), Arrays.asList("s")));
    val_2 = new Value(new Quantity(5, Arrays.asList("s"),
                                   new ArrayList<String>()));
    val_3 = new Value(new Quantity(2, new ArrayList<String>(),
                                   Arrays.asList("s")));
    val_4 = new Value(new Quantity(5, Arrays.asList("m"), Arrays.asList("s")));

    toNormal = new Value(new Quantity(1, Arrays.asList("mph"),
                                      new ArrayList<String>()));

  }

  @Test
  public void testProductEval() {
    theTester = new Product(val_1, val_2);
    assertEquals("Checking eval: product", new Quantity(50, Arrays.asList("m"),
                              new ArrayList<String>()), theTester.eval(db));
  }

  @Test
  public void testQuotientEval() {
    theTester = new Quotient(val_1, val_3);
    assertEquals("Checking eval: quotient", new Quantity(5, Arrays.asList("m"),
                              new ArrayList<String>()), theTester.eval(db));
  }

  @Test
  public void testPowerEval() {
    theTester = new Power(val_1, SQUARE);
    assertEquals("Checking eval: power", new Quantity(100,
                              Arrays.asList("m", "m"), Arrays.asList("s", "s")),
                              theTester.eval(db));
  }

  @Test
  public void testSumEval() {
    theTester = new Sum(val_1, val_4);
    assertEquals("Checking eval: sum", new Quantity(15, Arrays.asList("m"),
                              Arrays.asList("s")), theTester.eval(db));
  }

  @Test
  public void testDifferenceEval() {
    theTester = new Difference(val_1, val_4);
    assertEquals("Checking eval: differ", new Quantity(5, Arrays.asList("m"),
                              Arrays.asList("s")), theTester.eval(db));
  }

  @Test
  public void testNegationEval() {
    theTester = new Negation(val_1);
    assertEquals("Checking eval: negate", new Quantity(-10, Arrays.asList("m"),
                              Arrays.asList("s")), theTester.eval(db));
  }

  @Test
  public void testNormalizeEval() {
    Quantity qNormalMS =
                new Quantity((double)((8 * 40 * 16.5 * 12 * .0254)/(60 * 60)),
                             Arrays.asList("meter"), Arrays.asList("second"));

    theTester = new Normalize(toNormal);
    assertEquals("Checking eval: normal", qNormalMS, theTester.eval(db));
  }

  @Test
  public void testDefineEval() {
    theTester = new Define("+", val_2);
    assertEquals("Checking eval: define", new Quantity(5, Arrays.asList("s"),
                                new ArrayList<String>()), theTester.eval(db));
  }

  @Test
  public void testValueEval() {
    assertEquals("Checking eval: value", new Quantity(10, Arrays.asList("m"),
                                          Arrays.asList("s")), val_1.eval(db));
  }

}
