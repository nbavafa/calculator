package calculator;

import java.util.*;
import java.text.DecimalFormat;

/**
 * @author Nicholas Bavafa
 * @date 12/4/17
 * HW8
 */

/**
 * Quantity class defines a new type of object called Quantity.
 * It can hold a double and a map that contains the units for the
 * quantity. Basic scientific operations can be run on each quantity
 * as well, which can be seen below in each method.
 */
public class Quantity {

  double data;   //Holds the data for each quantity
  Map<String, Integer> map; //Holds a map of the units of each quantity

  /** Zero constructor for Quantity */
  public Quantity() {
    this.map = new HashMap<String, Integer>(); //Holds the units of the quantity
    this.data = 1;    //Default value of the data
  }

  /** A copy constructor for Quantity
   * @param toCopy the Quantity to copy over
   */
  public Quantity(Quantity toCopy) {
    if (toCopy == null) {
      throw new IllegalArgumentException();
    }
    this.data = toCopy.data;
    this.map = new HashMap<String, Integer>(toCopy.map);
  }

  /** Build constructor for Quantity
   * @param value the data to be stored in the constructor
   * @param numer the numer units of the Quantity
   * @param denom the denomination units of the Quantity
   */
  public Quantity(double value, List<String> numer, List<String> denom) {
    if (numer == null) {  //Illegal argument
      throw new IllegalArgumentException(); }
    if (denom == null) {//Illegal argument
      throw new IllegalArgumentException(); }

    this.data = value; //Assigns value to value passed in
    this.map = new HashMap<String, Integer>(); //Defines a new map
    int power = 0;    //used when assigning powers to each unit

    //Below creates a map with proper exponents of each unit

    //To hold what values have been added
    LinkedList added = new LinkedList<String>();

    for (int i = 0; i < numer.size(); i++) {  //Iterate through the numerator
        power = 0;
        if (!added.contains(numer.get(i))) {  //If unit has not been added yet

          //Iterate through numer and find how many times each unit is present
          for (int j = 0; j < numer.size(); j++) {
            if (numer.get(i).equals(numer.get(j))) { //If its the same unit
              power++; //Increment because in numerator
            }
          }

          //Iterate through denom and find how many times each unit is present
          for (int j = 0; j < denom.size(); j++) {
            if (numer.get(i).equals(denom.get(j))) { //If its the same unit
              power--; //Decrement because in denominator
            }
          }
          //If the power is 0, unit does not exist or cancelled out
          if (power !=0) map.put(numer.get(i), power);
          //Add the iterated through unit to the used list
          added.add(numer.get(i));
        }
    }


    for (int i = 0; i < denom.size(); i++) { //Iterate through the denominator
        power = 0; //Reset the power

        if (!added.contains(denom.get(i))) { //if the unit hasn't been added yet

          for (int j = 0; j < denom.size(); j++) {  //Find how many there are
            if (denom.get(i).equals(denom.get(j))) {
              power--;  //Decrement because denominator
            }
          }
          map.put(denom.get(i), power); //Put the power in the denominator
          added.add(denom.get(i));  //add the denominator to the iterated

        }
    }

  }

  /** mul() mulitplies a quantity by the passed in value
   * @param toMul The quantity to multiply
   * @return a new quantity holding the result
   */
  public Quantity mul(Quantity toMul) {
    double result;

    ArrayList<String> retNums = new ArrayList<String>();  //Hold numerators
    ArrayList<String> retDems = new ArrayList<String>();  //Hold denominators

    if (toMul == null) {
      throw new IllegalArgumentException("Null Quantity");
    }

    result = this.data * toMul.data;  //Multiply the data together

    Set thisSet = this.map.entrySet(); //Set of the current Quantity
    Set mulSet = toMul.map.entrySet(); //Set of the multiply quantity

    Iterator thisIter = thisSet.iterator(); //Iterator for current Quantity
    Iterator mulIter = mulSet.iterator();   //Iterator for multiply Quantity

    while (thisIter.hasNext()) {
      String[] entry = thisIter.next().toString().split("="); //Split up exps
      int exp =  Integer.parseInt(entry[1]);  //Parse exponent to integer

      if (exp > 0) {  //If a numerator
        for (int i = 0; i < exp; i++) {
          retNums.add(entry[0]);
        }
      }
      else {  //If a denominator
        for (int i = 0; i > exp; i--) {
          retDems.add(entry[0]);
        }
      }
    }

    while (mulIter.hasNext()) {

      String[] entry = mulIter.next().toString().split("=");
      int exp =  Integer.parseInt(entry[1]);

      if (exp > 0) {
        for (int i = 0; i < exp; i++) {
          retNums.add(entry[0]);
        }
      }
      else {
        for (int i = 0; i > exp; i--) {
          retDems.add(entry[0]);
        }
      }
    }

    return new Quantity(result, retNums, retDems);

  }

