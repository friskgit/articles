package org.henrikfrisk.client;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class NavigationPanel extends Composite {

  public interface Images extends Sections.Images {

    AbstractImagePrototype sectionLogo();

    AbstractImagePrototype chapterLogo();

    AbstractImagePrototype musicLogo();

  }
  
    private Sections sectionsStack = null;

    private int nextStackIndex = 0;

    private StackPanel stack = new StackPanel() {
            public void onBrowserEvent(Event e) {
                int oldIndex = getSelectedIndex();
                //                RootPanel.get("slot1").add(new HTML(e.toString()));
                super.onBrowserEvent(e);
                int newIndex = getSelectedIndex();
 //                if(oldIndex != newIndex) {
//                     updateSelectedStyles(oldIndex, newIndex);
//                 }
            }
        };

    public NavigationPanel(Images images) {
        sectionsStack = new Sections(images);
        add(images, new Chapters(), images.chapterLogo(), "Chapters");
        add(images, sectionsStack, images.sectionLogo(), "Sections");
        add(images, new Musics(), images.musicLogo(), "Music");
        initWidget(stack);
    }

    public Sections getSectionsStack() {
        return sectionsStack;
    }

    protected void onLoad() {
        stack.showStack(0);
//         updateSelectedStyles(-1, 0);
    }

    private void add(Images images, Widget widget, AbstractImagePrototype imageProto, String caption) {
        widget.addStyleName("nav-StackContent");
        stack.add(widget, createHeaderHTML(images, imageProto, caption), true);
    }
            
    private String computeHeaderId(int index) {
        return "header-" + this.hashCode() + "-" + index;
    }

    private String createHeaderHTML(Images images, AbstractImagePrototype imageProto, String caption) {
        boolean isTop = (nextStackIndex == 0);
        String cssId = computeHeaderId(nextStackIndex);
        nextStackIndex++;
        String captionHTML = "<table class='caption' cellpadding='0' cellspacing='0'>"
            + "<tr><td class='lcaption'>" + imageProto.getHTML()
            + "</td><td class='rcaption'><b style='white-space:nowrap'>" + caption 
            + "</b></td></tr></table>";
        return captionHTML;        
    }

  /**
   * Example of using the DOM class to do CSS class name tricks that have
   * become common to AJAX apps. In this case we add CSS class name for the
   * stack panel item that is below the selected item.
   */
  private void updateSelectedStyles(int oldIndex, int newIndex) {
    oldIndex++;
    if (oldIndex > 0 && oldIndex < stack.getWidgetCount()) {
      Element elem = DOM.getElementById(computeHeaderId(oldIndex));
      DOM.setElementProperty(elem, "className", "");
    }
    
    newIndex++;
    if (newIndex > 0 && newIndex < stack.getWidgetCount()) {
      Element elem = DOM.getElementById(computeHeaderId(newIndex));
      DOM.setElementProperty(elem, "className", "is-beneath-selected");
    }
  }
}