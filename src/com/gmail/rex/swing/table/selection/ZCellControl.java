package com.gmail.rex.swing.table.selection;

/**
 *
 * @author Rex
 * @since 10/June/2011
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class ZCellControl extends JPanel implements TableCellRenderer, TableCellEditor {

    private JComponent origin;
    private JCheckBox box;
    private ZTable tableSelector;
    
    public ZCellControl(ZTable ts) {
        super();
        tableSelector = ts;
        init();

    }

    private void init() {
        setLayout(new BorderLayout());
        box = new JCheckBox();
        box.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                onSelect();
                tableSelector.getTable().removeEditor();
            }
        });
        origin = new JPanel();
        add(box, BorderLayout.WEST);
        add(origin, BorderLayout.CENTER);
    }

    protected void onSelect() {
        int row = tableSelector.getTable().getEditingRow();
        int column = tableSelector.getTable().getEditingColumn();
        boolean select = box.isSelected();
        tableSelector.getModel().cellSelect(row, column, select);
    }

    @Override
    public Object getCellEditorValue() {
        // TODO : to be refactoried
    	JTable table = tableSelector.getTable();
    	if( table != null ) {
    		int row = tableSelector.getTable().getEditingRow();
            int column = tableSelector.getTable().getEditingColumn();
            ISelectionPolicy policy = tableSelector.getSelectionPolicy();
            if (policy != null && !policy.isSelectEnable(tableSelector, row, column)) {
            	JTextField tf = (JTextField) table.getEditorComponent();
            	return tf.getText();
            }
    	}
        return null;
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        // TODO to be refactoried
    	JTable table = tableSelector.getTable();
    	if( table != null ) {
    		int row = tableSelector.getTable().getEditingRow();
            int column = tableSelector.getTable().getEditingColumn();
            ISelectionPolicy policy = tableSelector.getSelectionPolicy();
            if (policy != null && !policy.isSelectEnable(tableSelector, row, column)) {
            	table.editingStopped( new ChangeEvent(table));	
        		table.removeEditor();	
            }
    	}
    	
        return true;
    }

    @Override
    public void cancelCellEditing() {
        // TODO Auto-generated method stub
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
    	Component c = getTableCellRendererComponent(table, value, isSelected, true, row, column);
    	//to be refactoried
        if( c instanceof JLabel ) {
        	return new JTextField( value.toString() );
        }
        return c;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        if (row == -1) {
            row = ZModel.COLUMN_HEADER_ID;
        }

        ISelectionPolicy policy = tableSelector.getSelectionPolicy();
        if (policy != null && policy.isSelectEnable(tableSelector, row, column)) {
            State state = tableSelector.getModel().getState(row, column);

            box.setText(value.toString());
            box.setFont(table.getFont());

            if (State.PART.equals(state)) {
                box.setBackground(Color.LIGHT_GRAY);
            } else {
                box.setBackground(UIManager.getColor("TableHeader.background"));
            }
            box.setForeground(UIManager.getColor("TableHeader.foreground"));

            box.setOpaque(true);
            box.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            box.setBorderPainted(true);

            box.setSelected(State.NONE.equals(state) ? false : true);

            return box;
        }
        return new JLabel(value.toString());

    }

}
