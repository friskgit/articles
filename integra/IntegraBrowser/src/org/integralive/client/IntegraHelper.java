package org.integralive.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Node;
import java.util.Vector;
import java.util.Collection;

public class IntegraHelper implements EventPreview {

    private BundleParser bundleParser = null;
    private IntegraBrowser integraBrowser = null;
    private static Vector objectRefVector = new Vector(10);
    private static ObjectReferences currentObjectRefs = null;

    public IntegraHelper(IntegraBrowser ib, BundleParser bp) {
        bundleParser = bp;
        integraBrowser = ib;
    }

    public boolean onEventPreview(Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK) {
            com.google.gwt.user.client.Element target = DOM.eventGetTarget(event);
            if ("a".equalsIgnoreCase(getTagName(target))) {
                String href = DOM.getElementAttribute(target, "href");
                href = href.trim();
                if(href.matches("obj.[0-9]+")) {
//                     currentObjectRefs = IntegraHelper.getLastObjectReference();
//                     if(currentObjectRefs != null)
//                         bundleParser.appendHistory(currentObjectRefs);
                    bundleParser.displayObjectAsync(href);
                }
                return false;
            }
        }
        return true;
    }

    public static ObjectReferences getObjectRefs() {
        return currentObjectRefs;
    }

    public static void setObjectRefs(ObjectReferences obj) {
        currentObjectRefs = obj;
    }

    public static Vector getObjectReferences() {
        return objectRefVector;
    }

    public static void addObjectReference(ObjectReferences obj) {
        if(objectRefVector.size() > 10)
            while(objectRefVector.size() > 10)
                objectRefVector.remove((Object)objectRefVector.firstElement());
        objectRefVector.add((Object)obj);
    }

    /**
     * Lookup the ObjectReferences object by instance name
     */
    public static ObjectReferences getObjectRefByName(String name) {
        ObjectReferences oref = null;
        ObjectReferences result = null;
        for(int i = 0; i < objectRefVector.size(); i++) {
            oref = (ObjectReferences)objectRefVector.elementAt(i);
            if(oref.getInstanceName().equals(name)) {
                result = oref;
                break;
            }
        }
        return result;
    }

    /**
     * Lookup the ObjectReferences object by instance name regex
     * matching. Note that this will return only the *last* match
     * in case multiple matches are found.
     */
    public static ObjectReferences getObjectRefByRegex(String name) {
        ObjectReferences oref = null;
        ObjectReferences result = null;
        String ret = "";
        for(int i = 0; i < objectRefVector.size(); i++) {
            oref = (ObjectReferences)objectRefVector.elementAt(i);
            if(oref.getInstanceName().matches(name)) {
                result = oref;
                break;
            }
        }
        return result;
    }

    public static ObjectReferences getLastObjectReference() {
        if(objectRefVector.size() > 0)
            return (ObjectReferences)objectRefVector.lastElement();
        else return null;
    }

    public static int getIndexOf(ObjectReferences or) {
        return objectRefVector.indexOf(or);
    }
    
    public static int getSize() {
        return objectRefVector.size();
    }

    public static String getPreviousObjectName() {
        int size = objectRefVector.size();
        String name = "";
        if(size > 1)
            name = ((ObjectReferences)objectRefVector.elementAt(size - 2)).getInstanceName();
        return name;
    }

    public static ObjectReferences getPreviousObject() {
        int size = objectRefVector.size();
        ObjectReferences ref = null;
        if(size > 1)
            ref = ((ObjectReferences)objectRefVector.elementAt(size - 2));
        return ref;
    }

    public static String[] getReferencesByAttribute(ObjectReferences or, String key) {
        if(or != null) {
            Object objArray = or.get(key);
            return (String[])objArray;
        }
        else return null;
    }

    /**
     * Get all references from enclosing objects.
     *
     * @return A String array of the references or null if object is empty.
     */
    public static String[] getParentObjectRefs() {
        Vector allRefs = new Vector();
        String comb[] = null;
        ObjectReferences oref = getPreviousObject();
        if(oref == null || oref.isEmpty())
            return null;
        Object combinedSet[] = oref.values().toArray();
        if(combinedSet != null) {
            for(int i = 0; i < combinedSet.length; i++) {
                comb = (String[])combinedSet[i];
                if(comb != null) {
                    for(int j = 0; j < comb.length; j++)
                        allRefs.add(comb[j]);
                }
            }
        }
        String allRefsString[] = new String[allRefs.size()];
        for(int k = 0; k < allRefs.size(); k++) {
            allRefsString[k] = (String)allRefs.get(k);
            //            GWT.log(allRefsString[k], null);
        }
        return allRefsString;
    }

    native String getTagName(com.google.gwt.user.client.Element element)
    /*-{
      return element.tagName;
    }-*/;

    
}