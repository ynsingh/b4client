package com.ehelpy.brihaspati4.voip;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JDialog;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JTable;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import net.proteanit.sql.DbUtils;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.Font;
import java.awt.Image;
import java.awt.Window;
import javax.swing.border.MatteBorder;
import javax.swing.table.TableModel;
import com.ehelpy.brihaspati4.Address_Book.import_database;
import com.ehelpy.brihaspati4.Address_Book.sqlite_connection;
import com.ehelpy.brihaspati4.Address_Book.upload_csv;
import com.ehelpy.brihaspati4.voip.B4services;
import com.ehelpy.brihaspati4.voip.voip_call;
import com.ehelpy.brihaspati4.voip.voip_rxcall;
import java.awt.Dimension;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.awt.Dialog.ModalityType;


@SuppressWarnings("serial")
public class voip_gui extends JFrame{

	private JPanel contentPane;
	static String Email_Id;
	public static JTable table;
	Connection con = null; //new
	private Popup popup;
//	public static ServerSocketChannel ss = null;
    
	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					voip_gui frame = new voip_gui();
					frame.setVisible(true);
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create frame.
	 */
	
	public voip_gui(){
		
		upload_csv.csv("information2.csv");
		
		B4services.voip_gui_window = true;
	       
		   addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				import_database.main(null);
				try {
					B4services.ss.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				B4services.voip_gui_window = false;
				B4services.service();
			}
		});
		setForeground(Color.RED);
		setSize(new Dimension(5, 0));
		setTitle("VOIP");
		setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 656, 439);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(255, 255, 255));
		scrollPane.setBounds(191, 56, 205, 251);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setSize(new Dimension(2, 2));
		table.setRowHeight(22);
		table.setRowMargin(2);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.setGridColor(Color.BLACK);
		table.setForeground(new Color(139, 0, 0));
		table.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(255, 255, 255)));
		table.setBackground(new Color(255, 228, 181));
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				table.getSelectedRow();
			
			}
		});
		scrollPane.setViewportView(table);
		
		/////////////////////////////////////////////////////////////
		// creating new connection with db and uploading it to JTable
		
		con = sqlite_connection.db_connector();//new
		String query = "SELECT Name,Email_Id  FROM information2 ORDER BY Name";
		try 
		{
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
					
			table.setModel(DbUtils.resultSetToTableModel(rs));
			stmt.close();
			con.close();
		} 
		catch (SQLException e1) 
		{
			e1.printStackTrace();
		}
		
		////////////////////////////////////////////////////////////
		
		JButton CALL = new JButton("");
		CALL.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("CALL");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		CALL.setBorderPainted(false);
		CALL.setContentAreaFilled(false);
		CALL.setHorizontalTextPosition(SwingConstants.LEADING);
		CALL.setHorizontalAlignment(SwingConstants.LEADING);
		CALL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int i = table.getSelectedRow();
				if(i==-1)
				{
					JOptionPane.showMessageDialog(null, "Please SELECT any ENTRY and then CLICK on this.");
				}
				
				else
				{ 
					voip_rxcall.flag = false;
					TableModel model = table.getModel();
					Email_Id = model.getValueAt(i, 1).toString();
					import_database.main(null);
					dispose();

					SwingWorker<Void, Void> mySwingWorker = new SwingWorker<Void, Void>()
				     {
				         @Override
				         protected Void doInBackground() throws Exception {

				            // mimic some long-running process here...
				        	voip_call.callstart_waiting(Email_Id); 
				            return null;
				         }
				     };

				     Window win = SwingUtilities.getWindowAncestor((AbstractButton)e.getSource());
				     final JDialog dialog = new JDialog(win, "Brihaspati 4", ModalityType.APPLICATION_MODAL);

				     mySwingWorker.addPropertyChangeListener
				     (
				    		 new PropertyChangeListener() 
				    		 {

				    			 @Override
				    			 public void propertyChange(PropertyChangeEvent evt)
				    			 {
				    				 if (evt.getPropertyName().equals("state"))
				    				 {
				    					 if (evt.getNewValue() == SwingWorker.StateValue.DONE)
				    					 {
				    						 dialog.dispose();
				    					 }
				    				 }
				    			 }
				    		 }
				      );
				      mySwingWorker.execute();

				      JProgressBar progressBar = new JProgressBar();
				      progressBar.setIndeterminate(true);
				      JPanel panel = new JPanel();
				      panel.add(new JLabel("Please wait while we connect you...."), BorderLayout.PAGE_START);
				      panel.add(progressBar, BorderLayout.PAGE_END);
				      dialog.add(panel);
				      dialog.setPreferredSize(new Dimension(300,100));
				      dialog.pack();
				      dialog.setLocationRelativeTo(win);
				      dialog.setVisible(true);
					
				}
           	}
		});
		Image img = new ImageIcon(this.getClass().getResource("/call.png")).getImage();
		CALL.setIcon(new ImageIcon(img));
		CALL.setBounds(406, 82, 43, 23);
		contentPane.add(CALL);
		
		
		
		JButton MENU = new JButton("");
		MENU.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
                if (popup != null) {
                    popup.hide();
                }
                JLabel text = new JLabel("MAIN MENU");
                popup = PopupFactory.getSharedInstance().getPopup(e.getComponent(), text, e.getXOnScreen(), e.getYOnScreen());
                popup.show();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				popup.hide();
			}
		});
		MENU.setContentAreaFilled(false);
		MENU.setBorderPainted(false);
		MENU.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				import_database.main(null);
				try 
				{
					B4services.ss.close();
				} catch (IOException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				B4services.service();
				B4services.voip_gui_window = false;
				dispose();
				
			}
		});
		Image img7 = new ImageIcon(this.getClass().getResource("/details.png")).getImage();
		MENU.setIcon(new ImageIcon(img7));
		MENU.setBounds(273, 366, 33, 23);
		contentPane.add(MENU);
		
	}
	
}
