package com.optivem.kata.banking.system;

import com.optivem.kata.banking.core.common.builders.requests.OpenAccountRequestBuilder;
import com.optivem.kata.banking.core.usecases.openaccount.OpenAccountResponse;
import com.optivem.kata.banking.core.usecases.viewaccount.ViewAccountResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.awt.*;

import static com.optivem.kata.banking.core.common.builders.requests.OpenAccountRequestBuilder.openAccountRequest;
import static com.optivem.kata.banking.core.common.builders.requests.ViewAccountRequestBuilder.viewAccountRequest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration
class BankAccountControllerSystemTest {

    @Autowired
    private WebTestClient client;

    @Test
    void should_open_account_given_valid_request() {
        var request = openAccountRequest().build();

        var response = client.post()
                .uri("bank-accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.CREATED)
                .expectBody(OpenAccountResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.getAccountNumber()).isNotBlank();

        var accountNumber = response.getAccountNumber();

        var viewResponse = client.get()
                .uri("bank-accounts/" + accountNumber)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK)
                .expectBody(ViewAccountResponse.class)
                .returnResult()
                .getResponseBody();

        assertThat(viewResponse).isNotNull();
        assertThat(viewResponse.getAccountNumber()).isEqualTo(accountNumber);
        assertThat(viewResponse.getFullName()).isNotBlank();
        assertThat(viewResponse.getBalance()).isNotNegative();
    }
}
