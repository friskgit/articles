package org.henrikfrisk.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.EventPreview;

 
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DisserReader implements EntryPoint, EventPreview {

    private static DisserReader disser;

    private DockPanel topPanel;
    private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
    private SuggestBox searchField = new SuggestBox(oracle);
    private NavigationPanel navPanel = new NavigationPanel(images);
    private HTML titleLabel = null;
    private VerticalPanel titlePanel = null;
    /**
     * Instantiate an application-level image bundle. This object will provide
     * programmatic access to all the images needed by widgets.
     */
    private static final Images images = (Images) GWT.create(Images.class);
    
    /**
     * An aggragate image bundle that pulls together all the images for this
     * application into a single bundle.
     */
    public interface Images extends NavigationPanel.Images {
    }


    public static DisserReader getDisser() {
        return disser;
    }

    public NavigationPanel getNavigation() {
        return navPanel;
    }
    /**
     * This is the entry point method.     */
    public void onModuleLoad() {
        disser = this;
        initDockPanel();
        topPanel.add(buildTitlePanel(), DockPanel.SOUTH);
        topPanel.add(buildNavigationPanel(), DockPanel.WEST);
        topPanel.add(buildSearchPanel(), DockPanel.EAST);
        RootPanel.get("header").add(topPanel);
        RootPanel.get("navigation").add(navPanel);
        // Used to check what user clicked on.
        DOM.addEventPreview(this);
    }
    
    /**
     * Function to intercept browser calls to fetch new HTML
     * pages. Instead the reference, if it's a local ref or an anchor
     * should be passsed to the the RPC handling mechanism in order to
     * fetch the requested document from the database by file name
     * lookup.
     */
     /* @Override */
    public boolean onEventPreview(Event event) {
        if (DOM.eventGetType(event) == Event.ONCLICK) {
            Element target = DOM.eventGetTarget(event);
            if ("a".equalsIgnoreCase(getTagName(target))) {
                String href = DOM.getElementAttribute(target, "href");
                if(!href.startsWith("http://")) {
                    //    getNavigation().getSectionsStack().callForSectionContent(href);
                    String[] s = href.split("#");
                    getNavigation().getSectionsStack().callForSectionContent(s[0]);
                    // setTitleString(s[0]);
                }
                return false;
            }
            if("sup".equalsIgnoreCase(getTagName(target))) {
                setTitleString("Is sup");
                return false;
            }
        }
        return true;
    }
    
    native String getTagName(Element element)
    /*-{
      return element.tagName;
      }-*/;
    
    public void setTitleString(String title) {
        if(this.titleLabel.getText().equals(title))
            return;
        else titleLabel.setHTML(title);
    }

    private void initDockPanel() {
        topPanel = new DockPanel();
        topPanel.setBorderWidth(0);
        topPanel.setSpacing(0);
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
        titleLabel = new HTML("<h2>Music, Computers and Interaction</h2>");
        titlePanel.add(titleLabel);
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
