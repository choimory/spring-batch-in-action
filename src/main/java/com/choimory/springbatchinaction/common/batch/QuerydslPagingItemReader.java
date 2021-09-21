package com.choimory.springbatchinaction.common.batch;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T> {
    //JPQL 관련 필드를 제거하고 Querydsl 쿼리를 람다로 받을수 있게 queryFunction 필드를 추가함
    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;
    protected final Map<String, Object> jpaPropertyMap = new HashMap<>();
    protected boolean transacted = true;//default value
    protected Function<JPAQueryFactory, JPAQuery<T>> queryFunction;

    //생성자, JpaPagingItemReader와 동일
    protected QuerydslPagingItemReader(){
        setName(ClassUtils.getShortName(QuerydslPagingItemReader.class));
    }

    //생성자2, ItemReader에서 생성자로 리더 생성하여 반환할때 사용
    public QuerydslPagingItemReader(EntityManagerFactory entityManagerFactory, int pageSize, Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
        this();
        this.entityManagerFactory = entityManagerFactory;
        this.queryFunction = queryFunction;
        setPageSize(pageSize);
    }

    //
    public void setTransacted(boolean transacted) {
        this.transacted = transacted;
    }

    //doReadPage()에서 사용
    protected void clearIfTransacted() {
        if (transacted) {
            entityManager.clear();
        }
    }

    //
    protected JPAQuery<T> createQuery() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFunction.apply(queryFactory);
    }

    protected void initResults() {
        if (CollectionUtils.isEmpty(results)) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }
    }

    protected void fetchQuery(JPAQuery<T> query) {
        if (!transacted) {
            List<T> queryResult = query.fetch();
            for (T entity : queryResult) {
                entityManager.detach(entity);
                results.add(entity);
            }
        } else {
            results.addAll(query.fetch());
        }
    }

    @Override
    protected void doOpen() throws Exception {
        super.doOpen();
        entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap);
        if (entityManager == null) {
            throw new DataAccessResourceFailureException("Unable to obtain an EntityManager");
        }
    }

    @Override
    protected void doClose() throws Exception {
        entityManager.close();
        super.doClose();
    }

    @Override
    protected void doReadPage() {
        clearIfTransacted();

        JPAQuery<T> query = createQuery()
                .offset(getPage() * getPageSize())
                .limit(getPageSize());

        initResults();

        fetchQuery(query);
    }

    @Override
    protected void doJumpToPage(int itemIndex) {
    }
}
