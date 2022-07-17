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

public class BundleEditor implements TableListener, MouseListener {
  
    private DockPanel editPanel = null;
    private BundleParser parser = null;
    private static Node rootNode = null;
    private Element element = null;
    private int currentObjectId = 0;
    private FlowPanel north = null;

    public BundleEditor(DockPanel editTab, BundleParser parser) {
        editPanel = editTab;
        this.parser = parser;
    }

    public void initEditor() {
        north = new FlowPanel();
        rootNode = BundleParser.getDocumentRoot();
        element = parser.getDocumentElement();
        getLastObjectId();
        HTML titleHTML = new HTML("<h2>" + parser.getName() + "</h2>");
        north.add(titleHTML);
        north.add(new HTML("<p>" + parser.getDescription() + "</p>"));
        editPanel.add(north, DockPanel.NORTH);
    }

    private int getLastObjectId() {
        NodeList nl = parser.getObjects();
        String id = "";
        int temp = 0;
        if(nl == null)
            return 0;
        for(int i = 0; i < nl.getLength(); i++) {
            id = parser.getObjectId(nl.item(i));
            temp = Integer.parseInt(id.replaceFirst("obj.", ""));
            if(temp > currentObjectId)
                currentObjectId = temp;
        }
        return currentObjectId;
    }

    public void onCellClicked(SourcesTableEvents sender, int row, int cell) {}

    public void onMouseDown(Widget sender, int x, int y) {}

    public void onMouseEnter(Widget sender) {}

    public void onMouseLeave(Widget sender) {}

    public void onMouseMove(Widget sender, int x, int y) {}

    public void onMouseUp(Widget sender, int x, int y) {}

}