package com.gmail.rex.swing.status;

/**
 * @author Rex
 * @since 29/June/2011
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class Main extends JFrame {
	public Main() {
		super("Main");
		
		final DefaultTableModel tm = new DefaultTableModel(0,3);
		/////////////////////////////////////////////////////////////////////////////////
		final Status<DefaultTableModel> status = new Status<DefaultTableModel>() {

			@Override
			public JComponent toView() {
				JTable table = new JTable( getStatus() );
				return new JScrollPane( table );
			}
			
		};
		final TableStatusIndicator indicator = new TableStatusIndicator( status );
		///////////////////////////////////////////////////////////////////////////////
		
		getContentPane().setLayout(new BorderLayout());
		JButton btn = new JButton("Test");
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				tm.addRow(new Object[] {1, System.currentTimeMillis(),"ok"});
				status.setStatus(tm);
				
			}
		});
		
		getContentPane().add(btn, BorderLayout.CENTER);
		JPanel p = new JPanel( new FlowLayout(FlowLayout.RIGHT) );
		/////////////////////////////
		p.add( indicator );
		////////////////////////////
		getContentPane().add(p, BorderLayout.SOUTH);
		
		
		
		//Background thread to generate the mock data
		
		new Thread() {
			public void run() {
				int i = 0;
				while( true ) {
					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					tm.addRow(new Object[] {i++, System.currentTimeMillis(),"ok"});
					status.setStatus(tm);
					//sb.getModel().addRow(new Object[]{i++, new Date(System.currentTimeMillis()), "ok"});
				}
			}
		}.start();
	}
	
	public static void main(String[] args) {
		Main frame = new Main();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(1);
			}
		});
		
		
		frame.setSize(300, 400);
		frame.setVisible(true);
	}
}
