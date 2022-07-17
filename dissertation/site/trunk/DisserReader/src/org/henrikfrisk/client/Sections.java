package org.henrikfrisk.client;

import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ImageBundle;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.TreeListener;
import com.google.gwt.user.client.ui.ChangeListener;

import java.util.Vector;

public class Sections extends Composite implements ChangeListener, TreeListener {

    //    private static Vector sec = new Vector();

    /**
     * An image bundle for this widget and an example of the use of @gwt.resource.
     */
    public interface Images extends ImageBundle {
        /**
         * @gwt.resource default_photo.jpg
         */
        AbstractImagePrototype sectionLogo();
        //        AbstractImagePrototype musicLogo();
    }

    /**
     * A simple popup that displays a contact's information.
     */
    private class SectionPopup extends PopupPanel {

        public SectionPopup(SectionInstance section) {
            // The popup's constructor's argument is a boolean specifying that it
            // auto-close itself when the user clicks outside of it.
            super(true);

            VerticalPanel inner = new VerticalPanel();
            Label titleLabel = new Label(section.title);
            inner.add(titleLabel);

            HorizontalPanel hp = new HorizontalPanel();
            hp.setSpacing(4);
            hp.add(images.sectionLogo().createImage());
            hp.add(inner);

            add(hp);
            setStyleName("nav-SectionPopup");
            titleLabel.setStyleName("nav-SectionPopupName");
        }
    }

    private static class PendingItem extends TreeItem {
        public PendingItem() {
            super("Please wait...");
        }
    }

    private static class SectionPrototype {
        
        public Vector children = new Vector();
        public TreeItem item;
        public SectionInstance instance;

        public SectionPrototype() {}

        public SectionPrototype(SectionInstance inst) {
            this.instance = inst;
        }

        public SectionPrototype(SectionInstance inst, Vector children) {
            this(inst);
            this.children = children;
        }
    }

    private static VerticalPanel panel = new VerticalPanel();
    private final Images images;
    public static Tree tree;
    private Vector sectionProtos;

    public Sections(Images images) {
        SimplePanel outer = new SimplePanel();
        outer.setWidget(panel);

        this.images = images;
        callForSections();

        tree = new Tree();
        tree.addTreeListener(this);
        tree.setWidth("20em");
        panel.add(tree);
        initWidget(outer);
        setStyleName("nav-Sections");
    }

    private void createItem(SectionPrototype s) {
        s.item = new TreeItem(s.instance.title);
        s.item.setUserObject(s);
        if(s.children.size() > 0) {
            s.item.addItem(new PendingItem());
        }
    }
    
    public void addSection(final SectionInstance section) {
        final HTML link = new HTML(section.title);
        panel.add(link);

        // Add a click listener that displays a ContactPopup when it is clicked.
        link.addClickListener(new ClickListener() {
                public void onClick(Widget sender) {
                    callForSectionContent(section.id);                   
                }
            });        
    }

    public void addSectionsAsTree(SectionInstance[] instances) {
        int sec = 1;
        int depth = SectionInstance.maxSectionDepth;
        sectionProtos = new Vector();
        // Init all sectionIndexes
        for(int i = 0; i < instances.length; i++)
            instances[i].setSectionIndexes(instances[i].sectionIndex);

        // Add sections
        for(int i = instances.length - 1; i > 0 ; i--) {
            sortSections(instances[i], instances[i-1], sec, sectionProtos);
            if(i == 1)
                sortSections(instances[i-1], instances[i], sec, sectionProtos);
        }
        // Add sub sections to the children nodes
        for(int i = 0; i < sectionProtos.size(); i++) {
            SectionPrototype s = (SectionPrototype)sectionProtos.get(i);
            sortSubSections(s.instance.sectionIndexes[1], 2, instances, s.children);
            for(int j = 0; j < s.children.size(); j++) {
                SectionPrototype subs = (SectionPrototype)s.children.get(j);
                sortSubSections(subs.instance.sectionIndexes[2], 3, instances, subs.children);
            }
        }
        // Add sections to the Tree.
        for(int i = sectionProtos.size()-1; i >= 0; i--) {
            SectionPrototype s = (SectionPrototype)sectionProtos.get(i);
            createItem(s);
            tree.addItem(s.item);
            //            RootPanel.get("slot1").add(new HTML(s.instance.title));
//             for(int j = 0; j < s.children.size(); j++) {
//                 SectionPrototype subs = (SectionPrototype)s.children.get(j);
//                 RootPanel.get("slot1").add(new HTML("--"+subs.instance.title));
//                 for(int k = 0; k < subs.children.size(); k++) {
//                     SectionPrototype subsubs = (SectionPrototype)subs.children.get(k);
//                     RootPanel.get("slot1").add(new HTML("----"+subsubs.instance.title));
//                 }
//             }
        }
    }
    
