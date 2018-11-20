/**
 * MenuItem.java
 * MenuItem class used for storing all the details of a particular menu item
 * @author Peter McBurney
 * CQU Student number: Q9707351  
 * @version 2.0
 * @since 23-04-2018
 */
package restaurantordergui;

import java.util.ArrayList; //Import for use of ArrayList

//START OF CLASS MenuItem
public class MenuItem {
    
    //Declare menuItem course
    private String itemCourse;
    //Declare menuItem meal type - Breakfast/Lunch/Dinner
    private String mealType;
    //Declare menuItem name
    private String itemName;   
    //Declare menuItem ID
    private int itemID;
    //Declare orderID variable, to be used when the menuItem is ordered via OrderedCustomer    
    private int orderID;
    
    //Declare ArrayList variable to store all the nutrients for a menu item
    private ArrayList<Nutrient> nutrients;
           
    //START OF DEFAULT CONSTRUCTOR
    public MenuItem()
    {
        itemCourse = new String();  
        mealType = new String();
        itemName = new String();
        itemID = 0;
        nutrients = new ArrayList<>();
        orderID = 0;
    }//END OF DEFAULT CONSTRUCTOR
        
    //START OF COPY CONSTRUCTOR
    public MenuItem(MenuItem menu)
    {
        this.itemCourse = new String(menu.itemCourse);
        this.mealType = new String(menu.mealType);
        this.itemName = new String(menu.itemName);
        this.itemID = menu.itemID;
        this.nutrients = new ArrayList<>(menu.nutrients);
        this.orderID = menu.orderID;
    }//END OF COPY CONSTRUCTOR
        
    //START OF PARAMETERISED CONSTRUCTOR
    public MenuItem(String itemCourse, String mealType, String itemName, int itemID, 
            int orderID, ArrayList<Nutrient> nutrients)
    {
        this.itemCourse = new String(itemCourse); 
        this.mealType = new String(mealType);
        this.itemName = new String(itemName);
        this.itemID = itemID;
        this.nutrients = new ArrayList<>(nutrients);
        this.orderID = orderID;
    }//END OF PARAMETERISED CONSTRUCTOR
       
