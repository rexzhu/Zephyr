package com.gmail.rex.swing.table.selection;

/**
 * 
 * @author Rex
 * @since 10/June/2011
 */

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.MouseInputListener;
import javax.swing.event.TableModelEvent;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class ZColumnHeader extends JTableHeader implements CellEditorListener {

	protected Component editorComp;
	protected TableCellEditor cellEditor;
	protected int editingColumn;

	private ZTable tableSelector;

	public ZColumnHeader(ZTable ts) {
		super();
		tableSelector = ts;
		if (tableSelector != null) {
			setTable(tableSelector.getTable());
			setColumnModel(tableSelector.getTable().getColumnModel());
			setReorderingAllowed(false);
			if( table != null ) {

				setDefaultRenderer(new ZCellControl( tableSelector ));
				
				tableSelector.tableChanged( new TableModelEvent( table.getModel() ) );	
			}
			
		}
	}


	public void updateUI() {
		setUI(new ZHeaderUI());
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
		ZColumn col = (ZColumn) columnModel.getColumn(columnIndex);
		return col.isHeaderEditable();
	}

	public TableCellEditor getCellEditor(int index) {
		int columnIndex = columnModel.getColumn(index).getModelIndex();
		ZColumn col = (ZColumn) columnModel.getColumn(columnIndex);
		return col.getHeaderEditor();
	}

	public void setCellEditor(TableCellEditor newEditor) {
		TableCellEditor oldEditor = cellEditor;
		cellEditor = newEditor;

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
		JCheckBox comp = (JCheckBox) editor.getTableCellEditorComponent(
				getTable(), value, true, ZModel.ROW_HEADER_ID, index);

		comp.setNextFocusableComponent(this);
		comp.setText(value.toString());
		comp.setBackground(UIManager.getColor("TableHeader.background"));
		comp.setForeground(UIManager.getColor("TableHeader.foreground"));

		State state = tableSelector.getModel().getState(ZModel.ROW_HEADER_ID,
				index);
		comp.setSelected(state.equals(State.ALL));
		return comp;
	}

/*	protected TableCellRenderer createDefaultRenderer() {
		return new ZCellControl( tableSelector );
	}*/

	public TableCellEditor getCellEditor() {
		return cellEditor;
	}

	@Override
	public void editingStopped(ChangeEvent e) {
		TableCellEditor editor = getCellEditor();
		if (editor != null) {
			String text = ((JCheckBox) editorComp).getText();
			boolean b = ((JCheckBox) editorComp).isSelected();
			tableSelector.getModel().columnSelect(editingColumn, b);
			columnModel.getColumn(editingColumn).setHeaderValue(text);
			removeEditor();
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

	// private UI
	private class ZHeaderUI extends BasicTableHeaderUI {
		protected MouseInputListener createMouseInputListener() {
			return new MouseInputHandler((ZColumnHeader) header);
		}

		public class MouseInputHandler extends
				BasicTableHeaderUI.MouseInputHandler {
			private Component dispatchComponent;

			protected ZColumnHeader header;

			public MouseInputHandler(ZColumnHeader header) {
				this.header = header;
			}

			private void setDispatchComponent(MouseEvent e) {
				Component editorComponent = header.getEditorComponent();
				Point p = e.getPoint();
				Point p2 = SwingUtilities.convertPoint(header, p,
						editorComponent);
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

				tableSelector.repaint();
				// header.getTable().repaint();
			}

		}

		public void paint(Graphics g, JComponent c) {
			super.paint(g, c);
			TableColumnModel cm = header.getColumnModel();
			Rectangle cellRect = header.getHeaderRect(0);
			for (int column = 0; column < cm.getColumnCount(); column++) {
				TableColumn aColumn = cm.getColumn(column);
				int columnWidth = aColumn.getWidth();
				cellRect.width = columnWidth;

				Component component = getHeaderRenderer(column);
				rendererPane.paintComponent(g, component, header, cellRect.x,
						cellRect.y, cellRect.width, cellRect.height, true);
				cellRect.x += columnWidth;
			}
		}

		protected Component getHeaderRenderer(int columnIndex) {
			TableColumn aColumn = header.getColumnModel()
					.getColumn(columnIndex);
			TableCellRenderer renderer = aColumn.getHeaderRenderer();
			if (renderer == null) {
				renderer = header.getDefaultRenderer();
			}

			return renderer.getTableCellRendererComponent(header.getTable(),
					aColumn.getHeaderValue(), false, false,
					ZModel.ROW_HEADER_ID, columnIndex);
		}
	}
}
