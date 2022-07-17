package org.henrikfrisk.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ArticleServiceAsync {

	public void getArticleSection(AsyncCallback callback);

    public void getChapterInstances(AsyncCallback callback);

    public void getSectionInstances(int chapterId, AsyncCallback callback);

    public void getSectionTitle(int chapterId, AsyncCallback callback);

    public void getSectionContent(int id, AsyncCallback callback);

    public void getSectionContent(String fileName, AsyncCallback callback);

    public void getChapterAbstract(int id, AsyncCallback callback);
}