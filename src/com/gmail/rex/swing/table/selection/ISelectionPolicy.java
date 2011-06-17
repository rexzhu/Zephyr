package com.gmail.rex.swing.table.selection;

public interface ISelectionPolicy {
	public boolean isSelectEnable( ZTable table, int row, int column );
}
