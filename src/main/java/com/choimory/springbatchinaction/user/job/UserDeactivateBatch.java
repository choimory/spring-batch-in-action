package com.choimory.springbatchinaction.user.job;

import com.choimory.springbatchinaction.common.batch.BugFixedRunIdIncrementer;
import com.choimory.springbatchinaction.common.batch.QuerydslPagingItemReader;
import com.choimory.springbatchinaction.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;

import static com.choimory.springbatchinaction.user.entity.QUser.user;

@Configuration
@RequiredArgsConstructor
public class UserDeactivateBatch {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private static final String PREFIX = "UserDeactivate";
    private static final int CHUCK_SIZE = 1000;
    private static final int PAGE_SIZE = CHUCK_SIZE;

    @Bean(PREFIX + "Job")
    public Job userDeactivateJob(){
        return jobBuilderFactory.get(PREFIX + "Job")
                .incrementer(new BugFixedRunIdIncrementer())
                .start(userDeactivateStep())
                .build();
    }

    @Bean(PREFIX + "Step")
    @JobScope
    public Step userDeactivateStep(){
        return stepBuilderFactory.get(PREFIX + "Step")
                .<User, User>chunk(CHUCK_SIZE)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean(PREFIX + "Reader")
    @StepScope
    public QuerydslPagingItemReader<User> reader(){
        QuerydslPagingItemReader<User> reader =  new QuerydslPagingItemReader<>(entityManagerFactory, PAGE_SIZE,
                query -> query.select(user)
                        .from(user)
                        .where(user.lastLoginDateTime.before(LocalDateTime.now()
                                .minusYears(1))));

        reader.setMaxItemCount(1000);

        return reader;
    }

    private ItemProcessor<User, User> processor(){
        return user -> {
            user.setIsActivated(false);
            return user;
        };

        /*기존 레코드 업데이트 테스트*/
        /*return user -> User.builder()
                .idx(user.getIdx())
                .isActivated(false)
                .password("changed")
                .build();*/
    }

    private JpaItemWriter<User> writer(){
        JpaItemWriter<User> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }
}
