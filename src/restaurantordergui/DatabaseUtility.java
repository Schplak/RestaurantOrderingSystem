/**
 * DatabaseUtility.java
 * DatabaseUtility class used for creating, accessing and manipulating the database
 * @author Peter McBurney
 * CQU Student number: Q9707351  
 * @version 1.0
 * @since 10-05-2018
 */
package restaurantordergui;


import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JOptionPane; //Provides JOptionPane class (GUI component)


//START OF CLASS DatabaseUtility
public class DatabaseUtility
{	
    //Declare database administration constants
    private final String SERVER_URL = "jdbc:mysql://localhost:3306"; 
    private final String DB_NAME = "Restaurant";
    private final String DB_PARAMETERS = "?autoReconnect=true&useSSL=false";
    private final String DB_URL = SERVER_URL + "/" + DB_NAME + DB_PARAMETERS; 
    
    private final String USERNAME = "root";
    private final String PASSWORD = "admin";

    //Declare database query variables
    private PreparedStatement selectAllOrderedCustomers;
    private PreparedStatement selectAllOrderedMenuItems;
    
    private PreparedStatement selectAllOrders;
    
    private PreparedStatement addNewMenuItem;
    private PreparedStatement addNewNutrient;
    private PreparedStatement addNewOrderedCustomer;
    private PreparedStatement addNewOrderedMenuItem;
    private PreparedStatement changeOrderedMenuItemStatus;
    private PreparedStatement createMenuItemTable;
    private PreparedStatement createNutrientTable;
    private PreparedStatement createOrderedCustomerTable;
    private PreparedStatement createOrderedMenuItemTable;
    private final String CREATE_DB_QUERY;
    private Statement statement;

    //Declare database initiation variables
    private Connection dbConnection, serverConnection;
    
    private ResultSet queryResults;

    private boolean newDatabase;
    
    private int numberOfTries;

    private int orderCount;
	
    //START OF CONSTRUCTOR
    public DatabaseUtility()
    {
        //Set string used in create database query
        CREATE_DB_QUERY = new String("CREATE DATABASE " + DB_NAME); 
        //set numberOfTries to 0, representing number of attempts to connect/create db
        numberOfTries = 0;
        //boolean is true if a new database was created
        newDatabase = false;
        //Variable to count number of orders when copying from database
        orderCount = 0;
        
        
    }//END OF CONSTRUCTOR


