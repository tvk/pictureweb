package com.senselessweb.pictureweb.events;

import org.springframework.context.ApplicationEvent;

public class AlbumsChangedEvent extends ApplicationEvent {

	public AlbumsChangedEvent() {
		super(Void.TYPE);
	}
	private static final long serialVersionUID = -720053836812172998L;

}
