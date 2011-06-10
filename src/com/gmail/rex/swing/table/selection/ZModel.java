package com.gmail.rex.swing.table.selection;

/**
 * 
 * @author Rex
 * @since 10/June/2011
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JTable;

public class ZModel {
	
	private HashMap<ZCell, State> zMapping = new HashMap<ZCell, State>();
	
	private JTable table = null;
	private ZTable tableSelector = null;
	
	public final static int ROW_HEADER_ID = Integer.MAX_VALUE;
	public final static int COLUMN_HEADER_ID = Integer.MAX_VALUE;
	
	public ZModel(ZTable ts) {
		tableSelector = ts;
		table = ts.getTable();
		refresh();
	}
	
	public void refresh() {
		int rowCount = table.getRowCount();
		int colCount = table.getColumnCount();
		
		for( int r = 0; r < rowCount; r++ ) { 
			for( int c = 0; c < colCount; c++ ) {
				zMapping.put(new ZCell(r,c), State.NONE );
			}
			
		}
		
		for( int r = 0; r < rowCount; r++ ) { 
			zMapping.put(new ZCell(r, COLUMN_HEADER_ID), State.NONE );
		}
		for( int c = 0; c < colCount; c++ ) {
			zMapping.put(new ZCell(ROW_HEADER_ID, c), State.NONE );
		}
		
		repaintUI();
		
	}
	
	private void repaintUI() {
		tableSelector.revalidate();
		tableSelector.repaint();		
	}

	public State getColumnState( int column ) {
		return getState( ROW_HEADER_ID, column );
	}
	public State getRowState( int row ) {
		return getState( row, COLUMN_HEADER_ID );		
	}
	
	public State getState( int row, int column ) {
		return zMapping.get(new ZCell(row, column));
	}
	
	public void columnSelect( int column, boolean select ) {
		if( isLegalColumnParameterValue( column ) ) {
			State state =  select ? State.ALL : State.NONE;
			
			Iterator<Entry<ZCell, State>> iter = zMapping.entrySet().iterator();
			while( iter.hasNext() ) {
				Entry<ZCell, State> entry = iter.next();
				if( entry.getKey().column == column ) {
					entry.setValue( state );
				}
			}
			
			//zMapping.put(new ZCell(ROW_HEADER_ID, column), state );
			updateHeaderCell();
			repaintUI();
		}		
	}
	
	public void rowSelect( int row, boolean select ) {
		if( isLegalRowParameterValue(row) ) {
			State state =  select ? State.ALL : State.NONE;
			Iterator<Entry<ZCell, State>> iter = zMapping.entrySet().iterator();
			while( iter.hasNext() ) {
				Entry<ZCell, State> entry = iter.next();
				if( entry.getKey().row == row ) {
					entry.setValue( state );
				}
			}			
			//zMapping.put(new ZCell(row, COLUMN_HEADER_ID), state );
			updateHeaderCell();
			repaintUI();
		}
		
	}
	
	/**
	 * update the row/column cell state.
	 * iterate all cells, to update row-header,column-header state
	 * @param row
	 * @param column
	 * @param select
	 */
	public void cellSelect( int row, int column, boolean select ) {
		if( isLegalRowParameterValue( row ) && isLegalColumnParameterValue(column) ) {

			
			State state =  select ? State.ALL : State.NONE;
			
			Iterator<Entry<ZCell, State>> iter = zMapping.entrySet().iterator();
			while( iter.hasNext() ) {
				Entry<ZCell, State> entry = iter.next();
				if( entry.getKey().row == row && entry.getKey().column == column ) {
					entry.setValue( state );					
				}				
			}	
			
			updateHeaderCell();
			
			repaintUI();
		}
		
	}
	
	private void updateHeaderCell() {
		int rowCount = table.getRowCount();
		int columnCount = table.getColumnCount();
		
		HashMap<ZCell, Integer> rowCache = new HashMap<ZCell, Integer>();
		HashMap<ZCell, Integer> colCache = new HashMap<ZCell, Integer>();
		
		Iterator<Entry<ZCell, State>> iter = zMapping.entrySet().iterator();
		while( iter.hasNext() ) {
			Entry<ZCell, State> entry = iter.next();
			ZCell cell = entry.getKey();
			State state = entry.getValue();
			
			if( cell.row == ROW_HEADER_ID || cell.column == COLUMN_HEADER_ID ) {
				continue;
			}
			
			if( State.ALL.equals( state ) ) {
				ZCell rowHead = new ZCell(cell.row, COLUMN_HEADER_ID );
				Integer cached = rowCache.get(rowHead);
				
				if( cached == null ) {
					cached = new Integer(1);
				} else {
					cached = Integer.valueOf( cached.intValue() + 1 );
				}
				
				rowCache.put(rowHead, cached);
				//////////////////////////////////////////////////////
				ZCell colHead = new ZCell( ROW_HEADER_ID, cell.column );
				cached = colCache.get(colHead);
				
				if( cached == null ) {
					cached = new Integer(1);
				} else {
					cached = Integer.valueOf( cached.intValue() + 1 );
				}
				
				colCache.put(colHead, cached);
			}
			
		}	
		
		for( int r = 0; r < rowCount; r++ ) {
			ZCell rowHead = new ZCell(r, COLUMN_HEADER_ID );
			int columnCountOfSelected = 0;
			Integer cached = rowCache.get(rowHead);
			if( cached != null ) {
				columnCountOfSelected = cached.intValue();
			}
			
			if( columnCountOfSelected == columnCount ) {
				zMapping.put(rowHead, State.ALL );
			} else if (columnCountOfSelected == 0 ) {
				zMapping.put(rowHead, State.NONE );
			} else {
				zMapping.put(rowHead, State.PART );
			}
			
			
		}
		
		for( int c = 0; c < columnCount; c++ ) {
			ZCell colHead = new ZCell(ROW_HEADER_ID, c );
			int rowCountOfSelected = 0;
			Integer cached = colCache.get(colHead);
			if( cached != null ) {
				rowCountOfSelected = cached.intValue();
			}
			
			if( rowCountOfSelected == rowCount ) {
				zMapping.put(colHead, State.ALL );
			} else if (rowCountOfSelected == 0 ) {
				zMapping.put(colHead, State.NONE );
			} else {
				zMapping.put(colHead, State.PART );
			}
			
			
		}
		
	}
	
	private boolean isLegalRowParameterValue( int row ) {
		if( row < 0 ) {
			return false;
		}
		if( table == null ) {
			return false;
		}
		if( row > table.getRowCount() && row != ROW_HEADER_ID ) {
			return false;
		}
		return true;
	}
	
	private boolean isLegalColumnParameterValue( int col ) {
		if( col < 0 ) {
			return false;
		}
		if( table == null ) {
			return false;
		}
		if( col > table.getColumnCount() && col != ROW_HEADER_ID ) {
			return false;
		}
		return true;
	}
}
