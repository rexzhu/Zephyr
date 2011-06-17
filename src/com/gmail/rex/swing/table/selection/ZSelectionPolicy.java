package com.gmail.rex.swing.table.selection;

public class ZSelectionPolicy implements ISelectionPolicy {
	
	public ZSelectionPolicy() {}
	
	@Override
	public boolean isSelectEnable( ZTable table, int row, int column ) {
		return true;
	}

}
