package com.cml.framework.processor.sample;

import com.cml.framework.processor.sample.repository.ProcessorTaskModelDO;
import com.cml.framework.processor.sample.repository.ProcessorTaskRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner(ProcessorTaskRepository taskRepository) {
        return args -> {
            System.out.println("---------------------");

            ProcessorTaskModelDO taskModel = new ProcessorTaskModelDO();
            taskModel.setId(1L);
            taskModel.setTaskType("a");
            System.out.println("-->save:" + taskRepository.save(taskModel));

            System.out.println("query:"+taskRepository.findById(1L));
        };
    }
}
