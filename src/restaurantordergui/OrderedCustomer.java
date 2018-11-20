/**
 * OrderedCustomer.java
 * OrderedCustomer class used for storing customer details and orders
 * @author Peter McBurney
 * CQU Student number: Q9707351  
 * @version 2.0
 * @since 23-04-2018
 */
package restaurantordergui;

//START OF CLASS OrderedCustomer

import java.sql.Date;

public class OrderedCustomer extends Customer {
    
    //MenuItem variables used to store details of the food and beverage MenuItem ordered
    private MenuItem foodOrdered;
    private MenuItem beverageOrdered;    
    //boolean variables to store if a customer ordered food, and if a customer ordered a beverage
    private boolean foodBeenOrdered;
    private boolean beverageBeenOrdered;
    
    //enums recording order status for food and beverage - "waiting", "served", or "billed"
    public static enum OrderStatus {WAITING, SERVED, BILLED};  
    private OrderStatus foodOrderedStatus, beverageOrderedStatus;
    
    //Date variable
    private Date orderDate;
    
    
 
    
    
    
    //START OF DEFAULT CONSTRUCTOR
    public OrderedCustomer()
    {       
        //Initialise isOrdered variables to false, as no food or beverage has been ordered at this point
        foodBeenOrdered = false;
        beverageBeenOrdered = false;
        //Initialise the MenuItem variables
        foodOrdered = new MenuItem();
        beverageOrdered = new MenuItem();
        foodOrderedStatus = foodOrderedStatus.WAITING;
        beverageOrderedStatus = beverageOrderedStatus.WAITING;
    }//END OF DEFAULT CONSTRUCTOR
    
    //START OF PARAMETERISED CONSTRUCTOR 1
    public OrderedCustomer(String firstName, int tableNo, Date orderDate)
    {
        //Set the name and table number    
        super(firstName, tableNo);
        this.orderDate = orderDate;
        //Initialise isOrdered variables to false, as no food or beverage has been ordered at this point
        foodBeenOrdered = false;
        beverageBeenOrdered = false;
        //Initialise the MenuItem variables
        foodOrdered = new MenuItem();
        beverageOrdered = new MenuItem(); 
        foodOrderedStatus = foodOrderedStatus.WAITING;
        beverageOrderedStatus = beverageOrderedStatus.WAITING;
    }//END OF PARAMETERISED CONSTRUCTOR 1

    //START OF PARAMETERISED CONSTRUCTOR 2
    public OrderedCustomer(String firstName, int tableNo, Date orderDate, MenuItem foodOrdered, MenuItem beverageOrdered)
    {
        //Set the name and table number
        super(firstName, tableNo);   
        this.orderDate = orderDate;
        //Initialise isOrdered variables to true, as both food and beverage has been ordered
        foodBeenOrdered = true;
        beverageBeenOrdered = true;                  
        //Set the foodOrdered variable to the foodOrdered MenuItem input
        this.foodOrdered = new MenuItem(foodOrdered);
        //Set the beverageOrdered variable to the beverageOrdered MenuItem input
        this.beverageOrdered = new MenuItem(beverageOrdered);
        foodOrderedStatus = foodOrderedStatus.WAITING;
        beverageOrderedStatus = beverageOrderedStatus.WAITING;
    }//END OF PARAMETERISED CONSTRUCTOR 2
 
    //START OF METHOD setFoodOrdered
    public void setFoodOrdered(MenuItem foodOrdered)
    {
        this.foodOrdered = new MenuItem(foodOrdered);
        foodBeenOrdered = true;
    }//END OF METHOD setFoodOrdered
    
    //START OF METHOD setBeverageOrdered
    public void setBeverageOrdered(MenuItem beverageOrdered)
    {
        this.beverageOrdered = new MenuItem(beverageOrdered);
        beverageBeenOrdered = true;
    }//END OF METHOD setBeverageOrdered
    
    //START OF METHOD getFoodOrdered
    public MenuItem getFoodOrdered()
    {
        return this.foodOrdered;
    }//END OF METHOD getFoodOrdered
    
    //START OF METHOD getBeverageOrdered
    public MenuItem getBeverageOrdered()
    {
        return this.beverageOrdered;
    }//END OF METHOD getBeverageOrdered
    
    //START OF METHOD isFoodOrdered
    public boolean isFoodOrdered()
    {
        return foodBeenOrdered;
    }//END OF METHOD isFoodOrdered
    
    //START OF METHOD isbeverageOrdered
    public boolean isBeverageOrdered()
    {
        return beverageBeenOrdered;
    }//END OF METHOD isbeverageOrdered
   
    //START OF METHOD getFoodOrderedStatus
    public OrderStatus getFoodOrderedStatus()
    {  
        return this.foodOrderedStatus;
    }//END OF METHOD getFoodOrderedStatus
    
    //START OF METHOD setFoodOrderedStatus
    public void setFoodOrderedStatus(OrderStatus foodOrderedStatus)
    {  
        this.foodOrderedStatus = foodOrderedStatus;    
    }//END OF METHOD setFoodOrderedStatus
    
    //START OF OVERRIDDEN METHOD setFoodOrderedStatus
    public void setFoodOrderedStatus(String orderedStatus)
    {  
        switch (orderedStatus)
        {
            case "Waiting": foodOrderedStatus = OrderStatus.WAITING; 
                break;
            case "Served": foodOrderedStatus = OrderStatus.SERVED; 
                break;
            case "Billed": foodOrderedStatus = OrderStatus.BILLED; 
                break;
        }      
    }//END OF METHOD setFoodOrderedStatus
    
    //START OF METHOD getBeverageOrderedStatus
    public OrderStatus getBeverageOrderedStatus()
    {  
        return this.beverageOrderedStatus;
    }//END OF METHOD getBeverageOrderedStatus
    
    //START OF METHOD setBeverageOrderedStatus
    public void setBeverageOrderedStatus(OrderStatus beverageOrderedStatus)
    {         
        this.beverageOrderedStatus = beverageOrderedStatus;       
    }//END OF METHOD setBeverageOrderedStatus        
     
    //START OF OVERRIDDEN METHOD setBeverageOrderedStatus
    public void setBeverageOrderedStatus(String orderedStatus)
    {  
        switch (orderedStatus)
        {
            case "Waiting": beverageOrderedStatus = OrderStatus.WAITING; 
                break;
            case "Served": beverageOrderedStatus = OrderStatus.SERVED; 
                break;
            case "Billed": beverageOrderedStatus = OrderStatus.BILLED; 
                break;
        }      
    }//END OF METHOD setBeverageOrderedStatus
    
    //START OF METHOD returnOrderStatus
    public String returnOrderStatus(OrderStatus orderStatus)
    {
        String tempString = new String();
        switch (orderStatus)
        {
            case WAITING: tempString = "Waiting";
                break;
            case SERVED: tempString = "Served";
                break;
            case BILLED: tempString = "Billed";
                break;
        }  
        return tempString;
    }
    //END OF METHOD returnOrderStatus
    
    
    //START OF OVERRIDDEN METHOD toString to convert OrderedCustomer values to a string for display
    /**
     * Only getName() is used in this toString method because the other variables are displayed in 
     * the RestaurantOrderGUI method displayMenuItem()
     */
    @Override
    public String toString() 
    {
        return String.format("%s%d%s%s\n", "TABLE: ", getTableNo(), "   NAME: ", getName());
    }//END OF OVERRIDDEN METHOD toString
     
}//END OF CLASS OrderedCustomer
