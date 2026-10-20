package com.zituhoq.banking_transaction.in.web.exception;

import com.zituhoq.banking_transaction.core.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // helper class to put correlation id
    private ErrorResponse error(
            String code,
            String message,
            HttpServletRequest req
    ){
        return new ErrorResponse(
                code,
                message,
                req.getHeader("X-Correlation-Id"),
                Instant.now()
        );
    }


    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ErrorResponse handleInsufficientFunds(InsufficientFundsException e, HttpServletRequest req){
        return error("INSUFFICIENT_FUNDS", e.getMessage(), req);
    }

    @ExceptionHandler(InvalidAccountStateException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ErrorResponse handleInvalidAccountState(InvalidAccountStateException e, HttpServletRequest req) {
        return error("INVALID_ACCOUNT_STATE", e.getMessage(), req);
    }

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAccountNotFound(AccountNotFoundException e, HttpServletRequest req) {
        return error("ACCOUNT_NOT_FOUND", e.getMessage(), req);
    }

    @ExceptionHandler(NegativeAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNegativeAmount(NegativeAmountException e, HttpServletRequest req) {
        return error("NEGATIVE_AMOUNT", e.getMessage(), req);
    }

    @ExceptionHandler(SameAccountTransferException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSameAccountTransfer(SameAccountTransferException e, HttpServletRequest req) {
        return error("SAME_ACCOUNT_TRANSFER", e.getMessage(), req);
    }

    @ExceptionHandler(InvalidAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidAmount(InvalidAmountException e, HttpServletRequest req) {
        return error("INVALID_AMOUNT", e.getMessage(), req);
    }



    // validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException e, HttpServletRequest req) {

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err ->
                        err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return error("VALIDATION_ERROR", message, req);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleOptimisticLock(OptimisticLockingFailureException e, HttpServletRequest req) {
        return error("CONCURRENT_MODIFICATION", e.getMessage(), req);
    }


    // generic errors
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneric(Exception e, HttpServletRequest req) {
        return error(
                "INTERNAL_SERVER_ERROR",
                "Unexpected error occurred",
                req
        );
    }
}
