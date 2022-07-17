package org.integralive.client;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.SuggestionEvent;

import java.util.Vector;
import java.util.HashMap;

public class BundleParser implements TableListener, MouseListener, SuggestionHandler {

    public class ViewsCommand implements Command {

        private String text = "";
        private String id = "";

        public ViewsCommand() {}
        
        public ViewsCommand(String text, String id) {
            this.text = text;
            this.id = id;
        }

        public void setText(String s) {
            text = s;
        }

        public String getText() {
            return text;
        }

        public void execute() {
            clearDocumentsTable();
            displayView(getViewContents(id), id);
        }
    }

    public class HistoryCommand implements Command {

        private String text = "";
        private String id = "";

        public HistoryCommand() {}
        
        public HistoryCommand(String text, String id) {
            this.text = text;
            this.id = id;
        }

        public void setText(String s) {
            text = s;
        }

        public String getText() {
            return text;
        }

        public void execute() {
            BundleParser.setCurrentObjectId(id);
            Node n = getObjectById(id);
            IntegraBrowser.
                getBrowserSingleton().
                loadAsynchronIXDDefinition(
                                           BundleParser.getNodeAttributeValue(n, 
                                                                              BundleParser.DEFINITION,
                                                                              BundleParser.HREF));   
            
        }
    }

    public static final int MODULE_COLLECTION = 1;
    public static final int INTEGRA_BUNDLE = 2;
    public static final int INTEGRA_COLLECTION = 3;

    public static final String OBJECT = "object";
    public static final String OBJECT_ID = "id";
    public static final String DEFINITION = "definition";
    public static final String HREF = "href";
    public static final String VIEW = "View";
    public static final String NAME = "name";

    private static final String USER_TABLE_LABEL_STYLE = "rh";
    private static final String USER_TABLE_STYLE = "table";
    private static final String IS_NULL = "<it>Undefined</it>";

    private MultiWordSuggestOracle oracle = null;
    private SuggestBox suggestBox = null;
    private HTML title = null;
    private HTML description = null;
    private Document dom = null;
    private Element element = null;
    private FlexTable contents = null;
    private VerticalPanel listPanel = null;
    private int currentRow = 0;
    private static String currentObjectId = null;
    private int docHeader = 0;
    private ClassDefinition authorClass = null;
    private ObjectTable ot = null;
    private static ParentReferences parentRefs = null;
    private static int integraCollectionType = 0;
    private static Node documentRoot = null;
    private static Vector historyMenuItems = new Vector();
    private MenuBar views = null;
    private MenuBar history = null;

    public BundleParser() {
        parentRefs = new ParentReferences(); 
    }

    public static ParentReferences getParentRefs() {
        return parentRefs;
    }

    public void parseBundle(String xmlText) {

        dom = XMLParser.parse(xmlText);
        element = dom.getDocumentElement();
        XMLParser.removeWhitespace(element);
        BundleParser.setDocumentRoot(dom.getFirstChild());
        String rootName = documentRoot.getNodeName();
        if(rootName.equals("ModuleCollection"))
            BundleParser.setCurrentDocumentType(BundleParser.MODULE_COLLECTION);
        else if(rootName.equals("IntegraBundle"))
            BundleParser.setCurrentDocumentType(BundleParser.INTEGRA_BUNDLE);
        else if(rootName.equals("IntegraCollection"))
            BundleParser.setCurrentDocumentType(BundleParser.INTEGRA_COLLECTION);
    }

    private MenuBar initializeMenu() {
        MenuBar m = new MenuBar();
        m.setAutoOpen(true);

        String v[][] = getViews();

        views = new MenuBar(true);
        if(v != null) {
            for(int i = 0; i < v[1].length; i++) {
                views.addItem(v[1][i], new ViewsCommand(v[1][i], v[0][i]));
            }
        }
        else views.addItem("No views", new ViewsCommand());

        history = new MenuBar(true);

        m.addItem(new MenuItem("Views", views));
        m.addItem(new MenuItem("History", history));

        m.setWidth("100%");
        return m;
    }
 
