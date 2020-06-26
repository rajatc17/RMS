/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rms;
import java.awt.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author rajat
 */
public class ITEM_SELECTION extends javax.swing.JFrame implements Connectivity{

    double final_price;
    
    FlowLayout layout = new FlowLayout();
    Dimension d = new Dimension(233,300);
 
    java.util.List<String> code = new ArrayList<>();
    java.util.ArrayList<String> oid = new ArrayList<>();
   
    Map<String,Integer> code_count = new HashMap<String,Integer>();
    Map<String,Integer> taken = new HashMap<String,Integer>();
    
    private void initList()
    {
        for(int i=1;i<=100;i++)
        {
            oid.add("O"+i);
        }
        this.oid=oid;
        //System.out.println(oid);
    }
    private void add_to_order(java.util.Date order_date)
    {
         add_to_map();
         String order_id=null;
         for(String str:oid)
         {
             if(!taken.containsKey(str))
             {
                 order_id=str;
                 break;
             }
         }
         
         try
         {
            Connection con=Connectivity.getConnection();
            String sql ="insert into orders(OrderID,OrderDate,Amount,CID,Discount,OrderStatus) values (?,?,?,?,?,?)";
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setString(1,order_id);
            ps.setDate(2,new java.sql.Date(order_date.getTime()));
            ps.setDouble(3, final_price);
            ps.setString(4, customer);
            ps.setString(5,DISCOUNT.getSelectedItem().toString());
            ps.setString(6,"PENDING");
            ps.execute();
            JOptionPane.showMessageDialog(this,"Saved Successfully");
                
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,e);
        }
         