  /** div() mulitplies a quantity by the passed in value
   * @param toDiv The quantity to divide
   * @return a new quantity holding the result
   */
  public Quantity div(Quantity toDiv) {
    double result;

    ArrayList<String> retNums = new ArrayList<String>();
    ArrayList<String> retDems = new ArrayList<String>();

    if (toDiv == null) {
      throw new IllegalArgumentException("Null Quantity");
    }

    if (toDiv.data == 0) {
      throw new IllegalArgumentException("Can not divide by 0");
    }

    result = (double)(this.data / toDiv.data);

    Set thisSet = this.map.entrySet(); //Set of the current Quantity
    Set divSet = toDiv.map.entrySet(); //Set of the multiply quantity

    Iterator thisIter = thisSet.iterator(); //Iterator for current Quantity
    Iterator divIter = divSet.iterator();   //Iterator for multiply Quantity

    while (thisIter.hasNext()) {
      String[] entry = thisIter.next().toString().split("="); //Split up exps
      int exp =  Integer.parseInt(entry[1]);  //Parse exponent to integer

      if (exp > 0) {  //If the exponent is positive
        for (int i = 0; i < exp; i++) {
          retNums.add(entry[0]);
        }
      }
      else {  //
        for (int i = 0; i > exp; i--) { //The exponent is negative
          retDems.add(entry[0]);
        }
      }
    }

    while (divIter.hasNext()) { //Divide Quantity iterator

      String[] entry = divIter.next().toString().split("="); //Split up exps
      int exp =  Integer.parseInt(entry[1]);  //Parse exponent to integer

      if (exp > 0) { //If the exponent is positive
        for (int i = 0; i < exp; i++) {
          retDems.add(entry[0]);
        }
      }
      else { //If the exponent is negative
        for (int i = 0; i > exp; i--) {
          retNums.add(entry[0]);
        }
      }
    }

    return new Quantity(result, retNums, retDems);
  }

  /** pow() raised a Quantity to the passed in power
   * @param power The power to raise Quantity by
   * @return a new quantity holding the result
   */
  public Quantity pow(int power) {
    Set thisSet = this.map.entrySet();
    Iterator thisIter = thisSet.iterator();
    ArrayList<String> retNums = new ArrayList<String>();
    ArrayList<String> retDems = new ArrayList<String>();

    while (thisIter.hasNext()) {
      String[] entry = thisIter.next().toString().split("=");
      int exp =  Integer.parseInt(entry[1]);

      if (exp > 0) {  //If exponent is positive
        for (int i = 0; i < (exp*Math.abs(power)); i++) {
          retNums.add(entry[0]);
        }
      }
      else {  //If exponent is negative
        for (int i = 0; i > (exp*Math.abs(power)); i--) {
          retDems.add(entry[0]);
        }
      }
    }


    Quantity toReturn = new Quantity(Math.pow(this.data, Math.abs(power)),
                                     retNums, retDems);
    if (power < 0) {
      toReturn = (new Quantity(1.0, new ArrayList<String>(),
                  new ArrayList<String>())).div(toReturn);
    }
    return toReturn;

  }

  /** add() adds a quantity by the passed in value
   * @param toAdd The quantity to add
   * @return a new quantity holding the result
   */
  public Quantity add(Quantity toAdd) {
    if (toAdd == null) {
      throw new IllegalArgumentException("Null Quantity");
    }

    if (this.map.equals(toAdd.map)) { //If the units are the same
      double value = this.data + toAdd.data;
      Quantity toReturn = new Quantity(this);
      toReturn.data = value;

      return toReturn;
    }
    else {
      throw new IllegalArgumentException("Illegal Units");
    }
  }

  /** sub() subtracts a quantity by the passed in value
   * @param toSub The quantity to subtract by
   * @return a new quantity holding the result
   */
  public Quantity sub(Quantity toSub) {
    if (toSub == null) {
      throw new IllegalArgumentException("Null Quantity");
    }

    if (this.map.equals(toSub.map)) { //If the units are equal
      double value = this.data - toSub.data;
      Quantity toReturn = new Quantity(this);
      toReturn.data = value;

      return toReturn;
    }
    else {
      throw new IllegalArgumentException("Illegal Units");
    }
  }

