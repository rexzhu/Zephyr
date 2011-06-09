package com.gmail.rex.swing.table.selection;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;


@SuppressWarnings("serial")
public class CheckBoxTableCellRender extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		if( table.getTableHeader() instanceof CheckBoxHeader ) {

			CheckBoxHeader header = (CheckBoxHeader)table.getTableHeader();
			String selection = "false";
			if( row == -1 ) {
				selection = header.getSelectedOption( CheckBoxHeader.HEADER_ROW, column );
			} else {
				selection = header.getSelectedOption( row, column );
			}
			
			JCheckBox box = new JCheckBox( value.toString() );
			box.setFont(table.getFont());		

			box.setBackground(UIManager.getColor("TableHeader.background"));
			box.setForeground(UIManager.getColor("TableHeader.foreground"));
			box.setSelected( "true".equalsIgnoreCase(selection) );
			box.setOpaque(true);
			box.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
			box.setBorderPainted(true);
			
			Rectangle old = table.getCellRect(row, column, true);
			setBounds(old.x, old.y, old.width, old.height + 10);
			
			return box;
		}
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
	}

}
