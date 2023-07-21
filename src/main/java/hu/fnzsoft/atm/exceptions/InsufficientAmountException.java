package hu.fnzsoft.atm.exceptions;

public class InsufficientAmountException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InsufficientAmountException(String errorMessage) {
		super(errorMessage);
	}
}
