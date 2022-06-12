# component-processor

#### 介绍
https://blog.csdn.net/cml_blog/article/details/107434967
https://blog.csdn.net/cml_blog/article/details/120856707
之前两篇文章讲解了如何处理分布式一致性实现原理，都是在理论上对一致性的保障进行说明，本篇将基于之前的介绍完成流程引擎具体的实现，真正落地到代码上。
这里将基于saga分布式事务实现原理，基于实际业务场景进行适配设计。

processor:一个完整的业务流程定义，比如用户注册 

flowTask:业务流程组成的节点，将业务流程拆分成多个节点，每个节点都是一个最小化业务单元

#### 软件架构
软件架构说明
![alt 架构方案](https://img-blog.csdnimg.cn/6a9e6ff0830a4c8e961a163ab5548a20.jpeg#pic_center
)


#### 如何使用
1 自定义processor,继承TaskDrivenFlowProcessor，sample这里做了个包装
```java
public abstract class DefaultFlowProcessor extends TaskDrivenFlowProcessor {

    @Autowired
    private FlowTaskHolder flowTaskHolder;

    @Autowired
    private ProcessorTaskModelRepository processorTaskModelRepository;

    @PostConstruct
    public void init() {
        this.setFlowTaskHolder(flowTaskHolder);
        this.setProcessorTaskModelRepository(processorTaskModelRepository);
    }

}

@Component
public class UserRegisterProcessor extends DefaultFlowProcessor {

    @Override
    public String processorType() {
        return ProcessorTypeEnums.USER_REGISTER.type();
    }

    @Override
    protected ProcessorContext buildContext(ProcessorRequest request) {
        return new ProcessorContext();;
    }

    @Override
    protected FlowTaskType[] flowTasks() {
        return new FlowTaskType[]{
                FlowTaskEnums.TASK_STEP1,
                FlowTaskEnums.TASK_STEP2,
                FlowTaskEnums.TASK_STEP3,
                FlowTaskEnums.TASK_STEP4,
        };
    }
}
```

2 自定义flowTask,实现接口:FlowTask
```java
@Component
public class Step1FlowTask implements FlowTask {
    @Override
    public FlowTaskType taskType() {
        return FlowTaskEnums.TASK_STEP1;
    }

    @Override
    public ProcessResult execute(ProcessorRequest request, ProcessorContext processorContext) {
        System.out.println("\tflowTask1 execute");
        return ProcessResult.success();
    }

}


```

3 注册processor和flowTask

```java
@Component
@Configuration
public class ProcessorConfiguration {

    @Bean
    public FlowTaskHolder flowTaskHolder(List<FlowTask> flowTasks) {
        FlowTaskHolder flowTaskHolder = new FlowTaskHolder();
        flowTasks.forEach(flowTaskHolder::register);
        return flowTaskHolder;
    }

    @Bean
    public ProcessorContainer processorContainer(List<FlowProcessor> flowProcessors) {
        ProcessorContainer processorContainer = new ProcessorContainer();
        flowProcessors.forEach(processorContainer::register);
        return processorContainer;
    }



}
```

4 组装上下文，调用对应的processor

```java
    @Autowired
    private ProcessorContainer processorContainer;
    ProcessResult processResult = processorContainer.take(ProcessorTypeEnums.USER_REGISTER.type()).process(processorRequest);
```