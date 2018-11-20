/**
 * Nutrient.java
 * Nutrient class used for storing the details of a nutrient
 * @author Peter McBurney
 * CQU Student number: Q9707351  
 * @version 1.0
 * @since 26-03-2018
 */
package restaurantordergui;

//START OF CLASS Nutrient
public class Nutrient {
    
    //Declare variables for nutrient name and nutrient value
    private String nutrientName;
    private double nutrientValue;
 
    //START OF DEFAULT CONSTRUCTOR
    public Nutrient()
    {
        nutrientName = new String();
        nutrientValue = 0;
    }//END OF DEFAULT CONSTRUCTOR
        
    //START OF PARAMETERISED CONSTRUCTOR
    public Nutrient(String nutrientName, double nutrientValue)
    {
        this.nutrientName = new String(nutrientName);
        this.nutrientValue = nutrientValue;
    }//END OF PARAMETERISED CONSTRUCTOR
    
    //START OF METHOD setNutrientName
    public void setNutrientName(String nutrientName)
    {
        this.nutrientName = nutrientName;        
    }//END OF METHOD setNutrientName
            
    //START OF METHOD setNutrientValue
    public void setNutrientValue(double nutrientValue)
    {   
        this.nutrientValue = nutrientValue;            
    }//END OF METHOD setNutrientValue
           
    //START OF METHOD getNutrientName
    public String getNutrientName()
    {
        return this.nutrientName;
    }//END OF METHOD getNutrientName
 
    //START OF METHOD getNutrientValue
    public double getNutrientValue()
    {
        return this.nutrientValue;
    }//END OF METHOD getNutrientValue
      
    //START OF OVERRIDDEN METHOD toString    
    @Override
    public String toString()
    {
        return String.format("%s %f", getNutrientName(), getNutrientValue());
    }//END OF OVERRIDDEN METHOD toString
}//END OF CLASS Nutrient
