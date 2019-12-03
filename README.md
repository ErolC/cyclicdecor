## cyclicDecor
一个是viewPager可以无限滚动的库

## 依赖
首先对该库进行依赖<br>
在项目的build.gradle文件中相应位置加入下列代码。
```groovy
    allprojects {
            repositories {
                //code ...
                maven { url 'https://jitpack.io' }
            }
    }
```
在项目app文件夹下的build.gradle文件中相应位置加入下列代码。
```groovy
    dependencies {
         implementation 'com.github.ErolC:cyclicdecor:1.0.0'
    }
```

## 使用
```
CyclicDecor decor = new CyclicDecor.Builder(binding.pager)//普通的viewPager
                .setAdapter(adapter)//库提供的CyclicAdapter的子类
                .setIndicator(binding.indicator)//库也提供了指示灯CyclicIndicator
                .automatic(3000)//可自动滑动，时间间隔是3s，如果设置为false则不会自动滑动,设置为true则会在创建完成时自动滑动而无需调用start，时间间隔为1s
                .isFastSwitch(false)//自动滑动时是否快速滑动
                .build();
                
                decor.start();//开始自动滑动
                decor.stop();//停止自动滑动
```
#### CyclicIndicator
库提供的指示灯，有自定义属性如下：
```
    app:selectColor="@color/colorAccent" 
    app:defaultColor="@color/colorPrimary"
    app:gravity="center" //指示灯位置
    app:interval="200dp" //指示灯间隔
    app:itemHeight="20dp" //指示灯高度 如果shape是circle，则itemHeight则是直径
    app:itemWidth="30dp" //指示灯宽度
    app:shape="circle" //指示灯形状 //api16 无效
```
例子：
```
 <com.erolc.cyclicdecor.indicators.CyclicIndicator
            android:id="@+id/indicator"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:selectColor="@color/colorAccent"
            app:defaultColor="@color/colorPrimary"
            app:gravity="end"
            app:interval="20dp"
            app:itemHeight="5dp"
            app:itemWidth="5dp"
            app:shape="circle"
            />
```
具体例子可以查看 [MainActivity]()