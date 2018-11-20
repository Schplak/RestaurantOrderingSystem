/**
 * RestaurantOrderGUI.java
 * RestaurantOrderGUI driver class for GUI and main method
 * @author Peter McBurney
 * CQU Student number: Q9707351  
 * @version 2.0
 * @since 23-04-2018
 * 
 * Project started: 23-04-2018
 * 
 */
package restaurantordergui;

//Import declarations
import java.util.ArrayList; //Provides ArrayList class for data storage
import java.util.Scanner; //Provides Scanner class for file/keyboard input
import java.io.IOException; //Provides input/output exceptions to handle errors
import java.nio.file.Paths; //Provides ability to catch IOexception for input/output
import javax.swing.BoxLayout; //Provides BoxLayout layout manager
import java.awt.FlowLayout; //Provides FlowLayout layout manager
import javax.swing.JFrame; //Provides JFrame class
import javax.swing.JTextField; //Provides JTextField class (GUI component)
import javax.swing.JLabel; //Provides JLabel class (GUI component)
import javax.swing.JComboBox; //Provides JComboBox class (GUI component)
import javax.swing.JPanel; //Provides JPanel class (GUI component)
import javax.swing.JTextArea; //Provides JTextArea class (GUI component)
import javax.swing.JButton; //Provides JButton class (GUI component)
import javax.swing.JOptionPane; //Provides JOptionPane class (GUI component)
import javax.swing.JScrollPane; //Provides JScrollPane class (GUI component)
import javax.swing.BorderFactory; //Provides GUI graphical border component
import javax.swing.JRadioButton; //Provides the JRadioButton class (GUI component)
import javax.swing.ButtonGroup; //Provides ButtonGroup class for JRadioButton grouping
import javax.swing.JList; //Provides the JList class for the waiting and served lists
import java.awt.Dimension; //Provides Dimension class to specify JPanel sizes
import java.awt.Font; //Provides ability to change fonts
import java.awt.event.ActionEvent; //Provides ActionEvent class for GUI events
import java.awt.event.ActionListener; //Provides ActionListener class to listen for GUI events
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import static javax.swing.ListSelectionModel.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;



//START OF CLASS RestaurantOrderGUI
public class RestaurantOrderGUI extends JFrame
{    
    //Declare and initialise main window size constants
    private static final int MAIN_WINDOW_WIDTH = 1330;
    private static final int MAIN_WINDOW_HEIGHT = 870;
    private static final int TOP_PANEL1_WIDTH = 1300;
    private static final int TOP_PANEL1_HEIGHT = 90;
    private static final int TOP_PANEL2_WIDTH = 1300;
    private static final int TOP_PANEL2_HEIGHT = 90;
    private static final int TOP_PANEL3_WIDTH = 1300;
    private static final int TOP_PANEL3_HEIGHT = 200;   
    private static final int MIDDLE_PANEL_WIDTH = 1300;
    private static final int MIDDLE_PANEL_HEIGHT = 370;
    private static final int BOTTOM_PANEL_WIDTH = 1300;
    private static final int BOTTOM_PANEL_HEIGHT = 70;    
    
    //Declare and initialise other size constants
    private static final int TEXT_AREA_WIDTH = 180;
    private static final int TEXT_AREA_HEIGHT = 19;
    private static final int CUST_NAME_BOX_WIDTH = 15;
    private static final int TABLE_NO_BOX_WIDTH = 3;
    private static final int CUSTOMER_LIST_ROW_COUNT = 8;
    private static final int CUSTOMER_LIST_WIDTH = 380;
    private static final int CUSTOMER_LIST_HEIGHT = 15;  
    
    //Declare and initialise other misc. constants
    private static final int NO_SELECTION = -1;
    private static final String DATA_FILE_NAME = new String("Ass2Data.csv");
    
    //Declare ArrayLists/Arrays
    //Declare an ArrayList of MenuItem objects to store each item and its values in the menu
    public final ArrayList<MenuItem> menu = new ArrayList<>();   
    //Declare an ArrayList of OrderedCustomer objects to store each customer order details
    private final ArrayList<OrderedCustomer> customers = new ArrayList<>();    
    //String to store headings read from first line of CSV file
    private String[] headings = new String[9];        
       
    //Declare LinkedLists
    private final LinkedList<String> waitingCustomers = new LinkedList<>();
    private final LinkedList<String> servedCustomers = new LinkedList<>();
    private final LinkedList<String> billedCustomers = new LinkedList<>();
    
    //Declare and initialise variable used for assigning a unique orderID to an ordered menuItem
    private int orderCount = 0;
     
    //Declare database variables
    private DatabaseUtility database;
    private Date todaysDate;
    private ArrayList<OrderedCustomer> orderQueue;
    
    //Declare GUI components    
    private final JPanel topPanel1, topPanel2, topPanel3, middlePanel, bottomPanel,
                        waitingCustPanel, servedCustPanel, billedCustPanel;    
    private final JLabel custDetailsLabel, tableNoLabel, mealTypeLabel, 
                        menuChoicesLabel, beverageLabel;                       
    private final JTextField custNameBox, tableNoBox;
    private JComboBox<String> foodComboBox, beverageComboBox;
    private final JTextArea textInfoArea;
    private final JScrollPane scrollTextArea;
    private final JButton enterDataButton, displayChoicesButton, displayOrderButton, 
                        clearDisplayButton, quitButton, prepareButton, billButton;
    private final JRadioButton bfastButton, lunchButton, dinnerButton;
    private final ButtonGroup mealChoicesButtons;
    private final JList waitingList, servedList, billedList;
    
    //Declare and initialise error type constants for error messages 
    private static final int BLANK_NAME_AND_TABLENO_ERROR = 1;
    private static final int BLANK_NAME_ERROR = 2;    
    private static final int INCORRECT_TABLENO_ERROR = 3;
    private static final int NO_SELECTION_ERROR = 4;
    private static final int NAME_TOO_LONG_ERROR = 5;
    private static final int INVALID_NAME_ERROR = 6;
    private static final int NO_ORDER_ERROR = 7;   
    
    //Declare string constants for meal types and courses
    public static final String BREAKFAST = new String("Breakfast");
    public static final String LUNCH = new String("Lunch");
    public static final String DINNER = new String("Dinner");
    public static final String FOOD = new String("Food");
    public static final String BEVERAGE = new String("Beverage");
    
