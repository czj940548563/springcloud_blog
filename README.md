# springcloud_blog
> * blog base on springcloud and vue
> * 目前完成的是权限和登录认证模块
> * 纯练手，整个项目需要重构，feign不应该单独一个服务，而是一个作为一个公用的common包，在每个服务需要时maven引入，或者在每个服务下都写一个暴露调用自己接口服务的feign包。
> * jwt鉴权应该写在gateway的GlobalFilter里，在过滤器中进行调用权限服务获取token信息和权限信息。鉴权成功就filter.chain，不成功就直接在过滤器中返回response401即可
> * 因练手原因，权限服务增删改查用了redis的zset数据结构存取，主要是为了达到分页的效果，在存zset过程中遇到将雪花算法生产的分布式id直接存到score中会丢失精度，score为Double类型，去掉符号位数为52位，雪花算法去掉符号位为63位。
> * (1)12bit序列号能表示4096个数。去掉最高位，能表示2048个数。所以单个msgid生成节点（dispatch模块）每毫秒，每个用户要超过2048条消息，才可能出现score重复。这个基本不可能发生。
> > * (2)去掉10bit工作机id号，需要同一毫秒，同一用户在不同的dispatch节点都接收到消息，score才可能冲突。
> >* (3)即使出现了score冲突（两条消息有相同score），最多造成拉取离线消息多拉取相同score的消息（本来一次拉取10条离线，结果可能拉到11条），对业务也没有影响。
> >* 因此采用去掉10bit工作机id和序列号的最高位bit将63bit（不含符号位）的msgid转换成52bit的score对业务上没有影响。同时解决了redis sorted zset丢失精度的问题。

