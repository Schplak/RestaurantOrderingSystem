/**
 * Customer.java
 * Customer class used to store customer name and table number
 * @author Peter McBurney
 * CQU Student number: Q9707351  
 * @version 1.0
 * @since 26-03-2018
 */
package restaurantordergui;

//START OF CLASS Customer
public class Customer {
        
    //Declare instance variables for customer name and table number
    private String firstName;
    private int tableNo;
    
    //Declare and initialise constant for Maximum number of characters in firstName
    public static final int MAX_CHAR_WIDTH = 20;    
    //Declare and initialise constant for maximum table number
    public static final int MAX_TABLE_NO = 8;
        
    //START OF DEFAULT CONSTRUCTOR
    public Customer()
    {
        firstName = new String();
        tableNo = 0;                    
    }//END OF DEFAULT CONSTRUCTOR
    
    //START OF PARAMETERISED CONSTRUCTOR
    public Customer(String firstName, int tableNo)
    {
        this.firstName = new String(firstName);
        this.tableNo = tableNo;                    
    }//END OF PARAMETERISED CONSTRUCTOR
     
    //START OF METHOD setName used to set the customer name for the Customer class instance
    public void setName(String firstName)
    {
        //check to make sure firstName entered is within length bounds
        if (firstName.length() > 0 && firstName.length() < MAX_CHAR_WIDTH)
        {
            this.firstName = new String(firstName);
        } 
        //If firstName is invalid, throw an exception
        else
        {
            throw new IllegalArgumentException("First name entered is not within bounds.");
        }
    }//END OF METHOD setName
    
    //START OF METHOD setTableNo used to set the table number for the Customer class instance
    public void setTableNo(int tableNo)
    {
        //check to make sure tableNo entered is within bounds
        if (tableNo >= 1 && tableNo <= MAX_TABLE_NO)
        {
            this.tableNo = tableNo;
        } 
        //If tableNo is invalid, throw an exception
        else
        {
            throw new IllegalArgumentException("Table number is not within bounds.");
        }
    }//END OF METHOD setTableNo
    
    //START OF METHOD getName used to get the customer name for the Customer class instance
    public String getName()
    {
        return this.firstName;
    }//END OF METHOD getName
    
    //START OF METHOD getTableNo used to get the table number for the Customer class instance
    public int getTableNo()
    {
        return this.tableNo;
    }//END OF METHOD getTableNo
    
    //START OF OVERRIDDEN METHOD toString to convert Customer values to a string for display
    @Override
    public String toString()
    {
        return String.format("%s %d",getName(), getTableNo());
    }//END OF OVERRIDDEN METHOD toString
    
}//END OF CLASS Customer
