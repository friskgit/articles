package org.integralive.client;

import com.google.gwt.core.client.GWT;
import java.util.HashMap;
import java.util.Vector;
import java.util.Set;
import java.util.Iterator;

public class ParentReferences extends HashMap {

    public static final int INSTRUCTION_TYPE = 1;
    public static final String MEDIA_CLIP = "mediaClip";
    public static final String INSTRUCTION_BLOCK = "instructionBlock";

    private String field = "";
    private static ParentReferences referencesMap = new ParentReferences();
    private static HashMap objectRefsHash = null;

    public ParentReferences() {
        ParentReferences.objectRefsHash = new HashMap();
    }

    public void addObjectReference(int type, String ref, String name, String URL) {
        if(type == INSTRUCTION_TYPE) {
            InstructionBlock ib = new InstructionBlock(name, URL, ref);
            ParentReferences.objectRefsHash.put(ref, ib);
        }
    }

    public static void addReferencesMap(String name, HashMap refs) {
        ParentReferences.referencesMap.put(name, refs);
    }

    public static HashMap getReferencesMap(String key) {
        return (HashMap)ParentReferences.referencesMap.get(key);
    }

    public static String[] getReferencesObjectNames(String key) {
        Object[] keys = null;
        if(ParentReferences.referencesMap.isEmpty())
            return null;
        else {
            keys = ParentReferences.referencesMap.keySet().toArray();
            String ret[] = new String[keys.length];
            for(int i = 0; i < keys.length; i++)
                ret[i] = (String)keys[i];
            return ret;
        }
    }

    public static String getReferencesMap(String key, String subKey) {
        String returnValue = "";
        HashMap hm = (HashMap)ParentReferences.referencesMap.get(key);
        returnValue = (String)hm.get(subKey);
        return returnValue;
    }

    public static void clearInstructionBlockArray() {
        if(ParentReferences.objectRefsHash.isEmpty())
            return;
        else {
            ParentReferences.objectRefsHash.clear();
        }
    }

    public static HashMap getParentReferences() {
        return objectRefsHash;
    }

    public InstructionBlock getInstructionBlock(String objectId) {
        return (InstructionBlock)ParentReferences.objectRefsHash.get(objectId);
    }

    /**
     * Retrieve all the InstructionBlock objects
     */
    public InstructionBlock[] getInstructionBlockArray() {
        if(ParentReferences.objectRefsHash.isEmpty())
            return null;
        else {
            Object keys[] = ParentReferences.objectRefsHash.keySet().toArray();
            InstructionBlock ib[] = new InstructionBlock[keys.length];
            for(int i = 0; i < keys.length; i++) {
                ib[i] = getInstructionBlock((String)keys[i]);
            }
            return ib;
        }
    }

    public Object[] getInstructionBlockKeys() {
        Object keys[] = null;
        if(ParentReferences.objectRefsHash.isEmpty())
            return null;
        else {
            keys = ParentReferences.objectRefsHash.keySet().toArray();
        }
        return keys;
    }

    public void printParentReferences() {
        if(ParentReferences.objectRefsHash.isEmpty())
            return;
        else {
            Object keys[] = ParentReferences.objectRefsHash.keySet().toArray();
            for(int i = 0; i < keys.length; i++) {
                String s = (String)keys[i];
                InstructionBlock ibck = getInstructionBlock(s);
                //                GWT.log(ibck.getName(), null);
            }
        }
    }

    public static void printReferencesMap() {
        if(ParentReferences.referencesMap.isEmpty())
            return;
        else {
            Object keys[] = ParentReferences.referencesMap.keySet().toArray();
            for(int i = 0; i < keys.length; i++) {
                String s = (String)keys[i];
                GWT.log(s, null);
            }
        }
    }

    public static void printAllReferencesObjects(String key) {
        String s[] = ParentReferences.getReferencesObjectNames(key);
        for(int i = 0; i < s.length; i++)
            GWT.log(s[i], null);
    }
}