package com.ZITOUNABank.Cold_Path;
import oracle.xdb.XMLType;

public class Record {
    public String RECID;
    public XMLType XMLRECORD;


    public String getRECID() {
        return RECID;
    }

    public void setRECID(String RECID) {
        this.RECID = RECID;

    }

    public XMLType setXMLRECORD(XMLType XMLRECORD) {
        return XMLRECORD;
    }


    @Override
    public String toString() {
        return "Record{" +
                "RECID='" + RECID + '\'' +
                ", XMLRECORD=" + XMLRECORD +
                '}';
    }
}
