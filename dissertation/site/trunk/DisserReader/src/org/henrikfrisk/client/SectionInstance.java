package org.henrikfrisk.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SectionInstance extends ChapterInstance implements IsSerializable {
    
    public static final int maxSectionDepth = 5;
    public int chapterId = 0;
    public String titleAsHTML = "";
    public int sectionIndex = 0;
    public transient int[] sectionIndexes = new int[maxSectionDepth];    
    /**
     *  Empty constructor required by interface com.google.gwt.user.client.rpc.IsSerializable 
     */
    public SectionInstance() {
        super();
    }

    public SectionInstance(int id) {
        super(id);
    }

    public SectionInstance(int id, int chapterId) {
        super(id);
        this.chapterId = id;
    }

    public SectionInstance(int id,
                           int chapterId,
                           String contents,
                           String created,
                           String updated,
                           String tags,
                           String title,
                           String titleAsHTML,
                           String url,
                           int sectionIndex) {
        super(id);
        sectionIndexes = new int[maxSectionDepth];
        this.chapterId = chapterId;
        this.contents = contents;
        this.created = created;
        this.updated = updated;
        this.tags = tags;
        this.title = title;
        this.titleAsHTML = titleAsHTML;
        this.url = url;
        this.sectionIndex = sectionIndex;
        //        setSectionIndexes(sectionIndex);
    }

    public void setSectionIndexes(int indexAsInt) {
        if(indexAsInt > 0) {
            String indexAsString = String.valueOf(indexAsInt);
            if(indexAsString.length() >= maxSectionDepth) {
                sectionIndexes[0] = Integer.parseInt(indexAsString.substring(0, 1));
                sectionIndexes[1] = Integer.parseInt(indexAsString.substring(1, 2));
                sectionIndexes[2] = Integer.parseInt(indexAsString.substring(2, 3));
                sectionIndexes[3] = Integer.parseInt(indexAsString.substring(3, 4));
                sectionIndexes[4] = Integer.parseInt(indexAsString.substring(4, 5));
            }
        }
    }
}


