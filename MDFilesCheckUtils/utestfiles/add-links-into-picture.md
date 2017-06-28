---
title: 在一张图片上添加多个热点链接
---

{% capture article %}

##1.下载安装图片编辑工具
使用Adobe Dreamweaver，简称DW。

![1](http://a1.qpic.cn/psb?/V13f2fdp4IPomM/w6jJOO.D6MQNp8XHTX5Yf0*hzVsBSHjZ6gboyx8Ts0s!/b/dBcBAAAAAAAA&bo=YAB9AGAAfQADACU!&rf=viewer_4)

##2.新建空白html文件

![2](http://a1.qpic.cn/psb?/V13f2fdp4IPomM/ZSm.mnAlS5uNap9JoQU6ya6b9ENVUN3*YE29*3lNxFg!/b/dPYAAAAAAAAA&bo=ygKzAMoCswADACU!&rf=viewer_4)

##3.删除多余html代码（可选）
点击“代码”按钮可以查看当前的html代码，为了之后拷贝方便，可以选择先将这部分自动生成的代码删光。

![3](http://a1.qpic.cn/psb?/V13f2fdp4IPomM/A9ZpjvnkW.piXMDyWfjvh.OOMI9MosWYmN5kEslVRU8!/b/dCABAAAAAAAA&bo=OgOXAToDlwEDACU!&rf=viewer_4)

##4.加载图片
点击“图像”按钮选择需要加载的图片

![4](http://a3.qpic.cn/psb?/V13f2fdp4IPomM/rkQuipRAFzwdF6zwmnnO1mGc7syYojRPV2bNCM9vHNY!/b/dBkBAAAAAAAA&bo=aAGMAGgBjAADACU!&rf=viewer_4)

有两种方式，既可以加载本地图片，也可以加载网络图片。

如果使用网络图片，则需要在URL后的输入框中填入图片的网络地址（工具会自动在“文件名”的地方生成“psb”），点击确认后即可在编辑窗口中看到图片。

![5](http://a1.qpic.cn/psb?/V13f2fdp4IPomM/5Cc6y0Vkl9kl.h.W3FGn.0TSdklW6Irt3kWMA8Q7V3k!/b/dCABAAAAAAAA&bo=uwJQArsCUAIDACU!&rf=viewer_4)

##5.选定热点区域
点击左下角的矩形热点工具，并在图片上拖动生成矩形区域（圆形和多边形工具建议不要使用，实测时发现使用圆形工具和多边形工具生成的链接区域和预定坐标位置有一定偏移，原因未知。）

![6](http://a1.qpic.cn/psb?/V13f2fdp4IPomM/APxCo28BzSHSdeMP.L4fEZdfrJt47k1Dauy2xSCwNPU!/b/dBcBAAAAAAAA&bo=vgE5Ab4BOQEDACU!&rf=viewer_4)

划好热点区域后，在“链接”中填入热点链接地址，在“目标”下拉菜单中选中_blank（即在新窗口中打开链接）。

![7](http://a3.qpic.cn/psb?/V13f2fdp4IPomM/xxxO*A4QHtVJG.Abxn8dOdTl0h7DqmJZIIDNrN.xFXM!/b/dB8BAAAAAAAA&bo=cALuAXAC7gEDACU!&rf=viewer_4)

##6.生成html代码
在第3步中提到过：任何时候只要点击左上角的“代码”按钮，就可以看到html代码。

图片链接全部加完之后，只需点击“代码”按钮，然后将<img\>标签和<map\>标签中的内容复制粘贴到需要的页面中即可。如果之前在第3步中提前删除了多余html代码的话，只需要全选+复制粘贴即可。

![8](http://a3.qpic.cn/psb?/V13f2fdp4IPomM/PzWOnnI7qwbhQaa.qTVUNry0tdUXugW4mRoKrOWghCk!/b/dBkBAAAAAAAA&bo=LgNdAi4DXQIDACU!&rf=viewer_4)

{% endcapture %}

{% include templates/home.md %}