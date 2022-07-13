package io.github.drhampust.mysql_sync.util;

public enum SQLDataType {
    // Text based below
    CHAR(false, true),
    VARCHAR(false, true),
    BINARY(false, true),
    VARBINARY(false, true),
    TINYBLOB(false),
    TINYTEXT(false),
    TEXT(false, true),
    BLOB(false, true),
    MEDIUMTEXT(false),
    MEDIUMBLOB(false),
    LONGTEXT(false),
    LONGBLOB(false),
    ENUM(false),
    SET(false),
    // Numerical below
    BIT(true, true),
    TINYINT(true, true),
    BOOL(true),
    BOOLEAN(true),
    SMALLINT(true, true),
    MEDIUMINT(true, true),
    INT(true, true),
    INTEGER(true, true),
    BIGINT(true, true),
    FLOAT(true, true),
    DOUBLE(true, true),
    DECIMAL(true, true);
    private final boolean sizeable;
    private final boolean numerical;

    private SQLDataType(boolean numerical) {
        this.numerical = numerical;
        this.sizeable = false;
    }
    private SQLDataType(boolean numerical, boolean sizeable) {
        this.numerical = numerical;
        this.sizeable = sizeable;
    }

    public boolean isNumerical() {
        return numerical;
    }

    public boolean isSizeable() {
        return sizeable;
    }
}