package com.gmail.rex.swing.status;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 * It's a sample of table UI/data implementing.
 * @author Rex
 *
 */
@SuppressWarnings("serial")
public class TableStatusIndicator extends JButton implements StatusChangeListener<DefaultTableModel> {

	public TableStatusIndicator(Status<DefaultTableModel> aStatus) {
		status = aStatus;
		
		status.addListener(this);
		
		monitor = new StatusMonitor();
		
		addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				monitorShow();
				
			}
		});
		setPreferredSize(new Dimension(10,10));
		setVisible(false);
	}

	@Override
	public void statusChanged(DefaultTableModel value) {
		setVisible(true);
		value.fireTableDataChanged();
	}

	protected void monitorHide() {
		monitor.setVisible(false);
	}
	
	protected void monitorShow() {
		monitor.setVisible(true);
	}
	
	private StatusMonitor monitor = null;
	private Status<DefaultTableModel> status = null;

	class StatusMonitor extends JDialog {
		StatusMonitor() {
			setTitle("Monitor");
			setModalityType(ModalityType.DOCUMENT_MODAL);
			
			getContentPane().setLayout(new BorderLayout() );
			getContentPane().add( status.toView(), BorderLayout.CENTER );
			JPanel opPanel = new JPanel( new FlowLayout(FlowLayout.RIGHT));
			JButton hide = new JButton("Hide");
			hide.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					monitorHide();
				}
			});
			opPanel.add(hide);
			getContentPane().add( opPanel, BorderLayout.SOUTH );
			
			addWindowListener(new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					monitorHide();
				}
				
			});
			
			setBounds(100, 100, 450, 300);
		}
		
		@Override
		public void setVisible( boolean visible ) {
			
			if( visible ) {
				
				getContentPane().add( status.toView(), BorderLayout.CENTER );
				
			}
			super.setVisible(visible);
		}

		
	}
}