    private void sortSubSections(int currentSection,
                                 int currentLevel,
                                 SectionInstance[] instances,
                                 Vector v) {
        SectionPrototype p;
        for(int i = 0; i<instances.length; i++) {
            if(instances[i].sectionIndexes[currentLevel] > 0 && 
               instances[i].sectionIndexes[currentLevel - 1] == currentSection &&
               instances[i].sectionIndexes[currentLevel + 1] == 0) {
                 p = new SectionPrototype(instances[i]);
                 v.add(p);
            }
        }
    }

    private void sortSections(SectionInstance current, 
                              SectionInstance next, 
                              int level,
                              Vector v) {
        SectionPrototype p = new SectionPrototype();
        if(current.sectionIndexes[level] != next.sectionIndexes[level]) {
            p.instance = current;
            v.add(p);
        }
    }

    public void onChange(Widget sender) {
    }
    
    public void onTreeItemSelected(TreeItem item) {
        SectionPrototype p = (SectionPrototype)item.getUserObject();
        callForSectionContent(p.instance.id);       
    }

    public void onTreeItemStateChanged(TreeItem item) {
        TreeItem child = item.getChild(0);
        if (child instanceof PendingItem) {
            item.removeItem(child);
            
            SectionPrototype proto = (SectionPrototype) item.getUserObject();
            for (int i = 0; i < proto.children.size(); ++i) {
                createItem((SectionPrototype)proto.children.get(i));
                item.addItem(((SectionPrototype)proto.children.get(i)).item);
            }
        }
        this.setPixelSize(tree.getOffsetWidth(), tree.getOffsetHeight());
    }

    public void callForSections() {
        ArticleServiceAsync articleService = (ArticleServiceAsync) GWT.create(ArticleService.class);
        ServiceDefTarget target = (ServiceDefTarget) articleService;
        String relativeUrl = GWT.getModuleBaseURL() + "articles";
        target.setServiceEntryPoint(relativeUrl);
        articleService.getSectionInstances(0,  new AsyncCallback() {
                public void onFailure(Throwable caught) {
                    GWT.log("Error ", caught);
                    caught.printStackTrace();
                    RootPanel.get("slot1").add(new HTML(caught.toString()));
                }
                
                public void onSuccess(Object result) {
                    Sections s = DisserReader.getDisser().getNavigation().getSectionsStack();
                    SectionInstance[] instances = (SectionInstance[]) result;
                    s.addSectionsAsTree(instances);
                }
            });
    }

    public void callForSectionContent(int id) {
        ArticleServiceAsync articleService = (ArticleServiceAsync) GWT.create(ArticleService.class);
        ServiceDefTarget target = (ServiceDefTarget) articleService;
        String relativeUrl = GWT.getModuleBaseURL() + "articles";
        target.setServiceEntryPoint(relativeUrl);
        articleService.getSectionContent(id, new AsyncCallback() {
                public void onFailure(Throwable caught) {
                    GWT.log("Error ", caught);
                    caught.printStackTrace();
                    RootPanel.get("text_frame").add(new HTML("error"));
                }
                
                public void onSuccess(Object result) {
                    RootPanel.get("text_frame").clear();
                    RootPanel.get("text_frame").add(new HTML((String)result));
                }
            });
    }

    public void callForSectionContent(String fileName) {
        ArticleServiceAsync articleService = (ArticleServiceAsync) GWT.create(ArticleService.class);
        ServiceDefTarget target = (ServiceDefTarget) articleService;
        String relativeUrl = GWT.getModuleBaseURL() + "articles";
        target.setServiceEntryPoint(relativeUrl);
        articleService.getSectionContent(fileName, new AsyncCallback() {
                public void onFailure(Throwable caught) {
                    GWT.log("Error ", caught);
                    caught.printStackTrace();
                    RootPanel.get("text_frame").add(new HTML("error"));
                }
                
                public void onSuccess(Object result) {
                    RootPanel.get("text_frame").clear();
                    RootPanel.get("text_frame").add(new HTML((String)result));
                }
            });
    }
}