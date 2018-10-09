package pkgs.HardwareStore;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import java.io.IOException;
import java.util.logging.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Scanner;

/**
 * This is the main class of the Hardware Store database manager.
 *
 * @author Jeffrey Vallejo
 */
//take out ActionListener when encountering a bug
public class MainApp extends JPanel {
    
    private boolean DEBUG = false;
    
    // create a new object of type pkgs
    private final HardwareStore hardwareStore;
    private static final Scanner CONSOLE_INPUT = new Scanner(System.in);
    
    // add JFrame objects
    private JFrame frame;
    public static JTextArea textArea = new JTextArea(200, 100); //this will hold the output
    private final static String newline = "\n";
    private static final Logger logger = Logger.getLogger("pkgs.MainApp");
    private static final FileHandler fh = initFh();
    //public JTextField textField;
    private static int userIdCounter;
    
    private static FileHandler initFh() {
        FileHandler fh = null;
        try {
            fh = new FileHandler("pkgs.log");
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fh;
    }
    
    /**
     *
     */
    public MainApp() throws IOException{
        //super(new BorderLayout());
        hardwareStore = new HardwareStore();
        //hardwareStore = pkgs.readDatabase();
        //this.scanner = new Scanner(System.in);
        frame = new JFrame("pkgs Interface");
        //outermost layout "mainBoarder"
        JComponent mainPanel = new JPanel(new BorderLayout());
        
        /* Inventory:
         * contains a list to show current items
         * gives text fields to add a new item
         * special operations buttons at the bottom for more options
         */
        //frame.setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();

        //remove these two lines after testing
        //TitledBorder mainBorder = BorderFactory.createTitledBorder("mainBorder");
        //tabbedPane.setBorder(mainBorder);

        //String[] columnInv = {"ID Number", "Name", "Quantity", "Price", "Category", "Brand/Type"};
        
        final DefaultTableModel invmodel = new DefaultTableModel();
        final JTable invTable = new JTable(invmodel);
        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(invTable.getModel());
        invTable.setRowSorter(rowSorter);
        invmodel.addColumn("ID Number");
        invmodel.addColumn("Name");
        invmodel.addColumn("Quantity");
        invmodel.addColumn("Price");
        invmodel.addColumn("Category");
        invmodel.addColumn("Brand/Type");
        invTable.setPreferredScrollableViewportSize(new Dimension(1000,95));

        if (DEBUG) {
            invTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(invTable);
                    logger.info("clicked a row in inventory table");
                }
            });
        }

        JScrollPane tableScroller = new JScrollPane(invTable);
        
        tabbedPane.addTab("Inventory", mainPanel);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        //panel1.setPreferredSize(new Dimension(10, 5));

        //adds the table and puts it at desired location
        JComponent tablePanel = new JPanel();
        tablePanel.add(tableScroller);
        TitledBorder b = BorderFactory.createTitledBorder("Inventory");
        tablePanel.setBorder(b);
        mainPanel.add(tablePanel, BorderLayout.PAGE_START);

        //make text fields here and assign it to center
        JComponent outField = new JPanel();
        outField.setLayout(new GridLayout());
        JComponent inField = new JPanel();
        inField.setLayout(new BoxLayout(inField, BoxLayout.PAGE_AXIS));

        //text field: ID
        JComponent idPanel = new JPanel();
        JTextField textID = new JTextField(20);
        JScrollPane paneID = new JScrollPane(textID);
        JLabel labelID = new JLabel ("ID              ");
        labelID.setLabelFor(textID);
        idPanel.add(labelID);
        idPanel.add(paneID);
        inField.add(idPanel);

        //text field: Name
        JComponent namePanel = new JPanel();
        JTextField textName = new JTextField(20);
        JScrollPane paneName = new JScrollPane(textName);
        JLabel labelName = new JLabel ("Name         ");
        labelName.setLabelFor(textName);
        namePanel.add(labelName);
        namePanel.add(paneName);
        inField.add(namePanel);

        //text field: Quantity
        JComponent qtyPanel = new JPanel();
        JTextField textQty = new JTextField(20);
        JScrollPane paneQty = new JScrollPane(textQty);
        JLabel labelQty = new JLabel ("Quantity     ");
        labelQty.setLabelFor(textQty);
        qtyPanel.add(labelQty);
        qtyPanel.add(paneQty);
        inField.add(qtyPanel);

        //text field: Price
        JComponent prcPanel = new JPanel();
        JTextField textPrc = new JTextField(20);
        JScrollPane panePrc = new JScrollPane(textPrc);
        JLabel labelPrc = new JLabel ("Price           ");
        labelPrc.setLabelFor(textPrc);
        prcPanel.add(labelPrc);
        prcPanel.add(panePrc);
        inField.add(prcPanel);


        //text field: Category
        JComponent panelCat = new JPanel();
        JTextField textCat = new JTextField(20);
        JScrollPane paneCat = new JScrollPane(textCat);
        JLabel labelCat = new JLabel ("Category     ");
        labelCat.setLabelFor(textCat);
        panelCat.add(labelCat);
        panelCat.add(paneCat);
        inField.add(panelCat);

        //text field: Brand/Type
        JComponent panelBnd = new JPanel();
        JTextField textBnd = new JTextField(20);
        JScrollPane paneBnd = new JScrollPane(textBnd);
        JLabel labelBnd = new JLabel ("Brand/Type ");
        labelBnd.setLabelFor(textBnd);
        panelBnd.add(labelBnd);
        panelBnd.add(paneBnd);
        inField.add(panelBnd);

        //adds a titled border around the field pane
        TitledBorder fldborder = BorderFactory.createTitledBorder("Fields");
        outField.setBorder(fldborder);
        outField.add(inField);

        //these will be the special operations buttons at the bottom below the text fields
        FlowLayout invBLayout = new FlowLayout();
        JPanel invButtonFlow = new JPanel();
        invButtonFlow.setLayout(invBLayout);
        //invBLayout.setAlignment(FlowLayout.TRAILING);
        inField.add(invButtonFlow);
        //mainPanel.add(invButtonFlow, BorderLayout.PAGE_END);

        JButton addButton = new JButton("ADD");
        invButtonFlow.add(addButton);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                String id = textID.getText();
                String name = textName.getText();

                //parse text to int
                String numQty = textQty.getText();
                int quantity = 0;

                //parse text to float
                String fltPrc = textPrc.getText();
                float price = 0;
                try{
                    quantity = Integer.parseInt(numQty);
                    price = Float.parseFloat(fltPrc);
   

                }catch(NumberFormatException n){
                    textArea.append("**OOPS. looks like data was entered improperly** \n");
                    textArea.append("please enter integers for quantity and float for price");
                    System.out.println("catch me");
                }

                String category = textCat.getText();
                String brand = textBnd.getText();



                if (category != null && !category.isEmpty()) {
                        HardwareStore.addNewSmallHardwareItem(id, name, quantity, price, category);
                        String[] dataInv = {id, name, numQty, fltPrc, category, "N/A"};
                        logger.info("added new small hardware item");
                        invmodel.addRow(dataInv);
                }
                else {
                    HardwareStore.addNewAppliance(id, name, quantity, price, category, brand);
                    String[] dataInv = {id, name, numQty, fltPrc, "N/A", brand};
                    logger.info("added new appliance");
                    invmodel.addRow(dataInv);
                }

                textID.setText("");
                textName.setText("");
                textQty.setText("");
                textPrc.setText("");
                textCat.setText("");
                textBnd.setText("");
                
            }
        });
        
        JButton removeButton = new JButton("REMOVE");
        invButtonFlow.add(removeButton);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String itemID = textID.getText();
                int index = HardwareStore.findItemIndex(itemID);
                HardwareStore.removeItem(index);
                logger.info("removed a quantity from an item");
            }
        });
    

        
        //adds the pane to the back panel
        mainPanel.add(outField, BorderLayout.CENTER);

        /*Update inventory panel
         *  used to update the quantity of selected row
         */
        JComponent outWest = new JPanel();
        outWest.setLayout(new GridLayout(2,1));
    
        JComponent inWestMsgr = new JPanel();
        textArea = new JTextArea(8,30);
        textArea.setEditable(false);
        JScrollPane messenger = new JScrollPane(textArea);
        //textArea.setLineWrap(true);
        
        textArea.append("Welcome to the pkgs Graphical user interface.\n");
        textArea.append("I will give you a quick guide through the buttons on this window\n");
        textArea.append("In the update panel you can update the quantity of an item by clicking \nthe item in the table,");
        textArea.append("enter a quantity, then click add or subtract to modify the data\n");
        textArea.append("In the fields panel, simply add the desired information then click add\n");
        textArea.append("or to remove an item entirely click the item in the table, then click remove\n");
        textArea.append("In the filter panel, simply start typing in the field you want to filter by\n");
        inWestMsgr.add(messenger);
        TitledBorder msgB = BorderFactory.createTitledBorder("Messenger");
        inWestMsgr.setBorder(msgB);
        outWest.add(inWestMsgr);
        
        
        JComponent inWestUpdate = new JPanel();
        inWestUpdate.setLayout(new BoxLayout(inWestUpdate, BoxLayout.PAGE_AXIS));
        outWest.add(inWestUpdate);
        //TitledBorder west = BorderFactory.createTitledBorder("");

        JComponent idPanelUpdate = new JPanel();
        JTextField textQtyUpdate = new JTextField(20);
        JScrollPane paneIDUpdate = new JScrollPane(textQtyUpdate);
        JLabel labelIDupdate = new JLabel ("Quantity ");
        labelIDupdate.setLabelFor(textQtyUpdate);
        idPanelUpdate.add(labelIDupdate);
        idPanelUpdate.add(paneIDUpdate);
        inWestUpdate.add(idPanelUpdate);
        TitledBorder updateB = BorderFactory.createTitledBorder("Update");
        inWestUpdate.setBorder(updateB);
        mainPanel.add(outWest, BorderLayout.WEST);
    
    
        FlowLayout updateBLayout = new FlowLayout();
        JPanel updateButtonFlow = new JPanel();
        updateButtonFlow.setLayout(updateBLayout);
        updateBLayout.setAlignment(FlowLayout.TRAILING);
        inWestUpdate.add(updateButtonFlow);
        //Button ADD: adds to the quantity in selected row
        JButton addQtyButton = new JButton("  PLUS  ");
        updateButtonFlow.add(addQtyButton);
        addQtyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            
                int selectedRow = invTable.getSelectedRow();
                if (selectedRow != -1) {
                    //parse text to int
                    String numQty = textQtyUpdate.getText();
                    int updateQty = Integer.parseInt(numQty);
                    int currentQty = Integer.parseInt(invTable.getValueAt(selectedRow, 2).toString());
                    int newQty = 0;
                    if (updateQty >= 0){
                        newQty = currentQty + updateQty;
                        invTable.setValueAt(newQty, selectedRow, 2);
                        logger.info("added quantity to an item");
                    }
                
                }
            }
        });
        JButton rmvQtyButton = new JButton("SUBTRACT");
        updateButtonFlow.add(rmvQtyButton);
        rmvQtyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            
                int selectedRow = invTable.getSelectedRow();
                if (selectedRow != -1) {
                    //parse text to int
                    String numQty = textQtyUpdate.getText();
                    int updateQty = Integer.parseInt(numQty);
                    int currentQty = Integer.parseInt(invTable.getValueAt(selectedRow, 2).toString());
                    int newQty = 0;
                    if (currentQty >= updateQty){
                        //pkgs.removeQuantity()
                        newQty = currentQty - updateQty;
                        invTable.setValueAt(newQty, selectedRow, 2);
                        logger.info("subtracted quantity from item");
                    }
                
                }
            }
        });
    
        /*Filter feilds:
         * allows user to filter the Jtable by typing into the textfields
         */
        
        JComponent outWestfltr = new JPanel();
        outWestfltr.setLayout(new GridLayout(2,1));
        outWestfltr.setLayout(new BoxLayout(outWestfltr, BoxLayout.PAGE_AXIS));
    
        JComponent idPanelfltr = new JPanel();
        JTextField textIDfltr = new JTextField(10);
        JScrollPane paneIDfltr = new JScrollPane(textIDfltr);
        JLabel labelIDfltr = new JLabel ("ID             ");
        labelIDfltr.setLabelFor(textIDfltr);
        idPanelfltr.add(labelIDfltr);
        idPanelfltr.add(paneIDfltr);
        outWestfltr.add(idPanelfltr);
        
        textIDfltr.getDocument().addDocumentListener(new DocumentListener() {
            
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textIDfltr.getText();
                
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    logger.info("filtered data in table by ID");
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
    
                String text = textIDfltr.getText();
    
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        JComponent panelNamefltr = new JPanel();
        JTextField textNamefltr = new JTextField(10);
        JScrollPane paneNamefltr = new JScrollPane(textNamefltr);
        JLabel labelNamefltr = new JLabel ("Name          ");
        labelNamefltr.setLabelFor(textNamefltr);
        panelNamefltr.add(labelNamefltr);
        panelNamefltr.add(paneNamefltr);
        outWestfltr.add(panelNamefltr);
    
        textNamefltr.getDocument().addDocumentListener(new DocumentListener() {
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textNamefltr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    logger.info("filtered data in table by name");
                }
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
            
                String text = textNamefltr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        JComponent PanelQtyfltr = new JPanel();
        JTextField textQtyfltr = new JTextField(10);
        JScrollPane paneQtyfltr = new JScrollPane(textQtyfltr);
        JLabel labelQtyfltr = new JLabel ("Quantity     ");
        labelQtyfltr.setLabelFor(textQtyfltr);
        PanelQtyfltr.add(labelQtyfltr);
        PanelQtyfltr.add(paneQtyfltr);
        outWestfltr.add(PanelQtyfltr);
        
    
        textQtyfltr.getDocument().addDocumentListener(new DocumentListener() {
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textQtyfltr.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    logger.info("filtered data in table by quantity");
                }
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
            
                String text = textQtyfltr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        JComponent PanelCatfltr = new JPanel();
        JTextField textCatfltr = new JTextField(10);
        JScrollPane paneCatfltr = new JScrollPane(textCatfltr);
        JLabel labelCatfltr = new JLabel ("Category      ");
        labelCatfltr.setLabelFor(textCatfltr);
        PanelCatfltr.add(labelCatfltr);
        PanelCatfltr.add(paneCatfltr);
        outWestfltr.add(PanelCatfltr);
    
        textCatfltr.getDocument().addDocumentListener(new DocumentListener() {
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textCatfltr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    logger.info("filtered data in table by category");
                }
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
            
                String text = textCatfltr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        JComponent PanelBndfltr = new JPanel();
        JTextField textBndfltr = new JTextField(10);
        JScrollPane paneBndfltr = new JScrollPane(textBndfltr);
        JLabel labelBndfltr = new JLabel ("Brand/Type   ");
        labelBndfltr.setLabelFor(textBndfltr);
        PanelBndfltr.add(labelBndfltr);
        PanelBndfltr.add(paneBndfltr);
        outWestfltr.add(PanelBndfltr);
    
        textBndfltr.getDocument().addDocumentListener(new DocumentListener() {
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textBndfltr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    logger.info("filtered data in table by Brand/Type");
                }
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
            
                String text = textBndfltr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        
        TitledBorder fltrB = BorderFactory.createTitledBorder("Filter");
        outWestfltr.setBorder(fltrB);
        mainPanel.add(outWestfltr, BorderLayout.EAST);
        
    
        /* Users TAB:
         * contains a list to show current items
         * gives text fields to add a new item
         * special operations buttons at the bottom for more options
         */
        frame.setLayout(new GridLayout(1,1));
        //JTabbedPane tabbedPane2 = new JTabbedPane();
        //remove these two lines after testing
        TitledBorder mainBorderUsr = BorderFactory.createTitledBorder("mainBorder");
        tabbedPane.setBorder(mainBorderUsr);
        //testing table
        //Object[] dataUsr = {};
    
        final DefaultTableModel usrmodel = new DefaultTableModel();
        final JTable usrTable = new JTable(usrmodel);
        TableRowSorter<TableModel> rowSorterusr = new TableRowSorter<>(usrTable.getModel());
        usrTable.setRowSorter(rowSorterusr);
        usrmodel.addColumn("ID Number");
        usrmodel.addColumn("First Name");
        usrmodel.addColumn("Last Name");
        usrmodel.addColumn("Social");
        usrmodel.addColumn("Salary");
        usrmodel.addColumn("Phone #");
        usrmodel.addColumn("Address");
        usrTable.setPreferredScrollableViewportSize(new Dimension(1000,95));

        if (DEBUG) {
            usrTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(usrTable);
                    logger.info("clicked a row in user table");
                }
            });
        }

        JScrollPane usrScroller = new JScrollPane(usrTable);

        JComponent mainPanelUsr = new JPanel(new BorderLayout());


        tabbedPane.addTab("User", mainPanelUsr);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        //panel1.setPreferredSize(new Dimension(10, 5));

        //adds the table and puts it at desired location
        JComponent usrTablePanel = new JPanel();
        usrTablePanel.add(usrScroller);
        TitledBorder bUsr = BorderFactory.createTitledBorder("Users");
        usrTablePanel.setBorder(bUsr);
        mainPanelUsr.add(usrTablePanel, BorderLayout.PAGE_START);

        //make text fields here and assign it to center
        JComponent outFieldusr = new JPanel();
        outFieldusr.setLayout(new GridLayout());
        JComponent inFieldusr = new JPanel();
        inFieldusr.setLayout(new BoxLayout(inFieldusr, BoxLayout.PAGE_AXIS));

        //text field: ID (per pkgs, not Required, iterator used
        JComponent idPanelUsr = new JPanel();
        JTextField textIDusr = new JTextField(20);
        JScrollPane paneIDusr = new JScrollPane(textIDusr);
        JLabel labelIDusr = new JLabel ("ID (Edit Only)");
        labelIDusr.setLabelFor(textIDusr);
        idPanelUsr.add(labelIDusr);
        idPanelUsr.add(paneIDusr);
        inFieldusr.add(idPanelUsr);

        //text field: First Name
        JComponent firstNamePanelusr = new JPanel();
        JTextField textFirstNameusr = new JTextField(20);
        JScrollPane paneNameusr = new JScrollPane(textFirstNameusr);
        JLabel labelNameusr = new JLabel ("First Name   ");
        labelNameusr.setLabelFor(textFirstNameusr);
        firstNamePanelusr.add(labelNameusr);
        firstNamePanelusr.add(paneNameusr);
        inFieldusr.add(firstNamePanelusr);

        //text field: Last Name (Edits needed)
        JComponent lastNamePanelusr = new JPanel();
        JTextField textLastNameusr = new JTextField(20);
        JScrollPane paneQtyusr = new JScrollPane(textLastNameusr);
        JLabel labelLastNameusr = new JLabel ("Last Name    ");
        labelLastNameusr.setLabelFor(textLastNameusr);
        lastNamePanelusr.add(labelLastNameusr);
        lastNamePanelusr.add(paneQtyusr);
        inFieldusr.add(lastNamePanelusr);

        //text field: Social (Edits Needed)
        JComponent socialPanelusr = new JPanel();
        JTextField textSocialusr = new JTextField(20);
        JScrollPane paneSocialusr = new JScrollPane(textSocialusr);
        JLabel labelSocialusr = new JLabel ("Social (No [-])");
        labelSocialusr.setLabelFor(textSocialusr);
        socialPanelusr.add(labelSocialusr);
        socialPanelusr.add(paneSocialusr);
        inFieldusr.add(socialPanelusr);


        //text field: Salary (Edits Needed) 
        JComponent panelSalusr = new JPanel();
        JTextField textSalusr = new JTextField(20);
        JScrollPane paneSalusr = new JScrollPane(textSalusr);
        JLabel labelSalusr = new JLabel ("Salary          ");
        labelSalusr.setLabelFor(textSalusr);
        panelSalusr.add(labelSalusr);
        panelSalusr.add(paneSalusr);
        inFieldusr.add(panelSalusr);

       //text field: Phone Number (Edits Needed) 
        JComponent panelNumusr = new JPanel();
        JTextField textNumusr = new JTextField(20);
        JScrollPane paneNumusr = new JScrollPane(textNumusr);
        JLabel labelNumusr = new JLabel ("Phone #          ");
        labelNumusr.setLabelFor(textNumusr);
        panelNumusr.add(labelNumusr);
        panelNumusr.add(paneNumusr);
        inFieldusr.add(panelNumusr);
        
       //text field: Address (Edits Needed) 
        JComponent panelAddusr = new JPanel();
        JTextField textAddusr = new JTextField(20);
        JScrollPane paneAddusr = new JScrollPane(textAddusr);
        JLabel labelAddusr = new JLabel ("Address          ");
        labelAddusr.setLabelFor(textAddusr);
        panelAddusr.add(labelAddusr);
        panelAddusr.add(paneAddusr);
        inFieldusr.add(panelAddusr);
        
        //adds a titled border around the field pane
        TitledBorder fldborderusr = BorderFactory.createTitledBorder("Fields");
        outFieldusr.setBorder(fldborderusr);
        outFieldusr.add(inFieldusr);

        //these will be the special operations buttons at the bottom below the text fields
        FlowLayout bLayoutusr = new FlowLayout();
        JPanel buttonFlowusr = new JPanel();
        buttonFlowusr.setLayout(bLayoutusr);
        inFieldusr.add(buttonFlowusr);

        mainPanelUsr.add(outFieldusr, BorderLayout.CENTER);
        
        JButton addButtonusr = new JButton("ADD");
        buttonFlowusr.add(addButtonusr);
        addButtonusr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

            	userIdCounter++;
                String strID = Integer.toString(userIdCounter);
                String firstName = textFirstNameusr.getText();
                String lastName = textLastNameusr.getText();
                
                String intSocial = textSocialusr.getText(); 
                int social = Integer.parseInt(intSocial);
                
                String fltSalary = textSalusr.getText();
                float salary = Float.parseFloat(fltSalary);
                
                String phoneNum = textNumusr.getText();
                String address = textAddusr.getText();

                if (phoneNum.isEmpty()) {
                	HardwareStore.addEmployee(firstName, lastName, social, salary);
                	String[] dataUsr = {strID, firstName, lastName, intSocial, fltSalary, "N/A", "N/A"};
                    usrmodel.addRow(dataUsr);
                    logger.info("added employee to user table");
                }
                else {
                	HardwareStore.addCustomer(firstName, lastName, phoneNum, address);
                	String[] dataUsr = {strID, firstName, lastName, "N/A", "N/A", phoneNum, address};
                    usrmodel.addRow(dataUsr);
                    logger.info("added customer to user table");
                }

                textIDusr.setText("");
                textFirstNameusr.setText("");
                textLastNameusr.setText("");
                textSocialusr.setText("");
                textSalusr.setText("");
                textNumusr.setText("");
                textAddusr.setText("");
            }
        });

        JButton editButtonusr = new JButton("EDIT");
        buttonFlowusr.add(editButtonusr);
        editButtonusr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check for selected row first
                if (usrTable.getSelectedRow() != -1) {
                    // remove selected row from the model
                    usrmodel.removeRow(usrTable.getSelectedRow());
                    logger.info("removed row from user table");
                }
                String text = textIDusr.getText();
                int id = Integer.parseInt(text); 
                int index = HardwareStore.findUserIndex(id);
                
                String firstName = textFirstNameusr.getText();
                String lastName = textLastNameusr.getText();
                
                String intSocial = textSocialusr.getText(); 
                int social = Integer.parseInt(intSocial);
                
                String fltSalary = textSalusr.getText();
                float salary = Float.parseFloat(fltSalary);
                
                String phoneNum = textNumusr.getText();
                String address = textAddusr.getText();
                
                if (phoneNum.isEmpty()) {
                	HardwareStore.addEmployee(firstName, lastName, social, salary);
                	String[] dataUsr = {text, firstName, lastName, intSocial, fltSalary, "N/A", "N/A"};
                    usrmodel.addRow(dataUsr);
                    logger.info("new employee added");
                }
                else {
                	HardwareStore.editCustomerInformation(index, firstName, lastName, phoneNum, address);
                	String[] dataUsr = {text, firstName, lastName, "N/A", "N/A", phoneNum, address};
                    usrmodel.addRow(dataUsr);
                    logger.info("new Customer added");
                }

                textIDusr.setText("");
                textFirstNameusr.setText("");
                textLastNameusr.setText("");
                textSocialusr.setText("");
                textSalusr.setText("");
                textNumusr.setText("");
                textAddusr.setText("");
            }
        });
        /*Filter fields:
         * allows user to filter the Jtable by typing into the textfields
         */
        JComponent outWestfltrusr = new JPanel();
        outWestfltrusr.setLayout(new GridLayout(2,1));
        outWestfltrusr.setLayout(new BoxLayout(outWestfltrusr, BoxLayout.PAGE_AXIS));
    
        JComponent PanelIDfltrusr = new JPanel();
        JTextField textIDfltrusr = new JTextField(10);
        JScrollPane paneIDfltrusr = new JScrollPane(textIDfltrusr);
        JLabel labelIDfltrusr = new JLabel ("ID       ");
        labelIDfltrusr.setLabelFor(textIDfltrusr);
        PanelIDfltrusr.add(labelIDfltrusr);
        PanelIDfltrusr.add(paneIDfltrusr);
        outWestfltrusr.add(PanelIDfltrusr);
    
        textIDfltrusr.getDocument().addDocumentListener(new DocumentListener() {
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textIDfltrusr.getText();
//                int lTQ = Integer.parseInt(text);
//                if ()
                if (text.trim().length() == 0) {
                    rowSorterusr.setRowFilter(null);
                } else {
                    rowSorterusr.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    logger.info("table filtered by ID");
                }
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
            
                String text = textIDfltrusr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorterusr.setRowFilter(null);
                } else {
                    
                    rowSorterusr.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        JComponent panelNamefltrusr = new JPanel();
        JTextField textFirstNamefltrusr = new JTextField(10);
        JScrollPane paneNamefltrusr = new JScrollPane(textFirstNamefltrusr);
        JLabel labelNamefltrusr = new JLabel ("Name    ");
        labelNamefltrusr.setLabelFor(textFirstNamefltrusr);
        panelNamefltrusr.add(labelNamefltrusr);
        panelNamefltrusr.add(paneNamefltrusr);
        outWestfltrusr.add(panelNamefltrusr);
    
        textFirstNamefltrusr.getDocument().addDocumentListener(new DocumentListener() {
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textFirstNamefltrusr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorterusr.setRowFilter(null);
                } else {
                    rowSorterusr.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    logger.info("table filtered by name");
                }
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
            
                String text = textFirstNamefltrusr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorterusr.setRowFilter(null);
                } else {
                    rowSorterusr.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        JComponent panelSocfltrusr = new JPanel();
        JTextField textSocfltrusr = new JTextField(10);
        JScrollPane paneSocfltrusr = new JScrollPane(textSocfltrusr);
        JLabel labelSocfltrusr = new JLabel ("Social    ");
        labelSocfltrusr.setLabelFor(textSocfltrusr);
        panelSocfltrusr.add(labelSocfltrusr);
        panelSocfltrusr.add(paneSocfltrusr);
        outWestfltrusr.add(panelSocfltrusr);
    
        textSocfltrusr.getDocument().addDocumentListener(new DocumentListener() {
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textSocfltrusr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorterusr.setRowFilter(null);
                } else {
                    rowSorterusr.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    logger.info("table filtered by social");
                }
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
            
                String text = textSocfltrusr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorterusr.setRowFilter(null);
                } else {
                    rowSorterusr.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        JComponent panelAddfltrusr = new JPanel();
        JTextField textAddfltrusr = new JTextField(10);
        JScrollPane paneAddfltrusr = new JScrollPane(textAddfltrusr);
        JLabel labelAddfltrusr = new JLabel ("Address    ");
        labelAddfltrusr.setLabelFor(textAddfltrusr);
        panelAddfltrusr.add(labelAddfltrusr);
        panelAddfltrusr.add(paneAddfltrusr);
        outWestfltrusr.add(panelAddfltrusr);
    
        textAddfltrusr.getDocument().addDocumentListener(new DocumentListener() {
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textAddfltrusr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorterusr.setRowFilter(null);
                } else {
                    rowSorterusr.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    logger.info("table filtered by address");
                }
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
            
                String text = textAddfltrusr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorterusr.setRowFilter(null);
                } else {
                    rowSorterusr.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        JComponent panelNumfltrusr = new JPanel();
        JTextField textNumfltrusr = new JTextField(10);
        JScrollPane paneNumfltrusr = new JScrollPane(textNumfltrusr);
        JLabel labelNumfltrusr = new JLabel ("Phone #    ");
        labelNumfltrusr.setLabelFor(textNumfltrusr);
        panelNumfltrusr.add(labelNumfltrusr);
        panelNumfltrusr.add(paneNumfltrusr);
        outWestfltrusr.add(panelNumfltrusr);
    
        textNumfltrusr.getDocument().addDocumentListener(new DocumentListener() {
        
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = textNumfltrusr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorterusr.setRowFilter(null);
                } else {
                    rowSorterusr.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    logger.info("table filtered by phone number");
                }
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
            
                String text = textNumfltrusr.getText();
            
                if (text.trim().length() == 0) {
                    rowSorterusr.setRowFilter(null);
                } else {
                    rowSorterusr.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        TitledBorder fltrBusr = BorderFactory.createTitledBorder("Filter");
        outWestfltrusr.setBorder(fltrBusr);
        mainPanelUsr.add(outWestfltrusr, BorderLayout.EAST);

        /* Sales:
         * contains a list to show current items
         * gives text fields to add a new item
         * special operations buttons at the bottom for more options
         */
        frame.setLayout(new GridLayout(1,1));
        //JTabbedPane tabbedPane3 = new JTabbedPane();

        DefaultTableModel salesmodel = new DefaultTableModel();
        final JTable salesTable = new JTable(salesmodel);
        salesmodel.addColumn("ID Number");
        salesmodel.addColumn("Sale Date");
        salesmodel.addColumn("Sale Qty");
        salesmodel.addColumn("Customer ID");
        salesmodel.addColumn("Employee ID");

        salesTable.setPreferredScrollableViewportSize(new Dimension(1000,95));
    
        if (DEBUG) {
            salesTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(salesTable);
                    logger.info("clicked on row in sales table");
                }
            });
        }
    
        JScrollPane salesScroller = new JScrollPane(salesTable);
    
        JComponent mainPanelSales = new JPanel(new BorderLayout());
    
    
        tabbedPane.addTab("Sales", mainPanelSales);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        //panel1.setPreferredSize(new Dimension(10, 5));
    
        //adds the table and puts it at desired location
        JComponent salesTablePanel = new JPanel();
        salesTablePanel.add(salesScroller);
        TitledBorder bSales = BorderFactory.createTitledBorder("Sales");
        salesTablePanel.setBorder(bSales);
        mainPanelSales.add(salesTablePanel, BorderLayout.PAGE_START);
    
        //make text fields here and assign it to center
        JComponent outFieldSales = new JPanel();
        JComponent inFieldSales = new JPanel();
        inFieldSales.setLayout(new BoxLayout(inFieldSales, BoxLayout.PAGE_AXIS));
    
        //text field: ID
        JComponent idPanelSales = new JPanel();
        JTextField textIDSales = new JTextField(20);
        JScrollPane paneIDSales = new JScrollPane(textIDSales);
        JLabel labelIDSales = new JLabel ("Item ID                  ");
        labelIDSales.setLabelFor(textIDSales);
        idPanelSales.add(labelIDSales);
        idPanelSales.add(paneIDSales);
        inFieldSales.add(idPanelSales);
    
        //text field: Date
        JComponent namePanelSales = new JPanel();
        JTextField textDateSales = new JTextField(20);
        JScrollPane paneNameSales = new JScrollPane(textDateSales);
        JLabel labelNameSales = new JLabel ("Sale date           ");
        labelNameSales.setLabelFor(textDateSales);
        namePanelSales.add(labelNameSales);
        namePanelSales.add(paneNameSales);
        inFieldSales.add(namePanelSales);
    
        //text field: Sales Quantity 
        JComponent qtyPanelSales = new JPanel();
        JTextField textQtySales = new JTextField(20);
        JScrollPane paneQtySales = new JScrollPane(textQtySales);
        JLabel labelQtySales = new JLabel ("Sale Qty            ");
        labelQtySales.setLabelFor(textQtySales);
        qtyPanelSales.add(labelQtySales);
        qtyPanelSales.add(paneQtySales);
        inFieldSales.add(qtyPanelSales);
    
        //text field: Cus Id
        JComponent prcPanelSales = new JPanel();
        JTextField textCusIdSales = new JTextField(20);
        JScrollPane panePrcSales = new JScrollPane(textCusIdSales);
        JLabel labelPrcSales = new JLabel ("Customer ID      ");
        labelPrcSales.setLabelFor(textCusIdSales);
        prcPanelSales.add(labelPrcSales);
        prcPanelSales.add(panePrcSales);
        inFieldSales.add(prcPanelSales);
    
    
        //text field: Emp Id
        JComponent panelCatSales = new JPanel();
        JTextField textEmpIdSales = new JTextField(20);
        JScrollPane paneCatSales = new JScrollPane(textEmpIdSales);
        JLabel labelCatSales = new JLabel ("Employee ID       ");
        labelCatSales.setLabelFor(textEmpIdSales);
        panelCatSales.add(labelCatSales);
        panelCatSales.add(paneCatSales);
        inFieldSales.add(panelCatSales);
    
        //adds a titled border around the field pane
        TitledBorder fldborderSales = BorderFactory.createTitledBorder("Fields");
        outFieldSales.setBorder(fldborderSales);
        outFieldSales.add(inFieldSales);
    
        //adds the pane to the back panel
        mainPanelSales.add(outFieldSales, BorderLayout.CENTER);
    
    
        //these will be the special operations buttons at the bottom below the text fields
        FlowLayout bLayoutSales = new FlowLayout();
        JPanel buttonFlowSales = new JPanel();
        buttonFlowSales.setLayout(bLayoutSales);
        bLayoutSales.setAlignment(FlowLayout.TRAILING);

        mainPanelSales.add(buttonFlowSales, BorderLayout.PAGE_END);
        
   
        JButton addButtonsales = new JButton("ADD");
        buttonFlowSales.add(addButtonsales);
        addButtonsales.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String itemID = textIDSales.getText();
                
                String textQuan = textQtySales.getText();
                int quantity = Integer.parseInt(textQuan);
                
                String date = textDateSales.getText();
                
                String textCust = textCusIdSales.getText();
                int customer = Integer.parseInt(textCust);
                		
                String textEmp = textEmpIdSales.getText(); 
                int employee = Integer.parseInt(textEmp);
                
                int itemIndex = HardwareStore.findItemIndex(itemID);
                
                HardwareStore.progressTransaction(itemID, quantity, customer, employee, itemIndex);
                
                String dataSales [] = {itemID, date, textQuan, textCust, textEmp};
                salesmodel.addRow(dataSales);
                
                textIDSales.setText("");
                textQtySales.setText("");
                textDateSales.setText("");
                textCusIdSales.setText("");
                textEmpIdSales.setText("");
                    logger.info("added a sale to the sales table");
            }
        });
        
        
        //add tabbed pane
        add(tabbedPane);
        
        // adds scrolling tabs
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    
    }
    
    private void printDebugData(JTable table) {
        int numRows = table.getRowCount();
        int numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();
        
        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }
    
    private static void createAndShowGUI() throws IOException {
        JFrame frame = new JFrame("tabbedPane");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.add(new MainApp());
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) throws Exception {
    
        //MainApp app = new MainApp();
        //app.createAndShowGUI();
        {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        createAndShowGUI();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    
}