package com.gmail.rex.swing.table.selection;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.table.JTableHeader;

@SuppressWarnings("serial")
public class CheckBoxRowHeader extends JList  {
	private Object[] header = null;
	private JTable table = null;
	
	private HashMap<String, Boolean> selecting = new HashMap<String, Boolean>();
	
	public CheckBoxRowHeader( JTable aTable, Object[] headerList ) {
		super();
		table = aTable;
		header = headerList;
		createUI();
			
	}
	
	private void updateTableSelection() {
		table.clearSelection();
		Iterator<String> rows = selecting.keySet().iterator();
		while( rows.hasNext() ) {
			String row = rows.next();
			int i = Integer.parseInt( row );
			boolean selected = selecting.get(row);
			if( selected ) {
				table.getSelectionModel().setSelectionInterval(i, i);	
			} else {
				table.getSelectionModel().removeIndexInterval(i,i);
			}
			
			
		}
	}
 	
	private void createUI() {
		ListModel rowModel = new AbstractListModel() {
			private static final long serialVersionUID = 1L;
			public int getSize() { 
		    	  if( table == null ) {
		    		  return 0;
		    	  }
		    	  return table.getRowCount() ; 
		      }
		    public Object getElementAt(int index) {
				if (table == null) {
					return "";
				}
				return header[index];
			}
	    };
	    setModel(rowModel);    
	    setFixedCellWidth(50);
	    setFixedCellHeight( table.getRowHeight() + table.getRowMargin() );
	    setCellRenderer(new RowHeaderRenderer(table));	    
	    setBackground( table.getTableHeader().getBackground() );

	    addMouseListener(new MouseAdapter() {
	    	public void mouseClicked(MouseEvent e) {
	    		int i = getSelectedIndex();
	    		Object o = selecting.get(String.valueOf(i) );
	    		if( o == null ) {
	    			selecting.put( String.valueOf(i), Boolean.valueOf(true));
	    		} else {
	    			boolean old = Boolean.valueOf(o.toString());
	    			selecting.put( String.valueOf(i), !old);
	    			
	    		}
	    		
	    		updateTableSelection();
	    		
	    		repaint();
	    		table.repaint();
	    	}
	    });
	}

	class RowHeaderRenderer extends JCheckBox implements ListCellRenderer {

		private JTable table;
		public RowHeaderRenderer(JTable table) {
			this.table = table;
			JTableHeader header = table.getTableHeader();
			setOpaque(true);
			//setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			setHorizontalAlignment(CENTER);
			setForeground(header.getForeground());
			setBackground(header.getBackground());
			setFont(header.getFont());
			setBorderPainted(true);
			setBorder( BorderFactory.createEtchedBorder() );
		}
		
		
		
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			setText((value == null) ? "" : value.toString());
			boolean select = false;
			String key = String.valueOf(index);
			if( selecting.get(key) != null ) {
				select = "true".equalsIgnoreCase(selecting.get(key).toString() );
			}
			setSelected( select );

			return this;
		}

	}
}