    private HorizontalPanel initalizeSuggestBox() {

        HorizontalPanel flow = new HorizontalPanel();
        Button searchButton = new Button("Search");
        oracle = new MultiWordSuggestOracle();
        suggestBox = new SuggestBox(oracle);
        
        constructNameList();

        suggestBox.addEventHandler(this);

        suggestBox.addStyleName("gwt-inputBox");
        flow.addStyleName("gwt-suggestField");
        searchButton.addStyleName("gwt-searchButton");
        flow.add(suggestBox);
        flow.add(searchButton);

        return flow;
    }

    private void constructNameList() {

        NodeList nl = BundleParser.getElementNodeList(element, "name");
        if(nl != null && nl.getLength() > 0) {
            String result[] = new String[nl.getLength()];
            for(int i = 0; i < nl.getLength(); i++) {
                result[i] = nl.item(i).getFirstChild().getNodeValue();
                oracle.add(result[i]);
            }
        } 
    }
    
    public void publishBundle(HorizontalPanel menuPanel, FlowPanel northPane, DockPanel mainPane) {
        
        menuPanel.add(initializeMenu());
        menuPanel.add(initalizeSuggestBox());
        listPanel = new VerticalPanel();
        HTML bundleName = new HTML("<h1>" + getName() + "</h1>");
        northPane.add(bundleName);
        
        /* Print the bundle name */
        title = new HTML("<h2>" + getName() + "</h2>");
        northPane.add(title);

        /* Print the bundle description */
        description = new HTML("<p>" + getDescription() + "</p>");
        northPane.add(description);
        
        int row = 0;
        /* Add collaborators */
        //        getCollaborators(getViewById("obj.43"));
        NodeList n = getCollaborators();
        if(n != null) {
            String version = getVersion();
            String collabLabel = "";
            if(version.equals(IS_NULL))
                collabLabel = "Collaborators for this version";
            else collabLabel = "Collaborators for version " + version; 
            contents = buildNavList(collabLabel);
            for(int i = 0; i < n.getLength(); i++) {
                row = i + 1;
                contents.setText(row, 0, BundleParser.getElementTextValue(getObjectById(n.item(i).getFirstChild().getNodeValue()), "name"));
                contents.getRowFormatter().setStyleName(row, "r1");
            }
        }
        
        /* Add objects list */
         NodeList ol = getObjects();
         if(ol != null) {
             ot = new ObjectTable();
             contents.setText(++row, 0, "Documents");
             docHeader = row;
             contents.getRowFormatter().setStyleName(row, "rh");
             row++;
             for(int i = 0; i<ol.getLength(); i++) {
                 if(getObjectType(ol.item(i)).equals("public")) {
                     contents.setText(row, 0, BundleParser.getElementTextValue(ol.item(i), "name"));
                     ot.addObject(row, getObjectId(ol.item(i)));
                     contents.getRowFormatter().setStyleName(row, "r1");
                     row++;
                 }
             }
             listPanel.add(contents);
         }

         /* Add the data to the main pane (tab 0) */
         listPanel.addStyleName("ntg-MenyList-style");
         listPanel.setWidth("120px");
         mainPane.add(listPanel, DockPanel.WEST);
    }

