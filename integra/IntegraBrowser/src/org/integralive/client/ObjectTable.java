package org.integralive.client;

import java.util.HashMap;

public class ObjectTable {
    
    private HashMap objectTable = null;

    public ObjectTable() {
        objectTable = new HashMap();
    }

    public HashMap getObjectMap() {
        return objectTable;
    }

    public void addObject(int row, String id) {
        objectTable.put(new Integer(row), id);
    }

    public String getObject(int row) {
        return (String)objectTable.get(new Integer(row));
    }

    public void clearTable() {
        objectTable.clear();
    }
}