package hu.fnzsoft.atm.service;

import hu.fnzsoft.atm.exceptions.BadWithdrawalAmountException;
import hu.fnzsoft.atm.exceptions.InsufficientAmountException;

import java.util.Map;

public interface AtmService {

    public Long deposit(Map<String, Integer> depositAmount);

    public Map<String, Integer> withdraw(Long amount) throws BadWithdrawalAmountException, InsufficientAmountException;
}
