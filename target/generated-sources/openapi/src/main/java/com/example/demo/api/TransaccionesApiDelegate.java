package com.example.demo.api;

import com.example.demo.model.ModelApiResponse;
import com.example.demo.model.Transaction;
import io.swagger.annotations.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A delegate to be called by the {@link TransaccionesApiController}}.
 * Implement this interface with a {@link org.springframework.stereotype.Service} annotated class.
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-02T02:37:50.172028800-05:00[America/Lima]")
public interface TransaccionesApiDelegate {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * GET /transacciones/historial : Retrieve transaction history
     * This endpoint returns the list of all transactions in the system.
     *
     * @return A list of transactions (status code 200)
     * @see TransaccionesApi#getTransactionHistory
     */
    default ResponseEntity<List<Transaction>> getTransactionHistory() {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"date\" : \"2000-01-23T04:56:07.000+00:00\", \"amount\" : 0.8008281904610115, \"soureAccount\" : \"soureAccount\", \"id\" : \"id\", \"type\" : \"type\", \"destinationAccount\" : \"destinationAccount\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * POST /transacciones/deposito/{accountNumber}/ : Register a deposit to an account
     * Registers a deposit to a specific account identified by the account number.
     *
     * @param accountNumber The account number where the deposit will be made. (required)
     * @param amount The amount of money to deposit. (required)
     * @return Deposit successfully registered (status code 200)
     *         or Account with number \&quot;accountNumber\&quot; not found. (status code 404)
     *         or Bad request due to invalid data. (status code 400)
     * @see TransaccionesApi#registerDeposit
     */
    default ResponseEntity<Transaction> registerDeposit(String accountNumber,
        Double amount) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"date\" : \"2000-01-23T04:56:07.000+00:00\", \"amount\" : 0.8008281904610115, \"soureAccount\" : \"soureAccount\", \"id\" : \"id\", \"type\" : \"type\", \"destinationAccount\" : \"destinationAccount\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * POST /transacciones/transferencia/ : Register a money transfer between two accounts
     * Registers a money transfer from a source account to a destination account.
     *
     * @param sourceAccount The source account number from which the transfer will be made. (required)
     * @param destinationAccount The destination account number where the transfer will be received. (required)
     * @param amount The amount of money to transfer. (required)
     * @return Withdrawal successfully registered (status code 200)
     *         or Bad request due to invalid data. (status code 400)
     *         or One or both accounts not found. (status code 404)
     * @see TransaccionesApi#registerTransfer
     */
    default ResponseEntity<Transaction> registerTransfer(String sourceAccount,
        String destinationAccount,
        Double amount) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"date\" : \"2000-01-23T04:56:07.000+00:00\", \"amount\" : 0.8008281904610115, \"soureAccount\" : \"soureAccount\", \"id\" : \"id\", \"type\" : \"type\", \"destinationAccount\" : \"destinationAccount\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

    /**
     * POST /transacciones/retiro/{accountNumber}/ : Register a withdrawal from an account
     * Registers a withdrawal from a specific account identified by the account number. number.
     *
     * @param accountNumber The account number from which the withdrawal will be made. (required)
     * @param amount The amount of money to withdraw. (required)
     * @return Withdrawal successfully registered (status code 200)
     *         or Account with number \&quot; accountNumber \&quot; not found.. (status code 404)
     *         or Bad request due to invalid data. (status code 400)
     *         or Withdrawal amount exceeds available balance. (status code 422)
     * @see TransaccionesApi#registerWithdrawal
     */
    default ResponseEntity<Transaction> registerWithdrawal(String accountNumber,
        Double amount) {
        getRequest().ifPresent(request -> {
            for (MediaType mediaType: MediaType.parseMediaTypes(request.getHeader("Accept"))) {
                if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
                    String exampleString = "{ \"date\" : \"2000-01-23T04:56:07.000+00:00\", \"amount\" : 0.8008281904610115, \"soureAccount\" : \"soureAccount\", \"id\" : \"id\", \"type\" : \"type\", \"destinationAccount\" : \"destinationAccount\" }";
                    ApiUtil.setExampleResponse(request, "application/json", exampleString);
                    break;
                }
            }
        });
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);

    }

}
