allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }


 导入:

dependencies {
        compile 'com.github.hc1234:MyHcUtils:5.0.1'
}

目录 :
     一:文件工具类型
     
     二:权限管理
     
     三:网路状态获取
     
     四:获取照片
     
     五:屏幕工具
     
     六:时间工具
     
     七:popwidow工具
     
     八:未读数控件
     
     九:Okhttp封装
     
     
     一 :文件工具
     
      调用 FileUtils.xxx
      
     方法: 
     1: CreteNewFile(Context context, String filepath) 创建文件
     2：CreteNewFileMidr(Context context, String filepath)创建文件夹
     3：Deletefile(Context context, String fielpath) 删除文件或文件夹
     4：GetFileSize(Context context, String filepath, Boolean isbackLong) 获取指定文件大小  isbacklong 为flase 返回文件大小String 如         1M,100K等   true 返回文件size大小 如1024
     5:ISSDCARD(Context context) 判断是否存在SD卡和存储权限是否打开
     
     
     二：权限管理
     
       调用 MyCheckPermissopn.xxx
       
       方法
       
       1:ApplyPermission(String permission,Activity activity) 判断并申请权限  requestCode值为MyCheckPermissopn.REQUSTCODE=1001
       
       2:checkPermission(@NonNull String permission, Context context) 判断是否有权限
       
       
       
       三:网路状态获取
       
       调用 NetWorkUtils.xxx
       
       方法
       
       1:isNetworkAvailable(Activity activity) 判断网络是否连接
       
       2:getNetworkState(Context context)判断网络类型  return "MOBILE" 为手机数据   return "WIFI" 为wifi类型网络
       
      
      
       四:获取照片
       
       调用 PhotoUtil.xxx
       
       方法
       
       1:takePicture(Activity activity) 默认拍照，自动生成文件 返回图片文件
       
       2:takePicture(Activity activity,File phtofile) 指定文件 拍照
       
       
       五:屏幕工具
       
       调用 ScreenUtil.xxx
       
       方法
       
       1：getScreenWidth(Context context)获取屏幕宽度
       
       2:getScreenHeight(Context context)获取屏幕高度
       
       3:getNavigationBarHeight(Context activity)获取导航栏高度
       
       4:getStatusHeight(Context context)获取状态栏高度
       
       5:dp2px(Context context,float dpValue) dp转px
      
       6:px2dp(Context context,float pxValue) px装dp
       
       7:sp2px(Context context,float spValue) sp转px
       
       8: px2sp(Context context,float pxValue) px转sp
       
       
       六:时间工具
       
       调用 TimeUtils.xxx
       
       1:getNetDateTime() 查询网站时间  默认格式yyyy-MM-dd HH:mm:ss 开启线程访问 
       
       2:getNetDateTime(String format) 查询网站时间 指定格式返回 开启线程访问 
       
       3:getDate() 获取本地时间 默认格式yyyy-MM-dd HH:mm:ss
    
       4:getDate(String format)获取本地时间 指定格式返回
       
       5: formatDateTime(long mss) 根据一个时间值 转换为 xx天 xx小时 xx分钟 xx秒
       
       6：paretime(String t1,String t2 ) 判断两个时间大小 返回正数 t1 比 t2大
       
       
       七:popwidow工具
       
       调用 HCSetFragmentDialog.Getinstace(FragmentManager fragmentManager).xxxx
       
       1: setlayout(int layoutid)设置布局文件
       
       2：setgravity(int gravity)设置显示位置 Gravity.Top等
       
       3:setwith(int with) 设置显示宽度 
       
       4:setheight(int height) 设置显示高度
       
       5:show() 显示
       
       6：getview(Getview getview)获取布局view 用于你对布局进行操作和初始化等
     	
	
       八:未读数控件
       
       使用 com.hcutils.hclibrary.views.Unreadview布局中使用 
       
       方法
       
       1:setUnreadVisibility(int visibi)未读数是否显示
       
       2:setUnreadcount(String number) 未读数显示的值
       
       3:setUnreadTextColor(int color) 设置字体显示颜色
       
       4:setUnreadBackground(int drawble)设置未读数背景显示
       
       5:setUnreadmove(Boolean ismove) 是否开启未读数拖动
       
       6:getUnreadCallback(unreadcallbck unreadcallbck)未读数拖动 回掉事件
       
       
       九:Okhttp封装
       
       调用 OkHttpNetWork.getOkhttpNetWork().xxx
       
       方法
       
       1: 
       OkhttpPost(final String uri, final RequestBody builder, final Context context, final Boolean isshowTishi, final    ReturnStr returnstr)
       
       uri 访问地址
       builder 参数组装  例如 RequestBody boby=new FormBody.Builder().add("key","value")
                .add("key","value")
                .add("key","value").build();
	isshowTishi 是否打开访问失败提示 
	returnstr 访问成功回掉数据
       
       
       
       	





