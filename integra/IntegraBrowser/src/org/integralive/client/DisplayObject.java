package org.integralive.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LoadListener;
import java.util.HashMap;

public class DisplayObject extends Composite implements ClickListener, TableListener {

    private static final String USER_TABLE_LABEL_STYLE = "rh";
    private static final String USER_TABLE_STYLE = "table";
    private static final String SCORE_BLOCK = "ScoreBlock";
    private static final int MAX_LENGTH_STRING = 128;

    private String currentObjectClass = "";
    private String currentMediaUrl = "";
    private static String fullStringRep = "";
    private int currentMediaType = 0;
    private HashMap inheritedDefs = new HashMap(10);
    private VerticalPanel panel = null;
    private int inheritedClasses = 0;
    private ClassDefinition topDefinition = null;
    private BundleParser bundle = null;
    private FlexTable instanceTable = null;
    private Image image = null;
    private ObjectReferences refs = null;

    public DisplayObject(ClassDefinition def, BundleParser bundle) {
        this.bundle = bundle;
        topDefinition = def;
        inheritedClasses = 0;
        currentObjectClass = def.getName();
        refs = new ObjectReferences(currentObjectClass, getCurrentObjectName(), BundleParser.getCurrentObjectId());
        panel = new VerticalPanel();
        panel.setHorizontalAlignment(VerticalPanel.ALIGN_LEFT);
        panel.add(new HTML("<h3>" + currentObjectClass + ": " + getCurrentObjectName() + "</h3>"));
        panel.add(new HTML(def.getDescription()));
        panel.add(def.getParentLink());
        HTML docs = def.getDocumentation();
        docs.addStyleName("ntg-Spacer");
        panel.add(docs);
        String puri = def.getParentUri();
        if(!puri.equals("Null"))
            loadAsynchronInheritedIXDDefinition(puri);
        initWidget(panel);
    }
    
    public Widget displayObject() {
        return null;
    }
    
    public String getCurrentObjectName() {
        String id = BundleParser.getCurrentObjectId();
        Node n = bundle.getObjectById(id);
        return BundleParser.getElementTextValue(n, "name");
    }

    private String getCurrentObjectDescription() {
        String id = BundleParser.getCurrentObjectId();
        Node n = bundle.getObjectById(id);
        return BundleParser.getElementTextValue(n, "description");
    }
    
    private void loadParentClasses(ClassDefinition def) {
        buildInstanceListTable();
        ClassDefinition crntDef = null;
        String puri = def.getParentUri();
        int row = 0;
        if(puri.equals("Null")) {
            instanceTable.setText(row++, 0, "Attributes");
            for(int i = inheritedClasses; i > 0; i--) {
                crntDef = (ClassDefinition)inheritedDefs.get(new Integer(i));
                if(crntDef != null) {
                    for(int k = 0; k < crntDef.getAttributeCount(); k++) {
                        instanceTable.setText(row, 0, crntDef.getAttributeName(k));
                        //                        instanceTable.setText(row, 1, crntDef.getAttributeDescription(k));
                        instanceTable.getCellFormatter().addStyleName(i + 1, 0, "ntg-AttributeTable-name");
                        if(row % 2 == 0)
                            instanceTable.getRowFormatter().setStyleName(row, "r1");
                        else instanceTable.getRowFormatter().setStyleName(row, "r2");
                        row++;
                    }
                }
            }
            for(int j = 0; j < topDefinition.getAttributeCount(); j++) {
                instanceTable.setText(row, 0, topDefinition.getAttributeName(j));
                if(row % 2 == 0)
                    instanceTable.getRowFormatter().setStyleName(row, "r1");
                else instanceTable.getRowFormatter().setStyleName(row, "r2");
                row++;
            }
            instanceTable.getFlexCellFormatter().setColSpan(0, 0, 3);
            addDataState();
            panel.add(instanceTable);
            setStyleName(getElement(), "ntg-Panel-layout");
        }
        else loadAsynchronInheritedIXDDefinition(puri);
    }

