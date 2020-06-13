package com.ehelpy.brihaspati4.DFS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Class responsible for Displaying the files uploaded to DFS.
 * This class displays the files that has been uploaded to DFS
 * after retrieving the index from uploaded.csv file
 * <p><b>Functions:</b> At the user end of DFS.</p>
 * <b>Note:</b> Change this file to change functionality
 * related to displaying files
 * @author <a href="https://t.me/sidharthiitk">Sidharth Patra</a>
 * @since   15th Feb 2020
 */
public class ListFiles {
    // for using the comma as delimiter in csv
    // any symbol can be used such as : or ::
    private final static String CSV_DELIMIT = ",";
    /**
     * Starts the listing process.
     * This method lists the uploaded file by displaying them
     * in a GUI. It also performs the actions on clicking the
     * download and delete buttons.
     * @throws IOException for input output exception
     * @throws IllegalArgumentException if illegal argument such as null are passed
     */
    public static void start() throws IllegalArgumentException, IOException {

        String fileName = "uploaded.csv";
        //Table related tasks
        TableModel tableModel = array2table(csv2array(fileName));
        JTable table = new JTable(tableModel);
        Font font = new Font("Verdana", Font.PLAIN, 12);
        table.setFont(font);
        table.setRowHeight(30);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        //Frame related tasks
        JFrame frame = new JFrame();
        JTextField textField = new JTextField();
        frame.setSize(600, 400);
        Color c3 = new Color(3, 1, 1, 192);
        frame.getContentPane().setBackground(c3);
        frame.add(new JScrollPane(table));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2,
                dim.height/2-frame.getSize().height/2);
        frame.setVisible(true);

        // download button related tasks
        JButton download = new JButton("Download");
        download.setBounds(595, 150, 195, 30);
        download.setBackground(Color.white);
        Color c4 = new Color(0, 0, 0, 180);
        download.setForeground(c4);
        download.setFont(new Font("Times New Roman", Font.BOLD, 20));

        // delete button related tasks
        JButton delete = new JButton("Delete");
        delete.setBounds(595, 195, 195, 30);
        delete.setBackground(Color.white);
        Color c2 = new Color(0, 0, 0, 180);
        delete.setForeground(c2);
        delete.setFont(new Font("Times New Roman", Font.BOLD, 20));

        // action listener for download
        download.addActionListener(e -> {
        if (table.getSelectedRows().length > 0) {
            int[] selectedRows = table.getSelectedRows();
            for (int selectedRow : selectedRows) {
                String fileSelected = table.getValueAt(selectedRow, 0).toString();
                JFrame frame1 = new JFrame("message");
                JOptionPane.showMessageDialog(frame1, "You Selected the file "
                        + fileSelected + "\n");
                //call the download class once user clicks on download
                try {
                    Download.start(fileSelected);
                } catch (IOException | GeneralSecurityException ex) {
                    ex.printStackTrace();
                }
            }
        }
        else{
            JFrame frame1 = new JFrame("message");
            JOptionPane.showMessageDialog(frame1,"Please select any of the files" );
        }
        });//action listener ends

        // action listener for delete
        delete.addActionListener(e -> {
            if (table.getSelectedRows().length > 0) {
                int[] selectedRows = table.getSelectedRows();
                for (int selectedRow : selectedRows) {
                    String fileSelected = table.getValueAt(selectedRow, 0).toString();
                    JFrame frame1 = new JFrame("message");
                    JOptionPane.showMessageDialog(frame1, "Confirm Delete "
                            + fileSelected + "\n");
                    try {
                        Delete.start("uploaded.csv", fileSelected);
                    } catch (IOException | NoSuchAlgorithmException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            else{
                JFrame frame1 = new JFrame("message");
                JOptionPane.showMessageDialog(frame1,"Please select any of the files" );
            }
        });//action listener ends

        //add the download button to frame
        frame.add(download);
        frame.add(textField);
        frame.setSize(800, 400);
        frame.setLayout(null);
        frame.setVisible(true);

        //add the delete button to frame
        frame.add(delete);
        frame.add(textField);
        frame.setSize(800, 400);
        frame.setLayout(null);
        frame.setVisible(true);
    }
    /**
     * Use this class to generate TableModel for JTable
     * import auxx.File;(Sarthak's Code)
     * String fileName = "userInfo.csv";
     * JTable jTable = new JTable(File.array2table(File.csv2array(fileName)));
     * @param array receive array after the CSV file has been read and create tableModel from it
     * @return tableModel object of default Table Model Type
     * If the tableModel Method is edited the layout in GUI can be changed
     */
    public static TableModel array2table(ArrayList<ArrayList<String>> array) {
        int maxColumn = 0;
        int rowSize = array.size();
        for (ArrayList<String> strings : array) {
            int columnSize = strings.size();
            if (columnSize > maxColumn) maxColumn = columnSize;
        }
        TableModel tableModel = new DefaultTableModel(rowSize, maxColumn);
        for(int i=0; i<rowSize; i++)
            for(int j=0; j<array.get(i).size(); j++)
                tableModel.setValueAt(array.get(i).get(j), i, j);
        return tableModel;
    }
    /**
     * Use this function to read a .CSV file into 2D ArrayList of String data(Sarthak's Code)
     * import auxx.File
     * modification to line 4th line of while loop to display only one column in GUI
     * Reference -
     * <ul>
     *  <li><a href="https://tools.ietf.org/html/rfc4180">RFC 4180</a> dated Oct 2005</li>
     *  <li><a href="https://tools.ietf.org/html/rfc7111">RFC 7111</a> dated Jan 2014</li>
     * </ul>
     * @param fileName <name>.csv of source file (or its complete path from code_jar root directory)
     * @return 2D ArrayList of String data
     */
    public static ArrayList<ArrayList<String>> csv2array(String fileName)
            throws IOException {
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        String path = System.getProperty("user.dir") + System.getProperty("file.separator") + fileName;
        FileReader file = new FileReader(path);
        Scanner file2record = new Scanner(file);
        while(file2record.hasNextLine()) {
            Scanner record2value = new Scanner(file2record.nextLine());
            record2value.useDelimiter(CSV_DELIMIT);
            ArrayList<String> record = new ArrayList<>();
            //while(record2value.hasNext())//activate line for seeing all the columns
                record.add(record2value.next());
            table.add(record);
            record2value.close();
        }
        file2record.close();
        file.close();
        return table;
    }
}
