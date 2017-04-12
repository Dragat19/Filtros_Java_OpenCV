package com.example.dragat19.myapplication;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;


public class FiltroCamJavaOpenCv extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

   private JavaCameraView mCamaraView;
   private Mat mRgba, mImgGrey , mImgCanny;

    BaseLoaderCallback mcallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    mCamaraView.enableView();
                    break;
                default:
                    super.onManagerConnected(status);
                    break;
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_filtro_javaopencv);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        mCamaraView = (JavaCameraView) findViewById(R.id.vCamara);
        mCamaraView.setVisibility(View.VISIBLE);
        mCamaraView.setCvCameraViewListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCamaraView!=  null){
            mCamaraView.disableView();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamaraView!=  null){
            mCamaraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()){
            Log.e("Filtros","OpenCV Funciona Corectamente");
            mcallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }else {
            Log.e("Filtros","OpenCV No Funciona ");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9,this,mcallback);
        }
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height,width, CvType.CV_8UC4);
        mImgGrey = new Mat(height,width, CvType.CV_8UC1);
        mImgCanny = new Mat(height,width, CvType.CV_8UC1);

    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();

        Imgproc.cvtColor(mRgba,mImgGrey,Imgproc.COLOR_RGB2GRAY);
        Imgproc.Canny(mImgGrey,mImgCanny,50,50);

        return mImgGrey;
    }
}