  /** negate() negates a quantity
   * @return a new quantity holding the result
   */
  public Quantity negate() {
    Quantity toReturn = new Quantity(-1, new ArrayList<String>(),
                                      new ArrayList<String>());
    return this.mul(toReturn);
  }

  /** normalize() normalizes a quantity
   * @param db A database of units
   * @return a new quantity holding the result
   */
  public Quantity normalize(Map<String,Quantity> db) {
    if (db == null) {
      throw new IllegalArgumentException("Null Database");
    }

    //Quantity toReturn
    Quantity toReturn = new Quantity(this.data,
                        new ArrayList<String>(), new ArrayList<String>());

    Set thisSet = this.map.entrySet();
    Iterator thisIter = thisSet.iterator();

    while (thisIter.hasNext()) { //If there is next units
      String[] entry = thisIter.next().toString().split("=");
      int exp =  Integer.parseInt(entry[1]);

      if (exp > 0) { //If the exponent is positive
        for (int i = 0; i < exp; i++) {
          toReturn = toReturn.mul(Quantity.normalizedUnit(entry[0], db));
        }
      }
      else { //If the exponent is negative
        for (int i = 0; i > exp; i--) {
          toReturn = toReturn.div(Quantity.normalizedUnit(entry[0], db));
        }
      }
    }
    return toReturn;
  }

  /** normalizedUnit() normalizes a unit in a database
   * @param unit the unit to normalize
   * @param db A database of all the units
   * @return a new quantity holding the result
   */
  public static Quantity normalizedUnit(String unit, Map<String,Quantity> db) {
    if (unit == null) {
      throw new IllegalArgumentException("No units");
    }
    if (db == null) {
      throw new IllegalArgumentException("Null Database");
    }

    double valToReturn = 1;

    Quantity curr = new Quantity(1, Arrays.asList(unit), new ArrayList<String>());
    Quantity convert = new Quantity(db.get(unit));

    while (convert != null) { //While there is still units to convert
      if ((curr.map).equals(convert.map) == false) {
  			curr = convert;
  			valToReturn = valToReturn * convert.data;

        Set convertSet = convert.map.entrySet();
        Iterator convertIter = convertSet.iterator();

        if (convertIter.hasNext()) { //If theres units still
          String[] entry = convertIter.next().toString().split("=");
          convert = db.get(entry[0]);
        }

      }
		}

    curr.data = valToReturn; //Set the data to the converted value
    return curr;

  }

  /** equals() checking equality of a quantity by the passed in value
   * @param compare The object to check is equal
   * @return a boolean true if equal, false otherwise
   */
  @Override
  public boolean equals(Object compare) {
    if (compare == null) {
      throw new IllegalArgumentException("Null Quantity");
    }

    if (compare instanceof Quantity) {
      String thisToString = this.toString();
      String compareToString = this.toString();

      if (thisToString.equals(compareToString)) { //If the toString are equal
        return true;
      }
    }
    return false;
  }

  /** hashCode() produces a hashcode for a quantity
   * @return an int holding the hashcode
   */
  public int hashCode() {
    int code = 0;
    String[] expts = this.toString().split("^"); //Compute the exponents
    if (expts.length != 1) {
      for (int i = 1; i < expts.length; i++) {
        code = code + Integer.parseInt(expts[i].substring(0,1));
      }
    }
    return code;
  }

  /** toString()
   * @return String is formatted for printing a Quantity
   */
  public String toString() {
    double valueOfTheQuantity = this.data;
    Map<String,Integer> mapOfTheQuantity = this.map;

    // Ensure we get the units in order
    TreeSet<String> orderedUnits =
        new TreeSet<String>(mapOfTheQuantity.keySet());

    StringBuffer unitsString = new StringBuffer();

    for (String key : orderedUnits) {
      int expt = mapOfTheQuantity.get(key);
      unitsString.append(" " + key);
      if (expt != 1)
           unitsString.append("^" + expt);
    }

    // Used to convert doubles to a string with a
    //   fixed maximum number of decimal places.
    DecimalFormat df = new DecimalFormat("0.0####");
    // Put it all together and return.
    return df.format(valueOfTheQuantity) + unitsString.toString();
  }


}
