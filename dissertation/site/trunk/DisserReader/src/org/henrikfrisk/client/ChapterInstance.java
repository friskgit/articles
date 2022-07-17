package org.henrikfrisk.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ChapterInstance implements IsSerializable {

    public int id = 0;
    public String contents = null;
    public String created = null;
    public String updated = null;
    public String tags = null;
    public String title = null;
    public String url = null;

    public ChapterInstance() {}

    public ChapterInstance(int id) {
        this.id = id;
    }

    public ChapterInstance(int id,
                           String contents,
                           String created,
                           String updated,
                           String tags,
                           String title,
                           String url) {
        this.id = id;
        this.contents = contents;
        this.created = created;
        this.updated = updated;
        this.tags = tags;
        this.title = title;
        this.url = url;
    }
}
