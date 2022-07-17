package org.integralive.client;

import com.google.gwt.core.client.GWT;

public class InstructionBlock {
        
    private int type = ParentReferences.INSTRUCTION_TYPE;
    private String name;
    private String URL;
    private String ref;
        
    public InstructionBlock(String name, String url, String ref) {
        this.name = name;
        this.URL = url;
        this.ref = ref;
    }
        
    public String getName() {
        return name;
    }

    public String getURL() {
        return URL;
    }

    public int getType() {
        return type;
    }

    public String getObjectRef() {
        return ref;
    }

    public void setURL(String url) {
        this.URL = url;
    }
}
