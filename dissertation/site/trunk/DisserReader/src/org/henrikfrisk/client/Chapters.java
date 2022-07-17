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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Chapters extends Composite {

    /**
     * An image bundle for this widget and an example of the use of @gwt.resource.
     */
    public interface Images extends ImageBundle {
        /**
         * @gwt.resource default_photo.jpg
         */
        AbstractImagePrototype chapterLogo();
    }

    /**
     * Simple data structure representing a contact.
     */

        private VerticalPanel panel = new VerticalPanel();
    //        private final Images images;

   
    public Chapters() {
        SimplePanel outer = new SimplePanel();
        outer.setWidget(panel);
        callForChapters();
        initWidget(outer);
        setStyleName("nav-Sections");
    }
    
    private void addChapter(final ChapterInstance chapter) {
        final HTML link = new HTML(chapter.title);
        panel.add(link);
        
        link.addClickListener(new ClickListener() {
                public void onClick(Widget sender) {
                    DisserReader.getDisser().setTitleString("<h2>"+chapter.title+"</h2>");
                    callForAbstract(chapter.id);
                }
            });
    }

    public void callForChapters() {
        ArticleServiceAsync articleService = (ArticleServiceAsync) GWT.create(ArticleService.class);
        ServiceDefTarget target = (ServiceDefTarget)articleService;
        String relativeUrl = GWT.getModuleBaseURL() + "articles";
        target.setServiceEntryPoint(relativeUrl);
        articleService.getChapterInstances(new AsyncCallback() {
                public void onFailure(Throwable caught) {
                    GWT.log("Error ", caught);
                    caught.printStackTrace();
                }
                
                public void onSuccess(Object result) {
                    ChapterInstance[] chapters = (ChapterInstance[])result;
                    for(int i=0;i<chapters.length;i++)
                        addChapter(chapters[i]);
                }
            });
    }

    public void callForAbstract(int id) {
        ArticleServiceAsync articleService = (ArticleServiceAsync) GWT.create(ArticleService.class);
        ServiceDefTarget target = (ServiceDefTarget)articleService;
        String relativeUrl = GWT.getModuleBaseURL() + "articles";
        target.setServiceEntryPoint(relativeUrl);
        articleService.getChapterAbstract(id, new AsyncCallback() {
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