package org.integralive.client;

import java.util.HashMap;

/**
 * Inner class to cache object references. See IntegraHelper
 *
 * Each Object has its own instance of this class with a HashMap of
 * attributeName/objectId(s) key/value pair (there may be many
 * references for each attributeName). Only attributes whos values are
 * object references are stored here. This class is used for caching
 * purposes and a Vector of the last N instances are kept in IntegraHelper.
 */
public class ObjectReferences extends HashMap {
        
    private String className = "";
    private String instanceName = "";
    private String objectId = "";

    public ObjectReferences () {}

    public ObjectReferences(String ntgClass, String instance, String id) {
        className = ntgClass;
        instanceName = instance;
        objectId = id;
    }

    public void setClassName(String ntgClass) {
        className = ntgClass;
    }

    public String getClassName() {
        return className;
    }

    public void setInstanceName(String ntgInstance) {
        instanceName = ntgInstance;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setObjectId(String id) {
        this.objectId = id;
    }

    public String getObjectId() {
        return this.objectId;
    }
}
