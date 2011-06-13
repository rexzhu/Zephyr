package com.gmail.rex.swing.table.selection;

/**
 * 
 * @author Rex
 * @since 10/June/2011
 */
import java.awt.BorderLayout;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

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

	public ZTable(final JTable aTable) {
		super();
		
		if (aTable != null) {

			
			this.table = aTable;			
			this.model = new ZModel(this);
			this.table.getModel().addTableModelListener(this);


			decorateTable();
		}
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		updateModel();				
	}
	
	private void updateModel() {
		updateColumnModel();
		
		if( rhProvide != null ) {
			rowHeader.setRowHeaders( rhProvide.getRowHeader() );
		}
		
		model.refresh();
	}
	
	private void updateColumnModel() {
		if( table == null ) {
			return;
		}
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
	}

	public JTable getTable() {
		return table;
	}

	public ZModel getModel() {
		return model;
	}

	private void decorateTable() {
		rowHeader = new ZRowHeader(this);
		if( rhProvide == null ) {
			setRowHeaderProvider( defaultRowHeaderProvider() );
		}
		
		JScrollPane scroll = new JScrollPane(table);	
		
		final TableColumnModel tcm = table.getColumnModel();
		Object proxy = Proxy.newProxyInstance(tcm.getClass().getClassLoader(), new Class[] {TableColumnModel.class}, new InvocationHandler() {
			
			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				if( "addColumn".equalsIgnoreCase(method.getName() ) ) {
					TableColumn tc = (TableColumn) args[0];
					ZColumn z = new ZColumn(ZTable.this);
					z.copyValues(tc);
					Object obj = method.invoke(tcm, z);
					if( rhProvide != null ) {
						rowHeader.setRowHeaders( rhProvide.getRowHeader() );
					}
					
					model.refresh();
					
					return obj;
				}
				return method.invoke(tcm, args);
			}
		});
		table.setColumnModel( (TableColumnModel) proxy );
		table.setTableHeader(new ZColumnHeader(this));
		ZCellControl control = new ZCellControl( this );
		table.setDefaultRenderer(Object.class, control );
		table.setDefaultEditor(Object.class, control);
		
		
		
		scroll.setRowHeaderView( rowHeader );

		setLayout(new BorderLayout());
		add(scroll, BorderLayout.CENTER);
	}
	
	private IRowHeaderProvider defaultRowHeaderProvider() {
		return new IRowHeaderProvider() {
			
			@Override
			public Object[] getRowHeader() {
				
				int rowCount = table.getRowCount();
				Object[] rh = new Object[rowCount];
				for( int x = 0; x < rowCount; x++ ) {
					rh[x] = new Integer(x);
				}
				return rh;
			}
		};
		
	}

	private ZRowHeader rowHeader = null;
	private IRowHeaderProvider rhProvide = null;
	
	public void setRowHeaderProvider(IRowHeaderProvider provider) {
		rhProvide = provider;
	}
	
	public List<ZCell> getSelectedCells() {
		return model.getCells(State.ALL);
	}

	public void addSelectionListner( IModelListener lsn ) {
		getModel().addModelListener(lsn);
	}


}
