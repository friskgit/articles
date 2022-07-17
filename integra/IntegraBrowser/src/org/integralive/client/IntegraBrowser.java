package org.integralive.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.DOM;
import com.google.gwt.core.client.GWT;

public class IntegraBrowser implements EntryPoint {
    private static final String XML_LABEL_STYLE = "xmlLabel";
    private static final String USER_TABLE_LABEL_STYLE = "rh";
    private static final String USER_TABLE_STYLE = "table";
    private static final String NOTES_STYLE = "notes";

    private static TabPanel tab = null;
    private static FlowPanel xmlSource = null;
    private static DockPanel xmlParsed = null;
    private static DockPanel xmlEdit = null;
    private static HorizontalPanel menuPanel = null;
    private static FlowPanel northPane = null;
    private static IntegraHelper integraHelper;
    private BundleParser bundleParser = null;
    private DisplayObject display = null;
    private BundleEditor bundleEditor = null;

    private static IntegraBrowser mainBrowser;

    public void onModuleLoad() {
        mainBrowser = this;
        tab = new TabPanel();
        xmlSource = new FlowPanel();
        xmlParsed = new DockPanel();
        xmlEdit = new DockPanel();
        northPane = new FlowPanel();
        menuPanel = new HorizontalPanel();
        tab.add(xmlParsed, "IXD Browser");
        tab.add(xmlSource, "IXD Source");
        tab.add(xmlEdit, "Edit IXD");
        tab.selectTab(0);
        RootPanel.get().add(tab);
        fileOpenInterface();
        bundleParser = new BundleParser();
        bundleEditor = new BundleEditor(xmlEdit, bundleParser);
        integraHelper = new IntegraHelper(this, bundleParser);
        DOM.addEventPreview(integraHelper);
    }

    public static IntegraBrowser getBrowserSingleton() {
        return mainBrowser;
    }

    public BundleParser getBundleParser() {
        return bundleParser;
    }

    public DockPanel getMainDockPanel() {
        return xmlParsed;
    }

    public DisplayObject getDisplayObject() {
        return display;
    }

    public static IntegraHelper getIntegraHelper() {
        return integraHelper;
    }

    public void fileOpenInterface() {

        final FormPanel formPanel = new FormPanel();
        formPanel.setAction("http://henrikfrisk.homeunix.net");
        formPanel.setEncoding(FormPanel.ENCODING_MULTIPART);
        formPanel.setMethod(FormPanel.METHOD_POST);
        formPanel.setWidget(menuPanel);

        final FileUpload upload = new FileUpload();
        upload.setName("loadFileElement");
        //        upload.setStylePrimaryName("gwt-inputBox");
        menuPanel.add(upload);
        
        menuPanel.add(new Button("Load", new ClickListener() {
                public void onClick(Widget sender) {
                    loadAsynchronXML("MyBundle.ixd");
                    //                         loadAsynchronXML("MyCollection.ixd");
                    //loadAsynchronXML(upload.getFilename());
                }
            }));
        northPane.add(menuPanel);
        xmlParsed.add(northPane, DockPanel.NORTH);
        menuPanel.addStyleName("ntg-northPane-style");
        menuPanel.addStyleName("inputBox");
    }

    public void loadAsynchronXML(String filename) {
        HTTPRequest.asyncGet(filename, new ResponseTextHandler() {
                public void onCompletion(String responseText) {
                    /* Display the raw xml */
                    xmlPane(responseText, xmlSource);
                    /* Parse and render the xml */
                    bundleParser.parseBundle(responseText);
                    bundleParser.publishBundle(menuPanel, northPane, xmlParsed);
                    bundleEditor.initEditor();
                }
            });
    }

    public void loadAsynchronIXDDefinition(String filename) {
        HTTPRequest.asyncGet(filename, new ResponseTextHandler() {
                public void onCompletion(String responseText) {
                    xmlPane(responseText, xmlSource);
                    if(display != null) {
                        xmlParsed.remove(display);
                        display = null;
                    }
                    display = new DisplayObject(new ClassDefinition(responseText), bundleParser);
                    xmlParsed.add(display, DockPanel.CENTER);
                }
            });
    }

    public void testPrintEast(String item) {
        xmlParsed.add(new HTML(item), DockPanel.EAST);
    }

