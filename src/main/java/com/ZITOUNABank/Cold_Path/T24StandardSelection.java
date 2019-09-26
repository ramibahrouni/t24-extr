package com.ZITOUNABank.Cold_Path;

import com.jbase.jremote.*;
import org.json.JSONObject;



public class T24StandardSelection {

    private String tableName;

    private JSONObject sysFieldJs;
    private JSONObject usrFieldJs;
    private String localRefPros;


    T24StandardSelection(String tableName) throws JRemoteException {
        this.tableName = tableName;
        this.sysFieldJs = new JSONObject();
        this.usrFieldJs = new JSONObject();
        loadTable();
    }



    private void loadTable() throws JRemoteException {

        DefaultJConnectionFactory cxf = new DefaultJConnectionFactory();
        cxf.setHost("t24-dev2");
        cxf.setPort(6060); // Port should match the port jAgent is listening
        JConnection cx = cxf.getConnection("copyprod", "copyprod");
        JStatement statement = cx.createStatement();

        JFile BP = cx.open("F.STANDARD.SELECTION");
        JDynArray record = BP.read(tableName);

        String sysType = "";
        for (int i = 1; i < record.getNumberOfValues(1); i++) {
            sysType = record.get(2, i);
            String fieldNo = "c"+record.get(3, i);
            String fieldName = record.get(1, i).replaceAll("\\.","_");
            if (sysType != null && sysType.equals("D") && !fieldNo.equals("0")) {

                this.sysFieldJs.put(fieldNo, fieldName);
                if (fieldName.equals("LOCAL_REF")) {
                    this.localRefPros = new String(fieldNo);
                }

            }

            cx.close();


        }


        // Local field
        for (int i = 1; i < record.getNumberOfValues(17); i++) {
            String fieldNameLR = record.get(15, i).replaceAll("\\.","_");;
            String fieldNoLR = record.get(17, i);
            if (fieldNoLR.startsWith("LOCAL.REF")) {
                fieldNoLR = fieldNoLR.split(",")[1];
                fieldNoLR ="m"+fieldNoLR.substring(0,fieldNoLR.length()-1);
                this.usrFieldJs.put(fieldNoLR,fieldNameLR);

            }
        }
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public JSONObject getSysFieldJs() { return sysFieldJs; }

    public void setSysFieldJs(JSONObject sysFieldJs) { this.sysFieldJs = sysFieldJs; }

    public JSONObject getUsrFieldJs() { return usrFieldJs; }

    public void setUsrFieldJs(JSONObject usrFieldJs) { this.usrFieldJs = usrFieldJs; }

    public String getLocalRefPros() {
        return localRefPros;
    }

    public void setLocalRefPros(String localRefPros) {
        this.localRefPros = localRefPros;
    }
}
