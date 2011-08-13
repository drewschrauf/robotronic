package com.drewschrauf.robotronic.threads;

public class ParsingException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParsingException() {
	}

	public ParsingException(String detailMessage) {
		super(detailMessage);
	}

	public ParsingException(Throwable throwable) {
		super(throwable);
	}

	public ParsingException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
