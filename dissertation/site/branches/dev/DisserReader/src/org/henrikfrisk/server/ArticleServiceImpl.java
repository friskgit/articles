package org.henrikfrisk.server;

import org.henrikfrisk.client.ArticleService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ArticleServiceImpl extends RemoteServiceServlet 
    implements ArticleService {

	public String getArticleSection() {

        String result = "Here you are sucker";

        return result;
    }
}
