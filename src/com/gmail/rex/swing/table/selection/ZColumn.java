package com.gmail.rex.swing.table.selection;

/**
 * 
 * @author Rex
 * @since 10/June/2011
 */

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class ZColumn extends TableColumn {
	protected TableCellEditor headerEditor;
	private ZTable tableSelector;
	
	public ZColumn(ZTable ts) {
		tableSelector = ts;
		setHeaderEditor(createDefaultHeaderEditor());
	}


	public void setHeaderEditor(TableCellEditor headerEditor) {
		this.headerEditor = headerEditor;
	}

	public TableCellEditor getHeaderEditor() {
		return headerEditor;
	}

	public boolean isHeaderEditable() {
		return true;
	}

	public void copyValues(TableColumn base) {
		modelIndex = base.getModelIndex();
		identifier = base.getIdentifier();
		width = base.getWidth();
		minWidth = base.getMinWidth();
		setPreferredWidth(base.getPreferredWidth());
		maxWidth = base.getMaxWidth();
		headerRenderer = base.getHeaderRenderer();
		headerValue = base.getHeaderValue();
		
		isResizable = base.getResizable();
		
		//TODO:replace the render/editor
		//cellRenderer = base.getCellRenderer();
		cellRenderer = new ZCellControl(tableSelector);
		//cellEditor = base.getCellEditor();
		
	}

	protected TableCellEditor createDefaultHeaderEditor() {
		return new DefaultCellEditor(new JCheckBox() );
	}
}
