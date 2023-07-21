package hu.fnzsoft.atm.service;

import hu.fnzsoft.atm.exceptions.BadWithdrawalAmountException;
import hu.fnzsoft.atm.exceptions.InsufficientAmountException;
import hu.fnzsoft.atm.model.Atm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HungarianAtmService implements AtmService {

    private final List<Integer> HUNGARIAN_BANK_NOTES_VALUE = List.of(1000, 2000, 5000, 10000, 20000);
    private final Map<String, Integer> EMPTY_WITHDRAW = Collections.emptyMap();

    @Autowired
    Atm atmMachine;

    @Override
    public Long deposit(Map<String, Integer> deposits) {
        atmMachine.depositToAtm(deposits);
        return getBudgetFromAVault();
    }
    private Long getBudgetFromAVault() {
        long budget = 0L;
        for (Map.Entry<String, Integer> e : atmMachine.getVault().entrySet()) {
            budget += Long.parseLong(e.getKey()) * e.getValue();
        }
        return budget;
    }

    @Override
    public Map<String, Integer> withdraw(Long withdrawalAmount) throws BadWithdrawalAmountException, InsufficientAmountException {
        if (withdrawalAmount % 1000 != 0) {
            throw new BadWithdrawalAmountException("The given amount is can not divide by 1000");
        }

        Map<String, Integer> withdrawalBanknotes = new HashMap<>();

        for (int i = HUNGARIAN_BANK_NOTES_VALUE.size() - 1; i >= 0; i--) {
            var demandQuantityOnANote = (int) (withdrawalAmount / HUNGARIAN_BANK_NOTES_VALUE.get(i));
            if (isActualDemandIsValid(i, demandQuantityOnANote)) {
                var reduceQuantity = getReduceQuantityOfABanknote(demandQuantityOnANote, HUNGARIAN_BANK_NOTES_VALUE.get(i).toString());
                if (reduceQuantity > 0) {
                    withdrawalAmount -= (long) reduceQuantity * HUNGARIAN_BANK_NOTES_VALUE.get(i);
                    withdrawalBanknotes.put(HUNGARIAN_BANK_NOTES_VALUE.get(i).toString(), reduceQuantity);
                }
            }
        }

        if (isWeSuccessWithWithdraw(withdrawalAmount)) {
            atmMachine.reduceTheAtmStock(withdrawalBanknotes);
        } else {
            withdrawalBanknotes = EMPTY_WITHDRAW;
        }

        if (atmMachine.isAtmIsEmpty()) {
            throw new InsufficientAmountException("ATM is not filled yet!");
        } else if (withdrawalBanknotes.isEmpty()) {
            throw new InsufficientAmountException("Not enough money to serve the request!");
        }
        return withdrawalBanknotes;
    }

    private boolean isActualDemandIsValid(int i, int demandQuantityOnANote) {
        return demandQuantityOnANote > 0 && atmMachine.getVault().containsKey(HUNGARIAN_BANK_NOTES_VALUE.get(i).toString());
    }

    private static boolean isWeSuccessWithWithdraw(long withdrawalAmount) {
        return withdrawalAmount == 0L;
    }

    private int getReduceQuantityOfABanknote(final int demandQuantityOnANote, final String bankNote) {
        return Math.min(demandQuantityOnANote, atmMachine.getVault().getOrDefault(bankNote, 0));
    }
}