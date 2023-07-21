package hu.fnzsoft.atm.model;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Atm {

    private final Map<String, Integer> VAULT = new HashMap<>();

    public Map<String, Integer> getVault() {
        return VAULT;
    }

    public void depositToAtm(Map<String, Integer> deposits) {
        deposits.forEach((bankNote, quantity) -> {
            if (VAULT.containsKey(bankNote)) VAULT.put(bankNote, VAULT.get(bankNote) + quantity);
            else VAULT.put(bankNote, quantity);
        });
    }

    public void reduceTheAtmStock(Map<String, Integer> resultOfWithdraw) {
        resultOfWithdraw.forEach((bankNote, quantity) -> VAULT.put(bankNote, VAULT.get(bankNote) - quantity));
    }

    public boolean isAtmIsEmpty() {
        return VAULT.isEmpty();
    }

}