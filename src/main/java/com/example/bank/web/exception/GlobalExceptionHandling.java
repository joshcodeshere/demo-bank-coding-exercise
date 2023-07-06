package com.example.bank.web.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.bank.exception.AccountDoesNotBelongToCustomerException;
import com.example.bank.exception.AccountHasNoCustomerException;
import com.example.bank.exception.AccountNotFoundException;
import com.example.bank.exception.CustomerExistsWithSsnException;
import com.example.bank.exception.CustomerNotFoundException;
import com.example.bank.exception.DepositMustBeGreaterThanZeroException;
import com.example.bank.exception.NoAccountsForCustomer;
import com.example.bank.exception.TransactionDoesNotBelongToAccountException;
import com.example.bank.exception.TransactionDoesNotBelongToCustomerException;
import com.example.bank.exception.TransactionHasInsufficientFundsException;
import com.example.bank.exception.TransactionNotFoundException;
import com.example.bank.exception.TransactionSourceCannotEqualDestination;
import com.example.bank.exception.TransferMustBeGreaterThanZeroException;
import com.example.bank.exception.WithdrawlMustBeGreaterThanZeroException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path.Node;

/**
 * Global Spring {@link ExceptionHandler} objects via {@link ControllerAdvice}
 * annotation.
 */
@ControllerAdvice
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler {

    /**
     * Handle validation errors from service layer, insteand of having the
     * validation just on the {@link RestController} objects.
     * 
     * @param exception the {@link ConstraintViolationException} to handle
     * @return a {@link Map} containing {@link ConstraintViolationException}
     *         message, stack,
     *         and the DTO fields with
     *         validation failures
     */
    @ExceptionHandler
    protected ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException exception, WebRequest request) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", "DTO contains validation errors");

        Map<String, Object> fields = new HashMap<>();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            if (violation == null || violation.getPropertyPath() == null) {
                continue;
            }
            String field = null;
            for (Node node : violation.getPropertyPath()) {
                if (node != null) {
                    field = node.getName();
                }
            }
            if (field != null) {
                fields.put(field, violation.getMessage());
            }
        }

        if (!fields.isEmpty()) {
            payload.put("fieldConstraintViolations", fields);
        }

        return handleExceptionInternal(exception, payload,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handle {@link RuntimeException} errors from service layer.
     * 
     * @param exception the {@link RuntimeException} to process
     * @return a map containing {@link RuntimeException} message, stack, and HTTP
     *         status code based on exception class
     */
    @ExceptionHandler
    protected ResponseEntity<Object> handleServiceLayerRuntimeException(
            RuntimeException exception, WebRequest request) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", exception.getMessage());
        return handleExceptionInternal(exception, payload,
                new HttpHeaders(), getHttpStatus(exception), request);
    }

    /**
     * @param exception
     * @return the {@link HttpStatus} for the given service layer
     *         {@link RuntimeException}
     */
    private HttpStatus getHttpStatus(RuntimeException exception) {
        if (exception instanceof AccountDoesNotBelongToCustomerException
                || exception instanceof TransactionDoesNotBelongToAccountException
                || exception instanceof TransactionDoesNotBelongToCustomerException
                || exception instanceof DepositMustBeGreaterThanZeroException
                || exception instanceof TransactionHasInsufficientFundsException
                || exception instanceof TransferMustBeGreaterThanZeroException
                || exception instanceof TransactionSourceCannotEqualDestination
                || exception instanceof WithdrawlMustBeGreaterThanZeroException) {

            return HttpStatus.BAD_REQUEST;

        } else if (exception instanceof CustomerExistsWithSsnException) {

            return HttpStatus.CONFLICT;

        } else if (exception instanceof AccountHasNoCustomerException
                || exception instanceof AccountNotFoundException
                || exception instanceof CustomerNotFoundException
                || exception instanceof NoAccountsForCustomer
                || exception instanceof TransactionNotFoundException) {

            return HttpStatus.NOT_FOUND;

        } else {

            return HttpStatus.INTERNAL_SERVER_ERROR;

        }
    }

}