        add_to_order_details(order_id);
         
    }
    private void add_to_order_details(String oid)
    {
        DefaultTableModel tab = (DefaultTableModel)FC.getModel();
        int rows = tab.getRowCount();
        
        for(int i=0;i<rows;i++)
        {
           
            try
            {
                    Connection con=Connectivity.getConnection();
                    String sql ="insert into orderdetails(OrderID,ICode,Description,Type,UnitPrice,QTY) values (?,?,?,?,?,?)";
                    PreparedStatement ps=con.prepareStatement(sql);
                    ps.setString(1,oid);
                    ps.setString(2,tab.getValueAt(i,0).toString());
                    ps.setString(3,tab.getValueAt(i,1).toString());
                    ps.setString(4,tab.getValueAt(i,4).toString());
                    ps.setDouble(5,Double.parseDouble(tab.getValueAt(i,2).toString()));
                    ps.setInt(6,Integer.parseInt(tab.getValueAt(i,3).toString()));
                    ps.execute();
                    JOptionPane.showMessageDialog(this,"Saved Successfully");
                
            }
            catch(Exception e)
            {
                JOptionPane.showMessageDialog(this,e);
            }
            
        }
        
        reset();
    }
    private void reset()
    {
        code_count.clear();
        DefaultTableModel tab = (DefaultTableModel)FC.getModel();
        tab.setRowCount(0);
        this.final_price=0;
        DATE.setDate(null);
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        PRICE.setText("");
    }
    private void add_to_map()
    {
        
        try
        { 
            Connection cn=Connectivity.getConnection();
            PreparedStatement ps=cn.prepareStatement("select OrderID from orders");
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                taken.put(rs.getString("OrderID"),1);
            }
        }
    catch(Exception e)
    {   
        JOptionPane.showMessageDialog(this,e);
    }
    }
    private void action(Object ob)
    {
       
        
        
        JButton b = (JButton)ob;
        JPanel parent = (JPanel)b.getParent();
        JLabel item = (JLabel)parent.getComponent(0);
        JLabel price = (JLabel)parent.getComponent(1);
        
        add_to_cart(item.getText(),price.getText(),parent.getToolTipText());
      
    }
    private void add_to_cart(String item,String price,String icode)
    {
        if(code_count.containsKey(icode))
        {
            JOptionPane.showMessageDialog(this,"ALREADY IN CART!");
        }
        else if(code_count.size()>4)
        {
            JOptionPane.showMessageDialog(this,"CART LIMIT EXCEEDED!");
        }
        else
        {
            code_count.put(icode,1);
            this.code_count=code_count;
            JOptionPane.showMessageDialog(this,"ADDED TO CART:\n"+icode+"\n"+item+"\n"+price);
            cart_update(item,price,icode);
            jButton1.setEnabled(true);
            jButton2.setEnabled(true);
            jButton3.setEnabled(true);
        }
    }
    private void upd(JPanel panel)
    {
        panel.repaint();
        panel.revalidate();
    }
    private void cart_update(String item,String price,String icode)
    {
        DefaultTableModel tab = (DefaultTableModel)FC.getModel();
        tab.setRowCount(tab.getRowCount());
        tab.addRow(new Object[]{icode,item,price.replace("Rs.",""),1,get_type(icode)});
        update_price(tab);
    }
    private String get_type(String icode)
    {
        try
        {
            Connection cn=Connectivity.getConnection();
            PreparedStatement ps=cn.prepareStatement("select * from item where ICode='"+icode+"'");
            ResultSet rs=ps.executeQuery();
            rs.next();
            return rs.getString("ItemType");
        }
        catch(Exception e)
        {
             JOptionPane.showMessageDialog(this,e);
        }
        return null;
    }
    private void update_price(DefaultTableModel table)
    {
        double p1=0;
        double p2=0;
        double total=0;
        for(int i=0;i<table.getRowCount();i++)
        {
            p1=Double.parseDouble(table.getValueAt(i,2).toString());
            p2=Double.parseDouble(table.getValueAt(i, 3).toString());
            
            total+=p1*p2;
        }
        this.final_price=total;
        PRICE.setText("Total Price = "+final_price);
    }
    private void populate (JPanel parent,String type)
    {
        layout.setAlignment(FlowLayout.LEFT);
        parent.setLayout(layout);
        String item="null";
        String desc="null";
        String icode="null";
        int i=0;
        try
        { 
            JPanel[] panels = new JPanel[100];
            JLabel[] p_label = new JLabel[100];
            JLabel[] desc_label = new JLabel[100];
            JButton[] buttons = new JButton[100];
            
            Connection cn=Connectivity.getConnection();
            PreparedStatement ps=cn.prepareStatement("select * from item where ItemType='"+type+"'");
            ResultSet rs=ps.executeQuery();
            Border blackline = BorderFactory.createLineBorder(Color.black);
            while(rs.next())
            {   
               
                item=rs.getString("UnitPrice");
                desc=rs.getString("Description");
                icode=rs.getString("ICode");
          
                p_label[i] = new JLabel("Rs."+item);
                p_label[i].setSize(70,70);
                p_label[i].setFont(new java.awt.Font("Bahnschrift", 1, 16));
                p_label[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                
                desc_label[i] = new JLabel(desc);
                desc_label[i].setSize(70,70);
                desc_label[i].setFont(new java.awt.Font("Bahnschrift", 1, 16));
                desc_label[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                
                buttons[i] = new JButton(item);
                buttons[i].setSize(70,100);
                buttons[i].setFont(new java.awt.Font("Bahnschrift", 1, 16));
                buttons[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                buttons[i].setText("ADD TO CART");
                                              
                buttons[i].addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                action(evt.getSource());
            }
        });
                
                
                panels[i] = new JPanel();
                panels[i].setLayout(new GridLayout(3,0));
                panels[i].setBorder(blackline);
                panels[i].setPreferredSize(d);
               
                panels[i].add(desc_label[i]);
                upd(panels[i]);
                panels[i].add(p_label[i]);
                upd(panels[i]);
                panels[i].add(buttons[i]);
                upd(panels[i]);
                panels[i].setToolTipText(icode);
                panels[i].setVisible(true);
                
                parent.add(panels[i]);
                upd(parent);
                                
                i++;
            }
            Double c=Math.floor(i/4);
            c++;
            System.out.println(c+" "+c.intValue());
            parent.setPreferredSize(new Dimension(980,(c.intValue())*330));
            
        }
    catch(Exception e)
    {   
        JOptionPane.showMessageDialog(this,e);
    }
      
    }
    public ITEM_SELECTION(String cashier,String customer) {
        initList();
        this.cashier=cashier;
        this.customer=customer;
        initComponents();
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        FC.getTableHeader().setFont(new Font("Bahnschrift", Font.BOLD, 14));
        FC.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        try
        {
            Statement stmt = connectdata();
            ResultSet rs = stmt.executeQuery("Select LName from login where LUser = '"+ cashier +"'");
            rs.next();
            String user =rs.getString(1);

            rs = stmt.executeQuery("Select LType from login where LUser = '"+ cashier +"'");
            rs.next();
            String type=rs.getString(1);
            WELCOME.setText(user+"("+type+")");
            
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,e);
        }
        
        try
        {
            Statement stmt = connectdata();
            ResultSet rs = stmt.executeQuery("Select CName from customer where CID = '"+ customer +"'");
            rs.next();
            String cname = rs.getString("CName");
            WELCOME1.setText(cname);
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this,e);
        }
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        populate(A,"Beverage");
        populate(B,"GB");
        populate(C,"Pizza");
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        MC = new javax.swing.JButton();
        TO = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        VO = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        VO1 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        WELCOME1 = new javax.swing.JLabel();
        WELCOME = new javax.swing.JLabel();
        cart_reset = new javax.swing.JButton();
        BACK = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        HEADER = new javax.swing.JLabel();
        PANEL = new javax.swing.JPanel();
        SPA = new javax.swing.JScrollPane();
        A = new javax.swing.JPanel();
        SPB = new javax.swing.JScrollPane();
        B = new javax.swing.JPanel();
        SPC = new javax.swing.JScrollPane();
        C = new javax.swing.JPanel();
        VC = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        FC = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        DATE = new com.toedter.calendar.JDateChooser();
        jButton4 = new javax.swing.JButton();
        INC = new javax.swing.JButton();
        DEC = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        DISCOUNT = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        PRICE = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Item Menu");
        setIconImage(new ImageIcon(getClass().getResource("image/icon.png")).getImage());
        setResizable(false);

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(51, 51, 51));

        MC.setBackground(new java.awt.Color(0, 0, 0));
        MC.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        MC.setForeground(new java.awt.Color(255, 255, 255));
        MC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rms/image/icons8_bar_52px_1.png"))); // NOI18N
        MC.setText("Beverages");
        MC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MCActionPerformed(evt);
            }
        });

        TO.setBackground(new java.awt.Color(0, 0, 0));
        TO.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        TO.setForeground(new java.awt.Color(255, 255, 255));
        TO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rms/image/icons8_bread_48px_1.png"))); // NOI18N
        TO.setText("Garlic Bread");
        TO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TOActionPerformed(evt);
            }
        });

        VO.setBackground(new java.awt.Color(0, 0, 0));
        VO.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        VO.setForeground(new java.awt.Color(255, 255, 255));
        VO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rms/image/icons8_pizza_70px_1.png"))); // NOI18N
        VO.setText("Pizzas");
        VO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VOActionPerformed(evt);
            }
        });

        VO1.setBackground(new java.awt.Color(0, 0, 0));
        VO1.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        VO1.setForeground(new java.awt.Color(255, 255, 255));
        VO1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rms/image/icons8_shopping_cart_63px_1.png"))); // NOI18N
        VO1.setText("View Cart");
        VO1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VO1ActionPerformed(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rms/image/ic_launcher.png"))); // NOI18N

        WELCOME1.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        WELCOME1.setForeground(new java.awt.Color(255, 255, 255));
        WELCOME1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        WELCOME.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        WELCOME.setForeground(new java.awt.Color(255, 255, 255));
        WELCOME.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        cart_reset.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        cart_reset.setText("RESET CART");
        cart_reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cart_resetActionPerformed(evt);
            }
        });

        BACK.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        BACK.setText("BACK TO CASHIER DASHBOARD");
        BACK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BACKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(WELCOME1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(WELCOME, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                            .addComponent(cart_reset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addComponent(jLabel7)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(BACK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(MC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TO, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                    .addComponent(VO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(VO1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(46, 46, 46))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addGap(1, 1, 1)
                .addComponent(WELCOME, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(WELCOME1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(MC)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TO, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(VO, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(VO1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(cart_reset, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BACK, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 700));

        HEADER.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        HEADER.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HEADER.setText("Item Menu");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(HEADER, javax.swing.GroupLayout.PREFERRED_SIZE, 799, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 181, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addComponent(HEADER)
                .addContainerGap())
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 980, 80));

        PANEL.setLayout(new java.awt.CardLayout());

        SPA.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SPA.setPreferredSize(new java.awt.Dimension(13, 13));

        A.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        SPA.setViewportView(A);

        PANEL.add(SPA, "card6");

        SPB.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SPB.setViewportView(B);

        PANEL.add(SPB, "card5");

        SPC.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        SPC.setViewportView(C);

        PANEL.add(SPC, "card5");

        FC.setBackground(new java.awt.Color(0, 0, 0));
        FC.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        FC.setForeground(new java.awt.Color(255, 255, 255));
        FC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item Code", "Description", "Unit Price", "Quantity", "Food Type"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        FC.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        FC.setIntercellSpacing(new java.awt.Dimension(0, 0));
        FC.setRowHeight(25);
        jScrollPane1.setViewportView(FC);

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("DISCOUNT");

        DATE.setDateFormatString("dd-MMM-yyyy");
        DATE.setFocusTraversalPolicyProvider(true);
        DATE.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N

        jButton4.setBackground(new java.awt.Color(255, 0, 0));
        jButton4.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        jButton4.setText("CONTINUE");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        INC.setBackground(new java.awt.Color(0, 0, 0));
        INC.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        INC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rms/image/icons8_add_70px_3.png"))); // NOI18N
        INC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                INCActionPerformed(evt);
            }
        });

        DEC.setBackground(new java.awt.Color(0, 0, 0));
        DEC.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        DEC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rms/image/icons8_minus_57px_1.png"))); // NOI18N
        DEC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DECActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jButton1.setText("RESET DATE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        DISCOUNT.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        DISCOUNT.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "NO DISCOUNT", "5%", "10%", "20%", "30%" }));

        jButton2.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        jButton2.setText("APPLY");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        jButton3.setText("REMOVE");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("SET ORDER DATE");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(182, 182, 182)
                        .addComponent(jButton1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(DATE, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(62, 62, 62)
                        .addComponent(INC, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(DEC, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jButton2)
                        .addGap(26, 26, 26)
                        .addComponent(jButton3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(DISCOUNT, 0, 158, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))))
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(117, 117, 117)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(620, Short.MAX_VALUE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DEC, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(INC, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(DATE, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(DISCOUNT, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3))))
                .addContainerGap())
            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel4Layout.createSequentialGroup()
                    .addGap(35, 35, 35)
                    .addComponent(jLabel4)
                    .addContainerGap(85, Short.MAX_VALUE)))
        );

        PRICE.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        PRICE.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout VCLayout = new javax.swing.GroupLayout(VC);
        VC.setLayout(VCLayout);
        VCLayout.setHorizontalGroup(
            VCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 980, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(VCLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PRICE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        VCLayout.setVerticalGroup(
            VCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, VCLayout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 413, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PRICE, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                .addContainerGap())
        );

        PANEL.add(VC, "card5");

        jPanel1.add(PANEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 90, 980, 610));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(1396, 739));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void MCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MCActionPerformed
        HEADER.setText("Beverages");
        PANEL.removeAll();
        PANEL.add(SPA);
        PANEL.repaint();
        PANEL.revalidate();
    }//GEN-LAST:event_MCActionPerformed

    private void TOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TOActionPerformed
        HEADER.setText("Garlic Breads");
        PANEL.removeAll();
        PANEL.add(SPB);
        PANEL.repaint();
        PANEL.revalidate();
    }//GEN-LAST:event_TOActionPerformed

    private void VOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VOActionPerformed
        HEADER.setText("Pizzas");
        PANEL.removeAll();
        PANEL.add(SPC);
        PANEL.repaint();
        PANEL.revalidate();
    }//GEN-LAST:event_VOActionPerformed

    private void VO1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VO1ActionPerformed
        HEADER.setText("Order Cart");
        PANEL.removeAll();
        PANEL.add(VC);
        PANEL.repaint();
        PANEL.revalidate();
    }//GEN-LAST:event_VO1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       if(DATE.getDate()==null)
       {
            JOptionPane.showMessageDialog(this,"PLEASE SELECT A DATE!");
            return;
       }
       else if(FC.getRowCount()==0)
       {
           JOptionPane.showMessageDialog(this,"CART IS EMPTY");
           return;
       }
        
       int reply = JOptionPane.showConfirmDialog(this, "ARE YOU SURE YOU WANT TO CONTINUE?", "CONFIRMATION WINDOW",JOptionPane.YES_NO_OPTION);
       if(reply == JOptionPane.NO_OPTION)return;
       
       Format formatter = new SimpleDateFormat("dd-MMM-yyyy");
       String s = formatter.format(DATE.getDate());
       add_to_order(DATE.getDate());
       
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cart_resetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cart_resetActionPerformed

        reset();
    }//GEN-LAST:event_cart_resetActionPerformed

    private void INCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_INCActionPerformed
        if(FC.getSelectionModel().isSelectionEmpty())
       {
           JOptionPane.showMessageDialog(this,"NO CART ITEM SELECTED!");
       }
       else if(!jButton2.isEnabled())
       {
           JOptionPane.showMessageDialog(this,"PLEASE REMOVE DISCOUNTS TO CONTINUE!");
       }
       else
        {
            int row = FC.getSelectedRow();
            if(Double.parseDouble(FC.getValueAt(row, 3).toString())>=5)
                {
                    JOptionPane.showMessageDialog(this,"MAX QUANTITY REACHED FOR THE ITEM!");
                    return;
                }
            Integer new_val = Integer.parseInt(FC.getValueAt(row, 3).toString())+1;
            System.out.print(new_val);
            FC.setValueAt(new_val.toString(), row, 3);
            update_price((DefaultTableModel)FC.getModel());
        }
    }//GEN-LAST:event_INCActionPerformed

    private void DECActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DECActionPerformed
        if(FC.getSelectionModel().isSelectionEmpty())
       {
           JOptionPane.showMessageDialog(this,"NO CART ITEM SELECTED!");
       }
        else if(!jButton2.isEnabled())
       {
           JOptionPane.showMessageDialog(this,"PLEASE REMOVE DISCOUNTS TO CONTINUE!");
       }
       else
        {
            
            int row = FC.getSelectedRow();
            if(Double.parseDouble(FC.getValueAt(row, 3).toString())==1)
                {
                    JOptionPane.showMessageDialog(this,"ITEM WILL BE REMOVED.");
                    
                    try{
                        code_count.remove(FC.getValueAt(row, 0).toString());
                    }catch(Exception e){ JOptionPane.showMessageDialog(this,e);}
                    
                    DefaultTableModel t = (DefaultTableModel)FC.getModel();
                    t.removeRow(row);
                    if(FC.getRowCount()==0)
                        {
                            jButton1.setEnabled(false);
                            jButton2.setEnabled(false);
                            jButton3.setEnabled(false);
                        }
                    update_price((DefaultTableModel)FC.getModel());
                    return;
                }
            Integer new_val = Integer.parseInt(FC.getValueAt(row, 3).toString())-1;
            System.out.print(new_val);
            FC.setValueAt(new_val.toString(), row, 3);
            update_price((DefaultTableModel)FC.getModel());
        }
    }//GEN-LAST:event_DECActionPerformed

    private void BACKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BACKActionPerformed
        this.dispose();
        CASHIER_DASHBOARD ob = new CASHIER_DASHBOARD(ITEM_SELECTION.cashier);
        ob.setVisible(true);
    }//GEN-LAST:event_BACKActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        DATE.setDate(null);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if(DISCOUNT.getSelectedItem().toString().equals("NO DISCOUNT"))
        {
            JOptionPane.showMessageDialog(this,"NO DISCOUNT APPLIED");
            update_price((DefaultTableModel)FC.getModel());
        }
        else
        {
            jButton2.setEnabled(false);
            String s = DISCOUNT.getSelectedItem().toString();
            String d = s.replace("%","");
            JOptionPane.showMessageDialog(this,s+" DISCOUNT APPLIED!");
            double dis = Double.parseDouble(d);
            double discount=dis/100;
            this.final_price = final_price-(final_price*(discount));
            PRICE.setText("Total Price = "+final_price);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        JOptionPane.showMessageDialog(this,"REMOVING APPLIED DISCOUNT");
        update_price((DefaultTableModel)FC.getModel());
        jButton2.setEnabled(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ITEM_SELECTION.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ITEM_SELECTION.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ITEM_SELECTION.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ITEM_SELECTION.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ITEM_SELECTION(cashier,customer).setVisible(true);
            }
        });
    }
    static private String cashier;
    static private String customer;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel A;
    private javax.swing.JPanel B;
    private javax.swing.JButton BACK;
    private javax.swing.JPanel C;
    private com.toedter.calendar.JDateChooser DATE;
    private javax.swing.JButton DEC;
    private javax.swing.JComboBox<String> DISCOUNT;
    private javax.swing.JTable FC;
    private javax.swing.JLabel HEADER;
    private javax.swing.JButton INC;
    private javax.swing.JButton MC;
    private javax.swing.JPanel PANEL;
    private javax.swing.JLabel PRICE;
    private javax.swing.JScrollPane SPA;
    private javax.swing.JScrollPane SPB;
    private javax.swing.JScrollPane SPC;
    private javax.swing.JButton TO;
    private javax.swing.JPanel VC;
    private javax.swing.JButton VO;
    private javax.swing.JButton VO1;
    private javax.swing.JLabel WELCOME;
    private javax.swing.JLabel WELCOME1;
    private javax.swing.JButton cart_reset;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    // End of variables declaration//GEN-END:variables
}