    //START OF METHOD initiateDatabase
    public void initiateDatabase()
    {
        
        System.out.println("DATABASE: Connecting to database " + DB_URL);
        try
        {
            System.out.print("DATABASE: ");
            
            //Connect to the database if possible
            dbConnection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            
            System.out.println("Connected");
                                   

            selectAllOrders = dbConnection.prepareStatement( //TO DELETE
                    "SELECT customerName, tableNumber, orderDate, " +
                    "menuItemID, orderedMenuItem.orderID, status " +                     
                    "FROM orderedMenuItem " +
                    "INNER JOIN orderedcustomer " +
                    "ON orderedMenuItem.orderID=orderedCustomer.orderID;");         
            
            selectAllOrderedCustomers = dbConnection.prepareStatement(
                    "SELECT * FROM orderedCustomer");

            selectAllOrderedMenuItems = dbConnection.prepareStatement(
                    "SELECT * FROM orderedMenuItem");
            
            
            addNewMenuItem = dbConnection.prepareStatement(
                    "INSERT INTO menuItem " +
                    "(menuItemID, menuItemName, itemCourse, mealType) " +
                    "VALUES (?, ?, ?, ?)");

            addNewNutrient = dbConnection.prepareStatement(
                    "INSERT INTO nutrient " +
                    "(menuItemID, nutrientName, nutrientValue) " +
                    "VALUES (?, ?, ?)");

            addNewOrderedCustomer = dbConnection.prepareStatement(
                    "INSERT INTO orderedCustomer " +
                    "(orderID, customerName, orderDate, tableNumber) " +
                    "VALUES (?, ?, ?, ?)");

            addNewOrderedMenuItem = dbConnection.prepareStatement(
                    "INSERT INTO orderedMenuItem " +
                    "(orderID, menuItemID, status) " +
                    "VALUES (?, ?, ?)");

            changeOrderedMenuItemStatus = dbConnection.prepareStatement(
                    "UPDATE orderedMenuItem " +
                    "SET status=? " +
                    "WHERE orderID=? AND menuItemID=?");

            
            createMenuItemTable = dbConnection.prepareStatement(
                    "CREATE TABLE menuItem " +
                    "( menuItemID SMALLINT UNSIGNED NOT NULL, " + 
                    "menuItemName VARCHAR (60) NOT NULL, " +
                    "itemCourse VARCHAR (20) NOT NULL, " +
                    "mealType VARCHAR (20) NOT NULL, " +
                    "PRIMARY KEY (menuItemID) )");

            createNutrientTable = dbConnection.prepareStatement(
                    "CREATE TABLE nutrient " +
                    "( nutrientID SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT, " + 
                    "menuItemID SMALLINT UNSIGNED NOT NULL, " +
                    "nutrientName VARCHAR (30) NOT NULL, " +
                    "nutrientValue DOUBLE UNSIGNED NOT NULL, " +
                    "PRIMARY KEY (nutrientID, menuItemID) )");

            createOrderedCustomerTable = dbConnection.prepareStatement(
                    "CREATE TABLE orderedCustomer " +
                    "( orderID SMALLINT UNSIGNED NOT NULL, " + 
                    "customerName VARCHAR (30) NOT NULL, " +
                    "orderDate DATE, " +
                    "tableNumber SMALLINT UNSIGNED NOT NULL, " +
                    "PRIMARY KEY (orderID) )");

            createOrderedMenuItemTable = dbConnection.prepareStatement(
                    "CREATE TABLE orderedMenuItem " +
                    "( orderID SMALLINT UNSIGNED NOT NULL, " + 
                    "menuItemID SMALLINT UNSIGNED NOT NULL, " +		
                    "status VARCHAR (20) NOT NULL, " +
                    "PRIMARY KEY (orderID, menuItemID) )");    
            
            //If there was a new database created, also create the associated tables
            if (newDatabaseCreated())
            {
                createDatabaseTables();
                System.out.println("DATABASE: Tables created");
            }                       
        }
        catch (SQLException e)
        {
            
            System.out.println("Not found!");

            System.out.print("MYSQL SERVER: ");            
            try
            {
                //Connect to the MySQL server if possible
                serverConnection = DriverManager.getConnection(SERVER_URL + DB_PARAMETERS, USERNAME, PASSWORD);
                statement = serverConnection.createStatement();
                System.out.println("Connected");                
                
                //Create database
                statement.executeUpdate(CREATE_DB_QUERY);
                newDatabase = true;
                System.out.println("MYSQL SERVER: Database \"" + DB_NAME + "\" created");   
                
                //Close the server connections
                serverConnection.close();
                statement.close();
                System.out.println("MYSQL SERVER: Disconnected");
                
                //Recursively call initiateDatabase and attempt up to 3 times to connect/create database
                if (numberOfTries < 3)
                {    
                    numberOfTries++;
                    initiateDatabase();                
                }                               
            }
            catch (SQLException e2)
            {
                System.out.println("Not found!");
                System.exit(0);
            }           
        }          
    }    
    //END OF METHOD initiateDatabase

    
    //START OF METHOD disconnectDatabase
    public void disconnectDatabase()
    {
        try
        {
            if (dbConnection != null)
            {
                dbConnection.close();
                System.out.println("DATABASE: Disconnected");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }//END OF METHOD disconnectDatabase
       
    
    //START OF METHOD newDatabaseCreated
    public boolean newDatabaseCreated()
    {
        return newDatabase;
    }
    //END OF METHOD newDatabaseCreated
    

    //START OF METHOD createDatabaseTables
    public void createDatabaseTables()
    {
        try
        {
            createMenuItemTable.executeUpdate();
            createNutrientTable.executeUpdate();
            createOrderedCustomerTable.executeUpdate();
            createOrderedMenuItemTable.executeUpdate();        
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }//END OF METHOD createDatabaseTables
    
    
    
    
    
  
    
    
    
    
    
    

            

            
    //START OF METHOD copyOrdersFromDatabase
    public ArrayList<OrderedCustomer> copyOrdersFromDatabase(ArrayList<MenuItem> menuItems) 
    {
        //Declare and initialise queue and result variables
        ArrayList<OrderedCustomer> customers = new ArrayList<OrderedCustomer>();
        ResultSet ordersResultSet = null;
        


        
        try
        {
            //Execute the select query
            ordersResultSet = selectAllOrders.executeQuery();
        
            
            //While there is another row in results
            while (ordersResultSet.next())
            {
                //Read variables from database
                String customerName = ordersResultSet.getString("customerName");
                int tableNumber = ordersResultSet.getInt("tableNumber");
                Date orderDate = ordersResultSet.getDate("orderDate");
                int menuItemID = ordersResultSet.getInt("menuItemID");
                int orderID = ordersResultSet.getInt("orderID");;
                String status = ordersResultSet.getString("status");; 
                
                //Initialise tempMenuItem
                MenuItem tempMenuItem = new MenuItem(getMenuItem(menuItemID, menuItems));
                
                //Initialise tempCustomer                
                OrderedCustomer tempCustomer = 
                        new OrderedCustomer(customerName, tableNumber, orderDate);
                
                //If the ordered menu item is food
                if (tempMenuItem.getItemCourse().equals("Food"))                   
                {
   
                    //Set the foodOrdered menuItem in tempCustomer
                    tempCustomer.setFoodOrdered(tempMenuItem);
                                        
                    //set menu item orderID
                    tempCustomer.getFoodOrdered().setOrderID(orderID); 
                    
                    //set order status
                    tempCustomer.setFoodOrderedStatus(status); 
                                        
                    //add a new OrderedCustomer to the queue
                    customers.add(tempCustomer);
                    
                }

                //ordersResultSet.next();
                
                //If the ordered menu item is beverage  
                if (tempMenuItem.getItemCourse().equals("Beverage"))
                {

                    //Set the foodOrdered menuItem in tempCustomer
                    tempCustomer.setBeverageOrdered(tempMenuItem);

                    //set menu item orderID
                    tempCustomer.getBeverageOrdered().setOrderID(orderID); 

                    //set order status
                    tempCustomer.setBeverageOrderedStatus(status); 

                    //add a new OrderedCustomer to the queue
                    customers.add(tempCustomer);
 
                }
     
                
                
                
                
                RestaurantOrderGUI.displayTest(tempCustomer);
            } 
                        
                        
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        } 
        
        
        
        
        return customers;
    }
    //END OF METHOD copyOrdersFromDatabase
            
            
            
    //START OF METHOD getMenuItem
    private MenuItem getMenuItem(int tempMenuItemID, ArrayList<MenuItem> menuItems)
    {
        //Count through menuItems
        for (int count = 0; count <= menuItems.size()-1; count++)
        {
            if (menuItems.get(count).getItemID() == tempMenuItemID)
                return menuItems.get(count);
        }
        return null;
    }          
    //END OF METHOD getMenuItem
            
            
    //START OF METHOD getOrderCount
    public int getOrderCount()
    {
        return orderCount;       
    }//END OF METHOD getOrderCount
            
            

            
    //START OF METHOD addMenuItem
    public void addMenuItem(int menuItemID, String itemName, String course, String mealType)
    {
        try
        {
            //Set variables in query to arguments   
            addNewMenuItem.setInt(1, menuItemID);
            addNewMenuItem.setString(2, itemName);
            addNewMenuItem.setString(3, course);
            addNewMenuItem.setString(4, mealType);
            //Execute query
            addNewMenuItem.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    //END OF METHOD addMenuItem
            
            
    //START OF METHOD addNutrient
    public void addNutrient(int menuItemID, String name, double value)
    {
        try
        {
            //Set variables in query to arguments
            addNewNutrient.setInt(1, menuItemID);
            addNewNutrient.setString(2, name);
            addNewNutrient.setDouble(3, value);                    
            //Execute query
            addNewNutrient.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    //END OF METHOD addNutrient


    //START OF METHOD addOrderedCustomer
    public void addOrderedCustomer(int orderID, String custName, Date date, int tableNo)
    {
        try
        {
            //Set variables in query to arguments
            addNewOrderedCustomer.setInt(1, orderID);
            addNewOrderedCustomer.setString(2, custName);        
            addNewOrderedCustomer.setDate(3, date);
            addNewOrderedCustomer.setInt(4, tableNo);
            //Execute query
            addNewOrderedCustomer.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    //END OF METHOD addOrderedCustomer
            

    //START OF METHOD addOrderedMenuItem
    public void addOrderedMenuItem(int orderID, int menuItemID, String status)
    {
        try
        {
            //Set variables in query to arguments
            addNewOrderedMenuItem.setInt(1, orderID);
            addNewOrderedMenuItem.setInt(2, menuItemID); 
            addNewOrderedMenuItem.setString(3, status);
            //Execute query
            addNewOrderedMenuItem.executeUpdate();                            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    //END OF METHOD addOrderedMenuItem
            

    //START OF METHOD setOrderedMenuItemStatus
    public void setOrderedMenuItemStatus(int orderID, int menuItemID, String status)
    {
        try
        {
            //Set variables in query to arguments
            changeOrderedMenuItemStatus.setString(1, status);
            changeOrderedMenuItemStatus.setInt(2, orderID);
            changeOrderedMenuItemStatus.setInt(3, menuItemID);
            //Execute query
            changeOrderedMenuItemStatus.executeUpdate();                   
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    //END OF METHOD setOrderedMenuItemStatus
            
 

}