package ApplicationRunner.AllControllers;

import ApplicationRunner.repository.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;


@Component
public class TextLengthChecker {
    @Autowired
    JmsTemplate jmsTemplate;

    @JmsListener(destination = "ForwardQueue")
    public void checkAndSendTextLength(Article article)   {
        article.setTextLength(article.getArticleText().length());
        jmsTemplate.convertAndSend("BackwardQueue", article);
        System.out.println("article processed and sent back " + article.getArticleTitle());
    }
}