    /**
     * Add the data values to the third column.
     *
     */
    public void addDataState() {
        String value = new String();
        int row = 0;
        HashMap hm = new HashMap();
        Node objectElement = bundle.getObjectById(BundleParser.getCurrentObjectId());
        NodeList valueList = BundleParser.getElementNodeList((Element)objectElement, "value");
        for(int i = 0; i < valueList.getLength(); i++) {
            value = valueList.item(i).getFirstChild().getNodeValue();
            /* Replace references with meaningful names */
            if(value.matches("obj.[0-9]+,*.*")) {
                String objects[];
                String name = new String();
                String ref = new String();
                objects = value.split(",");
                /* remove white space */
                for(int j = 0; j < objects.length; j++) {
                    objects[j] = objects[j].trim();
                    name = BundleParser.getElementTextValue(bundle.getObjectById(objects[j]), "name");
                    if(j == 0)
                        ref = "<a href='"+objects[j]+"'>"+name+"</a>";
                    else ref = ref + ", " + "<a href='"+objects[j]+"'>"+name+"</a>";
                    /* Store mediaBlock references */
                    if(instanceTable.getText(i+1, 0).equals(ParentReferences.MEDIA_CLIP) || 
                       instanceTable.getText(i+1, 0).equals(ParentReferences.INSTRUCTION_BLOCK))
                        hm.put(objects[j], name);
                }
                refs.put(instanceTable.getText(i+1, 0), objects);
                instanceTable.setHTML(i + 1, 2, ref);
            }
            else if(value.startsWith("http://")) {
                instanceTable.setHTML(i + 1, 2, "<a href=\"" + value + "\">" + value + "</a>");
            }
            else if(value.matches(".*@.*")) {
                instanceTable.setHTML(i + 1, 2, "<a href=\"mailto:" + value + "\">" + value + "</a>");
            }
            else if(value.startsWith("img/") || value.startsWith("video/")) {
                instanceTable.setText(i + 1, 2, value);
                instanceTable.getCellFormatter().addStyleName(i + 1, 2, "ntg-AttributeTable-clickable");
            }
            else if(value.length() > MAX_LENGTH_STRING) {
                DisplayObject.fullStringRep = value;
                String temp = value.substring(0, MAX_LENGTH_STRING);
                temp = temp.substring(0, temp.lastIndexOf(" ")) + "...";
                instanceTable.setHTML(i + 1, 2, temp);
            }
            else instanceTable.setText(i + 1, 2, value);
            instanceTable.getCellFormatter().addStyleName(i + 1, 2, "ntg-AttributeTable-value");
        }
        ParentReferences.addReferencesMap(getCurrentObjectName(), hm);
        IntegraHelper.addObjectReference(refs);
    }
    
    /**
     * The inlined image was clicked on.
     */
    public void onClick(Widget sender) {
        if(sender == image) {
            PopupWindow imgWindow = new PopupWindow(getCurrentObjectName(), currentMediaUrl, currentMediaType, true);
            int left = sender.getAbsoluteLeft() + 10;
            int top = sender.getAbsoluteTop() + 10;
            imgWindow.setPopupPosition(left, top);
            imgWindow.show();
        }
    }

    public void setCaption(String caption) {
    }

    public String getCaption() {
        return "";
    }

    public static String getFullStringRepresentation() {
        return fullStringRep;
    }

    public void loadAsynchronInheritedIXDDefinition(String filename) {
        HTTPRequest.asyncGet(filename, new ResponseTextHandler() {
                public void onCompletion(String theIXD) {
                    inheritedClasses = inheritedClasses + 1;
                    ClassDefinition parentDef = new ClassDefinition(theIXD, inheritedClasses);
                    inheritedDefs.put(new Integer(inheritedClasses), parentDef);
                    loadParentClasses(parentDef);
                }
            });
    }

    private FlexTable buildInstanceListTable() {
        instanceTable = new FlexTable();
        instanceTable.setStyleName(USER_TABLE_STYLE);
        instanceTable.setBorderWidth(0);
        instanceTable.getRowFormatter().setStyleName(0, USER_TABLE_LABEL_STYLE);
        instanceTable.addTableListener(this);
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        panel.add(instanceTable);
        return instanceTable;
    }

    /**
     * A cell in the table was clicked on.
     */
    public void onCellClicked(SourcesTableEvents sender, int row, int cell) {
        String contents = instanceTable.getText(row, cell);
        if(contents.startsWith("img/")) {
            currentMediaUrl = contents;
            currentMediaType = PopupWindow.IMAGE_TYPE;
            displayImage(contents);
        }
        if(contents.startsWith("video/")) {
            currentMediaUrl = contents;
            currentMediaType = PopupWindow.MOVIE_TYPE;
            displayVideo(contents);
        }
        if(instanceTable.getText(row, 0).equals("textRepresentation")) {
            String title = getCurrentObjectName() + ": " + getCurrentObjectDescription();
            PopupWindow textPopup = new PopupWindow(title, null, PopupWindow.TEXT_TYPE, true);
            int left = panel.getAbsoluteLeft() + 10;
            int top = panel.getAbsoluteTop() + 10;
            textPopup.setPopupPosition(left, top);
            textPopup.show();
        }
        if(row == 0) {
            IntegraHelper.getParentObjectRefs();
        }
    }

    public void displayImage(String imagePath) {
        currentMediaUrl = imagePath;
        image = new Image();
        image.addLoadListener(new LoadListener() {
                public void onError(Widget sender) {
                    panel.add(new HTML("<b>An error occurred while loading the image.</b>"));
                }
                
                public void onLoad(Widget sender) {
                }
            });
        image.setUrl(imagePath);
        image.setWidth("580px");
        image.addClickListener(this);
        panel.add(image);
    }

    public void displayVideo(String path) {
        PopupWindow imgWindow = new PopupWindow(getCurrentObjectName(), currentMediaUrl, currentMediaType, true);
        int left = panel.getAbsoluteLeft() + 10;
        int top = panel.getAbsoluteTop() + 10;
        imgWindow.setPopupPosition(left, top);
        imgWindow.show();
    }
}