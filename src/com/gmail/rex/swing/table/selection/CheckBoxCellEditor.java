package com.gmail.rex.swing.table.selection;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class CheckBoxCellEditor implements TableCellEditor, TableCellRenderer {

	public CheckBoxCellEditor() {
	}
	
	private JCheckBox box = new JCheckBox();
	
	@Override
	public Object getCellEditorValue() {
		return Boolean.valueOf(box.isSelected());
	}

	@Override
	public boolean isCellEditable(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean shouldSelectCell(EventObject anEvent) {
		return true;
	}

	@Override
	public boolean stopCellEditing() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void cancelCellEditing() {
		
		
	}

	@Override
	public void addCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCellEditorListener(CellEditorListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		box.setText(value.toString());
		return box;
	}

	
	//======================  TableCellRender ===================================//
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		box.setText(value.toString());
		return box;
	}

}
