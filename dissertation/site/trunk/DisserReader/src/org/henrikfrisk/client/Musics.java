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

public class Musics extends Composite {

    /**
     * An image bundle for this widget and an example of the use of @gwt.resource.
     */
//     public interface Images extends ImageBundle {
//         /**
//          * @gwt.resource default_photo.jpg
//          */
//         AbstractImagePrototype musicLogo();
//     }

    /**
     * Simple data structure representing a contact.
     */

    private MusicInstance[] mus = new MusicInstance[] {
        new MusicInstance(0, 0, "<p>This is chapter 1</p>", null, null, null, "Test 1", "test1.html"),
        new MusicInstance(1, 0, "<p>This is chpater 2</p>", null, null, null, "Test 2", "test2.html"),
        new MusicInstance(2, 0, "<p>This is chapter 3</p>", null, null, null, "Test 3", "test3.html")};

        private VerticalPanel panel = new VerticalPanel();
    //        private final Images images;

    public Musics() {
        SimplePanel outer = new SimplePanel();
        outer.setWidget(panel);
        
        //        this.images = (Images)images;
        // Add all the chapter to the list.
        for (int i = 0; i < mus.length; ++i) {
            addChapter(mus[i]);
        }
        
        initWidget(outer);
        setStyleName("nav-Sections");
    }
    
    private void addChapter(final MusicInstance piece) {
        final HTML link = new HTML("<a href='javascript:;'>" + piece.title + "</a>");
        panel.add(link);
        
        // Add a click listener that displays a ContactPopup when it is clicked.
        link.addClickListener(new ClickListener() {
                public void onClick(Widget sender) {
                }
            });
    }
}