    public void displayView(String[] id, String viewId) {
        Node n = null;
        int row = 0, i = 0;
        ot = new ObjectTable();
        title.setHTML("<h2>" + getViewNameById(viewId) + "</h2>");
        description.setHTML("<p>" + getViewDescriptionById(viewId) + "</p>");
        contents = buildNavList("Collaborators");
        contents.getRowFormatter().setStyleName(row, "rh");
        row++;
        String coll[] = getCollaborators(getViewById(viewId));
        if(coll != null) {
            for(i = 0; i < coll.length; i++) {
                contents.setText(row, 0, getObjectNameById(coll[i]));
                ot.addObject(row, coll[i]);
                contents.getRowFormatter().setStyleName(row, "r1");
                row++;
            }
        }
        contents.setText(row, 0, "Documents");
        docHeader = row;
        contents.getRowFormatter().setStyleName(row, "rh");
        row++;
        for(i = 0; i < id.length; i++) {
            String s = getObjectNameById(id[i]);
            ot.addObject(row, id[i]);
            contents.setText(row, 0, s);
            contents.getRowFormatter().setStyleName(row, "r1");
            row++;
        }
        listPanel.add(contents);
    }
    
    public void clearDocumentsTable() {
        contents.removeFromParent();
        ot.clearTable();
        ot = null;
        contents = null;
    }

    public void appendHistory(ObjectReferences ref) {
        String instanceName = ref.getInstanceName();
        //        GWT.log(String.valueOf(historyMenuItems.size()), null);
        if(historyMenuItems.size() > 0) {
            for(int i = 0; i < historyMenuItems.size(); i++) {
                MenuItem m = (MenuItem)historyMenuItems.get(i);
                GWT.log(m.getText() + " -- " + instanceName, null);
                if(m.getText().equals(instanceName)) {
                    history.removeItem(m);
                    historyMenuItems.removeElementAt(i);
                    break;
                }
            }
        }
        MenuItem mi = new MenuItem(ref.getInstanceName(), new HistoryCommand(ref.getInstanceName(), ref.getObjectId()));
        historyMenuItems.add(mi);
        history.addItem(mi);
    }
    
    public static void setCurrentObjectId(String id) {
        currentObjectId = id;
    }

    public static String getCurrentObjectId() {
        return currentObjectId;
    }

    public static int getCurrentDocumentType() {
        return integraCollectionType;
    }

    public static void setCurrentDocumentType(int typeConst) {
        integraCollectionType = typeConst;
    }

    public static Node getDocumentRoot() {
        return BundleParser.documentRoot;
    }

    public static void setDocumentRoot(Node n) {
        BundleParser.documentRoot = n;
    }

    public Document getDocumentModel() {
        return dom;
    }

    public Element getDocumentElement() {
        return element;
    }

    private FlexTable buildCollabDataTable(VerticalPanel panel, String label) {
        HTML l = new HTML("<h4>" + label + "</h4>");
        panel.add(l);
        FlexTable orderTable = new FlexTable();
        orderTable.setStyleName(USER_TABLE_STYLE);
        orderTable.setBorderWidth(0);
        orderTable.getRowFormatter().setStyleName(0, USER_TABLE_LABEL_STYLE);
        orderTable.setText(0, 0, "Name");
        orderTable.setText(0, 1, "E-mail");
        orderTable.setText(0, 2, "Web site");
        panel.add(orderTable);
        return orderTable;
    }
    
    private FlexTable buildInstanceListTable(VerticalPanel panel, String label) {
        HTML l = new HTML("<h4>" + label + "</h4>");
        panel.add(l);
        FlexTable orderTable = new FlexTable();
        orderTable.setStyleName(USER_TABLE_STYLE);
        orderTable.setBorderWidth(0);
        orderTable.getRowFormatter().setStyleName(0, USER_TABLE_LABEL_STYLE);
        orderTable.setText(0, 0, "Id");
        orderTable.setText(0, 1, "Type");
        orderTable.setText(0, 2, "Name");
        orderTable.setText(0, 3, "Description");
        orderTable.setText(0, 4, "State");
        panel.add(orderTable);
        return orderTable;
    }

    private FlexTable buildNavList(String label) {
        FlexTable navTable = new FlexTable();
        navTable.setStyleName(USER_TABLE_STYLE);
        navTable.setBorderWidth(0);
        navTable.getRowFormatter().setStyleName(0, USER_TABLE_LABEL_STYLE);
        navTable.setText(0, 0, label);
        navTable.addTableListener(this);
        return navTable;
    }

