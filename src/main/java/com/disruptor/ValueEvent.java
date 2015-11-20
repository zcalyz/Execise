package com.disruptor;

import com.lmax.disruptor.EventFactory;

public class ValueEvent {
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static final EventFactory<ValueEvent> EVENT_FACTORY = new EventFactory<ValueEvent>() {

		@Override
		public ValueEvent newInstance() {
			// TODO Auto-generated method stub
			return new ValueEvent();
		}
	};
}
