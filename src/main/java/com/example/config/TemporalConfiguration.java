package com.example.config;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.opentracingshim.OpenTracingShim;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.opentracing.OpenTracingClientInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TemporalConfiguration {

    @Bean
    public WorkflowClientOptions workflowClientOptions() {
        Tracer shim = OpenTracingShim.createTracerShim(GlobalOpenTelemetry.get());
        GlobalTracer.registerIfAbsent(shim);

        return WorkflowClientOptions.newBuilder()
                .setInterceptors(new OpenTracingClientInterceptor())
                .build();
    }
}
