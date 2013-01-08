# UrlImageView

AndroidのImageViewで、インターネット上にある画像を表示する独自ImageView。  
MIT Licenseで、自由にお使いください。  

変更点：
* 複数のUrlImageViewを利用されると、画像の読み込み順番がLIFOの方がなにかと良かったので、画像を読み込む優先順位を変更。
* WorkerThreadを実装しました。スレッド数は５つ。
* これにより、GridViewなどで利用した場合、ユーザ操作で見たい画像がある程度先に読まれるようになった。
* 画像のキャッシュの圧縮率は９０です。また、アニメーションGIFなどには対応してません。
* 画像のキャッシュデータをファイルに書き出す際に若干もたつくので、今後の課題です。

## 以下、注意点とサンプルActivity

# Android マニフェスト
* マニフェストファイルに android.permission.INTERNET のパーミッションをつける

    <uses-permission android:name="android.permission.INTERNET"/>    

# layoutファイル
* レイアウトファイルでは、ImageViewと同様の設定が可能。設定例。

    <jp.sharakova.android.urlimageview.UrlImageView    
        android:id="@+id/imageView"    
        android:layout_width="fill_parent"     
        android:layout_height="fill_parent"    
        />    

# Activity サンプル
    package jp.sharakova.android.urlimageview.sample;    
    import jp.sharakova.android.urlimageview.ImageCache;    
    import jp.sharakova.android.urlimageview.R;    
    import jp.sharakova.android.urlimageview.UrlImageView;    
    import jp.sharakova.android.urlimageview.UrlImageView.OnImageLoadListener;    
    import android.app.Activity;    
    import android.os.Bundle;    
    import android.widget.Toast;    
    
    public class UrlImageViewSampleActivity extends Activity {    
    
	    private UrlImageView mImageView;    
    
	    @Override    
	    public void onCreate(Bundle savedInstanceState) {    
		    super.onCreate(savedInstanceState);    
		    setContentView(R.layout.main);    
		    mImageView = (UrlImageView)findViewById(R.id.imageView);    
		    mImageView.setImageUrl("http://k.yimg.jp/images/top/sp/logo.gif", imageLoadListener);    
	    }    
    
        @Override    
        public void onDestroy() {    
            ImageCache.deleteAll(getCacheDir());    
            super.onDestroy();    
        }    
    
        final private OnImageLoadListener imageLoadListener = new OnImageLoadListener() {    
            @Override    
            public void onStart(String url) {    
                Toast.makeText(getApplicationContext(), "start", Toast.LENGTH_SHORT).show();    
            }    
    
            @Override    
            public void onComplete(String url) {    
                Toast.makeText(getApplicationContext(), "end", Toast.LENGTH_SHORT).show();    
            }    
        };    
    }    


* OnImageLoadListener の onStart で、画像を読み込む、直前に処理を入れる事ができます。
* OnImageLoadListener の onComplete で、画像を読み込んだ後の処理を実行できます。
* setImageUrl　で、画像をインターネットから読み込みを開始して、読み込み終わると画像を表示いたします。読み込んだ画像は、一時的にAndroid内にキャッシュし、2度目の表示では高速に読み込む事ができます。
* onDestroyで実行している。ImageCache.deleteAll(getCacheDir());で、Android内に保存したキャッシュを削除いたします。