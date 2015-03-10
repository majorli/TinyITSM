package com.jeans.tinyitsm;

import com.jeans.tinyitsm.event.HREvent;
import com.jeans.tinyitsm.event.HREventListener;

public class HREventObserver {
	
	private static HREventListener listener;

	public static void set(HREventListener l) {
		listener = l;
	}
	
	public static void remove() {
		listener = null;
	}
	
	public static void fire(HREvent e) {
		if (null == listener)
			return;
		listener.changed(e);
	}

}
