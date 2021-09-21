package com.choimory.springbatchinaction.common.batch;

import org.springframework.batch.item.database.JpaItemWriter;

import java.util.ArrayList;
import java.util.List;

public class JpaListItemWriter<T> extends JpaItemWriter<List<T>>{
    private JpaItemWriter<T> jpaItemWriter;

    public JpaListItemWriter(JpaItemWriter<T> jpaItemWriter){
        this.jpaItemWriter = jpaItemWriter;
    }

    @Override
    public void write(List<? extends List<T>> items) {
        List<T> totalList = new ArrayList<>();

        for(List<T> list : items){
            totalList.addAll(list);
        }

        jpaItemWriter.write(totalList);
    }
}
