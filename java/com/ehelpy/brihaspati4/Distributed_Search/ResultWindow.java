package com.ehelpy.brihaspati4.Distributed_Search;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;


public class ResultWindow extends JFrame{
    
    public static List<Result> final_result=new ArrayList<Result>();
    static String[] s={"File: E:\\SEWA\\HUMAN RESOURCE\\RANJAN JI\\Search\\data\\three.txt\n",
"File: E:\\SEWA\\HUMAN RESOURCE\\RANJAN JI\\Search\\data\\one.txt\n",
"File: E:\\SEWA\\HUMAN RESOURCE\\RANJAN JI\\Search\\data\\two.txt\n",
"File: E:\\SEWA\\HUMAN RESOURCE\\RANJAN JI\\Search\\data\\five.txt\n",
"File: E:\\SEWA\\HUMAN RESOURCE\\RANJAN JI\\Search\\data\\four.txt"};
    public static void main(String[] args)
    {
        showDemoWindow();
    }
    static void links(Map<String,String> mp)
    {
        Iterator it = mp.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry pair = (Map.Entry)it.next();
        System.out.println(pair.getKey());
        String key=pair.getKey().toString();
        String result=pair.getValue().toString();
        String lines[] = result.split("\\r?\\n");
        
        for(String s : lines)
        {
          
            Result r=new Result(key,s);
            final_result.add(r);
        }
        it.remove(); 
    }
    showWindow();
    }
    static void showWindow()
    {
        JFrame frame = new JFrame("Links");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 400);
        String[] data =new String[final_result.size()];
        int i=0;
        for(Result r : final_result)
        {
            data[i]=r.getLink();

        }
        JList jl=new JList();
        jl.setBounds(0, 0, 150, 300);
        frame.add(jl);
        frame.pack();
       
        frame.setVisible(true);
    }

    static void showDemoWindow()
    {
        JFrame frame = new JFrame("Links");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new GridLayout(1,5));
        JList jl=new JList(s);
        
        frame.add(jl);
        
        frame.setVisible(true);
    }
}
