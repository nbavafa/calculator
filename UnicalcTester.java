package calculator;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

/**
 * @author Nicholas Bavafa
 * @date 12/4/17
 * HW8
 */

public class UnicalcTester {
 /*
    S
    L
    E
    P
    K
    Q
    R
  */

  private Unicalc calc; //Declaration of a Unicalc

  private Value val_1;  //Declaration for a value to be passed to the classes
  private Value val_2;  //Declaration for a value to be passed to the classes
  private Value val_3;  //Declaration for a value to be passed to the classes
  private Value val_4;  //Declaration for a value to be passed to the classes

  @Before
  public void setUp() {
    calc = new Unicalc(); //A new instance of Unicalc
  }

  @Test
  public void testS() {
    calc.tokenize("def var 10 kg sec");

    val_1 = new Value(new Quantity(10, new ArrayList<String>(),
                                   new ArrayList<String>()));
    val_2 = new Value(new Quantity(1, Arrays.asList("kg"),
                                   new ArrayList<String>()));
    val_3 = new Value(new Quantity(1, Arrays.asList("sec"),
                                   new ArrayList<String>()));

    Define defineS = new Define("var", new Product(val_1,
                                       new Product(val_2, val_3)));
    assertEquals("Checking S with def", defineS, calc.S());
  }

  @Test
  public void testL() {
    calc.tokenize("# 13 mph");

    val_1 = new Value(new Quantity(13, new ArrayList<String>(),
                                   new ArrayList<String>()));
    val_2 = new Value(new Quantity(1, Arrays.asList("mph"),
                                   new ArrayList<String>()));

    Normalize normalL = new Normalize(new Product(val_1, val_2));
    assertEquals("Checking L with #", normalL, calc.L());

  }

  @Test
  public void testE() {
    calc.tokenize("7 hour + 8 hour");

    val_1 = new Value(new Quantity(7, new ArrayList<String>(),
                      new ArrayList<String>()));
    val_2 = new Value(new Quantity(1, Arrays.asList("hour"),
                      new ArrayList<String>()));
    val_3 = new Value(new Quantity(8, new ArrayList<String>(),
                      new ArrayList<String>()));
    val_4 = new Value(new Quantity(1, Arrays.asList("hour"),
                      new ArrayList<String>()));

    Sum sumE = new Sum(new Product(val_1, val_2), new Product(val_3, val_4));
    assertEquals("Checking E with +", sumE, calc.E());

  }

  @Test
  public void testP() {
    calc.tokenize("10 * 5 hour");

    val_1 = new Value(new Quantity(10, new ArrayList<String>(),
                      new ArrayList<String>()));
    val_2 = new Value(new Quantity(5, new ArrayList<String>(),
                      new ArrayList<String>()));
    val_3 = new Value(new Quantity(1, Arrays.asList("hour"),
                      new ArrayList<String>()));

    Product productP = new Product(val_1, new Product(val_2, val_3));
    assertEquals("Checking S with *", productP, calc.S());
  }

  @Test
  public void testK() {
    calc.tokenize("-10");

    val_1 = new Value(new Quantity(10, new ArrayList<String>(),
                      new ArrayList<String>()));

    Negation negateK = new Negation(val_1);
    assertEquals("Checking K with -10", negateK, calc.K());

  }

  @Test
  public void testQ() {
    calc.tokenize("8 hour");

    val_1 = new Value(new Quantity(8, new ArrayList<String>(),
                      new ArrayList<String>()));
    val_2 = new Value(new Quantity(1, Arrays.asList("hour"),
                      new ArrayList<String>()));

    Product productQ = new Product(val_1, val_2);
    assertEquals("Checking Q with 8 hour", productQ, calc.Q());

  }

  @Test
  public void testR() {
    calc.tokenize("10 ^ 3");
    val_1 = new Value(new Quantity(10, new ArrayList<String>(),
                      new ArrayList<String>()));
    Power powR = new Power(val_1, 3);
    assertEquals("Checking R with 10^3", powR, calc.R());


    calc.tokenize("(10 hour) ^ 2");
    val_1 = new Value(new Quantity(10, new ArrayList<String>(),
                      new ArrayList<String>()));
    val_2 = new Value(new Quantity(1, Arrays.asList("hour"),
                      new ArrayList<String>()));
    powR = new Power(new Product(val_1, val_2), 2);
    assertEquals("Checking R with 10hour^2", powR, calc.R());


    calc.tokenize("10 ^ -3");
    val_1 = new Value(new Quantity(10, new ArrayList<String>(),
                      new ArrayList<String>()));
    powR = new Power(val_1, -3);
    assertEquals("Checking R with 10^-3", powR, calc.R());


    calc.tokenize("sec ^ -2");
    val_1 = new Value(new Quantity(1, Arrays.asList("sec"),
                      new ArrayList<String>()));
    powR = new Power(val_1, -2);
    assertEquals("Checking R with sec^-2", powR, calc.R());

  }

}
