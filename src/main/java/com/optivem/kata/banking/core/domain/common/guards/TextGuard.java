package com.optivem.kata.banking.core.domain.common.guards;

import com.optivem.kata.banking.core.domain.accounts.Text;

public class TextGuard extends BaseGuard<Text> {

    public TextGuard(Text value) {
        super(value);
    }

    public void againstNullOrWhitespace(String message) {
        against(value::isNullOrWhitespace, message);
    }
}
