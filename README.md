# SuspendedBall
悬浮球
## 效果图

    app:showPercent="1"
 ![image](https://github.com/dalong982242260/SuspendedBall/blob/master/img/qiu.gif)   ![image](https://github.com/dalong982242260/SuspendedBall/blob/master/img/feitian.gif)  
 
    app:showPercent="0.5"
 ![image](https://github.com/dalong982242260/SuspendedBall/blob/master/img/qiu1.gif)   ![image](https://github.com/dalong982242260/SuspendedBall/blob/master/img/feitian1.gif) 

##使用

        <com.dl.draggable.DraggableFrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
                            xmlns:tools="http://schemas.android.com/tools"
                            xmlns:app="http://schemas.android.com/apk/res-auto"
                            android:layout_width="match_parent"
                            android:layout_height="match_parent"
                            android:orientation="vertical"
                            app:showPercent="1"
                            app:direction="all"
                            tools:context="com.dalong.suspendedball.MainActivity">
                        
                                 <!- view-->
                        
        </com.dl.draggable.DraggableFrameLayout>   
     
     
##Attributes

|name|format|description|
|:---:|:---:|:---:|
| showPercent | float |设置显示百分比
| direction | int |方向位置显示  left  right  all



