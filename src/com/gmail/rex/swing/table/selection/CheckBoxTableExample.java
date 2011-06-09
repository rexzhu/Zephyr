package com.gmail.rex.swing.table.selection;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

public class CheckBoxTableExample extends JFrame {
	Integer[][] rowData = new Integer[][] { 
			new Integer[] {1,2,3,4,5}, 
			new Integer[] {6,7,8,9,0}, 
			new Integer[] {11,12,13,14,15}, 
			new Integer[] {2,3,8,1,4}, 
			new Integer[] {2,3,4,5,1}
			};
	Object[] col = new Object[] {"A","B","C","D","E"};
	
	
	public CheckBoxTableExample() {
		super("CheckBoxTableExample");

		JTable table = new JTable(rowData, col);
		JScrollPane pane = new JScrollPane(table);
		
		//The entry of inject
		table.setTableHeader(new CheckBoxHeader(table));
		//rowHeader
		pane.setRowHeaderView(new CheckBoxRowHeader(table, new Object[] {"1","2","3","4","5"} ));
		
		
		getContentPane().add(pane);
	}

	public static void main(String[] args) {
		CheckBoxTableExample frame = new CheckBoxTableExample();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setSize(300, 400);
		frame.setVisible(true);
	}
}
