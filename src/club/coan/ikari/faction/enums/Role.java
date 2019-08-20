package club.coan.ikari.faction.enums;

import lombok.Getter;

@Getter
public enum Role {

    LEADER("***"),
    COLEADER("**"),
    CAPTAIN("*"),
    MEMBER("");

    private String astrix;

    Role(String astrix) {
        this.astrix = astrix;
    }

}
