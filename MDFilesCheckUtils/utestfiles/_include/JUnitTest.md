---
title: 测试用
-----
{% capture article %}

[不存在的](http://www.baidu.edu "www.baidu.edu")
[存在的](home1.md "home.md")


{% endcapture %}

{% include templates/home.md %}