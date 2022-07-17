package org.integralive.client;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Button;

public class PopupWindow extends DialogBox implements ClickListener {
 
    public static final int TEXT_TYPE = 1;
    public static final int IMAGE_TYPE = 2;
    public static final int MOVIE_TYPE = 3;
    public static final int ISNOT_INSTRUCTION = 1;
    private Button closeButton = null;
    private Button instrButtons[] = null;
    private BundleParser bundle = null;
    private boolean isInstr = true;

    public PopupWindow(String title, String url, int type, boolean instr) {
        if(!instr)
            isInstr = false;
        bundle = IntegraBrowser.getBrowserSingleton().getBundleParser();
        closeButton = new Button("Close", this);
        setText(title);
        DockPanel dock = new DockPanel();
        HorizontalPanel southPanel = new HorizontalPanel();
        HorizontalPanel parentPanel = new HorizontalPanel();
        String instrRefs[] = null;
        if(isInstr) {
            instrRefs = 
                IntegraHelper.getReferencesByAttribute(IntegraHelper.getPreviousObject(), 
                                                       "instructionBlock");
        }
        if(instrRefs != null) {
            instrButtons = new Button[instrRefs.length];
            for(int i = 0; i < instrRefs.length; i++) {
                InstructionBlock ib = new InstructionBlock(bundle.getObjectNameById(instrRefs[i]), null, instrRefs[i]);
                instrButtons[i] = new Button(ib.getName(), 
                                             new ButtonListener(ib,
                                                                bundle));
                southPanel.add(instrButtons[i]);
            }
        }
        dock.setSpacing(4);
        parentPanel.add(southPanel);
        parentPanel.add(closeButton);
        switch(type) {
        case PopupWindow.TEXT_TYPE:
            HTML text = new HTML(DisplayObject.getFullStringRepresentation());
            text.setWidth("400px");
            dock.add(text, DockPanel.CENTER);
            break;
        case PopupWindow.IMAGE_TYPE:
            Image img = new Image();
            img.setUrl(url);
            img.setWidth("100%");
            dock.add(img, DockPanel.CENTER);
            break;
        case PopupWindow.MOVIE_TYPE:
            HTML movie = new HTML("<div id=\"container\"><a href=\"http://www.macromedia.com/go/getflashplayer\">Get the Flash Player</a> to see this player.</div>" +
                                  "<script type=\"text/javascript\">" +
                                  "var s1 = new SWFObject(\"mediaplayer.swf\",\"mediaplayer\",\"320\",\"240\",\"8\");" +
                                  "s1.addParam(\"allowfullscreen\",\"true\");" +
                                  "s1.addVariable(\"width\",\"320\");" +
                                  "s1.addVariable(\"height\",\"240\");" +
                                  "s1.addVariable(\"file\",\""+ url +"\");" +
                                  "s1.write(\"container\"); </script>");
            dock.add(movie, DockPanel.CENTER);
            break;
        }
        dock.add(parentPanel, DockPanel.SOUTH);
        parentPanel.setCellHorizontalAlignment(closeButton, DockPanel.ALIGN_RIGHT);
        dock.setWidth("100%");
        setWidget(dock);
    }
        
    public void onClick(Widget sender) {
        if(sender == closeButton)
            hide();
    }
}
