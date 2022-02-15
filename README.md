# MyCloudMusic
网校寒假考核作业

## 简要介绍

红岩网校的寒假考核作业，一个简单的音乐播放器~~（这简陋的样子都不好意思叫网易云 ）~~，调用网易云的api实现登录，听歌功能。

打开activity先进入一个5秒可跳过的开屏界面，画面渐显，下方一个自定义view美化了UI，倒计时按钮用TimerTask任务更新。

开屏动画完进入登录界面，可以手机验证码方式登录，手机号加密码方式登录，邮箱登录，登录完后进入主界面。

主界面是tablayout+viewpager2实现（刚开始做了发现、播客、我的、关注、云村五个底栏，但只实现了我的页面~~(人太菜了，做一个都费劲，居然想做五个？)~~）

在个人界面顶部是CoordinatorLayout+AppBarLayout+CollapsingToolbarLayout的布局，想做置顶效果(不过做的很拉跨就是了)。下方歌单界面是一个tablayout+viewpager2的布局，来显示网易云的三种歌单（个人创建歌单、收藏歌单、推荐歌单），viewpager2里面是recycleview布局的fragment用来显示每一个歌曲，为了viewpager2嵌套的滑动冲突在内层viewpager2外套了层自定义framelayout解决。

点击歌单转到歌单的详情界面，上方用Appbarlayout实现置顶功能，下面是NestedScrollView中套recycleview(本来网易云歌单界面中在上方歌单简介和下方歌曲列表中还有层 “收藏|评论|分享” 的界面，可以放在NestedScroolView中，但个人没有实现)

点击歌曲进入到歌曲界面，Viewpager2嵌套ViewPager2，外层ViewPager2左右滑动实现歌曲切换功能，内层ViewPager2实现点击切换到歌词。内层的ViewPager2有两个fragment，光盘的fragment是一个自定义的view实现转圈效果，歌词的fragment是自定义ScrollView实现歌词的滑动效果。
## 界面展示
(gif展现出来卡卡的，实际要稍微好一点)

![image](https://github.com/18445/MyCloudMusic/blob/master/image/open4.gif)
![image](https://github.com/18445/MyCloudMusic/blob/master/image/open3.gif)
![image](https://github.com/18445/MyCloudMusic/blob/master/image/open2.gif)
![image](https://github.com/18445/MyCloudMusic/blob/master/image/open1.gif)
![image](https://github.com/18445/MyCloudMusic/blob/master/image/home1.gif)
![image](https://github.com/18445/MyCloudMusic/blob/master/image/home3.gif)
![image](https://github.com/18445/MyCloudMusic/blob/master/image/disk3.gif)
![image](https://github.com/18445/MyCloudMusic/blob/master/image/disk2.gif)
![image](https://github.com/18445/MyCloudMusic/blob/master/image/disk1.gif)

## 缺点反思
由于现阶段能力问题，许多自己的想法无法得到很好的实现

* 不会搭建架构，导致写项目的时候代码有些混乱，想到什么就写什么，写着写着把自己绕晕了

* UI做的太丑了，对怎么做好UI没什么头绪

* 工具类并没有理解是什么运行的，暂时只会用，只学了个皮毛

* 当外层点击事件和内层滑动事件发生冲突时不会解决

* 对ViewGroup的理解不够深，完成不了自己想要实现的功能

* 感觉项目还没有要用到本地存储的地方，还没有去学习，有点可惜

## 心得体会
比起寒假刚开始对安卓还是比较生疏来说，现在自己还算是入门了，最起码能写点小玩意了

* 复习了下之前学过的安卓知识

* 学习了一点基础的自定义view

* 了解滑动触碰的一些原理

* 在activity之间用extra传递数据，activity与fragment用viewmodel传递数据

* 基础的网络请求能够进行，能够加载一些网络资源

想想自己在不到半年的时间从什么都不懂的小白，能到自己现在用kotlin写写小玩意(尽管很拉)，还是很感慨的，尽管现在学得越来越多，感觉不会的也越来多:( ，在未来慢慢完善自己的缺点，争取多学点东西
