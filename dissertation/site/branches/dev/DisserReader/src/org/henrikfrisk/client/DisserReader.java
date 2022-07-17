package org.henrikfrisk.client;

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
import com.google.gwt.user.client.rpc.ServiceDefTarget;
 
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DisserReader implements EntryPoint {

    private DockPanel panel;
    private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
    private SuggestBox searchField = new SuggestBox(oracle);
    
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        initDockPanel();
        panel.add(buildTitlePanel(), DockPanel.SOUTH);
        panel.add(buildNavigationPanel(), DockPanel.WEST);
        panel.add(buildSearchPanel(), DockPanel.EAST);
        RootPanel.get("header").add(panel);

        final Button button = new Button("Click me");
        final Label label = new Label();

        button.addClickListener(new ClickListener() {
                public void onClick(Widget sender) {
                    if (label.getText().equals("")) {
                        label.setText("Made the call");
                        callForArticle();
                    }
                    else
                        label.setText("");
                }
            });

        // Assume that the host HTML has elements defined whose
        // IDs are "slot1", "slot2".  In a real app, you probably would not want
        // to hard-code IDs.  Instead, you could, for example, search for all 
        // elements with a particular CSS class and replace them with widgets.
        //
        RootPanel.get("slot1").add(button);
        RootPanel.get("slot2").add(label);
    }

    private void initDockPanel() {
        panel = new DockPanel();
        panel.setBorderWidth(0);
        panel.setSpacing(0);
    } 

    /**
     * This should go menu.WEST
     */
    public HorizontalPanel buildNavigationPanel() {
        HorizontalPanel navigation = new HorizontalPanel();
        navigation.setSpacing(3);
        Button backButton = new Button("Previous");
        Button tocButton = new Button("TOC");
        Button nextButton = new Button("Next");
        navigation.add(backButton);
        navigation.add(tocButton);
        navigation.add(nextButton);
        return navigation;
    }
    /**
     * This should go menu.SOUTH
     */
    private VerticalPanel buildTitlePanel() {
        VerticalPanel titlePanel = new VerticalPanel();
        titlePanel.setSpacing(3);
        HTML label = new HTML("<h2>The title of the paper.</h2>");
        titlePanel.add(label);
        return titlePanel;
    }
    /**
     * This should go menu.EAST
     */
    public HorizontalPanel buildSearchPanel() {
        HorizontalPanel searchPanel = new HorizontalPanel();
        searchPanel.setSpacing(3);
        Button searchButton = new Button("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        return searchPanel;
    }
    /**
     * 
     */
    public void callForArticle() {
        ArticleServiceAsync articleService = (ArticleServiceAsync) GWT.create(ArticleService.class);
        ServiceDefTarget target = (ServiceDefTarget) articleService;
        String relativeUrl = GWT.getModuleBaseURL() + "articles";
        target.setServiceEntryPoint(relativeUrl);
        articleService.getArticleSection(new ArticleCallback());
    }



    
}
