package com.optivem.kata.banking.core.usecases.depositfunds;

import an.awesome.pipelinr.Command;
import com.optivem.kata.banking.core.domain.accounts.AccountNumber;
import com.optivem.kata.banking.core.domain.accounts.BankAccount;
import com.optivem.kata.banking.core.domain.accounts.BankAccountRepository;
import com.optivem.kata.banking.core.domain.accounts.TransactionAmount;
import com.optivem.kata.banking.core.usecases.VoidResponse;

import static com.optivem.kata.banking.core.domain.common.extensions.Extension.extend;

public class DepositFundsUseCase implements Command.Handler<DepositFundsRequest, VoidResponse> {

    private final BankAccountRepository repository;

    public DepositFundsUseCase(BankAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public VoidResponse handle(DepositFundsRequest request) {
        var accountNumber = getAccountNumber(request);
        var amount = getTransactionAmount(request);

        var bankAccount = findBankAccount(accountNumber);
        bankAccount.deposit(amount);
        repository.update(bankAccount);
        return VoidResponse.EMPTY;
    }

    private AccountNumber getAccountNumber(DepositFundsRequest request) {
        return AccountNumber.of(request.getAccountNumber());
    }

    private TransactionAmount getTransactionAmount(DepositFundsRequest request) {
        return TransactionAmount.of(request.getAmount());
    }

    private BankAccount findBankAccount(AccountNumber accountNumber) {
        return extend(repository).findRequired(accountNumber);
    }
}
