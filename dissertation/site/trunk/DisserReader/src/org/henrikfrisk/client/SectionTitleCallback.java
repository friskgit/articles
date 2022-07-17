package org.henrikfrisk.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.core.client.GWT;

public class SectionTitleCallback implements AsyncCallback {

	public void onFailure(Throwable caught) {
 	    GWT.log("Error ", caught);
 		caught.printStackTrace();
        RootPanel.get("slot1").add(new HTML(caught.toString()));
	}

	public void onSuccess(Object result) {
        Sections s = DisserReader.getDisser().getNavigation().getSectionsStack();
        SectionInstance[] instances = (SectionInstance[]) result;
//         for(int i=0;i<instances.length;i++) {
//             s.addSection(instances[i]);
//         }
        RootPanel.get("slot1").add(new HTML("HERE"));
        s.addSectionsAsTree(instances);
    }
}