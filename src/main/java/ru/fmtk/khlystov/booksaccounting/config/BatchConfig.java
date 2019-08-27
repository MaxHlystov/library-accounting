package ru.fmtk.khlystov.booksaccounting.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.fmtk.khlystov.booksaccounting.domain.Author;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;

@EnableBatchProcessing
@Configuration
public class BatchConfig {
    private final Logger logger = LoggerFactory.getLogger("Batch");

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemReader<Author> reader() {
        MongoItemReader<Author> reader = new MongoItemReader<>();
        reader.setTemplate(mongoTemplate);
        reader.setSort(new HashMap<String, Sort.Direction>() {{
            put("_id", Sort.Direction.DESC);
        }});
        reader.setTargetType(Author.class);
        reader.setQuery("{}");
        //reader.setName("authorsMongoReader");
        return reader;
//        return new FlatFileItemReaderBuilder<Author>()
//                .name("personItemReader")
//                .resource(new FileSystemResource("entries.csv"))
//                .delimited()
//                .names(new String[]{"name", "age"})
//                .fieldSetMapper(new BeanWrapperFieldSetMapper<Author>() {{
//                    setTargetType(Author.class);
//                }})
//                .build();
    }

    @Bean
    public ItemProcessor processor() {
        return (ItemProcessor<Author, Author>) author -> {
            logger.info("Process author: %s", author.toString());
            return author;
        };
    }

    @Bean
    public JpaItemWriter<Author> writer() {
        return new JpaItemWriterBuilder<Author>()
                .entityManagerFactory(entityManagerFactory)
                //("authorItemWriterToH2")
                .build();
    }

    @Bean
    public Job importUserJob(Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }

    @Bean
    public Step step1(JpaItemWriter writer, ItemReader reader, ItemProcessor itemProcessor) {
        return stepBuilderFactory.get("step1")
                .chunk(5)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(new ItemReadListener<Author>() {
                    public void beforeRead() {
                        logger.info("Начало чтения");
                    }

                    public void afterRead(Author author) {
                        logger.info("Конец чтения %s", author.toString());
                    }

                    public void onReadError(Exception e) {
                        logger.info("Ошибка чтения");
                    }
                })
                .listener(new ItemWriteListener<Author>() {
                    public void beforeWrite(List<? extends Author> list) {
                        logger.info("Начало записи");
                    }

                    public void afterWrite(List<? extends Author> list) {
                        logger.info("Конец записи");
                    }

                    public void onWriteError(Exception e, List list) {
                        logger.info("Ошибка записи");
                    }
                })
                .listener(new ItemProcessListener<Author, Author>() {
                    public void beforeProcess(Author author) {
                        logger.info("Начало обработки");
                    }

                    public void afterProcess(Author authorBefore, Author authorAfter) {
                        logger.info("Конец обработки");
                    }

                    public void onProcessError(Author author, Exception e) {
                        logger.info("Ошбка обработки");
                    }
                })
                .listener(new ChunkListener() {
                    public void beforeChunk(ChunkContext chunkContext) {
                        logger.info("Начало пачки");
                    }

                    public void afterChunk(ChunkContext chunkContext) {
                        logger.info("Конец пачки");
                    }

                    public void afterChunkError(ChunkContext chunkContext) {
                        logger.info("Ошибка пачки");
                    }
                })
//                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }
}
