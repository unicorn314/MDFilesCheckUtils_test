## 引言使用方式

> 引言的使用方式是“>”符号，效果如图所示





## 表格表示方式

mackdown推荐的表格表示方式如下：

|ID    |Name    |Age  |

|------|:------:|----:|

|01    |Tom     |18   |

但在实际测试时无法正常显示为表格样式，原因未知。目前只能采用html标签中的table、tr、th、td标签来生成表格。实际效果如下：

<table class="table table-bordered table-striped table-condensed">  
    <tr>  
        <th>city</th>  
    	<th>weather</th>
    	<th>date</th>
		<th>comment</th>
    </tr>  
    <tr>  
        <td>深圳</td>  
	    <td>暴雨</td>
	    <td>5.16</td>
		<td>啊啊啊</td>
    </tr>
    <tr>  
        <td>深圳</td>  
	    <td>暴雪</td>
	    <td>5.16</td>
		<td>啊啊啊</td>
    </tr> 
    <tr>  
        <td>深圳</td>  
	    <td>晴</td>
	    <td>5.16</td>
		<td>啊啊啊</td>
    </tr> 
    <tr>  
        <td>深圳</td>  
	    <td>龙卷风</td>
	    <td>5.16</td>
		<td>啊啊啊
    </tr> 
</table> 

## 加入一个内嵌页面

使用html代码中的iframe标签。

实际效果如下：

<iframe src="http://blog.shengbin.me/posts/iframe-in-markdown-of-jekyll" width="700px" height="500px"></iframe>
----------



##引用图片

格式为：!\[图片名称](链接地址)
[![keyabingo](http://a3.qpic.cn/psb?/V13f2fdp4IPomM/QLqAgn1mvNf7OVISB7qCsitXsKjhqkrhrcCcqhhcdzs!/b/dBkBAAAAAAAA&bo=sARUA7AEVAMRADc!&rf=viewer_4&t=5)](www.baidu.com)