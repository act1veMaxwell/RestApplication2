package ApplicationRunner.AllControllers;

import ApplicationRunner.repository.Article;
import ApplicationRunner.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;


@Component
public class TextLengthUpdater {
    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    ArticleRepository articleRepository;

    @JmsListener(destination = "BackwardQueue")
    public void updateArticleTextLength(Article article)  {
        articleRepository.save(article);
        System.out.println(article.getArticleTitle() + " has made a circle. Txt length was updated: " + article.getTextLength());
    }
}
