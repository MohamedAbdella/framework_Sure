package com.sure.enums;

import lombok.Getter;

public class VotingType {
    @Getter
    public enum votingType {

        PUBLIC_VOTING(1),
        HIDDEN_VOTING(2),
        PUBLIC_SURVEY(3),
        HIDDEN_SURVEY(5);

        private final int value;

        votingType(int value) {
            this.value = value;
        }

    }
}
