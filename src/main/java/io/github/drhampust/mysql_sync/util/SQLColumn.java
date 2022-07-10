package io.github.drhampust.mysql_sync.util;

import io.github.drhampust.mysql_sync.Main;

public class SQLColumn {
    private final String columnName;
    private final sqlDataType type;
    private final String typeSize;
    private final String[] flags;

    public SQLColumn(String columnName, sqlDataType type){
        this(columnName, type, 0);
    }
    public SQLColumn(String columnName, sqlDataType type, int typeSize){
        this.columnName = columnName;
        this.type = type;
        flags = new String[]{"", "", "", ""};
        setZerofill(false);
        setUnsigned(false);
        setNullable(false);
        setDefault("NULL");
        if (type.isSizeable() && typeSize != 0)
            this.typeSize = "(" + typeSize + ")";
        else this.typeSize = "";
    }
    public SQLColumn(String columnName, sqlDataType type, boolean nullable){
        this(columnName, type, 0);
        setNullable(nullable);
    }
    public SQLColumn(String columnName, sqlDataType type, int typeSize, boolean nullable){
        this(columnName, type, typeSize);
        setNullable(nullable);
    }
    public SQLColumn(String columnName, sqlDataType type, int typeSize, boolean nullable, boolean zeroFill, boolean unsigned, String default_value){
        this(columnName, type, typeSize, nullable);
        setZerofill(zeroFill);
        setUnsigned(unsigned);
        setDefault(default_value);
    }

    private void setZerofill(boolean bool){
        if(type.isNumerical())
            this.flags[0] = bool? "ZEROFILL ": "";
    }
    private void setUnsigned(boolean bool){
        if(type.isNumerical())
            this.flags[1] = bool? "UNSIGNED ": "";
    }
    private void setNullable(boolean bool){
        this.flags[2] = bool? "": "NOT NULL ";
    }
    private void setDefault(String default_value){
        if (default_value.equals("NULL"))
            this.flags[3] = "";
        else
            this.flags[3] = "DEFAULT " + default_value;
    }

    private String getColumnName() {
        return columnName;
    }

    private String getType() {
        return type.name() + typeSize;
    }

    private String getFlags() {
        return flags[0] + flags[1] + flags[2] + flags[3];
    }

    @Override
    public String toString() {
        return ("`" + this.getColumnName() + "` " + this.getType() + " " + this.getFlags()).replaceAll(" *$", "");
    }
}
