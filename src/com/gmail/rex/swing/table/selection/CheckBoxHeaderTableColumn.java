package com.gmail.rex.swing.table.selection;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class CheckBoxHeaderTableColumn extends TableColumn {

	protected TableCellEditor headerEditor;

	public CheckBoxHeaderTableColumn() {
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
		//cellRenderer = new CheckBoxTableCellRender();
		//cellEditor = base.getCellEditor();
		
	}

	protected TableCellEditor createDefaultHeaderEditor() {

		return new DefaultCellEditor(new JCheckBox() );
	}

}
