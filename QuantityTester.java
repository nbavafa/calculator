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


public class QuantityTester {
  /**
  	3 Constructors
    mul
    div
    add
    sub
    negate
    normalizedUnit
    normalize
    pow
    equals
    hashCode
  */

  private Quantity q0;  //0 Constructor object
  private Quantity q1;  //copy constructor object
  private Quantity q3;  //new quantity constructor object
  private Quantity q3Copy; //A copy of q3 object
  private Quantity qPass;  //Used to test mul/div Quantity methods
  private Quantity qAdd;   //Used to test add/sub Quantity methods
  private Quantity qNormalize; //Used to test normalizations
  private Quantity qNorExpect; //Expected output of normalization
  private Quantity qNorMPH;

  private QuantityDB data = new QuantityDB(); //Initialize the database
  private Map<String,Quantity> db; //Create a new database

  @Before
  public void setUp() {
    q0 = new Quantity();   //To test first Constructor
    q1 = new Quantity(q0); //To test second Constructor
    q3 = new Quantity(10, Arrays.asList("m"), Arrays.asList("s")); //Third
    q3Copy = new Quantity(q3); //Copy the third Quantity

    //Used to test multiplication/division methods
    qPass = new Quantity(5, Arrays.asList("s"), new ArrayList<String>());

    //Used to test add/substrat methods
    qAdd = new Quantity(5, Arrays.asList("m"), Arrays.asList("s"));

    //Used to test normalization methods
    qNormalize = new Quantity(1, Arrays.asList("hour"),
                              new ArrayList<String>());
    qNorMPH = new Quantity((double)((8 * 40 * 16.5 * 12 * .0254)/(60 * 60)),
                              Arrays.asList("meter"), Arrays.asList("second"));
    qNorExpect = new Quantity(3600, Arrays.asList("second"),
                              new ArrayList<String>());

    //Get the database
    db = data.getDB();

  }
  @Test
  public void testZeroConstructor() {
    assertEquals("Checking zero constructor", new Quantity(1.0,
                  new ArrayList<String>(), new ArrayList<String>()).toString(), q0.toString());
  }

  @Test
  public void testCopyConstructor() {
    assertEquals("Checking copy of zero constructor", q0.toString(), q1.toString());
    assertEquals("Checking copy constructor", new Quantity(q3).toString(), q3.toString());
  }

  @Test
  public void testBuildConstructor() {
    assertEquals("Checking build constructor", new Quantity(10,
                  Arrays.asList("m"), Arrays.asList("s")).toString(), q3.toString());
    try {
      new Quantity(1.0, null, null);
      fail("Exception: Trying to create a Quantity will null arguments");
    }
    catch (IllegalArgumentException e) {  /*normal*/   }
  }


  @Test
  public void testMul() {
    assertEquals("Checking mul", new Quantity(50.0, Arrays.asList("m"),
                  new ArrayList<String>()), q3.mul(qPass));
    try {
      q3.mul(null);
      fail("Exception: Passed in a null AST");
    }
    catch (IllegalArgumentException e) {  /*normal*/   }
  }

  @Test
  public void testDiv() {
    assertEquals("Checking div", new Quantity(2, Arrays.asList("m"),
                  Arrays.asList("s", "s")), q3.div(qPass));
    try {
      q3.div(null);
      fail("Exception: Passed in a null AST");
    }
    catch (IllegalArgumentException e) {  /*normal*/   }

    try {
      q3.div(new Quantity(0, new ArrayList<String>(), new ArrayList<String>()));
      fail("Exception: Dividing by AST of value 0");
    }
    catch (IllegalArgumentException e) {  /*normal*/  }
  }

  @Test
  public void testAdd() {
    assertEquals("Checking add", new Quantity(15, Arrays.asList("m"),
                  Arrays.asList("s")), q3.add(qAdd));
    try {
      q3.add(qPass);
      fail("Exception: Adding by AST of not compatiable units");
    }
  catch (IllegalArgumentException e) {  /*normal*/  }
  }

  @Test
  public void testSub() {
    assertEquals("Checking sub", new Quantity(5, Arrays.asList("m"),
                  Arrays.asList("s")), q3.sub(qAdd));
    try {
      q3.sub(new Quantity(10, Arrays.asList("cm"), Arrays.asList("s")));
      fail("Exception: Subtracting by AST of not compatiable units");
    }
  catch (IllegalArgumentException e) {  /*normal*/  }
  }

  @Test
  public void testNegate() {
    assertEquals("Checking negate", new Quantity(-10, Arrays.asList("m"),
                  Arrays.asList("s")), q3.negate());
  }

  @Test
  public void testNormalizedUnit(){

    assertEquals("Checking normalizedUnit", qNorMPH,
                  Quantity.normalizedUnit("mph", db));
  }

  @Test
  public void testNormalize() {
    assertEquals("Checking normalize", qNorExpect, qNormalize.normalize(db));
  }

  @Test
  public void testPow(){
    Quantity pow3 = new Quantity(1000,Arrays.asList("m", "m", "m"),
                                  Arrays.asList("s", "s","s"));
    Quantity pow1 = new Quantity(10, Arrays.asList("m"), Arrays.asList("s"));
    Quantity pow0 = new Quantity(1, new ArrayList<String>(),
                                  new ArrayList<String>());
    Quantity pow_1 = new Quantity((double)(1.0/10.0), Arrays.asList("s"),
                                  Arrays.asList("m"));
    Quantity pow_3 = new Quantity((double)(1.0/1000.0),
                                  Arrays.asList("s", "s", "s"),
                                  Arrays.asList("m", "m", "m"));

    assertEquals("Checking power of 3", pow3, q3.pow(3));
    assertEquals("Checking power of 1", pow1, q3.pow(1));
    assertEquals("Checking power of 0", pow0, q3.pow(0));
    assertEquals("Checking power of -1", pow_1, q3.pow(-1));
    assertEquals("CHecking power of -3", pow_3, q3.pow(-3));

    assertEquals("Checking power (double) of (1.63)", pow1, q3.pow((int) 1.63));
  }

  @Test
  public void testEquals() {
    assertEquals("Checking equals", true, q3Copy.equals(q3));
  }

  @Test
  public void testHashCode() {
    assertEquals("Checking equal hashCodes", q3.hashCode(), q3Copy.hashCode());
  }

}
