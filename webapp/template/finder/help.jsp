<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<title>帮助中心</title>
<style type="text/css">
ul.list{list-style-type: disc;}
ul.list li{line-height: 24px; font-size: 13px;}
table{font-size: 13px;}
table tr td{padding-left: 4px; height: 24px; font-size: 13px;}
</style>
</head>
<body>
<div class="finder">
    <div class="menu-bar" style="padding-top: 4px;">
        <div style="float: left; width: 80px;">
            <a class="button disabled" href="javascript:void(0)"><span class="back-disabled"></span></a>
            <a class="button" href="javascript:void(0)" onclick="window.location.reload();" title="刷新"><span class="refresh"></span></a>
        </div>
        <div style="float: left; height: 28px; position: relative;"></div>
        <div style="float: right; width: 40px;">
            <a class="button" href="${requestURI}?action=finder.help" title="帮助"><span class="help"></span></a>
        </div>
    </div>
</div>

<div class="finder">
    <h1>功能</h1>
    <ul class="list">
        <li>Finder是一个模拟windows资源浏览器的web文件管理工具。</li>
        <li>支持全键盘操作，几乎所有操作均有对应的快捷键支持。</li>
        <li>支持右键菜单，文件的常规操作都可以通过右键菜单完成。</li>
        <li>支持文件重命名，点击选中文件，然后按F2即可重命名文件。</li>
        <li>支持大上传，超大文件会自动分段上传，默认设置每次上传10M。</li>
        <li>支持文件拖拽上传，可同时拖拽多个文件上传。</li>
        <li>支持截图上传，截图之后按Ctrl + V。</li>
        <li>grep支持，类似linux系统的grep命令，支持任意大小的文件。</li>
        <li>less支持，类似linux系统的less命令，支持任意大小的文件。</li>
        <li>tail支持，类似linux系统的tail命令，支持任意大小的文件。</li>
    </ul>
</div>

<div class="finder">
    <h1>使用技巧</h1>
    <ul class="list">
        <li>双击文件名可以打开文件。</li>
        <li>按住Ctrl键可以在新窗口打开less和tail。</li>
        <li>音频播放的同时可以继续其他操作，包括切换不同的文件夹。</li>
        <li>地址栏的suggest列表也是支持键盘上下键滚动的哦。</li>
    </ul>
</div>

<div class="finder">
    <h1>快捷键</h1>
    <table>
        <tr>
            <td style="width: 480px; height: 30px; background-color: #efefef;">操作</td>
            <td style="width: 300px; height: 30px; background-color: #efefef;">快捷键</td>
        </tr>
        <tr>
            <td>重命名</td>
            <td>F2</td>
        </tr>
        <tr>
            <td>向上滚动</td>
            <td>UP</td>
        </tr>
        <tr>
            <td>向下滚动</td>
            <td>DOWN</td>
        </tr>
        <tr>
            <td>向左滚动</td>
            <td>LEFT</td>
        </tr>
        <tr>
            <td>向右滚动</td>
            <td>RIGHT</td>
        </tr>
        <tr>
            <td>全选</td>
            <td>CTRL + A</td>
        </tr>
        <tr>
            <td>剪切</td>
            <td>CTRL + X</td>
        </tr>
        <tr>
            <td>拷贝</td>
            <td>CTRL + C</td>
        </tr>
        <tr>
            <td>粘贴</td>
            <td>CTRL + V</td>
        </tr>
        <tr>
            <td>多选</td>
            <td>CTRL + CLICK</td>
        </tr>
        <tr>
            <td>多选</td>
            <td>ALT + CLICK</td>
        </tr>
        <tr>
            <td>多选</td>
            <td>CTRL + [UP|DOWN|LEFT|RIGHT]</td>
        </tr>
        <tr>
            <td>删除选中文件</td>
            <td>DELETE</td>
        </tr>
    </table>
</div>
<div class="h20"></div>
</body>
</html>
