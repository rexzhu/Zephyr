package com.gmail.rex.swing.table.selection;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class CheckBoxHeaderUI extends BasicTableHeaderUI {
	protected MouseInputListener createMouseInputListener() {
		return new MouseInputHandler((CheckBoxHeader) header);
	}

	public class MouseInputHandler extends BasicTableHeaderUI.MouseInputHandler {
		private Component dispatchComponent;

		protected CheckBoxHeader header;

		public MouseInputHandler(CheckBoxHeader header) {
			this.header = header;
		}

		private void setDispatchComponent(MouseEvent e) {
			Component editorComponent = header.getEditorComponent();
			Point p = e.getPoint();
			Point p2 = SwingUtilities.convertPoint(header, p, editorComponent);
			dispatchComponent = SwingUtilities.getDeepestComponentAt(
					editorComponent, p2.x, p2.y);
		}

		private boolean repostEvent(MouseEvent e) {
			if (dispatchComponent == null) {
				return false;
			}
			MouseEvent e2 = SwingUtilities.convertMouseEvent(header, e,
					dispatchComponent);
			dispatchComponent.dispatchEvent(e2);
			return true;
		}

		public void mousePressed(MouseEvent e) {
			if (!SwingUtilities.isLeftMouseButton(e)) {
				return;
			}
			super.mousePressed(e);

			if (header.getResizingColumn() == null) {
				Point p = e.getPoint();
				TableColumnModel columnModel = header.getColumnModel();
				int index = columnModel.getColumnIndexAtX(p.x);
				if (index != -1) {
					if (header.editCellAt(index, e)) {
						setDispatchComponent(e);
						repostEvent(e);

					}
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			super.mouseReleased(e);
			if (!SwingUtilities.isLeftMouseButton(e)) {
				return;
			}
			
			repostEvent(e);

			dispatchComponent = null;
			
			header.getTable().repaint();
		}

	}


	public void paint(Graphics g, JComponent c) { 
		super.paint(g, c);
		TableColumnModel cm = header.getColumnModel(); 
		Rectangle cellRect = header.getHeaderRect(0); 
		for(int column = 0; column < cm.getColumnCount() ; column++) { 
			TableColumn aColumn = cm.getColumn(column); 
			int columnWidth = aColumn.getWidth();
			cellRect.width = columnWidth;
			
			Component component = getHeaderRenderer(column); 
	        rendererPane.paintComponent(g, component, header, cellRect.x, cellRect.y,
	                            cellRect.width, cellRect.height, true);
			cellRect.x += columnWidth;
		    }
	}
	
	private Component getHeaderRenderer(int columnIndex) { 
        TableColumn aColumn = header.getColumnModel().getColumn(columnIndex); 
		TableCellRenderer renderer = aColumn.getHeaderRenderer(); 
	    if (renderer == null) { 
		    renderer = header.getDefaultRenderer(); 
		}

        return renderer.getTableCellRendererComponent(header.getTable(), 
						aColumn.getHeaderValue(),
                                                false, false, 
                                                -1, columnIndex);
    }
}
