package com.gmail.rex.swing.table.selection;

/**
 * 
 * @author Rex
 * @since 10/June/2011
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

@SuppressWarnings("serial")
public class ZRowHeader extends JList {
	
	private ZTable tableSelector;
	private Object[] rowHeaders;
	
	public ZRowHeader( ZTable ts, Object[] headers ) {
		tableSelector = ts;
		rowHeaders = headers;
		
		if( tableSelector != null ) {
			createUI();
		}
	}
	
	public ZRowHeader( ZTable ts ) {
		this( ts, null );		
		setRowHeaders(createDefaultRowHeaders() );
	}
	
	public void setRowHeaders( Object[] headers ) {
		if( headers != null ) {
			rowHeaders = headers;
		}
	}
	
	protected Object[] createDefaultRowHeaders() {
		int rowCount = tableSelector.getTable().getRowCount();
		Object[] h = new Object[rowCount];
		for( int i = 0; i < rowCount; i++ ) {
			h[i] = new Integer(i);
		}
		return h;
	}
	
	private void createUI() {
		final JTable table = tableSelector.getTable();		
		
	    setModel(new AbstractListModel() {

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
				
				if( index < 0 || index > rowHeaders.length ) {
					return "";
				}
				return rowHeaders[index];
			}
	    });    
	    setFixedCellWidth(50);
	    //setFixedCellHeight( table.getRowHeight() + table.getRowMargin() );
	    setFixedCellHeight( table.getRowHeight());
	    setCellRenderer( createRowHeaderRender() );
	    setBackground( table.getTableHeader().getBackground() );

	    addMouseListener(new MouseAdapter() {
	    	public void mouseClicked(MouseEvent e) {	    		
	    		rowClicked();
	    	}
	    });
	}

	protected ListCellRenderer createRowHeaderRender() {
		return new RowHeaderRenderer( tableSelector.getTable() );
	}
	
	/**
	 * ALL->NONE
	 * NONE->ALL
	 * PART->ALL
	 * @param row
	 * @return
	 */
	private State newState(int row) {
		State oldState = tableSelector.getModel().getState(row, ZModel.COLUMN_HEADER_ID);
		State newState = State.ALL;
		if( State.ALL.equals(oldState) ) {
			newState = State.NONE;
		}
		return newState;
	}

	protected void rowClicked() {		
		int row = getSelectedIndex();
		boolean select =  State.ALL.equals(newState(row)) ? true : false;		
		tableSelector.getModel().rowSelect(row, select );			
	}

	private class RowHeaderRenderer extends JCheckBox implements ListCellRenderer {

		private JTable table;
		
		public RowHeaderRenderer(JTable aTable) {
			this.table = aTable;
			
		}
		
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			State state = tableSelector.getModel().getRowState(index);			

			JTableHeader header = table.getTableHeader();
			setForeground(header.getForeground());
			if( State.PART.equals(state) ) {
				setBackground(Color.LIGHT_GRAY );
			} else {
				setBackground(header.getBackground());
			}
			setFont(header.getFont());			
			setOpaque(true);
			setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			setHorizontalAlignment(CENTER);			
			setBorderPainted(true);
			
			String title = (value == null) ? "" : value.toString(); 
			setText( title );
			setToolTipText(title);
			
			setSelected( !state.equals(State.NONE) );
			
			
			return this;
		}

	}
	//end class RowHeaderRenderer
}
