package ru.fmtk.khlystov.booksaccounting.changelogs;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.fmtk.khlystov.booksaccounting.domain.Author;
import ru.fmtk.khlystov.booksaccounting.domain.Book;
import ru.fmtk.khlystov.booksaccounting.domain.Genre;

import java.util.ArrayList;
import java.util.List;


@ChangeLog(order = "001")
public class InitMongoDBDataChangeLog {

    private List<Genre> genres;
    private List<Author> authors;

    @ChangeSet(order = "000", id = "dropDB", author = "Khlystov")
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initGenres", author = "Khlystov")
    public void initGenres(MongoTemplate template) {
        genres = saveValues(template,
                new Genre[]{
                        new Genre("Драма"),
                        new Genre("ЖЗЛ"),
                        new Genre("Фантастика")}
        );
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "Khlystov")
    public void initStudents(MongoTemplate template) {
        authors = saveValues(template,
                new Author[]{
                        new Author("Алексей", "Толстой"),
                        new Author("Скотт", "Фицжеральд"),
                        new Author("Айзек", "Азимов"),
                        new Author("Алексей", "Сидоров")});
    }

    @ChangeSet(order = "003", id = "initBooks", author = "Khlystov")
    public void initBooks(MongoTemplate template) {
        saveValues(template,
                new Book[]{
                        new Book("Детство Никиты",
                                "Повесть русского советского писателя Толстого Алексея Николаевича, рассказывающая о жизни и приключениях мальчика из деревни - сыне сельского помещика.",
                                authors.get(0),
                                genres.get(0)),
                        new Book("Илья Ефимович Репин",
                                "Документальное произведение, 1943 год.",
                                authors.get(3),
                                genres.get(1)),
                        new Book("Основание",
                                "Роман представляет собой сборник из пяти рассказов (вместе образующих единый сюжет).",
                                authors.get(2),
                                genres.get(2)),
                        new Book("Великий Гэтсби",
                                "Действие романа, главной линией сюжета которого является любовная история с детективной и трагической развязкой, развивается недалеко от Нью-Йорка, на «золотом побережье» Лонг-Айленда среди вилл богачей. Восхищаясь богатыми и их очарованием Фицджеральд в то же время подвергает сомнению неограниченный материализм и кризис морали Америки той эпохи.",
                                authors.get(1),
                                genres.get(0))});
    }

    private <T> List<T> saveValues(MongoTemplate template, T[] array) {
        List<T> result = new ArrayList<>();
        for (T val : array) {
            result.add(template.insert(val));
        }
        return result;
    }
}