    private FlexTable createOrderTable(FlowPanel xmlParsed, String label) {
        HTML orderTableLabel = new HTML("<h2>" + label + "</h2>");
        xmlParsed.add(orderTableLabel);
        FlexTable orderTable = new FlexTable();
        orderTable.setStyleName(USER_TABLE_STYLE);
        orderTable.setBorderWidth(0);
        orderTable.getRowFormatter().setStyleName(0, USER_TABLE_LABEL_STYLE);
        orderTable.setText(0, 0, "Index");
        orderTable.setText(0, 1, "Attribute");
        orderTable.setText(0, 2, "Value");
        xmlParsed.add(orderTable);
        return orderTable;
    }

    /**
     * Creates the xml representation of xmlText. xmlText is assumed to have
     * been validated for structure on the server.
     * 
     * @param xmlText xml text
     * @param xmlParsed panel to display customer record
     */
    private void browsingPane(String xmlText, FlowPanel xmlParsed) {

        if(xmlParsed.getWidgetCount() > 0)
            xmlParsed.clear();

        String title = "";
        Document ixdDom = XMLParser.parse(xmlText);
        Element ixdElement = ixdDom.getDocumentElement();
        XMLParser.removeWhitespace(ixdElement);

        // Class Name (qualified)
        String nameValue = getElementTextValue(ixdElement, "name");
        String parentValue = getAttributeTextValue(ixdElement, "parent", "title");
        if(parentValue.equals("") || parentValue == null)
            title = "<h1>" + nameValue + "</h1>";
        else title = "<h1>" + parentValue + "::" + nameValue + "</h1>";
        HTML titleHTML = new HTML(title);
        xmlParsed.add(titleHTML);

        // Class description
        String notesValue = getElementTextValue(ixdElement, "description");
        Label notesText = new Label();
        notesText.setStyleName(NOTES_STYLE);
        notesText.setText(notesValue);
        xmlParsed.add(notesText);

        // Work properties table
        FlexTable pendingTable = createOrderTable(xmlParsed, "Work properties");

        // Fill Orders Table
        NodeList attributes = ixdElement.getElementsByTagName("attribute");
        int propsRowPos = 0;
        for (int i = 0; i < attributes.getLength(); i++) {
            Element attr = (Element) attributes.item(i);
            int rowPos;
            rowPos = ++propsRowPos;
            int columnPos = 0;
            fillInPropsTableRow(ixdElement, attr, pendingTable, rowPos, columnPos);
        }
    }

    private void fillInPropsTableRow(Element ixdElement, Element attr,
                                     HTMLTable table, int rowPos, int columnPos) {
        if(rowPos % 2 == 0)
            table.getRowFormatter().setStyleName(rowPos, "r1");
        else table.getRowFormatter().setStyleName(rowPos, "r2");
        // Attr ID
        String attrId = attr.getAttribute("id");
        table.setText(rowPos, columnPos++, attrId);

        // Item
        Element name = (Element) attr.getElementsByTagName("name").item(0);
        String nameValue = name.getFirstChild().getNodeValue();
        table.setText(rowPos, columnPos++, nameValue);
    }

    /**
     * Utility method to return the values of elements of the form <myTag>tag
     * value</myTag>
     */
    private String getElementTextValue(Element parent, String elementTag) {
        return parent.getElementsByTagName(elementTag).item(0).getFirstChild()
            .getNodeValue();
    }

    private String getAttributeTextValue(Element parent, String elementTag, String attributeTag) {
        Node node = parent.getElementsByTagName(elementTag).item(0);
        return node.getAttributes().getNamedItem(attributeTag).getNodeValue();
    }

    /**
     * Show the raw XML.
     * 
     * @param xmlText
     * @param xmlSource
     */
    private void xmlPane(String xmlText, final FlowPanel xmlSource) {
        if(xmlSource.getWidgetCount() > 0)
            xmlSource.clear();
        xmlText = xmlText.replaceAll("<", "&#60;");
        xmlText = xmlText.replaceAll(">", "&#62;");
        Label xml = new HTML("<pre>" + xmlText + "</pre>", false);
        xml.setStyleName(XML_LABEL_STYLE);
        xmlSource.add(xml);
    }
}
