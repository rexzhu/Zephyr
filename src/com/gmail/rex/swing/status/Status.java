package com.gmail.rex.swing.status;

/**
 * @author Rex
 * @since 29/June/2011
 */
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;


public abstract class Status<T> {
	
	private T statusObject;
	
	public T getStatus() {
		return statusObject;				
	}
	
	public void setStatus(T value) {
		statusObject = value;
		for( StatusChangeListener<T> listener: listeners ) {
			listener.statusChanged( statusObject );
		}
	}
	
	public abstract JComponent toView() ;
	
	private List<StatusChangeListener<T>> listeners = new ArrayList<StatusChangeListener<T>>();

	public void addListener(StatusChangeListener<T> lsn) {
		listeners.add( lsn );		
	}
	public void removeListener(StatusChangeListener<T> lsn) {
		listeners.remove( lsn );		
	}
}
