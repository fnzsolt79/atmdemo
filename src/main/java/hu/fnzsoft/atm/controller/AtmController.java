package hu.fnzsoft.atm.controller;

import hu.fnzsoft.atm.exceptions.BadWithdrawalAmountException;
import hu.fnzsoft.atm.exceptions.InsufficientAmountException;
import hu.fnzsoft.atm.service.HungarianAtmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/")
public class AtmController {

    @Autowired
    HungarianAtmService atmService;

    @Operation(summary = "You can deposit money into ATM")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit money if use a banknote which is available in Hungary",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)) })})
    @PostMapping(value = "/Deposit", consumes = "application/json", produces = "application/json")
    public Long depositMoney(@RequestBody Map<String, Integer> request) {
        return atmService.deposit(request);
    }

    @Operation(summary = "You can withdraw money from ATM if a sufficient amount is in the machine.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdraw request was successful!",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class)) }),
            @ApiResponse(responseCode = "503", description = "ATM is out of service or not enough money", content = @Content),
            @ApiResponse(responseCode = "400", description = "Entering an invalid amount ", content = @Content) })
    @PostMapping(value = "/Withdrawal", consumes = "application/json", produces = "application/json")
    public Map<String, Integer> withdrawMoney(@RequestBody Long request) {
        try {
            return atmService.withdraw(request);
        }
        catch (BadWithdrawalAmountException exc) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad given amount!", exc);
        } catch (InsufficientAmountException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "ATM is not available at the moment or do not have enough banknote!", e);
        }

    }

}