    public String getName() {
        return BundleParser.getElementTextValue(element, "name");
    }

    public String getDescription() {
        String result = null;
        result = BundleParser.getElementTextValue(element, "description");
        if(result == null)
            result = IS_NULL;
        return result;
    }

    public NodeList getCollaborators() {
        NodeList nodes = null;
        nodes = BundleParser.getElementNodeList(element, "collaborator");
        return nodes;
    }

    public String[] getCollaborators(Node n) {
        Element e = (Element)n;
        NodeList nl = e.getElementsByTagName("collaborator");
        int items = nl.getLength();
        if(items < 1)
            return null;
        String result[] = new String[items];
        for(int i = 0; i < items; i++)
            result[i] = nl.item(i).getFirstChild().getNodeValue();
        return result;
    }

    private String getCollaborator(String id) {
        String result = "not found";
        NodeList nl = BundleParser.getElementNodeList(element, BundleParser.OBJECT);
        for(int i = 0; i < nl.getLength(); i++) {
            if(BundleParser.getNodeAttributeValue(nl.item(i), BundleParser.OBJECT, "id").equals(id)) {
                result = BundleParser.getElementTextValue(nl.item(i), "name");
                break;
            }
        }
        return result;
    }
    
    private String[] getViewContents(String viewId) {
        Node n = getViewById(viewId);
        if(n == null)
            return null;
        String refs = BundleParser.getElementTextValue(n, "documentsList");
        String result[] = refs.split(",");
        for(int i = 0; i < result.length; i++)
            result[i] = result[i].trim();
        return result;
    }
    
    public String getVersion() {
        String result = null;
        result = BundleParser.getElementTextValue(element, "version");
        if(result == null)
            result = IS_NULL;
        return result;
    }

    public String getTags() {
        return null;
    }

    public NodeList getObjects() {
        return BundleParser.getElementNodeList(element, "object");
    }

    private NodeList getNonAnonymousObjects() {
        NodeList nl = BundleParser.getElementNodeList(element, "object");
        return nl;
    }

    public String getObjectId(Node n) {
        return n.getAttributes().getNamedItem("id").getNodeValue();
    }

    public String getObjectType(Node n) {
        return n.getAttributes().getNamedItem("type").getNodeValue();
    }

    public Element getBundleElement() {
        return element;
    }

    public String[][] getViews() {
        NodeList views = element.getElementsByTagName(BundleParser.VIEW);
        if(views.getLength() < 1)
            return null;
        String result[][] = new String[2][views.getLength()];
        for(int i = 0; i < views.getLength(); i ++) {
            NodeList children = views.item(i).getChildNodes();
            for(int j = 0; j < children.getLength(); j++) {
                String s = children.item(j).getNodeName();
                if(s.equals(BundleParser.NAME)) {
                    result[0][i] = views.item(i).getAttributes().getNamedItem("id").getNodeValue();
                    result[1][i] = children.item(j).getFirstChild().getNodeValue();
                    break;
                }
            }
                
        }
        return result;
    }

    public static String getNodeAttributeValue(Node n, String elementTag, String attributeTag) {
        String result = "";
        NodeList list = n.getChildNodes();
        for(int i = 0; i < list.getLength(); i++ ) {
            if(list.item(i).getNodeName().equals(elementTag)) {
                result = list.item(i).getAttributes().getNamedItem(attributeTag).getNodeValue();
            }
        }
        return result;
    }

    public static String getElementTextValue(Element e, String elementTag) {
        NodeList nodes = e.getElementsByTagName(elementTag);
        if(nodes.getLength() < 1)
            return null;
        else return nodes.item(0).getFirstChild().getNodeValue();
    }

    public static String getElementTextValue(Node n, String elementTag) {
        String result = "";
        NodeList list = n.getChildNodes();
        for(int i = 0; i < list.getLength(); i++ ) {
            if(list.item(i).getNodeName().equals(elementTag)) {
                result = list.item(i).getFirstChild().getNodeValue();
            }
        }
        return result;
    }
    
