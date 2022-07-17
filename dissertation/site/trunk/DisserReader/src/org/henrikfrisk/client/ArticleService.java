package org.henrikfrisk.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ArticleService extends RemoteService {

	public String getArticleSection();

    public ChapterInstance[] getChapterInstances();

    public SectionInstance[] getSectionInstances(int chapterId);

    public String getSectionTitle(int chapterId);

    public String getSectionContent(int id);

    public String getSectionContent(String fileName);

    public String getChapterAbstract(int id);
}