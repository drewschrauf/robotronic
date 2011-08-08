package com.drewschrauf.robotronic;

public class ParsingException extends Exception {

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