    //START OF METHOD setItemName
    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }//END OF METHOD setItemName
    //START OF METHOD getItemName
    public String getItemName()
    {        
        return this.itemName;
    }//END OF METHOD getItemName
        
    //START OF METHOD setItemCourse - used for "Food" or "Beverage"
    public void setItemCourse(String itemCourse)
    {
        this.itemCourse = 
                itemCourse.substring(0,1).toUpperCase() + itemCourse.substring(1).toLowerCase();
    }//END OF METHOD setItemCourse
    
    //START OF METHOD getItemCourse
    public String getItemCourse()
    {        
        return this.itemCourse;
    }//END OF METHOD getItemCourse
       
    //START OF METHOD setMealType   -   used for“Breakfast”, “Lunch”, and “Dinner”
    public void setMealType(String mealType)
    {
        this.mealType = 
                mealType.substring(0,1).toUpperCase() + mealType.substring(1).toLowerCase();
    }//END OF METHOD setMealType
    
    //START OF METHOD getMealType
    public String getMealType()
    {        
        return this.mealType;
    }//END OF METHOD getMealType

    //START OF METHOD setItemID
    public void setItemID(int itemID)
    {
        this.itemID = itemID;
    }//END OF METHOD setItemID
    
    //START OF METHOD getItemID
    public int getItemID()
    {        
        return this.itemID;
    }//END OF METHOD getItemID
            
    //START OF METHOD setOrderID
    public void setOrderID(int orderID)
    {
        this.orderID = orderID;
    }//END OF METHOD setOrderID
       
    //START OF METHOD getorderID
    public int getorderID()
    {        
        return this.orderID;
    }//END OF METHOD getorderID        
    
    //START OF METHOD setNutrient --- not currently used but may need in the future
    public void setNutrient(int index, Nutrient nutrients)
    {
        this.nutrients.add(index, nutrients);
    }//END OF METHOD setNutrient
    
    //START OF METHOD addNutrient --- not currently used but may need in the future
    public void addNutrient(Nutrient nutrients)
    {
        this.nutrients.add(nutrients);
    }//END OF METHOD addNutrient  
    
    //START OF METHOD getNutrient
    public Nutrient getNutrient(int index)
    {
        return this.nutrients.get(index);
    }//END OF METHOD getNutrient
    
    //START OF METHOD setNutrientsArray
    public void setNutrientsArray(ArrayList<Nutrient> nutrients)
    {
        this.nutrients = new ArrayList<>(nutrients);
    }//END OF METHOD setNutrientsArray
    
    //START OF METHOD getNutrientsArray
    public ArrayList<Nutrient> getNutrientsArray()
    {
        return this.nutrients;
    }//END OF METHOD setNutrientsArray
    
    //START OF METHOD getMenuNutrientsTotals to get the sum of each nutrientValue from 2 MenuItem objects
    public static MenuItem getMenuNutrientsTotals(MenuItem item1, MenuItem item2)
    {
        //Create new temporary MenuItem
        MenuItem totals = new MenuItem();   

        //Initialise variables that are not used in the calculation
        //Set itemCourse to "TOTALS" for the totals line (to be used in the .toString() method)
        totals.setItemCourse("TOTALS");
        totals.setItemName("");
        totals.setItemID(0);
        
        //If there is an item1 and item2
        if ((!item1.getItemCourse().isEmpty()) && (!item2.getItemCourse().isEmpty()))
        {
            //Count through the nutrients 
            for (int count = 0; count < 5; count++)
            {            
                //Set the totals nutrient values to item1 + item2
                totals.addNutrient(new Nutrient("",
                item1.getNutrient(count).getNutrientValue() +
                item2.getNutrient(count).getNutrientValue() ));                              
            }
        }
        //Else if there is only a food order
        else if (!item1.getItemCourse().isEmpty())
        {   
            //Set the totals nutrient values to item1 using the copy constructor
            totals = new MenuItem(item1);         
        }
        //Else if there is only a beverage order
        else
        {
            //Set the totals nutrient values to item2 using the copy constructor
            totals = new MenuItem(item2);            
        }
        //Return the totals MenuItem
        return totals;
    }//END OF METHOD getMenuNutrientsTotals
    
    //START OF OVERLOADED METHOD getMenuNutrientsTotals to get the MenuItem item1 values 
    //and modify the itemCourse, itemName and itemID variables for TOTALS display
    public static MenuItem getMenuNutrientsTotals(MenuItem item1)
    {
        //Create new temporary MenuItem
        MenuItem totals = new MenuItem(item1);   

        //Initialise variables that are not used in the calculation
        //Set itemCourse to "TOTALS" for the totals line (to be used in the .toString() method)
        totals.setItemCourse("TOTALS");
        totals.setItemName("");
        totals.setItemID(0);
                
        //Return the totals MenuItem
        return totals;
    }//END OF OVERLOADED METHOD getMenuNutrientsTotals
    
    //START OF OVERRIDDEN METHOD toString to convert the MenuItem into a string
    /**
     * The getNutrientValue() method is used here instead of the Nutrient toString() method 
     * because of the need to format the double variables differently for text output
     * (i.e. nutrients.get(0).getNutrientValue() needs 0 post-decimal point values
     * e.g. Energy = "717", whereas nutrients.get(1).getNutrientValue() to
     * nutrients.get(4).getNutrientValue() need 1 post-decimal point value
     * e.g. Protein = "15.6".    
     */
    @Override
    public String toString()
    {
        return String.format("%-12s%-58s%-16.0f%-16.1f%-42.1f%-18.1f%-16.1f\n", 
                getItemCourse(), getItemName(), 
                nutrients.get(0).getNutrientValue(),
                nutrients.get(1).getNutrientValue(),
                nutrients.get(2).getNutrientValue(),
                nutrients.get(3).getNutrientValue(),
                nutrients.get(4).getNutrientValue());
    }//END OF OVERRIDDEN METHOD toString
        
}//END OF CLASS MenuItem
