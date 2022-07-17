package org.integralive.client;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

public class ButtonListener implements ClickListener {

    private InstructionBlock ibck = null;
    private String url = null;
    private Node objectNode = null;

    public ButtonListener(InstructionBlock ib, BundleParser bparser) {
        ibck = ib;
        if(ibck.getURL() == null && ibck.getType() == ParentReferences.INSTRUCTION_TYPE) {
            Element e;
            e = (Element)bparser.getObjectById(ib.getObjectRef());
            NodeList nl = e.getElementsByTagName("value");
            url = nl.item(4).getFirstChild().getNodeValue();
            ib.setURL(url);
        }
    }

    public void onClick(Widget sender) {
        DisplayObject disp = IntegraBrowser.getBrowserSingleton().getDisplayObject();
        if(url.startsWith("img/")) {
            //            disp.displayImage(url);
        }
        if(url.startsWith("video/")) {
            PopupWindow videoWindow = new PopupWindow(ibck.getName(), url, PopupWindow.MOVIE_TYPE, false);
            int left = sender.getParent().getAbsoluteLeft() + 10;
            int top = sender.getParent().getAbsoluteTop() + 10;
            videoWindow.setPopupPosition(left, top);
            videoWindow.show();
         }
    }
}