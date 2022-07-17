package org.henrikfrisk.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MusicInstance extends ChapterInstance implements IsSerializable {

    public int chapterId = 0;
    
    /**
     *  Empty constructor required by interface com.google.gwt.user.client.rpc.IsSerializable 
     */
    public MusicInstance() {
        super();
    }

    public MusicInstance(int id) {
        super(id);
    }

    public MusicInstance(int id, int chapterId) {
        super(id);
        this.chapterId = id;
    }

    public MusicInstance(int id,
                         int chapterId,
                         String contents,
                         String created,
                         String updated,
                         String tags,
                         String title,
                         String url) {
        super(id);
        this.chapterId = chapterId;
        this.contents = contents;
        this.created = created;
        this.updated = updated;
        this.tags = tags;
        this.title = title;
        this.url = url;
    }
}

