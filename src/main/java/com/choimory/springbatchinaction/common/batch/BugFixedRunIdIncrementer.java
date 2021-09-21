package com.choimory.springbatchinaction.common.batch;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;

public class BugFixedRunIdIncrementer extends RunIdIncrementer {
    private static final String RUN_ID = "run.id";

    @Override
    public JobParameters getNext(JobParameters parameters) {
        JobParameters jobParameters = (parameters == null) ? new JobParameters() : parameters;
        return new JobParametersBuilder().addLong(RUN_ID, parameters.getLong(RUN_ID))
                .toJobParameters();
    }
}
