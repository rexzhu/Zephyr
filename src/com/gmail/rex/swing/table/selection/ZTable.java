package com.gmail.rex.swing.table.selection;

/**
 * 
 * @author Rex
 * @since 10/June/2011
 */
import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class ZTable extends JPanel implements TableModelListener {

	private JTable table;
	private ZModel model;

	public ZTable(JTable aTable) {
		super();
		if (aTable != null) {
			this.table = aTable;
			this.model = new ZModel(this);
			table.getModel().addTableModelListener(this);

			decorateTable();
		}
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		TableColumnModel cm = table.getColumnModel();
		int n = table.getColumnCount();
		ZColumn[] newCols = new ZColumn[n];
		TableColumn[] oldCols = new TableColumn[n];
		for (int i = 0; i < n; i++) {
			oldCols[i] = cm.getColumn(i);
			newCols[i] = new ZColumn(this);
			newCols[i].copyValues(oldCols[i]);
		}
		for (int i = 0; i < n; i++) {
			cm.removeColumn(oldCols[i]);
		}
		for (int i = 0; i < n; i++) {
			cm.addColumn(newCols[i]);
		}

		model.refresh();
	}

	public JTable getTable() {
		return table;
	}

	public ZModel getModel() {
		return model;
	}

	private void decorateTable() {
		JScrollPane scroll = new JScrollPane(table);
		table.setTableHeader(new ZColumnHeader(this));
		ZCellControl control = new ZCellControl( this );
		table.setDefaultRenderer(Object.class, control );
		table.setDefaultEditor(Object.class, control);
		
		scroll.setRowHeaderView(new ZRowHeader(this));

		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}

}
