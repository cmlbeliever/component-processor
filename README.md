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
1 注册processor和flowtask

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

2 组装上下文，调用对应的processor

```java
    @Autowired
    private ProcessorContainer processorContainer;
    ProcessResult processResult = processorContainer.take(ProcessorTypeEnums.USER_REGISTER.type()).process(processorRequest);
```