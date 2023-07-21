package hu.fnzsoft.atm.exceptions;

public class BadWithdrawalAmountException  extends Exception {

	private static final long serialVersionUID = 1L;

	public BadWithdrawalAmountException(String errorMessage){
		super(errorMessage);
    }

}
