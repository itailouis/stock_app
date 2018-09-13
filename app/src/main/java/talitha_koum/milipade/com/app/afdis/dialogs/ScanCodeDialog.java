package talitha_koum.milipade.com.app.afdis.dialogs;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.io.IOException;

import talitha_koum.milipade.com.app.afdis.R;

import static android.Manifest.permission;
import static android.content.Context.VIBRATOR_SERVICE;


public class ScanCodeDialog extends DialogFragment implements QRCodeReaderView.OnQRCodeReadListener {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 12;

    public ScanCodeDialog() {

    }
    private final String TAG = ScanCodeDialog.class.getSimpleName();
    private SimpleDialogListener mHost;
    private static final float BEEP_VOLUME = 0.15f;
    private static final long VIBRATE_DURATION = 200L;

    private QRCodeReaderView qrCodeReaderView;
    //private ViewfinderView viewfinderView;
    private MediaPlayer mediaPlayer;
    private String results="";



    private final MediaPlayer.OnCompletionListener beepListener = new BeepListener();
    //private SharedPreferences settings;
    private boolean playBeep=true;
    private boolean vibrate=true;

    public interface SimpleDialogListener {
        void onPositiveResult(String dlg);
        void onNegativeResult(String dlg);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        //mHelper = new OrderDbHelper(getActivity());
        //final Message product = b.getParcelable("message");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Create the custom layout using the LayoutInflater class
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.scan_layout,null);
        qrCodeReaderView = (QRCodeReaderView) v.findViewById(R.id.preview_view);
        //viewfinderView = (ViewfinderView) v.findViewById(R.id.ViewfinderView);
        //viewfinderView.
        if (ActivityCompat.checkSelfPermission(getContext(), permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
            //Utils.log("Camera Permission Granted . ");

        } else {
            requestCameraPermission();
            //Utils.log(" Requesting Camera Permission ");

        }
        initQRCodeReaderView();
        initBeepSound();
        builder.setView(v);
        return builder.create();
    }
    @Override
    public void onCancel(DialogInterface dlg) {
        super.onCancel(dlg);
        Log.i(TAG, "Dialog cancelled");
        mHost.onNegativeResult("scanning canceled");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHost = (SimpleDialogListener)activity;
    }

    private void initQRCodeReaderView() {
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setBackCamera();
        qrCodeReaderView.setTorchEnabled(true);
        qrCodeReaderView.setQRDecodingEnabled(true);
        qrCodeReaderView.startCamera();

    }
    @Override
    public void onQRCodeRead(Result result, PointF[] points) {
        //Snackbar.make(qrCodeReaderView, result.getBarcodeFormat()+"  "+result.getText(), Snackbar.LENGTH_SHORT).show();
        playBeepSoundAndVibrate();
        qrCodeReaderView.stopCamera();
        if (result.getBarcodeFormat()== BarcodeFormat.QR_CODE){
            mHost.onNegativeResult("no results found ");
            dismiss();
        }else{
            mHost.onPositiveResult(result.getText());
            dismiss();
        }
        //qrCodeReaderView.startCamera();

    }
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }
    private static class BeepListener implements MediaPlayer.OnCompletionListener {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    }
    private void requestCameraPermission() {


        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Camera access is required for QR code scanner.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override public void onClick(View view) {
                    requestPermissions( new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
                }
            }).show();
        } else{
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Requesting camera permission. Required for QR code scanner.",
                    Snackbar.LENGTH_SHORT).show();
            requestPermissions(new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_REQUEST_CAMERA);
        }


    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Camera permission was granted. Now you can scan QR code", Snackbar.LENGTH_SHORT).show();
            initQRCodeReaderView();
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Camera permission request was denied, Can't able to start QR scan", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
}

