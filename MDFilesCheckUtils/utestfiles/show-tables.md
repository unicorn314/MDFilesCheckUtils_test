---
title: 表格表示方式
---

{% capture article %}

## 表格表示方式

mackdown推荐的表格表示方式如下：

	|ID    |Name    |Age  |
	|------|:------:|----:|
	|01    |Tom     |18   |
	|02    |Jack    |20   |

但实际测试时无法识别为table样式，原因不明。

目前的替代解决方案是直接采用html标签，即html中的table、tr、th、td标签来生成表格。

代码如下：

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

实际效果如下：

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

<a style="color:red">可以看到，即使最后一个td标签没有正常的用反斜杠封闭，mackdown依然可以正常识别。</a>

{% endcapture %}

{% include templates/home.md %}