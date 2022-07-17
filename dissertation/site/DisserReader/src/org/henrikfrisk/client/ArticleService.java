package org.henrikfrisk.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface ArticleService extends RemoteService {
	public String getArticleSection();
}