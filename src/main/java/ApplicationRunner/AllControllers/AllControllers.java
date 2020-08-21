package ApplicationRunner.AllControllers;

import ApplicationRunner.Exceptions.ArticleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RestController;
import ApplicationRunner.repository.Article;
import ApplicationRunner.repository.ArticleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class AllControllers {

    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    JmsTemplate jmsTemplate;

    @GetMapping("check/{id}")
    public String sendToCheckThroughActiveMQ(@PathVariable long id) {
        Optional<Article> articleToSend =articleRepository.findById(id);
        jmsTemplate.convertAndSend("ForwardQueue", articleToSend.get());
        return "article "+ articleToSend.get().getArticleTitle() + "was sent";
    }

    @GetMapping("/home") // Возвращает список всех Article
    public Iterable<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    @GetMapping("/list")
    public String getList() {
        articleRepository.save(new Article("Article1", "text1"));
        articleRepository.save(new Article("Article2", "text2"));
        return new String("list");
    }

    @GetMapping("/home/{id}") // Возвращает 1 Article по id
    public Article getOneArticle(@PathVariable long id) {
        return articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(id));
    }

    @PostMapping("/home") // сохраняет новый Article и возвращает его
    Article CreateNewArticle(@RequestBody Article newArticle) {
        return articleRepository.save(newArticle);
    }


    @PutMapping("/home/{id}") // Заменяет старый Article новым newArticle по id, если такого id не существует - генерирует новый Article и возвращает его.
    Article replaceArticleIfExist(@RequestBody Article newArticle, @PathVariable long id) {
        if (articleRepository.existsById(id)) {
            Article replacedArticle = articleRepository.findById(id).orElse(new Article());
            replacedArticle.setArticleTitle(newArticle.getArticleTitle());
            replacedArticle.setArticleText(newArticle.getArticleText());
            articleRepository.save(replacedArticle);
            return replacedArticle;
        } else
            return articleRepository.save(newArticle);
    }

    @DeleteMapping("/home/{id}") // Удаляет Article по Id, возвращает удаленный Article
    public Article remove(@PathVariable long id)    {
        Article articleToRemove = articleRepository.findById(id).orElseThrow(RuntimeException::new);

        articleRepository.delete(articleToRemove);
        return articleToRemove;
    }

}
