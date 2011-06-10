package com.gmail.rex.swing.table.selection;

/**
 * 
 * @author Rex
 * @since 10/June/2011
 */
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.WindowConstants;


@SuppressWarnings("serial")
public class Zephyr extends JFrame {
	Integer[][] rowData = new Integer[][] { 
			new Integer[] {1,2,3,4,5}, 
			new Integer[] {6,7,8,9,0}, 
			new Integer[] {11,12,13,14,15}, 
			new Integer[] {2,3,8,1,4}, 
			new Integer[] {2,3,4,5,1}
			};
	Object[] col = new Object[] {"A","B","C","D","E"};
	
	
	public Zephyr() {
		super("Zephyr");

		JTable table = new JTable(rowData, col);
		
		ZTable selector = new ZTable(table);
		
		getContentPane().add(selector);
	}

	public static void main(String[] args) {
		Zephyr frame = new Zephyr();
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
