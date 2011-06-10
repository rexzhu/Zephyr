package com.gmail.rex.swing.table.selection;

/**
 * 
 * @author Rex
 * @since 10/June/2011
 */
public enum State {
	NONE(0) {
		public String toString() {
			return "None";
		}
		
	}
	
	, 
	
	PART(1) {
		public String toString() {
			return "Part";
		}
	}
	
	,ALL(2) {
		public String toString() {
			return "All";
		}
	};
	
	private int value = -1; 
	
	State(int v) {
		value = v;
	}
	
	public int getState() {
		return value;
	}
	
	public String toString() {
		throw new UnsupportedOperationException("UnsupportMethod");
	}

}