    //START OF DEFAULT CONSTRUCTOR
    public RestaurantOrderGUI()
    {
        //Set the Name of the main window
        super("Restaurant Ordering System");        
        //Set the RestaurantOrderGUI frame to box layout style
        setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS )); 
  
        //Initialise GUI components that can be initialised without reading from file
        topPanel1 = new JPanel();
        topPanel2 = new JPanel();
        topPanel3 = new JPanel();
        middlePanel = new JPanel();
        bottomPanel = new JPanel();       
        waitingCustPanel = new JPanel(); 
        servedCustPanel = new JPanel(); 
        billedCustPanel  = new JPanel(); 
                
        custDetailsLabel = new JLabel("Customer Name");
        custNameBox = new JTextField(CUST_NAME_BOX_WIDTH);
        tableNoLabel = new JLabel("          Table Number");
        tableNoBox = new JTextField("1",TABLE_NO_BOX_WIDTH);        
        mealTypeLabel = new JLabel("Meal:   ");
        bfastButton = new JRadioButton(BREAKFAST,false); 
        lunchButton = new JRadioButton(LUNCH,false); 
        dinnerButton = new JRadioButton(DINNER,false); 
        mealChoicesButtons = new ButtonGroup();
        //Add radio buttons to a button group to link them
        mealChoicesButtons.add(bfastButton);
        mealChoicesButtons.add(lunchButton);
        mealChoicesButtons.add(dinnerButton);               
        menuChoicesLabel = new JLabel("                      Food");        
        beverageLabel = new JLabel("          Beverage");
        
        //Initialise waitingList with data from waitingCustomers LinkedList
        waitingList = new JList(waitingCustomers.toArray());
        //Setup waitingList attributes
        waitingList.setVisibleRowCount(CUSTOMER_LIST_ROW_COUNT);
        waitingList.setFixedCellWidth(CUSTOMER_LIST_WIDTH);
        waitingList.setFixedCellHeight(CUSTOMER_LIST_HEIGHT);
        waitingList.setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
        
        //Initialise servedList with data from servedCustomers LinkedList
        servedList = new JList(servedCustomers.toArray());
        //Setup servedList attributes
        servedList.setVisibleRowCount(CUSTOMER_LIST_ROW_COUNT);
        servedList.setFixedCellWidth(CUSTOMER_LIST_WIDTH);
        servedList.setFixedCellHeight(CUSTOMER_LIST_HEIGHT);
        servedList.setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
                        
        //Initialise billedList with data from billedCustomers LinkedList
        billedList = new JList(billedCustomers.toArray());
        //Setup billedList attributes
        billedList.setVisibleRowCount(CUSTOMER_LIST_ROW_COUNT);
        billedList.setFixedCellWidth(CUSTOMER_LIST_WIDTH);
        billedList.setFixedCellHeight(CUSTOMER_LIST_HEIGHT);
        billedList.setSelectionMode(MULTIPLE_INTERVAL_SELECTION);
        
        
        textInfoArea = new JTextArea(TEXT_AREA_HEIGHT,TEXT_AREA_WIDTH);
        scrollTextArea = new JScrollPane(textInfoArea, 
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        enterDataButton = new JButton("Enter Data");
        displayChoicesButton = new JButton("Display Choices");
        prepareButton = new JButton("Prepare Order");
        billButton = new JButton("Bill Customer");
        displayOrderButton = new JButton("Display Order");
        clearDisplayButton = new JButton("Clear Display");
        quitButton = new JButton("Quit");
        
        
        //Initialise database variables
        database = new DatabaseUtility();
        //Set todaysDate to today as per system time
        todaysDate = new Date(System.currentTimeMillis());
        //Initialise queue for orders from the database
        orderQueue = new ArrayList<>();
        
    }//END OF DEFAULT CONSTRUCTOR
    
    
    //######## START OF SYSTEM IMPLEMENTATION METHODS ########
    //START OF METHOD ReadMenuFile used to open, read info and close file
    private void readMenuFile(String fileName)
    {               
        //Temporary Nutrient ArrayList to input into temporary MenuItem
        ArrayList<Nutrient> tempNutrientArray = new ArrayList();               
        //Try-with-resources statement to declare and initialise Scanner object to read from file
        try (Scanner fileScanner = new Scanner(Paths.get(fileName)))
        {                              
            //Read the first line of the file for the headings
            String readHeadings = fileScanner.nextLine();
            //Split readHeadings into individual headings
            headings = readHeadings.split(",");           
            //Temporary int, double and variables for file input validation
            int tempInt = 0;
            double tempDouble = 0;                       
            //While there is a next line
            while (fileScanner.hasNextLine())
            {
                //Read the next line in the file
                String readLine = fileScanner.nextLine();                               
                //Declare and initialise Scanner object for reading each value in a line
                Scanner lineScanner = new Scanner(readLine);                
                //Set scanner delimiter to separate values by ","
                lineScanner.useDelimiter(",");                 
                //Temporary MenuItem to input into MenuItem ArrayList
                MenuItem tempMenuItem = new MenuItem();                                              
                //Read each value in the line into tempMenuItem in the following format:
                //Item Course, Meal Type, Item Name, Nutrient value x 5, Item ID                
                tempMenuItem.setItemCourse(lineScanner.next());
                tempMenuItem.setMealType(lineScanner.next());
                tempMenuItem.setItemName(lineScanner.next());                          
                //Assign headings(names) and values to temporary Nutrient variable
                //count from 2 to 6 to skip some headings not needed
                for (int count = 0; count <= 4; count++)
                {
                    //Temporary Nutrient variable to input into temporary Nutrient ArrayList
                    Nutrient tempNutrient = new Nutrient();            
                    //Set the temporary nutrient name from headings String array
                    //count+3 due to the first 3 array elements not being nutrient headings
                    tempNutrient.setNutrientName(headings[count+3]);                   
                    //Read the next string from the lineScanner
                    String tempString = new String(lineScanner.next());                                                                               
                    //Set the temporary nutrient value read from file
                    //Input file validation - confirms the nutrient values read from the file are valid
                    //If values are not valid double values, throws NumberFormatException
                    tempNutrient.setNutrientValue(Double.parseDouble(tempString));                                                           
                    //Add the nutrient into the nutrient array
                    tempNutrientArray.add(tempNutrient); 
                }            
                //add temporary Nutrient array to temporary MenuItem
                tempMenuItem.setNutrientsArray(tempNutrientArray);
                //Clear temporary Nutrient ArrayList
                tempNutrientArray.clear();           
                //Read the next string from the lineScanner for itemID
                String tempString = new String(lineScanner.next());            
                //Check itemID is a valid int - throws NumberFormatException if not
                tempInt = Integer.parseInt(tempString);            
                //add item ID if its valid
                tempMenuItem.setItemID(tempInt);               
                //Add tempMenuItem into the main MenuItem ArrayList "menu"
                menu.add(tempMenuItem);
                //Close the Scanner input stream when finished reading the line
                lineScanner.close(); 
            }
            //Close the Scanner input file
            fileScanner.close();            
        }
        //Catch the input-output exception if file cannot be opened
        catch (IOException ioexception)
        {
            System.err.println("Error: cannot open file.");
            System.exit(1);
        }   
        //Catch the incorrect format exception if the file is in the incorrect format
        catch (NumberFormatException numberformatexception)
        {
            System.err.println("Error: incorrect file format.");
            System.exit(1);
        }        
    }//END OF METHOD readMenuFile
          
    //START OF METHOD displayGUI used to start the GUI ordering system
    private void displayGUI()
    {      
        //Initialise food and beverage combo boxes
        foodComboBox = new JComboBox<>();        
        beverageComboBox = new JComboBox<>();
                        
        //Set default combo box data to breakfast meals
        setComboBoxData(foodComboBox, BREAKFAST, FOOD);
        setComboBoxData(beverageComboBox, BREAKFAST, BEVERAGE);
        
        //Method to add components to topPanel1 and topPanel2 and setup
        setupTopPanels();
        //Method to add components to middlePanel and setup
        setupMiddlePanel();
        //Method to add components to bottomPanel and setup
        setupBottomPanel();   
        
        //Add the GUI panels to the main JFrame window
        add(topPanel1);
        add(topPanel2);
        add(topPanel3);
        add(middlePanel);
        add(bottomPanel);               
        
        //Make the window not resizeable
        setResizable(false);
        //Set exit on close to occur when the user presses the close button
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set the size of the main window
        setSize(MAIN_WINDOW_WIDTH, MAIN_WINDOW_HEIGHT);        
        //Center the window in the screen
        setLocationRelativeTo(null);        
        //Make the GUI window visible
        setVisible(true);    
    }//END OF METHOD displayGUI
    
    //START OF METHOD startMenuOrderingSystem used to initiate event handling and database for the GUI
    private void startMenuOrderingSystem()
    {                       
        //If a new database has been created, load menuItem and nutrient data into it
        if (database.newDatabaseCreated())
        {
            //count thru menuItems ArrayList
            for (int count = 0; count <= menu.size()-1; count++)
            {
                //add menuItem into menuItem table in database
                database.addMenuItem(
                        menu.get(count).getItemID(),
                        menu.get(count).getItemName(),
                        menu.get(count).getItemCourse(),
                        menu.get(count).getMealType());
                
                //count thru nutrients in each menuItem to add to database
                for (int count2 = 0; count2 <= menu.get(count).getNutrientsArray().size()-1; count2++)
                {
                    database.addNutrient(menu.get(count).getItemID(),
                            menu.get(count).getNutrient(count2).getNutrientName(),
                            menu.get(count).getNutrient(count2).getNutrientValue());
                }
            }
        }
        //Else if there is an existing database, read orders from database into variables
        else
        {
            //Copy the orders from the database into a queue
            orderQueue = database.copyOrdersFromDatabase(menu);

System.out.println("---------------------------------------------");             
System.out.println("ORDERS COPIED FROM DATABASE");            
System.out.println();    

displayTest(orderQueue);

            //Set orderCount to current number of orders in system
            orderCount = database.getOrderCount();
            
System.out.println("ORDERCOUNT: " + orderCount);
System.out.println(); 



            //go through array list 
            for (int x = 0; x < orderQueue.size(); x++)
            {
                //copy head of queue to ArrayList<OrderedCustomer> customers
                customers.add(orderQueue.get(x));
                
                //if there is a food menuItem ordered
                if (orderQueue.get(x).isFoodOrdered())
                {
                    //add the item to the appropriate list
                    switch (orderQueue.get(x).getFoodOrderedStatus())
                    {
                        case WAITING: waitingCustomers.add(getMenuItemListDisplayLine(
                                orderQueue.get(x), orderQueue.get(x).getFoodOrdered()));
                                break;
                        case SERVED: servedCustomers.add(getMenuItemListDisplayLine(
                                orderQueue.get(x), orderQueue.get(x).getFoodOrdered()));
                                break;
                        case BILLED: billedCustomers.add(getMenuItemListDisplayLine(
                                orderQueue.get(x), orderQueue.get(x).getFoodOrdered()));
                                break;                        
                    }                                        
                }
                //if there is a beverage menuItem ordered
                if (orderQueue.get(x).isBeverageOrdered())
                {
                    //add the item to the appropriate list
                    switch (orderQueue.get(x).getBeverageOrderedStatus())
                    {
                        case WAITING: waitingCustomers.add(getMenuItemListDisplayLine(
                                orderQueue.get(x), orderQueue.get(x).getBeverageOrdered()));
                                break;
                        case SERVED: servedCustomers.add(getMenuItemListDisplayLine(
                                orderQueue.get(x), orderQueue.get(x).getBeverageOrdered()));
                                break;
                        case BILLED: billedCustomers.add(getMenuItemListDisplayLine(
                                orderQueue.get(x), orderQueue.get(x).getBeverageOrdered()));
                                break;                        
                    }                                        
                }                
            }
               
            //Display lists in JLists
            waitingList.setListData(waitingCustomers.toArray());
            servedList.setListData(servedCustomers.toArray());
            billedList.setListData(billedCustomers.toArray());
        }
        
        
        
        
        //addEnterDataButtonEvent to add an action listener for the enter data button
        addEnterDataButtonEvent();
        
        //addDisplayChoicesButtonEvent to add an action listener for the display choices button
        addDisplayChoicesButtonEvent();            
        
        //addPrepareOrderButtonEvent to add an action listener for the prepare order button
        addPrepareOrderButtonEvent();
        
        //addBillButtonEvent to add an action listener for the bill button
        addBillButtonEvent();
        
        //addDisplayOrderButtonEvent to add an action listener for the display order button
        addDisplayOrderButtonEvent();
        
        //addClearDisplayButtonEvent to add an action listener for the clear display button
        addClearDisplayButtonEvent();
        
        //addQuitButtonEvent to add an action listener for the quit button
        addQuitButtonEvent();                  
                     
        //addCustNameBoxListener to listen for custNameBox text entered/deleted (for disabling of prepare and bill buttons)
        addCustNameBoxListener();
        
        //addBfastRadioListener to listen for user selection of meal type "Breakfast"
        addBfastRadioListener();
        
        //addLunchRadioListener to listen for user selection of meal type "Lunch"
        addLunchRadioListener();
        
        //addDinnerRadioListener to listen for user selection of meal type "Dinner"
        addDinnerRadioListener();
        
    }//END OF METHOD startMenuOrderingSystem
         
    //START OF METHOD displayError to display the particular message for the situation
    private void displayError(int errorType)
    {
        switch (errorType)
        {                        
            //Blank customer name and table number error message = 1
            case BLANK_NAME_AND_TABLENO_ERROR:
                JOptionPane.showMessageDialog(this, 
                        "Error - Customer Name cannot be blank and Table Number must be between 1 and " 
                                + Customer.MAX_TABLE_NO,
                        "Customer Input", JOptionPane.ERROR_MESSAGE);
                break;            
            //Blank customer name error message = 2
            case BLANK_NAME_ERROR:
                JOptionPane.showMessageDialog(this, "Error - Customer Name cannot be blank",
                        "Customer Input", JOptionPane.ERROR_MESSAGE);
                break;                   
            //Incorrect table number error message = 3
            case INCORRECT_TABLENO_ERROR:
                JOptionPane.showMessageDialog(this, 
                        "Error - Table Number must be a number between 1 and " + Customer.MAX_TABLE_NO,
                        "Customer Input", JOptionPane.ERROR_MESSAGE);
                break;           
            //No food or beverage selected error message = 4
            case NO_SELECTION_ERROR:
                JOptionPane.showMessageDialog(this, 
                        "Error - Food and beverage selection cannot both be <None>",
                        "Customer Input", JOptionPane.ERROR_MESSAGE);
                break;               
            //Customer name too long error message = 5
            case NAME_TOO_LONG_ERROR:
                JOptionPane.showMessageDialog(this, 
                        "Error - Customer name cannot be longer than " + Customer.MAX_CHAR_WIDTH
                        + " characters",
                        "Customer Input", JOptionPane.ERROR_MESSAGE);
                break;   
            //Customer name invalid message = 6
            case INVALID_NAME_ERROR:
                JOptionPane.showMessageDialog(this, 
                        "Error - Customer name must contain only letters",
                        "Customer Input", JOptionPane.ERROR_MESSAGE);
                break;      
            //No Customer order message = 7
            case NO_ORDER_ERROR:
                JOptionPane.showMessageDialog(this, 
                        "Error - There are no orders to display",
                        "Display Order", JOptionPane.ERROR_MESSAGE);
                break;                                                                  
        }    
    }//END OF METHOD displayError
    //######## END OF SYSTEM IMPLEMENTATION METHODS ########
    
   
    //######## START OF GUI PANEL SETUP METHODS ########
    //START OF METHOD setupTopPanels to add the components to topPanel1 and topPanel2
    private void setupTopPanels()
    {
        //SETUP OF TOPPANEL1
        //Set the layout of topPanel1 to FlowLayout
        topPanel1.setLayout(new FlowLayout());
        //Add the GUI components to topPanel1
        topPanel1.add(custDetailsLabel);
        topPanel1.add(custNameBox);
        topPanel1.add(tableNoLabel);
        topPanel1.add(tableNoBox);        
        //Set the maximum width and height of topPanel1
        topPanel1.setMaximumSize(new Dimension(TOP_PANEL1_WIDTH, TOP_PANEL1_HEIGHT));
        //Set the border style and panel title for topPanel1
        topPanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Customer Details"));
                
        //SETUP OF TOPPANEL2
        //Set the layout of topPanel2 to FlowLayout
        topPanel2.setLayout(new FlowLayout());
        //Add the GUI components to the top panel2
        topPanel2.add(mealTypeLabel);
        topPanel2.add(bfastButton);
        topPanel2.add(lunchButton);
        topPanel2.add(dinnerButton);       
        topPanel2.add(menuChoicesLabel);
        topPanel2.add(foodComboBox);
        topPanel2.add(beverageLabel);
        topPanel2.add(beverageComboBox);    
        //Set the breakfast button to default selected value
        bfastButton.setSelected(true);   
        //Set the maximum width and height of topPanel2
        topPanel2.setMaximumSize(new Dimension(TOP_PANEL2_WIDTH, TOP_PANEL2_HEIGHT));
        //Set the border style and panel title for topPanel2
        topPanel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Menu Choices"));
        
        //SETUP OF TOPPANEL3
        //Setup of sub-panels of TOPPANEL3 - set layouts, add JLists, set sizes, set borders
        waitingCustPanel.setLayout(new FlowLayout());
        servedCustPanel.setLayout(new FlowLayout());
        billedCustPanel.setLayout(new FlowLayout());
        
        //Add a scroll bar to each JList, and add each JList to a sub-panel
        waitingCustPanel.add(new JScrollPane(waitingList, 
                                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        servedCustPanel.add(new JScrollPane(servedList, 
                                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)); 
        billedCustPanel.add(new JScrollPane(billedList, 
                                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));  

        //Set borders for the sub-panels
        waitingCustPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Waiting Orders"));
        servedCustPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Served Orders"));
        billedCustPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Billed Orders"));        
                
        //Set the layout of topPanel3 to FlowLayout
        topPanel3.setLayout(new FlowLayout());
        //Add the GUI components to the top panel3              
        topPanel3.add(waitingCustPanel);
        topPanel3.add(servedCustPanel);
        topPanel3.add(billedCustPanel);       
        //Set the maximum width and height of topPanel3
        topPanel3.setMaximumSize(new Dimension(TOP_PANEL3_WIDTH, TOP_PANEL3_HEIGHT));
        //Set the border style and panel title for topPanel3
        topPanel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Customer Orders"));      
    }//END OF METHOD setupTopPanels
        
    //START OF METHOD setupMiddlePanel to add the components to middlePanel
    private void setupMiddlePanel()
    {       
        //Set the layout of middlePanel to FlowLayout
        middlePanel.setLayout(new FlowLayout());
        //Set the border style for the text area
        textInfoArea.setBorder(BorderFactory.createLoweredBevelBorder());
        //Make the text box uneditable
        textInfoArea.setEditable(false);
        //Set the text in the text box to monospaced to align columns more easily
        textInfoArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));  

        //Add the GUI text area component with scroll bar to the middle panel       
        middlePanel.add(scrollTextArea);
        //Set the maximum width and height of middlePanel
        middlePanel.setMaximumSize(new Dimension(MIDDLE_PANEL_WIDTH, MIDDLE_PANEL_HEIGHT));
        //Set the border style and panel title for middlePanel
        middlePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), 
                "Your Menu choices and Nutritional information"));    
    }//END OF METHOD setupMiddlePanel
        
    //START OF METHOD setupBottomPanel to add the components to bottomPanel
    private void setupBottomPanel()
    {       
        //Set the layout of middlePanel to FlowLayout
        bottomPanel.setLayout(new FlowLayout());        
        //Add the GUI components to the bottom panel            
        bottomPanel.add(enterDataButton);
        bottomPanel.add(displayChoicesButton);
        bottomPanel.add(prepareButton);
        bottomPanel.add(billButton);
        bottomPanel.add(displayOrderButton);
        bottomPanel.add(clearDisplayButton);
        bottomPanel.add(quitButton);       
        //Set the maximum width and height of middlePanel
        bottomPanel.setMaximumSize(new Dimension(BOTTOM_PANEL_WIDTH, BOTTOM_PANEL_HEIGHT));
        //Set the border style and panel title for middlePanel
        bottomPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Command Buttons"));
    }//END OF METHOD setupBottomPanel
    //######## END OF GUI PANEL SETUP METHODS ########
    
        
    //######## START OF BUTTON EVENT METHODS ########     
    //START OF METHOD addEnterDataButtonEvent to add an action listener for the enter data button
    private void addEnterDataButtonEvent()
    {
        //Add action listener and anonymous inner class to perform an action
        //Allow "enter data" button to input the data and selections into data structures
        enterDataButton.addActionListener(
            //Create anonymous inner class
            new ActionListener()
            {
                //Override ActionListener method to process event
                @Override
                public void actionPerformed(ActionEvent event)
                {
                    //Boolean to determine if data entered is valid. Set initially to true
                    boolean dataIsValid = true;                   
                    //Get the selected index of the selected combo box elements for food and beverage
                    int foodElement = (foodComboBox.getSelectedIndex()-1);
                    int beverageElement = (beverageComboBox.getSelectedIndex()-1); 
                    //Get the text entered in the customer name box
                    String customerName = custNameBox.getText();
                    //Declare and initialise int table number, to read text input in table number box
                    int tableNo = 0;                          
                    try
                    {                    
                        //Clears the text box
                        textInfoArea.setText(null);                       
                        //If customer name is empty
                        if (customerName.isEmpty())
                        {
                            displayError(BLANK_NAME_ERROR);
                            dataIsValid = false;
                        }                         
                        //If customer name is too long (i.e. length > maximum number of characters) display error
                        else if (customerName.length() > Customer.MAX_CHAR_WIDTH)
                        {                           
                            displayError(NAME_TOO_LONG_ERROR);
                            //Clear the invalid input
                            custNameBox.setText(null);
                            dataIsValid = false;
                        }                       
                        //If customer name has invalid characters, display error
                        else if (!customerName.matches("[A-Za-z ]+"))
                        {                           
                            displayError(INVALID_NAME_ERROR);
                            //Clear the invalid input
                            custNameBox.setText(null);
                            dataIsValid = false;
                        }
                        //If statement that checks if there are no selections (-1 = no selection)
                        else if ((foodElement == NO_SELECTION) && (beverageElement == NO_SELECTION))
                        {
                            //If there are no selections, display error
                            displayError(NO_SELECTION_ERROR);
                            dataIsValid = false;
                        }  
                        else
                        {    
                            //Read the table number text box input into an int - throws NumberFormatException
                            tableNo = Integer.parseInt(tableNoBox.getText()); //else here?                   
                            //If table number is outside bounds
                            if ((tableNo < 1) || (tableNo > Customer.MAX_TABLE_NO))
                            {
                                dataIsValid = false;
                                //Throw an exception
                                throw new NumberFormatException();
                            }
                        }
                    }
                    //Catch the NumberFormatException thrown when invalid data given to tableNo
                    catch (NumberFormatException numberformatexception)
                    {
                        //If table number entered is in an invalid format, display error
                        displayError(INCORRECT_TABLENO_ERROR);
                        //Clear the invalid input
                        tableNoBox.setText("1");
                        dataIsValid = false;
                    }                   
                    //If the data is valid, enter the values into data structures
                    if (dataIsValid)
                    {
                        //Increment orderCount to get a unique orderID
                        orderCount++;
                        
                        //Set the customer name and table number of the temporary OrderedCustomer object
                        OrderedCustomer tempOrder = new OrderedCustomer(customerName, tableNo, todaysDate);
                         //If statement that checks if food has been chosen
                        if (foodElement != NO_SELECTION) 
                        {
                            
                            //If food was chosen, set the tempOrder object with food element
                            tempOrder.setFoodOrdered(menu.get(getMenuItemNo(foodComboBox, FOOD)));                                                                                   
                            //Assign a unique orderID to the menuItem ordered
                            tempOrder.getFoodOrdered().setOrderID(orderCount);
                            //Set food ordered item to "waiting"
                            tempOrder.setFoodOrderedStatus(OrderedCustomer.OrderStatus.WAITING);
                            //Add item ordered to waitingCustomers LinkedList
                            waitingCustomers.add(getMenuItemListDisplayLine(tempOrder, tempOrder.getFoodOrdered()));  
                            
                            //Add the Ordered menuItem into the database
                            database.addOrderedMenuItem(orderCount, 
                                    tempOrder.getFoodOrdered().getItemID(), 
                                    tempOrder.returnOrderStatus(tempOrder.getFoodOrderedStatus()));                             
                        }
                        //Else if no food was chosen, set tempOrder foodOrdered.itemName to "None"
                        else
                        {
                            //Set the itemName variable to "None"
                            tempOrder.getFoodOrdered().setItemName("None");                            
                        }                                               
                        //If statement that checks if beverage has been chosen
                        if (beverageElement != NO_SELECTION)
                        {
                            
                            //If beverage was chosen, set the tempOrder object with beverage element
                            tempOrder.setBeverageOrdered(menu.get(getMenuItemNo(beverageComboBox, BEVERAGE)));                                                        
                            //Assign a unique orderID to the menuItem ordered
                            tempOrder.getBeverageOrdered().setOrderID(orderCount);
                            //Set beverage ordered item to "waiting"
                            tempOrder.setBeverageOrderedStatus(OrderedCustomer.OrderStatus.WAITING);
                            //Add item ordered to waitingCustomers LinkedList
                            waitingCustomers.add(getMenuItemListDisplayLine(tempOrder, tempOrder.getBeverageOrdered())); 
                            
                            //Add the Ordered menuItem into the database
                            database.addOrderedMenuItem(orderCount, 
                                    tempOrder.getBeverageOrdered().getItemID(), 
                                    tempOrder.returnOrderStatus(tempOrder.getBeverageOrderedStatus()));                                                 
                        }
                        //Else if no beverage was chosen, set tempOrder beverageOrdered.itemName to "None"
                        else
                        {
                            //Set the itemName variable to "None"
                            tempOrder.getBeverageOrdered().setItemName("None");                            
                        }                        
                                                                       
                        //Generate the order - add the OrderedCustomer tempOrder details into the customers ArrayList
                        customers.add(tempOrder);
                                              
//###                   SAVE TO DATABASE

                        //Add the OrderedCustomer order data into the database
                        database.addOrderedCustomer(orderCount, tempOrder.getName(), 
                                todaysDate, tempOrder.getTableNo());
                        
                        
//TESTING                        
                        
                        

                        //Set the waitingList JList data to the waitingCustomers LinkedList
                        waitingList.setListData(waitingCustomers.toArray());

                        //Display message dialog box with details entered
                        JOptionPane.showMessageDialog(null, 
                            "TABLE NUMBER: " + tableNo +
                            "\nCUSTOMER NAME: " + customerName +
                            "\nMEAL: " + tempOrder.getFoodOrdered().getMealType() +
                            "\nFOOD ORDERED: " +  tempOrder.getFoodOrdered().getItemName() +      
                            "\nBEVERAGE ORDERED: " + tempOrder.getBeverageOrdered().getItemName(),
                            "Customer order entered", 
                            JOptionPane.INFORMATION_MESSAGE);  
                        //reset the values of GUI components text boxes, combo boxes, and text area
                        resetValues();   
                    }                   
                }
            }
        );//end of enterDataButton action
    }//END OF METHOD addEnterDataButtonEvent
    
    //START OF METHOD addDisplayChoicesButtonEvent to add an action listener for the display choices button
    private void addDisplayChoicesButtonEvent()
    {
        //Add action listener and anonymous inner class to perform an action
        //Allow "display choices" button to display the choices in the textInfoArea text box
        displayChoicesButton.addActionListener(
            //Create anonymous inner class
            new ActionListener()
            {
                //Override ActionListener method to process event
                @Override
                public void actionPerformed(ActionEvent event)
                {                   
                    //Get the selected index of the selected combo box elements for food and beverage
                    int foodElement = (foodComboBox.getSelectedIndex()-1);
                    int beverageElement = (beverageComboBox.getSelectedIndex()-1); 
                    //Clears the text box ready for the display
                    textInfoArea.setText(null);                   
                    //If statement that checks if there are no selections (-1 = no selection)
                    if ((foodElement == NO_SELECTION) && (beverageElement == NO_SELECTION))
                    {
                        //If there are no selections, display error
                        displayError(NO_SELECTION_ERROR);
                    }
                    //Else continue with displaying the items
                    else
                    {                       
                        //Display the menu headings
                        displayMenuHeadings(textInfoArea, headings);                                                
                        //If there are both food and beverage elements selected
                        if ((foodElement != NO_SELECTION) && (beverageElement != NO_SELECTION))
                        {
                            //Display the food element
                            displayMenuItem(textInfoArea, menu.get(getMenuItemNo(foodComboBox, FOOD)));
                            //Display the beverage element
                            displayMenuItem(textInfoArea, menu.get(getMenuItemNo(beverageComboBox, BEVERAGE)));
                            //Display the totals
                            displayMenuTotal(textInfoArea, MenuItem.getMenuNutrientsTotals(
                                menu.get(getMenuItemNo(foodComboBox, FOOD)), menu.get(getMenuItemNo(beverageComboBox, BEVERAGE))));
                        }
                        //If there is only a food element selected
                        else if (foodElement != NO_SELECTION)
                        {
                            //Display the food element
                            displayMenuItem(textInfoArea, menu.get(getMenuItemNo(foodComboBox, FOOD)));
                            //Display the totals
                            displayMenuTotal(textInfoArea, MenuItem.getMenuNutrientsTotals(
                                menu.get(getMenuItemNo(foodComboBox, FOOD))));
                        }
                        //If there is only a beverage element selected
                        else if (beverageElement != NO_SELECTION)
                        {
                            //Display the beverage element
                            displayMenuItem(textInfoArea, menu.get(getMenuItemNo(beverageComboBox, BEVERAGE)));
                            //Display the totals
                            displayMenuTotal(textInfoArea, MenuItem.getMenuNutrientsTotals(
                                menu.get(getMenuItemNo(beverageComboBox, BEVERAGE))));                        
                        }
                    }            
                }
            }
        );//end of displayChoicesButton action
    }//END OF METHOD addDisplayChoicesButtonEvent
    
    
    
    
    
    //START OF METHOD addDisplayOrderButtonEvent to add an action listener for the display order button
    private void addDisplayOrderButtonEvent()
    {
        //Add action listener and anonymous inner class to perform an action
        //Allow "display order" button to display the table order in the textInfoArea text box
        displayOrderButton.addActionListener(
            //Create anonymous inner class
            new ActionListener()
            {
                //Override ActionListener method to process event
                @Override
                public void actionPerformed(ActionEvent event)
                {
                    //Reset the values of GUI components text boxes, combo boxes, and text area
                    resetValues();                   
                    try
                    {
                        //Declare and initialise stringInput to the input the user enters from the input dialog box
                        String stringInput = new String(JOptionPane.showInputDialog(null, 
                                "Enter table number", "User Input", JOptionPane.QUESTION_MESSAGE));
                        //Parse the String stringInput to the int variable userInput
                        int userInput = Integer.parseInt(stringInput);
                        //If table number is outside bounds
                        if ((userInput < 1) || (userInput > Customer.MAX_TABLE_NO))
                        {                            
                            //Throw an exception
                            throw new NumberFormatException();
                        } 
                        //If there are orders to display
                        if (tableOrderExists(customers, userInput))
                        {     
                            //Display the order with table number as userInput
                            displayTableOrder(textInfoArea, customers, userInput);
                            //Sets the caret position back to the top, to view from the top of the displayed data
                            textInfoArea.setCaretPosition(0);                           
                        }
                        else
                        {
                            //Display error stating that there are no orders for that table number
                            displayError(NO_ORDER_ERROR);
                        }     
                    }
                    //Catch the NumberFormatException thrown when invalid data given to tableNo
                    catch (NumberFormatException e)
                    {
                        //If table number entered is in an invalid format, display error
                        displayError(INCORRECT_TABLENO_ERROR);                        
                    }
                    //Catch the NullPointerException so that the cancel button of the dialog closes the dialog
                    catch (NullPointerException e){}                   
                }
            }
        );//end of displayOrderButton action
    }//END OF METHOD addDisplayOrderButtonEvent
        
    //START OF METHOD addClearDisplayButtonEvent to add an action listener for the clear display button
    private void addClearDisplayButtonEvent()
    {
        //Add action listener and anonymous inner class to perform an action
        //Allow "clear display" button to clear the textInfoArea text box
        clearDisplayButton.addActionListener(
            //Create anonymous inner class
            new ActionListener()
            {
                //Override ActionListener method to process event
                @Override
                public void actionPerformed(ActionEvent event)
                {
                    //Reset the GUI components values of text boxes, combo boxes and text area
                    resetValues();
                }
            }
        );//end of clearDisplayButton action
    }//END OF METHOD addClearDisplayButtonEvent
    
    //START OF METHOD addQuitButtonEvent to add an action listener for the quit button
    private void addQuitButtonEvent()
    {
        //Add action listener and anonymous inner class to perform an action
        //Allow "quit" button to exit program when button is clicked
        quitButton.addActionListener(
            //Create anonymous inner class
            new ActionListener()
            {
                //Override ActionListener method to process event
                @Override
                public void actionPerformed(ActionEvent event)
                {
                    //Disconnect from database if connected
                    database.disconnectDatabase();
                    //Exit program
                    System.exit(0);
                }
            }
        );//end of quitButton action
    }//END OF METHOD addQuitButtonEvent
    
    //START OF METHOD addPrepareOrderButtonEvent to add an action listener for the Prepare order button
    private void addPrepareOrderButtonEvent()
    {
        //Add action listener and anonymous inner class to perform an action
        //Allow "prepare" button to prepare an order when button is clicked
        prepareButton.addActionListener(
            //Create anonymous inner class
            new ActionListener()
            {
                //Override ActionListener method to process event
                @Override
                public void actionPerformed(ActionEvent event)
                {                       
                    swapSelectionsAndUpdateLists(waitingList, servedList, waitingCustomers, servedCustomers, 
                                                    OrderedCustomer.OrderStatus.SERVED);                  
                }
            }
        );//end of prepareButton action
    }//END OF METHOD addPrepareOrderButtonEvent
    
    //START OF METHOD addBillButtonEvent to add an action listener for the Bill button
    private void addBillButtonEvent()
    {
        //Add action listener and anonymous inner class to perform an action
        //Allow "bill" button to bill an order when button is clicked
        billButton.addActionListener(
            //Create anonymous inner class
            new ActionListener()
            {
                //Override ActionListener method to process event
                @Override
                public void actionPerformed(ActionEvent event)
                {     
                    
                    swapSelectionsAndUpdateLists(servedList, billedList, servedCustomers, billedCustomers, 
                                                    OrderedCustomer.OrderStatus.BILLED);
                }
            }
        );//end of billButton action
    }//END OF METHOD addBillButtonEvent
        
    
    //START OF METHOD addcustNameBoxListener() to add a text insert listener to custNameBox
    private void addCustNameBoxListener()
    {
        //Create new DocumentListener
        DocumentListener custNameBoxListener = new DocumentListener()
        {   
            //Override methods in abstract class DocumentListener
            @Override
            public void insertUpdate(DocumentEvent documentEvent) 
            {
                prepareButton.setEnabled(false);
                billButton.setEnabled(false);
            }           
            @Override
            public void removeUpdate(DocumentEvent documentEvent)                    
            {
                //Document variable to get the text length from the text box                
                Document doc = (Document)documentEvent.getDocument();
                if (doc.getLength() == 0)
                {
                    prepareButton.setEnabled(true);
                    billButton.setEnabled(true);
                }
            }            
            @Override
            public void changedUpdate(DocumentEvent documentEvent) {}
        };
        //Add the document listener to custNameBox
        custNameBox.getDocument().addDocumentListener(custNameBoxListener);  
    }//END OF METHOD addcustNameBoxListener()
    
    
    
    
    
    //START OF METHOD addBfastRadioListener to listen for a radio button selection
    private void addBfastRadioListener()
    {
        bfastButton.addItemListener(event -> 
        {
            if (event.getStateChange() == ItemEvent.SELECTED)
            {
                setComboBoxData(foodComboBox, BREAKFAST, FOOD);
                setComboBoxData(beverageComboBox, BREAKFAST, BEVERAGE);
            }
        });
    }
    //END OF METHOD addBfastRadioListener
    
    //START OF METHOD addLunchRadioListener to listen for a radio button selection
    private void addLunchRadioListener()
    {
        lunchButton.addItemListener(event -> 
        {
            if (event.getStateChange() == ItemEvent.SELECTED)
            {
                setComboBoxData(foodComboBox, LUNCH, FOOD);
                setComboBoxData(beverageComboBox, LUNCH, BEVERAGE);
            }
        });
    }
    //END OF METHOD addLunchRadioListener
    
    //START OF METHOD addDinnerRadioListener to listen for a radio button selection
    private void addDinnerRadioListener()
    {
        dinnerButton.addItemListener(event -> 
        {
            if (event.getStateChange() == ItemEvent.SELECTED)
            {
                setComboBoxData(foodComboBox, DINNER, FOOD);
                setComboBoxData(beverageComboBox, DINNER, BEVERAGE);
            }
        });
    }
    //END OF METHOD addDinnerRadioListener
    //######## END OF BUTTON EVENT METHODS ########  
    
         
    //######## START OF SET, GET AND DISPLAY ITEM METHODS ########
    //START OF METHOD setComboBoxData to set the data in the combo box specified
    private void setComboBoxData(JComboBox box, String mealType, String itemCourse)
    {
        //Remove all items from the combo box
        box.removeAllItems();
        //Add the String "<None>" to the box
        //to allow for customers that don't want a drink, or don't want food.        
       box.addItem("<None>" + 
               "                                            " +
               "                                            ");               
        //count through ArrayList menu to find menu items with the mealTypes and mealCoursesrequired
        for (int count = 0; count <= (menu.size()-1); count++)
        {       
            //If the mealType and itemCourse match, add to the tempArrayList
            if ((menu.get(count).getMealType().equals(mealType)) &&
            (menu.get(count).getItemCourse().equals(itemCourse)))
            {                
		box.addItem(menu.get(count).getItemName());
            }
        }        	        
    }//END OF METHOD setComboBoxData

    //START OF METHOD displayTableOrder to display the whole order for the table
    private void displayTableOrder(JTextArea textBox, ArrayList<OrderedCustomer> customer, int tableNumber)
    {
        //Display title with table number
        textBox.append(String.format("%s%d\n\n", "ORDER FOR TABLE NUMBER ", tableNumber));       
        //Count through the OrderedCustomer ArrayList
        for (int count = 0; count < customer.size(); count++)
        {
            //If there is an OrderedCustomer object with tableNo that equals the table number being searched for
            if (customer.get(count).getTableNo() == tableNumber)
            {
                //Display the OrderedCustomer customer order details
                textBox.append(String.format("%s%s\n", "CUSTOMER NAME: ", customer.get(count).getName()));
                //Display the OrderedCustomer customer order details
                if (customer.get(count).isFoodOrdered())
                    textBox.append(String.format("%s%s\n","MEAL: ", customer.get(count).getFoodOrdered().getMealType()));            
                else    
                    textBox.append(String.format("%s%s\n","MEAL: ", customer.get(count).getBeverageOrdered().getMealType()));
                //Display headings for the MenuItems selected for food and beverage
                displayMenuHeadings(textBox, headings);               
                //If both food and beverage have been ordered
                if ((customer.get(count).isFoodOrdered()) && (customer.get(count).isBeverageOrdered()))
                {
                    //Display the food MenuItem to the textBox
                    displayMenuItem(textBox, customer.get(count).getFoodOrdered());
                    //Display the beverage MenuItem to the textBox
                    displayMenuItem(textBox, customer.get(count).getBeverageOrdered());
                    //Display the totals to the textBox
                    displayMenuTotal(textBox, MenuItem.getMenuNutrientsTotals(
                            customer.get(count).getFoodOrdered(), 
                            customer.get(count).getBeverageOrdered()));
                }
                //Else if only food has been ordered
                else if (customer.get(count).isFoodOrdered())
                {
                    //Display the food MenuItem to the textBox
                    displayMenuItem(textBox, customer.get(count).getFoodOrdered());
                    //Display the total (which is only the food MenuItem) to the textBox
                    displayMenuTotal(textBox, MenuItem.getMenuNutrientsTotals(
                            customer.get(count).getFoodOrdered()));                                                
                }
                //Else only beverage must have been ordered 
                //(since both food and beverage cannot be "<None>")
                else
                {
                    //Display the beverage MenuItem to the textBox
                    displayMenuItem(textBox, customer.get(count).getBeverageOrdered());
                    //Display the total (which is only the food MenuItem) to the textBox
                    displayMenuTotal(textBox, MenuItem.getMenuNutrientsTotals(
                            customer.get(count).getBeverageOrdered()));
                }
            }
        }       
    }//END OF METHOD displayTableOrder

    //START OF METHOD displayMenuItem to display the details of the menu menuItem
    private void displayMenuItem(JTextArea textBox, MenuItem menu)
    {
        //Use the .toString() MenuItem method to append the text to the text box
        textBox.append(menu.toString());
    }//END OF METHOD displayMenuItem
        
    //START OF METHOD displayMenuHeadings to display the headings of the menuItem ArrayList at element
    private void displayMenuHeadings(JTextArea textBox, String[] headings)
    {        
        //Display headings in the text box
        textBox.append(String.format("%s%s\n%-12s%-58s%-16s%-16s%-42s%-18s%-16s\n%s%s\n",
                "------------------------------------------------------------------------------------------",
                "------------------------------------------------------------------------------------------",
                headings[0], headings[2], headings[3], headings[4],
                headings[5], headings[6], headings[7],
                "------------------------------------------------------------------------------------------",
                "------------------------------------------------------------------------------------------")); 
    }//END OF METHOD displayMenuHeadings
           
    //START OF METHOD displayMenuTotal to display the total of the menuItem elements selected 
    private void displayMenuTotal(JTextArea textBox, MenuItem totals)
    {
        //Display the totals in the text box
        //textBox.append(String.format("\n%s%s\n%-70s%-16.0f%-16.1f%-42.1f%-18.1f%-18.1f\n%s%s", 
        textBox.append(String.format("%s%s\n%s%s%s\n\n\n\n",         
                "------------------------------------------------------------------------------------------",
                "------------------------------------------------------------------------------------------",                  
                totals.toString(),
                "------------------------------------------------------------------------------------------",
                "------------------------------------------------------------------------------------------")); 
    }//END OF METHOD displayMenuTotal
       
    //START OF METHOD tableOrderExists
    private boolean tableOrderExists(ArrayList<OrderedCustomer> customers, int tableNo)
    {
        //If the customers ArrayList has at least 1 element
        if (customers.size() > 0)
        {
            //Count through each element of the customers ArrayList
            for (int count = 0; count < customers.size(); count++)
            {            
                //Checks to see if the tableNo exists in the ArrayList customers
                if (customers.get(count).getTableNo() == tableNo) 
                    //Return true if the tableNo exists in the ArrayList
                    return true;  
            }
        }
        //Return false if not returned true already
        return false;        
    }//END OF METHOD tableOrderExists 
    
    //START OF METHOD resetValues to reset values in text boxes, combo boxes, and text area
    private void resetValues()
    {
        //Clear the textInfoArea text box
        textInfoArea.setText(null);                                        
        //Set the text boxes, combo boxes and radio buttons back to their default values
        custNameBox.setText(null);
        tableNoBox.setText("1");
        
//**    SET FOOD AND BEVERAGE COMBOS TO "BREAKFAST"  

        foodComboBox.setSelectedIndex(0);
        beverageComboBox.setSelectedIndex(0);
        bfastButton.setSelected(true);     
        prepareButton.setEnabled(true);
        billButton.setEnabled(true);
        waitingList.clearSelection();
        servedList.clearSelection();
        billedList.clearSelection();
        //Set the cursor (focus) to the customer name box
        custNameBox.requestFocus();
    }//END OF METHOD resetValues 
    
    //START OF METHOD getMealButton to return an OrderedCustomer enum CustomerMeal of the meal selected
    private String getMealButton(JRadioButton bfast, JRadioButton lunch, JRadioButton dinner)
    {
        if (bfast.isSelected())
                return BREAKFAST;
        else if (lunch.isSelected())
                return LUNCH;          
        else if (dinner.isSelected())
                return DINNER;   
        return "NULL";   
    }//END OF METHOD getMealButton   
    
    
    //START OF METHOD getMenuItemNo to get the menu item index in the ArrayList
    private int getMenuItemNo(JComboBox box, String itemCourse)
    {
        //Set selection to the name of the selected item
        String selection = new String(box.getSelectedItem().toString());
        //Set mealType to the name of the selected meal via radio buttons
        String mealType = new String(getMealButton(bfastButton, lunchButton, dinnerButton));
        //Count through all the menu items to find one that has the same name, meal type and item course
        for (int count = 0; count <= menu.size()-1; count++)
        {
            if ((menu.get(count).getItemName().equals(selection))
                    && (menu.get(count).getMealType().equals(mealType))
                    && (menu.get(count).getItemCourse().equals(itemCourse)))
            {
                return count;
            }
        }
        return NO_SELECTION;
    }
    //END OF METHOD
    
    
    //START OF METHOD getMenuItemListDisplayLine, to convert menuItem values into a string for display
    private String getMenuItemListDisplayLine(OrderedCustomer customer, MenuItem item)
    {
        return String.format("%04d%s%d%s%s%s%s", item.getorderID(), ":      Item ", item.getItemID(),
                            ":      Name: ", customer.getName(), "      Table: ", customer.getTableNo());
    }//END OF METHOD 
    
    
    
    
    
    //START OF METHOD getItemUniqueKey, to create a unique key from orderID and menuItemID
    public int getItemUniqueKey(String line)
    {                
        int uniqueKey = (Integer.parseInt(line.split(":")[0]) * 10000);
        String str1 = new String(line.split(":")[1]);
        String str2 = new String(line.split(":")[2]);
        
System.out.println("KEY: "+uniqueKey);        
System.out.println("STR1: "+str1);
System.out.println("STR2: "+str2);        
        
        return uniqueKey;
    }//END OF METHOD getItemUniqueKey
    
    //START OF OVERLOADED METHOD getItemUniqueKey, to create a unique key from orderID and menuItemID
    public int getItemUniqueKey(int orderID, int menuItemID)
    {                
        int uniqueKey = (orderID * 10000) + menuItemID;
  
        return uniqueKey;
    }//END OF METHOD getItemUniqueKey
    
    
    
    //START OF METHOD swapSelectionsAndUpdateLists, to update the list based on selections
    public void swapSelectionsAndUpdateLists(JList inputList, JList outputList, 
                                            LinkedList<String> input, LinkedList<String> output, OrderedCustomer.OrderStatus orderStatus)
    {                        
        //Get selected item values from JList waitingList
        ArrayList<String> tempList = new ArrayList<>(inputList.getSelectedValuesList());
        int[] tempIndices = new int[inputList.getSelectedIndices().length];
        tempIndices = inputList.getSelectedIndices();
        
        //for each selected value, find the menuItem in OrderedCustomer, and change orderstatus to "Served"
        //For each selection
        for (int countSelections = 0; countSelections < tempList.size(); countSelections++)
        {
            //Get the orderID of the selection
            int tempOrderID = getItemUniqueKey(tempList.get(countSelections));

            //Count through each OrderedCustomer to find the matching orderID
            for (int countCust = 0; countCust < customers.size(); countCust++)
            {
                //If the orderID matches the corresponding food menuItem 
               if (tempOrderID == customers.get(countCust).getFoodOrdered().getorderID())
                {
                    //Set the food ordered status to "Served"
                    customers.get(countCust).setFoodOrderedStatus(orderStatus);
                    break;
                }
                //Else if the orderID matches the corresponding beverage menuItem 
                else if (tempOrderID == customers.get(countCust).getBeverageOrdered().getorderID())
                {
                    //Set the beverage ordered status to "Served"
                    customers.get(countCust).setBeverageOrderedStatus(orderStatus);
                    break;
                }
            }
            //add selected item to servedCustomers linkedlist
            output.add(tempList.get(countSelections));

            //Create iterator to iterate through servedCustomers
            Iterator<String> listIterator = input.iterator();
            //While there is an element in waitingCustomers
            while (listIterator.hasNext())
            {
                //If the selected item is the same as the next item from the iterator
                if (listIterator.next().equals(tempList.get(countSelections)))
                {                
                    //remove selected item from waitingCustomers linkedlist
                    listIterator.remove();
                }
            }
        }                                                                           
        //sort servedCustomers linkedlist into order, with lowest orderID first 
        output.sort(Comparator.naturalOrder());
        //update waitingList and display
        inputList.setListData(input.toArray()); 
        //update servedList and display
        outputList.setListData(output.toArray());    
    }//END OF METHOD swapSelectionsAndUpdateLists
        
    
    //######## END OF SET, GET AND DISPLAY ITEM METHODS ########
    
     
    
    
    //## TESTING
    public static void displayTest(ArrayList<OrderedCustomer> temp)
    {
        
        
        for (int x = 0; x > temp.size(); x++)
        {
            
                    
            System.out.print("NAME: " + temp.get(x).getName());        
            System.out.println("      TABLE NO: " + temp.get(x).getTableNo()); 
            System.out.print("- FOOD MENUITEM ID: " + temp.get(x).getFoodOrdered().getItemID());
            System.out.print("      ORDER ID: " + temp.get(x).getFoodOrdered().getorderID());
            System.out.print("      STATUS: " + temp.get(x).getFoodOrderedStatus());        
            System.out.println("      MENUITEM NAME: " + temp.get(x).getFoodOrdered().getItemName());
            
            System.out.print("- BEVERAGE MENUITEM ID: " + temp.get(x).getBeverageOrdered().getItemID());
            System.out.print("      ORDER ID: " + temp.get(x).getBeverageOrdered().getorderID());
            System.out.print("      STATUS: " + temp.get(x).getBeverageOrderedStatus());  
            System.out.println("      MENUITEM NAME: " + temp.get(x).getBeverageOrdered().getItemName()); 
            
            System.out.println();
            
            
        }
    }
    public static void displayTest(OrderedCustomer temp)
    {        
                    
            System.out.print("NAME: " + temp.getName());        
            System.out.println("      TABLE NO: " + temp.getTableNo()); 
            System.out.print("- FOOD MENUITEM ID: " + temp.getFoodOrdered().getItemID());
            System.out.print("      ORDER ID: " + temp.getFoodOrdered().getorderID());
            System.out.print("      STATUS: " + temp.getFoodOrderedStatus());        
            System.out.println("      MENUITEM NAME: " + temp.getFoodOrdered().getItemName());   
            System.out.print("- BEVERAGE MENUITEM ID: " + temp.getBeverageOrdered().getItemID());
            System.out.print("      ORDER ID: " + temp.getBeverageOrdered().getorderID());
            System.out.print("      STATUS: " + temp.getBeverageOrderedStatus());  
            System.out.println("      MENUITEM NAME: " + temp.getBeverageOrdered().getItemName()); 
            
            System.out.println();
       
        
    }
    
    
    
    
    
    
    
    //###### START OF MAIN METHOD ######
    public static void main(String[] args)
    {     
        //Declare and initialise new ResaurantOrderGUI object
        RestaurantOrderGUI app = new RestaurantOrderGUI();        
        
        //Opens the file, reads the file into the ArrayList menu, and closes it
        app.readMenuFile(DATA_FILE_NAME);
        
        //Initiates database
        app.database.initiateDatabase();
                
        
        
        //Initialise the GUI by adding components
        app.displayGUI();
            
        //Initialise the ordering system GUI and listen for events/actions
        app.startMenuOrderingSystem();    
    }//###### END OF MAIN METHOD ######    
}//END OF CLASS RestaurantOrderGUI