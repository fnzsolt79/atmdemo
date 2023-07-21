package hu.fnzsoft.atm;

import hu.fnzsoft.atm.exceptions.BadWithdrawalAmountException;
import hu.fnzsoft.atm.exceptions.InsufficientAmountException;
import hu.fnzsoft.atm.service.AtmService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

@SpringBootTest
public class AtmServiceUnitTest {
	
	final static Logger log = LoggerFactory.getLogger(AtmServiceUnitTest.class);

    @Autowired
    AtmService hatmService;

    @BeforeEach
    public void clearAtmVault() {
        try {
            hatmService.withdraw(hatmService.deposit(Map.of()));
        } catch (BadWithdrawalAmountException | InsufficientAmountException e) {
            log.info("When we try empty Atm we caught: " + e.toString());
        }
    }

    @Test
    public void testIfWeDoNotHaveEnoughMoneyInTheFirstRound() throws BadWithdrawalAmountException, InsufficientAmountException {
        var vault = hatmService.deposit(Map.of("1000", 2, "10000", 3));
        assertEquals(vault, 32000L);
        assertThrows(InsufficientAmountException.class, () -> {hatmService.withdraw(16000L);});
        vault = hatmService.deposit(Map.of("1000", 5));
        assertEquals(vault, 37000L);
        Map<String, Integer> banknotes =  hatmService.withdraw(16000L);
        assertFalse(banknotes.isEmpty());
    }

    @Test
    public void testIfWeHaveEnoughMoneyInTheAtm() throws BadWithdrawalAmountException, InsufficientAmountException {
        var vault = hatmService.deposit(Map.of("1000", 1, "2000", 1, "5000", 1,"10000", 1, "20000", 1));
        assertEquals(vault, 38000L);
        Map<String, Integer> banknotes =  hatmService.withdraw(16000L);
        assertEquals(1, banknotes.get("10000"));
        assertEquals(1, banknotes.get("5000"));
        assertEquals(1, banknotes.get("1000"));
    }

    @Test
    public void testIfWeDoNotHaveEnoughBanknote() {
        var vault = hatmService.deposit(Map.of("1000", 1, "2000", 1, "5000", 1,"10000", 1, "20000", 1));
        assertEquals(vault, 38000L);
        assertThrows(InsufficientAmountException.class, () -> {hatmService.withdraw(160000L);
        });
    }

    @Test
    public void testIfWeDoNotInitTheAtm() {
        assertThrows(InsufficientAmountException.class, () -> {
        	hatmService.withdraw(1000L);
        });
    }

    @Test
    public void testIfWeWantWithdrawWrongAmount() {
        assertThrows(BadWithdrawalAmountException.class, () -> {
            hatmService.withdraw(100L);
        });
    }

    @Test
    public void testAgainIfWeHaveEnoughMoneyInTheAtm() throws BadWithdrawalAmountException, InsufficientAmountException {
        var vault = hatmService.deposit(Map.of("1000", 2, "10000", 3));
        assertEquals(vault, 32000L);
        Map<String, Integer> banknotes =  hatmService.withdraw(32000L);
        assertFalse(banknotes.isEmpty());
        assertEquals(3, banknotes.get("10000"));
        assertEquals(2, banknotes.get("1000"));
    }

}
