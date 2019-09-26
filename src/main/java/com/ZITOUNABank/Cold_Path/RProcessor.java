package com.ZITOUNABank.Cold_Path;


import com.jbase.jremote.JRemoteException;
import oracle.xdb.XMLType;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Set;

public class RProcessor implements Processor {


    @Autowired
    ConfigProperties configProp;

    T24StandardSelection ts;




    @Override
    public void process(Exchange exchange) throws Exception {
        String name = configProp.getConfigValue("table_name");
        ts = new T24StandardSelection(name);
        JSONObject  sysFieldJs =  ts.getSysFieldJs();
        JSONObject usrFieldJs = ts.getUsrFieldJs();
        Map rows = exchange.getIn().getBody(Map.class);
        Record record = new Record();
        record.setRECID((String) rows.get("RECID"));
        XMLType xml = record.setXMLRECORD((XMLType) rows.get("XMLRECORD"));
        String row = xml.getStringVal();
        JSONObject jsob = new JSONObject();
        JSONObject js = new JSONObject();
        JSONArray LRefArr = new JSONArray();
        JSONObject LRefElem = new JSONObject();
        JSONObject json = XML.toJSONObject(row);
        JSONObject jObject = json.getJSONObject("row".trim());
        Set<String> keySet = jObject.keySet();
        keySet.retainAll(sysFieldJs.keySet());
        for (String keyStr : keySet) {
            Object keyvalue = jObject.get(keyStr);
            js.put(sysFieldJs.get(keyStr).toString(), keyvalue);
            if (!keyvalue.equals("")) {
                if (keyvalue instanceof JSONObject) {
                    for (String vStr : ((JSONObject) keyvalue).keySet()) {
                        if (vStr.equals("content")) {
                            jsob.put(sysFieldJs.get(keyStr).toString(), ((JSONObject) keyvalue).get("content"));
                        }
                    }

                } else if (keyvalue instanceof JSONArray) {
                    JSONArray jArray = (JSONArray) keyvalue;
                    if (keyStr.equals(ts.getLocalRefPros())) {
                        for (int i = 0; i < jArray.length(); i++) {
                            if (jArray.get(i) instanceof JSONObject){
                                JSONObject item_LR = jArray.getJSONObject(i);
                                JSONArray arr = item_LR.names();
                                for (int j = 0; j < arr.length(); j++) {
                                    if (arr.getString(j).equals("s")) {
                                        item_LR.remove(arr.getString(j));
                                    }
                                    if (arr.getString(j).equals("m")) {
                                        if (item_LR.has("content")) {
                                            if (!item_LR.get(arr.getString(j)).equals("")) {
                                                item_LR.put(arr.getString(j) + "" + item_LR.get(arr.getString(j)).toString(), item_LR.get("content"));
                                                item_LR.remove("m");
                                                item_LR.remove("content");
                                            }
                                        }
                                        Set<String> keyS = item_LR.keySet();
                                        keyS.retainAll(usrFieldJs.keySet());

                                        for (String ky : keyS) {
                                            LRefElem.put(usrFieldJs.get(ky).toString(), item_LR.get(ky));
                                        }
                                    }
                                }
                            } else {
                                LRefArr.put(jArray.get(i));
                            }

                        }
                        LRefArr.put(LRefElem);
                        jsob.put(sysFieldJs.get(keyStr).toString(), LRefArr);
                    } else {
                        JSONArray Array = new JSONArray();
                        for (int i = 0; i < jArray.length(); i++) {

                            if (jArray.get(i) instanceof JSONObject) {
                                JSONObject item = jArray.getJSONObject(i);
                                JSONArray item_arr = item.names();
                                for (int j = 0; j < item_arr.length(); j++) {
                                    if (item_arr.getString(j).equals("s")) {
                                        item.remove(item_arr.getString(j));
                                    }
                                    if (item_arr.getString(j).equals("m")) {
                                        item.remove("m");
                                        if (item.has("content")) {
                                            Array.put(item.get("content"));
                                        }
                                    }


                                }
                            } else {
                                Array.put(jArray.get(i));
                            }
                        }

                        jsob.put(sysFieldJs.get(keyStr).toString(), Array);
                    }

                } else {

                    jsob.put(sysFieldJs.get(keyStr).toString(), keyvalue);
                }
                jsob.put("ID_T24", record.getRECID());
//                jsob.put(sysFieldJs.get(keyStr).toString(), keyvalue.toString());
            }
        }
        exchange.getOut().setBody(jsob.toString());
    }
}
