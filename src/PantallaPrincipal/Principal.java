package PantallaPrincipal;

import Principales.Conexion;
import Principales.FiltrosTextField;
import Principales.RequestFocusListener;
import Principales.Ticket;
import Principales.VerTabla;
import Productos.Combos.Combos;
import Ordenes.FrameOrdenes;
import Stock.FrameStock;
import Productos.Unidades.Unidades;
import Productos.Variantes.Variantes;
import Stock.ConexionStock;
import Stock.TablaStock;
import Ventas.Productos.FrameVentasProducto;
import Ventas.Productos.FrameVentasProductoDiario;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Franco
 */
public class Principal extends javax.swing.JFrame {

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("Imagenes/if_Turkey_131944.png"));
        return retValue;
    }

    Conexion conn = new Conexion();
    ConexionPrincipal conPrincipal = new ConexionPrincipal();
    ConexionStock constock = new ConexionStock();
    FiltrosTextField filtros = new FiltrosTextField();
    VerTabla v = new VerTabla();
    TablaStock vStock = new TablaStock();
    Botones b = new Botones();
    Integer pedidosya = 3;
    Date fecha22 = new Date(System.currentTimeMillis());

    public Principal() throws SQLException {
        initComponents();
        
        
        txtTelefonoDelivery.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    conPrincipal.cargarNombre(txtTelefonoDelivery.getText(), txtNombreDelivery, txtDireccionDelivery);
                }

           }
        });

        for(int i=1;i==2;)
        {
            System.out.println(i);
        }
        
        filtros.filtroeneteros(txtCosto);
        Integer costo = 0;
        ResultSet rs = conn.select("extras");
        try{
            while(rs.next()){
                costo = rs.getInt(3);
            }
        }catch(Exception ex){
            
        }
        txtCosto.setText(""+costo);

        String turno = "";
        rs = conn.select("extras");
        try {
            while (rs.next()) {
                turno = rs.getString(2);
            }
        } catch (Exception ex) {

        }
        txtElturno.setText(turno);


        conn.settotal( txtOrden,  txtDelivery,  txtVntasDia,  txtVntasDelivery,  txtVntasOtros);
        
        if(Integer.parseInt(txtOrden.getText()) > 0 || Integer.parseInt(txtDelivery.getText()) > 0){
            JOptionPane.showMessageDialog(null, "El turno anterior no fue cerrado. Debe realizar el cierre del turno");
        }

        v.visualizar_tablaventa(tabla, txtTotal);
        vStock.visualizar_stock(tStockeo);

        tabla.setEnabled(false);
        jScrollPane1.setViewportView(tabla);
        
        
        ResultSet rs2 = conn.select("combos");
        
        Integer tCombo = 0;
        Integer identificador = pedidosya;
        
        try{
            while(rs2.next()){
                Integer id = rs2.getInt(1);
                String nombre = rs2.getString(2);
                if(pedidosya == 5){
                    identificador = 4;
                }
                Integer precio = rs2.getInt(identificador);

                JButton por = new JButton();
                por.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent ae) {
                        conn.guardarconsumoventa(nombre, precio,1,1);
                        
                        Connection con = conn.conectar();
                        ResultSet rs3 = null;
                        try {
                            PreparedStatement ps3 = con.prepareStatement("SELECT * FROM comboproducto c RIGHT JOIN  productos p ON c.id_producto = p.id WHERE c.id_combo = "+id);
                            rs3 = ps3.executeQuery();
                            while(rs3.next()){
                                conn.guardarconsumoventa(rs3.getString(5), 0, rs3.getInt(3), 0);
                            }
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        v.visualizar_tablaventa(tabla, txtTotal);
                    }
                });

                //Cargo el icono en un label para luego guardarlo en el button

                JLabel nomprodu = new JLabel(nombre);
                JLabel precprodu = new JLabel();

                precprodu = new JLabel("$"+precio);
                nomprodu.setFont(new Font("Serif", Font.BOLD, 18));
                precprodu.setFont(new Font("Serif", Font.BOLD, 18));

                por.setLayout(new BorderLayout());
                por.add(nomprodu, BorderLayout.CENTER);
                por.add(precprodu, BorderLayout.EAST);

                por.setSize(180,60);
                por.setVisible(true);

                    por.setLocation(1,tCombo);
                    pCombos.add(por);
                    tCombo = tCombo + 65;
                    pCombos.setPreferredSize(new Dimension(180,tCombo));

            }


            }catch(Exception ex){
                System.out.println("Error en cargar productos principal");
            }
        
        
        
        sCombos.setViewportView(pCombos);
        sCombos.getViewport().setView(pCombos);
        pCombos.setLayout(null);

        rs = conn.select("productos");

        Integer tunidades = 0;
        Integer tbebidas = 0;

        try {
            while (rs.next()) {
                String nombre = rs.getString(2);
                Integer precio = rs.getInt(pedidosya);
                Integer tipo = rs.getInt(4);

                JButton por = new JButton();
                por.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        conn.guardarconsumoventa(nombre, precio,1,1);
                        v.visualizar_tablaventa(tabla, txtTotal);
                    }
                });

                //Cargo el icono en un label para luego guardarlo en el button
                JLabel nomprodu = new JLabel(nombre);
                JLabel precprodu = new JLabel();

                precprodu = new JLabel("$" + precio);

                nomprodu.setFont(new Font("Serif", Font.BOLD, 18));
                precprodu.setFont(new Font("Serif", Font.BOLD, 18));

                por.setLayout(new BorderLayout());
                por.add(nomprodu, BorderLayout.CENTER);
                por.add(precprodu, BorderLayout.EAST);

                por.setSize(180, 60);
                por.setVisible(true);

                if (tipo == 0) {
                    por.setLocation(1, tunidades);
                    pUnidad.add(por);
                    tunidades = tunidades + 65;
                    pUnidad.setPreferredSize(new Dimension(180, tunidades));
                }
                if (tipo == 1) {
                    por.setLocation(1, tbebidas);
                    pBebida.add(por);
                    tbebidas = tbebidas + 65;
                    pBebida.setPreferredSize(new Dimension(180, tbebidas));
                }
            }

        } catch (Exception ex) {
            System.out.println("Error en cargar productos principal");
        }

        sCombos.setViewportView(pCombos);
        sCombos.getViewport().setView(pCombos);
        sCombos.getVerticalScrollBar().setUnitIncrement(16);
        pCombos.setLayout(null);

        sUnidad.setViewportView(pUnidad);
        sUnidad.getViewport().setView(pUnidad);
        sUnidad.getVerticalScrollBar().setUnitIncrement(16);
        pUnidad.setLayout(null);

        sBebida.setViewportView(pBebida);
        sBebida.getViewport().setView(pBebida);
        sBebida.getVerticalScrollBar().setUnitIncrement(16);
        pBebida.setLayout(null);
        
        
        rs = conn.select("variantes");
        
        Integer tEnsaladas = 0;
        Integer tSalsas = 0;

        
        try{
            while(rs.next()){
                String nombre = rs.getString(2);
                Integer tipo = rs.getInt(3);

                JButton por = new JButton();
                por.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent ae) {
                        conn.guardarconsumoventa(nombre, 0,1,2);
                        v.visualizar_tablaventa(tabla, txtTotal);
                    }
                });

                //Cargo el icono en un label para luego guardarlo en el button

                JLabel nomprodu = new JLabel(nombre);

                nomprodu.setFont(new Font("Serif", Font.BOLD, 14));

                por.setLayout(new BorderLayout());
                por.add(nomprodu, BorderLayout.CENTER);

                por.setSize(150,40);
                por.setVisible(true);

                if(tipo == 0){
                    por.setLocation(1,tEnsaladas);
                    pEnsalada.add(por);
                    tEnsaladas = tEnsaladas + 42;
                    pEnsalada.setPreferredSize(new Dimension(175,tEnsaladas));
                }
                if(tipo == 1){
                    por.setLocation(1,tSalsas);
                    pSalsa.add(por);
                    tSalsas = tSalsas + 42;
                    pSalsa.setPreferredSize(new Dimension(175,tSalsas));
                }
                }


            }catch(Exception ex){
                System.out.println("Error en cargar productos principal");
            }
        
        sEnsalada.setViewportView(pEnsalada);
        sEnsalada.getViewport().setView(pEnsalada);
        pEnsalada.setLayout(null);
        
        sSalsa.setViewportView(pSalsa);
        sSalsa.getViewport().setView(pSalsa);
        pSalsa.setLayout(null);
        
        

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
        dskPane = new javax.swing.JDesktopPane();
        jPanel3 = new javax.swing.JPanel();
        sBebida = new javax.swing.JScrollPane();
        pBebida = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        sUnidad = new javax.swing.JScrollPane();
        pUnidad = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        sCombos = new javax.swing.JScrollPane();
        pCombos = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tStockeo = new javax.swing.JTable();
        sEnsalada = new javax.swing.JScrollPane();
        pEnsalada = new javax.swing.JPanel();
        sSalsa = new javax.swing.JScrollPane();
        pSalsa = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtTelefonoDelivery = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtNombreDelivery = new javax.swing.JTextField();
        txtDireccionDelivery = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtCosto = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        pedidosyabtn = new javax.swing.JButton();
        txtElturno = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtVntasDelivery = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtVntasDia = new javax.swing.JTextField();
        txtOrden = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtVntasOtros = new javax.swing.JTextField();
        txtDelivery = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnEfectivo = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        cmbVenta = new javax.swing.JComboBox<>();
        btnTarjeta = new javax.swing.JButton();
        btnOnline = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        mnuProductos = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        mnuVentasFechas = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pinto El Pollo");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setIconImage(getIconImage());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        dskPane.setBackground(new java.awt.Color(0, 102, 255));

        jPanel3.setBackground(new java.awt.Color(153, 153, 255));

        sBebida.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pBebida.setOpaque(false);

        javax.swing.GroupLayout pBebidaLayout = new javax.swing.GroupLayout(pBebida);
        pBebida.setLayout(pBebidaLayout);
        pBebidaLayout.setHorizontalGroup(
            pBebidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 438, Short.MAX_VALUE)
        );
        pBebidaLayout.setVerticalGroup(
            pBebidaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 766, Short.MAX_VALUE)
        );

        sBebida.setViewportView(pBebida);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel5.setText("Bebida");

        sUnidad.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pUnidad.setOpaque(false);

        javax.swing.GroupLayout pUnidadLayout = new javax.swing.GroupLayout(pUnidad);
        pUnidad.setLayout(pUnidadLayout);
        pUnidadLayout.setHorizontalGroup(
            pUnidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 238, Short.MAX_VALUE)
        );
        pUnidadLayout.setVerticalGroup(
            pUnidadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 766, Short.MAX_VALUE)
        );

        sUnidad.setViewportView(pUnidad);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel3.setText("Unidad");

        sCombos.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pCombos.setOpaque(false);

        javax.swing.GroupLayout pCombosLayout = new javax.swing.GroupLayout(pCombos);
        pCombos.setLayout(pCombosLayout);
        pCombosLayout.setHorizontalGroup(
            pCombosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 238, Short.MAX_VALUE)
        );
        pCombosLayout.setVerticalGroup(
            pCombosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 766, Short.MAX_VALUE)
        );

        sCombos.setViewportView(pCombos);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel2.setText("Combos");

        tStockeo.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tStockeo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Producto", "Stock"
            }
        ));
        jScrollPane2.setViewportView(tStockeo);

        sEnsalada.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout pEnsaladaLayout = new javax.swing.GroupLayout(pEnsalada);
        pEnsalada.setLayout(pEnsaladaLayout);
        pEnsaladaLayout.setHorizontalGroup(
            pEnsaladaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 215, Short.MAX_VALUE)
        );
        pEnsaladaLayout.setVerticalGroup(
            pEnsaladaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 319, Short.MAX_VALUE)
        );

        sEnsalada.setViewportView(pEnsalada);

        sSalsa.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        pSalsa.setPreferredSize(new java.awt.Dimension(177, 319));

        javax.swing.GroupLayout pSalsaLayout = new javax.swing.GroupLayout(pSalsa);
        pSalsa.setLayout(pSalsaLayout);
        pSalsaLayout.setHorizontalGroup(
            pSalsaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 177, Short.MAX_VALUE)
        );
        pSalsaLayout.setVerticalGroup(
            pSalsaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 319, Short.MAX_VALUE)
        );

        sSalsa.setViewportView(pSalsa);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel6.setText("Ensalada");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel9.setText("Salsa");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Telefono:");

        txtTelefonoDelivery.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtTelefonoDelivery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoDeliveryActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel12.setText("Nombre:");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel14.setText("Direccion:");

        txtNombreDelivery.setEditable(false);
        txtNombreDelivery.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtNombreDelivery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDeliveryActionPerformed(evt);
            }
        });

        txtDireccionDelivery.setEditable(false);
        txtDireccionDelivery.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        txtDireccionDelivery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionDeliveryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDireccionDelivery, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtNombreDelivery, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTelefonoDelivery))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(47, 47, 47))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(39, 39, 39))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtTelefonoDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtNombreDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDireccionDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(101, 101, 101)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(sCombos, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sUnidad, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sBebida, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sEnsalada, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sSalsa)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sEnsalada, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                            .addComponent(sSalsa, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addComponent(sUnidad, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
                    .addComponent(sCombos, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(sBebida, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Cantidad", "Producto", "Total"
            }
        ));
        tabla.setEnabled(false);
        jScrollPane1.setViewportView(tabla);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Total:");

        txtTotal.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTotal.setText("0");
        txtTotal.setFocusable(false);
        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Costo de delivery: ");

        txtCosto.setText("0");
        txtCosto.setFocusable(false);
        txtCosto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCostoActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/if_new-24_103173.png"))); // NOI18N
        jButton1.setToolTipText("Editar Valor Delivery");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        pedidosyabtn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        pedidosyabtn.setText("PedidosYa");
        pedidosyabtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pedidosyabtnActionPerformed(evt);
            }
        });

        txtElturno.setEditable(false);
        txtElturno.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtElturno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtElturnoActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel7.setText("Turno:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtElturno)
                        .addGap(18, 18, 18)
                        .addComponent(pedidosyabtn))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtElturno, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(pedidosyabtn, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel7)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11)
                        .addComponent(txtCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        txtVntasDelivery.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtVntasDelivery.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtVntasDelivery.setFocusable(false);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel10.setText("Delivery");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setText("Efectivo");

        txtVntasDia.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtVntasDia.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtVntasDia.setFocusable(false);
        txtVntasDia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVntasDiaActionPerformed(evt);
            }
        });

        txtOrden.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        txtOrden.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtOrden.setFocusable(false);
        txtOrden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOrdenActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel13.setText("Local");

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel17.setText("En Calle");

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel18.setText("Otros");

        txtVntasOtros.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtVntasOtros.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtVntasOtros.setFocusable(false);
        txtVntasOtros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVntasOtrosActionPerformed(evt);
            }
        });

        txtDelivery.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        txtDelivery.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtDelivery.setFocusable(false);
        txtDelivery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDeliveryActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton2.setText("Ticket");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(204, 255, 204));

        btnEfectivo.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnEfectivo.setText("Efectivo");
        btnEfectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEfectivoActionPerformed(evt);
            }
        });

        btnCancelar.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 51, 51));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        cmbVenta.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        cmbVenta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Local", "Delivery" }));

        btnTarjeta.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnTarjeta.setText("Tarjeta");
        btnTarjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTarjetaActionPerformed(evt);
            }
        });

        btnOnline.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnOnline.setText("Online");
        btnOnline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOnlineActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)
                    .addComponent(cmbVenta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOnline, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEfectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(btnTarjeta, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                        .addComponent(btnOnline, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtVntasDia))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel18)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtVntasOtros, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel17)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtVntasDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton2))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtVntasDia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtVntasDelivery, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(5, 5, 5)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVntasOtros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        dskPane.setLayer(jPanel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dskPane.setLayer(jPanel4, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout dskPaneLayout = new javax.swing.GroupLayout(dskPane);
        dskPane.setLayout(dskPaneLayout);
        dskPaneLayout.setHorizontalGroup(
            dskPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dskPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1012, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        dskPaneLayout.setVerticalGroup(
            dskPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dskPaneLayout.createSequentialGroup()
                .addGroup(dskPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 76, Short.MAX_VALUE))
        );

        jMenu5.setText("Archivo");

        jMenuItem5.setText("Resumenes Por Fecha");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem5);

        jMenuItem4.setText("Cierre De Turno");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem4);

        jMenuBar1.add(jMenu5);

        jMenu1.setText("Productos");

        mnuProductos.setText("Unitarios");
        mnuProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProductosActionPerformed(evt);
            }
        });
        jMenu1.add(mnuProductos);

        jMenuItem6.setText("Combos");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem7.setText("Ensaladas Y Salsas");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ventas");

        jMenuItem9.setText("Ordenes Del Dia");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem11.setText("Ventas Por Producto Del Dia");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);

        mnuVentasFechas.setText("Ventas Por Producto");
        mnuVentasFechas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVentasFechasActionPerformed(evt);
            }
        });
        jMenu2.add(mnuVentasFechas);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Tickets");

        jMenuItem1.setText("Imprimir Ultimo Ticket");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem10.setText("Armar Ticket ");
        jMenu3.add(jMenuItem10);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Varios");
        jMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu4ActionPerformed(evt);
            }
        });

        jMenuItem2.setText("Nuevo Ingreso");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem2);

        jMenuItem3.setText("Nuevo Egreso");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem8.setText("Stock Diario");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(dskPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(dskPane)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEfectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEfectivoActionPerformed

        if (cmbVenta.getSelectedIndex() == 0) {
            b.venta(1, txtTotal, txtOrden, txtDelivery, txtVntasDia, txtVntasDelivery, txtVntasOtros, txtElturno, fecha22, pedidosya, pedidosyabtn, tabla, jScrollPane1, tStockeo);
        } else {
            b.delivery(1,txtTotal, txtOrden, txtDelivery, txtVntasDia, txtCosto, txtVntasDelivery, txtVntasOtros, txtElturno, fecha22, pedidosya, pedidosyabtn, tabla, jScrollPane1, tStockeo, txtTelefonoDelivery.getText());
            txtTelefonoDelivery.setText("");
            txtNombreDelivery.setText("");
            txtDireccionDelivery.setText("");
        }
        vStock.visualizar_stock(tStockeo);
        cmbVenta.setSelectedIndex(0);
    }//GEN-LAST:event_btnEfectivoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        conn.vaciar("venta");
        v.visualizar_tablaventa(tabla, txtTotal);

        tabla.setEnabled(false);
        jScrollPane1.setViewportView(tabla);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void mnuProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProductosActionPerformed
        Unidades miProducto = new Unidades(tabla, txtTotal, pUnidad, pBebida, sUnidad, sBebida);
        dskPane.add(miProducto);
        miProducto.show();
    }//GEN-LAST:event_mnuProductosActionPerformed

    private void mnuVentasFechasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVentasFechasActionPerformed
        FrameVentasProducto enfechas;
        try {
            enfechas = new FrameVentasProducto();
            enfechas.setLocation(100, 20);
            dskPane.add(enfechas);
            enfechas.show();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuVentasFechasActionPerformed

    private void jMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (txtCosto.isFocusable()) {
            txtCosto.setFocusable(false);
            Integer costo = Integer.parseInt(txtCosto.getText());
            String update = "UPDATE extras SET costodelivery = ?";
            Connection con = conn.conectar();
            PreparedStatement ps = null;
            try {
                ps = con.prepareStatement(update);
                ps.setInt(1, costo);
                ps.executeUpdate();

            } catch (Exception ex) {
                System.out.println(ex);
            }
        } else {
            txtCosto.setFocusable(true);
            txtCosto.requestFocusInWindow();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtCostoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCostoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCostoActionPerformed

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        ResultSet rs = conn.select("ordenes ORDER BY id ASC");
        String ultimoticket = "Aun no se emitio ticket";

        try {
            while (rs.next()) {
                ultimoticket = rs.getString(6);
            }
        } catch (Exception ex) {

        }
        
        Ticket.ImprimirDocumento(ultimoticket);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void txtElturnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtElturnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtElturnoActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        JTextField txDescripcion = new JTextField(20);
        txDescripcion.addAncestorListener(new RequestFocusListener());
        JTextField txGasto = new JTextField(20);
        filtros.filtrotamaño(txDescripcion, 29);
        filtros.filtrotamañodecimal(txGasto, 9);
        filtros.filtrodecimales(txGasto);
        filtros.evitarPegar(txDescripcion);
        filtros.evitarPegar(txGasto);

        JPanel mySalida = new JPanel();
        mySalida.setLayout(new BoxLayout(mySalida, BoxLayout.Y_AXIS));
        mySalida.add(new JLabel("Descripcion:"));
        mySalida.add(txDescripcion);
        mySalida.add(new JLabel("Total:"));
        mySalida.add(txGasto);

        int confirm = JOptionPane.showConfirmDialog(null, mySalida, "Ingreso", JOptionPane.OK_CANCEL_OPTION);

        if (confirm != 0) {
            return;
        }

        if (txDescripcion.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debe escribir una descripcion", "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (txGasto.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debe indicar el importe", "", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            conn.guardar_ingreso(txDescripcion.getText(), Double.parseDouble(txGasto.getText()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Hubo un error al guardar el ingreso", "", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        JTextField txDescripcion = new JTextField(20);
        txDescripcion.addAncestorListener(new RequestFocusListener());
        JTextField txGasto = new JTextField(20);
        filtros.filtrotamaño(txDescripcion, 29);
        filtros.filtrotamañodecimal(txGasto, 9);
        filtros.filtrodecimales(txGasto);

        JPanel mySalida = new JPanel();
        mySalida.setLayout(new BoxLayout(mySalida, BoxLayout.Y_AXIS));
        mySalida.add(new JLabel("Descripcion:"));
        mySalida.add(txDescripcion);
        mySalida.add(new JLabel("Importe:"));
        mySalida.add(txGasto);

        int confirm = JOptionPane.showConfirmDialog(null, mySalida, "Salida", JOptionPane.OK_CANCEL_OPTION);

        if (confirm != 0) {
            return;
        }

        if (txDescripcion.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debe escribir una descripcion", "", JOptionPane.WARNING_MESSAGE);
            return;
        } else if (txDescripcion.getText().length() > 29) {
            JOptionPane.showMessageDialog(null, "La descripcion no puede contener mas de 30 caracteres", "", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (txGasto.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Debe indicar el importe", "", JOptionPane.WARNING_MESSAGE);
            return;
        } else if (txGasto.getText().length() > 9) {
            JOptionPane.showMessageDialog(null, "El importe no debe contener mas de 10 caracteres", "", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            conn.guardar_salida(txDescripcion.getText(), Double.parseDouble(txGasto.getText()));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Hubo un error al guardar el egreso", "", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        JTextArea txResumen = new JTextArea();
        Double totalsalidas = 0.00;
        Double totalingresos = 0.00;
        Double local = 0.00;
        Double delivery = 0.00;
        Double otros = 0.00;
        Double total = 0.00;
        Double efectivo = 0.00;
        Double debito = 0.00;
        Double online = 0.00;
        Double pedidosya = 0.00;
        Double pedidosyaeft = 0.00;

        JTextField txNombre = new JTextField(20);
        txNombre.addAncestorListener(new RequestFocusListener());
        filtros.filtrotamaño(txNombre, 10);
        filtros.filtroeneteros(txNombre);

        JPanel myPanelPrincipal = new JPanel();
        myPanelPrincipal.setLayout(new BoxLayout(myPanelPrincipal, BoxLayout.Y_AXIS));
        myPanelPrincipal.add(new JLabel("Efectivo:"));
        myPanelPrincipal.add(txNombre);
        txNombre.requestFocusInWindow();

        int conf = JOptionPane.showConfirmDialog(null, myPanelPrincipal, "Efectivo total", JOptionPane.OK_CANCEL_OPTION);

        if (conf != 0) {
            return;
        }

        Double saldo = Double.parseDouble(txNombre.getText());
        local = Double.parseDouble(txtVntasDia.getText());
        delivery = Double.parseDouble(txtVntasDelivery.getText());
        otros = Double.parseDouble(txtVntasOtros.getText());
        total = local + delivery + otros;

        txResumen.append("Total De Ventas: $" + total);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("   Efectivo: $" + local);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("   Delivery: $" + delivery +" (" + txtDelivery.getText() + ")");
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("   Otros: $" + otros);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("Otros ingresos:"); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea

        ResultSet rs = null;

        rs = conn.select("ingresos");
        try {
            while (rs.next()) {
                txResumen.append("   " + rs.getString(1) + " $" + rs.getDouble(2));
                txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
                totalingresos = totalingresos + rs.getDouble(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        total = total + totalingresos;
        txResumen.append("Total Neto: $" + total);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("Salidas:");
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea

        rs = conn.select("salidas");
        try {
            while (rs.next()) {
                txResumen.append("   " + rs.getString(1) + " $" + rs.getDouble(2));
                txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
                totalsalidas = totalsalidas + rs.getDouble(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        total = total - totalsalidas;
        txResumen.append("Total Bruto: $" + total);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea

        Connection con = conn.conectar();
        ResultSet medios = null;

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM ordenes");
            medios = ps.executeQuery();
            while (medios.next()) {
                String tipo = medios.getString(1);
                Double toti = medios.getDouble(2);
                Integer pago = medios.getInt(4);
                switch (pago) {
                    case 1:
                        efectivo = efectivo + toti;
                        if(tipo.equals("pedidosya")){
                            pedidosyaeft = pedidosyaeft + toti;
                        }
                        break;
                    case 3:
                        online = online + toti;
                        if(tipo.equals("pedidosya")){
                            pedidosya = pedidosya + toti;
                        }
                        break;
                    case 2:
                        debito = debito + toti;
                        break;
                }
    
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }
        
        Double sobrante = saldo - total + debito  + online;

        txResumen.append("Efectivo en Caja: $" + saldo);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("   PedidosYa: $" + pedidosyaeft);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("Debito: $" + debito);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("Online: $" + online);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("   MercadoPAgo: $" + (online-pedidosya));
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("   PedidosYa: $" + pedidosya);
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        if(sobrante >= 0){
            txResumen.append("Hay Un Sobrante De: $" + sobrante);
        }else{
            txResumen.append("Hay Un Faltante De: $" + sobrante);
        }
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("Se vendio: ");
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        
        rs = conn.select("stock");

        try {
            String nombre = "";
            Integer contar = 0;
            while (rs.next()) {
                contar = 0;
                nombre = rs.getString(1);
                ResultSet rs2 = constock.contarVentas(nombre);
                while (rs2.next()) {
                    contar = contar + rs2.getInt(1);
                }
                
                txResumen.append("  " + rs.getString(1) + "..." + contar);
                txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
                totalingresos = totalingresos + rs.getDouble(2);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        txResumen.append("Quedo: ");
        txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
        
        rs = conn.select("stock");

        try {
            String nombre = "";
            Integer descontar = 0;
            while (rs.next()) {
                descontar = 0;
                nombre = rs.getString(1);
                ResultSet rs2 = constock.contarVentas(nombre);
                while (rs2.next()) {
                    descontar = descontar + rs2.getInt(1);
                }
                descontar = rs.getInt(2) - descontar;
                
                txResumen.append("  " + rs.getString(1) + "..." + descontar);
                txResumen.append(System.getProperty("line.separator")); // Esto para el salto de línea
                totalingresos = totalingresos + rs.getDouble(2);

            }
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        

        JButton por = new JButton();
        por.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String strin = txResumen.getText();
                Ticket.ImprimirDocumento(strin);
            }
        });
        
        JLabel nomprodu = new JLabel("Imprimir");
        nomprodu.setFont(new Font("Serif", Font.BOLD, 18));
        por.setLayout(new BorderLayout());
        por.add(nomprodu, BorderLayout.CENTER);
        por.setVisible(true);
        
        JPanel myTelefono = new JPanel();
        
        JButton cierre = new JButton();
        cierre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int rta = JOptionPane.showConfirmDialog(rootPane, "Realizar esta accion imprimira los totales del dia y cerrara el turno, desea continuar?");
                if (rta != 0) {
                    return;
                }
                
                String ticketdeldia = txResumen.getText();
                conn.guardarTicket(ticketdeldia, txtElturno.getText());

                String sdf = new SimpleDateFormat("dd-MM-yyyy").format(fecha22);
                Ticket.ImprimirDocumento("================================\n" + "Cierre del dia: " + sdf + "\nTurno: " + txtElturno.getText() + "\n================================\n" + "Cantidad de ordenes: " + txtOrden.getText() + "\n" + "Total: $" + txtVntasDia.getText() + "\n================================\n" + "Cantidad de deliverys: " + txtDelivery.getText() + "\n" + "Total: $" + txtVntasDelivery.getText() + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n" + "\n");

                if (txtElturno.getText().equals("Mañana")) {
                    String saludo = "Tarde";
                    String update = "UPDATE extras SET turno = ?";
                    Connection con = conn.conectar();
                    PreparedStatement ps = null;
                    try {
                        ps = con.prepareStatement(update);
                        ps.setString(1, saludo);
                        ps.executeUpdate();

                    } catch (Exception ex) {
                        System.out.println("Error al actualizar");
                    } 
                    ResultSet diarias = conn.select("vtaproductos");
                    try{
                        while(diarias.next()){
                            conPrincipal.guardarVentas(diarias.getInt(1), diarias.getString(2), diarias.getInt(3), diarias.getString(4), "Mañana");
                        }
                        diarias.close();
                        
                    }catch(Exception ex){
                        System.out.print(ex);
                    }

                    txtElturno.setText(saludo);

                } else {
                    String saludo = "Mañana";
                    String update = "UPDATE extras SET turno = ?";
                    Connection con = conn.conectar();
                    PreparedStatement ps = null;
                    try {
                        ps = con.prepareStatement(update);
                        ps.setString(1, saludo);
                        ps.executeUpdate();
                    } catch (Exception ex) {
                        System.out.println("Error al actualizar");
                    }
                    
                    ResultSet diarias = conn.select("vtaproductos");
                    try{
                        while(diarias.next()){
                            conPrincipal.guardarVentas(diarias.getInt(1), diarias.getString(2), diarias.getInt(3), diarias.getString(4), "Tarde");
                        }
                        diarias.close();
                        
                    }catch(Exception ex){
                        System.out.print(ex);
                    }

                    txtElturno.setText(saludo);
                }
                conn.vaciar("salidas");
                conn.vaciar("ordenes");
                conn.vaciar("ingresos");
                conn.vaciar("vtaproductos");
                conn.settotal(txtOrden, txtDelivery, txtVntasDia, txtVntasDelivery, txtVntasOtros);
                System.exit(0);
            }
        });

        nomprodu = new JLabel("Cerrar Turno");
        nomprodu.setFont(new Font("Serif", Font.BOLD, 18));
        cierre.setLayout(new BorderLayout());
        cierre.add(nomprodu, BorderLayout.CENTER);
        cierre.setVisible(true);
        
        txResumen.setEditable(false);

        JScrollPane scroll = new JScrollPane(txResumen);

        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        myTelefono.setPreferredSize(new Dimension(300, 500));

        
        myTelefono.setLayout(new BoxLayout(myTelefono, BoxLayout.Y_AXIS));
        myTelefono.add(scroll, BorderLayout.CENTER);

        JOptionPane.showOptionDialog(null, myTelefono, "Resumen", JOptionPane.DEFAULT_OPTION, JOptionPane.DEFAULT_OPTION, null, new Object[]{por, cierre}, null);
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void txtVntasDiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVntasDiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVntasDiaActionPerformed

    private void pedidosyabtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pedidosyabtnActionPerformed
        if (pedidosya == 3) {
            pedidosya = 5;
            pedidosyabtn.setText("Local");
        } else {
            pedidosya = 3;
            pedidosyabtn.setText("PedidosYa");
        }

        pCombos.removeAll();
        pUnidad.removeAll();
        pBebida.removeAll();
        
        
        ResultSet rs2 = conn.select("combos");
        
        Integer tCombo = 0;
        Integer identificador = pedidosya;
        
        try{
            while(rs2.next()){
                Integer id = rs2.getInt(1);
                String nombre = rs2.getString(2);
                if(pedidosya == 5){
                    identificador = 4;
                }
                Integer precio = rs2.getInt(identificador);

                JButton por = new JButton();
                por.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent ae) {
                        conn.guardarconsumoventa(nombre, precio,1,1);
                        
                        
                        Connection con = conn.conectar();
                        ResultSet rs3 = null;
                        try {
                            PreparedStatement ps3 = con.prepareStatement("SELECT * FROM comboproducto c RIGHT JOIN  productos p ON c.id_producto = p.id WHERE c.id_combo = "+id);
                            rs3 = ps3.executeQuery();
                            while(rs3.next()){
                                conn.guardarconsumoventa(rs3.getString(5), 0, rs3.getInt(3),0);
                            }
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        v.visualizar_tablaventa(tabla, txtTotal);
                    }
                });

                //Cargo el icono en un label para luego guardarlo en el button

                JLabel nomprodu = new JLabel(nombre);
                JLabel precprodu = new JLabel();

                precprodu = new JLabel("$"+precio);
                nomprodu.setFont(new Font("Serif", Font.BOLD, 18));
                precprodu.setFont(new Font("Serif", Font.BOLD, 18));

                por.setLayout(new BorderLayout());
                por.add(nomprodu, BorderLayout.CENTER);
                por.add(precprodu, BorderLayout.EAST);

                por.setSize(180,60);
                por.setVisible(true);

                    por.setLocation(1,tCombo);
                    pCombos.add(por);
                    tCombo = tCombo + 65;
                    pCombos.setPreferredSize(new Dimension(180,tCombo));

            }


            }catch(Exception ex){
                System.out.println("Error en cargar productos principal");
            }
        
        
        
        sCombos.setViewportView(pCombos);
        sCombos.getViewport().setView(pCombos);
        pCombos.setLayout(null);

        ResultSet rs = conn.select("productos");

        
        Integer tunidades = 0;
        Integer tbebidas = 0;

        try {
            while (rs.next()) {
                Integer id = rs.getInt(1);
                String nombre = rs.getString(2);
                Integer precio = rs.getInt(pedidosya);
                Integer tipo = rs.getInt(4);

                JButton por = new JButton();
                por.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        conn.guardarconsumoventa(nombre, precio,1,1);
                        v.visualizar_tablaventa(tabla, txtTotal);
                    }
                });

                //Cargo el icono en un label para luego guardarlo en el button
                JLabel nomprodu = new JLabel(nombre);
                JLabel precprodu = new JLabel();

                precprodu = new JLabel("$" + precio);

                nomprodu.setFont(new Font("Serif", Font.BOLD, 18));
                precprodu.setFont(new Font("Serif", Font.BOLD, 18));

                por.setLayout(new BorderLayout());
                por.add(nomprodu, BorderLayout.CENTER);
                por.add(precprodu, BorderLayout.EAST);

                por.setSize(180, 60);
                por.setVisible(true);

                if (tipo == 0) {
                    por.setLocation(1, tunidades);
                    pUnidad.add(por);
                    tunidades = tunidades + 65;
                    pUnidad.setPreferredSize(new Dimension(180, tunidades));
                }
                if (tipo == 1) {
                    por.setLocation(1, tbebidas);
                    pBebida.add(por);
                    tbebidas = tbebidas + 65;
                    pBebida.setPreferredSize(new Dimension(180, tbebidas));
                }
            }

        } catch (Exception ex) {
            System.out.println("Error en cargar productos principal");
        }

        sCombos.setViewportView(pCombos);
        sCombos.getViewport().setView(pCombos);
        sCombos.getVerticalScrollBar().setUnitIncrement(16);
        pCombos.setLayout(null);

        sUnidad.setViewportView(pUnidad);
        sUnidad.getViewport().setView(pUnidad);
        sUnidad.getVerticalScrollBar().setUnitIncrement(16);
        pUnidad.setLayout(null);

        sBebida.setViewportView(pBebida);
        sBebida.getViewport().setView(pBebida);
        sBebida.getVerticalScrollBar().setUnitIncrement(16);
        pBebida.setLayout(null);

    }//GEN-LAST:event_pedidosyabtnActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        TicketsPorFecha miResumen = new TicketsPorFecha();
        dskPane.add(miResumen);
        miResumen.show();
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void txtOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrdenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrdenActionPerformed

    private void txtDeliveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDeliveryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDeliveryActionPerformed

    private void txtVntasOtrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVntasOtrosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVntasOtrosActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        Combos miCombo = new Combos(tabla, txtTotal, pCombos, sCombos);
        dskPane.add(miCombo);
        miCombo.show();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        Variantes miVariante = new Variantes(tabla, txtTotal, pEnsalada, pSalsa, sEnsalada, sSalsa);
        dskPane.add(miVariante);
        miVariante.show();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void btnOnlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOnlineActionPerformed
        
        if (cmbVenta.getSelectedIndex() == 0) {
            b.venta(3, txtTotal, txtOrden, txtDelivery, txtVntasDia, txtVntasDelivery, txtVntasOtros, txtElturno, fecha22, pedidosya, pedidosyabtn, tabla, jScrollPane1, tStockeo);
        } else {
            b.delivery(3,txtTotal, txtOrden, txtDelivery, txtVntasDia, txtCosto, txtVntasDelivery, txtVntasOtros, txtElturno, fecha22, pedidosya, pedidosyabtn, tabla, jScrollPane1, tStockeo, txtTelefonoDelivery.getText());
            txtTelefonoDelivery.setText("");
            txtNombreDelivery.setText("");
            txtDireccionDelivery.setText("");
        }
        vStock.visualizar_stock(tStockeo);
        cmbVenta.setSelectedIndex(0);
    }//GEN-LAST:event_btnOnlineActionPerformed

    private void btnTarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTarjetaActionPerformed
        
        if (cmbVenta.getSelectedIndex() == 0) {
            b.venta(2, txtTotal, txtOrden, txtDelivery, txtVntasDia, txtVntasDelivery, txtVntasOtros, txtElturno, fecha22, pedidosya, pedidosyabtn, tabla, jScrollPane1, tStockeo);
        } else {
            b.delivery(2,txtTotal, txtOrden, txtDelivery, txtVntasDia, txtCosto, txtVntasDelivery, txtVntasOtros, txtElturno, fecha22, pedidosya, pedidosyabtn, tabla, jScrollPane1, tStockeo, txtTelefonoDelivery.getText());
            txtTelefonoDelivery.setText("");
            txtNombreDelivery.setText("");
            txtDireccionDelivery.setText("");
        }
        vStock.visualizar_stock(tStockeo);
        cmbVenta.setSelectedIndex(0);
    }//GEN-LAST:event_btnTarjetaActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        FrameOrdenes miOrdenes = new FrameOrdenes(txtOrden,  txtDelivery,  txtVntasDia,  txtVntasDelivery,  txtVntasOtros, txtElturno.getText(), tStockeo);
        dskPane.add(miOrdenes);
        miOrdenes.show();
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        FrameStock miStock = new FrameStock(tStockeo, txtElturno.getText());
        dskPane.add(miStock);
        miStock.show();
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void txtTelefonoDeliveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoDeliveryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoDeliveryActionPerformed

    private void txtNombreDeliveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDeliveryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDeliveryActionPerformed

    private void txtDireccionDeliveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionDeliveryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionDeliveryActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        byte[] normalText = new byte[]{0x1D, 0x21, 0x00};
        byte[] normalSize = new byte[]{0x1D, 0x21, 0x00};
        byte[] largeSize = new byte[]{0x1D, 0x21, 0x11};
        byte[] boldOn = new byte[]{0x1b, 0x45, 0x01};
        byte[] boldOff = new byte[]{0x1b, 0x45, 0x00};
        byte[] doubleWidth = new byte[]{0x1b, 0x21, 0x20};
        byte[] doubleHeight = new byte[]{0x1b, 0x21, 0x10};
        byte[] left = new byte[]{0x1b, 0x61, 0x00};
        byte[] rigth = new byte[]{0x1b, 0x61, 0x02};
        byte[] center = new byte[]{0x1b, 0x61, 0x01};
        byte[] imagen = new byte[]{0x1d, 0x76, 0x30, 0x00};
        byte[] bb = new byte[]{0x1D, 0x21, 0x35};
        Integer sorteo = 0;
        
        ResultSet rs = conn.select("extras");
        try{
            while(rs.next()){
                sorteo = rs.getInt(1);
            }
        }catch(Exception ex){
            
        }
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Ticket.tamañoletra(boldOn);
        Ticket.tamañoletra(center);
        Ticket.tamañoletra(largeSize);
        Ticket.ImprimirDocumento("Pinto el pollo\n");
        Ticket.tamañoletra(bb);
        Ticket.ImprimirDocumento("Numero:\n" + sorteo + "\n\n");
        Ticket.tamañoletra(normalText);
        Ticket.tamañoletra(doubleHeight);
        Ticket.tamañoletra(left);
        Ticket.ImprimirDocumento("Dejenos sus datos para participar del sorteo\n\nNombre:\n\n\nTelefono:\n\n\n Fecha: "+dateFormat.format(fecha22)+"\n\n\n\n\n\n\n");
        
        
        String update = "UPDATE extras SET sorteo = sorteo + 1";
        Connection con = conn.conectar();
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(update);

            ps.executeUpdate();

        } catch (Exception ex) {
            System.out.println("Error al actualizar");
        }

        
        
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        FrameVentasProductoDiario enfechas;
        try {
            enfechas = new FrameVentasProductoDiario();
            enfechas.setLocation(100, 20);
            dskPane.add(enfechas);
            enfechas.show();
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

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
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Principal().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEfectivo;
    private javax.swing.JButton btnOnline;
    private javax.swing.JButton btnTarjeta;
    private javax.swing.JComboBox<String> cmbVenta;
    private javax.swing.JDesktopPane dskPane;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem mnuProductos;
    private javax.swing.JMenuItem mnuVentasFechas;
    private javax.swing.JPanel pBebida;
    public javax.swing.JPanel pCombos;
    private javax.swing.JPanel pEnsalada;
    private javax.swing.JPanel pSalsa;
    private javax.swing.JPanel pUnidad;
    private javax.swing.JButton pedidosyabtn;
    private javax.swing.JScrollPane sBebida;
    private javax.swing.JScrollPane sCombos;
    private javax.swing.JScrollPane sEnsalada;
    private javax.swing.JScrollPane sSalsa;
    private javax.swing.JScrollPane sUnidad;
    private javax.swing.JTable tStockeo;
    private javax.swing.JTable tabla;
    private javax.swing.JTextField txtCosto;
    private javax.swing.JTextField txtDelivery;
    private javax.swing.JTextField txtDireccionDelivery;
    private javax.swing.JTextField txtElturno;
    private javax.swing.JTextField txtNombreDelivery;
    private javax.swing.JTextField txtOrden;
    private javax.swing.JTextField txtTelefonoDelivery;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtVntasDelivery;
    private javax.swing.JTextField txtVntasDia;
    private javax.swing.JTextField txtVntasOtros;
    // End of variables declaration//GEN-END:variables
}
