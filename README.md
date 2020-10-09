# RefreshRecycler
一个多状态加载的刷新控件

## 简单用例
#### 1.在 build.gradle 中添加依赖
```
//必选
implementation 'androidx.recyclerview:recyclerview:1.1.0'
implementation 'com.mellivora:RefreshRecycler:1.2.0'
implementation 'com.scwang.smart:refresh-layout-kernel:2.0.1'

//可选
implementation  'com.scwang.smart:refresh-header-classics:2.0.1'    //经典刷新头
implementation  'com.scwang.smart:refresh-header-radar:2.0.1'       //雷达刷新头
implementation  'com.scwang.smart:refresh-header-falsify:2.0.1'     //虚拟刷新头
implementation  'com.scwang.smart:refresh-header-material:2.0.1'    //谷歌刷新头
implementation  'com.scwang.smart:refresh-header-two-level:2.0.1'   //二级刷新头
implementation  'com.scwang.smart:refresh-footer-ball:2.0.1'        //球脉冲加载
implementation  'com.scwang.smart:refresh-footer-classics:2.0.1'    //经典加载

```
如果使用 AndroidX 在 gradle.properties 中添加

```
android.useAndroidX=true
android.enableJetifier=true

```

#### 全局设置
```java
class App: Application {
    //static 代码段可以防止内存泄露
    init {
        //自定义刷新头视图
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        //自定义加载更多视图
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            TingZhiClassicsFooter(context)
        }
        //----------- 以下有默认视图（可不设置）  -----------
        //自定义空内容视图
        MultipleStatusView.setEmptyCreator { context, layout ->
            EmptyStatusView() //自定义View参考该默认View
        }
        //自定义加载视图
        MultipleStatusView.setLoadingCreator { context, layout ->
            LoadingStatusView()
        }
        //自定义错误视图
        MultipleStatusView.setErrorCreator{ context, layout ->
            ErrorStatusView()
        }
        //自定义网络错误视图
        MultipleStatusView.setNetErrorCreator { context, layout ->
            NetErrorStatusView()
        }
    }
}
```


#### 1.在 Activity 或者 Fragment 中添加代码
```java
  PullRecyclerView pullView = pullView.findViewById(R.id.pullview)
  //使用自定义的错误视图(一般使用全局的视图即可，这里模拟有特殊需求的场景)
  pullView.statusView.setErrorStatus(DemoErrorStatus())
  //设置空内容提示
  pullView.setEmptyHint("该列表空空如也~")
  //开启下拉刷新 和 加载更多
  pullView.setPullEnable(true, true) 
  //设置加载监听
  pullView.setPullListener { isRefresh, page -> loadData(isRefresh, page) }
  //默认加载使用加载布局
  pullView.showLoading()
```

#### 2.数据加载完毕
```
  pullView.loadFinish(isRefresh, moreEnable) //加载完成
  pullView.loadError(isRefresh, message, code) //加载错误(错误布局根据code切换)
```


#### 获取内置控件
```java
PullRecyclerView.swipeRecyclerView //内置的RecyclerView列表控件
PullRecyclerView.refreshLayout //内置的SmartRefreshLayout刷新控件
PullRecyclerView.statusView //内置的MultipleStatusView状态切换控件
```
