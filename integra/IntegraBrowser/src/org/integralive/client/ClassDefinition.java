package org.integralive.client;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.core.client.GWT;

public class ClassDefinition {

    private String name;
    private String parent;
    private String parentUri;
    private HTML parentLink;
    private String description;
    private HTML documentation;
    private String version;
    private int numberOfAttributes = 0;
    private String[] attributeNames;
    private String[] attributeDescriptions;
    private Document dom = null;
    private Element element = null; 
    private int classIndex = 0;
    
    private static final String NAME = "name";
    private static final String PARENT = "parent";
    private static final String DESCRIPTION = "description";
    private static final String DOCUMENTATION = "documentation";
    private static final String VERSION = "version";
    private static final String HREF = "href";
    private static final String TITLE = "title";

    public ClassDefinition(String ixdClassDef) {
        parseIXD(ixdClassDef);
        initObject();
    }

    public ClassDefinition(String ixdClassDef, int id) {
        classIndex = id;
        parseIXD(ixdClassDef);
        initObject();
    }

    private void parseIXD(String ixdClassDef) {
        dom = XMLParser.parse(ixdClassDef);
        element = dom.getDocumentElement();
        XMLParser.removeWhitespace(element);
    }

    private void initObject() {
        name = BundleParser.getElementTextValue(element, NAME);
        parent = BundleParser.getAttributeTextValue(element, PARENT, TITLE);
        parentUri = BundleParser.getAttributeTextValue(element, PARENT, HREF);
        description = BundleParser.getElementTextValue(element, DESCRIPTION);
        documentation = constructDocumentation();
        version = BundleParser.getElementTextValue(element, VERSION);
        constructAttributeNameArray();
        parentLink = constructParentUri();
    }
    
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getParent() {
        return parent;
    }

    public String getParentUri() {
        return parentUri;
    }

    public HTML getParentLink() {
        return parentLink;
    }

    public HTML getDocumentation() {
        return documentation;
    }

    public String getVersion() {
        return version;
    }

    public String[] getAttributeNames() {
        return attributeNames;
    }

    public String getAttributeName(int index) {
        return attributeNames[index];
    }

    public String getAttributeDescription(int index) {
        if(attributeDescriptions == null)
            return attributeDescriptions[index];
        else return "";
    }

    public int getAttributeCount() {
        return numberOfAttributes;
    }

    public int getClassIndex() {
        return classIndex;
    }

    public void setClassIndex(int id) {
        classIndex = id;
    }

    private HTML constructDocumentation() {
         String link = BundleParser.getAttributeTextValue(element, DOCUMENTATION, "title");
         HTML hyperLink = 
             new HTML("<a href=\"" + BundleParser.getAttributeTextValue(element, DOCUMENTATION, HREF) + "\">"+link+"</a>");
        return hyperLink;
    }

    private void constructAttributeNameArray() {
        NodeList nl = BundleParser.getElementNodeList(element, "attribute");
        Element attributeElement = null;
        Element nameElement = null;
        Element descriptionElement = null;
        numberOfAttributes = nl.getLength();
        if(BundleParser.getCurrentDocumentType() == BundleParser.MODULE_COLLECTION)
            numberOfAttributes = numberOfAttributes / 2;
        //        GWT.log(String.valueOf(numberOfAttributes), null);
        attributeNames = new String[numberOfAttributes];
        //        attributeDescriptions = new String[numberOfAttributes];
        for(int i = 0; i < numberOfAttributes; i++) {
            attributeElement = (Element)nl.item(i);
            nameElement = (Element)attributeElement.getElementsByTagName("name").item(0);
            if(nameElement == null)
                return;
            //            descriptionElement = (Element)attributeElement.getElementsByTagName("description").item(0);
            attributeNames[i] = nameElement.getFirstChild().getNodeValue();
            //            attributeDescriptions[i] = descriptionElement.getFirstChild().getNodeValue();
        }
    }
    
    private HTML constructParentUri() {
        return new HTML("Inherits interface from <a href=\"" + parentUri + "\">" + parent + "</a>");
    }
}