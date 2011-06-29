package com.gmail.rex.swing.status;

/**
 * 
 * @author Rex
 * @since 29/June/2011
 * 
 * @param <T>
 * 
 */
public interface StatusChangeListener<T> {
	void statusChanged(T value);
}
