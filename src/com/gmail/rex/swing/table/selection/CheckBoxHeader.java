package com.gmail.rex.swing.table.selection;

import java.awt.Component;
import java.awt.Rectangle;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class CheckBoxHeader extends JTableHeader implements CellEditorListener, TableModelListener {
	public static final int HEADER_ROW = 100;
	public static final int MAX_COL_SIZE = 100;
	
	protected Component editorComp;
	protected TableCellEditor cellEditor;
	protected int editingColumn;

	public CheckBoxHeader(final JTable table) {
		super(table.getColumnModel());
		table.getModel().addTableModelListener(this);
		setReorderingAllowed(false);
		recreateTableColumn(columnModel);
		table.setDefaultRenderer(Object.class, new CheckBoxTableCellRender());
		
		table.setSelectionModel( new DefaultListSelectionModel() {
			public void setSelectionInterval(int index0, int index1) {
				super.setSelectionInterval(index0, index1);
				for( int i = index0; i <= index1; i++ ) {
					for( int j = 0; j < table.getColumnCount(); j++ ) {
						int key = i * MAX_COL_SIZE + j;
						options.put(key, "true");
					}
					
				}
				
			}
			
			public void removeIndexInterval(int index0, int index1) {
				super.removeIndexInterval(index0, index1);
				for( int i = index0; i <= index1; i++ ) {
					for( int j = 0; j < table.getColumnCount(); j++ ) {
						int key = i * MAX_COL_SIZE + j;
						options.put(key, "false");
					}
					
				}
			}

		});
	}

	public void updateUI() {
		setUI(new CheckBoxHeaderUI());
		resizeAndRepaint();
		invalidate();
	}

	public Component getEditorComponent() {
		return editorComp;
	}

	public boolean editCellAt(int index, EventObject e) {
		if (cellEditor != null && !cellEditor.stopCellEditing()) {
			return false;
		}
		if (!isCellEditable(index)) {
			return false;
		}
		TableCellEditor editor = getCellEditor(index);

		if (editor != null && editor.isCellEditable(e)) {
			editorComp = prepareEditor(editor, index);
			editorComp.setBounds(getHeaderRect(index));
			add(editorComp);
			editorComp.validate();
			setCellEditor(editor);
			setEditingColumn(index);
			editor.addCellEditorListener(this);

			return true;
		}
		return false;
	}

	public void setEditingColumn(int aColumn) {
		editingColumn = aColumn;
	}

	public boolean isCellEditable(int index) {
		if (getReorderingAllowed()) {
			return false;
		}
		int columnIndex = columnModel.getColumn(index).getModelIndex();
		CheckBoxHeaderTableColumn col = (CheckBoxHeaderTableColumn) columnModel
				.getColumn(columnIndex);
		return col.isHeaderEditable();
	}

	public TableCellEditor getCellEditor(int index) {
		int columnIndex = columnModel.getColumn(index).getModelIndex();
		CheckBoxHeaderTableColumn col = (CheckBoxHeaderTableColumn) columnModel
				.getColumn(columnIndex);
		return col.getHeaderEditor();
	}

	public void setCellEditor(TableCellEditor newEditor) {
		TableCellEditor oldEditor = cellEditor;
		cellEditor = newEditor;

		// firePropertyChange

		if (oldEditor != null && oldEditor instanceof TableCellEditor) {
			((TableCellEditor) oldEditor)
					.removeCellEditorListener((CellEditorListener) this);
		}
		if (newEditor != null && newEditor instanceof TableCellEditor) {
			((TableCellEditor) newEditor)
					.addCellEditorListener((CellEditorListener) this);
		}
	}

	@SuppressWarnings("deprecation")
	public Component prepareEditor(TableCellEditor editor, int index) {
		Object value = columnModel.getColumn(index).getHeaderValue();
		boolean isSelected = true;
		int row = HEADER_ROW;
		int column = index;
		JTable table = getTable();
		
		JCheckBox comp = (JCheckBox) editor.getTableCellEditorComponent(
				table, value, isSelected, row, column);
		
		comp.setNextFocusableComponent(this);
		comp.setText(value.toString());
		comp.setBackground(UIManager.getColor("TableHeader.background"));
		comp.setForeground(UIManager.getColor("TableHeader.foreground"));
		int key = HEADER_ROW * MAX_COL_SIZE + column;
		comp.setSelected( "true".equalsIgnoreCase(options.get(key) ));
		return comp;
	}

	protected TableCellRenderer createDefaultRenderer() {
		return new CheckBoxTableCellRender();
	}
	
	public TableCellEditor getCellEditor() {
	    return cellEditor;
	  }

	@Override
	public void editingStopped(ChangeEvent e) {
		TableCellEditor editor = getCellEditor();
	    if (editor != null) {
	    	String text = ((JCheckBox)editorComp).getText();
	    	boolean b = ((JCheckBox)editorComp).isSelected();
	      Object value = editor.getCellEditorValue();
	      System.out.println(editingColumn + "---" + value + "----" + b ) ;
	      
	      
	      columnSelect(editingColumn, b);
	      //String text = ((JCheckBox)editorComp).getText();
	      columnModel.getColumn(editingColumn).setHeaderValue(text);
	      removeEditor();
	    }
		
		
	}
	
	private void columnSelect(int col, boolean select ) {
		int key = HEADER_ROW * MAX_COL_SIZE + col;
		String tag = Boolean.toString(select);
		options.put(key, tag);
		for(int row  = 0; row < table.getRowCount(); row++ ) {
			key = row * MAX_COL_SIZE + col;
			options.put(key, tag);
		}
	}
	@Override
	public void editingCanceled(ChangeEvent e) {
		removeEditor();
	}
	
	public void removeEditor() {
	    TableCellEditor editor = getCellEditor();
	    if (editor != null) {
	      editor.removeCellEditorListener(this);

	      requestFocus();
	      remove(editorComp);

	      Rectangle cellRect = getHeaderRect(editingColumn);

	      setCellEditor(null);
	      setEditingColumn(-1);
	      editorComp = null;

	      repaint(cellRect);
	    }
	  }

	

	/**
	 * replace table column to CheckBoxHeaderTableColumn
	 * 
	 * @param columnModel
	 */
	protected void recreateTableColumn(TableColumnModel columnModel) {
		
		int n = columnModel.getColumnCount();
		CheckBoxHeaderTableColumn[] newCols = new CheckBoxHeaderTableColumn[n];
		TableColumn[] oldCols = new TableColumn[n];
		for (int i = 0; i < n; i++) {
			oldCols[i] = columnModel.getColumn(i);
			newCols[i] = new CheckBoxHeaderTableColumn();
			newCols[i].copyValues(oldCols[i]);
		}
		for (int i = 0; i < n; i++) {
			columnModel.removeColumn(oldCols[i]);
		}
		for (int i = 0; i < n; i++) {
			columnModel.addColumn(newCols[i]);
		}
	}
	
	
	private HashMap<Integer, String> options = new HashMap<Integer, String>();
	
	public String getSelectedOption( int row, int col ) {
		int key = row * MAX_COL_SIZE + col;
		String o = options.get(key);
		if ( o == null ) {
			o = "false";
			
			columnSelect(col, false);
		}
		return o;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		recreateTableColumn(columnModel);
		
	}

}