    public static String getAttributeTextValue(Element e, String elementTag, String attributeTag) {
        String result = "";
        NodeList nl = BundleParser.getElementNodeList(e, elementTag);
        if(nl == null)
            return null;
        for(int i = 0; i<nl.getLength();i++) {
            result = nl.item(i).getAttributes().getNamedItem(attributeTag).getNodeValue();
        }
        return result;
    }

    public Node getObjectById(String id) {
        Node node = null;
        NodeList nl = BundleParser.getElementNodeList(element, BundleParser.OBJECT);
        if(nl == null)
            return null;
        for(int i = 0; i<nl.getLength();i++) {
            if(nl.item(i).getAttributes().getNamedItem(BundleParser.OBJECT_ID).getNodeValue().equals(id)) {
                node = nl.item(i);
                break;
            }
        }
        return node;
    }

    public String getObjectIdByName(String name) {
        String result = null;
        NodeList nl = BundleParser.getElementNodeList(element, "name");
        if(nl != null && nl.getLength() > 0) {
            for(int i = 0; i < nl.getLength(); i++) {
                if(nl.item(i).getFirstChild().getNodeValue().equals(name)) {
                    if(nl.item(i).getParentNode().getNodeName().equals("object")) {
                        result = getObjectId(nl.item(i).getParentNode());
                        break;
                    }
                }
            }
        }
        return result;
    }

    public Node getViewById(String id) {
        Node node = null;
        NodeList nl = BundleParser.getElementNodeList(element, BundleParser.VIEW);
        if(nl == null)
            return null;
        for(int i = 0; i<nl.getLength();i++) {
            if(nl.item(i).getAttributes().getNamedItem(BundleParser.OBJECT_ID).getNodeValue().equals(id)) {
                node = nl.item(i);
                break;
            }
        }
        return node;
    }

    public String getObjectNameById(String id) {
        Node n = getObjectById(id);
        return BundleParser.getElementTextValue(n, "name");
    }

    public String getViewNameById(String id) {
        Node n = getViewById(id);
        return BundleParser.getElementTextValue(n, "name");
    }

    public String getViewDescriptionById(String id) {
        Node n = getViewById(id);
        return BundleParser.getElementTextValue(n, "description");
    }
    
    public static NodeList getElementNodeList(Element e, String elementTag) {
        return e.getElementsByTagName(elementTag);
    }
    
    private void selectTableRow(int row) {
        if(currentRow != 0)
            contents.getRowFormatter().setStyleName(currentRow, "r1");
        currentRow = row;
        contents.getRowFormatter().setStyleName(row, "r2");
    }

    public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
        if(row == 0 || row == docHeader)
            return;
        else {
            selectTableRow(row);
            String objectId = ot.getObject(row);
            displayObjectAsync(objectId);
        }
    }

    public void onMouseDown(Widget sender, int x, int y) {

    }

    public void onMouseEnter(Widget sender) {
        
    }

    public void onMouseLeave(Widget sender) {}

    public void onMouseMove(Widget sender, int x, int y) {}

    public void onMouseUp(Widget sender, int x, int y) {}

    public void onSuggestionSelected(SuggestionEvent e) {

        String id = getObjectIdByName(e.getSelectedSuggestion().getReplacementString());
        displayObjectAsync(id);
    }

    public void displayObjectAsync(String objectId) {

        BundleParser.setCurrentObjectId(objectId);
        ObjectReferences ref = IntegraHelper.getLastObjectReference();
        if(ref != null)
            appendHistory(ref);
        Node n = getObjectById(objectId);
        IntegraBrowser.getBrowserSingleton().loadAsynchronIXDDefinition(getNodeAttributeValue(n, 
                                                                                              BundleParser.DEFINITION,
                                                                                              BundleParser.HREF));
    }
}